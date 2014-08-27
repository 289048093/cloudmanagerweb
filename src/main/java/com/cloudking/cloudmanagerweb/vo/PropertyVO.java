/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 属性VO
 * 
 * @author CloudKing
 * 
 */
public class PropertyVO extends BaseVO {
    /*
     * 基本信息
     */
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private String value;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 描述
     */
    private String desc;
    /**
     * 字段类型
     */
    private String type;

    /**
     * 正则表达式验证
     */
    private String regex;

    /**
     * 错误信息
     */
    private String errorMSG;

    /**
     * 获取默认值
     * 
     * @return
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 设置默认值
     * 
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 获取键
     * 
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置键
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取value
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取描述
     * 
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置描述
     * 
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public void setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
    }

}
