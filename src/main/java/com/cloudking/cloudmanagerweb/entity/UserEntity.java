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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 用户实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_user")
public class UserEntity extends BaseEntity {
    /**
     * 用户名
     */
    @Column(name = "USERNAME_", length = 12, nullable = false)
    private String username;

    /**
     * 用户真实姓名
     */
    @Column(name = "REALNAME_", length = 12, nullable = false)
    private String realname;

    /**
     * 登陆密码
     */
    @Column(name = "PASSWORD_", columnDefinition = "CHAR(32)", nullable = false)
    private String password;

    /**
     * email
     */
    @Column(name = "EMAIL_")
    private String email;

    /**
     * 手机
     */
    @Column(name = "CELLPHONE_")
    private String cellPhone;

    /**
     * 地址
     */
    @Column(name = "TELPHONE_")
    private String telPhone;

    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;
    /**
     * 是否已删除
     */
    @Column(name = "DELETEFLAG_")
    private Boolean deleteFlag;

    /*
     * 关系
     */
    /**
     * 虚机虚拟机
     */
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VirtualMachineEntity.class, mappedBy = "createUser", fetch = FetchType.LAZY)
    private List<VirtualMachineEntity> virtualMachines;
    /**
     * 域
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = DomainEntity.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<DomainEntity> domains;
    /**
     * 事件
     */
    @OneToMany(cascade = CascadeType.ALL, targetEntity = EventLogEntity.class, mappedBy = "user", fetch = FetchType.LAZY)
    private List<EventLogEntity> eventLogs;

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * 获取用户名
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the virtualMachines
     */
    public List<VirtualMachineEntity> getVirtualMachines() {
        return virtualMachines;
    }

    /**
     * @param virtualMachines
     *            the virtualMachines to set
     */
    public void setVirtualMachines(List<VirtualMachineEntity> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }

    public List<DomainEntity> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainEntity> domains) {
        this.domains = domains;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof UserEntity) {
            UserEntity user = (UserEntity) obj;
            return this.getId().equals(user.getId());
        }
        return false;
    }

    /**
     * 重写hashcode
     */
    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public List<EventLogEntity> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(List<EventLogEntity> eventLogs) {
        this.eventLogs = eventLogs;
    }
}