/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 10, 2012  12:08:31 PM
 */
package com.cloudking.cloudmanagerweb.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;

import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderCategoryDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.PortalOrderEntity;
import com.cloudking.cloudmanagerweb.entity.PortalUserEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderActionEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderCategoryEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderSolutionEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.WorkOrderActionVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * 机型WebService
 * 
 * @author CloudKing
 */
public class PortalService {
    /**
     * portalUserDAO
     */
    @Resource
    PortalUserDAO portalUserDAO;
    /**
     * workOrderDAO
     */
    @Resource
    WorkOrderDAO workOrderDAO;

    /**
     * machineTypeDAO
     */
    @Resource
    MachineTypeDAO machineTypeDAO;

    /**
     * workOrderCategoryDAO
     */
    @Resource
    WorkOrderCategoryDAO workOrderCategoryDAO;
    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * templateDAO
     */
    @Resource
    TemplateDAO templateDAO;

    /**
     * 
     */
    @Resource
    PortalOrderDAO portalOrderDAO;

    /**
     * 添加Portal订单 objects[0] = username; objects[1] = realname; objects[2] = applyMsg; objects[3] = vmRes; 其中 vmRes格式 ：
     * machineType,domain,template|machineType,domain,template
     * 
     * @return
     */
    public String addPortalOrder(String username, String realname, String applyMsg, String vmRes, String dueTimeType,
                    String email) {
        //参数验证
        if (StringUtil.isBlank(username)) {
            return "{success:false,msg:'用户名不存在'}";
        }
        if (StringUtil.isBlank(realname)) {
            return "{success:false,msg:'用户真实姓名不存在'}";
        }
        if (StringUtil.isBlank(vmRes)) {
            return "{success:false,msg:'未选择资源'}";
        }
        if (StringUtil.isBlank(dueTimeType)) {
            return "{success:false,msg:'过期时间错误'}";
        }
        if (StringUtil.isBlank(email)) {
            return "{success:false,msg:'邮箱地址不存在'}";
        }
        try {
            Date currentTime = new Date();
            //创建用户
            String userId = StringUtil.encrypt(username, Constant.PORTAL_USERNAME_SALT); //经过编码后的用户名
            PortalUserEntity portalUserEntity = portalUserDAO.getUserByUserID(userId);
            Boolean newUserFlag = false;
            //如果是新用户就新增一个
            if (portalUserEntity == null) {
                portalUserEntity = new PortalUserEntity();
                portalUserEntity.setAddTime(currentTime);
                portalUserEntity.setRealname(realname);
                portalUserEntity.setUserId(userId);
                portalUserEntity.setEmail(email);
                newUserFlag = true;
            }

            String[] machineTypeArr = null;
            if (vmRes.indexOf("|") > 0) {
                machineTypeArr = vmRes.split("\\|");
            } else {
                machineTypeArr = new String[1];
                machineTypeArr[0] = vmRes;
            }

            PortalOrderEntity portalOrderEntity = null;
            DomainEntity dominEntity = null;
            MachineTypeEntity machineType = null;
            TemplateEntity templateEntity = null;

            //遍历资源套餐，添加一条以上的订单
            for (String tmpStr : machineTypeArr) {
                //新建订单
                portalOrderEntity = new PortalOrderEntity();
                portalOrderEntity.setApplicant(portalUserEntity);
                portalOrderEntity.setDueTimeType(dueTimeType);
                portalOrderEntity.setApplyMsg(applyMsg);
                //资源
                String[] tmpResArr = tmpStr.split(",");

                //机型
                machineType = machineTypeDAO.get(Long.parseLong(tmpResArr[0]));
                if (machineType != null) {
                    portalOrderEntity.setMachineType(machineType);
                } else {
                    return "{success:false,msg:'格式错误'}";
                }

                //域
                dominEntity = domainDAO.get(Long.parseLong(tmpResArr[1]));
                if (dominEntity == null) {
                    return "{success:false,msg:'格式错误'}";
                }

                //模板
                templateEntity = templateDAO.get(Long.parseLong(tmpResArr[2]));
                if (templateEntity != null) {
                    portalOrderEntity.setTemplate(templateEntity);
                } else {
                    return "{success:false,msg:'格式错误'}";
                }

                //创建工单
                WorkOrderCategoryEntity workOrderCatagory = workOrderCategoryDAO
                                .get(Constant.WORK_ORDER_CATEGORY_NEW_VM);
                if (workOrderCatagory == null) {
                    return "{success:false,msg:'网络超时，请联系管理员'}";
                }
                portalOrderDAO.insert(portalOrderEntity);

                WorkOrderEntity workOrder = new WorkOrderEntity();
                workOrder.setSerialNumber(ProjectUtil.createWorkOrderSerialNumber());
                workOrder.setCategory(workOrderCatagory);
                workOrder.setReceiveDomain(dominEntity);
                workOrder.setReceiver(dominEntity.getUser());
                workOrder.setRefId(portalOrderEntity.getId());
                workOrder.setTitle(workOrderCatagory.getName());
                workOrder.setContent("用户：" + realname + "申请配置为：[cpu：" + machineType.getCpu() + "核,内存："
                                + machineType.getMemory() + "M,硬盘：" + machineType.getDisk() + "G]的虚拟机");
                workOrder.setCreateTime(currentTime);
                workOrder.setReportTime(currentTime);
                workOrder.setStatus(Constant.WORKORDER_ACCEPING);

                workOrderDAO.insert(workOrder);
            }
            if (newUserFlag) {
                portalUserDAO.insert(portalUserEntity);
            }
            return "{success:true,msg:'请求已提交,等待审核'}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'请求超时'}";
        }
    }

