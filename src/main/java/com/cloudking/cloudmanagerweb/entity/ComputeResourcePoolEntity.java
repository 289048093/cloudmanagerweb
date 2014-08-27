/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 资源池实体
 * 
 * @author CloudKing
 */
/**
 * @author CloudKing
 */
@Entity
@Table(name = "tb_computeresourcepool")
public class ComputeResourcePoolEntity extends BaseEntity {
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
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;
    /**
     * cpu使用倍数
     */
    @Column(name = "CPU_RATE_")
    private Float cpuRate;
    /**
     * 内存使用倍数
     */
    @Column(name = "MEMORY_RATE_")
    private Float memoryRate;

    /*
     * 关系
     */
    /**
     * 资源
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "computeResourcePool", targetEntity = ComputeResourceEntity.class)
    private List<ComputeResourceEntity> computeResources;
    /**
     * 域
     */
    @OrderBy("id ASC")
    @ManyToMany(mappedBy = "computeResourcePools", cascade = CascadeType.ALL)
    private List<DomainEntity> domains;

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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<ComputeResourceEntity> getComputeResources() {
        return computeResources;
    }

    public void setComputeResources(List<ComputeResourceEntity> computeResources) {
        this.computeResources = computeResources;
    }

    public List<DomainEntity> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainEntity> domains) {
        this.domains = domains;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ComputeResourcePoolEntity) {
            ComputeResourcePoolEntity domain = (ComputeResourcePoolEntity) obj;
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

    public Float getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(Float cpuRate) {
        this.cpuRate = cpuRate;
    }

    public Float getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(Float memoryRate) {
        this.memoryRate = memoryRate;
    }


}
