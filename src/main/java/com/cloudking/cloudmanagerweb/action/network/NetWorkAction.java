/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.network;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.network.NetWorkService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.NetWorkVO;

/**
 * 网络action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/netWorkManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/netWork/netWork.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/netWork/netWork.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/netWork/netWork.jsp") })
public class NetWorkAction extends BaseAction<NetWorkVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 3483323948900166673L;
    /**
     * netWorkService
     */
    @Resource
    private transient NetWorkService netWorkService;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/netWork")
    public String execute() throws Exception {
        return SUCCESS;
    }

    /**
     * 查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        netWorkService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加网络
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            netWorkService.insert(cloudContext);
        }
        return JUMP;
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String delete() throws Exception {
        netWorkService.delete(cloudContext);
        return JUMP;
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String update() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            netWorkService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 初始化新增或则修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdate() throws Exception {
        netWorkService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 验证新增货修改的数据 (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {
        super.validate();
        HttpServletRequest req = getRequest();
        String action = req.getRequestURI();
        Boolean isAdd = action.lastIndexOf("add.action") != -1;
        if (isAdd) {
            String cidrRegx = "^(((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3})(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\/(3[0-2]|[1-2]\\d|[1-9])$";
            String ipRegx = "^(((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3})(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";
            String name = req.getParameter("cloudContext.vo.name");
            String cidr = req.getParameter("cloudContext.vo.cidr");
            String startIp = req.getParameter("cloudContext.vo.startIP");
            String endIp = req.getParameter("cloudContext.vo.endIP");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("网络名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("网络名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(cidr)) {
                cloudContext.addErrorMsg("网段不能为空! \\r");
            } else if (!cidr.matches(cidrRegx)) {
                cloudContext.addErrorMsg("网段格式不正确! \\r");
            }
            if (StringUtil.isBlank(startIp)) {
                cloudContext.addErrorMsg("起始IP不能为空! \\r");
            } else if (!startIp.matches(ipRegx)) {
                cloudContext.addErrorMsg("起始IP格式不正确! \\r");
            }
            if (StringUtil.isBlank(endIp)) {
                cloudContext.addErrorMsg("结束IP不能为空! \\r");
            } else if (!endIp.matches(ipRegx)) {
                cloudContext.addErrorMsg("结束IP格式不正确! \\r");
            }
        }
    }
}
