/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 26, 2012  4:44:04 PM
 */
package com.cloudking.cloudmanagerweb.action.dashboard;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.dashboard.DashboardService;
import com.cloudking.cloudmanagerweb.vo.DashboardVO;

/**
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/dashboardManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/dashboard/dashboard.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/dashboard/dashboard.jsp") })
public class DashboardAction extends BaseAction<DashboardVO> {

    /**
     * 
     */
    private static final long serialVersionUID = -8142985042980860322L;

    /**
     * computeResourcePoolService
     */
    @Resource
    private transient DashboardService dashboardService;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/dashboard")
    public String execute() throws Exception {
        return SUCCESS;
    }

    /**
     * 查询订单与图表数据
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        dashboardService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 分页查询工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryHandlingWorkOrderOnPageChange() throws Exception {
        dashboardService.queryHandlingWorkOrderOnPageChange(cloudContext);
        return JSON;
    }

    /**
     * 域切换查询工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryHandlingWorkOrderOnDomainChange() throws Exception {
        dashboardService.queryHandlingWorkOrderOnDomainChange(cloudContext);
        return JSON;
    }

    /**
     * 系统资源域图表切换
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String querySystemChartOnDomainChange() throws Exception {
        dashboardService.querySystemChartOnDomainChange(cloudContext);
        return JSON;
    }

    /**
     * 计算节点池资源切换
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryComputePoolChartOnPoolChange() throws Exception {
        dashboardService.queryComputePoolChartOnPoolChange(cloudContext);
        return JSON;
    }

    /**
     * 存储节点池资源切换
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryStoragePoolChartOnPoolChange() throws Exception {
        dashboardService.queryStoragePoolChartOnPoolChange(cloudContext);
        return JSON;
    }

}
