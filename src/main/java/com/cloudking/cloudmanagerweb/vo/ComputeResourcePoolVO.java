/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:50:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class ComputeResourcePoolVO extends BaseVO {
    /**
     * 基本信息
     */
    /**
     * 名字
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否被设置
     */
    private Long domainId;

    /**
     * cpu使用倍数
     */
    private Float cpuRate;
    /**
     * 内存使用倍数
     */
    private Float memoryRate;

    /**
     * 总cpu
     */
    private Integer totalCpu;

    /**
     * 总虚拟cpu（物理*超配系数）
     */
    private Integer totalVirtualCpu;
    /**
     * 总内存
     */
    private Integer totalMemory;
    /**
     * 总虚拟内存（物理*超配系数）
     */
    private Integer totalVirtualMemory;
    /**
     * 总可用cpu
     */
    private Integer availableCpu;
    /**
     * 总可用内存
     */
    private Integer availableMemory;

    /**
     * 计算节点数
     */
    private Integer computeResourceNum;

    /**
     * 虚机数量
     */
    private Integer vmNum;

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

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ComputeResourcePoolVO) {
            ComputeResourcePoolVO pool = (ComputeResourcePoolVO) obj;
            return this.getId().equals(pool.getId());
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

    public Integer getTotalCpu() {
        return totalCpu;
    }

    public void setTotalCpu(Integer totalCpu) {
        this.totalCpu = totalCpu;
    }

    public Integer getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Integer totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Integer getComputeResourceNum() {
        return computeResourceNum;
    }

    public void setComputeResourceNum(Integer computeResourceNum) {
        this.computeResourceNum = computeResourceNum;
    }

    public Integer getVmNum() {
        return vmNum;
    }

    public void setVmNum(Integer vmNum) {
        this.vmNum = vmNum;
    }

    public Integer getAvailableCpu() {
        return availableCpu;
    }

    public void setAvailableCpu(Integer availableCpu) {
        this.availableCpu = availableCpu;
    }

    public Integer getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(Integer availableMemory) {
        this.availableMemory = availableMemory;
    }

    public Integer getTotalVirtualCpu() {
        return totalVirtualCpu;
    }

    public void setTotalVirtualCpu(Integer totalVirtualCpu) {
        this.totalVirtualCpu = totalVirtualCpu;
    }

    public Integer getTotalVirtualMemory() {
        return totalVirtualMemory;
    }

    public void setTotalVirtualMemory(Integer totalVirtualMemory) {
        this.totalVirtualMemory = totalVirtualMemory;
    }

}
