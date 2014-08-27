/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 门户用户中间表
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_portaluser")
public class PortalUserEntity extends BaseEntity {
    /**
     * 门户用户传过来的唯一标识Id
     */
    @Column(name = "USERID_", columnDefinition = "CHAR(32)",nullable = false)
    private String userId;

    /**
     * 用户真实姓名
     */
    @Column(name = "REALNAME_", length = 12, nullable = false)
    private String realname;
    
    /**
     * email
     */
    @Column(name = "EMAIL_",nullable = false)
    private String email;

    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /*
     * 关系
     */
    /**
     * 订单
     */
    @OneToMany(cascade = CascadeType.ALL, targetEntity = PortalOrderEntity.class, mappedBy = "applicant", fetch = FetchType.LAZY)
    private List<PortalOrderEntity> order;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<PortalOrderEntity> getOrder() {
        return order;
    }

    public void setOrder(List<PortalOrderEntity> order) {
        this.order = order;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}