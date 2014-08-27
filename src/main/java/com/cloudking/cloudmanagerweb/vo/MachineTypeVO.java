/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 配置VO
 * 
 * @author CloudKing
 * 
 */
public class MachineTypeVO extends BaseVO implements Comparable<MachineTypeVO> {
    /**
     * 名字
     */
    private String name;
    /**
     * 描述
     */
    private String desc;

    /**
     * CPU
     */
    private Integer cpu;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 硬盘
     */
    private Integer disk;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 域id
     */
    private Long domainId;

    /**
     * 域名
     */
    private String domainName;
    /**
     * 是否是自己的配置
     */
    private Boolean selfMachineType;

    public Boolean getSelfMachineType() {
        return selfMachineType;
    }

    public void setSelfMachineType(Boolean selfMachineType) {
        this.selfMachineType = selfMachineType;
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

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MachineTypeVO) {
            MachineTypeVO machineType = (MachineTypeVO) obj;
            return this.getId().equals(machineType.getId());
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

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(MachineTypeVO o) {
        return this.getAddTime().compareTo(o.getAddTime());
    }
}