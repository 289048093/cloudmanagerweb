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
import com.cloudking.cloudmanagerweb.PropertyManager;
import com.cloudking.cloudmanagerweb.entity.MenuEntity;

/**
 * MenuDAO
 * 
 * @author CloudKing
 */
@Repository("menuDAO")
public final class MenuDAO extends BaseDAO<MenuEntity> {

    /**
     * 
     * 根据用户查找信息
     * 
     * @exception SQLException
     *                sql异常
     */
    @SuppressWarnings("unchecked")
    public List<MenuEntity> queryByDomain(Long domainID) throws SQLException {
        return list(
                        "select   _menu from DomainEntity as _domain inner join _domain.menu as _menu  "
                                        + " where  _domain.id=:domainID order by _menu.code asc", "domainID", domainID);
    }

    /**
     * 查询所有菜单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<MenuEntity> queryAllMenu() throws SQLException {
        return list("from MenuEntity as _menu");
    }

    /**
     * 查询域默认菜单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<MenuEntity> getDefaultDomainMenu() throws SQLException {
        String defaultMenuUrl = PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_DEFAULTDOMAINMENU); 
        StringBuilder menuBuilder = new StringBuilder();
        if (defaultMenuUrl.length() > 0) {
            String[] menuArr = defaultMenuUrl.split(",");
            for (String menuStr : menuArr) {
                menuBuilder.append("'" + menuStr.trim() + "',");
            }
        }
        if (menuBuilder.length() > 0) {
            menuBuilder = menuBuilder.delete(menuBuilder.length() - 1, menuBuilder.length());
        }
        String categoryMenuIds = getDefaultCategoryDomainMenuIds();
        return list("from MenuEntity as _menu where _menu.url in(" + menuBuilder.toString() + ") or _menu.id in("+categoryMenuIds+")");
    }
    
    /**
     * 获取默认大类别栏目菜单Ids
     */
    @SuppressWarnings("unchecked")
    private String getDefaultCategoryDomainMenuIds(){
        String defaultMenuUrl = PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_DEFAULTDOMAINCATEGORYMENU); 
        StringBuilder menuBuilder = new StringBuilder();
        if (defaultMenuUrl.length() > 0) {
            String[] menuArr = defaultMenuUrl.split(",");
            for (String menuStr : menuArr) {
                menuBuilder.append(menuStr.trim() + ",");
            }
        }
        if (menuBuilder.length() > 0) {
            menuBuilder = menuBuilder.delete(menuBuilder.length() - 1, menuBuilder.length());
        } 
        return menuBuilder.toString();
    }

    /**
     * 查询当前域所有的菜单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<MenuEntity> queryMenuOnThisDomain(Long domainId) throws SQLException {
        return list(
                        "select distinct _menu from DomainEntity as _domain inner join _domain.menu as _menu where  _domain.id=:domainId order by _menu.code asc",
                        "domainId", domainId);
    }

}
