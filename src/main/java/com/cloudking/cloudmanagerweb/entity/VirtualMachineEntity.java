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
 * 虚拟机虚拟机实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_virtualmachine")
public class VirtualMachineEntity extends BaseEntity {
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
     * 虚机名字
     */
    @Column(name = "VMNAME_")
    private String vmName;

    /**
     * 虚机IP
     */
    @Column(name = "IP_")
    private String ip;

    /**
     * 虚拟创建标记位(creating,success,failed)
     */
    @Column(name = "CREATEDFLAG_")
    private String createdFlag;

    /**
     * 虚拟机创建成功或失败保存信息
     */
    @Column(name = "CREATEDRESULTMSG_")
    private String createdResultMsg;

    /**
     * 快照操作失败标记
     */
    @Column(name = "OPERATEFAILFLAG_")
    private Boolean operateFailFlag;
    /**
     * 添加日期
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;
    /**
     * 自动备份周期
     */
    @Column(name = "backupTimeMark_")
    private String backupTimeMark;

    /**
     * 关系
     */
    /**
     * 创建者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATEUSER_ID_")
    private UserEntity createUser;

    /**
     * 网络
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NETWORK_ID_")
    private NetWorkEntity netWork;

    /**
     * 域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOMAIN_ID_")
    private DomainEntity domain;

    /**
     * 配置
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACHINETYPE_ID_")
    private MachineTypeEntity machineType;

    /**
     * 模板
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID_")
    private TemplateEntity template;

    /**
     * 计算节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPUTERESOURCE_ID_")
    private ComputeResourceEntity computeResource;

    /**
     * 卷
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VolumnEntity.class, mappedBy = "virtualMachine", fetch = FetchType.LAZY)
    private List<VolumnEntity> volumns;

    /**
     * 快照
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VMSnapshotEntity.class, mappedBy = "virtualMachine", fetch = FetchType.LAZY)
    private List<VMSnapshotEntity> vmSnapshots;
    /**
     * 虚机备份
     */
    @OrderBy("addTime ASC")
    @OneToMany(cascade = CascadeType.ALL, targetEntity = VMBackupEntity.class, mappedBy = "virtualMachine", fetch = FetchType.LAZY)
    private List<VMBackupEntity> vmBackups;

    /**
     * @return the computeResource
     */
    public ComputeResourceEntity getComputeResource() {
        return computeResource;
    }

    /**
     * @param computeResource
     *            the computeResource to set
     */
    public void setComputeResource(ComputeResourceEntity computeResource) {
        this.computeResource = computeResource;
    }

    /**
     * @return the createUser
     */
    public UserEntity getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     *            the createUser to set
     */
    public void setCreateUser(UserEntity createUser) {
        this.createUser = createUser;
    }

    public Date getAddTime() {
        return addTime;
    }

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
     * @return the domain
     */
    public DomainEntity getDomain() {
        return domain;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(DomainEntity domain) {
        this.domain = domain;
    }

    /**
     * @return the machineType
     */
    public MachineTypeEntity getMachineType() {
        return machineType;
    }

    /**
     * @param machineType
     *            the machineType to set
     */
    public void setMachineType(MachineTypeEntity machineType) {
        this.machineType = machineType;
    }

    /**
     * @return the template
     */
    public TemplateEntity getTemplate() {
        return template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(TemplateEntity template) {
        this.template = template;
    }

    /**
     * @return the vmName
     */
    public String getVmName() {
        return vmName;
    }

    /**
     * @param vmName
     *            the vmName to set
     */
    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    /**
     * @return the volumns
     */
    public List<VolumnEntity> getVolumns() {
        return volumns;
    }

    /**
     * @param volumns
     *            the volumns to set
     */
    public void setVolumns(List<VolumnEntity> volumns) {
        this.volumns = volumns;
    }

    public NetWorkEntity getNetWork() {
        return netWork;
    }

    public void setNetWork(NetWorkEntity netWork) {
        this.netWork = netWork;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCreatedFlag() {
        return createdFlag;
    }

    public void setCreatedFlag(String createdFlag) {
        this.createdFlag = createdFlag;
    }

    public String getCreatedResultMsg() {
        return createdResultMsg;
    }

    public void setCreatedResultMsg(String createdResultMsg) {
        this.createdResultMsg = createdResultMsg;
    }

    public List<VMSnapshotEntity> getVmSnapshots() {
        return vmSnapshots;
    }

    public void setVmSnapshots(List<VMSnapshotEntity> vmSnapshots) {
        this.vmSnapshots = vmSnapshots;
    }

    public Boolean getOperateFailFlag() {
        return operateFailFlag;
    }

    public void setOperateFailFlag(Boolean operateFailFlag) {
        this.operateFailFlag = operateFailFlag;
    }

    public List<VMBackupEntity> getVmBackups() {
        return vmBackups;
    }

    public void setVmBackups(List<VMBackupEntity> vmBackups) {
        this.vmBackups = vmBackups;
    }

    public String getBackupTimeMark() {
        return backupTimeMark;
    }

    public void setBackupTimeMark(String backupTimeMark) {
        this.backupTimeMark = backupTimeMark;
    }
}