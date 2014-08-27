/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.virtualmachine;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.storage.Volume;
import com.cloudking.cloudmanager.core.virtualization.Status;
import com.cloudking.cloudmanager.core.virtualization.VirtualMachine;
import com.cloudking.cloudmanager.core.virtualization.VmUtils;
import com.cloudking.cloudmanager.core.virtualization.VmUtils.VmStatusInfo;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.BackupStorageDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourcePoolDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.FaultTolerantDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserBinVirtualMachineOrderDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VMBackupDAO;
import com.cloudking.cloudmanagerweb.dao.VMSnapshotDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.VolumnDAO;
import com.cloudking.cloudmanagerweb.entity.BackupStorageEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.FaultTolerantEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.NetWorkEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.entity.VMBackupEntity;
import com.cloudking.cloudmanagerweb.entity.VMSnapshotEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.util.VMUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;
import com.cloudking.cloudmanagerweb.vo.MachineTypeVO;
import com.cloudking.cloudmanagerweb.vo.NetWorkVO;
import com.cloudking.cloudmanagerweb.vo.TemplateVO;
import com.cloudking.cloudmanagerweb.vo.VMBackupVO;
import com.cloudking.cloudmanagerweb.vo.VMSnapshotVO;
import com.cloudking.cloudmanagerweb.vo.VirtualMachineVO;
import com.cloudking.cloudmanagerweb.vo.VolumnVO;

