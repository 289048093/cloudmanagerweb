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
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 资源
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "tb_computeresource")
public class ComputeResourceEntity extends BaseEntity {
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
     * CPU
     */
    @Column(name = "CPU_")
    private Integer cpu;
    /**
     * 可用cpu
     */
    @Column(name = "CPU_AVAILABLE_")
    private Integer cpuAvailable;

    /**
     * 内存
     */
    @Column(name = "MEMORY_")
    private Integer memory;
    /**
     * 可用内存
     */
    @Column(name = "MEMORY_AVAILABLE_")
    private Integer memoryAvailable;

    /**
     * IP地址
     */
    @Column(name = "IP_")
    private String ip;

    /**
     * 是否导致机架报警 格式："cpu;内存"
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
     * 关系
     */
    /**
     * 资源
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MACHINERACK_ID_")
    private MachineRackEntity machineRack;

    /**
     * 资源池
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPUTERESOURCEPOOL_ID_")
    private ComputeResourcePoolEntity computeResourcePool;

    /**
     * 虚拟机
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "computeResource", targetEntity = VirtualMachineEntity.class)
    private List<VirtualMachineEntity> virtualMachines;

    public List<VirtualMachineEntity> getVirtualMachines() {
        return virtualMachines;
    }

    public void setVirtualMachines(List<VirtualMachineEntity> virtualMachines) {
        this.virtualMachines = virtualMachines;
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

    public ComputeResourcePoolEntity getComputeResourcePool() {
        return computeResourcePool;
    }

    public void setComputeResourcePool(ComputeResourcePoolEntity computeResourcePool) {
        this.computeResourcePool = computeResourcePool;
    }

    /**
     * @return the cpu
     */
    public Integer getCpu() {
        return cpu;
    }

    /**
     * @param cpu
     *            the cpu to set
     */
    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the memory
     */
    public Integer getMemory() {
        return memory;
    }

    /**
     * @param memory
     *            the memory to set
     */
    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getRrdPath() {
        return rrdPath;
    }

    public void setRrdPath(String rrdPath) {
        this.rrdPath = rrdPath;
    }

    public Integer getCpuAvailable() {
        return cpuAvailable;
    }

    public void setCpuAvailable(Integer cpuAvailable) {
        this.cpuAvailable = cpuAvailable;
    }

    public Integer getMemoryAvailable() {
        return memoryAvailable;
    }

    public void setMemoryAvailable(Integer memoryAvailable) {
        this.memoryAvailable = memoryAvailable;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ComputeResourceEntity) {
            ComputeResourceEntity domain = (ComputeResourceEntity) obj;
            return this.getId().equals(domain.getId());
        }
        return false;
    }

    /**
     * 重写hashcode
     */
    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

}