    /**
     * 查询我的订单
     * 
     * @param username
     *            门户用户唯一身份标识
     * @param start
     *            分页查询起始记录位置
     * @param limit
     *            分布查询终止记录位置
     * @return
     */
    public String queryMyOrder(String username, int start, int limit) {
        try {
            //创建用户
            String userId = StringUtil.encrypt(username, Constant.PORTAL_USERNAME_SALT); //经过编码后的用户名
            //查询总记录数目
            int totalCount = portalOrderDAO.getQueryCountByUserID(userId);
            //查询记录条目
            List<Object[]> orderEntitys = portalOrderDAO.queryByUserID(userId, start, limit);
            List<WorkOrderVO> orderVOs = new ArrayList<WorkOrderVO>();
            WorkOrderVO workOrderVO = null;
            for (Object[] objects : orderEntitys) {
                workOrderVO = new WorkOrderVO();
                workOrderVO.setId(objects[0] == null ? null : Long.parseLong(objects[0].toString()));
                workOrderVO.setSerialNumber(objects[1] == null ? "" : objects[1].toString());
                workOrderVO.setTitle(objects[2] == null ? "" : objects[2].toString());
                workOrderVO.setContent(objects[3] == null ? "" : objects[3].toString());
                workOrderVO.setSolveMsg(objects[4] == null ? "" : objects[4].toString());
                workOrderVO.setCreateTime(objects[5] == null ? null : (Date) objects[5]);
                String status = objects[6] == null ? null : objects[6].toString();
                if (Constant.WORKORDER_ACCEPING.equals(status)) {
                    workOrderVO.setStatus("处理中");
                } else if (Constant.WORKORDER_SOLVED.equals(status)) {
                    workOrderVO.setStatus("已处理");
                } else if (Constant.WORKORDER_CLOSED.equals(status)) {
                    workOrderVO.setStatus("已关闭");
                }
                workOrderVO.setReceiveDomainName(objects[7] == null ? "" : objects[7].toString());
                orderVOs.add(workOrderVO);
            }
            return "{success:true,msg:'',data:" + ProjectUtil.objectToJSON(orderVOs) + ",totalRecord:" + totalCount
                            + "}";

        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'请求超时'}";
        }

    }

    /**
     * 浏览工单
     * 
     * @param workOrderId
     * @return
     */
    public String queryMyOrderView(Long workOrderId) {
        try {

            WorkOrderEntity workOrderEntity = workOrderDAO.get(workOrderId);
            if (workOrderEntity == null) {
                return "{success:false,msg:'工单不存在'}";
            }
            //工单信息
            WorkOrderVO workOrderVO = new WorkOrderVO();
            BeanUtils.copyProperties(workOrderEntity, workOrderVO);
            workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
            workOrderVO.setSolveMsg(workOrderEntity.getSolveMsg()==null?"":workOrderEntity.getSolveMsg());

            if (workOrderEntity.getSendDomain() != null) {
                if (workOrderEntity.getSendDomain().getUser() != null) {
                    workOrderVO.setSendUserName(workOrderEntity.getSendDomain().getUser().getRealname());
                } else {
                    workOrderVO.setSendUserName("系统");
                }
                workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
            } else {
                workOrderVO.setSendDomainName("系统");
            }

            if (workOrderEntity.getReceiveDomain() != null) {
                if (workOrderEntity.getReceiveDomain().getUser() != null) {
                    workOrderVO.setReceiverName(workOrderEntity.getReceiveDomain().getUser().getRealname());
                } else {
                    workOrderVO.setReceiverName("系统");
                }
                workOrderVO.setReceiveDomainName(workOrderEntity.getReceiveDomain().getName());
            } else {
                workOrderVO.setReceiveDomainName("系统");
            }

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
            }else{
                workOrderVO.setSolutionName("");
            }
            return "{success:true,workOrder:" + ProjectUtil.objectToJSON(workOrderVO) + ",workOrderActions:"
                            + ProjectUtil.objectToJSON(workOrderActionVOs) + "}";

        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'请求超时'}";
        }

    }
}
