/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 资源工单
 * 
 * @author CloudKing
 */
public class ResourceOrderVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -8081885211806450168L;
    /*
     * 基本信息
     */
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 关闭时间
     */
    private Date closeTime;
    /**
     * 工单状态
     */
    private String status;
    /**
     * 流水号
     */
    private String serialNumber;
    
    /**
     * 存储空间
     */
    private Long storageCapacity;
    /**
     * cpu
     */
    private Integer cpu;
    /**
     * 内存
     */
    private Integer memory;

    /*
     * 额外信息
     */
    /**
     * 发送则名字
     */
    private String senderRealName;

    /**
     * 接受者名字
     */
    private String receiverRealName;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSenderRealName() {
        return senderRealName;
    }

    public void setSenderRealName(String senderRealName) {
        this.senderRealName = senderRealName;
    }

    public String getReceiverRealName() {
        return receiverRealName;
    }

    public void setReceiverRealName(String receiverRealName) {
        this.receiverRealName = receiverRealName;
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
