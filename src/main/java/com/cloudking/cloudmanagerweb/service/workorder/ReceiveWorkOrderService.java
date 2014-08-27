/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.workorder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.virtualization.VirtualMachine;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserBinVirtualMachineOrderDAO;
import com.cloudking.cloudmanagerweb.dao.ResourceOrderDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.VolumnDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderActionDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderCategoryDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderSolutionDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.NetWorkEntity;
import com.cloudking.cloudmanagerweb.entity.PortalOrderEntity;
import com.cloudking.cloudmanagerweb.entity.PortalUserBinVirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.ResourceOrderEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderActionEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderCategoryEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderSolutionEntity;
import com.cloudking.cloudmanagerweb.service.virtualmachine.VirtualMachineService;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.MailTemplateUtil;
import com.cloudking.cloudmanagerweb.util.MailUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.NetWorkVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderActionVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderCategoryVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderSolutionVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * WorkOrderService
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("recevieWorkOrderService")
public class ReceiveWorkOrderService extends BaseService {

    /**
     * virtualMachineService
     */
    @Resource
    private VirtualMachineService virtualMachineService;

    /**
     * resourceOrderDAO
     */
    @Resource
    private ResourceOrderDAO resourceOrderDAO;

    /**
     * workOrderCategoryDAO
     */
    @Resource
    private WorkOrderCategoryDAO workOrderCategoryDAO;

    /**
     * workOrderActionDAO
     */
    @Resource
    private WorkOrderActionDAO workOrderActionDAO;

    /**
     * netWorkDAO
     */
    @Resource
    private NetWorkDAO netWorkDAO;

    /**
     * workOrderDAO
     */
    @Resource
    private WorkOrderDAO workOrderDAO;

    /**
     * 订单DAO
     */
    @Resource
    private transient PortalOrderDAO portalOrderDAO;
    /**
     * 存储资源DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;
    /**
     * 计算节点DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;
    /**
     * 存储DAO
     */
    @Resource
    private VolumnDAO volumnDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * 虚拟机虚拟机DAO
     */
    @Resource
    private transient VirtualMachineDAO virtualMachineDAO;

    /**
     * user DAO
     */
    @Resource
    private transient UserDAO userDAO;
    /**
     * workOrderSolutionDAO
     */
    @Resource
    private transient WorkOrderSolutionDAO workOrderSolutionDAO;

    /**
     * 用户与虚拟机关系DAO
     */
    @Resource
    private transient PortalUserBinVirtualMachineOrderDAO portalUserBinVirtualMachineOrderDAO;

