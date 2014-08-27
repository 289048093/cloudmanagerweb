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
 * 机架
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "tb_machinerack")
public class MachineRackEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 机架名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 机架描述
     */
    @Column(name = "DESC_")
    private String desc;

    /**
     * 是否导致机房报警
     */
    @Column(name = "WARN4ROOM_", nullable = false)
    private Boolean warn4Room;
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
     * 机房
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MACHINEROOM_ID_")
    private MachineRoomEntity machineRoom;

    /**
     * 计算节点
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = ComputeResourceEntity.class, mappedBy = "machineRack", fetch = FetchType.LAZY)
    private List<ComputeResourceEntity> computeResources;
    /**
     * 存储资源
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = StorageResourceEntity.class, mappedBy = "machineRack", fetch = FetchType.LAZY)
    private List<StorageResourceEntity> storageResources;

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

    public Boolean getWarn4Room() {
        return warn4Room;
    }

    public void setWarn4Room(Boolean warn4Room) {
        this.warn4Room = warn4Room;
    }

    public MachineRoomEntity getMachineRoom() {
        return machineRoom;
    }

    public void setMachineRoom(MachineRoomEntity machineRoom) {
        this.machineRoom = machineRoom;
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

    public List<ComputeResourceEntity> getComputeResources() {
        return computeResources;
    }

    public void setComputeResources(List<ComputeResourceEntity> computeResources) {
        this.computeResources = computeResources;
    }

    public List<StorageResourceEntity> getStorageResources() {
        return storageResources;
    }

    public void setStorageResources(List<StorageResourceEntity> storageResources) {
        this.storageResources = storageResources;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

}
