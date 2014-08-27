/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 用户VO
 * 
 * @author CloudKing
 * 
 */
public class UserVO extends BaseVO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户真实姓名
     */
    private String realname;
    /**
     * 密码
     */
    private String password;
    /**
     * 域ID
     */
    private Long domainId;

    /**
     * email
     */
    private String email;

    /**
     * 手机
     */
    private String cellPhone;

    /**
     * 地址
     */
    private String telPhone;

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

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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
}
