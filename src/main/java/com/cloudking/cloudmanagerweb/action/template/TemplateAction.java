/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.template;

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
import com.cloudking.cloudmanagerweb.service.template.TemplateService;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.TemplateVO;

/**
 * 虚机模版Action
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/templateManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/template/template.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/template/template.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/template/template.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "initUpload", type = "dispatcher", location = "/template/upload.jsp")

})
public class TemplateAction extends BaseAction<TemplateVO> {
    /**
     * 
     */
    private static final long serialVersionUID = -3717080712894187487L;
    /**
     * 模版service
     */
    @Resource
    private transient TemplateService templateService;

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/template")
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
        templateService.query(cloudContext);
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
            templateService.insert(cloudContext);
        }
        return JUMP;
    }

    /**
     * 上传文件
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initUpload() throws Exception {
        templateService.initUpload(cloudContext);
        return JSON;
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String delete() throws Exception {
        templateService.delete(cloudContext);
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
            templateService.update(cloudContext);
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
        templateService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 切换域 获取相关数据
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryData4ChangeDomain() throws Exception {
        templateService.queryData4ChangeDomain(cloudContext);
        return JSON;
    }

    /**
     * 查询文件上传表是否存在相同的文件名或者MD5
     * 
     * @throws Exception
     *             所有异常
     */
    public String queryFileExsit() throws Exception {
        templateService.queryFileExsit(cloudContext);
        return JSON;
    }

    /**
     * 删除文件上传表
     * 
     * @throws Exception
     *             所有异常
     */
    public String deleteFileUpload() throws Exception {
        templateService.deleteFileUpload(cloudContext);
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
            String name = req.getParameter("cloudContext.vo.name");
            String type = req.getParameter("cloudContext.vo.type");
            String url = req.getParameter("cloudContext.vo.url");
            String fileName = req.getParameter("cloudContext.vo.fileName");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("镜像名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(type)) {
                cloudContext.addErrorMsg("镜像类型不能为空! \\r");
            } else if (type.equals(Constant.TEMPLATE_TYPE_DOWNLOAD.toString())) {
                if (StringUtil.isBlank(url)) {
                    cloudContext.addErrorMsg("远程镜像rul地址不能为空 \\r");
                }
            } else if (type.equals(Constant.TEMPLATE_TYPE_LOCAL.toString())) {
                if (StringUtil.isBlank(fileName)) {
                    cloudContext.addErrorMsg("必须选择一个镜像");
                }
            }
        }
        if (isAdd || isUpdate) {
            String username = req.getParameter("cloudContext.vo.username");
            String password = req.getParameter("cloudContext.vo.password");
            if (StringUtil.isBlank(username)) {
                cloudContext.addErrorMsg("模版用户名不能为空");
            }
            if (StringUtil.isBlank(password)) {
                cloudContext.addErrorMsg("模版密码不能为空");
            }
        }
    }
}
