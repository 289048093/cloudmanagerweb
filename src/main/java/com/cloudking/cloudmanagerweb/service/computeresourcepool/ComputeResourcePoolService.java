/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.computeresourcepool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourcePoolDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;
import com.cloudking.cloudmanagerweb.vo.VirtualMachineVO;

/**
 * 资源池service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("computeResourcePoolService")
public class ComputeResourcePoolService extends BaseService {
    /**
     * 资源池DAO
     */
    @Resource
    private ComputeResourcePoolDAO computeResourcePoolDAO;
    /**
     * 网络DAO
     */
    @Resource
    private transient NetWorkDAO netWorkDAO;
    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;

    /**
     * 机柜DAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 资源DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * 虚机DAO
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;

    /**
     * 添加资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        //验证
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.getByName(cloudContext.getVo()
                        .getName());
        if (computeResourcePoolEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        computeResourcePoolEntity = new ComputeResourcePoolEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), computeResourcePoolEntity);
        computeResourcePoolEntity.setAddTime(new Date());

        computeResourcePoolDAO.insert(computeResourcePoolEntity);

        //插入与domain的关系
        updateDomainBidPool(computeResourcePoolEntity, cloudContext);
        //添加成功标志，弹出资源配置界面
        cloudContext.addParam("addSuccessCrpId", computeResourcePoolEntity.getId());
        //更新根域的资源使用情况
        cloudContext.addSuccessMsg("添加成功!");
        insertEventLog("新建资源池:" + computeResourcePoolEntity.getName(), cloudContext);
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        //结果集
        List<ComputeResourcePoolVO> queryResult = new ArrayList<ComputeResourcePoolVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        computeResourcePoolDAO.getQueryCount(cloudContext.getStringParam("qName")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objss = computeResourcePoolDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getPageInfo());
            ComputeResourcePoolEntity computeResourcePoolEntity = null;
            ComputeResourcePoolVO computeResourcePoolVO = null;
            Object[] crObjs = null;
            Integer vmNum = null;
            for (Object[] objs : objss) {
                computeResourcePoolVO = new ComputeResourcePoolVO();
                computeResourcePoolEntity = (ComputeResourcePoolEntity) objs[0];
                BeanUtils.copyProperties(computeResourcePoolEntity, computeResourcePoolVO);
                computeResourcePoolVO.setVmNum(Integer.valueOf(objs[1].toString()));
                queryCrpResource(computeResourcePoolVO);
                computeResourcePoolVO
                                .setTotalVirtualCpu((int) (computeResourcePoolVO.getTotalCpu() * computeResourcePoolVO
                                                .getCpuRate()));
                computeResourcePoolVO
                                .setTotalVirtualMemory((int) (computeResourcePoolVO.getTotalMemory() * computeResourcePoolVO
                                                .getMemoryRate()));
                queryResult.add(computeResourcePoolVO);
            }
        }
        cloudContext.addParam("computeResourcePools", queryResult);
    }

    /**
     * 查询资源池的资源情况
     * 
     * @param computeResourcePoolVO
     * @throws SQLException
     *             sql异常
     */
    private void queryCrpResource(ComputeResourcePoolVO computeResourcePoolVO) throws SQLException {
        Object[] crObjs = computeResourcePoolDAO.sumComputeResourceWithCpuAndMemByPoolId(computeResourcePoolVO.getId());
        computeResourcePoolVO.setComputeResourceNum(crObjs[0] == null ? 0 : Integer.valueOf(crObjs[0].toString()));
        computeResourcePoolVO.setTotalCpu(crObjs[1] == null ? 0 : Integer.valueOf(crObjs[1].toString()));
        computeResourcePoolVO.setAvailableCpu(crObjs[2] == null ? 0 : Integer.valueOf(crObjs[2].toString()));
        computeResourcePoolVO.setTotalMemory(crObjs[3] == null ? 0 : Integer.valueOf(crObjs[3].toString()));
        computeResourcePoolVO.setAvailableMemory(crObjs[4] == null ? 0 : Integer.valueOf(crObjs[4].toString()));
    }

    /**
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.get(cloudContext.getVo().getId());
        if (computeResourcePoolEntity.getComputeResources().size() > 0) {
            cloudContext.addErrorMsg(String.format("【%1$s】存在绑定资源，请先取消绑定", computeResourcePoolEntity.getName()));
            return;
        }
        computeResourcePoolDAO.clearDomainByPoolId(computeResourcePoolEntity.getId());
        computeResourcePoolDAO.delete(computeResourcePoolEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog("删除资源池:" + computeResourcePoolEntity.getName(), cloudContext);
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.get(cloudContext.getVo().getId());
        if (computeResourcePoolEntity == null) {
            cloudContext.addErrorMsg("资源池不存在！");
            return;
        }
        computeResourcePoolEntity.setDesc(cloudContext.getVo().getDesc());
        computeResourcePoolDAO.update(computeResourcePoolEntity);
        //插入与domain的关系
//        updateDomainBidPool(computeResourcePoolEntity, cloudContext);//域不修改
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
        insertEventLog("修改资源池:" + computeResourcePoolEntity.getName(), cloudContext);
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.get(cloudContext.getVo()
                            .getId());
            if (computeResourcePoolEntity == null) {
                cloudContext.addErrorMsg("资源池不存在！");
                return;
            }
            ComputeResourcePoolVO computeResourcePoolVO = new ComputeResourcePoolVO();
            BeanUtils.copyProperties(computeResourcePoolEntity, computeResourcePoolVO);
            cloudContext.addParam("dataVo", computeResourcePoolVO);
            List<DomainEntity> domainEntitys = computeResourcePoolEntity.getDomains();
            if (domainEntitys.size() > 0) {
                DomainVO domainVO = null;
                List<DomainVO> domainVOs = new ArrayList<DomainVO>();
                for (DomainEntity entity : domainEntitys) {
                    domainVO = new DomainVO();
                    BeanUtils.copyProperties(entity, domainVO);
                    domainVOs.add(domainVO);
                }
                cloudContext.addParam("selectedDomains", domainVOs);
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

        //初始化域列表
        List<DomainEntity> domainEntitys = domainDAO.list();
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity domainEntity : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            domainVOs.add(domainVO);
        }
        String domainsZtreeStr = ProjectUtil.generateDomainForZtree(domainVOs);
        cloudContext.addParam("domains", domainsZtreeStr);
    }

    /**
     * 查询资源。
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryResource(CloudContext<ComputeResourcePoolVO> cloudContext) throws SQLException {
        List<ComputeResourceVO> computeResourceVOs = new ArrayList<ComputeResourceVO>();
        List<Object[]> objses = computeResourceDAO.queryNoPage(cloudContext.getStringParam("qResourceByName"),
                        cloudContext.getLongParam("qResourceByRoom"), cloudContext.getLongParam("qResourceByRack"));
        ComputeResourceVO computeResourceVO = null;
        for (Object[] objs : objses) {
            ComputeResourceEntity computeResourceEntity = (ComputeResourceEntity) objs[0];
            computeResourceVO = new ComputeResourceVO();
            BeanUtils.copyProperties(computeResourceEntity, computeResourceVO);
            ComputeResourcePoolEntity computeResourcePoolEntity = (ComputeResourcePoolEntity) objs[1];
            if (computeResourcePoolEntity != null) {//已经配置的资源，在配置其他资源池时不显示
                if (computeResourcePoolEntity.getId().equals(cloudContext.getVo().getId())) {
                    computeResourceVO.setComputeResourcePoolId(computeResourcePoolEntity.getId());
                    computeResourceVO.setVmNum(virtualMachineDAO.getVmCountByComputeId(computeResourceEntity.getId()));
                } else {
                    continue;
                }
            }
            computeResourceVOs.add(computeResourceVO);
        }
        cloudContext.addParam("computeResources", computeResourceVOs);
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRackByRoom(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
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
        cloudContext.addParam("cascadeData", machineRacks);
    }

    /**
     * 查询指定计算节点池下的计算节点
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryComputeResourceByPoolId(CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        Long poolId = cloudContext.getLongParam("poolId");
        if (poolId == null) {
            cloudContext.addErrorMsg("计算节点池不存在");
            return;
        }
        //结果集
        List<ComputeResourceVO> queryResult = new ArrayList<ComputeResourceVO>();
        List<Object[]> resultSet = computeResourceDAO.queryComputeResourceByPoolId(poolId);
        ComputeResourceVO computeResourceVO = null;
        for (Object[] result : resultSet) {
            computeResourceVO = new ComputeResourceVO();
            BeanUtils.copyProperties(result[0], computeResourceVO);
            computeResourceVO.setMachineRackName((String) result[1]);
            computeResourceVO.setMachineRoomName((String) result[2]);
            computeResourceVO.setVmNum(Integer.valueOf(result[3].toString()));
            queryResult.add(computeResourceVO);
        }
        cloudContext.addParam("computeResources", queryResult);
    }

    /**
     * 查询指定计算节点下的虚拟机
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryVirtualMachineByComputeResourceId(CloudContext<ComputeResourcePoolVO> cloudContext)
                    throws Exception {
        Long resourceId = cloudContext.getLongParam("resourceId");
        if (resourceId == null) {
            cloudContext.addErrorMsg("计算节点不存在");
            return;
        }
        //结果集
        List<VirtualMachineVO> queryResult = new ArrayList<VirtualMachineVO>();
        List<Object> resultSet = virtualMachineDAO.queryVirtualMachineByComputeResourceId(resourceId);
        VirtualMachineVO virtualMachineVO = null;
        for (Object objs : resultSet) {
            Object[] obj = (Object[]) objs;
            virtualMachineVO = new VirtualMachineVO();
            virtualMachineVO.setId(Long.parseLong(obj[0].toString()));
            virtualMachineVO.setName(obj[1].toString());
            virtualMachineVO.setDesc(obj[2] == null ? "" : obj[2].toString());
            virtualMachineVO.setVmName(obj[3].toString());
            virtualMachineVO.setIp(obj[4] == null ? "-" : obj[4].toString());
            virtualMachineVO.setCreatedFlag(obj[5] == null ? "" : obj[5].toString());
            virtualMachineVO.setCreatedResultMsg(obj[6] == null ? "-" : obj[6].toString());
            if (obj[7] == null) {
                virtualMachineVO.setOperateFailFlag(false);
            } else {
                virtualMachineVO.setOperateFailFlag(Boolean.parseBoolean(obj[7].toString()));
            }
            virtualMachineVO.setAddTime((Date) obj[8]);
            virtualMachineVO.setComputeResourceIP(obj[9] == null ? "" : obj[9].toString());
            virtualMachineVO.setCreator(obj[10] == null ? "" : obj[10].toString());
            virtualMachineVO.setOwner(obj[11] == null ? "" : obj[11].toString());
            virtualMachineVO.setDueTime((Date) obj[12]);
            //获得虚拟机状态
//            virtualMachineVO.setStatus(Status.unknow.toString());
            queryResult.add(virtualMachineVO);
        }
        cloudContext.addParam("virtualMachines", queryResult);
    }

    /**
     * 更新域和池的关系
     * 
     * @param computeResourcePoolEntity
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void updateDomainBidPool(ComputeResourcePoolEntity computeResourcePoolEntity,
                    CloudContext<ComputeResourcePoolVO> cloudContext) throws Exception {
        String domainIdsStr = cloudContext.getStringParam("domainIDs");
        computeResourcePoolDAO.clearDomainByPoolId(computeResourcePoolEntity.getId());
        if (!StringUtil.isBlank(domainIdsStr)) {
            String[] domainIdStrs = domainIdsStr.split(",");
            Long domainId = null;
            for (String idStr : domainIdStrs) {
                domainId = Long.parseLong(idStr);
                computeResourcePoolDAO.insertPoolBidDomain(computeResourcePoolEntity.getId(), domainId);
            }
        }
    }

    /**
     * 更新根域的cpu和内存使用情况
     * 
     * @param cpu
     *            cpu增量
     * @param availableCpu
     * @param memory
     * @param availableMemory
     * @throws SQLException
     *             sql异常
     */
    private void updateRootDomainResource(Integer cpu, Integer availableCpu, Integer memory, Integer availableMemory)
                    throws SQLException {
        DomainEntity rootDomain = domainDAO.get(Constant.ROOT_DOMAIN_ID);
        //更新根域的资源使用情况
        rootDomain.setCpuTotalNum(rootDomain.getCpuTotalNum() == null ? 0 : rootDomain.getCpuTotalNum() + cpu);//添加时，主机资源总数即为可用的
        rootDomain.setCpuAvailableNum((int) (rootDomain.getCpuAvailableNum() == null ? 0 : rootDomain
                        .getCpuAvailableNum()
                        + availableCpu));
        rootDomain.setMemoryCapacity(rootDomain.getMemoryCapacity() == null ? 0 : rootDomain.getMemoryCapacity()
                        + memory);
        rootDomain.setMemoryAvailableCapacity((int) (rootDomain.getMemoryAvailableCapacity() == null ? 0 : rootDomain
                        .getMemoryAvailableCapacity()
                        + availableMemory));
        domainDAO.update(rootDomain);
    }

    /**
     * 初始化资源
     * 
     * @param cloudContext
     * @throws SQLException
     *             所有异常
     */
    public void initUpdateComputeResource(CloudContext<ComputeResourcePoolVO> cloudContext) throws SQLException {
        queryResource(cloudContext);
    }

    /**
     * 设置资源
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void updateSetComputeResource(CloudContext<ComputeResourcePoolVO> cloudContext) throws SQLException {
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.get(cloudContext.getVo().getId());
        if (computeResourcePoolEntity == null) {
            cloudContext.addErrorMsg("资源池不存在！");
            return;
        }
        //原来池的配置情况
        Integer cpu = 0;
        Integer availableCpu = 0;
        Integer memory = 0;
        Integer availableMemory = 0;
        String prevIdsStr = cloudContext.getStringParam("prevResourceIds");
        List<ComputeResourceEntity> prevComputeResources = new ArrayList<ComputeResourceEntity>();
        if (!prevIdsStr.equals("")) {
            String[] prevIds = prevIdsStr.split(",");
            //修改前的计算节点
            for (String idStr : prevIds) {
                ComputeResourceEntity compute = computeResourceDAO.get(Long.parseLong(idStr));
                if (compute == null) {
                    cloudContext.addErrorMsg("计算节点不存在，请刷新后重试");
                    return;
                }
                prevComputeResources.add(compute);
            }
        }
        //配置资源
        String[] resourceIds = (String[]) cloudContext.getObjectParam("resourceIds");
        if (resourceIds != null) {
            for (String resourceIdStr : resourceIds) {
                ComputeResourceEntity computeResourceEntity = computeResourceDAO.get(Long.parseLong(resourceIdStr));
                if (prevComputeResources.contains(computeResourceEntity)) {//如果是原来的，就先从以前的list移除，剩下的就是要删除的
                    prevComputeResources.remove(computeResourceEntity);
                } else {//不过不是原来的，即为新增的
                    computeResourceEntity.setComputeResourcePool(computeResourcePoolEntity);
                    computeResourceDAO.update(computeResourceEntity);
                    //如果是新添加进来的，则要重新计算超配率
                    computeResourceEntity
                                    .setCpuAvailable((int) (computeResourceEntity.getCpu() * computeResourcePoolEntity
                                                    .getCpuRate()));
                    computeResourceEntity
                                    .setMemoryAvailable((int) (computeResourceEntity.getMemory() * computeResourcePoolEntity
                                                    .getMemoryRate()));
                    cpu += computeResourceEntity.getCpu();
                    availableCpu += computeResourceEntity.getCpuAvailable();
                    memory += computeResourceEntity.getMemory();
                    availableMemory += computeResourceEntity.getMemoryAvailable();
                }
            }
        }
        //解除要删除的计算节点的关系
        StringBuilder computes = new StringBuilder();
        for (ComputeResourceEntity compute : prevComputeResources) {//剩下的即为新删除的
            Integer vmCount = virtualMachineDAO.getVmCountByComputeId(compute.getId());
            if (vmCount > 0) {
                computes.append(compute.getName()).append(",");
                continue;
            }
            compute.setComputeResourcePool(null);
            computeResourceDAO.update(compute);
            cpu -= compute.getCpu();
            availableCpu -= compute.getCpuAvailable();
            memory -= compute.getMemory();
            availableMemory -= compute.getMemoryAvailable();
        }
        //更新根域的总cpu和可用cpu，总内存和可用内存
        updateRootDomainResource((int) (cpu * computeResourcePoolEntity.getCpuRate()), availableCpu,
                        (int) (memory * computeResourcePoolEntity.getMemoryRate()), availableMemory);
        if (computes.length() > 0) {
            cloudContext.addWarnMsg(computes.replace(computes.length() - 1, computes.length(), " ").append("存在虚拟机!")
                            .toString());
        } else {
            cloudContext.addErrorMsg("设置成功");
        }
        insertEventLog("修改资源池:" + computeResourcePoolEntity.getName() + " 的计算节点", cloudContext);
    }

    /**
     * 从计算池中移除单个计算节点
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void updateSetComputeResourceFromPool(CloudContext<ComputeResourcePoolVO> cloudContext) throws SQLException {
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.get(cloudContext
                        .getLongParam("poolId"));
        if (computeResourcePoolEntity == null) {
            cloudContext.addErrorMsg("资源池不存在！");
            return;
        }
        ComputeResourceEntity computeResourcesEntity = computeResourceDAO.get(cloudContext.getLongParam("computeId"));
        if (computeResourcesEntity == null) {
            cloudContext.addErrorMsg("计算节点不存在！");
            return;
        }
        DomainEntity rootDomain = domainDAO.get(Constant.ROOT_DOMAIN_ID);
        if (rootDomain == null) {
            cloudContext.addErrorMsg("计算节点不存在！");
            return;
        }

        //判断节点是否存在虚拟机
        Integer vmCount = virtualMachineDAO.getVmCountByComputeId(computeResourcesEntity.getId());
        if (vmCount > 0) {
            cloudContext.addErrorMsg("节点还存在" + vmCount + "台虚拟机");
            return;
        }

        //更新根域的总cpu和可用cpu，总内存和可用内存
        int cpu = (int) (computeResourcesEntity.getCpu() * computeResourcePoolEntity.getCpuRate());
        int availableCpu = computeResourcesEntity.getCpuAvailable();
        int memory = (int) (computeResourcesEntity.getMemory() * computeResourcePoolEntity.getMemoryRate());
        int availableMemory = computeResourcesEntity.getMemoryAvailable();

        rootDomain.setCpuTotalNum(rootDomain.getCpuTotalNum() == null ? 0 : rootDomain.getCpuTotalNum() - cpu);
        rootDomain.setCpuAvailableNum((int) (rootDomain.getCpuAvailableNum() == null ? 0 : rootDomain
                        .getCpuAvailableNum()
                        - availableCpu));
        rootDomain.setMemoryCapacity(rootDomain.getMemoryCapacity() == null ? 0 : rootDomain.getMemoryCapacity()
                        - memory);
        rootDomain.setMemoryAvailableCapacity((int) (rootDomain.getMemoryAvailableCapacity() == null ? 0 : rootDomain
                        .getMemoryAvailableCapacity()
                        - availableMemory));
        domainDAO.update(rootDomain);

        //解除计算节点与计算池的关系
        computeResourcesEntity.setComputeResourcePool(null);
        computeResourceDAO.update(computeResourcesEntity);

        //返回资源池更新页面数据
        BeanUtils.copyProperties(computeResourcePoolEntity, cloudContext.getVo());
        queryCrpResource(cloudContext.getVo());

        cloudContext.addSuccessMsg("操作成功");
    }
}
