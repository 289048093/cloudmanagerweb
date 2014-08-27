/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 20, 2012  5:59:27 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class VMSnapshotVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = 5041076067541903306L;
    /**
     * 名字
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 操作情况：是否在删除中等。。
     */
    private String operateFlag;

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

    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(String operateFlag) {
        this.operateFlag = operateFlag;
    }

}
