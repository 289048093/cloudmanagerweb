/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.workorder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.ResourceOrderDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderActionDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderAttachmentDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderCategoryDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderSolutionDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.ResourceOrderEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderActionEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderAttachmentEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderCategoryEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderSolutionEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.ResourceOrderVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderAttachmentVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderCategoryVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * WorkOrderService
 * 
 * @author CloudKing
 */
@Service("sendWorkOrderService")
public class SendWorkOrderService extends BaseService {

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
     * resourceOrderDAO
     */
    @Resource
    private ResourceOrderDAO resourceOrderDAO;

    /**
     * workOrderAttachmentDAO
     */
    @Resource
    private WorkOrderAttachmentDAO workOrderAttachmentDAO;
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
     * workOrderDAO
     */
    @Resource
    private WorkOrderDAO workOrderDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * recevieWorkOrderService
     */
    @Resource
    private ReceiveWorkOrderService recevieWorkOrderService;

    /**
     * 查询我发送的工单
     * 
     * @param context
     * @throws Exception
     *             e
     */
    public void querySendWorkOrder(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long domainId = cloudContext.getLongParam("qDomain");
        if (domainId == null) {
            querySendWorkOrderInAllDomain(cloudContext);
            return;
        }
        Long sendUserId = cloudContext.getLoginedUser().getId();
        if (sendUserId == null) {
            cloudContext.addErrorMsg("登录超时");
            return;
        }
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        workOrderDAO.querySendWorkOrder(cloudContext.getStringParam("qTitle"), cloudContext
                                        .getLongParam("qCategory"), startDate, endDate, domainId, cloudContext.getLongParam("qReceiveDomain")));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objects = workOrderDAO.querySendWorkOrder(cloudContext.getStringParam("qTitle"),
                            cloudContext.getLongParam("qCategory"), startDate, endDate, domainId, cloudContext.getLongParam("qReceiveDomain"), cloudContext
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
                workOrderVO.setReceiveDomainName(workOrderEntity.getReceiveDomain().getName());
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
        cloudContext.addParam("receiveDomains", obtainDescendantDomainByUserId(cloudContext.getLoginedUser().getId()));
        cloudContext.addParam("categorys", woCategoryVOs);
        cloudContext.addParam("workOrders", workOrderVOs);
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
     * 用户所有域中查询我发送的工单
     * 
     * @param context
     * @throws Exception
     *             e
     */
    private void querySendWorkOrderInAllDomain(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        Long sendUserId = cloudContext.getLoginedUser().getId();
        if (sendUserId == null) {
            cloudContext.addErrorMsg("登录超时");
            return;
        }
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        workOrderDAO.querySendWorkOrderInAllDoaminsCount(cloudContext.getStringParam("qTitle"),
                                        cloudContext.getLongParam("qCategory"), startDate, endDate, domainIds,cloudContext.getLongParam("qReceiveDomain")));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objects = workOrderDAO.querySendWorkOrderInAllDoamins(cloudContext.getStringParam("qTitle"),
                            cloudContext.getLongParam("qCategory"), startDate, endDate, domainIds,cloudContext.getLongParam("qReceiveDomain"), cloudContext
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
                workOrderVO.setReceiveDomainName(workOrderEntity.getReceiveDomain().getName());
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
        cloudContext.addParam("receiveDomains", obtainDescendantDomainByUserId(cloudContext.getLoginedUser().getId()));
        cloudContext.addParam("categorys", woCategoryVOs);
        cloudContext.addParam("workOrders", workOrderVOs);
    }

    /**
     * 创建资源申请单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public void insertResourceOrder(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        if (Constant.ROOT_DOMAIN_CODE.equals(cloudContext.getLoginedUser().getDomainCode())) {
            cloudContext.addErrorMsg("根域不能创建资源申请单");
            return;
        }
        DomainEntity sendDomain = domainDAO.load(cloudContext.getLongParam("sendDomain"));
        if (sendDomain == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }
        DomainEntity receiveDomain = domainDAO.findByCode(cloudContext.getLoginedUser().getDomainCode().substring(0,
                        cloudContext.getLoginedUser().getDomainCode().length() - 2));
        if (receiveDomain == null) {
            cloudContext.addErrorMsg("上级域不存在");
            return;
        }
        Long storageCapacity = cloudContext.getLongParam("storageCapacity") == null ? 0L : cloudContext
                        .getLongParam("storageCapacity");
        Integer cpu = cloudContext.getIntegerParam("cpu") == null ? 0 : cloudContext.getIntegerParam("cpu");
        Integer memory = cloudContext.getIntegerParam("memory") == null ? 0 : cloudContext.getIntegerParam("memory");

        String title = cloudContext.getStringParam("title");
        String content = cloudContext.getStringParam("content");

        if (storageCapacity != null && storageCapacity.equals(0L) && cpu.equals(0) && memory.equals(0)) {
            cloudContext.addErrorMsg("CPU，内存，存储必需有一项不为0");
            return;
        }

        ResourceOrderEntity resourceOrderEntity = new ResourceOrderEntity();
        //普通字段拷贝
        ResourceOrderVO resourceOrderVO = new ResourceOrderVO();
        resourceOrderVO.setTitle(title);
        resourceOrderVO.setStorageCapacity(storageCapacity);
        resourceOrderVO.setCpu(cpu);
        resourceOrderVO.setMemory(memory);
        resourceOrderVO.setContent(content);

        BeanUtils.copyProperties(resourceOrderVO, resourceOrderEntity);
        Date date = new Date();
        //创建时间
        resourceOrderEntity.setCreateTime(date);
        // 工单状态
        resourceOrderEntity.setStatus(Constant.RESOURCEORDER_ACCEPING);
        // 流水号
        resourceOrderEntity.setSerialNumber(DateUtil.format(date, Constant.RESOURCEORDER_SERIALIZABLE_NUMBER));
        //更新时间
        resourceOrderEntity.setUpdateTime(date);
        resourceOrderDAO.insert(resourceOrderEntity);

        //创建工单
        WorkOrderCategoryEntity workOrderCatagory = workOrderCategoryDAO
                        .get(Constant.WORK_ORDER_CATEGORY_NEW_RESOURCEORDER);
        if (workOrderCatagory == null) {
            cloudContext.addErrorMsg("工单类别不存在");
            return;
        }

        WorkOrderEntity workOrder = new WorkOrderEntity();
        workOrder.setSerialNumber(ProjectUtil.createWorkOrderSerialNumber());
        workOrder.setCategory(workOrderCatagory);
        workOrder.setReceiveDomain(receiveDomain);
        workOrder.setReceiver(receiveDomain.getUser());
        workOrder.setSendDomain(sendDomain);
        workOrder.setSendUser(sendDomain.getUser());
        workOrder.setRefId(resourceOrderEntity.getId());
        workOrder.setTitle(title);
        workOrder.setContent("用户：" + cloudContext.getLoginedUser().getRealname() + "向域:" + receiveDomain.getName()
                        + " 申请配置为：[cpu：" + (cpu == null ? 0 : cpu) + "核,内存：" + (memory == null ? 0 : memory) + "M,存储："
                        + (storageCapacity == null ? 0L : storageCapacity) + "G]的资源申请单");
        workOrder.setCreateTime(date);
        workOrder.setUpdateTime(date);
        workOrder.setReportTime(date);
        workOrder.setStatus(Constant.WORKORDER_ACCEPING);
        workOrderDAO.insert(workOrder);

        cloudContext.addErrorMsg("申请单已提交");
    }

    /**
     * 初始化新增普通工单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void initCommonOrderInfo(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        cloudContext.addParam("domains", obtainDescendantDomainByUserId(cloudContext.getLoginedUser().getId()));
        cloudContext.addParam("currentDomains", obtainDomainsByUserId(cloudContext.getLoginedUser().getId()));
    }

    /**
     * 获取后代域集合
     * 
     * @param userId
     * @return
     * @throws SQLException
     *             sql异常
     */
    private List<DomainVO> obtainDescendantDomainByUserId(Long userId) throws SQLException {
        List<DomainEntity> domainEntitys = domainDAO.queryDescendantDomainByUserId(userId);
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity domainEntity : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            domainVOs.add(domainVO);
        }
        return domainVOs;
    }

