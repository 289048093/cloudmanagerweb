/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 19, 2012  11:08:55 AM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class VirtualMachineVO extends BaseVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 虚机名字
     */
    private String vmName;
    /**
     * 描述
     */
    private String desc;
    /**
     * 状态
     */
    private String status;
    
    /**
     * 虚拟机拥有者
     */
    private String owner;
    
    /**
     * 虚拟机创建者
     */
    private String creator;

    /**
     * 虚拟创建标记位(creating,success,failed)
     */
    private String createdFlag;
    /**
     * 域名
     */
    private String domainName;
    
    /**
     * 域code
     */
    private String domainCode;

    /**
     * IP
     */
    private String ip;

    /**
     * 虚拟机创建成功或失败保存信息
     */
    private String createdResultMsg;
    /**
     * 快照操作失败标记
     */
    private Boolean operateFailFlag;

    /**
     * 计算资源IP
     */
    private String computeResourceIP;
    
    /**
     * 端口
     */
    private Integer port;
    
    /**
     * 添加日期
     */
    private Date addTime;
    
    /**
     * 到期日期
     */
    private Date dueTime;
    /**
     * 自动备份时间（月;日;周;时;分）
     */
    private String backupTimeMark;

    public String getBackupTimeMark() {
        return backupTimeMark;
    }

    public void setBackupTimeMark(String backupTimeMark) {
        this.backupTimeMark = backupTimeMark;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getComputeResourceIP() {
        return computeResourceIP;
    }

    public void setComputeResourceIP(String computeResourceIP) {
        this.computeResourceIP = computeResourceIP;
    }

    public Boolean getOperateFailFlag() {
        return operateFailFlag;
    }

    public void setOperateFailFlag(Boolean operateFailFlag) {
        this.operateFailFlag = operateFailFlag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
