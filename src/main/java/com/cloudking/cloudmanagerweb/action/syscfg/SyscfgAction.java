/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.syscfg;

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
import com.cloudking.cloudmanagerweb.service.syscfg.SyscfgService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.PropertyVO;

/**
 * 全局设置action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/syscfgManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/syscfg/syscfg.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/syscfg/syscfg.jsp") })
public class SyscfgAction extends BaseAction<PropertyVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 3483323948900166673L;
    /**
     * syscfgService
     */
    @Resource
    private transient SyscfgService syscfgService;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/syscfg")
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
        syscfgService.query(cloudContext);
        return SUCCESS;
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
            syscfgService.update(cloudContext);
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
        syscfgService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 表单参数验证 (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {
        super.validate();
        HttpServletRequest req = getRequest();
        String action = req.getRequestURI();
        Boolean isAdd = action.lastIndexOf("update.action") != -1;
        if (isAdd) {
            String key = req.getParameter("cloudContext.vo.key");
            String value = req.getParameter("cloudContext.vo.value");
            if (StringUtil.isBlank(key)) {
                cloudContext.addErrorMsg("服务器接收key为空! \\r");
            }
            if (StringUtil.isBlank(value)) {
                cloudContext.addErrorMsg("必须指定值! \\r");
            }
        }
    }
}
