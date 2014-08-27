/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 菜单VO
 * 
 * @author CloudKing
 * 
 */
public class MenuVO extends BaseVO {
    /**
     * 基本信息
     */
    private String name;
    /**
     * 模块描述
     */
    private String desc;
    /**
     * url地址
     */
    private String url; 

    /**
     * 层级及顺序编号
     */
    private String code;

    /**
     * 图片
     */
    private String img;
    /**
     * 是否拥有该菜单
     */
    private boolean hasMenuFlag;

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     *            the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

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
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     *            the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    } 
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getHasMenuFlag() {
        return hasMenuFlag;
    }

    public void setHasMenuFlag(Boolean hasMenuFlag) {
        this.hasMenuFlag = hasMenuFlag;
    }
}
