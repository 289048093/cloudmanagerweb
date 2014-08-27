/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.sql.SQLException;
import java.util.Date;

import javax.persistence.EntityManager;

import com.cloudking.cloudmanagerweb.util.ProjectUtil;

/**
 * 所有Service的父类
 * 
 * @author CloudKing
 */
public abstract class BaseService {

    /**
     * 插入事件记录
     * 
     * @param log
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    protected void insertEventLog(String log, CloudContext<?> cloudContext)
                    throws SQLException {
        if (cloudContext == null) {
            return;
        }
        if (cloudContext.getLoginedUser() == null) {
            return;
        }
        if (cloudContext.getLoginedUser().getDomainID() == null) {
            return;
        }
        insertEventLog(log, cloudContext.getLoginedUser().getDomainID(),
                        cloudContext);
    }

    /**
     * 插入事件记录
     * 
     * @param log
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    protected void insertEventLog(String log, Long domainId,
                    CloudContext<?> cloudContext) throws SQLException {

        if (cloudContext == null) {
            return;
        }
        if (domainId == null) {
            return;
        }
        if (cloudContext.getLoginedUser() == null) {
            return;
        }
        if (cloudContext.getLoginedUser().getId() == null) {
            return;
        }

        EntityManager em = ProjectUtil.getEntityManager();
        em.getTransaction().begin();
        Long loginedUserId = cloudContext.getLoginedUser().getId();
        String desc = log;
        em.createNativeQuery(
                        "insert into tb_eventlog(USER_ID_,DOMAIN_NAME_,DESC_,ADDTIME_)"
                                        + " values(:userId,(select NAME_ from tb_domain where ID_=:domainId),:desc,:time)")
                        .setParameter("userId", loginedUserId)
                        .setParameter("domainId", domainId)
                        .setParameter("desc", desc)
                        .setParameter("time", new Date()).executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
