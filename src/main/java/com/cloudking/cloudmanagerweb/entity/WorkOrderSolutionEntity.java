/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 工单解决情况
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_workordersolution")
public class WorkOrderSolutionEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     *  工单标题
     */
    @Column(name = "NAME_", length = 50, nullable = false)
    private String name;
    /**
     *  是否内置
     */
    @Column(name = "BUILDIN_")
    private Boolean buildin;
    /**
     *  描述
     */
    @Column(name = "DESC_", length = 255)
    private String desc;

    /**
     * 关系
     */
    // 工单
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solution", targetEntity = WorkOrderEntity.class, fetch = FetchType.LAZY)
    private List<WorkOrderEntity> workOrders;

    public List<WorkOrderEntity> getWorkOrders() {
        return workOrders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBuildin() {
        return buildin;
    }

    public void setBuildin(Boolean buildin) {
        this.buildin = buildin;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
