/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 工单
 * 
 * @author CloudKing
 * 
 */
public class WorkOrderVO extends BaseVO { 
    /**
     * 工单标题
     */
    private String title;
    /**
     * 工单内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 到期时间
     */
    private Date dueDate;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 解决时间
     */
    private Date solutionTime;
    /**
     * 关闭时间
     */
    private Date closeTime;
    /**
     * 延期时间
     */
    private Date delayTime;
    /**
     * 工单解决备注
     */
    private String solveMsg;
    /**
     * 工单状态
     */
    private String status;
    /**
     * 流水号
     */
    private String serialNumber;
    /**
     * 工单与业务实体关系Id
     */
    private Long refId;
    
    /**
     * 浏览工单发送域
     */
    private String sendDomainName;
    
    /**
     * 浏览工单接收域
     */
    private String receiveDomainName;
    
    /**
     * 创建人员 没有创建人员就是自动工单
     */
    private String sendUserName;
    /**
     * 接单人员
     */
    private String receiverName;
    /**
     * 工单类型 没有工单类型就是自动工单
     */
    private String categoryName;  
    /**
     * 工单类型Id
     */
    private Long categoryId;   
    /**
     * 工单解决方案
     */
    private String solutionName;
    
    /**
     * 附件计数器
     */
    private int attachmentCount;
    
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public Long getRefId() {
        return refId;
    }
    public void setRefId(Long refId) {
        this.refId = refId;
    }
    public String getSendUserName() {
        return sendUserName;
    }
    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getSolutionName() {
        return solutionName;
    }
    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getSolveMsg() {
        return solveMsg;
    }
    public void setSolveMsg(String solveMsg) {
        this.solveMsg = solveMsg;
    }
    public String getSendDomainName() {
        return sendDomainName;
    }
    public void setSendDomainName(String sendDomainName) {
        this.sendDomainName = sendDomainName;
    }
    public String getReceiveDomainName() {
        return receiveDomainName;
    }
    public void setReceiveDomainName(String receiveDomainName) {
        this.receiveDomainName = receiveDomainName;
    }
    public int getAttachmentCount() {
        return attachmentCount;
    }
    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    } 
}
