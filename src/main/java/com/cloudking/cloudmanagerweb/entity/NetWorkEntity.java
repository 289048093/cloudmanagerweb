/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 网络实体
 * 
 * @author CloudKing
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tb_network")
public class NetWorkEntity extends BaseEntity {
    
    /**
     * 网络名
     */
    @Column(name = "NAME_", nullable = false)
    private String name;
    
    /**
     * 内部真正网络名
     */
    @Column(name = "REALNAME_", nullable = false)
    private String realname;
    
    /**
     * 开始IP
     */
    @Column(name = "STARTIP_", nullable = false)
    private String startIP;

    /**
     * 结束IP
     */
    @Column(name = "ENDIP_", nullable = false)
    private String endIP;
    /**
     * 网段
     */
    @Column(name = "CIDR_", nullable = false)
    private String cidr;
    /**
     * 网络类型nat,bridge
     */
    @Column(name = "TYPE_", nullable = false)
    private String type;
    
    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;
    
    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    public String getStartIP() {
        return startIP;
    }

    public void setStartIP(String startIP) {
        this.startIP = startIP;
    }

    public String getEndIP() {
        return endIP;
    }

    public void setEndIP(String endIP) {
        this.endIP = endIP;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


}
