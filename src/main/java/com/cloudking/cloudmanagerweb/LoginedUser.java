/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MenuVO;

/**
 * 已经登录的用户记录类
 * 
 * @author CloudKing
 * 
 */
public class LoginedUser implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5689315459231356551L;
    /**
     * 基本信息
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户真实姓名
     */
    private String realname;
    /**
     * 最后一次登陆时间
     */
    private Date lastLoginTime;
    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 域ID
     */
    private Long domainID;
    /**
     * 域code
     */
    private String domainCode;
    /**
     * 域名
     */
    private String domainName;

    /**
     * 权限
     */
    private String rightsUrls = "";

    /**
     * 菜单
     */
    private List<MenuVO> menus = new ArrayList<MenuVO>();

    /**
     * 登录用户的域
     */
    private List<DomainVO> domains;

    /**
     * 获取ID
     * 
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取最后一次登录时间
     * 
     * @return
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置最后一次登录时间
     * 
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取注册时间
     * 
     * @return
     */
    public Date getRegisterTime() {
        return registerTime;
    }

    /**
     * 设置注册时间
     * 
     * @param registerTime
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 是否拥有某个权限
     * 
     * @param url
     * @return
     */
    public Boolean containRights(String url) {
        return rightsUrls.contains(url);
    }

    /**
     * 是否拥有某个模块
     * 
     * @param url
     * @return
     */
    public Boolean containModule(String url) {
        //TODO
        return true;
    }

    @JSON(serialize = false)
    public String getRightsUrls() {
        return this.rightsUrls;
    }

    public void setRights(String rightsUrls) {
        this.rightsUrls = rightsUrls;
    }

    @JSON(serialize = false)
    public List<MenuVO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuVO> menus) {
        this.menus = menus;
    }

    /**
     * 添加菜单
     * 
     * @param menuVO
     */
    public void addMenu(MenuVO menuVO) {
        this.menus.add(menuVO);
    }

    /**
     * @return the domainID
     */
    public Long getDomainID() {
        return domainID;
    }

    /**
     * @param domainID
     *            the domainID to set
     */
    public void setDomainID(Long domainID) {
        this.domainID = domainID;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @JSON(serialize = false)
    public List<DomainVO> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainVO> domains) {
        this.domains = domains;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
