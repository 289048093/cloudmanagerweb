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
 * 工单类别
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_workordercategory")
public class WorkOrderCategoryEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 类别名称
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 类别描述
     */
    @Column(name = "DESC_")
    private String desc;
    /**
     * 是否内置
     */
    @Column(name = "BUILDIN_")
    private Boolean buildin;

    /**
     * 关系
     */
    /**
     * 工单
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", targetEntity = WorkOrderEntity.class, fetch = FetchType.LAZY)
    private List<WorkOrderEntity> workOrders;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<WorkOrderEntity> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrderEntity> workOrders) {
        this.workOrders = workOrders;
    }

    public Boolean getBuildin() {
        return buildin;
    }

    public void setBuildin(Boolean buildin) {
        this.buildin = buildin;
    }
}
