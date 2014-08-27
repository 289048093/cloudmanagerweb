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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 工单动作
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_workorderaction")
public class WorkOrderActionEntity extends BaseEntity {
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
    @Column(name = "CREATETIME_", nullable = false)
    private Date createTime;
    /**
     * 工单类型，1：注释操作，2：追加内容
     */
    @Column(name = "TYPE_", columnDefinition = "char(1)")
    private String type;

    /**
     * 关系
     */
    /**
     * 创建人员  
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIONUSER_ID_")
    private UserEntity actionUser;

    /**
     * 工单  
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKORDER_ID_")
    private WorkOrderEntity workOrder;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserEntity getActionUser() {
        return actionUser;
    }

    public void setActionUser(UserEntity actionUser) {
        this.actionUser = actionUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WorkOrderEntity getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrderEntity workOrder) {
        this.workOrder = workOrder;
    }

}
