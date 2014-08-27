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
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * ResourceDAO
 * 
 * @author CloudKing
 */
@Repository("storageResourceDAO")
public final class StorageResourceDAO extends BaseDAO<StorageResourceEntity> {

    /**
     * 分页查询资源
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> query(String name, Long machineRoomID, Long machineRackID, PageInfo pageInfo)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select resource_,resource_.machineRack.name,resource_.machineRack.machineRoom.name  from StorageResourceEntity resource_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  machineRack.id=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        //排序
        queryQL.append("  order by resource_.addTime asc");

        List<Object[]> resultSet = pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
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
    public Integer getQueryCount(String name, Long machineRoomID, Long machineRackID) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from StorageResourceEntity where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  machineRack.id=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 按机资源名和机架ID查询
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public StorageResourceEntity getByNameAndRack(String name, Long rackID) throws SQLException {
        return uniqueResult("from StorageResourceEntity where name=:name and machineRack.id=:rackID", Arrays
                        .asList(new String[] { "name", "rackID" }), Arrays.asList(new Object[] { name, rackID }));
    }

    /**
     * 不分页查询资源
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是StorageResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryNoPage(String name, String type, Long machineRoomID, Long machineRackID)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select resource_,resourcePool_ from StorageResourceEntity resource_ left join resource_.storageResourcePool resourcePool_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 类型 
        if (!StringUtil.isBlank(type)) {
            queryQL.append(" and  type=:type ");
            paramNames.add("type");
            paramValues.add(type);
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  machineRack.id=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        //排序
        queryQL.append("  order by resource_.addTime asc");
        return list(queryQL.toString(), paramNames, paramValues);
    }

    /**
     * 查询存储资源和其关联的域
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryStorageWithDomain() throws SQLException {
        return list("select storageResource_,domain_ from StorageResourceEntity storageResource_ left join storageResource_.domains domain_");
    }

    /**
     * 插入存储和域的关系
     * 
     * @param storageId
     * @param domainId
     * @throws SQLException
     *             sql异常
     */
    public void insertStorageBidDomain(Long storageId, Long domainId) throws SQLException {
        insertBySQL("insert into tb_domain_bid_storage(DOMAIN_ID_,STORAGE_ID_) values(:domainId,:storageId)", Arrays
                        .asList(new String[] { "domainId", "storageId" }), Arrays.asList(new Object[] { domainId,
                        storageId }));
    }

    /**
     * 清除存储所绑定的所有域
     * 
     * @param storageId
     * @throws SQLException
     *             sql异常
     */
    public void clearDomainByStorageId(Long storageId) throws SQLException {
        deleteBySQL("delete from tb_domain_bid_storage where STORAGE_ID_=:storageId", Arrays
                        .asList(new String[] { "storageId" }), Arrays.asList(new Object[] { storageId }));
    }

    /**
     * 查询域下存储资源
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<StorageResourceEntity> queryStorageInSpecifyDomain(Long domainId) throws SQLException {
        return list(
                        "select storageResource_ from StorageResourceEntity storageResource_ inner join storageResource_.domain domain_ "
                                        + "where domain_.id=:domainId", "domainId", domainId);
    }

    /**
     * 根据机架获取总数据数
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getCountByMachineRack(Long machineRackID) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from StorageResourceEntity where machineRack.id=:rackID", "rackID",
                        machineRackID).toString());
    }

    /**
     * 查找指定Ip的存储,一个ip只能创建一个存储
     * 
     * @param ip
     * @return
     * @throws SQLException
     *             sql异常
     */
    public StorageResourceEntity findStorageByIp(String ip) throws SQLException {
        return (StorageResourceEntity) uniqueResult("from StorageResourceEntity where ip = :ip", Arrays
                        .asList(new String[] { "ip" }), Arrays.asList(new Object[] { ip }));
    }