    /**
     * 加载附件
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void initAttachments(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        List<WorkOrderAttachmentEntity> woAttachments = workOrderAttachmentDAO
                        .queryAttachmentsByWorkOrderId(workOrderId);

        List<WorkOrderAttachmentVO> workOrderAttachmentVOs = new ArrayList<WorkOrderAttachmentVO>();
        WorkOrderAttachmentVO workOrderAttachmentVO = null;
        for (WorkOrderAttachmentEntity woaEntity : woAttachments) {
            workOrderAttachmentVO = new WorkOrderAttachmentVO();
            BeanUtils.copyProperties(woaEntity, workOrderAttachmentVO);
            workOrderAttachmentVO.setWorkOrderStatus(woaEntity.getWorkOrder().getStatus());
            workOrderAttachmentVOs.add(workOrderAttachmentVO);
        }
        cloudContext.addParam("attachements", workOrderAttachmentVOs);
    }

    /**
     * 获得可创建的工单类别
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void queryWorkOrderCategoryForSend(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        List<WorkOrderCategoryEntity> categorys = new ArrayList<WorkOrderCategoryEntity>();
        if (cloudContext.getLoginedUser().getUsername().equals(Constant.ADMINISTRATOR)) {
            categorys.add(workOrderCategoryDAO.get(Constant.WORK_ORDER_CATEGORY_COMMON));
        } else {
            categorys = workOrderCategoryDAO.listWorkOrderCategoryForSend();
        }
        List<WorkOrderCategoryVO> categoryVOs = new ArrayList<WorkOrderCategoryVO>();
        WorkOrderCategoryVO categoryVO = null;
        for (WorkOrderCategoryEntity category : categorys) {
            categoryVO = new WorkOrderCategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVOs.add(categoryVO);
        }
        cloudContext.addParam("categorys", categoryVOs);
    }

    /**
     * 添加普通工单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertCommonWorkOrder(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long receiveDomainId = cloudContext.getLongParam("receiveDomain");
        DomainEntity receiveDomain = domainDAO.getExistUserDomain(receiveDomainId);
        if (receiveDomain == null) {
            cloudContext.addErrorMsg("接收域不存在或没有域管理员");
            return;
        }

        DomainEntity sendDomainEntity = domainDAO.get(cloudContext.getLongParam("sendDomain"));
        if (sendDomainEntity == null) {
            cloudContext.addErrorMsg("登录超时，请重新登录");
            return;
        }

        WorkOrderCategoryEntity workOrderCatagory = workOrderCategoryDAO.get(Constant.WORK_ORDER_CATEGORY_COMMON);
        if (workOrderCatagory == null) {
            cloudContext.addErrorMsg("工单类别不存在");
            return;
        }

        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("登录超时");
            return;
        }

        WorkOrderVO workOrderVO = cloudContext.getVo();

        WorkOrderEntity workOrder = new WorkOrderEntity();
        workOrder.setSerialNumber(ProjectUtil.createWorkOrderSerialNumber());
        workOrder.setCategory(workOrderCatagory);
        workOrder.setReceiveDomain(receiveDomain);
        workOrder.setReceiver(receiveDomain.getUser());
        workOrder.setSendDomain(sendDomainEntity);
        workOrder.setSendUser(sendDomainEntity.getUser());
        workOrder.setTitle(workOrderVO.getTitle());
        workOrder.setContent(workOrderVO.getContent());
        workOrder.setDueDate(workOrderVO.getDueDate());
        Date date = new Date();
        workOrder.setCreateTime(date);
        workOrder.setReportTime(date);
        workOrder.setCreateTime(date);
        workOrder.setStatus(Constant.WORKORDER_ACCEPING);
        workOrderDAO.insert(workOrder);

        //上传附件
        if (!StringUtil.isBlank(cloudContext.getStringParam("filename"))) {
            boolean flag = saveUploadExtraFile(workOrder, userEntity, cloudContext);
            if (!flag) {
                cloudContext.addErrorMsg("文件上传失败");
            } else {
                cloudContext.addSuccessMsg("操作成功");
            }
        } else {
            cloudContext.addSuccessMsg("操作成功");
        }
    }

    /**
     * 上传附件
     * 
     * @param cloudContext
     * @throws Exception
     *             e
     */
    public boolean saveUploadExtraFile(WorkOrderEntity workOrder, UserEntity userEntity,
                    CloudContext<WorkOrderVO> cloudContext) throws Exception {
        boolean flag = false;
        File extraFile = (File) cloudContext.getObjectParam("file");
        String fileName = cloudContext.getStringParam("filename");

        WorkOrderAttachmentEntity attachmentEntity = workOrderAttachmentDAO.queryAttachementByWorkOrderIdAndFileName(
                        workOrder.getId(), fileName);
        if (attachmentEntity != null) {
            cloudContext.addErrorMsg("文件已经存在");
            return flag;
        }

        //创建附件文件
        String suffix = ProjectUtil.getFileSubffix(fileName);
        File targetFile = ProjectUtil.genrateAttachmentFile(workOrder.getSerialNumber(), suffix);
        String contentType = cloudContext.getStringParam("contentType");
        FileUtils.copyFile(extraFile, targetFile);

        try {
            //保存文件信息
            attachmentEntity = new WorkOrderAttachmentEntity();
            Date date = new Date();
            attachmentEntity.setCreateTime(date);
            attachmentEntity.setCreateUser(userEntity);
            attachmentEntity.setFileName(fileName);
            attachmentEntity.setFileSize((int) extraFile.length());
            attachmentEntity.setMimeType(contentType);
            attachmentEntity.setUuidName(targetFile.getName());
            attachmentEntity.setWorkOrder(workOrder);
            workOrderAttachmentDAO.insert(attachmentEntity);
            flag = true;
        } catch (Exception e) {
            targetFile.delete();//如果保存信息失败，删除文件
            throw e;
        }
        return flag;
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

        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }

        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("会话超时");
            return;
        }

        ResourceOrderEntity resourceOrderEntity = resourceOrderDAO.get(workOrderEntity.getRefId());
        if (resourceOrderEntity == null) {
            cloudContext.addErrorMsg("资源申请单不存在!");
            return;
        }
        //解决情况
        WorkOrderSolutionEntity workOrderSolutionEntity = workOrderSolutionDAO.get(solutionId);
        if (workOrderSolutionEntity == null) {
            cloudContext.addErrorMsg("没有相应解决情况!");
            return;
        }

        if (workOrderEntity.getStatus().equals(Constant.WORKORDER_CLOSED)) {
            cloudContext.addErrorMsg("工单状态不正确");
            return;
        }

        //更新工单状态
        Date currentTime = new Date();
        workOrderEntity.setStatus(Constant.WORKORDER_CLOSED);
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setReceiver(userEntity);
        workOrderEntity.setSolveMsg(handleMsg);
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        workOrderEntity.setCloseTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction("关闭资源申请单工单");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】执行关闭操作，理由：" + handleMsg);
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);

        workOrderActionDAO.insert(workActionEntity);
        workOrderDAO.update(workOrderEntity);
        cloudContext.addSuccessMsg("操作成功!");
    }

    /**
     * 关闭普通工单
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insertCommonWorkOrderClose(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        Long workOrderId = cloudContext.getLongParam("workOrderId");
        Long solutionId = cloudContext.getLongParam("workOrderSolutions");
        String handleMsg = cloudContext.getStringParam("handleMsg");

        WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
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

        if (workOrderEntity.getStatus().equals(Constant.WORKORDER_CLOSED)) {
            cloudContext.addErrorMsg("工单状态不正确");
            return;
        }

        //更新工单状态
        Date currentTime = new Date();
        workOrderEntity.setStatus(Constant.WORKORDER_CLOSED);
        workOrderEntity.setUpdateTime(currentTime);
        workOrderEntity.setReceiver(userEntity);
        workOrderEntity.setSolveMsg(handleMsg);
        workOrderEntity.setSolution(workOrderSolutionEntity);
        workOrderEntity.setSolutionTime(currentTime);
        workOrderEntity.setCloseTime(currentTime);
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction("关闭日常工单");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(currentTime);
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity.setContent("用户：【" + userEntity.getRealname() + "】执行关闭操作，理由：" + handleMsg);
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);

        workOrderActionDAO.insert(workActionEntity);
        workOrderDAO.update(workOrderEntity);
        cloudContext.addSuccessMsg("操作成功!");
    }

    /**
     * 查看工单详细信息
     * 
     * @param context
     * @throws Exception
     *             e
     */
    public void queryWorkOrderDetail(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        recevieWorkOrderService.queryWorkOrderDetail(cloudContext);
    }

    /**
     * 下载附件
     * 
     * @param cloudContext
     * @throws Exception
     *             e
     */
    public void downloadWorkOrderAttachment(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        WorkOrderAttachmentEntity attachmentEntity = workOrderAttachmentDAO.get(cloudContext
                        .getLongParam("attachmentId"));
        File tmpAttachment = ProjectUtil.getAttachmentFile(attachmentEntity.getWorkOrder().getSerialNumber(),
                        attachmentEntity.getUuidName());
        File attachment = new File(attachmentEntity.getFileName());
        FileUtils.copyFile(tmpAttachment, attachment);
        cloudContext.addParam("attachment", attachment);
    }

    /**
     * 删除附件
     * 
     * @param cloudContext
     * @throws Exception
     *             e
     */
    public void deleteAttachment(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        WorkOrderAttachmentEntity attachmentEntity = workOrderAttachmentDAO.get(cloudContext
                        .getLongParam("attachmentId"));
        if (attachmentEntity == null) {
            cloudContext.addErrorMsg("附件不存在");
            return;
        }
        UserEntity userEntity = userDAO.get(cloudContext.getLoginedUser().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("登录超时");
            return;
        }

        WorkOrderEntity workOrderEntity = attachmentEntity.getWorkOrder();
        if (workOrderEntity == null) {
            cloudContext.addErrorMsg("工单不存在");
            return;
        }
        //添加动作
        List<WorkOrderActionEntity> actions = workOrderEntity.getWorkOrderActions();
        WorkOrderActionEntity workActionEntity = new WorkOrderActionEntity();
        workActionEntity.setAction("删除附件");
        workActionEntity.setActionUser(userEntity);
        workActionEntity.setCreateTime(new Date());
        workActionEntity.setWorkOrder(workOrderEntity);
        workActionEntity
                        .setContent("用户：【" + userEntity.getRealname() + "】删除附件【" + attachmentEntity.getFileName() + "】");
        actions.add(workActionEntity);
        workOrderEntity.setWorkOrderActions(actions);

        workOrderActionDAO.insert(workActionEntity);
        workOrderAttachmentDAO.delete(attachmentEntity);
        cloudContext.addSuccessMsg("删除成功");
    }

    /**
     * 初始化新增资源申请工单
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initApplyResourceWorkerOrder(CloudContext<WorkOrderVO> cloudContext) throws Exception {
        cloudContext.addParam("currentDomains", obtainDomainsByUserId(cloudContext.getLoginedUser().getId()));
    }

    /**
     * 获取用户的域
     * 
     * @param cloudContext
     * @return
     * @throws SQLException
     *  SQL异常
     */
    private List<DomainVO> obtainDomainsByUserId(Long userId) throws SQLException {
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(userId);
        List<DomainVO> currentDomainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            currentDomainVOs.add(domainVO);
        }
        return currentDomainVOs;
    }
}
