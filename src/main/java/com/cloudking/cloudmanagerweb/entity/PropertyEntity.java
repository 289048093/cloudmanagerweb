/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 属性
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_property")
public class PropertyEntity extends BaseEntity {
    /**
     * 键
     */
    @Column(name = "KEY_", nullable = false, unique = true)
    private String key;
    /**
     * 值
     */
    @Column(name = "VALUE_", nullable = false)
    private String value;

    /**
     * 默认值
     */
    @Column(name = "DEFAULTVALUE_")
    private String defaultValue;

    /**
     * 字段类型
     */
    @Column(name = "TYPE_", length = 20)
    private String type;

    /**
     * 正则表达式验证
     */
    @Column(name = "REGEX_")
    private String regex;
    /**
     * 正则表达式错误信息
     */
    @Column(name = "ERRORMSG_")
    private String errorMSG;
    /**
     * 描述
     */
    @Column(name = "DESC_")
    private String desc;

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
     * 获取值
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值
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
