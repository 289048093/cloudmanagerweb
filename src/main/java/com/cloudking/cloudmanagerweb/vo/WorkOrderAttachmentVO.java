/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * checkcommand 附件
 * 
 * @author CloudKing
 * 
 */
public class WorkOrderAttachmentVO extends BaseVO {
    /**
     * 名字
     */
    private String fileName;
    /**
     * uuidName
     */
    private String uuidName;
    /**
     * 类型
     */
    private String mimeType;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 文件大小
     */
    private Integer fileSize; 
    /**
     * 创建人员  
     */
    private String createUserName;
    /**
     * 工单状态
     */
    private String workOrderStatus;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getUuidName() {
        return uuidName;
    }
    public void setUuidName(String uuidName) {
        this.uuidName = uuidName;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getFileSize() {
        return fileSize;
    }
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    public String getCreateUserName() {
        return createUserName;
    }
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    public String getWorkOrderStatus() {
        return workOrderStatus;
    }
    public void setWorkOrderStatus(String workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    } 
}
