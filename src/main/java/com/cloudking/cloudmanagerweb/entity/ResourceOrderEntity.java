/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 资源工单
 * 
 * @author CloudKing
 */
@Entity
@Table(name = "tb_resourceorder")
public class ResourceOrderEntity extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 8874966895069310228L;
    /*
     * 基本信息
     */
    /**
     * 标题
     */
    @Column(name = "TITLE_", nullable = false)
    private String title;
    /**
     * 内容
     */
    @Lob
    @Column(name = "CONTENT_", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String content;
    /**
     * 创建时间
     */
    @Column(name = "CREATETIME_")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "UPDATETIME_")
    private Date updateTime;
    /**
     * 关闭时间
     */
    @Column(name = "CLOSETIME_")
    private Date closeTime;
    /**
     * 工单状态
     */
    @Column(name = "STATUS_", columnDefinition = "char(1)", nullable = false)
    private String status;
    /**
     * 流水号 RO_yyyyMMdd_HHmmss
     */
    @Column(name = "SERIALNUMBER_", columnDefinition = "char(18)", nullable = false)
    private String serialNumber;
    /**
     * 存储大小
     */
    @Column(name="STORAGECAPACITY_")
    private Long storageCapacity;
    /**
     * cpu(C)
     */
    @Column(name="CPU_")
    private Integer cpu;
    /**
     * 内存
     */
    @Column(name = "MEMORY_")
    private Integer memory;
   

    /**
     * 工单的操作
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resourceOrder", fetch = FetchType.LAZY)
    @OrderBy("addTime ASC")
    private List<ResourceOrderActionEntity> resourceOrderActions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<ResourceOrderActionEntity> getResourceOrderActions() {
        return resourceOrderActions;
    }

    public void setResourceOrderActions(List<ResourceOrderActionEntity> resourceOrderActions) {
        this.resourceOrderActions = resourceOrderActions;
    }

    public Long getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(Long storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

}
