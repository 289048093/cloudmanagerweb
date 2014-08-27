/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * checkcommand 附件
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_workorderattachment")
public class WorkOrderAttachmentEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 名字
     */
    @Column(name = "FILENAME_")
    private String fileName;
    /**
     * uuidName
     */
    @Column(name = "UUIDNAME_")
    private String uuidName;
    /**
     * 类型
     */
    @Column(name = "MIMETYPE_")
    private String mimeType;
    /**
     * 创建日期
     */
    @Column(name = "CREATETIME_")
    private Date createTime;
    /**
     * 文件大小
     */
    @Column(name = "FILESIZE_")
    private Integer fileSize;

    /**
     * 关系
     */
    /**
     * 创建人员  
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATEUSER_ID_")
    private UserEntity createUser;

    /**
     * 工单  
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ID_")
    private WorkOrderEntity workOrder;

    public WorkOrderEntity getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderEntity workOrder) {
        this.workOrder = workOrder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public UserEntity getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserEntity createUser) {
        this.createUser = createUser;
    }

    public String getUuidName() {
        return uuidName;
    }

    public void setUuidName(String uuidName) {
        this.uuidName = uuidName;
    }

}
