/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.workorder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.workorder.SendWorkOrderService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * vs
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@Namespace("/sendWorkorderManager")
@Results( { @Result(name = "printDetailView", type = "dispatcher", location = "/workorder/workOrderView_.jsp"),
            @Result(name = "sendWorkOrderView", type = "dispatcher", location = "/workorder/sendWorkOrder.jsp"),
            @Result(type = "stream", name = "downloadAttachment", params = { "inputName", "attachment", "contentType",
                            "application/octet-stream", "encode", "true", "contentDisposition",
                            "attachment;filename=%{#request.downloadFileName}" })})
/**
 * WorkOrderAction
 */
public class SendWorkOrderAction extends BaseAction<WorkOrderVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * attachment
     */
    private transient InputStream attachment;

    /**
     * file
     */
    private File file;
    /**
     * contentType
     */
    private String contentType;
    /**
     * filename
     */
    private String filename;
    
    /**
     * sendWorkOrderService
     */
    @Resource
    private transient SendWorkOrderService sendWorkOrderService;

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/sendWorkorder")
    public String execute() throws Exception {
        return INPUT;
    }

    /**
     * 查询我发送的工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String querySendWorkOrder() throws Exception {
        sendWorkOrderService.querySendWorkOrder(cloudContext);
        return "sendWorkOrderView";
    }

    /**
     * 创建资源申请单工单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public String addResourceOrder() throws Exception {
        sendWorkOrderService.insertResourceOrder(cloudContext);
        cloudContext.clearVoAndPageInfo();
        sendWorkOrderService.querySendWorkOrder(cloudContext);
        return "sendWorkOrderView";
    }

    /**
     * 初始化新建工单信息
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public String initCommonOrderInfo() throws Exception {
        sendWorkOrderService.initCommonOrderInfo(cloudContext);
        return JSON;
    }
    
    /**
     * 加载附件
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public String initAttachments() throws Exception {
        sendWorkOrderService.initAttachments(cloudContext);
        return JSON;
    }
    
    
    
    /**
     * 获得可创建的工单类别
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public String queryWorkOrderCategoryForSend() throws Exception {
        sendWorkOrderService.queryWorkOrderCategoryForSend(cloudContext);
        return JSON;
    }

    /**
     * 添加普通工单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public String insertCommonWorkOrder() throws Exception {
        getResponse().setContentType("text/html;charset=utf-8");
        if (!StringUtil.isBlank(cloudContext.getStringParam("filename")) && file == null) {
            getResponse().getWriter().println("{'success':false,'msg':'附件太大，工单添加失败！'}");
            return NONE;
        }
        Long loginedUserId = getLoginedUser().getId();
        cloudContext.addParam("_loginedUserId", loginedUserId.toString());
        if (file != null) {
            cloudContext.addParam("file", file);
            cloudContext.addParam("filename", filename);
            cloudContext.addParam("contentType", contentType);
        }

        sendWorkOrderService.insertCommonWorkOrder(cloudContext);
        cloudContext.clearVoAndPageInfo();
        sendWorkOrderService.querySendWorkOrder(cloudContext);
        return "sendWorkOrderView";
    }

    /**
     * 关闭资源申请单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String insertResourceOrderClose() throws Exception {
        sendWorkOrderService.insertResourceOrderClose(cloudContext);
        return JSON;
    }

    /**
     * 关闭普通工单
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String insertCommonWorkOrderClose() throws Exception {
        sendWorkOrderService.insertCommonWorkOrderClose(cloudContext);
        return JSON;
    }

    /**
     * 查看工单详细信息
     * 
     * @return
     * @throws Exception
     *             e
     */
    public String view() throws Exception {
        cloudContext.addParam("workOrderId", getRequest().getParameter("workOrderId"));
        sendWorkOrderService.queryWorkOrderDetail(cloudContext);
        setAttrByRequest("cloudContext", cloudContext);
        return "printDetailView";
    }
    
    /**
     * 下载附件
     * 
     * @return
     * @throws Exception e
     */
    public String downloadWorkOrderAttachment() throws Exception {
        sendWorkOrderService.downloadWorkOrderAttachment(cloudContext);
        File attachmentFile = (File) cloudContext.getObjectParam("attachment");
        setAttrByRequest("downloadFileName", attachmentFile.getName());
        attachment = new BufferedInputStream(new FileInputStream(attachmentFile));
        return "downloadAttachment";
    }
    
    /**
     * 初始化申请资源工单
     * @return
     * @throws Exception 所有异常
     */
    public String initApplyResourceWorkerOrder() throws Exception{
        sendWorkOrderService.initApplyResourceWorkerOrder(cloudContext);
        return JSON;
    }
    
    /**
     * 删除附件
     * 
     * @return
     * @throws Exception e
     */
    public String deleteAttachment() throws Exception {
        sendWorkOrderService.deleteAttachment(cloudContext);
        return JSON;
    }
    @JSON(serialize = false)
    public InputStream getAttachment() {
        return attachment;
    }
    
    public void setUpload(File file) {
        this.file = file;
    }

    public void setUploadContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUploadFileName(String filename) {
        this.filename = filename;
    }
}
