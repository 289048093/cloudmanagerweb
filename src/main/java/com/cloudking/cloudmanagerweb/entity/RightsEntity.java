/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 权限控制类
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_rights")
public class RightsEntity extends BaseEntity {
    /**
     * 基本信息
     */

    //权限名字
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;

    /**
     * 权限描述
     */
    @Column(name = "DESC_")
    private String desc;

    /**
     * 请求地址
     */
    @Column(name = "URL_", nullable = false, unique = true)
    private String url;

    /**
     * 关系
     */

    // 菜单
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID_")
    private MenuEntity menu;

    /**
     * 域
     */
    @OrderBy("id ASC")
    @ManyToMany(mappedBy = "rights", fetch = FetchType.LAZY)
    private List<DomainEntity> domain;

    public void setDomain(List<DomainEntity> domain) {
        this.domain = domain;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public void setMenu(MenuEntity menu) {
        this.menu = menu;
    }

    /**
     * @return the domain
     */
    public List<DomainEntity> getDomain() {
        return domain;
    }

}
