/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 工单解决情况
 * 
 * @author CloudKing
 * 
 */
public class WorkOrderSolutionVO extends BaseVO {
    /**
     *  工单标题
     */
    private String name;
    /**
     *  是否内置
     */
    private Boolean buildin;
    /**
     *  描述
     */
    private String desc;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getBuildin() {
        return buildin;
    }
    public void setBuildin(Boolean buildin) {
        this.buildin = buildin;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