    /**
     * 虚拟机创建工单初始化数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdateForVm(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        //获取网络
        List<NetWorkVO> netWorkVOs = new ArrayList<NetWorkVO>();
        List<NetWorkEntity> netWorkEntitys = netWorkDAO.list();
        NetWorkVO netWorkVO = null;
        for (NetWorkEntity netWorkEntity : netWorkEntitys) {
            netWorkVO = new NetWorkVO();
            BeanUtils.copyProperties(netWorkEntity, netWorkVO);
            netWorkVOs.add(netWorkVO);
        }

        //获取解决情况
        List<WorkOrderSolutionVO> workOrderSolutionVOs = new ArrayList<WorkOrderSolutionVO>();
        WorkOrderSolutionVO workOrderSolutionVO = null;
        List<WorkOrderSolutionEntity> solutions = workOrderSolutionDAO.list();
        for (WorkOrderSolutionEntity solution : solutions) {
            workOrderSolutionVO = new WorkOrderSolutionVO();
            BeanUtils.copyProperties(solution, workOrderSolutionVO);
            workOrderSolutionVOs.add(workOrderSolutionVO);
        }

        //获取默认虚拟机名称
        SimpleDateFormat formater = new SimpleDateFormat("yyMMdd");
        String nameKeyWord = formater.format(new Date());

        List<Object> defaultVirtualMachineName = portalOrderDAO.queryPortalUsernameAndCountsByRefId(cloudContext
                        .getLongParam("workOrderId"), nameKeyWord);
        StringBuilder defaultName = new StringBuilder();
        if (defaultVirtualMachineName != null) {
            Object[] objects = (Object[]) defaultVirtualMachineName.get(0);
            defaultName.append(objects[0] == null ? "" : objects[0].toString());
            defaultName.append("_" + nameKeyWord + "_");
            defaultName.append(objects[1] == null ? "1" : Integer.parseInt(objects[1].toString()) + 1);
        }
        cloudContext.addParam("netWorks", netWorkVOs);
        cloudContext.addParam("solutions", workOrderSolutionVOs);
        cloudContext.addParam("defaultVirtualMachineName", defaultName.toString());
    }

    /**
     * 删除虚拟机申请单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void deleteVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        String handleMsg = cloudContext.getStringParam("handleMsg");
        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }

        if (!workOrderEntity.getCategory().getId().equals(Constant.WORK_ORDER_CATEGORY_DELETE_VM)) {
            cloudContext.addErrorMsg("工单类型不正确");
            return;
        }

        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("会话超时");
            return;
        }

        PortalOrderEntity portalOrderEntity = portalOrderDAO.get(workOrderEntity.getRefId());
        if (portalOrderEntity == null) {
            cloudContext.addErrorMsg("订单不存在");
            return;
        }

        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(portalOrderEntity.getRefId());
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机不存在！请刷新后重试");
            return;
        }

        //解决情况
        WorkOrderSolutionEntity workOrderSolutionEntity = workOrderSolutionDAO.get(cloudContext
                        .getLongParam("workOrderSolutions"));
        if (workOrderSolutionEntity == null) {
            cloudContext.addErrorMsg("没有相应解决情况!");
            return;
        }

        if (!workOrderEntity.getStatus().equals(Constant.WORKORDER_ACCEPING)) {
            cloudContext.addErrorMsg("工单状态不正确");
            return;
        }

        DomainEntity domainEntity = domainDAO.get(cloudContext.getLoginedUser().getDomainID());
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在。");
            return;
        }

        String createdFlag = virtualMachineEntity.getCreatedFlag();
        if (!createdFlag.equals(Constant.VM_CREATE_SUCCESS) && !createdFlag.equals(Constant.VM_CREATE_FAILED)) {
            cloudContext.addErrorMsg("当前虚拟机状态不允许删除");
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

        //更新工单信息
        Date currentTime = new Date();
        workOrderEntity.setStatus(Constant.WORKORDER_SOLVED);
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setSolveMsg(handleMsg);
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction("同意删除虚拟机申请");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】对序列号为：" + workOrderEntity.getSerialNumber()
                        + " 的工单执行同意删除虚拟机操作");
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);

        //邮件信息
        Map<String, String> emailInfoMap = MailTemplateUtil.getApproveDelVirtualMachineMailTemplate(portalOrderEntity
                        .getApplicant().getEmail(), portalOrderEntity.getApplicant().getRealname(), userEntity
                        .getRealname(), handleMsg);

        MailUtil.sendMail(emailInfoMap.get("subject"), emailInfoMap.get("content"), emailInfoMap.get("email"));
        cloudContext.addSuccessMsg("操作成功，邮件发送成功");

        workOrderActionDAO.insert(workActionEntity);
        workOrderDAO.update(workOrderEntity);
        domainDAO.update(domainEntity);
        computeResourceDAO.update(computeResourceEntity);
        //删除虚拟机与关联用户信息
        portalUserBinVirtualMachineOrderDAO.deleteByVmId(virtualMachineEntity.getId());
        virtualMachineDAO.deleteById(virtualMachineEntity.getId());
        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 工单-新建虚拟机
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }

        if (!workOrderEntity.getCategory().getId().equals(Constant.WORK_ORDER_CATEGORY_NEW_VM)) {
            cloudContext.addErrorMsg("工单类型不正确");
            return;
        }

        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("会话超时");
            return;
        }

        PortalOrderEntity portalOrderEntity = portalOrderDAO.get(workOrderEntity.getRefId());
        if (portalOrderEntity == null) {
            cloudContext.addErrorMsg("订单不存在");
            return;
        }

        DomainEntity domainEntity = workOrderEntity.getReceiveDomain();
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }

        TemplateEntity templateEntity = portalOrderEntity.getTemplate();
        if (templateEntity == null) {
            cloudContext.addErrorMsg("模板不存在");
            return;
        }

        MachineTypeEntity machineTypeEntity = portalOrderEntity.getMachineType();
        if (machineTypeEntity == null) {
            cloudContext.addErrorMsg("机型不存在");
            return;
        }

        NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getLongParam("netWork"));
        if (netWorkEntity == null) {
            cloudContext.addErrorMsg("没有相应网络!");
            return;
        }
        //解决情况
        WorkOrderSolutionEntity workOrderSolutionEntity = workOrderSolutionDAO.get(cloudContext
                        .getLongParam("workOrderSolutions"));
        if (workOrderSolutionEntity == null) {
            cloudContext.addErrorMsg("没有相应解决情况!");
            return;
        }

        if (!workOrderEntity.getStatus().equals(Constant.WORKORDER_ACCEPING)) {
            cloudContext.addErrorMsg("工单状态不正确");
            return;
        }

        String virtualMahcineName = cloudContext.getStringParam("virtualMachineName");
        if (StringUtil.isBlank(virtualMahcineName)) {
            cloudContext.addErrorMsg("虚拟机名字不为空");
            return;
        }

        String virtualMachineDesc = cloudContext.getStringParam("desc");
        boolean customComputeResourceFlag = cloudContext.getBooleanParam("customComputeResourceFlag");
        Long computeResourceID = cloudContext.getLongParam("computeResourceID");

        //邮件信息
        Map<String, String> emailInfoMap = MailTemplateUtil.getApproveMailTemplate(portalOrderEntity.getApplicant()
                        .getEmail(), portalOrderEntity.getApplicant().getRealname(), machineTypeEntity.getCpu(),
                        machineTypeEntity.getMemory(), machineTypeEntity.getDisk(), templateEntity.getName(),
                        templateEntity.getUsername(), templateEntity.getPassword());

        //调用通用类通过真正的虚拟机
        Object[] result = virtualMachineService.insertVirtualMachine(domainEntity, userEntity, templateEntity,
                        machineTypeEntity, netWorkEntity, virtualMahcineName, virtualMachineDesc,
                        customComputeResourceFlag, computeResourceID,null, emailInfoMap);
        if (!Boolean.parseBoolean(result[0].toString())) {
            cloudContext.addErrorMsg(result[1].toString());
            return;
        }
        VirtualMachineEntity virtualMachineEntity = (VirtualMachineEntity) result[2];
        if (virtualMachineEntity == null) {
            cloudContext.addErrorMsg("虚拟机对象无法获取");
            return;
        }

        //创建用户与虚拟机关系
        PortalUserBinVirtualMachineEntity userVm = new PortalUserBinVirtualMachineEntity();
        userVm.setPortalUser(portalOrderEntity.getApplicant());
        userVm.setVirtualMachine(virtualMachineEntity);
        //计算过期时间
        Date dueTime = null;
        Calendar calender = Calendar.getInstance();
        if (portalOrderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_NEVERDUE)) {
            dueTime = null;
        } else if (portalOrderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_MONTH)) {
            calender.add(Calendar.DAY_OF_MONTH, 30);
            dueTime = calender.getTime();
        } else if (portalOrderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_YEAR)) {
            calender.add(Calendar.DAY_OF_YEAR, 365);
            dueTime = calender.getTime();
        } else {
            cloudContext.addErrorMsg("订单有效期限不正确");
            return;
        }
        userVm.setDueTime(dueTime);

        //更新工单信息
        Date currentTime = new Date();
        workOrderEntity.setStatus(Constant.WORKORDER_SOLVED);
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setReceiver(userEntity);
        workOrderEntity.setSolveMsg(cloudContext.getStringParam("handleMsg"));
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction("新建虚拟机");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】对序列号为：" + workOrderEntity.getSerialNumber()
                        + " 的工单执行同意并创建虚拟机操作");
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);

        workOrderActionDAO.insert(workActionEntity);
        portalUserBinVirtualMachineOrderDAO.insert(userVm);

        cloudContext.addSuccessMsg("操作成功");
    }

    /**
     * 工单-虚拟机申请拒绝
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertRejectNewVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        updateVmWorkOrderStatus(cloudContext.getLongParam("workOrderId"), cloudContext.getLoginedUser().getId(),
                        cloudContext.getLongParam("workOrderSolutions"), cloudContext.getStringParam("handleMsg"),
                        true, cloudContext);
    }

    /**
     * 工单-虚拟机申请 关闭
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertCloseNewVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        updateVmWorkOrderStatus(cloudContext.getLongParam("workOrderId"), cloudContext.getLoginedUser().getId(),
                        cloudContext.getLongParam("workOrderSolutions"), cloudContext.getStringParam("handleMsg"),
                        false, cloudContext);
    }

    /**
     * 查询指定资源池下的计算节点，并要求cpu内存参数大于指定值
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryResourceByPoolAndCpuMemory(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long poolId = cloudContext.getLongParam("poolId");
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }

        PortalOrderEntity orderEntity = portalOrderDAO.get(workOrderEntity.getRefId());
        if (orderEntity == null) {
            cloudContext.addErrorMsg("订单不存在");
            return;
        }
        List<ComputeResourceEntity> computeResources = computeResourceDAO.queryResourceByPoolAndCpuMemory(poolId,
                        orderEntity.getMachineType().getCpu(), orderEntity.getMachineType().getMemory());
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
     * 查看工单详细信息
     * 
     * @param context
     * @throws Exception
     *             e
     */
    public void queryWorkOrderDetail(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");

        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }
        //工单信息
        WorkOrderVO workOrderVO = new WorkOrderVO();
        BeanUtils.copyProperties(workOrderEntity, workOrderVO);
        workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
        
