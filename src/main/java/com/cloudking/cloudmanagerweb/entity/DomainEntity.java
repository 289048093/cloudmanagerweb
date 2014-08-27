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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 域
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_domain")
public class DomainEntity extends BaseEntity {
    /**
     * 基本信息
     */
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
     * 编码 域相当于一个树，code格式为XXXXXX，每两个X表示一层，总共100层，每层最多100个。
     */
    @Column(name = "CODE_", length = 200)
    private String code;

    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /**
     * 存储空间大小
     */
    @Column(name = "STORAGECAPACITY_")
    private Long storageCapacity;
    /**
     * 剩余存储空间大小
     */
    @Column(name = "AVAILABLESTORAGECAPACITY_")
    private Long availableStorageCapacity;
    /**
     * 总cpu
     */
    @Column(name = "CPU_TOTAL_NUM_")
    private Integer cpuTotalNum;
    /**
     * 可用cpu
     */
    @Column(name = "CPU_AVAILABEL_NUM_")
    private Integer cpuAvailableNum;
    /**
     * 总内存
     */
    @Column(name = "MEMORY_CAPACITY_")
    private Integer memoryCapacity;
    /**
     * 可用内存
     */
    @Column(name = "MEMORY_AVAILABLE_CAPACITY_")
    private Integer memoryAvailableCapacity;
    /**
     * 备份存储
     */
    @Column(name = "BACKUPSTORAGE_CAPACITY_")
    private Long backupStorageCapacity;
    /**
     * 可用备份存储
     */
    @Column(name="AVAILABLE_BACKUPSTORAGE_CAPACITY_")
    private Long availableBackupStorageCapacity;

    /*
     * 关系
     */
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_")
    private UserEntity user;

    /**
     * 模块
     */
    @OrderBy("id ASC")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_domain_bid_menu", joinColumns = { @JoinColumn(name = "DOMAIN_ID_", referencedColumnName = "ID_") }, inverseJoinColumns = { @JoinColumn(name = "MENU_ID_", referencedColumnName = "ID_") })
    private List<MenuEntity> menu;

    /**
     * 权限
     */
    @OrderBy("id ASC")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_domain_bid_rights", joinColumns = { @JoinColumn(name = "DOMAIN_ID_", referencedColumnName = "ID_") }, inverseJoinColumns = { @JoinColumn(name = "RIGHTS_ID_", referencedColumnName = "ID_") })
    private List<RightsEntity> rights;

    /**
     * 计算节点池
     */
    @OrderBy("addTime ASC")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_domain_bid_pool", joinColumns = { @JoinColumn(name = "DOMAIN_ID_", referencedColumnName = "ID_") }, inverseJoinColumns = { @JoinColumn(name = "POOL_ID_", referencedColumnName = "ID_") })
    private List<ComputeResourcePoolEntity> computeResourcePools;
    /**
     * 模版
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = TemplateEntity.class, mappedBy = "domain", fetch = FetchType.LAZY)
    private List<TemplateEntity> templates;
    /**
     * 虚机虚拟机
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VirtualMachineEntity.class, mappedBy = "domain", fetch = FetchType.LAZY)
    private List<VirtualMachineEntity> virtualMachines;
    /**
     * 机型
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = MachineTypeEntity.class, mappedBy = "domain", fetch = FetchType.LAZY)
    private List<MachineTypeEntity> machineTypes;
    /**
     * 存储资源
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = StorageResourceEntity.class, mappedBy = "domain", fetch = FetchType.LAZY)
    private List<StorageResourceEntity> storageResources;

    /**
     * 接手的工单
     */
    @OrderBy("createTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = WorkOrderEntity.class, mappedBy = "receiveDomain", fetch = FetchType.LAZY)
    private List<WorkOrderEntity> receiveWorkOrders;

    /**
     * 接手的工单
     */
    @OrderBy("createTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = WorkOrderEntity.class, mappedBy = "sendDomain", fetch = FetchType.LAZY)
    private List<WorkOrderEntity> sendWorkOrders;
    

    public List<MachineTypeEntity> getMachineTypes() {
        return machineTypes;
    }

    public void setMachineTypes(List<MachineTypeEntity> machineTypes) {
        this.machineTypes = machineTypes;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<ComputeResourcePoolEntity> getComputeResourcePools() {
        return computeResourcePools;
    }

    public void setComputeResourcePools(List<ComputeResourcePoolEntity> computeResourcePools) {
        this.computeResourcePools = computeResourcePools;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * @return the virtualMachines
     */
    public List<VirtualMachineEntity> getVirtualMachines() {
        return virtualMachines;
    }

    /**
     * @param virtualMachines
     *            the virtualMachines to set
     */
    public void setVirtualMachines(List<VirtualMachineEntity> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }

    public List<MenuEntity> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuEntity> menu) {
        this.menu = menu;
    }

    public List<RightsEntity> getRights() {
        return rights;
    }

    public void setRights(List<RightsEntity> rights) {
        this.rights = rights;
    }

    public List<TemplateEntity> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateEntity> templates) {
        this.templates = templates;
    }

    public Long getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(Long storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public Long getAvailableStorageCapacity() {
        return availableStorageCapacity;
    }

    public void setAvailableStorageCapacity(Long availableStorageCapacity) {
        this.availableStorageCapacity = availableStorageCapacity;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DomainEntity) {
            DomainEntity domain = (DomainEntity) obj;
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

    public List<StorageResourceEntity> getStorageResources() {
        return storageResources;
    }

    public void setStorageResources(List<StorageResourceEntity> storageResources) {
        this.storageResources = storageResources;
    } 
    public List<WorkOrderEntity> getReceiveWorkOrders() {
        return receiveWorkOrders;
    }

    public void setReceiveWorkOrders(List<WorkOrderEntity> receiveWorkOrders) {
        this.receiveWorkOrders = receiveWorkOrders;
    }

    public List<WorkOrderEntity> getSendWorkOrders() {
        return sendWorkOrders;
    }

    public void setSendWorkOrders(List<WorkOrderEntity> sendWorkOrders) {
        this.sendWorkOrders = sendWorkOrders;
    }

    public Integer getCpuTotalNum() {
        return cpuTotalNum;
    }

    public void setCpuTotalNum(Integer cpuTotalNum) {
        this.cpuTotalNum = cpuTotalNum;
    }

    public Integer getCpuAvailableNum() {
        return cpuAvailableNum;
    }

    public void setCpuAvailableNum(Integer cpuAvailableNum) {
        this.cpuAvailableNum = cpuAvailableNum;
    }

    public Integer getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(Integer memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public Integer getMemoryAvailableCapacity() {
        return memoryAvailableCapacity;
    }

    public void setMemoryAvailableCapacity(Integer memoryAvailableCapacity) {
        this.memoryAvailableCapacity = memoryAvailableCapacity;
    }

    public Long getBackupStorageCapacity() {
        return backupStorageCapacity;
    }

    public void setBackupStorageCapacity(Long backupStorageCapacity) {
        this.backupStorageCapacity = backupStorageCapacity;
    }

    public Long getAvailableBackupStorageCapacity() {
        return availableBackupStorageCapacity;
    }

    public void setAvailableBackupStorageCapacity(Long availableBackupStorageCapacity) {
        this.availableBackupStorageCapacity = availableBackupStorageCapacity;
    }

}
