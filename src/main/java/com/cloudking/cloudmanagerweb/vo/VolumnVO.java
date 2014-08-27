/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 卷
 * 
 * @author CloudKing
 */
public class VolumnVO extends BaseVO {
    /**
     * 名字
     */
    private String name;
    /**
     * 卷大小
     */
    private Long size;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 映像卷标记位
     */
    private String imageVolumnFlag;

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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getImageVolumnFlag() {
        return imageVolumnFlag;
    }

    public void setImageVolumnFlag(String imageVolumnFlag) {
        this.imageVolumnFlag = imageVolumnFlag;
    }

}
