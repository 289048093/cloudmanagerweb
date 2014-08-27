/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 19, 2012  11:08:55 AM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class PortalUserBinVirtualMachineVO extends BaseVO {
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
     * 配置
     */
    private String machineTypeArgs;
    
    /**
     * 虚拟创建标记位(creating,success,failed)
     */
    private String createdFlag;
    
    /**
     * 虚拟机创建成功或失败保存信息
     */
    private String createdResultMsg;
    
    /**
     * Ip地址
     * @return
     */
    private String ip;
    
    /**
     * cpu
     * @return
     */
    private int cpu;
    
    /**
     * memory
     * @return
     */
    private int memory;
    
    /**
     * disk
     * @return
     */
    private int disk;
    /**
     * network
     * @return
     */
    private String network;
    
    /**
     * 域
     * @return
     */
    private String domainName;
    /**
     * 模板
     * @return
     */
    private String templateName;
    

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

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDisk() {
        return disk;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getMachineTypeArgs() {
        return machineTypeArgs;
    }

    public void setMachineTypeArgs(String machineTypeArgs) {
        this.machineTypeArgs = machineTypeArgs;
    }
}
