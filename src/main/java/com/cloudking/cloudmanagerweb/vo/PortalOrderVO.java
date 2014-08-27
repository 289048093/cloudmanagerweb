/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 19, 2012  4:00:16 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class PortalOrderVO extends BaseVO {
    /**
     * CPU
     */
    private Integer cpu;

    /**
     * 内存
     */
    private Integer memory;

    /**
     * 硬盘
     */
    private Integer disk;

    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 配置
     */
    private String machineTypeArgs;
    

    /**
     * 订单状态 1:审批中 2:审批通过 3:审批拒绝(包括客观拒绝)
     */
    private String status;

    /**
     * portal用户资源申请的域Id,与本系统域相关联
     */
    private String domainName;
    
    /**
     * 有效期限
     */
    private String dueTimeType;

    /**
     * 申请时间
     */
    private Date applyTime;
    
    /**
     * 申请时间至(用以搜索)
     */
    private Date applyTimeTo;

    /**
     * 申请备注
     */
    private String applyMsg;

    /**
     * 审批时间
     */
    private Date handleTime;

    /**
     * 审批备注
     */
    private String handleMsg;

    /**
     * 申请人
     */
    private String applicantName;

    /**
     * 审批人
     */
    private String handlerName;

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
    } 

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }  

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyMsg() {
        return applyMsg;
    }

    public void setApplyMsg(String applyMsg) {
        this.applyMsg = applyMsg;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Date getApplyTimeTo() {
        return applyTimeTo;
    }

    public void setApplyTimeTo(Date applyTimeTo) {
        this.applyTimeTo = applyTimeTo;
    }

    public String getMachineTypeArgs() {
        return machineTypeArgs;
    }

    public void setMachineTypeArgs(String machineTypeArgs) {
        this.machineTypeArgs = machineTypeArgs;
    }

    public String getDueTimeType() {
        return dueTimeType;
    }

    public void setDueTimeType(String dueTimeType) {
        this.dueTimeType = dueTimeType;
    } 
}
