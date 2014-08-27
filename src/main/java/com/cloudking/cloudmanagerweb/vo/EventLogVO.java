/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:50:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class EventLogVO extends BaseVO {
    /**
     * 
     */
    private static final long serialVersionUID = 7804569460409683954L;
    /**
     * 基本信息
     */
    /**
     * 名字
     */
    private String userName;
    /**
     * 域
     */
    private String domainName;
    /**
     * 时间
     */
    private Date addTime;
    /**
     * 描述
     */
    private String desc;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

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
}