/**
 * 虚拟机虚拟机service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("virtualMachineService")
public class VirtualMachineService extends BaseService {
    /**
     * 虚拟机虚拟机DAO
     */
    @Resource
    private transient VirtualMachineDAO virtualMachineDAO;
    /**
     * 配置DAO
     */
    @Resource
    private MachineTypeDAO machineTypeDAO;
    /**
     * 模板DAO
     */
    @Resource
    private TemplateDAO templateDAO;
    /**
     * User DAO
     */
    @Resource
    private UserDAO userDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * 网络DAO
     */
    @Resource
    private NetWorkDAO netWorkDAO;

    /**
     * 计算节点DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;

    /**
     * 计算节点池DAO
     */
    @Resource
    private ComputeResourcePoolDAO computeResourcePoolDAO;
    /**
     * 存储DAO
     */
    @Resource
    private VolumnDAO volumnDAO;
    /**
     * 存储资源DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;
    /**
     * 用户虚拟机关联DAO
     */
    @Resource
    private PortalUserBinVirtualMachineOrderDAO portalUserBinVirtualMachineOrderDAO;
    /**
     * 快照备份DAO
     */
    @Resource
    private VMSnapshotDAO vMSnapshotDAO;
    /**
     * 虚机备份DAO
     */
    @Resource
    private VMBackupDAO vMBackupDAO;

    /**
     * 机房dao
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;
    /**
     * rackDAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 容错
     */
    @Resource
    private FaultTolerantDAO faultTolerantDAO;
    /**
     * 备份存储
     */
    @Resource
    private BackupStorageDAO backupStorageDAO;

    /**
     * 新建虚拟机通用类 - 用以同步工单
     * 
     * @return object数组 1：true/false 2:错误信息 3:虚拟机对象(门户对象需要使用以建立关系)
     * @throws Exception
     *             e
     */
    public Object[] insertVirtualMachine(DomainEntity domainEntity, UserEntity userEntity,
                    TemplateEntity templateEntity, MachineTypeEntity machineTypeEntity, NetWorkEntity netWorkEntity,
                    String virtualMachineName, String virtualMachineDesc, boolean customComputeResourceFlag,
                    Long computeResourceID, String virtualMachineBackupTimeMark, Map<String, String> emailInfoMap)
                    throws Exception {
        Object[] objects = new Object[] { false, "", null };
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.getByNameAndDomain(virtualMachineName,
                        domainEntity.getId());
        if (virtualMachineEntity != null) {
            objects[1] = String.format("【%1$s】域下面的【%2$s】已经存在", domainEntity.getName(), virtualMachineName);
            return objects;
        }

        //域资源验证
        if (domainEntity.getCpuAvailableNum() < machineTypeEntity.getCpu()) {
            objects[1] = "添加失败，当前域可用CPU为：" + domainEntity.getCpuAvailableNum();
            return objects;
        }
        if (domainEntity.getMemoryAvailableCapacity() < machineTypeEntity.getMemory()) {
            objects[1] = "添加失败，当前域可用内存为：" + domainEntity.getMemoryAvailableCapacity();
            return objects;
        }
        if (domainEntity.getAvailableStorageCapacity() < machineTypeEntity.getDisk()) {
            objects[1] = "添加失败，当前域可用存储为：" + domainEntity.getAvailableStorageCapacity();
            return objects;
        }
        //查找资源
        Object[] computeObjects = matchComputeResource(machineTypeEntity, domainEntity, customComputeResourceFlag,
                        computeResourceID);
        ComputeResourceEntity computeResourceEntity = null;
        try {
            if (computeObjects[0] != null) {
                computeResourceEntity = (ComputeResourceEntity) computeObjects[0];
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }

        if (computeResourceEntity == null) {
            objects[1] = computeObjects[1].toString();
            return objects;
        }

        //查找存储
        StorageResourceEntity storageResourceEntity = storageResourceDAO.getByCapacity(machineTypeEntity.getDisk()
                        .longValue());
        if (storageResourceEntity == null) {
            objects[1] = "没有符合此配置的存储!";
            return objects;
        }
        Date currentTime = new Date();

        //装载虚拟机参数
        virtualMachineEntity = new VirtualMachineEntity();
        virtualMachineEntity.setDomain(domainEntity);
        virtualMachineEntity.setAddTime(currentTime);
        virtualMachineEntity.setMachineType(machineTypeEntity);
        virtualMachineEntity.setTemplate(templateEntity);
        virtualMachineEntity.setComputeResource(computeResourceEntity);
        virtualMachineEntity.setNetWork(netWorkEntity);
        String vmName = ProjectUtil.createVmName();
        virtualMachineEntity.setVmName(vmName);
        virtualMachineEntity.setCreateUser(userEntity);
        virtualMachineEntity.setCreatedFlag(Constant.VM_CREATING);
        virtualMachineEntity.setCreatedResultMsg("创建中");
        virtualMachineEntity.setDesc(virtualMachineDesc);
        virtualMachineEntity.setName(virtualMachineName);
        virtualMachineEntity.setBackupTimeMark(virtualMachineBackupTimeMark);

        Long vmCapacityForKb = ProjectUtil.gigaToKByte(machineTypeEntity.getDisk().longValue());
        //装载卷参数
        String volumnName = ProjectUtil.createVolumnName();
        VolumnEntity volumnEntity = new VolumnEntity();
        volumnEntity.setName(volumnName);
        volumnEntity.setAddTime(currentTime);
        volumnEntity.setVirtualMachine(virtualMachineEntity);
        volumnEntity.setStorageResource(storageResourceEntity);
        volumnEntity.setSize(vmCapacityForKb);
        volumnEntity.setImageVolumnFlag(Constant.IMAGE_VOLUMN_FALG_TRUE);

        //修改存储大小
        storageResourceEntity.setAvailableCapacity(storageResourceEntity.getAvailableCapacity()
                        - volumnEntity.getSize());

        //更新域的资源可用情况
        domainEntity.setCpuAvailableNum(domainEntity.getCpuAvailableNum() - machineTypeEntity.getCpu());
        domainEntity.setMemoryAvailableCapacity(domainEntity.getMemoryAvailableCapacity()
                        - machineTypeEntity.getMemory());
        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity() - vmCapacityForKb);

        //更新计算节点资源
        computeResourceEntity.setCpuAvailable(computeResourceEntity.getCpuAvailable() - machineTypeEntity.getCpu());
        computeResourceEntity.setMemoryAvailable(computeResourceEntity.getMemoryAvailable()
                        - machineTypeEntity.getMemory());

        objects[0] = true;
        objects[1] = "添加成功!";
        objects[2] = virtualMachineEntity;

        // 插入虚拟机
        virtualMachineDAO.insert(virtualMachineEntity);

        //插入容错记录
        FaultTolerantEntity ftEntity = new FaultTolerantEntity();
        ftEntity.setRefid(virtualMachineEntity.getId());
        ftEntity.setType(Constant.VM_FAULT_TOLERANT);
        faultTolerantDAO.insert(ftEntity);

        //调用core
        String imgFilePath = ProjectUtil.getTemplateDir() + File.separator + templateEntity.getDomain().getCode()
                        + File.separator + templateEntity.getFileName();
        try {
            Volume volume = Volume
                            .createVolume(volumnName, storageResourceEntity.getPoolName(), volumnEntity.getSize());
            VMUtil.createVirtualMachine(virtualMachineEntity.getId(), ftEntity.getId(), virtualMachineEntity
                            .getVmName(), machineTypeEntity.getCpu(), ProjectUtil.megaToKByte(machineTypeEntity
                            .getMemory().longValue()), volume, imgFilePath, netWorkEntity.getRealname(),
                            virtualMachineEntity.getComputeResource().getIp(), emailInfoMap);
            domainDAO.update(domainEntity);
            storageResourceDAO.update(storageResourceEntity);
            computeResourceDAO.update(computeResourceEntity);
            volumnDAO.insert(volumnEntity);
        } catch (VirtualizationException e) {
            objects[0] = false;
            LogUtil.warn(e);
            objects[1] = e.getMessage();
            virtualMachineDAO.delete(virtualMachineEntity);
        }
        return objects;
    }

    /**
     * 添加虚拟机
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<VirtualMachineVO> cloudContext) throws Exception {

        DomainEntity domainEntity = domainDAO.findByCode(cloudContext.getVo().getDomainCode());
        if (domainEntity == null) {
            cloudContext.addErrorMsg("添加失败，当前域不存在，请刷新后再试！");
            return;
        }

        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("用户未登录");
            return;
        }
        TemplateEntity templateEntity = templateDAO.load(cloudContext.getLongParam("templateID"));
        if (templateEntity == null) {
            cloudContext.addErrorMsg("添加失败，模版不存在！");
            return;
        }
        MachineTypeEntity machineTypeEntity = machineTypeDAO.load(cloudContext.getLongParam("machineTypeID"));
        if (machineTypeEntity == null) {
            cloudContext.addErrorMsg("添加失败，配置不存在！");
            return;
        }
        //查找网络
        NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getLongParam("netWork"));
        if (netWorkEntity == null) {
            cloudContext.addErrorMsg("没有相应网络!");
            return;
        }
        //查找资源池
        Integer poolCount = computeResourcePoolDAO.getPoolCountByDomainId(domainEntity.getId());
        if (poolCount == 0) {
            cloudContext.addErrorMsg(String.format("域【%1$s】没有分配资源池", domainEntity.getName()));
            return;
        }
        //查找计算节点
        Integer computeCount = computeResourceDAO.getComputeCountByDomainId(domainEntity.getId());
        if (computeCount == 0) {
            cloudContext.addErrorMsg(String.format("域【%1$s】的资源池没有分配计算节点", domainEntity.getName()));
            return;
        }
        String virtualMahcineName = cloudContext.getVo().getName();
        String virtualMachineDesc = cloudContext.getVo().getDesc();
        String virtualMachineBackupTimeMark = cloudContext.getVo().getBackupTimeMark();
        boolean customComputeResourceFlag = cloudContext.getBooleanParam("customComputeResourceFlag");
        Long computeResourceID = cloudContext.getLongParam("computeResourceID");
        Object[] result = insertVirtualMachine(domainEntity, userEntity, templateEntity, machineTypeEntity,
                        netWorkEntity, virtualMahcineName, virtualMachineDesc, customComputeResourceFlag,
                        computeResourceID, virtualMachineBackupTimeMark, null);
        if (Boolean.parseBoolean(result[0].toString())) {
            cloudContext.addSuccessMsg(result[1].toString());
            insertEventLog(String.format("添加虚拟机:【%1$s】", virtualMahcineName), domainEntity.getId(), cloudContext);
        } else {
            cloudContext.addErrorMsg(result[1].toString());
        }
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        cloudContext.getPageInfo().setEachPageData(10);
        Long loginedUserId = cloudContext.getLoginedUser().getId();
        //结果集
        List<VirtualMachineVO> queryResult = new ArrayList<VirtualMachineVO>();
        //总数据数
        cloudContext.getPageInfo()
                        .setDataCount(
                                        virtualMachineDAO.getQueryCount(cloudContext.getStringParam("qName"),
                                                        cloudContext.getLongParam("qRoom"), cloudContext
                                                                        .getLongParam("qRack"), cloudContext
                                                                        .getLongParam("qCompute"), cloudContext
                                                                        .getLongParam("qDomain"), loginedUserId));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object> tmpResult = virtualMachineDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLongParam("qRoom"), cloudContext.getLongParam("qRack"), cloudContext
                            .getLongParam("qCompute"), cloudContext.getLongParam("qDomain"), loginedUserId,
                            cloudContext.getPageInfo());
            VirtualMachineVO virtualMachineVO = null;
            for (Object objs : tmpResult) {
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
                virtualMachineVO.setDomainName(obj[13] == null ? "" : obj[13].toString());
                //获得虚拟机状态
                virtualMachineVO.setStatus(Status.unknow.toString());
                queryResult.add(virtualMachineVO);
            }
        }
        cloudContext.addParam("virtualMachines", queryResult);
        //查询机房，供虚机筛选用
        List<MachineRoomEntity> rooms = machineRoomDAO.list();
        List<MachineRoomVO> machineRoomVOs = new ArrayList<MachineRoomVO>();
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity room : rooms) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(room, machineRoomVO);
            machineRoomVOs.add(machineRoomVO);
        }
        cloudContext.addParam("machineRooms", machineRoomVOs);
        //查询域列表
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOs);
    }

    /**
     * 查询当前域拥有的资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryResourcePoolByCurrentDomain(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long domainId = cloudContext.getLongParam("domainId");
        List<ComputeResourcePoolEntity> computeResourcePools = computeResourcePoolDAO
                        .queryResourcePoolByCurrentDomain(domainId);
        List<ComputeResourcePoolVO> computeResourcePoolVOs = new ArrayList<ComputeResourcePoolVO>();
        ComputeResourcePoolVO computeResoucePoolVO = null;
        for (ComputeResourcePoolEntity computeResourcePoolEnity : computeResourcePools) {
            computeResoucePoolVO = new ComputeResourcePoolVO();
            BeanUtils.copyProperties(computeResourcePoolEnity, computeResoucePoolVO);
            computeResourcePoolVOs.add(computeResoucePoolVO);
        }
        cloudContext.addParam("computeResourcePools", computeResourcePoolVOs);
    }

    /**
     * 查询虚拟机卷
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryVolumnByVmId(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long vmId = cloudContext.getLongParam("vmId");
        if (vmId == null) {
            cloudContext.addErrorMsg("虚拟机不存在");
            return;
        }
        List<VolumnEntity> volumns = volumnDAO.getVolumnsByVmId(vmId);
        List<VolumnVO> volumnVOs = new ArrayList<VolumnVO>();
        VolumnVO volumnVO = null;
        for (VolumnEntity volumnEntity : volumns) {
            volumnVO = new VolumnVO();
            BeanUtils.copyProperties(volumnEntity, volumnVO);
            volumnVO.setSize(ProjectUtil.kByteToGiga(volumnEntity.getSize()).longValue());
            volumnVOs.add(volumnVO);
        }
        cloudContext.addParam("volumns", volumnVOs);
    }

    /**
     * 异步查询虚拟机状态，并返回相应状态
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryVirtualMachineStatusByVmId(CloudContext<VirtualMachineVO> cloudContext) throws Exception {

        String vmIds = cloudContext.getStringParam("vmIds");
        if (StringUtil.isBlank(vmIds)) {
            cloudContext.addErrorMsg("没有虚拟机");
            return;
        }

        List<Object[]> objects = virtualMachineDAO.queryVmInfoAndHostIpByVMIDsOrderByCRIP(vmIds);
        if (objects == null) {
            cloudContext.addErrorMsg("没有虚拟机");
            return;
        }

        VirtualMachineEntity virtualMachine = null;
        List<VirtualMachineVO> machineVOs = new ArrayList<VirtualMachineVO>();
        VirtualMachineVO virtualMachineVO = null;
        String hostIp = "";
        if (objects.isEmpty()) {
            cloudContext.addParam("result", machineVOs);
            return;
        }
        hostIp = ((Object[]) objects.get(0))[0].toString();
        //获取状态
        List<String> virtualMachineNames = new ArrayList<String>();
        Map<String, VirtualMachineVO> vmMap = new HashMap<String, VirtualMachineVO>();
        for (Object[] objectArr : objects) {
            //检查主机IP是否不同
            if (!hostIp.equals(objectArr[0].toString())) {
                loadSingleCRStatus(hostIp, virtualMachineNames, vmMap);
                hostIp = objectArr[0].toString();
            }

            //加入虚拟机列表
            virtualMachine = (VirtualMachineEntity) objectArr[1];

            virtualMachineVO = new VirtualMachineVO();
            BeanUtils.copyProperties(virtualMachine, virtualMachineVO);
            virtualMachineVO.setCreatedResultMsg(virtualMachine.getCreatedResultMsg().replaceAll("\"", " ").replaceAll(
                            "'", " "));
            if (virtualMachine.getOperateFailFlag() != null) {
                virtualMachineVO.setOperateFailFlag(virtualMachine.getOperateFailFlag());
            } else {
                virtualMachineVO.setOperateFailFlag(false);
            }
            virtualMachineVO.setComputeResourceIP(hostIp);
            virtualMachineVO.setStatus(Status.unknow.toString());
            machineVOs.add(virtualMachineVO);
            virtualMachineNames.add(virtualMachineVO.getVmName());
            vmMap.put(virtualMachineVO.getVmName(), virtualMachineVO);
        }
        //最后一个计算节点
        loadSingleCRStatus(hostIp, virtualMachineNames, vmMap);
        cloudContext.addParam("result", machineVOs);
    }

    /**
     * 获取单个计算节点上虚机的状态
     * 
     * @param hostIp
     * @param virtualMachineNames
     * @param vmMap
     */
    private void loadSingleCRStatus(String hostIp, List<String> virtualMachineNames, Map<String, VirtualMachineVO> vmMap) {
        if (virtualMachineNames.isEmpty()) {
            return;
        }
        VirtualMachineVO virtualMachineVO = null;
        //调用core
        List<VmStatusInfo> vmStatusInfo = VmUtils.listVmStatus(virtualMachineNames, hostIp);
        for (VmStatusInfo statusInfo : vmStatusInfo) {
            virtualMachineVO = vmMap.get(statusInfo.getVmName());
            virtualMachineVO.setStatus(statusInfo.getStatus().toString());
            virtualMachineVO.setPort(virtualMachineVO.getStatus().equals(Status.running.toString()) ? VmUtils
                            .getvncPort(virtualMachineVO.getVmName(), virtualMachineVO.getComputeResourceIP()) : null);
        }
        //如果ip不同了，则清空，重新加入
        virtualMachineNames.clear();
        vmMap.clear();
    }

    /**
     * 删除卷
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void deleteVolumn(CloudContext<VirtualMachineVO> cloudContext) throws Exception {

        Long volumnId = cloudContext.getLongParam("volumnId");
        VolumnEntity volumnEntity = volumnDAO.get(volumnId);

        if (volumnEntity == null) {
            cloudContext.addErrorMsg("卷不存在");
            return;
        }
        VirtualMachineEntity virtualMachine = volumnEntity.getVirtualMachine();
        if (virtualMachine == null) {
            cloudContext.addErrorMsg("卷所在虚拟机不存在");
            return;
        }
        DomainEntity domainEntity = virtualMachine.getDomain();
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }

        if (Constant.IMAGE_VOLUMN_FALG_TRUE.equals(volumnEntity.getImageVolumnFlag())) {
            cloudContext.addErrorMsg("映像卷不能删除");
            return;
        }

        //查询状态
        Status status = getVirtualMachineStatus(virtualMachine, cloudContext);
        if (status != Status.shutoff) {
            cloudContext.addErrorMsg("只有关机状态的虚拟机才能删除卷.");
            return;
        }

        //调用core删除真实卷
        boolean coreOpFlag = false;
        try {
            VirtualMachine virtualMachineCore = VirtualMachine.getVM(virtualMachine.getVmName(), virtualMachine
                            .getComputeResource().getIp());
            if (virtualMachineCore != null) {
                Volume volume = Volume.getVolume(volumnEntity.getName(), volumnEntity.getStorageResource()
                                .getPoolName());
                if (volume != null) {
                    virtualMachineCore.detachVolume(volume);
                    coreOpFlag = true;
                }
            }
        } catch (VirtualizationException e) {
            LogUtil.warn(e);
            cloudContext.addErrorMsg(e.getMessage());
        }
        if (!coreOpFlag) {
            return;
        }

        //修改存储可用大小
        StorageResourceEntity storageResourceEntity = volumnEntity.getStorageResource();
        storageResourceEntity.setAvailableCapacity(storageResourceEntity.getAvailableCapacity()
                        + volumnEntity.getSize());

        //更新当前域下剩余空间大小
        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity() + volumnEntity.getSize());
        storageResourceDAO.update(storageResourceEntity);
        virtualMachineDAO.update(virtualMachine);
        domainDAO.update(domainEntity);
        volumnDAO.delete(volumnEntity);
        cloudContext.addSuccessMsg("卷删除成功！");
        insertEventLog(String.format("虚拟机【%1$s】的删除卷,大小为：【%2$s】G", virtualMachine.getName(), ProjectUtil
                        .kByteToGiga(volumnEntity.getSize())), domainEntity.getId(), cloudContext);
    }

    /**
     * 添加卷
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insertVolumn(CloudContext<VirtualMachineVO> cloudContext) throws Exception {

        Long volumnSize = cloudContext.getLongParam("volumnSize");
        Long vmId = cloudContext.getLongParam("vmId");
        String volumnName = ProjectUtil.createVolumnName();

        VirtualMachineEntity virtualMachine = virtualMachineDAO.get(vmId);
        if (virtualMachine == null) {
            cloudContext.addErrorMsg("卷所在虚拟机不存在");
            return;
        }

        DomainEntity domainEntity = virtualMachine.getDomain();
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }

        //查询状态
        Status status = getVirtualMachineStatus(virtualMachine, cloudContext);
        if (status != Status.shutoff) {
            cloudContext.addErrorMsg("只有关机状态的虚拟机才能添加卷.");
            return;
        }
        StorageResourceEntity storageResourceEntity = storageResourceDAO.getMaxDiskStorageByDomainId(domainEntity
                        .getId());

        Long capacity = ProjectUtil.kByteToGiga(
                        Math.min(storageResourceEntity.getAvailableCapacity(), domainEntity
                                        .getAvailableStorageCapacity())).longValue();
        //域资源验证
        if (capacity < volumnSize) {
            cloudContext.addErrorMsg("添加失败，当前域添加的最大存储为：" + capacity + "G");
            return;
        }

        storageResourceEntity = storageResourceDAO.getByCapacity(ProjectUtil.gigaToKByte(volumnSize));
        if (storageResourceEntity == null) {
            cloudContext.addErrorMsg("没有找到可用的存储： " + volumnSize);
            return;
        }

        //调用core创建真实volumn
        boolean coreOpFlag = false;
        try {
            VirtualMachine virtualMachineCore = VirtualMachine.getVM(virtualMachine.getVmName(), virtualMachine
                            .getComputeResource().getIp());
            if (virtualMachineCore != null) {
                Volume volume = Volume.createVolume(volumnName, storageResourceEntity.getPoolName(), ProjectUtil
                                .gigaToKByte(volumnSize.longValue()));
                if (volume != null) {
                    virtualMachineCore.attachVolume(volume);
                    coreOpFlag = true;
                }
            }
        } catch (VirtualizationException e) {
            //删除卷
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
        }
        if (!coreOpFlag) {
            return;
        }

        //初始化新volumn
        VolumnEntity volumnEntity = new VolumnEntity();
        volumnEntity.setName(volumnName);
        volumnEntity.setAddTime(new Date());
        volumnEntity.setVirtualMachine(virtualMachine);
        volumnEntity.setStorageResource(storageResourceEntity);
        volumnEntity.setSize(volumnSize);
        volumnEntity.setImageVolumnFlag(Constant.IMAGE_VOLUMN_FALG_FALSE);

        //更新域存储空间大小
        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity() - volumnSize);
        //更新存储容量
        storageResourceEntity.setAvailableCapacity(storageResourceEntity.getAvailableCapacity() - volumnSize);
        //更新虚拟机卷
        virtualMachine.getVolumns().add(volumnEntity);
        volumnDAO.insert(volumnEntity);
        virtualMachineDAO.update(virtualMachine);
        domainDAO.update(domainEntity);
        storageResourceDAO.update(storageResourceEntity);
        cloudContext.addSuccessMsg("卷添加成功！");
        insertEventLog(String.format("虚拟机【%1$s】的添加卷,大小为：【%2$s】", virtualMachine.getName(), volumnEntity.getSize()),
                        domainEntity.getId(), cloudContext);
    }

    /**
     * 查询指定资源池下的计算节点，并要求cpu内存参数大于指定值
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryResourceByPoolAndCpuMemory(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long poolId = cloudContext.getLongParam("poolId");
        Integer cpu = cloudContext.getIntegerParam("cpu");
        Integer memory = cloudContext.getIntegerParam("memory");

        List<ComputeResourceEntity> computeResources = computeResourceDAO.queryResourceByPoolAndCpuMemory(poolId, cpu,
                        memory);
        List<ComputeResourceVO> computeResourceVOs = new ArrayList<ComputeResourceVO>();
        ComputeResourceVO computeResouceVO = null;
        for (ComputeResourceEntity computeResourceEnity : computeResources) {
            computeResouceVO = new ComputeResourceVO();
            BeanUtils.copyProperties(computeResourceEnity, computeResouceVO);
            computeResourceVOs.add(computeResouceVO);
        }
        cloudContext.addParam("computeResources", computeResourceVOs);
    }

    /**
     * 删除虚拟机
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }

        DomainEntity domainEntity = virtualMachineEntity.getDomain();
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在。");
            return;
        }

        String createdFlag = virtualMachineEntity.getCreatedFlag();
        if (!createdFlag.equals(Constant.VM_CREATE_SUCCESS) && !createdFlag.equals(Constant.VM_CREATE_FAILED)) {
            cloudContext.addErrorMsg("当前虚拟机状态不允许删除");
            return;
        }
        //删除备份
        deleteBackupOperate(cloudContext, virtualMachineEntity);
        if (!cloudContext.getSuccessIngoreWarn()) {
            return;
        }

        //删除创建成功状态的虚拟机数据
        if (createdFlag.equals(Constant.VM_CREATE_SUCCESS)) {
            boolean result = deleteCoreVirtualMachine(virtualMachineEntity, cloudContext);
            if (!result) {
                return;
            }
        }

        //修改存储可用大小
        List<VolumnEntity> volumns = virtualMachineEntity.getVolumns();
        StorageResourceEntity storageResourceEntity = null;
        for (VolumnEntity volumn : volumns) {
            storageResourceEntity = volumn.getStorageResource();
            storageResourceEntity.setAvailableCapacity(storageResourceEntity.getAvailableCapacity() + volumn.getSize());
            storageResourceDAO.update(storageResourceEntity);
        }
        //更新当前域下剩余空间大小
        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity()
                        + volumnDAO.getTotalSize(virtualMachineEntity.getId()));
        Integer cpu = virtualMachineEntity.getMachineType().getCpu();
        Integer memory = virtualMachineEntity.getMachineType().getMemory();
        domainEntity.setCpuAvailableNum(domainEntity.getCpuAvailableNum() + cpu);
        domainEntity.setMemoryAvailableCapacity(domainEntity.getMemoryAvailableCapacity() + memory);

        //更新计算节点空间大小
        ComputeResourceEntity computeResourceEntity = virtualMachineEntity.getComputeResource();
        computeResourceEntity.setCpuAvailable(computeResourceEntity.getCpuAvailable() + cpu);
        computeResourceEntity.setMemoryAvailable(computeResourceEntity.getMemoryAvailable() + memory);

        domainDAO.update(domainEntity);
        computeResourceDAO.update(computeResourceEntity);
        //删除虚拟机与关联用户信息
        portalUserBinVirtualMachineOrderDAO.deleteByVmId(virtualMachineEntity.getId());
        virtualMachineDAO.deleteById(cloudContext.getVo().getId());
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog(String.format("删除虚拟机:【%1$s】", virtualMachineEntity.getName()), domainEntity.getId(),
                        cloudContext);
    }

    /**
     * 删除真实虚拟机
     * 
     * @param virtualMachineEntity
     * @return
     */
    public boolean deleteCoreVirtualMachine(VirtualMachineEntity virtualMachineEntity,
                    CloudContext<VirtualMachineVO> cloudContext) {
        try {
            //调用core
            VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(), virtualMachineEntity
                            .getComputeResource().getIp());
            if (virtualMachine == null) {
                LogUtil.info("虚拟机[" + virtualMachineEntity.getVmName() + "]在后台已经不存在");
                return true;
            }
            virtualMachine.delete();
        } catch (VirtualizationException e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return false;
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            LogUtil.error(e);
            return false;
        }
        return true;
    }

    /**
     * 查询虚拟机状态
     * 
     * @param virtualMachineEntity
     *            业务虚拟机实体类
     * @param cloudContext
     *            上下文
     * @return default Status.error
     */
    private Status getVirtualMachineStatus(VirtualMachineEntity virtualMachineEntity,
                    CloudContext<VirtualMachineVO> cloudContext) {
        Status status = Status.unknow;
        if (Constant.VM_CREATE_SUCCESS.equals(virtualMachineEntity.getCreatedFlag())) {
            try {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                if (virtualMachine == null) {
                    return status;
                }
                status = virtualMachine.getStatus();
            } catch (Exception e) {
                LogUtil.error(e);
                cloudContext.addErrorMsg(e.getMessage());
            }
        }
        return status;
    }

    /**
     * vnc
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void vnc(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(), virtualMachineEntity
                            .getComputeResource().getIp());

            cloudContext.addParam("hostname", virtualMachineEntity.getComputeResource().getIp());
            cloudContext.addParam("port", VmUtils.getvncPort(virtualMachineEntity.getVmName(), virtualMachine
                            .getHostIp()));
            cloudContext.addParam("vmName", virtualMachineEntity.getName());
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 开机
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void startup(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.shutoff || status == Status.defined) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.start();
                //提示信息
                cloudContext.addSuccessMsg("正在开机,请稍候查看状态。 ");
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
        insertEventLog(String.format("开启虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity.getDomain()
                        .getId(), cloudContext);
    }

    /**
     * 重启
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void reboot(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.running) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.reboot();
                //提示信息
                cloudContext.addSuccessMsg("正在重启，请稍候查看状态。");
                insertEventLog(String.format("重启虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity
                                .getDomain().getId(), cloudContext);
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 恢复
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void resume(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.paused) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.resume();
                //提示信息
                cloudContext.addSuccessMsg("正在恢复中，请稍候查看状态。");
                insertEventLog(String.format("恢复虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity
                                .getDomain().getId(), cloudContext);
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 挂起
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void suspend(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.running) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.suspend();
                //提示信息
                cloudContext.addSuccessMsg("正在挂起，请稍候查看状态。");
                insertEventLog(String.format("挂起虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity
                                .getDomain().getId(), cloudContext);
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 关机
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void shutdown(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.running) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.shutdown();
                //提示信息
                cloudContext.addSuccessMsg("正在关机，请稍候查看状态(如果关闭失败，请重试或强制关机)");
                insertEventLog(String.format("关闭虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity
                                .getDomain().getId(), cloudContext);
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        virtualMachineEntity.setDesc(cloudContext.getVo().getDesc());
        virtualMachineEntity.setBackupTimeMark(cloudContext.getVo().getBackupTimeMark());
        virtualMachineDAO.update(virtualMachineEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        //获取模板
        List<TemplateVO> templates = new ArrayList<TemplateVO>();

        TemplateVO templateVO = null;
        MachineTypeVO machineTypeVO = null;
        if (!cloudContext.getBooleanParam("updateFlag")) {
            List<TemplateEntity> templateEntitys = templateDAO.queryByDomain(cloudContext.getLoginedUser()
                            .getDomainID());
            for (TemplateEntity templateEntity : templateEntitys) {
                templateVO = new TemplateVO();
                BeanUtils.copyProperties(templateEntity, templateVO);
                templates.add(templateVO);
            }
            cloudContext.addParam("templates", templates);
            //获取配置
            List<MachineTypeVO> machineTypes = new ArrayList<MachineTypeVO>();
            List<MachineTypeEntity> machineTypeEntitys = machineTypeDAO.queryByDomain(cloudContext.getLoginedUser()
                            .getDomainID());
            for (MachineTypeEntity machineTypeEntity : machineTypeEntitys) {
                machineTypeVO = new MachineTypeVO();
                BeanUtils.copyProperties(machineTypeEntity, machineTypeVO);
                machineTypes.add(machineTypeVO);
            }
            cloudContext.addParam("machineTypes", machineTypes);
        }
        //获取网络
        List<NetWorkVO> netWorkVOs = new ArrayList<NetWorkVO>();
        List<NetWorkEntity> netWorkEntitys = netWorkDAO.list();
        NetWorkVO netWorkVO = null;
        for (NetWorkEntity netWorkEntity : netWorkEntitys) {
            netWorkVO = new NetWorkVO();
            BeanUtils.copyProperties(netWorkEntity, netWorkVO);
            netWorkVOs.add(netWorkVO);
        }
        cloudContext.addParam("netWorks", netWorkVOs);
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
            if (virtualMachineEntity == null) {
                cloudContext.addErrorMsg("虚拟机不存在！");
                return;
            }
            VirtualMachineVO virtualMachineVO = new VirtualMachineVO();
            BeanUtils.copyProperties(virtualMachineEntity, virtualMachineVO);
            virtualMachineVO.setDomainCode(virtualMachineEntity.getDomain().getCode());
            cloudContext.addParam("dataVo", virtualMachineVO);
            templateVO = new TemplateVO();
            BeanUtils.copyProperties(virtualMachineEntity.getTemplate(), templateVO);
            cloudContext.addParam("vmTemplate", templateVO);
            machineTypeVO = new MachineTypeVO();
            BeanUtils.copyProperties(virtualMachineEntity.getMachineType(), machineTypeVO);
            cloudContext.addParam("vmMachineType", machineTypeVO);
            cloudContext.addParam("netWorkID", virtualMachineEntity.getNetWork().getId());
        }
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOs);
    }

    /**
     * 检测cpu或者内存是否超配
     * 
     * @param computeResourceEntity
     * @param machineTypeEntity
     * @return
     * @throws SQLException
     *             sql异常
     */

    private Map<Boolean, String> testCPUOrMemory(ComputeResourceEntity computeResourceEntity,
                    MachineTypeEntity machineTypeEntity, DomainEntity currentDomain) throws SQLException {
        Map<Boolean, String> map = new HashMap<Boolean, String>();
        Integer minLimitCpu = null;
        minLimitCpu = computeResourceEntity.getCpu();
        if (minLimitCpu < computeResourceEntity.getCpuAvailable()) {
            minLimitCpu = computeResourceEntity.getCpuAvailable();
        }
        if (minLimitCpu < currentDomain.getCpuAvailableNum()) {
            minLimitCpu = currentDomain.getCpuAvailableNum();
        }
        if (machineTypeEntity.getCpu() > minLimitCpu) {
            map.put(false, "当前申请的虚机cpu超配！");
            return map;
        }
        Integer minLimitMemory = null;
        minLimitMemory = computeResourceEntity.getMemory();
        if (minLimitMemory < computeResourceEntity.getMemoryAvailable()) {
            minLimitMemory = computeResourceEntity.getMemoryAvailable();
        }
        if (minLimitMemory < currentDomain.getMemoryAvailableCapacity()) {
            minLimitMemory = currentDomain.getMemoryAvailableCapacity();
        }
        if (machineTypeEntity.getMemory() < minLimitMemory) {
            map.put(false, "当前申请的虚机内存超配！");
            return map;
        }
        map.put(true, "");
        return map;
    }

    /**
     * 备份
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insertSnapshot(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("该虚机不存在，请刷新后重试！");
            return;
        }
        String desc = cloudContext.getStringParam("desc");
        String snapshotName = ProjectUtil.createSnapshotName();
        VMSnapshotEntity snapshotEntity = new VMSnapshotEntity();
        snapshotEntity.setName(snapshotName);
        snapshotEntity.setVirtualMachine(virtualMachineEntity);
        snapshotEntity.setAddTime(new Date());
        snapshotEntity.setDesc(desc);
        snapshotEntity.setOperateFlag("正在创建中");
        vMSnapshotDAO.insert(snapshotEntity);
        //设置虚机状态
        virtualMachineEntity.setCreatedFlag(Constant.CREATE_SNAPSHOT_FLAG);
        virtualMachineEntity.setCreatedResultMsg("快照正在创建");
        virtualMachineDAO.update(virtualMachineEntity);
        cloudContext.addParam("vmId", virtualMachineEntity.getId());
        cloudContext.addParam("snapshotId", snapshotEntity.getId());
        insertEventLog(String.format("虚拟机【%1$s】创建快照【%2$s】", virtualMachineEntity.getName(), snapshotEntity.getDesc()),
                        virtualMachineEntity.getDomain().getId(), cloudContext);
    }

    /**
     * 还原
     * 
     * @param cloudContext
     * @throws SQLException
     * @throws Exception
     *             所有异常
     */
    public void restore(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long snapshotId = cloudContext.getLongParam("snapshotId");
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("该虚机不存在，请刷新后重试！");
            return;
        }
        VMSnapshotEntity snapshot = vMSnapshotDAO.get(snapshotId);
        snapshot.setOperateFlag("正在还原中");
        vMSnapshotDAO.update(snapshot);
        virtualMachineEntity.setCreatedFlag(Constant.RESTORE_SNAPSHOT_FLAG);
        virtualMachineEntity.setCreatedResultMsg("虚机系统正在还原");
        virtualMachineDAO.update(virtualMachineEntity);
        cloudContext.addParam("vmId", virtualMachineEntity.getId());
        cloudContext.addParam("snapshotId", snapshot.getId());
        insertEventLog(String.format("虚拟机【%1$s】还原快照【%2$s】", virtualMachineEntity.getName(), snapshot.getDesc()),
                        virtualMachineEntity.getDomain().getId(), cloudContext);
    }

    /**
     * 初始化还原
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initRestore(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        List<VMSnapshotEntity> snapshotEntitys = virtualMachineEntity.getVmSnapshots();
        VMSnapshotVO snapshotVO = null;
        List<VMSnapshotVO> snapshotVOs = new ArrayList<VMSnapshotVO>();
        for (VMSnapshotEntity entity : snapshotEntitys) {
            snapshotVO = new VMSnapshotVO();
            BeanUtils.copyProperties(entity, snapshotVO);
            snapshotVOs.add(snapshotVO);
        }
        cloudContext.addParam("snapshots", snapshotVOs);
    }

    /**
     * 删除快照
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void deleteSnapshot(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long snapshotId = cloudContext.getLongParam("snapshotId");
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("该虚机不存在，请刷新后重试！");
            return;
        }
        VMSnapshotEntity snapshot = vMSnapshotDAO.get(snapshotId);
        snapshot.setOperateFlag("正在删除中");
        vMSnapshotDAO.update(snapshot);
        virtualMachineEntity.setCreatedFlag(Constant.DELETE_SNAPSHOT_FLAG);
        virtualMachineEntity.setCreatedResultMsg("快照正在删除中");
        virtualMachineDAO.update(virtualMachineEntity);
        cloudContext.addParam("vmId", virtualMachineEntity.getId());
        cloudContext.addParam("snapshotId", snapshot.getId());
        insertEventLog(String.format("虚拟机【%1$s】删除快照【%2$s】", virtualMachineEntity.getName(), snapshot.getDesc()),
                        virtualMachineEntity.getDomain().getId(), cloudContext);
    }

    /**
     * @param cloudContext
     */
    public void queryAllSuperDomainTemplate(CloudContext<VirtualMachineVO> cloudContext) {
        List<TemplateVO> templates = new ArrayList<TemplateVO>();
        String currentDomainCode = cloudContext.getStringParam("domainCode");
        try {
            addSuperDomainTemplate(templates, currentDomainCode);
        } catch (Exception e) {
            LogUtil.error(e);
            cloudContext.addErrorMsg("查询出错！");
        }
        cloudContext.addParam("parentsDomainTemplate", templates);
    }

    /**
     * 查询所有上级域下的模版（包括自己）
     * 
     * @param templates
     * @param domainCode
     * @throws SQLException
     *             sql异常
     */
    private void addSuperDomainTemplate(List<TemplateVO> templates, String domainCode) throws SQLException {
        List<TemplateEntity> entitys = templateDAO.queryByDomainCode(domainCode);
        TemplateVO vo = null;
        for (TemplateEntity entity : entitys) {
            vo = new TemplateVO();
            BeanUtils.copyProperties(entity, vo);
            templates.add(vo);
        }
        if (domainCode.length() > 2) {
            addSuperDomainTemplate(templates, domainCode.substring(0, domainCode.length() - 2));
        }
    }

    /**
     * @param cloudContext
     */
    public void queryAllSuperDomainMachineTypes(CloudContext<VirtualMachineVO> cloudContext) {
        List<MachineTypeVO> machineTypes = new ArrayList<MachineTypeVO>();
        String currentDomainCode = cloudContext.getStringParam("domainCode");
        try {
            addSuperDomainMachineType(machineTypes, currentDomainCode);
        } catch (Exception e) {
            LogUtil.error(e);
            cloudContext.addErrorMsg("查询出错！");
        }
        cloudContext.addParam("parentsDomainMachineTypes", machineTypes);
    }

    /**
     * 查询所有上级域下的机型（包括自己）
     * 
     * @param machineTypes
     * @param domainCode
     * @throws SQLException
     *             sql异常
     */
    private void addSuperDomainMachineType(List<MachineTypeVO> machineTypes, String domainCode) throws SQLException {
        List<MachineTypeEntity> entitys = machineTypeDAO.queryByDomainCode(domainCode);
        MachineTypeVO vo = null;
        for (MachineTypeEntity entity : entitys) {
            vo = new MachineTypeVO();
            BeanUtils.copyProperties(entity, vo);
            machineTypes.add(vo);
        }
        if (domainCode.length() > 2) {
            addSuperDomainMachineType(machineTypes, domainCode.substring(0, domainCode.length() - 2));
        }
    }

    /**
     * 匹配计算节点
     * 
     * @param machineTypeEntity
     * @param domainEntity
     * @param cloudContext
     * @return object数组 1：匹配计算节点对象 2：无匹配节点时的错误信息
     * @throws SQLException
     *             sql异常
     */
    private Object[] matchComputeResource(MachineTypeEntity machineTypeEntity, DomainEntity domainEntity,
                    boolean customComputeResourceFlag, Long computeResourceID) throws SQLException {
        Object[] objects = new Object[2];
        ComputeResourceEntity computeResourceEntity = null;

        //查看是否选择了自定义
        if (customComputeResourceFlag) {
            computeResourceEntity = computeResourceDAO.get(computeResourceID);
            if (computeResourceEntity == null) {
                objects[0] = null;
                objects[1] = "添加失败，计算节点不存在！";
                return objects;
            }
            //查看CPU和内存信息是否匹配
            if (computeResourceEntity.getCpu() < machineTypeEntity.getCpu()
                            || computeResourceEntity.getCpuAvailable() < machineTypeEntity.getCpu()
                            || computeResourceEntity.getMemory() < machineTypeEntity.getMemory()
                            || computeResourceEntity.getMemoryAvailable() < machineTypeEntity.getMemory()) {
                objects[0] = null;
                objects[1] = "添加失败:" + msg4MaxCpuAndMaxMemoryCompute(domainEntity.getId());
                return objects;
            }
        } else {
            computeResourceEntity = computeResourceDAO.getByCpuAndMemoryAndDomainId(machineTypeEntity.getCpu(),
                            machineTypeEntity.getMemory(), domainEntity.getId());
            if (computeResourceEntity == null) {
                String error = msg4MaxCpuAndMaxMemoryCompute(domainEntity.getId());
                objects[0] = null;
                objects[1] = "添加失败:" + error;
                return objects;
            }
        }
        objects[0] = computeResourceEntity;
        objects[1] = "";
        return objects;
    }

    /**
     * 找到可设最大cpu 和 可设置最大内存的计算节点，返回描述字符串
     * 
     * @param domainID
     * @return
     * @throws SQLException
     *             sql异常
     */
    private String msg4MaxCpuAndMaxMemoryCompute(Long domainID) throws SQLException {
        ComputeResourceEntity maxCpuCompute = computeResourceDAO.getOptimalCpuCompute(domainID);
        ComputeResourceEntity maxMemoryCompute = computeResourceDAO.getOptimalMemoryCompute(domainID);
        if (maxCpuCompute == null || maxMemoryCompute == null) {
            return "没有可用计算节点";
        }
        Integer cpu4maxCpuCR = Math.min(maxCpuCompute.getCpu(), maxCpuCompute.getCpuAvailable());
        Integer memory4MaxCpuCR = Math.min(maxCpuCompute.getMemory(), maxCpuCompute.getMemoryAvailable());
        Integer memory4MaxMemCR = Math.min(maxMemoryCompute.getMemory(), maxMemoryCompute.getMemoryAvailable());
        Integer cpu4MaxMemCR = Math.min(maxCpuCompute.getCpu(), maxCpuCompute.getCpuAvailable());
        return "\\r 可设最大CPU的计算节点为" + maxCpuCompute.getName() + "：\\r cpu " + cpu4maxCpuCR + ",内存 " + memory4MaxCpuCR
                        + ";\\r 可设最大内存的计算节点为" + maxMemoryCompute.getName() + "：\\r cpu " + cpu4MaxMemCR + ",内存 "
                        + memory4MaxMemCR;
    }

    /**
     * @param cloudContext
     */
    public void queryRackByRoom(CloudContext<VirtualMachineVO> cloudContext) {
        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        MachineRoomEntity machineRoomEntity = null;
        try {
            machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomID"));
        } catch (SQLException e) {
            LogUtil.error(e);
            cloudContext.addErrorMsg("获取机房出错！");
            return;
        }
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

    /**
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryComputeByRack(CloudContext<VirtualMachineVO> cloudContext) throws SQLException {
        List<ComputeResourceEntity> computes = computeResourceDAO.queryByRackId(cloudContext.getLongParam("rackID"));
        List<ComputeResourceVO> computeVOs = new ArrayList<ComputeResourceVO>();
        ComputeResourceVO computeVO = null;
        for (ComputeResourceEntity e : computes) {
            computeVO = new ComputeResourceVO();
            BeanUtils.copyProperties(e, computeVO);
            computeVOs.add(computeVO);
        }
        cloudContext.addParam("computes", computeVOs);
    }

    /**
     * 切换域时获取数据
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryData4ChangeDomain(CloudContext<VirtualMachineVO> cloudContext) throws SQLException {
        String domainCode = cloudContext.getStringParam("domainCode");
        List<TemplateEntity> templates = templateDAO.queryByDomainCode(domainCode);
        List<TemplateVO> templateVOs = new ArrayList<TemplateVO>();
        TemplateVO templateVO = null;
        for (TemplateEntity e : templates) {
            templateVO = new TemplateVO();
            BeanUtils.copyProperties(e, templateVO);
            templateVOs.add(templateVO);
        }
        cloudContext.addParam("templates", templateVOs);

        List<MachineTypeEntity> mts = machineTypeDAO.queryByDomainCode(domainCode);
        List<MachineTypeVO> mtVOs = new ArrayList<MachineTypeVO>();
        MachineTypeVO mtVO = null;
        for (MachineTypeEntity e : mts) {
            mtVO = new MachineTypeVO();
            BeanUtils.copyProperties(e, mtVO);
            mtVOs.add(mtVO);
        }
        cloudContext.addParam("machineTypes", mtVOs);

    }

    /**
     * vnc下载
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void vncDownLoad(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        String uri = ProjectUtil.getHomeDir().getAbsolutePath() + File.separator + "client" + File.separator
                        + "VNCSetup.msi";
        File vnc = new File(uri);
        FileInputStream fis = new FileInputStream(vnc);
        cloudContext.addParam("vncName", vnc.getName());
        cloudContext.addParam("vncFis", fis);
    }

    /**
     * 强制关机
     * 
     * @param cloudContext
     * @throws Exception
     *             sql异常
     */
    public void forceShutdown(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }
        try {
            Status status = getVirtualMachineStatus(virtualMachineEntity, cloudContext);
            if (status == Status.running) {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                virtualMachineEntity.getComputeResource().getIp());
                virtualMachine.enforceShutdown();
                //提示信息
                cloudContext.addSuccessMsg("正在强制关机，请稍候查看状态。");
                insertEventLog(String.format("强制关闭虚拟机【%1$s】", virtualMachineEntity.getName()), virtualMachineEntity
                                .getDomain().getId(), cloudContext);
            } else {
                cloudContext.addErrorMsg("虚拟机状态不正确");
            }
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
    }

    /**
     * 所有异常
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initMoveVm(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long vmId = cloudContext.getVo().getId();
        VirtualMachineEntity vmEntity = virtualMachineDAO.get(vmId);
        if (vmEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在，请刷新后重试！");
            return;
        }
        ComputeResourceEntity crEntity = vmEntity.getComputeResource();
        if (crEntity == null) {
            cloudContext.addErrorMsg(String.format("虚拟机【%1$s】的计算节点不存在！", vmEntity.getName()));
            return;
        }
        ComputeResourcePoolEntity crpEntity = computeResourcePoolDAO.findByVmId(vmEntity.getId());
        if (crpEntity == null) {
            cloudContext.addErrorMsg(String.format("虚拟机【%1$s】的资源池不存在！", vmEntity.getName()));
            return;
        }
        MachineTypeEntity mtEntity = vmEntity.getMachineType();
        Integer cpu = mtEntity.getCpu();
        Integer memory = mtEntity.getMemory();
        List<ComputeResourceEntity> crEntitys = computeResourceDAO.queryByPoolIdAndMinCPUAndMinMemoryExcludVmCr(
                        crpEntity.getId(), cpu, memory, crEntity.getId());
        List<ComputeResourceVO> crVOs = new ArrayList<ComputeResourceVO>();
        ComputeResourceVO crVO = null;
        for (ComputeResourceEntity e : crEntitys) {
            crVO = new ComputeResourceVO();
            BeanUtils.copyProperties(e, crVO);
            crVOs.add(crVO);
        }
        cloudContext.addParam("computeResources", crVOs);
    }

    /**
     * 虚拟机迁移
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void updateMigrateVm(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        Long vmId = cloudContext.getVo().getId();
        VirtualMachineEntity vmEntity = virtualMachineDAO.get(vmId);
        if (vmEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在，请刷新后重试");
            return;
        }
        if (!vmEntity.getCreatedFlag().equals(Constant.VM_CREATE_SUCCESS)) {
            cloudContext.addErrorMsg("虚拟机状态不正确，请稍后再试");
            return;
        }
        Long computeId = cloudContext.getLongParam("computeResourceId");
        ComputeResourceEntity crEntity = computeResourceDAO.get(computeId);
        if (crEntity == null) {
            cloudContext.addErrorMsg("要迁移的计算节点不存在");
            return;
        }
        if (vmEntity.getComputeResource().getIp().equals(crEntity.getIp())) {
            cloudContext.addErrorMsg(String.format("虚拟机已经迁移到【%1$s】", crEntity.getIp()));
            return;
        }
        MachineTypeEntity mtEntity = vmEntity.getMachineType();
        if (mtEntity == null) {
            cloudContext.addErrorMsg("模版不存在，请刷新后重试！");
            return;
        }
        if (mtEntity.getCpu() > crEntity.getCpuAvailable()) {
            cloudContext.addErrorMsg("迁移失败：虚拟机的cpu核数大于计算节点的剩余cpu核数");
            return;
        }
        if (mtEntity.getMemory() > crEntity.getMemoryAvailable()) {
            cloudContext.addErrorMsg("迁移失败：虚拟机的内存大于计算节点的剩余内存");
            return;
        }
        vmEntity.setCreatedFlag(Constant.VM_OPERATE_MIGRATING);
        vmEntity.setCreatedResultMsg(Constant.VM_MIGRATING_MSG);
        virtualMachineDAO.update(vmEntity);
        cloudContext.addParam("vmId", vmEntity.getId());
        cloudContext.addParam("crId", crEntity.getId());
        insertEventLog(String.format("虚拟机【%1$s】迁移至【%2$s】", vmEntity.getName(), crEntity.getName()), vmEntity
                        .getDomain().getId(), cloudContext);
    }

    /**
     * 初始话备份虚机
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void initRestore2Storage(CloudContext<VirtualMachineVO> cloudContext) throws SQLException {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        List<VMBackupEntity> snapshotEntitys = virtualMachineEntity.getVmBackups();
        VMBackupVO backupVO = null;
        List<VMBackupVO> vMBackupVOs = new ArrayList<VMBackupVO>();
        for (VMBackupEntity entity : snapshotEntitys) {
            backupVO = new VMBackupVO();
            BeanUtils.copyProperties(entity, backupVO);
            vMBackupVOs.add(backupVO);
        }
        cloudContext.addParam("vMBackups", vMBackupVOs);
    }

    /**
     * 备份虚机
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void restore2Storage(CloudContext<VirtualMachineVO> cloudContext) throws SQLException {
        Long backupId = cloudContext.getLongParam("backupId");
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("该虚机不存在，请刷新后重试！");
            return;
        }
        if (!virtualMachineEntity.getCreatedFlag().equals(Constant.VM_CREATE_SUCCESS)) {
            cloudContext.addErrorMsg("该虚机状态不正确，请刷新或者稍后重试！");
            return;
        }
        //TODO  备份存储资源修改
        VMBackupEntity backup = vMBackupDAO.get(backupId);
        backup.setOperateFlag("正在还原中");
        vMBackupDAO.update(backup);
        virtualMachineEntity.setCreatedFlag(Constant.RESTORE_BACKUP_FLAG);
        virtualMachineEntity.setCreatedResultMsg("虚机系统正在还原");
        virtualMachineDAO.update(virtualMachineEntity);
        cloudContext.addParam("vmId", virtualMachineEntity.getId());
        cloudContext.addParam("backupId", backup.getId());
        insertEventLog(String.format("虚拟机【%1$s】还原备份【%2$s】", virtualMachineEntity.getName(), backup.getDesc()),
                        virtualMachineEntity.getDomain().getId(), cloudContext);
    }

    /**
     * 备份虚拟机
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void insertBackup2Storage(CloudContext<VirtualMachineVO> cloudContext) throws SQLException {
        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("该虚机不存在，请刷新后重试！");
            return;
        }
        if (!virtualMachineEntity.getCreatedFlag().equals(Constant.VM_CREATE_SUCCESS)) {
            cloudContext.addErrorMsg("该虚机状态不正确，请刷新或者稍后重试！");
            return;
        }
        String backupDesc = cloudContext.getStringParam("desc");
        //
        BackupStorageEntity bsEntity = backupStorageDAO.obtainMaxBackupStorage();
        if (bsEntity == null) {
            cloudContext.addErrorMsg("没有可用的备份存储！");
            return;
        }
        //删除以前的老备份  TODO ... 删除后备份失败？
        deleteBackupOperate(cloudContext, virtualMachineEntity);
        String backupName = String.format("vm_%1$s_backup", virtualMachineEntity.getName());
        VMBackupEntity vMBackupEntity = new VMBackupEntity();
        vMBackupEntity.setBackupStorage(bsEntity);
        vMBackupEntity.setName(backupName);
        vMBackupEntity.setVirtualMachine(virtualMachineEntity);
        vMBackupEntity.setAddTime(new Date());
        vMBackupEntity.setDesc(backupDesc);
        vMBackupEntity.setCapacity(0L);//先置为0，防止哪空间是空指针，拿到存储后赋值
        vMBackupEntity.setOperateFlag("正在备份中");
        vMBackupDAO.insert(vMBackupEntity);
        //设置vMSnapshotDAO   
        virtualMachineEntity.setCreatedFlag(Constant.CREATE_BACKUP_FLAG);
        virtualMachineEntity.setCreatedResultMsg("正在创建备份");
        virtualMachineDAO.update(virtualMachineEntity);
        cloudContext.addParam("vmId", virtualMachineEntity.getId());
        cloudContext.addParam("backupId", vMBackupEntity.getId());
        insertEventLog(String.format("虚拟机【%1$s】创建备份【%2$s】", virtualMachineEntity.getName(), vMBackupEntity.getDesc()),
                        virtualMachineEntity.getDomain().getId(), cloudContext);
    }

    /**
     * 删除备份
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void deleteBackupVmData(CloudContext<VirtualMachineVO> cloudContext) throws Exception {
        VirtualMachineEntity vmEntity = virtualMachineDAO.get(cloudContext.getVo().getId());
        deleteBackupOperate(cloudContext, vmEntity);
        if (cloudContext.getSuccessIngoreWarn()) {
            cloudContext.addSuccessMsg("删除备份成功");
        }
    }

    /**
     * 删除备份操作
     * 
     * @param cloudContext
     * @param vmEntity
     * @throws SQLException
     *             sql异常
     */
    private void deleteBackupOperate(CloudContext<VirtualMachineVO> cloudContext, VirtualMachineEntity vmEntity)
                    throws SQLException {
        List<VMBackupEntity> vMBackups = vmEntity.getVmBackups();
        if (vMBackups.isEmpty()) {
            return;
        }
        VMBackupEntity backupEntity = vMBackups.get(0);
        BackupStorageEntity bsEntity = backupEntity.getBackupStorage();
        //调用core
        try {
            VirtualMachine vm = VirtualMachine.getVM(vmEntity.getVmName(), vmEntity.getComputeResource().getIp());
            if (vm == null) {
                cloudContext.addWarnMsg("虚拟机不存在");
            } else {
                vm.deleteBackUp(bsEntity.getIp(), backupEntity.getName());
            }
        } catch (VirtualizationException e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        }
        //更新资源
        bsEntity.setAvailableCapacity(bsEntity.getAvailableCapacity() + backupEntity.getCapacity());
        backupStorageDAO.update(bsEntity);
        vMBackups.remove(backupEntity);
        vmEntity.setVmBackups(vMBackups);
        virtualMachineDAO.update(vmEntity);
        vMBackupDAO.delete(backupEntity);
    }
}
