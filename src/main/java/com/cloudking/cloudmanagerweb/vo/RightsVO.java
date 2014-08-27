/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * 配置VO
 * 
 * @author CloudKing
 * 
 */
public class RightsVO extends BaseVO {
    /**
     * 名字
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 是否拥有此权限
     */
    private boolean hasRights;
    
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Boolean getHasRights() {
        return hasRights;
    }
    public void setHasRights(Boolean hasRights) {
        this.hasRights = hasRights;
    }

  
}