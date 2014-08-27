/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 菜单实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_menu")
public class MenuEntity extends BaseEntity {
    /**
     * 基本信息
     */
    @Column(name = "NAME_", length = 20, nullable = false, unique = true)
    private String name;
    /**
     * 模块描述
     */
    @Column(name = "DESC_")
    private String desc;
    /**
     * url地址
     */
    @Column(name = "URL_")
    private String url; 

    /**
     * 层级及顺序编号
     */
    @Column(name = "CODE_")
    private String code;

    /**
     * 图片
     */
    @Column(name = "IMG_")
    private String img;

    /**
     * 关系
     */
    //域
    @OrderBy("id ASC")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "menu", fetch = FetchType.LAZY)
    private List<DomainEntity> domain;

    /**
     * 权限
     */
    @OrderBy("id ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = RightsEntity.class, mappedBy = "menu", fetch = FetchType.LAZY)
    private List<RightsEntity> rights;

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     *            the img to set
     */
    public void setImg(String img) {
        this.img = img;
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
    public void setDomain(List<DomainEntity> domain) {
        this.domain = domain;
    }

    public void setRights(List<RightsEntity> rights) {
        this.rights = rights;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
