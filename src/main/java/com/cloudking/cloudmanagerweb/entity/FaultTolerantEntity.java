/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 资源
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "tb_fault_tolerant")
public class FaultTolerantEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 数据记录的id
     */
    @Column(name = "REFID_", nullable = false)
    private Long refid;
    /**
     * 类型
     */
    @Column(name = "TYPE_")
    private Integer type;
    /**
     * 参数（key=value）分号隔开
     */
    @Column(name = "PARAMS_")
    private String params;

    public Long getRefid() {
        return refid;
    }

    public void setRefid(Long refid) {
        this.refid = refid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
