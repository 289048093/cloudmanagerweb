/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.PortalUserEntity;

/**
 * 属性DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("portalUserDAO")
public class PortalUserDAO extends BaseDAO<PortalUserEntity> {
    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public PortalUserEntity getUserByUserID(String userID) throws SQLException {
        return uniqueResult("from PortalUserEntity as _user where _user.userId=:userId", "userId", userID);
    }

}
