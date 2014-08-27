/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.PropertyManager;
import com.cloudking.cloudmanagerweb.entity.RightsEntity;

/**
 * RightsDAO
 * 
 * @author CloudKing
 */
@Repository("rightsDAO")
public final class RightsDAO extends BaseDAO<RightsEntity> {

    /**
     * 
     * 根据用户查找权限
     * 
     * @exception SQLException
     *                sql异常
     */
    @SuppressWarnings("unchecked")
    public List<RightsEntity> queryRightsByDomain(Long domainID) throws SQLException {  
        return list("select distinct _rights from DomainEntity as _domain inner join _domain.rights as _rights  where  _domain.id=:domainID",
                        "domainID", domainID);
    }
    
    /**
     * 
     * 查询域默认权限
     * 
     * @exception SQLException
     *                sql异常
     */
    @SuppressWarnings("unchecked")
    public List<RightsEntity> getDefaultDomainRights() throws SQLException {
        String defaultRightsUrl = PropertyManager.getInstance().getXMLProperty(PropertyManager.XML_CLOUDMANAGERWEB_DEFAULTDOMAINRIGHTS);
        StringBuilder rightsBuilder = new StringBuilder();
        if(defaultRightsUrl.length()>0){
            String []menuArr = defaultRightsUrl.split(",");
            for(String str : menuArr){
                rightsBuilder.append("'"+str.trim()+"',");
            }
        }
        if(rightsBuilder.length()>0){
            rightsBuilder = rightsBuilder.delete(rightsBuilder.length()-1,rightsBuilder.length());
        }
        return list("from RightsEntity as _rights where _rights.url in("+rightsBuilder.toString()+")");
    }
    
    /**
     * 
     * 查询当前菜单所有权限
     * 
     * @exception SQLException
     *                sql异常
     */
    @SuppressWarnings("unchecked")
    public List<RightsEntity> queryAllRightsOnThisMenu(Long menuId) throws SQLException {
        return list("select distinct _rights from MenuEntity as _menu inner join _menu.rights as _rights  where  _menu.id=:menuId",
                        "menuId", menuId);
    }
    
    /**
     * 
     * 查询当前域在该菜单上所拥有的权限
     * 
     * @exception SQLException
     *                sql异常
     */
    @SuppressWarnings("unchecked")
    public List<RightsEntity> queryDomainRightsOnThisMenu(Long menuId,Long domainId) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select distinct _rights from RightsEntity as _rights inner join _rights.domain as _domain inner join _rights.menu as _menu where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        
        queryQL.append(" and  _menu.id=:menuId");
        paramNames.add("menuId");
        paramValues.add(menuId);
        
        queryQL.append(" and _domain.id=:domainId");
        paramNames.add("domainId");
        paramValues.add(domainId);
        
        return list(queryQL.toString(),paramNames,paramValues);
    }

}
