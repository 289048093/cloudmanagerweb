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
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 配置DAO
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unchecked")
@Repository("machineTypeDAO")
public final class MachineTypeDAO extends BaseDAO<MachineTypeEntity> {

    /**
     * 根据名字获取MachineType
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public MachineTypeEntity getByName(String name) throws SQLException {
        return uniqueResult("from MachineTypeEntity where name=:name", "name", name);
    }

    /**
     * 获取分页数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> query(String name, Long domainId, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select machineType_,domain_ from MachineTypeEntity machineType_ left join machineType_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  machineType_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 域
        if (domainId != null) {
            queryQL.append(" and  machineType_.domain.id=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        //排序
        queryQL.append("  order by machineType_.addTime asc");
        List<Object[]> resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 获取分页数据(查询指定域的所有上级数据)
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryAllPrev(String name, String domainCode, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select machineType_,domain_ from MachineTypeEntity machineType_ left join machineType_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  machineType_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 域
        if (!StringUtil.isBlank(domainCode)) {
            queryQL.append(" and :domainCode like concat(machineType_.domain.code,'%') ");
            paramNames.add("domainCode");
            paramValues.add(domainCode);
        }
        //排序
        queryQL.append("  order by machineType_.addTime asc");
        List<Object[]> resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 查询指定域下的机型
     * 
     * @param domainId
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<MachineTypeEntity> queryByDomain(Long domainId) throws SQLException {
        return (List<MachineTypeEntity>) list(
                        "select _type from MachineTypeEntity as _type inner join _type.domain as _domain where _domain.id=:domainId order by _type.addTime ASC",
                        "domainId", domainId);
    }

    /**
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String name, Long domainId) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(machineType_.id) from MachineTypeEntity machineType_ left join machineType_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  machineType_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 域
        if (domainId != null) {
            queryQL.append(" and  machineType_.domain.id=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCountAllPrev(String name, String domainCode) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(machineType_.id) from MachineTypeEntity machineType_ left join machineType_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  machineType_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 域
        if (StringUtil.isBlank(domainCode)) {
            queryQL.append(" and :domainCode like concat(machineType_.domain.code,'%')");
            paramNames.add("domainCode");
            paramValues.add(domainCode);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 
     * @param domainCode
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<MachineTypeEntity> queryByDomainCode(String domainCode) throws SQLException {
        return (List<MachineTypeEntity>) list(
                        "select _type from MachineTypeEntity as _type inner join _type.domain as _domain where _domain.code=:domainCode order by _type.addTime ASC",
                        "domainCode", domainCode);
    }

    /**
     * @param id
     * @throws Exception
     *             所有异常
     */
    public Integer getCountByDomainId(Long id) throws Exception {
        return Integer.parseInt(uniqueResultObject(
                        "select count(tb_mt.id) from MachineTypeEntity tb_mt where tb_mt.domain.id=:domainId ",
                        "domainId", id).toString());
    }
    
}