        if (workOrderEntity.getSendDomain() != null) {
            if (workOrderEntity.getSendDomain().getUser() != null) {
                workOrderVO.setSendUserName(workOrderEntity.getSendDomain().getUser().getRealname());
            }else{
                workOrderVO.setSendUserName("系统");
            }
            workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
        }else{
            workOrderVO.setSendDomainName("系统");
        }

        if (workOrderEntity.getReceiveDomain() != null) {
            if (workOrderEntity.getReceiveDomain().getUser() != null) {
                workOrderVO.setReceiverName(workOrderEntity.getReceiveDomain().getUser().getRealname());
            }else{
                workOrderVO.setReceiverName("系统");
            }
            workOrderVO.setReceiveDomainName(workOrderEntity.getReceiveDomain().getName());
        }else{
            workOrderVO.setReceiveDomainName("系统");
        }
        
        cloudContext.addParam("workOrder", workOrderVO);
        //操作流信息
        List<WorkOrderActionEntity> workOrderActions = workOrderEntity.getWorkOrderActions();
        List<WorkOrderActionVO> workOrderActionVOs = new ArrayList<WorkOrderActionVO>();
        WorkOrderActionVO workOrderActionVO = null;
        for (WorkOrderActionEntity entity : workOrderActions) {
            workOrderActionVO = new WorkOrderActionVO();
            BeanUtils.copyProperties(entity, workOrderActionVO);
            workOrderActionVO.setActionUserName(entity.getActionUser() == null ? "系统" : entity.getActionUser()
                            .getRealname());
            workOrderActionVOs.add(workOrderActionVO);
        }

