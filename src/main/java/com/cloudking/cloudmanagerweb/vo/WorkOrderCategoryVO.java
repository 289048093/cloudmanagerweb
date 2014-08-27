/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 22, 2012  2:46:14 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 * 
 */
public class WorkOrderCategoryVO extends BaseVO {
    /**
     * 类别名称
     */
    private String name;
    /**
     * 类别描述
     */
    private String desc;
    /**
     * 是否内置
     */
    private Boolean buildin;
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
    public Boolean getBuildin() {
        return buildin;
    }
    public void setBuildin(Boolean buildin) {
        this.buildin = buildin;
    } 

}
