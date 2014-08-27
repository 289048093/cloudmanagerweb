/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.computeresource;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.compute.Compute;
import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;

/**
 * 资源service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("computeResourceService")
public class ComputeResourceService extends BaseService {

    /**
     * 资源DAO
     */
    @Resource
    private transient ComputeResourceDAO computeResourceDAO;

    /**
     * 机房DAO
     */
    @Resource
    private transient MachineRoomDAO machineRoomDAO;

    /**
     * 机架DAO
     */
    @Resource
    private transient MachineRackDAO machineRackDAO;

    /**
     * 网络DAO
     */
    @Resource
    private transient NetWorkDAO netWorkDAO;
    /**
     * 虚拟机DAO
     */
    @Resource
    private transient VirtualMachineDAO virtualMachineDAO;

    /**
     * 搜索资源
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        //结果集
        List<ComputeResourceVO> queryResult = new ArrayList<ComputeResourceVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        computeResourceDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext
                                        .getLongParam("qRoom"), cloudContext.getLongParam("qRack")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> resultSet = computeResourceDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLongParam("qRoom"), cloudContext.getLongParam("qRack"), cloudContext.getPageInfo());
            ComputeResourceVO computeResourceVO = null;
            for (Object[] result : resultSet) {
                computeResourceVO = new ComputeResourceVO();
                BeanUtils.copyProperties(result[0], computeResourceVO);
                computeResourceVO.setMachineRackName((String) result[1]);
                computeResourceVO.setMachineRoomName((String) result[2]);
                computeResourceVO.setVmNum(Integer.valueOf(result[3].toString()));
                queryResult.add(computeResourceVO);
            }
        }
        cloudContext.addParam("computeResources", queryResult);
        //获取机房
        List<MachineRoomVO> machineRooms = new ArrayList<MachineRoomVO>();
        List<MachineRoomEntity> machineRoomEntitys = machineRoomDAO.listOrderBy("addTime");
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity machineRoomEntity : machineRoomEntitys) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
            machineRooms.add(machineRoomVO);
        }
        cloudContext.addParam("machineRooms", machineRooms);
        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        if (cloudContext.getLongParam("qRoom") != null) {
            List<MachineRackEntity> machineRackEntitys = machineRoomDAO.get(cloudContext.getLongParam("qRoom"))
                            .getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
        }
        cloudContext.addParam("machineRacks", machineRacks);
    }

    /**
     * 新增
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insert(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        //查找机架
        MachineRackEntity machineRackEntity = machineRackDAO.load(cloudContext.getLongParam("rackID"));
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("选择的机架不存在！");
            return;
        }
        //验证
        ComputeResourceEntity computeResourceEntity = computeResourceDAO.getByNameAndRack(cloudContext.getVo()
                        .getName(), machineRackEntity.getId());
        if (computeResourceEntity != null) {
            cloudContext.addErrorMsg(String.format("此机架下面的【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        computeResourceEntity = computeResourceDAO.getByIp(cloudContext.getVo().getIp());
        if (computeResourceEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】下已经存在计算节点【%2$s】", cloudContext.getVo().getIp(),
                            computeResourceEntity.getName()));
            return;
        }
        computeResourceEntity = new ComputeResourceEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), computeResourceEntity);
        computeResourceEntity.setAddTime(new Date());
        computeResourceEntity
                        .setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                                        .getVo().getWarn4Rack());
        computeResourceEntity.setMachineRack(machineRackEntity);
        computeResourceEntity.setIdentityName(ProjectUtil.createComputeResourceIdentityName());

        //设置rrd文件路径
        computeResourceEntity.setRrdPath(ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + machineRackEntity.getMachineRoom().getIdentityName() + File.separator
                        + machineRackEntity.getIdentityName() + File.separator
                        + computeResourceEntity.getIdentityName());
        /*
         * 调用core
         */
        try {
            // 查看是否存在计算机，如果存在获取主机的CPU和内存
            Compute compute = Compute.createCompute(computeResourceEntity.getIp(), cloudContext.getVo().getUsername(),
                            cloudContext.getVo().getPassword());
            computeResourceEntity.setCpu(compute.getCpus());
            computeResourceEntity.setMemory(ProjectUtil.kByteToMega(compute.getTotalMem()));

            //创建rrd文件
            RRDUtil.createComputeResourceRRDS(computeResourceEntity.getRrdPath());
            //提示信息
            computeResourceDAO.insert(computeResourceEntity);
            cloudContext.addSuccessMsg("添加成功!");
            insertEventLog("新建计算节点：" + computeResourceEntity.getName(), cloudContext);

        } catch (VirtualizationException e) {
            //失败删除rrd文件
            RRDUtil.deleteComputeResourceRRDS(computeResourceEntity.getRrdPath());
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        }
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void update(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        ComputeResourceEntity computeResourceEntity = computeResourceDAO.get(cloudContext.getVo().getId());
        if (computeResourceEntity == null) {
            cloudContext.addErrorMsg("资源不存在");
            return;
        }
        MachineRoomEntity destRoomEntity = machineRoomDAO.get(cloudContext.getLongParam("roomID"));
        if (destRoomEntity == null) {
            cloudContext.addErrorMsg("机房不存在，请刷新后重试！");
            return;
        }
        MachineRackEntity destRackEntity = machineRackDAO.get(cloudContext.getLongParam("rackID"));
        if (destRackEntity == null) {
            cloudContext.addErrorMsg("机架不存在，请刷新后重试");
            return;
        }
        String destRRDPath = ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + destRoomEntity.getIdentityName() + File.separator + destRackEntity.getIdentityName()
                        + File.separator + computeResourceEntity.getIdentityName();
        //rrd文件迁移
        try {
            RRDUtil.moveRRDFile(computeResourceEntity.getRrdPath(), destRRDPath);
        } catch (Exception e) {
            cloudContext.addErrorMsg("rrd文件迁移错误！");
            LogUtil.error(e);
            return;
        }
        computeResourceEntity.setMachineRack(destRackEntity);
        computeResourceEntity.setRrdPath(destRRDPath);
        computeResourceEntity.setDesc(cloudContext.getVo().getDesc());
        computeResourceEntity
                        .setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                                        .getVo().getWarn4Rack());
        computeResourceDAO.update(computeResourceEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
        insertEventLog("修改计算节点：" + computeResourceEntity.getName(), cloudContext);
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void delete(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        Integer vmCount = virtualMachineDAO.queryVmCountInSpecifyComputeResource(cloudContext.getVo().getId());
        ComputeResourceEntity compute = computeResourceDAO.get(cloudContext.getVo().getId());
        if (compute == null) {
            cloudContext.addErrorMsg("该主机不存在，请刷新后重试！");
            return;
        }
        if (vmCount > 0) {
            cloudContext.addErrorMsg("该计算节点尚有虚拟机存在!");
            return;
        }
        ComputeResourcePoolEntity pool = compute.getComputeResourcePool();
        if (pool != null) {
            cloudContext.addErrorMsg(String.format("此计算节点存在资源池【%1$s】中，请先从池中删除", pool.getName()));
            return;
        }
        //调用core
        try {
            Compute.deleteCompute(compute.getIp());
        } catch (VirtualizationException e) {
            LogUtil.warn(e);
            e.printStackTrace();
            cloudContext.addErrorMsg(e.getMessage());
            return;
        }

        //删除rrd文件
        RRDUtil.deleteComputeResourceRRDS(compute.getRrdPath());
        //提示信息
        computeResourceDAO.deleteById(cloudContext.getVo().getId());
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog("删除计算节点:" + compute.getName(), cloudContext);

    }

    /**
     * 初始化新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        //获取机房
        List<MachineRoomVO> machineRooms = new ArrayList<MachineRoomVO>();
        List<MachineRoomEntity> machineRoomEntitys = machineRoomDAO.listOrderBy("addTime");
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity machineRoomEntity : machineRoomEntitys) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
            machineRooms.add(machineRoomVO);
        }
        cloudContext.addParam("machineRooms", machineRooms);
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            ComputeResourceEntity computeResourceEntity = computeResourceDAO.get(cloudContext.getVo().getId());
            if (computeResourceEntity == null) {
                cloudContext.addErrorMsg("资源不存在！");
                return;
            }
            ComputeResourceVO computeResourceVO = new ComputeResourceVO();
            BeanUtils.copyProperties(computeResourceEntity, computeResourceVO);
            cloudContext.addParam("dataVo", computeResourceVO);
            cloudContext.addParam("machineRackID", computeResourceEntity.getMachineRack().getId());
            cloudContext.addParam("machineRoomID", computeResourceEntity.getMachineRack().getMachineRoom().getId());
            //获取机架
            List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
            List<MachineRackEntity> machineRackEntitys = computeResourceEntity.getMachineRack().getMachineRoom()
                            .getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
            cloudContext.addParam("machineRacks", machineRacks);
        }
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRackByRoom(CloudContext<ComputeResourceVO> cloudContext) throws Exception {
        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        MachineRoomEntity machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomID"));
        if (machineRoomEntity != null) {
            List<MachineRackEntity> machineRackEntitys = machineRoomEntity.getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
        }
        cloudContext.addParam("machineRacks", machineRacks);
    }
}
