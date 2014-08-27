/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 工单
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_workorder")
public class WorkOrderEntity extends BaseEntity {
    /**
     * 基本信息
     */
    /**
     * 工单标题
     */
    @Column(name = "TITLE_", nullable = false)
    private String title;
    /**
     * 工单内容
     */
    @Lob
    @Column(name = "CONTENT_", nullable = false)
    @Basic(fetch = FetchType.LAZY)
    private String content;
    /**
     * 工单解决备注
     */
    @Lob
    @Column(name = "SOLVEMSG_")
    @Basic(fetch = FetchType.LAZY)
    private String solveMsg;
    /**
     * 创建时间
     */
    @Column(name = "CREATETIME_")
    private Date createTime;
    /**
     * 到期时间
     */
    @Column(name = "DUEDATE_")
    private Date dueDate;
    /**
     * 更新时间
     */
    @Column(name = "UPDATETIME_")
    private Date updateTime;
    /**
     * 解决时间
     */
    @Column(name = "SOLUTIONTIME_")
    private Date solutionTime;
    /**
     * 关闭时间
     */
    @Column(name = "CLOSETIME_")
    private Date closeTime;
    /**
     * 延期时间
     */
    @Column(name = "DELAYTIME_")
    private Date delayTime;
    
    /**
     * 上报时间
     */
    @Column(name = "REPORTTIME_")
    private Date reportTime;
    /**
     * 工单状态
     */
    @Column(name = "STATUS_", nullable = false)
    private String status;
    /**
     * 流水号
     */
    @Column(name = "SERIALNUMBER_", nullable = false)
    private String serialNumber;
    /**
     * 工单与业务实体关系Id
     */
    @Column(name = "REFID_")
    private Long refId;
    

    /**
     * 关系
     */
    /**
     * 接收域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVE_DOMAIN_ID_")
    private DomainEntity receiveDomain;
    
    /**
     * 发送域
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEND_DOMAIN_ID_")
    private DomainEntity sendDomain;
    
    /**
     * 创建人员 没有创建人员就是自动工单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID_")
    private UserEntity sendUser;
    /**
     * 接单人员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID_")
    private UserEntity receiver;
    /**
     * 工单类型 没有工单类型就是自动工单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID_")
    private WorkOrderCategoryEntity category;  
    /**
     * 工单解决方案
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLUTION_ID_")
    private WorkOrderSolutionEntity solution;

    /**
     * 工单的操作
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workOrder", fetch = FetchType.LAZY)
    @OrderBy("createTime DESC")
    private List<WorkOrderActionEntity> workOrderActions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserEntity getSendUser() {
        return sendUser;
    }

    public void setSendUser(UserEntity sendUser) {
        this.sendUser = sendUser;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public WorkOrderCategoryEntity getCategory() {
        return category;
    }

    public void setCategory(WorkOrderCategoryEntity category) {
        this.category = category;
    } 

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Date delayTime) {
        this.delayTime = delayTime;
    }

    public List<WorkOrderActionEntity> getWorkOrderActions() {
        return workOrderActions;
    }

    public void setWorkOrderActions(List<WorkOrderActionEntity> workOrderActions) {
        this.workOrderActions = workOrderActions;
    } 

    public WorkOrderSolutionEntity getSolution() {
        return solution;
    }

    public void setSolution(WorkOrderSolutionEntity solution) {
        this.solution = solution;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSolutionTime() {
        return solutionTime;
    }

    public void setSolutionTime(Date solutionTime) {
        this.solutionTime = solutionTime;
    } 

    public DomainEntity getReceiveDomain() {
        return receiveDomain;
    }

    public void setReceiveDomain(DomainEntity receiveDomain) {
        this.receiveDomain = receiveDomain;
    }

    public DomainEntity getSendDomain() {
        return sendDomain;
    }

    public void setSendDomain(DomainEntity sendDomain) {
        this.sendDomain = sendDomain;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getSolveMsg() {
        return solveMsg;
    }

    public void setSolveMsg(String solveMsg) {
        this.solveMsg = solveMsg;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

}
