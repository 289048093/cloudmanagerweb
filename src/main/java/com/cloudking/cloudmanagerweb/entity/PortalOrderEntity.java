/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 门户用户资源申请表
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_portalorder")
public class PortalOrderEntity extends BaseEntity { 
    /**
     * 申请备注
     */
    @Column(name = "APPLYMSG_")
    private String applyMsg; 
    
    /**
     * 有效期限
     */
    @Column(name = "DUETIMETYPE_")
    private String dueTimeType;
    
    /**
     * 订单与业务实体关系
     */
    @Column(name = "REFID_")
    private Long refId;
    

    /*
     * 关系
     */ 
    /**
     * 模板
     */
    @ManyToOne(cascade=CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID")
    private TemplateEntity template;
    
    /**
     * 配置
     */
    @ManyToOne(cascade=CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name = "MACHINETYPE_ID")
    private MachineTypeEntity machineType;
    
    /**
     * 申请人
     */
    @ManyToOne(cascade=CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICANT_ID_")
    private PortalUserEntity applicant; 
 
    public TemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(TemplateEntity template) {
        this.template = template;
    }
 

    public String getApplyMsg() {
        return applyMsg;
    }

    public void setApplyMsg(String applyMsg) {
        this.applyMsg = applyMsg;
    }
 
    public PortalUserEntity getApplicant() {
        return applicant;
    }

    public void setApplicant(PortalUserEntity applicant) {
        this.applicant = applicant;
    }

    public String getDueTimeType() {
        return dueTimeType;
    }

    public void setDueTimeType(String dueTimeType) {
        this.dueTimeType = dueTimeType;
    }
    public MachineTypeEntity getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineTypeEntity machineType) {
        this.machineType = machineType;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    } 
}