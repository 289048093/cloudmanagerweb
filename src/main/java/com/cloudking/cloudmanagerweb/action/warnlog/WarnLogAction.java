/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.warnlog;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.warnlog.WarnLogService;
import com.cloudking.cloudmanagerweb.vo.WarnLogVO;

/**
 * 事件action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/warnLogManager")
@Results( {
                @Result(name = "success", type = "dispatcher", location = "/warnLog/warnLog.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/warnLog/warnLog.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(type = "stream", name = "stream", params = { "contentType", "application/zip", "inputName",
                                "bais", "allowCaching", "false", "encode", "true", "contentDisposition",
                                "attachment;filename=%{#request.downloadFileName}" }) })
public class WarnLogAction extends BaseAction<WarnLogVO> {

    /**
     * 
     */
    private static final long serialVersionUID = -7295632536726444781L;
    /**
     * warnLogService
     */
    @Resource
    private transient WarnLogService warnLogService;

    /**
     * 输出流
     */
    private transient ByteArrayInputStream bais;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/warnLog")
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
        warnLogService.query(cloudContext);
        return SUCCESS;
    }
    
    /**
     * 根据机房查找机架，级联操作
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryRackByRoom() throws Exception {
        warnLogService.queryRackByRoom(cloudContext);
        return JSON;
    }
    /**
     * 查找设备
     * @return
     */
    public String querEquipmentByRack(){
        warnLogService.querEquipmentByRack(cloudContext);
        return JSON;
    }

    /**
     * 导出数据
     * 
     * @throws Exception
     *             所有异常
     */
    public String exportData() throws Exception {
        warnLogService.exportData(cloudContext);
        getRequest().setAttribute("downloadFileName", cloudContext.getStringParam("zipName"));
        bais = (ByteArrayInputStream) cloudContext.getObjectParam("baStream");
        return "stream";
    }

    public ByteArrayInputStream getBais() {
        return bais;
    }
}
