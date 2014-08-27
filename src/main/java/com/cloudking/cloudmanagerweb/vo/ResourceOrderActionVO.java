/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 资源工单动作
 * 
 * @author CloudKing
 * 
 */
public class ResourceOrderActionVO extends BaseVO {
    /**
     * 
     */
    private static final long serialVersionUID = -4884983007487372180L;
    /**
     * 
     */
    /**
     * 基本信息
     */
    /**
     * 动作描述
     */
    private String action;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 订单类型，1：资源操作，2：追加内容
     */
    private String type;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
