/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 门户用户与虚拟机关系维护表
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_portaluserbinvirtualmachine")
public class PortalUserBinVirtualMachineEntity extends BaseEntity {
    
    /**
     * 门户用户
     */
    @ManyToOne(cascade=CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTALUSER_ID_")
    private PortalUserEntity portalUser;

    /**
     * 虚拟机
     */
    @OneToOne(cascade={CascadeType.REFRESH},fetch = FetchType.LAZY,optional=true)  
    @JoinColumn(name="VIRTUALMACHINE_ID")  
    private VirtualMachineEntity virtualMachine;
    
    /**
     * 虚拟机到期时间
     */
    @Column(name = "DUETIME_")
    private Date dueTime;

    public PortalUserEntity getPortalUser() {
        return portalUser;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public void setPortalUser(PortalUserEntity portalUser) {
        this.portalUser = portalUser;
    }

    public VirtualMachineEntity getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachineEntity virtualMachine) {
        this.virtualMachine = virtualMachine;
    }
 
}