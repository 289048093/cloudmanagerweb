/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.portalorder;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.portalorder.PortalOrderService;
import com.cloudking.cloudmanagerweb.vo.PortalOrderVO;

/**
 * 门户订单Action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/orderManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/portalorder/portalorder.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/portalorder/portalorder.jsp") })
public class PortalOrderAction extends BaseAction<PortalOrderVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 882267718053979554L;
    /**
     * 
     */
    @Resource
    private transient PortalOrderService orderService;

    /**
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/order")
    public String execute() throws Exception {
        // TODO Auto-generated method stub
        return INPUT;
    }

    /**
     * 查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        orderService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 审批拒绝
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String setOrderRejected() throws Exception {
        orderService.updateOrderRejected(cloudContext);
        return JSON;
    }

    /**
     * 查询指定资源池下的计算节点，并要求cpu内存参数大于指定值
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryResourceByPoolAndCpuMemory() throws Exception {
        orderService.queryResourceByPoolAndCpuMemory(cloudContext);
        return JSON;
    }

    /**
     * 查询当前域拥有的资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryResourcePoolByCurrentDomain() throws Exception {
        cloudContext.addParam("domainId", getLoginedUser().getDomainID());
        orderService.queryResourcePoolByCurrentDomain(cloudContext);
        return JSON;
    }

    /**
     * 初始化新增或修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdate() throws Exception {
        orderService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 审批通过
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String setOrderApproved() throws Exception {
        orderService.updateOrderApproved(cloudContext);
        return JSON;
    }

}
