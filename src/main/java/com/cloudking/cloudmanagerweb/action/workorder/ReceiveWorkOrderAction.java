/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.workorder;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.workorder.ReceiveWorkOrderService;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * vs
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@Namespace("/receiveWorkorderManager")
@Results( { @Result(name = "printDetailView", type = "dispatcher", location = "/workorder/workOrderView_.jsp"),
    @Result(name = "receiveWorkOrderView", type = "dispatcher", location = "/workorder/receiveWorkOrder.jsp")
})
/**
 * WorkOrderAction
 */
public class ReceiveWorkOrderAction extends BaseAction<WorkOrderVO> {

    /**
     * recevieWorkOrderService
     */
    @Resource
    private transient ReceiveWorkOrderService recevieWorkOrderService;

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/receiveWorkorder")
    public String execute() throws Exception {
        return INPUT;
    }

    /**
     * 虚拟机创建工单初始化数据
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdateForVm() throws Exception {
        recevieWorkOrderService.initAddOrUpdateForVm(cloudContext);
        return JSON;
    }
    
    /**
     * 工单-同意删除虚拟机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String deleteVirtualMachine() throws Exception {
        recevieWorkOrderService.deleteVirtualMachine(cloudContext);
        return JSON;
    }
    
    /**
     * 工单-拒绝删除虚拟机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String rejectDelVirtualMachine() throws Exception {
        recevieWorkOrderService.insertRejectDelVirtualMachine(cloudContext);
        return JSON;
    }
    
    /**
     * 工单-关闭删除虚拟机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String closeDelVirtualMachine() throws Exception {
        recevieWorkOrderService.insertCloseDelVirtualMachine(cloudContext);
        return JSON;
    }

    /**
     * 工单-虚拟机新建
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String newVirtualMachine() throws Exception {
        recevieWorkOrderService.insertVirtualMachine(cloudContext);
        return JSON;
    }

    /**
     * 工单-虚拟机申请拒绝
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String rejectNewVirtualMachine() throws Exception {
        recevieWorkOrderService.insertRejectNewVirtualMachine(cloudContext);
        return JSON;
    }

    /**
     * 工单-虚拟机申请关闭
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String closeNewVirtualMachine() throws Exception {
        recevieWorkOrderService.insertCloseNewVirtualMachine(cloudContext);
        return JSON;
    }
    
    /**
     * 工单-解决普通工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String updateCommonWorkOrderSovle() throws Exception {
        recevieWorkOrderService.updateCommonWorkOrderSovle(cloudContext);
        return JSON;
    }
    
    /**
     * 工单-关闭普通工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String updateCommonWorkOrderClose() throws Exception {
        recevieWorkOrderService.updateCommonWorkOrderClose(cloudContext);
        return JSON;
    }
    
    /**
     * 创建虚拟机手动选择计算节时匹配合适节点
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryResourceByPoolAndCpuMemory() throws Exception {
        recevieWorkOrderService.queryResourceByPoolAndCpuMemory(cloudContext);
        return JSON;
    }

    /**
     * 查看工单详细信息
     * 
     * @return
     * @throws Exception
     *             e
     */
    public String view() throws Exception {
        cloudContext.addParam("workOrderId", getRequest().getParameter("workOrderId"));
        recevieWorkOrderService.queryWorkOrderDetail(cloudContext);
        setAttrByRequest("cloudContext", cloudContext);
        return "printDetailView";
    }

    /**
     * 查询我接收的工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryReceiveWorkOrder() throws Exception {
        recevieWorkOrderService.queryReceiveWorkOrder(cloudContext);
        return "receiveWorkOrderView";
    }
    
    /**
     * 同意资源申请单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String insertResourceOrderApprove() throws Exception {
        recevieWorkOrderService.insertResourceOrderApprove(cloudContext);
        return JSON;
    }
    
    /**
     * 拒绝资源申请单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String insertResourceOrderReject() throws Exception {
        recevieWorkOrderService.insertResourceOrderReject(cloudContext);
        return JSON;
    }
    
    /**
     * 关闭资源申请单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String insertResourceOrderClose() throws Exception {
        recevieWorkOrderService.insertResourceOrderClose(cloudContext);
        return JSON;
    }

}
