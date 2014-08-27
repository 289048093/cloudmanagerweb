/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 存储资源
 * 
 * @author CloudKing
 * 
 */
public class BackupStorageVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -4984763392420892647L;
    /**
     * 资源名字
     */
    private String name;
    /**
     * 资源描述
     */
    private String desc;

    /**
     * IP地址
     */
    private String ip;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 空间大小
     */
    private Long capacity;
    /**
     * 可用空间大小
     */
    private Long availableCapacity;
    /**
     * 实际可用空间
     */
    private Long realAvailableCapacity;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否导致机架报警
     */
    private String warn4Rack;

    /*
     * 其他信息
     */
    /**
     * 机房名字
     */
    private String machineRoomName;
    /**
     * 机架名字
     */
    private String machineRackName;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Long availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Long getRealAvailableCapacity() {
        return realAvailableCapacity;
    }

    public void setRealAvailableCapacity(Long realAvailableCapacity) {
        this.realAvailableCapacity = realAvailableCapacity;
    }

    public String getMachineRoomName() {
        return machineRoomName;
    }

    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    public String getMachineRackName() {
        return machineRackName;
    }

    public void setMachineRackName(String machineRackName) {
        this.machineRackName = machineRackName;
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

    public String getWarn4Rack() {
        return warn4Rack;
    }

    public void setWarn4Rack(String warn4Rack) {
        this.warn4Rack = warn4Rack;
    }

}
