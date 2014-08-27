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
import com.cloudking.cloudmanagerweb.entity.NetWorkEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 网络DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("netWorkDAO")
public final class NetWorkDAO extends BaseDAO<NetWorkEntity> {

    /**
     * 通过名称查找
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public NetWorkEntity getByName(String name) throws SQLException {
        return uniqueResult("from NetWorkEntity where name = :name", "name", name);
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
    public List<NetWorkEntity> query(String name, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder("from NetWorkEntity  where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //排序
        queryQL.append(" order by addTime asc");
        List<NetWorkEntity> resultSet = (List<NetWorkEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<NetWorkEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String name) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from NetWorkEntity where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

}
