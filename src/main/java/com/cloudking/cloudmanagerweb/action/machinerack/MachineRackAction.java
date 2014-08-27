/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.machinerack;

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
import com.cloudking.cloudmanagerweb.service.machinerack.MachineRackService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;

/**
 * 机架Action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/machineRackManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/machineRack/machineRack.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/machineRack/machineRack.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/machineRack/machineRack.jsp") })
public class MachineRackAction extends BaseAction<MachineRackVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 882267718053979554L;
    /**
     * 
     */
    @Resource
    private transient MachineRackService machineRackService;

    /**
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/machineRack")
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
        machineRackService.query(cloudContext);
        setAttrByRequest("qName", cloudContext.getStringParam("qName"));
        setAttrByRequest("qRoom", cloudContext.getStringParam("qRoom"));
        return SUCCESS;
    }

    /**
     * 添加机架
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            machineRackService.insert(cloudContext);
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
        machineRackService.delete(cloudContext);
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
            machineRackService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 初始化新怎或则修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdate() throws Exception {
        machineRackService.initAddOrUpdate(cloudContext);
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
            String roomId = req.getParameter("cloudContext.params.roomID");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("机架名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("机架名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(roomId)) {
                cloudContext.addErrorMsg("请选择机房！");
            }
        }
    }
}
