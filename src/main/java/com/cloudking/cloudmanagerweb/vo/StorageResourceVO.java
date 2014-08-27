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
public class StorageResourceVO extends BaseVO {

    /**
     * 资源名字
     */
    private String name;
    /**
     * 资源描述
     */
    private String desc;
    /**
     * 资源类型
     */
    private String type;

    /**
     * IP地址
     */
    private String ip;
    /**
     * 存储池名字
     */
    private String poolName;
    /**
     * 是否导致机架报警
     */
    private String warn4Rack;
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

    /*
     * 其他信息
     */
    /**
     * 域
     */
    private Long domainId;
    /**
     * 机房名字
     */
    private String machineRoomName;
    /**
     * 机架名字
     */
    private String machineRackName;
    /**
     * 资源池ID
     */
    private Long storageResourcePoolId;
    /**
     * 资源池名称
     */
    private String storageResourcePoolName;

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
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
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

    /**
     * @return the warn4Rack
     */
    public String getWarn4Rack() {
        return warn4Rack;
    }

    /**
     * @param warn4Rack
     *            the warn4Rack to set
     */
    public void setWarn4Rack(String warn4Rack) {
        this.warn4Rack = warn4Rack;
    }

    /**
     * @return the machineRoomName
     */
    public String getMachineRoomName() {
        return machineRoomName;
    }

    /**
     * @param machineRoomName
     *            the machineRoomName to set
     */
    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    /**
     * @return the machineRackName
     */
    public String getMachineRackName() {
        return machineRackName;
    }

    /**
     * @param machineRackName
     *            the machineRackName to set
     */
    public void setMachineRackName(String machineRackName) {
        this.machineRackName = machineRackName;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public Long getStorageResourcePoolId() {
        return storageResourcePoolId;
    }

    public void setStorageResourcePoolId(Long storageResourcePoolId) {
        this.storageResourcePoolId = storageResourcePoolId;
    }

    public String getStorageResourcePoolName() {
        return storageResourcePoolName;
    }

    public void setStorageResourcePoolName(String storageResourcePoolName) {
        this.storageResourcePoolName = storageResourcePoolName;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    /**
     * @return the poolName
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * @param poolName
     *            the poolName to set
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
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
}
