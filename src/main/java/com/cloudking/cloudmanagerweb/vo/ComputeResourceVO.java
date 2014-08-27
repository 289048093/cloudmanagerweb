/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 计算节点
 * 
 * @author CloudKing
 * 
 */
public class ComputeResourceVO extends BaseVO {

    /**
     * 资源名字
     */
    private String name;
    /**
     * 资源描述
     */
    private String desc;
    /**
     * 资源类型
     */
    private String type;

    /**
     * CPU
     */
    private Integer cpu;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * IP地址
     */
    private String ip;
    /**
     * 是否导致机架报警 格式 cpu;内存
     */
    private String warn4Rack;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 虚拟机数量
     */
    private Integer vmNum;
    /**
     * 计算节点用户名
     */
    private String username;
    /**
     * 计算节点密码
     */
    private String password;
    /**
     * 可用cpu
     */
    private Integer cpuAvailable;
    /**
     * 可用内存
     */
    private Integer memoryAvailable;

    /*
     * 其他信息
     */
    /**
     * 机房名字
     */
    private String machineRoomName;
    /**
     * 机架名字
     */
    private String machineRackName;
    /**
     * 资源池ID
     */
    private Long computeResourcePoolId;
    /**
     * 资源池名称
     */
    private String computeResourcePoolName;

    public Long getComputeResourcePoolId() {
        return computeResourcePoolId;
    }

    public void setComputeResourcePoolId(Long computeResourcePoolId) {
        this.computeResourcePoolId = computeResourcePoolId;
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
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
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
     * @return the warn4Rack
     */
    public String getWarn4Rack() {
        return warn4Rack;
    }

    /**
     * @param warn4Rack
     *            the warn4Rack to set
     */
    public void setWarn4Rack(String warn4Rack) {
        this.warn4Rack = warn4Rack;
    }

    /**
     * @return the machineRoomName
     */
    public String getMachineRoomName() {
        return machineRoomName;
    }

    /**
     * @param machineRoomName
     *            the machineRoomName to set
     */
    public void setMachineRoomName(String machineRoomName) {
        this.machineRoomName = machineRoomName;
    }

    /**
     * @return the machineRackName
     */
    public String getMachineRackName() {
        return machineRackName;
    }

    /**
     * @param machineRackName
     *            the machineRackName to set
     */
    public void setMachineRackName(String machineRackName) {
        this.machineRackName = machineRackName;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getComputeResourcePoolName() {
        return computeResourcePoolName;
    }

    public void setComputeResourcePoolName(String computeResourcePoolName) {
        this.computeResourcePoolName = computeResourcePoolName;
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

    public Integer getVmNum() {
        return vmNum;
    }

    public void setVmNum(Integer vmNum) {
        this.vmNum = vmNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCpuAvailable() {
        return cpuAvailable;
    }

    public void setCpuAvailable(Integer cpuAvailable) {
        this.cpuAvailable = cpuAvailable;
    }

    public Integer getMemoryAvailable() {
        return memoryAvailable;
    }

    public void setMemoryAvailable(Integer memoryAvailable) {
        this.memoryAvailable = memoryAvailable;
    }

}
