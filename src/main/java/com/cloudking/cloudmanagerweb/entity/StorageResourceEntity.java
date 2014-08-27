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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 存储资源
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "tb_storageresource")
public class StorageResourceEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 资源名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 资源描述
     */
    @Column(name = "DESC_")
    private String desc;

    /**
     * IP地址
     */
    @Column(name = "IP_")
    private String ip;

    /**
     * 存储池名字
     */
    @Column(name = "POOLNAME_")
    private String poolName;
    /**
     * 是否导致机架报警
     */
    @Column(name = "WARN4RACK_")
    private String warn4Rack;
    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /**
     * 标识名字
     */
    @Column(name = "IDENTITYNAME_", nullable = false)
    private String identityName;

    /**
     * RRD路径
     */
    @Column(name = "RRDPATH_", nullable = false)
    private String rrdPath;

    /**
     * 存储资源空间大小
     */
    @Column(name = "CAPACITY_")
    private Long capacity;

    /**
     * 可用大小
     */
    @Column(name = "AVAILABLE_CAPACITY_")
    private Long availableCapacity;
    /**
     * 关系
     */
    /**
     * 资源
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MACHINERACK_ID_")
    private MachineRackEntity machineRack;

    /**
     * 卷
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VolumnEntity.class, mappedBy = "storageResource", fetch = FetchType.LAZY)
    private List<VolumnEntity> volumns;

    /**
     * 域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID_")
    private DomainEntity domain;

    public DomainEntity getDomain() {
        return domain;
    }

    public void setDomain(DomainEntity domain) {
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
     * @return the machineRack
     */
    public MachineRackEntity getMachineRack() {
        return machineRack;
    }

    /**
     * @param machineRack
     *            the machineRack to set
     */
    public void setMachineRack(MachineRackEntity machineRack) {
        this.machineRack = machineRack;
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

    public List<VolumnEntity> getVolumns() {
        return volumns;
    }

    public void setVolumns(List<VolumnEntity> volumns) {
        this.volumns = volumns;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public String getRrdPath() {
        return rrdPath;
    }

    public void setRrdPath(String rrdPath) {
        this.rrdPath = rrdPath;
    }

    public Long getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Long availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

}
