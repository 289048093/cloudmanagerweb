/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  2012-12-27  下午3:12:18
 */
package com.cloudking.cloudmanagerweb.action.backupstorage;

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
import com.cloudking.cloudmanagerweb.service.backupstorage.BackupStorageService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.BackupStorageVO;

/**
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/backupStorageManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/backupStorage/backupStorage.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/backupStorage/backupStorage.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/backupStorage/backupStorage.jsp") })
public class BackupStorageAction extends BaseAction<BackupStorageVO> {
    /**
     * 
     */
    private static final long serialVersionUID = 882267718053979554L;
    /**
     * 
     */
    @Resource
    private transient BackupStorageService backupStorageService;

    /**
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/backupStorage")
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
        backupStorageService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加资源
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            backupStorageService.insert(cloudContext);
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
        backupStorageService.delete(cloudContext);
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
            backupStorageService.update(cloudContext);
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
        backupStorageService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryRackByRoom() throws Exception {
        backupStorageService.queryRackByRoom(cloudContext);
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
            String ip = req.getParameter("cloudContext.vo.ip");
            String ipRegx = "^(((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3})(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$";
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("存储名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(ip)) {
                cloudContext.addErrorMsg("ip不能为空! \\r");
            } else if (!ip.matches(ipRegx)) {
                cloudContext.addErrorMsg("ip格式错误! \\r");
            }
            String rackId = req.getParameter("cloudContext.params.rackID");
            if (StringUtil.isBlank(rackId)) {
                cloudContext.addErrorMsg("机架必须选择! \\r");
            }
        }
    }
}
