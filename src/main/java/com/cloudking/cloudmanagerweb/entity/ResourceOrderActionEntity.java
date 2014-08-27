/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 资源工单动作
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_resourceorderaction")
public class ResourceOrderActionEntity extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = -4964480509782075644L;
    /**
     * 基本信息
     */
    /**
     * 动作描述
     */
    @Column(name = "ACTION_")
    private String action;
    /**
     * 内容
     */
    @Lob
    @Column(name = "CONTENT_")
    private String content;
    /**
     * 创建时间
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /*
     * 关系
     */
    /**
     * 工单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESOURCEORDER_ID_")
    private ResourceOrderEntity resourceOrder;

    public ResourceOrderEntity getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(ResourceOrderEntity resourceOrder) {
        this.resourceOrder = resourceOrder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
