/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 17, 2012  1:37:44 PM
 */
package com.cloudking.cloudmanagerweb.vo;

import java.util.Date;

import com.cloudking.cloudmanagerweb.BaseVO;

/**
 * @author CloudKing
 */
public class TemplateVO extends BaseVO implements Comparable<TemplateVO> {
    /**
     * 名字
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 状态 1，表示正常，0，表示正在下载文件，-1表示失败。
     * 
     * @return
     */
    private Integer status;

    /**
     * 回显消息
     */
    private Integer type;

    /**
     * 镜像文件名
     * 
     * @return
     */
    private String fileName;

    /**
     * URL地址
     */
    private String url;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 域名
     */
    private String domainName;
    /**
     * 域code
     */
    private String domainCode;

    /**
     * 模板用户
     */
    private String username;

    /**
     * 模板密码
     */
    private String password;
    /**
     * 是否是自己的模版
     */
    private Boolean selfTemplate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
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

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TemplateVO o) {
        return this.getAddTime().compareTo(o.getAddTime());
    }

    /**
     * 重写equals
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof TemplateVO) {
            TemplateVO templateVO = (TemplateVO) obj;
            return this.getId().equals(templateVO.getId());
        }
        return false;
    }

    /**
     * 重写hashcode
     */
    @Override
    public int hashCode() {
        return this.getId().intValue();
    }

    public Boolean getSelfTemplate() {
        return selfTemplate;
    }

    public void setSelfTemplate(Boolean selfTemplate) {
        this.selfTemplate = selfTemplate;
    }

}
