/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 机房
 * 
 * @author CloudKing
 * 
 */
public class MachineRoomVO extends BaseVO {
    /**
     * 基本信息
     */
    /**
     * 机房名字
     * 
     */
    private String name;
    /**
     * 机房描述
     * 
     */
    private String desc;

    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 机房状态
     * 
     */
    private String status;

    public String getName() {
        return name;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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

}
