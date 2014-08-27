/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.machineroom;

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
import com.cloudking.cloudmanagerweb.service.machineroom.MachineRoomService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;

/**
 * 机房Action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/machineRoomManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/machineRoom/machineRoom.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/machineRoom/machineRoom.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/machineRoom/machineRoom.jsp") })
public class MachineRoomAction extends BaseAction<MachineRoomVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 882267718053979554L;
    /**
     * 
     */
    @Resource
    private transient MachineRoomService machineRoomService;

    /**
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/machineRoom")
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
        machineRoomService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加机房
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            machineRoomService.insert(cloudContext);
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
        machineRoomService.delete(cloudContext);
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
            machineRoomService.update(cloudContext);
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
        machineRoomService.initAddOrUpdate(cloudContext);
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
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("机房名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("机房名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
        }
    }

}
