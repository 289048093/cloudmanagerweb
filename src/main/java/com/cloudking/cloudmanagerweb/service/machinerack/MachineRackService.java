/**
 * 
 */
package com.cloudking.cloudmanagerweb.service.machinerack;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;

/**
 * 机架service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("machineRackService")
public class MachineRackService extends BaseService {

    /**
     * 机架DAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;

    /**
     * 存储资源DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;
    /**
     * 计算资源DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;

    /**
     * 搜索机架
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<MachineRackVO> cloudContext) throws Exception {
        //结果集
        List<MachineRackVO> queryResult = new ArrayList<MachineRackVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        machineRackDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext
                                        .getLongParam("qRoom")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objss = machineRackDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLongParam("qRoom"), cloudContext.getPageInfo());
            MachineRackVO machineRackVO = null;
            MachineRackEntity machineRackEntity = null;
            for (Object[] objs : objss) {
                machineRackEntity = (MachineRackEntity) objs[0];
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRackVO.setRoomName(objs[1].toString());
                queryResult.add(machineRackVO);
            }
        }
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
        cloudContext.addParam("machineRacks", queryResult);
    }

    /**
     * 新增
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insert(CloudContext<MachineRackVO> cloudContext) throws Exception {
        //查找机房
        MachineRoomEntity machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomID"));
        if (machineRoomEntity == null) {
            cloudContext.addErrorMsg("选择的机房不存在！");
            return;
        }
        //验证
        MachineRackEntity machineRackEntity = machineRackDAO.getByNameAndRoom(cloudContext.getVo().getName(),
                        machineRoomEntity.getId());
        if (machineRackEntity != null) {
            cloudContext.addErrorMsg(String.format("此机房下面的【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        machineRackEntity = new MachineRackEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), machineRackEntity);
        machineRackEntity.setAddTime(new Date());
        machineRackEntity.setMachineRoom(machineRoomEntity);
        machineRackEntity.setIdentityName(ProjectUtil.createMachineRackIdentityName());

        //创建文件夹
        File dir = new File(new File(ProjectUtil.getRRDDir(), machineRoomEntity.getIdentityName()), machineRackEntity
                        .getIdentityName());
        if (dir.exists()) {
            //存在，并且不是目录，就删除，再添加
            if (!dir.isDirectory()) {
                if (dir.delete()) {
                    dir.mkdirs();
                }
            }
        } else {
            dir.mkdirs();
        }

        //
        machineRackDAO.insert(machineRackEntity);
        //提示信息

        cloudContext.addSuccessMsg("添加成功!");
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void update(CloudContext<MachineRackVO> cloudContext) throws Exception {
        //查找机房
        MachineRoomEntity machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomID"));
        if (machineRoomEntity == null) {
            cloudContext.addErrorMsg("选择的机房不存在！");
            return;
        }
        MachineRackEntity machineRackEntity = machineRackDAO.get(cloudContext.getVo().getId());
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("机架不存在");
            return;
        }
        MachineRoomEntity srcRoomEntity = machineRackEntity.getMachineRoom();
        //移动rrd文件
        String srcRackDir = ProjectUtil.getRRDDir() + File.separator + srcRoomEntity.getIdentityName() + File.separator
                        + machineRackEntity.getIdentityName();
        String destRackDir = ProjectUtil.getRRDDir() + File.separator + machineRoomEntity.getIdentityName()
                        + File.separator + machineRackEntity.getIdentityName();
        //移动文件
        try {
            RRDUtil.moveRRDDir(srcRackDir, destRackDir);
        } catch (Exception e) {
            cloudContext.addErrorMsg("rrd文件迁移错误");
            LogUtil.error(e);
            return;
        }
        machineRackEntity.setDesc(cloudContext.getVo().getDesc());
        machineRackEntity.setMachineRoom(machineRoomEntity);
        machineRackEntity.setWarn4Room(cloudContext.getVo().getWarn4Room());
        machineRackDAO.update(machineRackEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void delete(CloudContext<MachineRackVO> cloudContext) throws Exception {
        MachineRackEntity machineRackEntity = machineRackDAO.get(cloudContext.getVo().getId());
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("删除失败，机架不存在！");
            return;
        }
        if (computeResourceDAO.getCountByMachineRack(machineRackEntity.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，请先删除下面已存在的计算节点资源！");
            return;
        }
        if (storageResourceDAO.getCountByMachineRack(machineRackEntity.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，请先删除下面已存在的存储节点资源！");
            return;
        }
        //删除文件
        File dir = new File(new File(ProjectUtil.getRRDDir(), machineRackEntity.getMachineRoom().getIdentityName()),
                        machineRackEntity.getIdentityName());
        if (dir.exists()) {
            dir.delete();
        }
        machineRackDAO.delete(machineRackEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 初始化新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<MachineRackVO> cloudContext) throws Exception {
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
            MachineRackEntity machineRackEntity = machineRackDAO.get(cloudContext.getVo().getId());
            if (machineRackEntity == null) {
                cloudContext.addErrorMsg("机架不存在！");
                return;
            }
            MachineRackVO machineRackVO = new MachineRackVO();
            BeanUtils.copyProperties(machineRackEntity, machineRackVO);
            cloudContext.addParam("dataVo", machineRackVO);
            cloudContext.addParam("machineRoomID", machineRackEntity.getMachineRoom().getId());
        }

    }
}
