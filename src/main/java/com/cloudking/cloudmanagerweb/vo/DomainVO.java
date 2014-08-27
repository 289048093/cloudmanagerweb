/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 19, 2012  4:00:16 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class DomainVO extends BaseVO {
    /**
     * 基本信息
     */
    /**
     * 名字
     */
    private String name;
    /**
     * 存储空间大小
     */
    private Long storageCapacity;
    /**
     * 剩余存储空间
     */
    private Long availableStorageCapacity;
    /**
     * 总cpu
     */
    private Integer cpuTotalNum;
    /**
     * 可用cpu
     */
    private Integer cpuAvailableNum;
    /**
     * 总内存
     */
    private Integer memoryCapacity;
    /**
     * 可用内存
     */
    private Integer memoryAvailableCapacity;
    /**
     * 域所有者
     */
    private String username;
    /**
     * 是否是根域
     */
    private Boolean rootDomain;
    
    /**
     * 备份存储
     */
    private Long backupStorageCapacity;
    /**
     * 可用备份存储
     */
    private Long availableBackupStorageCapacity;

    /**
     * 描述
     */
    private String desc;
    /**
     * 编码 域相当于一个树，code格式为XXXXXX，每两个X表示一层，总共100层，每层最多100个。
     */
    private String code;
    /**
     * 虚拟机数量
     */
    private Integer vmNum;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVmNum() {
        return vmNum;
    }

    public void setVmNum(Integer vmNum) {
        this.vmNum = vmNum;
    }

    public Boolean getRootDomain() {
        return rootDomain;
    }

    public void setRootDomain(Boolean rootDomain) {
        this.rootDomain = rootDomain;
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DomainVO) {
            DomainVO user = (DomainVO) obj;
            return this.getId().equals(user.getId());
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
