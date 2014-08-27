/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * MachineRackDAO
 * 
 * @author CloudKing
 */
@Repository("machineRackDAO")
public final class MachineRackDAO extends BaseDAO<MachineRackEntity> {

    /**
     * 分页查询机架
     * 
     * @param name
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> query(String name, Long machineRoomID, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select tb_rack,tb_rack.machineRoom.name from MachineRackEntity tb_rack  where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        if (machineRoomID != null) {
            queryQL.append(" and  machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //排序
        queryQL.append("  order by addTime asc");
        List<Object[]> resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames,
                        paramValues, pageInfo.getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                            .getStart(), pageInfo.getLimit());
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
    public Integer getQueryCount(String name, Long machineRoomID) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from MachineRackEntity where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        if (machineRoomID != null) {
            queryQL.append(" and  machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 按机机架名和机房ID查询
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public MachineRackEntity getByNameAndRoom(String name, Long roomID) throws SQLException {
        return uniqueResult("from MachineRackEntity where name=:name and machineRoom.id=:roomID", Arrays
                        .asList(new String[] { "name", "roomID" }), Arrays.asList(new Object[] { name, roomID }));
    }

    /**
     * 根据机房获取总数据数
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getCountByMachineRoom(Long machineRoomID) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from MachineRackEntity where machineRoom.id=:roomID", "roomID", machineRoomID)
                        .toString());
    }

    /**
     * @param longParam
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<MachineRackEntity> queryByRoomId(Long roomId) throws SQLException {
        return list("from MachineRackEntity where machineRoom.id=:roomId", "roomId", roomId);
    }
}
