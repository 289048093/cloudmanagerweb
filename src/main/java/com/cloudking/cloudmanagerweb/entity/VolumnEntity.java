/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 卷
 * 
 * @author CloudKing
 */
@Entity
@Table(name = "tb_volumn")
public class VolumnEntity extends BaseEntity {
    /**
     * 名字
     */
    @Column(name = "NAME_", length = 20, nullable = false)
    private String name;
    /**
     * 卷大小();
     */
    @Column(name = "SIZE_", nullable = false)
    private Long size;
    /**
     * 映像卷标记位
     */
    @Column(name = "IMAGEVOLUMNFLAG_", columnDefinition = "CHAR(1)")
    private String imageVolumnFlag;

    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /*
     * 关系
     */
    /**
     * 虚机
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VIRTUALMACHINE_ID_")
    private VirtualMachineEntity virtualMachine;

    /**
     * 存储资源
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORAGERESOURCE_ID_")
    private StorageResourceEntity storageResource;

    public StorageResourceEntity getStorageResource() {
        return storageResource;
    }

    public void setStorageResource(StorageResourceEntity storageResource) {
        this.storageResource = storageResource;
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
     * @return the virtualMachine
     */
    public VirtualMachineEntity getVirtualMachine() {
        return virtualMachine;
    }

    /**
     * @param virtualMachine
     *            the virtualMachine to set
     */
    public void setVirtualMachine(VirtualMachineEntity virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getImageVolumnFlag() {
        return imageVolumnFlag;
    }

    public void setImageVolumnFlag(String imageVolumnFlag) {
        this.imageVolumnFlag = imageVolumnFlag;
    }
}
