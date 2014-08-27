/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.FileUploadEntity;

/**
 * 虚拟机模版DAO
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unchecked")
@Repository("fileUploadDAO")
public final class FileUploadDAO extends BaseDAO<FileUploadEntity> {

    /**
     * 查询文件上传表是否存在相同的md5
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer queryMD5Exsit(String md5) throws SQLException {
        return Integer
                        .parseInt(uniqueResultObject(
                                        "select count(tb_f.id) from FileUploadEntity tb_f where tb_f.md5=:md5 ", "md5",
                                        md5).toString());
    }
}
