/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.user;

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
import com.cloudking.cloudmanagerweb.LoginedUser;
import com.cloudking.cloudmanagerweb.service.user.UserService;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.UserVO;

/**
 * 用户Action
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/userManager")
@Results( { @Result(name = "loginSuccess", type = "redirect", location = "/dashboardManager/dashboard!query.action"),
                @Result(name = "loginFailed", type = "dispatcher", location = "/login.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "success", type = "dispatcher", location = "/user/user.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/user/user.jsp"),
                @Result(name = "logout", type = "redirect", location = "/login.jsp") })
public class UserAction extends BaseAction<UserVO> {
    /**
     * 
     */
    private static final long serialVersionUID = -3717080712894187487L;
    /**
     * 用户service
     */
    @Resource
    private transient UserService userService;

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/user")
    public String execute() throws Exception {
        // TODO Auto-generated method stub
        return SUCCESS;
    }

    /**
     * 登录
     * 
     * @throws Exception
     *             所有异常
     */
    public String login() throws Exception {
        cloudContext.addParam(Constant.VERIFY_CODE, getSession().getAttribute(Constant.VERIFY_CODE));
        if (StringUtil.isBlank(cloudContext.getVo().getUsername())) {
            cloudContext.addErrorMsg("用户名不能为空");
        }
        if (StringUtil.isBlank(cloudContext.getVo().getPassword())) {
            cloudContext.addErrorMsg("密码不能为空!");
        }
        if (StringUtil.isBlank(cloudContext.getStringParam("checkCode"))) {
            cloudContext.addErrorMsg("验证码不能为空!");
        }
        if (cloudContext.getSuccessIngoreWarn()) {
            userService.login(cloudContext);
        }
        cloudContext.addParam(Constant.LOGINED_USER, cloudContext.getObjectParam(Constant.LOGINED_USER));
        putLoginedUser((LoginedUser) cloudContext.getObjectParam(Constant.LOGINED_USER));
        return cloudContext.getSuccessIngoreWarn() ? "loginSuccess" : "loginFailed";
    }

    /**
     * 登录页域查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String queryDomainForLogin() throws Exception {
        userService.queryDomainForLogin(cloudContext);
        return JSON;
    }

    /**
     * 注销
     * 
     * @throws Exception
     *             所有异常
     */
    public String logout() throws Exception {
        userService.logout(cloudContext);
        getSession().invalidate();
        return "logout";
    }

    /**
     * 查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        userService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加存储
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            userService.insert(cloudContext);
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
        userService.delete(cloudContext);
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
            userService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 修改自身信息
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String updateSelf() throws Exception {
        userService.updateSelf(cloudContext);
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
        userService.initAddOrUpdate(cloudContext);
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
        Boolean isAdd = action.lastIndexOf("add.action") != -1;
        Boolean isUpdate = action.lastIndexOf("update.action") != -1;
        if (isAdd) {
            String username = req.getParameter("cloudContext.vo.username");
            String pwd = req.getParameter("cloudContext.vo.password");
            if (StringUtil.isBlank(username)) {
                cloudContext.addErrorMsg("用户名不能为空! \\r");
            }
            if (StringUtil.isBlank(pwd)) {
                cloudContext.addErrorMsg("密码不能为空! \\r");
            }
        }
        if (isAdd || isUpdate) {
            String realname = req.getParameter("cloudContext.vo.realname");
            if (StringUtil.isBlank(realname)) {
                cloudContext.addErrorMsg("姓名不能为空! \\r");
            }
        }
    }
}
