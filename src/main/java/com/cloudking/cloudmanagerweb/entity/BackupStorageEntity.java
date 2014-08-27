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
@Entity
@Table(name = "tb_backupstorage")
public class BackupStorageEntity extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = -3843216102543463630L;
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
     * 存储资源空间大小（kb）
     */
    @Column(name = "CAPACITY_")
    private Long capacity;

    /**
     * 可用大小（kb）
     */
    @Column(name = "AVAILABLE_CAPACITY_")
    private Long availableCapacity;
    /**
     * 是否导致机架报警
     */
    @Column(name = "WARN4RACK_")
    private String warn4Rack;
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
     * 虚机备份
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VMBackupEntity.class, mappedBy = "backupStorage", fetch = FetchType.LAZY)
    private List<VMBackupEntity> vmBackup;

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

    public List<VMBackupEntity> getVmBackup() {
        return vmBackup;
    }

    public void setVmBackup(List<VMBackupEntity> vmBackup) {
        this.vmBackup = vmBackup;
    }

    public String getWarn4Rack() {
        return warn4Rack;
    }

    public void setWarn4Rack(String warn4Rack) {
        this.warn4Rack = warn4Rack;
    }

}
