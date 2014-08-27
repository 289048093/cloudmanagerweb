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
import com.cloudking.cloudmanagerweb.entity.WarnLogEntity;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 报警DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("warnLogDAO")
public final class WarnLogDAO extends BaseDAO<WarnLogEntity> {

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
    public List<WarnLogEntity> query(String desc, String equipmentIdentity, String startDate, String endDate,
                    PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder("select tb_wl from WarnLogEntity tb_wl where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_wl.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(equipmentIdentity)) {
            queryQL.append("and tb_wl.equipmentIdentity = :equipmentIdentity ");
            paramNames.add("equipmentIdentity");
            paramValues.add(equipmentIdentity);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wl.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wl.addTime <= :endDate ");
            paramNames.add("endDate");
            paramValues.add(DateUtil.parseDateTime(endDate));
        }
        //排序
        queryQL.append(" order by tb_wl.addTime desc");
        List<WarnLogEntity> resultSet = (List<WarnLogEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<WarnLogEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
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
    public Integer getQueryCount(String desc, String equipmentIdentity, String startDate, String endDate)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(tb_wl.id) from WarnLogEntity tb_wl  where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_wl.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(equipmentIdentity)) {
            queryQL.append("and tb_wl.equipmentIdentity = :equipmentIdentity ");
            paramNames.add("equipmentIdentity");
            paramValues.add(equipmentIdentity);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wl.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wl.addTime <= :endDate ");
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
        deleteByJPQL("delete from WarnLogEntity where addTime<:time", Arrays.asList("time"), Arrays
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
    public List<WarnLogEntity> queryNoPage(String desc, String equipmentIdentity, String startDate, String endDate)
                    throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder("select tb_wl from WarnLogEntity tb_wl where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(desc)) {
            queryQL.append(" and  tb_wl.desc like :desc ");
            paramNames.add("desc");
            paramValues.add("%" + desc + "%");
        }
        if (!StringUtil.isBlank(equipmentIdentity)) {
            queryQL.append("and tb_wl.equipmentIdentity = :equipmentIdentity ");
            paramNames.add("equipmentIdentity");
            paramValues.add(equipmentIdentity);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wl.addTime >= :startDate ");
            paramNames.add("startDate");
            paramValues.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wl.addTime <= :endDate ");
            paramNames.add("endDate");
            paramValues.add(DateUtil.parseDateTime(endDate));
        }
        //排序
        queryQL.append(" order by tb_wl.addTime desc");
        List<WarnLogEntity> resultSet = list(queryQL.toString(), paramNames, paramValues);
        return resultSet;
    }
}
