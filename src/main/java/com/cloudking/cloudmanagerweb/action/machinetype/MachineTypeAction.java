/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.machinetype;

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
import com.cloudking.cloudmanagerweb.service.machinetype.MachineTypeService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.MachineTypeVO;

/**
 * 配置Action
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/machineTypeManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/machinetype/machinetype.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/machinetype/machinetype.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/machinetype/machinetype.jsp") })
public class MachineTypeAction extends BaseAction<MachineTypeVO> {
    /**
     * 
     */
    private static final long serialVersionUID = -3717080712894187487L;
    /**
     * 配置service
     */
    @Resource
    private transient MachineTypeService machineTypeServicess;

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/machineType")
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
        machineTypeServicess.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加模版
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            machineTypeServicess.insert(cloudContext);
        }
        cloudContext.clearVoAndPageInfo();
        machineTypeServicess.query(cloudContext);
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
        machineTypeServicess.delete(cloudContext);
        cloudContext.clearVoAndPageInfo();
        machineTypeServicess.query(cloudContext);
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
            machineTypeServicess.update(cloudContext);
        }
        cloudContext.clearVoAndPageInfo();
        machineTypeServicess.query(cloudContext);
        return JUMP;
    }

    /**
     * 初始化新怎或则修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdae() throws Exception {
        machineTypeServicess.initAddOrUpdae(cloudContext);
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
        if (isAdd) {
            String name = req.getParameter("cloudContext.vo.name");
            String cpu = req.getParameter("cloudContext.vo.cpu");
            String memory = req.getParameter("cloudContext.vo.memory");
            String disk = req.getParameter("cloudContext.vo.disk");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("镜像名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(cpu)) {
                cloudContext.addErrorMsg("cpu必须选择! \\r");
            }
            if (StringUtil.isBlank(memory)) {
                cloudContext.addErrorMsg("内存必须选择! \\r");
            }
            if (StringUtil.isBlank(disk)) {
                cloudContext.addErrorMsg("硬盘必须选择! \\r");
            }
        }
    }
}
