/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;

/**
 * 卷DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("volumnDAO")
public final class VolumnDAO extends BaseDAO<VolumnEntity> {
    /**
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getTotalSize(Long vmID) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select sum(tb_v.size) from VolumnEntity  tb_v where tb_v.virtualMachine.id=:vmID", "vmID", vmID)
                        .toString());
    }
    
    /**
     * 根据虚拟机Id查询卷
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<VolumnEntity> getVolumnsByVmId(Long vmID) throws SQLException {
        return (List<VolumnEntity>)list("from VolumnEntity  _volumn where _volumn.virtualMachine.id=:vmID", "vmID", vmID);
    }

}
