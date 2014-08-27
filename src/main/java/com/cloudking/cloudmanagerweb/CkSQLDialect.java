/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
/**
 * 自定义SQL方言
 * @author CloudKing
 */
public class CkSQLDialect extends MySQLDialect {
    /**
     * 默认构造方法
     */
    public CkSQLDialect(){
        super();
        registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
    }

}