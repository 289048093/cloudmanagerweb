/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Dec 6, 2012  10:04:29 AM
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
 * @author CloudKing
 */
@Entity
@Table(name = "tb_eventlog")
public class EventLogEntity extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 619979475308290285L;

    /**
     * 添加时间
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;
    /**
     * 域
     */
    @Column(name = "DOMAIN_NAME_")
    private String domainName;

    /*
     * 关系
     */
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_")
    private UserEntity user;

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