    /**
     * 查询指定域下符合硬盘大小的存储最小磁盘
     * 
     * @param capacity
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public StorageResourceEntity getByCapacity(Long capacity) throws SQLException {
        List<StorageResourceEntity> tmpResults = pageQuery(" from StorageResourceEntity  "
                        + " where availableCapacity>=:capacity order by availableCapacity ASC", Arrays
                        .asList(new String[] { "capacity" }), Arrays.asList(new Object[] { capacity }), 0, 1);
        StorageResourceEntity result = null;
        if (!tmpResults.isEmpty()) {
            result = tmpResults.get(0);
        }
        return result;
    }

    /**
     * 获取最大的存储资源
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public StorageResourceEntity getMaxDiskStorage() throws SQLException {
        List<StorageResourceEntity> tmpResults = pageQuery(
                        "from StorageResourceEntity    order by availableCapacity DESC", 0, 1);
        StorageResourceEntity result = null;
        if (!tmpResults.isEmpty()) {
            result = tmpResults.get(0);
        }
        return result;
    }

    /**
     * 获取域下的存储图表数据
     * 
     * @param domainId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] sumStorageResourceChartByDomain(Long domainId) throws SQLException {
        return (Object[]) uniqueResultObject("select sum(tb_sr.capacity),sum(tb_sr.availableCapacity) "
                        + " from StorageResourceEntity tb_sr "
                        + "inner join tb_sr.domain tb_doamin where tb_doamin.id=:domainId", Arrays
                        .asList(new String[] { "domainId" }), Arrays.asList(new Object[] { domainId }));
    }

    /**
     * 获取指定池下的存储图表数据
     * 
     * @param storageId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] sumStorageResourceChartById(Long storageId) throws SQLException {
        return (Object[]) uniqueResultObject("select sum(tb_sr.capacity),sum(tb_sr.availableCapacity) "
                        + " from StorageResourceEntity tb_sr where tb_sr.id=:storageId", Arrays
                        .asList(new String[] { "storageId" }), Arrays.asList(new Object[] { storageId }));
    }

    /**
     * 域下指定池下的存储图表数据
     * 
     * @param storageId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] sumStorageResourceChartInSpecifyDomains(String domainIds) throws SQLException {
        StringBuilder domainIdsBuilder = new StringBuilder();
        if (domainIds.length() > 0) {
            String[] tmpArr = domainIds.split(",");
            for (String str : tmpArr) {
                domainIdsBuilder.append("'" + str.trim() + "',");
            }
        }
        if (domainIdsBuilder.length() > 0) {
            domainIdsBuilder = domainIdsBuilder.delete(domainIdsBuilder.length() - 1, domainIdsBuilder.length());
        }
        return (Object[]) uniqueResultObjectBySQL("select sum(tb_sr.CAPACITY_),sum(tb_sr.AVAILABLE_CAPACITY_) "
                        + " from (select tb_sr1.CAPACITY_,tb_sr1.AVAILABLE_CAPACITY_ from tb_storageresource tb_sr1 "
                        + " inner join tb_domain tb_d on tb_d.ID_= tb_sr1.DOMAIN_ID_ and tb_d.ID_ in ("
                        + domainIdsBuilder.toString() + ")" + ") tb_sr ");
    }

    /**
     * 域下指定池下的存储
     * 
     * @param storageId
     * @return
     * @throws SQLException
     *             sql
     */
    public List<StorageResourceEntity> queryStorageResourceInSpecifyDomains(String domainIds) throws SQLException {
        StringBuilder domainIdsBuilder = new StringBuilder();
        if (domainIds.length() > 0) {
            String[] tmpArr = domainIds.split(",");
            for (String str : tmpArr) {
                domainIdsBuilder.append("'" + str.trim() + "',");
            }
        }
        if (domainIdsBuilder.length() > 0) {
            domainIdsBuilder = domainIdsBuilder.delete(domainIdsBuilder.length() - 1, domainIdsBuilder.length());
        }
        return (List<StorageResourceEntity>) list("select DISTINCT(tb_sr) "
                        + " from StorageResourceEntity tb_sr where tb_sr.domain.id in (" + domainIdsBuilder.toString()
                        + ")");
    }

    /**
     * 获得域的最大存储
     * 
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    public StorageResourceEntity getMaxDiskStorageByDomainId(Long id) throws SQLException {
        return uniqueResult(
                        "select tb_sr from StorageResourceEntity tb_sr where tb_sr.availableCapacity = (select max(tb_sr1.availableCapacity) from StorageResourceEntity tb_sr1 where tb_sr1.domain.id=:domainId)",
                        "domainId", id);
    }
}
