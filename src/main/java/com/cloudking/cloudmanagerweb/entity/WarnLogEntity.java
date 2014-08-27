/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Dec 6, 2012  10:04:29 AM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * @author CloudKing
 */
@Entity
@Table(name = "tb_warnlog")
public class WarnLogEntity extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 619979475308290285L;

    /**
     * 添加时间
     */
    @Column(name = "ADDTIME_", nullable = false)
    private Date addTime;

    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;
    /**
     * 设备标识
     */
    @Column(name="EQUIPMENT_IDENTITY_")
    private String equipmentIdentity;

    public String getEquipmentIdentity() {
        return equipmentIdentity;
    }

    public void setEquipmentIdentity(String equipmentIdentity) {
        this.equipmentIdentity = equipmentIdentity;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
