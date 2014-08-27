/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.eventlog;

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
import com.cloudking.cloudmanagerweb.service.eventlog.EventLogService;
import com.cloudking.cloudmanagerweb.vo.EventLogVO;

/**
 * 事件action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/eventLogManager")
@Results( {
                @Result(name = "success", type = "dispatcher", location = "/eventLog/eventLog.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/eventLog/eventLog.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(type = "stream", name = "stream", params = { "contentType", "application/zip", "inputName",
                                "bais", "allowCaching", "false", "encode", "true", "contentDisposition",
                                "attachment;filename=%{#request.downloadFileName}" }) })
public class EventLogAction extends BaseAction<EventLogVO> {

    /**
     * 
     */
    private static final long serialVersionUID = -7295632536726444781L;
    /**
     * eventLogService
     */
    @Resource
    private transient EventLogService eventLogService;

    /**
     * 输出流
     */
    private transient ByteArrayInputStream bais;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/eventLog")
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
        eventLogService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 导出数据
     * 
     * @throws Exception
     *             所有异常
     */
    public String exportData() throws Exception {
        eventLogService.exportData(cloudContext);
        getRequest().setAttribute("downloadFileName", cloudContext.getStringParam("zipName"));
        bais = (ByteArrayInputStream) cloudContext.getObjectParam("baStream");
        return "stream";
    }

    public ByteArrayInputStream getBais() {
        return bais;
    }
}
