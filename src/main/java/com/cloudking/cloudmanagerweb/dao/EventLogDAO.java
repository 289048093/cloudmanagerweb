/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.entity.EventLogEntity;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 事件DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("eventLogDAO")
public final class EventLogDAO extends BaseDAO<EventLogEntity> {

    /**
     * 获取分页数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> query(String desc, Long userId, String domainName, String startDate, String endDate,
                    PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select tb_el,tb_u from EventLogEntity tb_el inner join tb_el.user tb_u where tb_el.domainName in "
                                        + "(select distinct tb_d.name from DomainEntity tb_d,DomainEntity tb_d2 where tb_d2.code in "
                                        + "  (select tb_d3.code from DomainEntity tb_d3 where tb_d3.user.id=:userId) "
                                        + "and tb_d.code like concat(tb_d2.code,'%')) ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_el.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(domainName)) {
            queryQL.append(" and  tb_el.domainName = :domainName ");
            paramNames.add("domainName");
            paramValues.add(domainName);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_el.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_el.addTime <= :endDate ");
            paramNames.add("endDate");
            paramValues.add(DateUtil.parseDateTime(endDate));
        }
        //排序
        queryQL.append(" order by tb_el.addTime desc");
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
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String desc, Long userId, String domainName, String startDate, String endDate)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(tb_el.id) from EventLogEntity tb_el inner join tb_el.user tb_u where tb_el.domainName in "
                                        + "(select distinct tb_d.name from DomainEntity tb_d,DomainEntity tb_d2 where tb_d2.code in "
                                        + "  (select tb_d3.code from DomainEntity tb_d3 where tb_d3.user.id=:userId) "
                                        + "and tb_d.code like concat(tb_d2.code,'%')) ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_el.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(domainName)) {
            queryQL.append(" and  tb_el.domainName = :domainName ");
            paramNames.add("domainName");
            paramValues.add(domainName);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_el.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_el.addTime <= :endDate ");
            paramNames.add("endDate");
            paramValues.add(DateUtil.parseDateTime(endDate));
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * @param time
     * @throws SQLException
     *             sql异常
     */
    public void deleteByAddTime(Date time) throws SQLException {
        deleteByJPQL("delete from EventLogEntity where addTime<:time", Arrays.asList("time"), Arrays
                        .asList((Object) time));
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
    @SuppressWarnings("unchecked")
    public List<Object[]> queryNoPage(String desc, Long userId, String domainName, String startDate, String endDate)
                    throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select tb_el,tb_u from EventLogEntity tb_el inner join tb_el.user tb_u where tb_el.domainName in "
                                        + "(select distinct tb_d.name from DomainEntity tb_d,DomainEntity tb_d2 where tb_d2.code in "
                                        + "  (select tb_d3.code from DomainEntity tb_d3 where tb_d3.user.id=:userId) "
                                        + "and tb_d.code like concat(tb_d2.code,'%')) ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_el.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(domainName)) {
            queryQL.append(" and  tb_el.domainName = :domainName ");
            paramNames.add("domainName");
            paramValues.add(domainName);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_el.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_el.addTime <= :endDate ");
            paramNames.add("endDate");
            paramValues.add(DateUtil.parseDateTime(endDate));
        }
        //排序
        queryQL.append(" order by tb_el.addTime desc");
        List<Object[]> resultSet = (List<Object[]>) list(queryQL.toString(), paramNames, paramValues);
        return resultSet;
    }
}
