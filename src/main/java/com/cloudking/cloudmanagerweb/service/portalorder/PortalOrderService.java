/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.portalorder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourcePoolDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserBinVirtualMachineOrderDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.VolumnDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.NetWorkEntity;
import com.cloudking.cloudmanagerweb.entity.PortalOrderEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.MailUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.NetWorkVO;
import com.cloudking.cloudmanagerweb.vo.PortalOrderVO;

/**
 * 资源service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("orderService")
public class PortalOrderService extends BaseService {

    /**
     * 计算节点池DAO
     */
    @Resource
    private ComputeResourcePoolDAO computeResourcePoolDAO;
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
     * 网络DAO
     */
    @Resource
    private NetWorkDAO netWorkDAO;

    /**
     * 虚拟机虚拟机DAO
     */
    @Resource
    private transient VirtualMachineDAO virtualMachineDAO;

    /**
     * 模板DAO
     */
    @Resource
    private TemplateDAO templateDAO;

    /**
     * user DAO
     */
    @Resource
    private transient UserDAO userDAO;

    /**
     * 机型DAO
     */
    @Resource
    private transient MachineTypeDAO machineTypeDAO;
    /**
     * 用户与虚拟机关系DAO
     */
    @Resource
    private transient PortalUserBinVirtualMachineOrderDAO portalUserBinVirtualMachineOrderDAO;

    /**
     * 搜索订单
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<PortalOrderVO> cloudContext) throws Exception {
//        //总数据数
//        cloudContext.getPageInfo().setDataCount(
//                        portalOrderDAO.getQueryCount(cloudContext.getLoginedUser().getDomainID(), cloudContext
//                                        .getLongParam("qTemplate"), cloudContext.getLongParam("qMachineType"),
//                                        cloudContext.getVo().getStatus(), cloudContext.getVo().getApplyTime(),
//                                        cloudContext.getVo().getApplyTimeTo()));
//
//        List<PortalOrderVO> orderVOs = new ArrayList<PortalOrderVO>();
//        //查询数据
//        if (cloudContext.getPageInfo().getDataCount() > 0) {
//            List<PortalOrderEntity> orderEntitys = portalOrderDAO.query(cloudContext.getLoginedUser().getDomainID(),
//                            cloudContext.getLongParam("qTemplate"), cloudContext.getLongParam("qMachineType"),
//                            cloudContext.getVo().getStatus(), cloudContext.getVo().getApplyTime(), cloudContext.getVo()
//                                            .getApplyTimeTo(), cloudContext.getPageInfo());
//            PortalOrderVO portalOrderVO = null;
//            for (PortalOrderEntity tmpEntity : orderEntitys) {
//                portalOrderVO = new PortalOrderVO();
//                BeanUtils.copyProperties(tmpEntity, portalOrderVO);
//                portalOrderVO.setApplicantName(tmpEntity.getApplicant().getRealname());
//                if (tmpEntity.getHandler() != null) {
//                    portalOrderVO.setHandlerName(tmpEntity.getHandler().getRealname());
//                }
//                portalOrderVO.setDomainName(tmpEntity.getDomain().getName());
//                portalOrderVO.setTemplateName(tmpEntity.getTemplate().getName());
//                portalOrderVO.setMachineTypeArgs(tmpEntity.getMachineType().getName().trim() + "|CPU(核)="
//                                + tmpEntity.getMachineType().getCpu() + ",内存(M)="
//                                + tmpEntity.getMachineType().getMemory() + ",硬盘="
//                                + tmpEntity.getMachineType().getDisk());
//                portalOrderVO.setCpu(tmpEntity.getMachineType().getCpu());
//                portalOrderVO.setMemory(tmpEntity.getMachineType().getMemory());
//                portalOrderVO.setDisk(tmpEntity.getMachineType().getDisk());
//                orderVOs.add(portalOrderVO);
//            }
//        }
//        //查询模板
//        List<TemplateEntity> templateEntitys = templateDAO.queryByDomain(cloudContext.getLoginedUser().getDomainID());
//        List<TemplateVO> templateVOs = new ArrayList<TemplateVO>();
//        TemplateVO templateVO = null;
//        for (TemplateEntity templateEntity : templateEntitys) {
//            templateVO = new TemplateVO();
//            BeanUtils.copyProperties(templateEntity, templateVO);
//            templateVOs.add(templateVO);
//        }
//        cloudContext.addParam("template", templateVOs);
//
//        //查询机型
//        List<MachineTypeEntity> machineTypeEntitys = machineTypeDAO.queryByDomain(cloudContext.getLoginedUser()
//                        .getDomainID());
//        List<MachineTypeVO> machineTypeVOs = new ArrayList<MachineTypeVO>();
//        MachineTypeVO machineTypeVO = null;
//        for (MachineTypeEntity machineTypeEntity : machineTypeEntitys) {
//            machineTypeVO = new MachineTypeVO();
//            BeanUtils.copyProperties(machineTypeEntity, machineTypeVO);
//            machineTypeVOs.add(machineTypeVO);
//        }
//        cloudContext.addParam("machineType", machineTypeVOs);
//
//        cloudContext.addParam("order", orderVOs);
    }

    /**
     * 审批通过
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void updateOrderApproved(CloudContext<PortalOrderVO> cloudContext) throws Exception {
//        Long orderId = cloudContext.getLongParam("orderId");
//        UserEntity user = userDAO.get(cloudContext.getLoginedUser().getId());
//        if (user == null) {
//            cloudContext.addErrorMsg("会话超时");
//            return;
//        }
//
//        PortalOrderEntity orderEntity = portalOrderDAO.get(orderId);
//        if (orderEntity == null) {
//            cloudContext.addErrorMsg("订单不存在");
//            return;
//        }
//
//        DomainEntity domainEntity = orderEntity.getDomain();
//        if (domainEntity == null) {
//            cloudContext.addErrorMsg("域不存在");
//            return;
//        }
//
//        TemplateEntity templateEntity = orderEntity.getTemplate();
//        if (templateEntity == null) {
//            cloudContext.addErrorMsg("模板不存在");
//            return;
//        }
//
//        MachineTypeEntity machineTypeEntity = orderEntity.getMachineType();
//        if (machineTypeEntity == null) {
//            cloudContext.addErrorMsg("机型不存在");
//            return;
//        }
//
//        if (!orderEntity.getDomain().getId().equals(cloudContext.getLoginedUser().getDomainID())) {
//            cloudContext.addErrorMsg("非法操作");
//            return;
//        }
//        if (!orderEntity.getStatus().equals(Constant.PORTAL_ORDER_HANDLING)) {
//            cloudContext.addErrorMsg("订单状态不正确");
//            return;
//        }
//
//        String virtualMachineName = orderEntity.getApplicant().getRealname() + "_" + ProjectUtil.createVmName();
//        VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.getByNameAndDomain(virtualMachineName,
//                        domainEntity.getId());
//        if (virtualMachineEntity != null) {
//            cloudContext.addErrorMsg(String.format("【%1$s】域下面的【%2$s】已经存在", domainEntity.getName(), virtualMachineName));
//            return;
//        }
//
//        //域资源验证
//        if (domainEntity.getCpuAvailableNum() < machineTypeEntity.getCpu()) {
//            cloudContext.addErrorMsg("添加失败，当前域可用CPU为：" + domainEntity.getCpuAvailableNum());
//            return;
//        }
//        if (domainEntity.getMemoryAvailableCapacity() < machineTypeEntity.getMemory()) {
//            cloudContext.addErrorMsg("添加失败，当前域可用内存为：" + domainEntity.getMemoryAvailableCapacity());
//            return;
//        }
//        if (domainEntity.getAvailableStorageCapacity() < machineTypeEntity.getDisk()) {
//            cloudContext.addErrorMsg("添加失败，当前域可用存储为：" + domainEntity.getAvailableStorageCapacity());
//            return;
//        }
//
//        //查找资源
//        ComputeResourceEntity computeResourceEntity = matchComputeResource(machineTypeEntity, domainEntity,
//                        cloudContext);
//        if (computeResourceEntity == null) {
//            return;
//        }
//
//        //查找存储
//        StorageResourceEntity storageResourceEntity = storageResourceDAO.getByCapacity(machineTypeEntity.getDisk()
//                        .longValue());
//        if (storageResourceEntity == null) {
//            cloudContext.addErrorMsg("没有符合此配置的存储!");
//            return;
//        }
//
//        //查找网络
//        NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getLongParam("netWork"));
//        if (netWorkEntity == null) {
//            cloudContext.addErrorMsg("没有相应网络!");
//            return;
//        }
//
//        //装载虚拟机参数
//        virtualMachineEntity = new VirtualMachineEntity();
//        virtualMachineEntity.setDomain(domainEntity);
//        virtualMachineEntity.setAddTime(new Date());
//        virtualMachineEntity.setMachineType(machineTypeEntity);
//        virtualMachineEntity.setTemplate(templateEntity);
//        virtualMachineEntity.setComputeResource(computeResourceEntity);
//        virtualMachineEntity.setNetWork(netWorkEntity);
//        virtualMachineEntity.setDesc(cloudContext.getStringParam("desc"));
//        String vmName = ProjectUtil.createVmName();
//        virtualMachineEntity.setVmName(vmName);
//        virtualMachineEntity.setName(virtualMachineName);
//        virtualMachineEntity.setCreateUser(user);
//        virtualMachineEntity.setCreatedFlag(Constant.VM_CREATING);
//        virtualMachineEntity.setCreatedResultMsg("创建中");
//
//        //volumn
//        String volumnName = ProjectUtil.createVolumnName();
//        VolumnEntity volumnEntity = new VolumnEntity();
//        volumnEntity.setName(volumnName);
//        volumnEntity.setAddTime(new Date());
//        volumnEntity.setVirtualMachine(virtualMachineEntity);
//        volumnEntity.setStorageResource(storageResourceEntity);
//        volumnEntity.setSize(machineTypeEntity.getDisk().longValue());
//        volumnEntity.setImageVolumnFlag(Constant.IMAGE_VOLUMN_FALG_TRUE);
//
//        //修改存储大小
//        storageResourceEntity.setAvailableCapacity(storageResourceEntity.getAvailableCapacity()
//                        - volumnEntity.getSize());
//
//        //更新域的资源可用情况
//        Long vmCapacity = machineTypeEntity.getDisk().longValue();
//        domainEntity.setCpuAvailableNum(domainEntity.getCpuAvailableNum() - machineTypeEntity.getCpu());
//        domainEntity.setMemoryAvailableCapacity(domainEntity.getMemoryAvailableCapacity()
//                        - machineTypeEntity.getMemory());
//        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity() - vmCapacity);
//
//        //更新计算节点资源
//        computeResourceEntity.setCpuAvailable(computeResourceEntity.getCpuAvailable() - machineTypeEntity.getCpu());
//        computeResourceEntity.setMemoryAvailable(computeResourceEntity.getMemoryAvailable()
//                        - machineTypeEntity.getMemory());
//
//        //创建用户与虚拟机关系
//        PortalUserBinVirtualMachineEntity userVm = new PortalUserBinVirtualMachineEntity();
//        userVm.setPortalUser(orderEntity.getApplicant());
//        userVm.setVirtualMachine(virtualMachineEntity);
//        //计算过期时间
//        Date dueTime = null;
//        Calendar calender = Calendar.getInstance();
//        if (orderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_NEVERDUE)) {
//            dueTime = null;
//        } else if (orderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_MONTH)) {
//            calender.add(Calendar.DAY_OF_MONTH, 30);
//            dueTime = calender.getTime();
//        } else if (orderEntity.getDueTimeType().equals(Constant.PORTAL_ORDER_DUETIME_TYPE_YEAR)) {
//            calender.add(Calendar.DAY_OF_YEAR, 365);
//            dueTime = calender.getTime();
//        } else {
//            cloudContext.addErrorMsg("订单有效期限不正确");
//            return;
//        }
//        userVm.setDueTime(dueTime);
//
//        //添加订单
//        orderEntity.setHandleMsg(cloudContext.getStringParam("handleMsg"));
//        orderEntity.setStatus(Constant.PORTAL_ORDER_APPROVED);
//        orderEntity.setHandler(user);
//        orderEntity.setHandleTime(new Date());
//
//        domainDAO.update(domainEntity);
//        computeResourceDAO.update(computeResourceEntity);
//        virtualMachineDAO.insert(virtualMachineEntity);
//        volumnDAO.insert(volumnEntity);
//        portalUserBinVirtualMachineOrderDAO.insert(userVm);
//        portalOrderDAO.update(orderEntity);
//        cloudContext.addSuccessMsg("添加成功!");
//
//        //邮件信息
//        Map<String, String> emailInfoMap = getMailInfo(orderEntity, Constant.PORTAL_ORDER_APPROVED,userVm.getDueTime());
//
//        //调用core
//        String imgFilePath = ProjectUtil.getTemplateDir() + File.separator + templateEntity.getDomain().getCode()
//                        + File.separator + templateEntity.getFileName();
//        try {
//            Volume volume = Volume.createVolume(volumnName, storageResourceEntity.getPoolName(), ProjectUtil
//                            .gigaToKByte(volumnEntity.getSize().longValue()));
//            VMUtil.createVirtualMachine(virtualMachineEntity.getId(), virtualMachineEntity.getVmName(),
//                            computeResourceEntity.getCpu(), ProjectUtil.megaToKByte(computeResourceEntity.getMemory()
//                                            .longValue()), volume, imgFilePath, netWorkEntity.getRealname(),
//                            virtualMachineEntity.getComputeResource().getIp(), emailInfoMap);
//
//        } catch (Exception e) {
//            cloudContext.addErrorMsg(e.getMessage());
//            LogUtil.error(e);
//            return;
//        }
    }

    /**
     * 获得邮件信息
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private Map<String, String> getMailInfo(PortalOrderEntity orderEntity, String resultFlag,Date dueTime) {
        Map<String, String> emailInfoMap = new HashMap<String, String>();
//
//        emailInfoMap.put("subject", Constant.PORTAL_VM_EMAIL_SUBJECT);
//        emailInfoMap.put("email", orderEntity.getApplicant().getEmail());
//
//        StringBuilder contentBuilder = new StringBuilder();
//        contentBuilder.append(orderEntity.getApplicant().getRealname());
//        contentBuilder.append(",您好!\r\n");
//        if (resultFlag.equals(Constant.PORTAL_ORDER_APPROVED)) {
//            contentBuilder.append("您在" + orderEntity.getApplyTime() + "申请的虚拟机订单已经通过审核！\r\n");
//            contentBuilder.append("配置如下：");
//            contentBuilder.append("CPU:" + orderEntity.getMachineType().getCpu() + "核,");
//            contentBuilder.append("内存：" + orderEntity.getMachineType().getMemory() + "M,");
//            contentBuilder.append("硬盘：" + orderEntity.getMachineType().getDisk() + "G,");
//            contentBuilder.append("操作系统：" + orderEntity.getTemplate().getName());
//            contentBuilder.append("\r\n有效时间：" + dueTime);
//            contentBuilder.append("\r\n登录用户名：" + orderEntity.getTemplate().getUsername());
//            contentBuilder.append("\r\n登录密码：" + orderEntity.getTemplate().getPassword());
//        } else if (resultFlag.equals(Constant.PORTAL_ORDER_REJECT)) {
//            contentBuilder.append("您在" + orderEntity.getApplyTime() + "申请的虚拟机订单没有通过审核！\r\n");
//            contentBuilder.append("\r\n理由：" + orderEntity.getHandleMsg());
//            contentBuilder.append("\r\n审批人：" + orderEntity.getHandler().getRealname());
//        }
//        emailInfoMap.put("content", contentBuilder.toString());
//
        return emailInfoMap;
    }

    /**
     * 匹配计算节点
     * 
     * @param machineTypeEntity
     * @param domainEntity
     * @param cloudContext
     * @return
     * @throws SQLException
     *             sql异常
     */
    private ComputeResourceEntity matchComputeResource(MachineTypeEntity machineTypeEntity, DomainEntity domainEntity,
                    CloudContext<PortalOrderVO> cloudContext) throws SQLException {
        ComputeResourceEntity computeResourceEntity = null;
        //查看是否选择了自定义
        if (cloudContext.getBooleanParam("customComputeResourceFlag")) {
            computeResourceEntity = computeResourceDAO.get(cloudContext.getLongParam("computeResourceID"));
            if (computeResourceEntity == null) {
                cloudContext.addErrorMsg("添加失败，计算节点不存在！");
                return null;
            }
            //查看CPU和内存信息是否匹配
            if (computeResourceEntity.getCpu() < machineTypeEntity.getCpu()
                            || computeResourceEntity.getCpuAvailable() < machineTypeEntity.getCpu()
                            || computeResourceEntity.getMemory() < machineTypeEntity.getMemory()
                            || computeResourceEntity.getMemoryAvailable() < machineTypeEntity.getMemory()) {
                cloudContext.addErrorMsg("添加失败:" + msg4MaxCpuAndMaxMemoryCompute(domainEntity.getId()));
                return null;
            }
        } else {
            computeResourceEntity = computeResourceDAO.getByCpuAndMemoryAndDomainId(machineTypeEntity.getCpu(),
                            machineTypeEntity.getMemory(), domainEntity.getId());
            if (computeResourceEntity == null) {
                String error = msg4MaxCpuAndMaxMemoryCompute(domainEntity.getId());
                cloudContext.addErrorMsg("添加失败:" + error);
                return null;
            }
        }
        return computeResourceEntity;
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
        return "<br>可设最大CPU的计算节点为" + maxCpuCompute.getName() + "：\\r cpu " + cpu4maxCpuCR + ",内存 " + memory4MaxCpuCR
                        + ";\\r 可设最大内存的计算节点为" + maxMemoryCompute.getName() + "：\\r cpu " + cpu4MaxMemCR + ",内存 "
                        + memory4MaxMemCR;
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<PortalOrderVO> cloudContext) throws Exception {
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
    }

    /**
     * 查询指定资源池下的计算节点，并要求cpu内存参数大于指定值
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryResourceByPoolAndCpuMemory(CloudContext<PortalOrderVO> cloudContext) throws Exception {
        Long poolId = cloudContext.getLongParam("poolId");
        Long orderId = cloudContext.getLongParam("orderId");

        PortalOrderEntity orderEntity = portalOrderDAO.get(orderId);
        if (orderEntity != null) {
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
        } else {
            cloudContext.addErrorMsg("订单不存在");
        }
    }

    /**
     * 查询当前域拥有的资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryResourcePoolByCurrentDomain(CloudContext<PortalOrderVO> cloudContext) throws Exception {
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
     * 审批拒绝
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void updateOrderRejected(CloudContext<PortalOrderVO> cloudContext) throws Exception {
//        Long orderId = cloudContext.getLongParam("orderId");
//        String handlerMsg = cloudContext.getStringParam("handleMsg");
//        UserEntity user = userDAO.get(cloudContext.getLoginedUser().getId());
//        if (user == null) {
//            cloudContext.addErrorMsg("会话超时");
//            return;
//        }
//        PortalOrderEntity order = portalOrderDAO.get(orderId);
//        if (order == null) {
//            cloudContext.addErrorMsg("订单不存在");
//            return;
//        }
//        if (order.getStatus().equals(Constant.PORTAL_ORDER_HANDLING)) {
//            order.setHandleMsg(handlerMsg);
//            order.setHandler(user);
//            order.setHandleTime(new Date());
//            order.setStatus(Constant.PORTAL_ORDER_REJECT);
//            Map<String, String> emailInfoMap = getMailInfo(order, Constant.PORTAL_ORDER_REJECT,null);
//            boolean sendMailFlag = sendRejectMail(emailInfoMap);
//            if(sendMailFlag){
//                portalOrderDAO.update(order);
//            }else{
//                cloudContext.addErrorMsg("邮件发送失败");
//            }
//            
//        } else {
//            cloudContext.addErrorMsg("订单状态不正确");
//        }
    }

    /**
     * 发送邮件
     * 
     * @param vmEntity
     */
    public boolean sendRejectMail(Map<String, String> emailInfoMap) {
        try {
            String subject = emailInfoMap.get("subject");
            String email = emailInfoMap.get("email");
            String content = emailInfoMap.get("content");
            MailUtil.sendMail(subject, content, email);
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
            return false;
        }
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

    private Map<Boolean, String> testCPUOrMemoryPassRate(ComputeResourceEntity computeResourceEntity,
                    MachineTypeEntity machineTypeEntity) throws SQLException {
        Map<Boolean, String> map = new HashMap<Boolean, String>();
        Object[] objs = computeResourceDAO.getSumCPUAndMemory(computeResourceEntity.getId());
        Long sumCpu = ((Long) objs[0] == null) ? 0L : (Long) objs[0];
        Long sumMemory = ((Long) objs[1] == null) ? 0L : (Long) objs[1];
        map.put(true, "");
        return map;
    }

}
