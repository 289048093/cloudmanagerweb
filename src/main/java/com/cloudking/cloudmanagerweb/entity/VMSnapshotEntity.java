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
 * 快照
 * 
 * @author CloudKing
 */
@Entity
@Table(name = "tb_vmsnapshot")
public class VMSnapshotEntity extends BaseEntity {
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
     * 操作情况：是否在删除中等。。
     */
    @Column(name="operateFlag")
    private String operateFlag;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(String operateFlag) {
        this.operateFlag = operateFlag;
    }

}
