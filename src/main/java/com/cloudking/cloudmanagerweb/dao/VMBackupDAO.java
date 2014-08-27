/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.VMBackupEntity;

/**
 * 备份DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("vMBackupDAO")
public final class VMBackupDAO extends BaseDAO<VMBackupEntity> {

    /**
     * 统计备文件份数量
     * @param id
     * @return
     * @throws SQLException sql异常
     */
    public Integer countByBackupStorageId(Long id) throws SQLException {
        Object obj = uniqueResultObject("select count(tb_bu.id) from VMBackupEntity tb_bu where tb_bu.backupStorage.id=:bsId", "bsId", id);
        return Integer.parseInt(obj.toString());
    }
    
}