        //解决方案
        WorkOrderSolutionEntity solution = workOrderEntity.getSolution();
        if (solution != null) {
            workOrderVO.setSolutionName(solution.getName());
        }

        cloudContext.addParam("workOrderActions", workOrderActionVOs);
    }

    /**
     * 查询我接收的工单
     * 
     * @param context
     * @throws Exception
     *             e
     */
    public void queryReceiveWorkOrder(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long domainId = cloudContext.getLongParam("qDomain");
        if (domainId == null) {
            queryReceiveWorkOrderInAllDomain(cloudContext);
            return;
        }
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        workOrderDAO.queryReceiveWorkOrder(cloudContext.getStringParam("qTitle"), cloudContext
                                        .getLongParam("qCategory"), startDate, endDate, domainId,cloudContext.getLongParam("qSendDomain")));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objects = workOrderDAO.queryReceiveWorkOrder(cloudContext.getStringParam("qTitle"),
                            cloudContext.getLongParam("qCategory"), startDate, endDate, domainId,cloudContext.getLongParam("qSendDomain"), cloudContext
                                            .getPageInfo());
            WorkOrderVO workOrderVO = null;
            WorkOrderEntity workOrderEntity = null;
            int attachmentCount = 0;
            for (Object[] object : objects) {
                workOrderVO = new WorkOrderVO();
                workOrderEntity = (WorkOrderEntity) object[0];
                attachmentCount = Integer.parseInt(object[1].toString());

                BeanUtils.copyProperties(workOrderEntity, workOrderVO);
                workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
                workOrderVO.setCategoryId(workOrderEntity.getCategory().getId());
                if(workOrderEntity.getSendDomain() == null){
                    workOrderVO.setSendDomainName("系统");
                }else{
                    workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
                }
                workOrderVO.setAttachmentCount(attachmentCount);
                workOrderVOs.add(workOrderVO);
            }
        }

        //工单类别
        List<WorkOrderCategoryEntity> woCategoryEntitys = workOrderCategoryDAO.list();
        List<WorkOrderCategoryVO> woCategoryVOs = new ArrayList<WorkOrderCategoryVO>();
        WorkOrderCategoryVO woCategoryVo = null;
        for (WorkOrderCategoryEntity categoryEntity : woCategoryEntitys) {
            woCategoryVo = new WorkOrderCategoryVO();
            BeanUtils.copyProperties(categoryEntity, woCategoryVo);
            woCategoryVOs.add(woCategoryVo);
        }

        //查询域列表
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        
        domainEntitys = domainDAO.list();
        List<DomainVO> sendDomainVOs = new ArrayList<DomainVO>();
        for(DomainEntity e:domainEntitys){
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            sendDomainVOs.add(domainVO);
        }
        cloudContext.addParam("sendDomains", sendDomainVOs);
        cloudContext.addParam("domains", domainVOs);
        cloudContext.addParam("categorys", woCategoryVOs);
        cloudContext.addParam("workOrders", workOrderVOs);
    }

    /**
     * 用户所有域中查询我接收的工单
     * 
     * @param context
     * @throws Exception
     *             e
     */
    private void queryReceiveWorkOrderInAllDomain(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        workOrderDAO.queryReceiveWorkOrderInAllDoaminsCount(cloudContext.getStringParam("qTitle"),
                                        cloudContext.getLongParam("qCategory"), startDate, endDate, domainIds,cloudContext.getLongParam("qSendDomain")));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objects = workOrderDAO.queryReceiveWorkOrderInAllDoamins(cloudContext
                            .getStringParam("qTitle"), cloudContext.getLongParam("qCategory"), startDate, endDate,
                            domainIds,cloudContext.getLongParam("qSendDomain"), cloudContext.getPageInfo());
            WorkOrderVO workOrderVO = null;
            WorkOrderEntity workOrderEntity = null;
            int attachmentCount = 0;
            for (Object[] object : objects) {
                workOrderVO = new WorkOrderVO();
                workOrderEntity = (WorkOrderEntity) object[0];
                attachmentCount = Integer.parseInt(object[1].toString());

                BeanUtils.copyProperties(workOrderEntity, workOrderVO);
                workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
                workOrderVO.setCategoryId(workOrderEntity.getCategory().getId());
                if(workOrderEntity.getSendDomain() == null){
                    workOrderVO.setSendDomainName("系统");
                }else{
                    workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
                }
                workOrderVO.setAttachmentCount(attachmentCount);
                workOrderVOs.add(workOrderVO);
            }
        }
        //工单类别
        List<WorkOrderCategoryEntity> woCategoryEntitys = workOrderCategoryDAO.list();
        List<WorkOrderCategoryVO> woCategoryVOs = new ArrayList<WorkOrderCategoryVO>();
        WorkOrderCategoryVO woCategoryVo = null;
        for (WorkOrderCategoryEntity categoryEntity : woCategoryEntitys) {
            woCategoryVo = new WorkOrderCategoryVO();
            BeanUtils.copyProperties(categoryEntity, woCategoryVo);
            woCategoryVOs.add(woCategoryVo);
        }

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
        domainEntitys = domainDAO.list();
        List<DomainVO> sendDomainVOs = new ArrayList<DomainVO>();
        for(DomainEntity e:domainEntitys){
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            sendDomainVOs.add(domainVO);
        }
        cloudContext.addParam("sendDomains", sendDomainVOs);
        cloudContext.addParam("categorys", woCategoryVOs);
        cloudContext.addParam("workOrders", workOrderVOs);
    }

    /**
     * 工单-拒绝删除虚拟机申请
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertRejectDelVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        updateVmWorkOrderStatus(cloudContext.getLongParam("workOrderId"), cloudContext.getLoginedUser().getId(),
                        cloudContext.getLongParam("workOrderSolutions"), cloudContext.getStringParam("handleMsg"),
                        true, cloudContext);
    }

    /**
     * 工单-关闭删除虚拟机申请
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertCloseDelVirtualMachine(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        updateVmWorkOrderStatus(cloudContext.getLongParam("workOrderId"), cloudContext.getLoginedUser().getId(),
                        cloudContext.getLongParam("workOrderSolutions"), cloudContext.getStringParam("handleMsg"),
                        false, cloudContext);
    }

    /**
     * 工单-通过资源申请
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertResourceOrderApprove(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");

        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }
        ResourceOrderEntity resourceOrderEntity = resourceOrderDAO.get(workOrderEntity.getRefId());
        if (resourceOrderEntity == null) {
            cloudContext.addErrorMsg("资源申请单不存在!");
            return;
        }
        DomainEntity receiveDomainEntity = workOrderEntity.getReceiveDomain();
        if (receiveDomainEntity == null) {
            cloudContext.addErrorMsg("接收域不存在");
            return;
        }

        DomainEntity sendDomainEntity = workOrderEntity.getSendDomain();
        if (sendDomainEntity == null) {
            cloudContext.addErrorMsg("发送域不存在");
            return;
        }

        //域资源验证
        if (receiveDomainEntity.getCpuAvailableNum() < resourceOrderEntity.getCpu()) {
            cloudContext.addErrorMsg("添加失败，当前域可用CPU为：" + receiveDomainEntity.getCpuAvailableNum());
            return;
        }
        if (receiveDomainEntity.getMemoryAvailableCapacity() < resourceOrderEntity.getMemory()) {
            cloudContext.addErrorMsg("添加失败，当前域可用内存为：" + receiveDomainEntity.getMemoryAvailableCapacity());
            return;
        }
        if (receiveDomainEntity.getAvailableStorageCapacity() < resourceOrderEntity.getStorageCapacity()) {
            cloudContext.addErrorMsg("添加失败，当前域可用存储为：" + receiveDomainEntity.getAvailableStorageCapacity());
            return;
        }

        //更新域资源
        sendDomainEntity.setAvailableStorageCapacity(sendDomainEntity.getAvailableStorageCapacity()
                        + resourceOrderEntity.getStorageCapacity());
        sendDomainEntity.setStorageCapacity(sendDomainEntity.getStorageCapacity()
                        + resourceOrderEntity.getStorageCapacity());
        sendDomainEntity.setCpuAvailableNum(sendDomainEntity.getCpuAvailableNum() + resourceOrderEntity.getCpu());
        sendDomainEntity.setCpuTotalNum(sendDomainEntity.getCpuTotalNum() + resourceOrderEntity.getCpu());
        sendDomainEntity.setMemoryAvailableCapacity(sendDomainEntity.getMemoryAvailableCapacity()
                        + resourceOrderEntity.getMemory());
        sendDomainEntity.setMemoryCapacity(sendDomainEntity.getMemoryCapacity() + resourceOrderEntity.getMemory());

        receiveDomainEntity.setAvailableStorageCapacity(receiveDomainEntity.getAvailableStorageCapacity()
                        - resourceOrderEntity.getStorageCapacity());
        receiveDomainEntity.setCpuAvailableNum(receiveDomainEntity.getCpuAvailableNum() - resourceOrderEntity.getCpu());
        receiveDomainEntity.setMemoryAvailableCapacity(receiveDomainEntity.getMemoryAvailableCapacity()
                        - resourceOrderEntity.getMemory());

        //更新工单状态
        String actionName = "通过资源申请单";
        String opName = "通过";
        updateWorkOrderStatus(workOrderId, solutionId, Constant.WORK_ORDER_CATEGORY_NEW_RESOURCEORDER, handleMsg,
                        Arrays.asList(Constant.WORKORDER_ACCEPING), Constant.WORKORDER_SOLVED, actionName, opName,
                        cloudContext);

        domainDAO.update(sendDomainEntity);
        domainDAO.update(receiveDomainEntity);
    }

    /**
     * 工单-拒绝资源申请
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertResourceOrderReject(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");

        //更新工单状态
        String actionName = "拒绝资源申请单";
        String opName = "拒绝";
        updateWorkOrderStatus(workOrderId, solutionId, Constant.WORK_ORDER_CATEGORY_NEW_RESOURCEORDER, handleMsg,
                        Arrays.asList(Constant.WORKORDER_ACCEPING), Constant.WORKORDER_SOLVED, actionName, opName,
                        cloudContext);
    }

    /**
     * 工单-关闭资源申请
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertResourceOrderClose(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");

        //更新工单状态
        String actionName = "关闭资源申请单";
        String opName = "关闭";
        updateWorkOrderStatus(workOrderId, solutionId, Constant.WORK_ORDER_CATEGORY_NEW_RESOURCEORDER, handleMsg,
                        Arrays.asList(Constant.WORKORDER_ACCEPING, Constant.WORKORDER_SOLVED),
                        Constant.WORKORDER_CLOSED, actionName, opName, cloudContext);
    }

    /**
     * 解决普通工单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void updateCommonWorkOrderSovle(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");
        //更新工单状态
        String actionName = "解决工单";
        String opName = "解决";
        updateWorkOrderStatus(workOrderId, solutionId, Constant.WORK_ORDER_CATEGORY_COMMON, handleMsg, Arrays
                        .asList(Constant.WORKORDER_ACCEPING), Constant.WORKORDER_SOLVED, actionName, opName,
                        cloudContext);
    }

    /**
     * 关闭普通工单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void updateCommonWorkOrderClose(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");
        //更新工单状态
        String actionName = "关闭工单";
        String opName = "关闭";
        updateWorkOrderStatus(workOrderId, solutionId, Constant.WORK_ORDER_CATEGORY_COMMON, handleMsg, Arrays.asList(
                        Constant.WORKORDER_ACCEPING, Constant.WORKORDER_SOLVED), Constant.WORKORDER_CLOSED, actionName,
                        opName, cloudContext);
    }

    /**
     * 更新工单状态通用类,不包括虚拟机类别
     * 
     * @throws Exception
     *             e
     */
    @SuppressWarnings("unused")
    private void updateWorkOrderStatus(Long workOrderId, Long solutionId, Long category, String handleMsg,
                    List<String> allowStatus, String nextStatus, String actionName, String opName,
                    CloudContext<WorkOrderVO> cloudContext) throws Exception {
        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }
        if (!workOrderEntity.getCategory().getId().equals(category)) {
            cloudContext.addErrorMsg("工单类型不存在");
            return;
        }
        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("会话超时");
            return;
        }
        //解决情况
        WorkOrderSolutionEntity workOrderSolutionEntity = workOrderSolutionDAO.get(solutionId);
        if (workOrderSolutionEntity == null) {
            cloudContext.addErrorMsg("没有相应解决情况!");
            return;
        }

        boolean statusFlag = false;
        for (String allowState : allowStatus) {
            if (workOrderEntity.getStatus().equals(allowState)) {
                statusFlag = true;
                break;
            }
        }

        if (!statusFlag) {
            cloudContext.addErrorMsg("工单状态不正确!");
            return;
        }

        Date currentTime = new Date();
        workOrderEntity.setStatus(nextStatus);
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setReceiver(userEntity);
        workOrderEntity.setSolveMsg(handleMsg);
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        workOrderEntity.setCloseTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction(actionName);
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】执行" + opName + "工单操作，理由：" + handleMsg);
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);
        workOrderActionDAO.insert(workActionEntity);
        workOrderDAO.update(workOrderEntity);
        cloudContext.addSuccessMsg("操作成功!");
    }

    /**
     * 取消或关闭虚拟机类型工单
     * 
     * @param isReject
     *            取消或关闭标记位 true 表示取消，false 表示关闭
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    private void updateVmWorkOrderStatus(Long workOrderId, Long userId, Long solutionId, String handleMsg,
                    boolean isReject, CloudContext<WorkOrderVO> cloudContext) throws Exception {
        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }

        UserEntity userEntity = userDAO.get(userId);
        if (userEntity == null) {
            cloudContext.addErrorMsg("会话超时");
            return;
        }

        PortalOrderEntity portalOrderEntity = portalOrderDAO.get(workOrderEntity.getRefId());
        if (portalOrderEntity == null) {
            cloudContext.addErrorMsg("订单不存在");
            return;
        }

        DomainEntity domainEntity = workOrderEntity.getReceiveDomain();
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }

        //解决情况
        WorkOrderSolutionEntity workOrderSolutionEntity = workOrderSolutionDAO.get(solutionId);
        if (workOrderSolutionEntity == null) {
            cloudContext.addErrorMsg("没有相应解决情况!");
            return;
        }

        boolean isSolve = false;
        if (workOrderEntity.getStatus().equals(Constant.WORKORDER_CLOSED)) {
            cloudContext.addErrorMsg("工单状态不正确");
            return;
        } else if (workOrderEntity.getStatus().equals(Constant.WORKORDER_SOLVED)) {
            isSolve = true;
        }

        //更新工单信息
        Date currentTime = new Date();
        String suitableWord = "";
        if (isReject) {
            workOrderEntity.setStatus(Constant.WORKORDER_SOLVED);
            suitableWord = "拒绝";
        } else {
            workOrderEntity.setStatus(Constant.WORKORDER_CLOSED);
            suitableWord = "关闭";
            workOrderEntity.setCloseTime(currentTime);
        }
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setSolveMsg(handleMsg);
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction(suitableWord + "工单");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】对序列号为：" + workOrderEntity.getSerialNumber()
                        + " 的工单执行" + suitableWord + "操作");
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);
        workOrderActionDAO.insert(workActionEntity);

        if (!isSolve) {
            String content = "您序列号为[" + workOrderEntity.getSerialNumber() + "]的申请已经被拒绝！";
            Map<String, String> emailInfoMap = MailTemplateUtil.getRejectMailTemplate(portalOrderEntity.getApplicant()
                            .getEmail(), portalOrderEntity.getApplicant().getRealname(), content, userEntity
                            .getRealname(), handleMsg);
            MailUtil.sendMail(emailInfoMap.get("subject"), emailInfoMap.get("content"), emailInfoMap.get("email"));
        }
        cloudContext.addSuccessMsg("操作成功");
    }

    /**
     * 获取当前用户所有域Ids
     * 
     * @throws Exception
     *             e
     */
    private String getDomainIdsFromLoginUser(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        String result = "";
        List<DomainEntity> domains = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        if (domains == null || domains.size() < 1) {
            cloudContext.addErrorMsg("登录超时");
        }
        StringBuilder domainIdsBuilder = new StringBuilder();
        for (DomainEntity domain : domains) {
            domainIdsBuilder.append(domain.getId() + ",");
        }
        if (domainIdsBuilder.length() > 0) {
            domainIdsBuilder = domainIdsBuilder.delete(domainIdsBuilder.length() - 1, domainIdsBuilder.length());
            result = domainIdsBuilder.toString();
        }
        return result;
    }

    /**
     * 删除真实虚拟机
     * 
     * @param virtualMachineEntity
     * @return
     */
    public boolean deleteCoreVirtualMachine(VirtualMachineEntity virtualMachineEntity,
                    CloudContext<WorkOrderVO> cloudContext) {
        try {
            VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(), virtualMachineEntity
                            .getComputeResource().getIp());
            if (virtualMachine == null) {
                LogUtil.info("虚拟机[" + virtualMachineEntity.getVmName() + "]在后台已经不存在");
                return true;
            }
            virtualMachine.delete();
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return false;
        }
        return true;
    }
}
