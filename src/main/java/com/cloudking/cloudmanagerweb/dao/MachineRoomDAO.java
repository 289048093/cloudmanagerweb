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
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * machineRoomDAO
 * 
 * @author CloudKing
 */
@Repository("machineRoomDAO")
public final class MachineRoomDAO extends BaseDAO<MachineRoomEntity> {

    /**
     * 分页查询机房
     * 
     * @param name
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<MachineRoomEntity> query(String name, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder("from MachineRoomEntity  where 1=1");
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
        queryQL.append("  order by addTime asc");
        List<MachineRoomEntity> resultSet = (List<MachineRoomEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<MachineRoomEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 获取总数据数
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String name) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from MachineRoomEntity where 1=1");
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

    /**
     * 按机机房名查找
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public MachineRoomEntity getByName(String name) throws SQLException {
        return uniqueResult("from MachineRoomEntity where name=:name", "name", name);
    }
}
