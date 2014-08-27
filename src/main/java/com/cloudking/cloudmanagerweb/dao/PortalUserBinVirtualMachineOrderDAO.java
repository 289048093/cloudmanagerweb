/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.Arrays;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.PortalUserBinVirtualMachineEntity;

/**
 * 属性DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("portalUserBinVirtualMachineOrderDAO")
public class PortalUserBinVirtualMachineOrderDAO extends BaseDAO<PortalUserBinVirtualMachineEntity> {
    /**
     * 根据虚机删除
     * 
     * @param long2
     * @param long1
     * @param string
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public void deleteByVmId(Long vmId) throws SQLException {
        deleteBySQL("delete from tb_portaluserbinvirtualmachine where    VIRTUALMACHINE_ID=:domainID", Arrays
                        .asList("domainID"), Arrays.asList((Object) vmId));
    }
}
