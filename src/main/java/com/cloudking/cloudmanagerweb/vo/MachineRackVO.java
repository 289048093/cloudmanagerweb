/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 机架
 * 
 * @author CloudKing
 * 
 */
public class MachineRackVO extends BaseVO {
    /**
     * 基本信息
     */
    /**
     * 机架名字
     */
    private String name;
    /**
     * 机架描述
     * 
     */
    private String desc;
    /**
     * 机柜状态(true:报警 false:不报警)
     * 
     */
    private Boolean warn;

    /**
     * 是否导致机房报警
     * 
     */
    private Boolean warn4Room;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 机房名字
     */
    private String roomName;

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return
     */
    public Boolean getWarn4Room() {
        return warn4Room;
    }

    /**
     * 
     * @param warn4Room
     */
    public void setWarn4Room(Boolean warn4Room) {
        this.warn4Room = warn4Room;
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
     * @return the warn
     */
    public Boolean getWarn() {
        return warn;
    }

    /**
     * @param warn
     *            the warn to set
     */
    public void setWarn(Boolean warn) {
        this.warn = warn;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
