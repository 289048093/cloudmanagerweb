/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.Set;

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
 * 配置实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_machinetype")
public class MachineTypeEntity extends BaseEntity {
    /**
     * 名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;
    /**
     * CPU
     */
    @Column(name = "CPU_")
    private Integer cpu;

    /**
     * 内存
     */
    @Column(name = "MEMORY_")
    private Integer memory;

    /**
     * 硬盘
     */
    @Column(name = "DISK_")
    private Integer disk;
    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;
    /**
     * 域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID_")
    private DomainEntity domain;

    /**
     * 关系
     * 
     */
    /**
     * 虚拟机
     */
    @OneToMany(cascade = CascadeType.MERGE, targetEntity = VirtualMachineEntity.class, mappedBy = "machineType", fetch = FetchType.LAZY)
    private Set<VirtualMachineEntity> virtualMachines;

    /**
     * @return the virtualMachines
     */
    public Set<VirtualMachineEntity> getVirtualMachines() {
        return virtualMachines;
    }

    /**
     * @param virtualMachines
     *            the virtualMachines to set
     */
    public void setVirtualMachines(Set<VirtualMachineEntity> virtualMachines) {
        this.virtualMachines = virtualMachines;
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

    /**
     * @return the disk
     */
    public Integer getDisk() {
        return disk;
    }

    /**
     * @param disk
     *            the disk to set
     */
    public void setDisk(Integer disk) {
        this.disk = disk;
    }

    public DomainEntity getDomain() {
        return domain;
    }

    public void setDomain(DomainEntity domain) {
        this.domain = domain;
    }
}