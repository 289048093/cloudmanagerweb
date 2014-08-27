/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudking.cloudmanagerweb.BaseEntity;

/**
 * 虚拟机模版实体
 * 
 * @author CloudKing
 * 
 */
@Entity
@Table(name = "tb_fileupload")
public class FileUploadEntity extends BaseEntity {

    /**
     * 文件名
     */
    @Column(name = "FILENAME_")
    private String filename;
    
    /**
     * md5
     */
    @Column(name = "MD5_")
    private String md5;

    /**
     * 上传时间
     */
    @Column(name = "CREATETIME_", nullable = false)
    private Date createTime;

 
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}