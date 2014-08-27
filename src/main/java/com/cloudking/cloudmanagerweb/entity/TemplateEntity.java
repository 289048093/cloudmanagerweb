/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 虚拟机模版实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_template")
public class TemplateEntity extends BaseEntity {
    /**
     * 名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;

    /**
     * 状态
     */
    @Column(name = "STATUS_")
    private Integer status;

    /**
     * 类型：1，表示本地。2，表示下载，3，标识上传
     */
    @Column(name = "TYPE_")
    private Integer type;

    /**
     * 镜像文件名
     */
    @Column(name = "FILENAME_")
    private String fileName;

    /**
     * URL地址
     */
    @Column(name = "URL_")
    private String url;

    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /**
     * 模板用户
     */
    @Column(name = "USERNAME_")
    private String username;

    /**
     * 模板密码
     */
    @Column(name = "PASSWORD_")
    private String password;
    /**
     * 关系
     * 
     */
    /**
     * 虚拟机
     */
    @OneToMany(cascade = CascadeType.MERGE, targetEntity = VirtualMachineEntity.class, mappedBy = "template", fetch = FetchType.LAZY)
    private Set<VirtualMachineEntity> virtualMachines;
    /**
     * 所属域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID_")
    private DomainEntity domain;

    /**
     * @return the virtualMachines
     */
    public Set<VirtualMachineEntity> getVirtualMachines() {
        return virtualMachines;
    }

    /**
     * @param virtualMachines
     *            the virtualMachines to set
     */
    public void setVirtualMachines(Set<VirtualMachineEntity> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }

    /**
     * @return the addTime
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public DomainEntity getDomain() {
        return domain;
    }

    public void setDomain(DomainEntity domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}