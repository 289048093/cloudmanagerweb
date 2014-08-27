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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 机房
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "tb_machineroom")
public class MachineRoomEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 机房名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 机房描述
     */
    @Column(name = "DESC_")
    private String desc;

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
     * 关系
     */
    /**
     * 机架
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = MachineRackEntity.class, mappedBy = "machineRoom", fetch = FetchType.LAZY)
    private List<MachineRackEntity> machineRacks;

    /**
     * 机房名字
     */
    public String getName() {
        return name;
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
     * 机房名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 机房描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 机房描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the machineRacks
     */
    public List<MachineRackEntity> getMachineRacks() {
        return machineRacks;
    }

    /**
     * @param machineRacks
     *            the machineRacks to set
     */
    public void setMachineRacks(List<MachineRackEntity> machineRacks) {
        this.machineRacks = machineRacks;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }
}
