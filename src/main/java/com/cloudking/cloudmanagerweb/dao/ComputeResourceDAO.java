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
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * ResourceDAO
 * 
 * @author CloudKing
 */
@Repository("computeResourceDAO")
public final class ComputeResourceDAO extends BaseDAO<ComputeResourceEntity> {

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
                        "select resource_,resource_.machineRack.name,resource_.machineRack.machineRoom.name,"
                                        + "(select count(tb_vm.id) from ComputeResourceEntity tb_cr join tb_cr.virtualMachines tb_vm where tb_cr.id=resource_.id) "
                                        + "from ComputeResourceEntity resource_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  resource_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  resource_.machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  resource_.machineRack.id=:rackID ");
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
        StringBuilder queryQL = new StringBuilder("select count(*) from ComputeResourceEntity where 1=1");
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
     * 查询指定计算节点池下的计算节点
     * 
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryComputeResourceByPoolId(Long computeResourcePoolId) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select resource_,resource_.machineRack.name,resource_.machineRack.machineRoom.name,"
                                        + " (select count(tb_vm.id) from ComputeResourceEntity tb_cr join tb_cr.virtualMachines tb_vm where tb_cr.id=resource_.id) "
                                        + " from ComputeResourceEntity resource_ where resource_.computeResourcePool.id=:computeResourcePoolId"
                                        + " order by resource_.addTime asc");
        return (List<Object[]>) list(queryQL.toString(), "computeResourcePoolId", computeResourcePoolId);
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
                        "select count(*) from ComputeResourceEntity where machineRack.id=:rackID", "rackID",
                        machineRackID).toString());
    }

    /**
     * 按机资源名和机架ID查询
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourceEntity getByNameAndRack(String name, Long rackID) throws SQLException {
        return uniqueResult("from ComputeResourceEntity where name=:name and machineRack.id=:rackID", Arrays
                        .asList(new String[] { "name", "rackID" }), Arrays.asList(new Object[] { name, rackID }));
    }

    /**
     * 查询某个计算节点池下面的大于等于传入的CPU,内存值的计算节点（物理和超配两者都要满足）
     * 
     * @param cpu
     * @param memory
     * @param domainID
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourceEntity> queryResourceByPoolAndCpuMemory(Long poolID, Integer cpu, Integer memory)
                    throws SQLException {
        return list(
                        "select _cr from ComputeResourceEntity as _cr  inner join _cr.computeResourcePool  as _crp "
                                        + "   where _crp.id=:poolID and _cr.cpuAvailable>=:cpuAvailable and _cr.memoryAvailable>=:memoryAvailable "
                                        + " and _cr.cpu>=:cpu and _cr.memory>=:memory", Arrays.asList(new String[] {
                                        "poolID", "cpuAvailable", "memoryAvailable", "cpu", "memory" }), Arrays
                                        .asList(new Object[] { poolID, cpu, memory, cpu, memory }));
    }

    /**
     * 查询某个域下面计算节点
     * 
     * @param cpu
     * @param memory
     * @param domainID
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<ComputeResourceEntity> queryByDomain(Long domainID) throws SQLException {
        return list(
                        "select tb_cr from DomainEntity tb_domain inner join tb_domain.computeResourcePools  tb_crp inner join tb_crp.computeResources tb_cr "
                                        + "   where tb_domain.id=:domainID ", "domainID", domainID);
    }

    /**
     * 不分页查询资源
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是ComputeResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryNoPage(String name, Long machineRoomID, Long machineRackID) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select resource_,resourcePool_ from ComputeResourceEntity resource_ left join resource_.computeResourcePool resourcePool_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  resource_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  resource_.machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  resource_.machineRack.id=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        //排序
        queryQL.append("  order by resource_.addTime asc");
        return list(queryQL.toString(), paramNames, paramValues);
    }

    /**
     * 查找指定ip的计算资源
     * 
     * @param ip
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourceEntity getByIp(String ip) throws SQLException {
        return uniqueResult("from ComputeResourceEntity where ip = :ip", Arrays.asList(new String[] { "ip" }), Arrays
                        .asList(new Object[] { ip }));
    }

    /**
     * 获取计算节点的已用CPU和内存
     * 
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public Object[] getSumCPUAndMemory(Long id) throws SQLException {
        return (Object[]) uniqueResultObject(
                        "select sum(machineType_.cpu),sum(machineType_.memory) "
                                        + "from VirtualMachineEntity vm_ left join vm_.machineType machineType_ left join vm_.computeResource cr_ "
                                        + "where cr_.id = :computeResourceId ", Arrays
                                        .asList(new String[] { "computeResourceId" }), Arrays
                                        .asList(new Object[] { id }));
    }

    /**
     * 获取指定域下符合cpu和内存的计算节点
     * 
     * @param cpu
     * @param memory
     * @param domainId
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public ComputeResourceEntity getByCpuAndMemoryAndDomainId(Integer cpu, Integer memory, Long domainId)
                    throws SQLException {
        List<ComputeResourceEntity> tmpResult = (List<ComputeResourceEntity>) pageQuery(
                        "select compute_ from ComputeResourceEntity compute_ inner join compute_.computeResourcePool pool_ inner join pool_.domains domains_ "
                                        + " where compute_.cpu>=:cpu and compute_.cpuAvailable>=:cpu and compute_.memoryAvailable>=:memory and compute_.memory>=:memory and domains_.id=:domainId",
                        Arrays.asList(new String[] { "cpu", "memory", "domainId" }), Arrays.asList(new Object[] { cpu,
                                        memory, domainId }), 0, 1);
        if (tmpResult.isEmpty()) {
            return null;
        } else {
            return tmpResult.get(0);
        }
    }

    /**
     * 获取最优的CPU资源
     * 
     * @param domainID
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourceEntity getOptimalCpuCompute(Long domainID) throws SQLException {
        String sql = " select tb_2.ID_ " + "   from (select tb_computeresource. CPU_, "
                        + "                tb_computeresource.CPU_AVAILABLE_, "
                        + "                tb_computeresource.ID_ " + "           from tb_computeresource "
                        + "          inner join tb_computeresourcepool "
                        + "             on tb_computeresource.COMPUTERESOURCEPOOL_ID_ = "
                        + "                tb_computeresourcepool.ID_ " + "          inner join tb_domain_bid_pool "
                        + "             on tb_domain_bid_pool.POOL_ID_ = tb_computeresourcepool.ID_ "
                        + "          inner join tb_domain "
                        + "             on tb_domain_bid_pool.DOMAIN_ID_ = tb_domain.ID_ "
                        + "          where tb_domain.ID_ = 1 " + "            AND exists "
                        + "          (select max(tb_1.value_) value_2 " + "                   from (select case "
                        + "                                  when CPU_ >= CPU_AVAILABLE_ then "
                        + "                                   CPU_AVAILABLE_ "
                        + "                                  else " + "                                   CPU_ "
                        + "                                end value_ "
                        + "                           from tb_computeresource "
                        + "                          inner join tb_computeresourcepool "
                        + "                             on tb_computeresource.COMPUTERESOURCEPOOL_ID_ = "
                        + "                                tb_computeresourcepool.ID_ "
                        + "                          inner join tb_domain_bid_pool "
                        + "                             on tb_domain_bid_pool.POOL_ID_ = "
                        + "                                tb_computeresourcepool.ID_ "
                        + "                          inner join tb_domain "
                        + "                             on tb_domain_bid_pool.DOMAIN_ID_ = tb_domain.ID_ "
                        + "                          where tb_domain.ID_ =:domainID) tb_1 "
                        + "                 having CPU_ >= value_2 AND CPU_AVAILABLE_ >= value_2)) tb_2 "
                        + "       order by CPU_AVAILABLE_ DESC limit 1  ";
        Object tmpID = uniqueResultObjectBySQL(sql, "domainID", domainID);
        ComputeResourceEntity result = null;
        if (tmpID != null) {
            result = get(Long.valueOf(tmpID.toString()));
        }
        return result;
    }

    /**
     * 获取最优的内存资源
     * 
     * @param domainID
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourceEntity getOptimalMemoryCompute(Long domainID) throws SQLException {
        String sql = " select tb_2.ID_ " + "   from (select tb_computeresource. MEMORY_, "
                        + "                tb_computeresource.MEMORY_AVAILABLE_, "
                        + "                tb_computeresource.ID_ " + "           from tb_computeresource "
                        + "          inner join tb_computeresourcepool "
                        + "             on tb_computeresource.COMPUTERESOURCEPOOL_ID_ = "
                        + "                tb_computeresourcepool.ID_ " + "          inner join tb_domain_bid_pool "
                        + "             on tb_domain_bid_pool.POOL_ID_ = tb_computeresourcepool.ID_ "
                        + "          inner join tb_domain "
                        + "             on tb_domain_bid_pool.DOMAIN_ID_ = tb_domain.ID_ "
                        + "          where tb_domain.ID_ = 1 " + "            AND exists "
                        + "          (select max(tb_1.value_) value_2 " + "                   from (select case "
                        + "                                  when MEMORY_ >= MEMORY_AVAILABLE_ then "
                        + "                                   MEMORY_AVAILABLE_ "
                        + "                                  else " + "                                   MEMORY_ "
                        + "                                end value_ "
                        + "                           from tb_computeresource "
                        + "                          inner join tb_computeresourcepool "
                        + "                             on tb_computeresource.COMPUTERESOURCEPOOL_ID_ = "
                        + "                                tb_computeresourcepool.ID_ "
                        + "                          inner join tb_domain_bid_pool "
                        + "                             on tb_domain_bid_pool.POOL_ID_ = "
                        + "                                tb_computeresourcepool.ID_ "
                        + "                          inner join tb_domain "
                        + "                             on tb_domain_bid_pool.DOMAIN_ID_ = tb_domain.ID_ "
                        + "                          where tb_domain.ID_ =:domainID) tb_1 "
                        + "                 having MEMORY_ >= value_2 AND MEMORY_AVAILABLE_ >= value_2)) tb_2 "
                        + "       order by MEMORY_AVAILABLE_ DESC limit 1  ";
        Object tmpID = uniqueResultObjectBySQL(sql, "domainID", domainID);
        ComputeResourceEntity result = null;
        if (tmpID != null) {
            result = get(Long.valueOf(tmpID.toString()));
        }
        return result;
    }

    /**
     * @param longParam
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourceEntity> queryByRackId(Long rackId) throws SQLException {
        return list("from ComputeResourceEntity tb_cr where tb_cr.machineRack.id=:rackId", "rackId", rackId);
    }

    /**
     * 通过虚机Id获取
     * 
     * @param vmId
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourceEntity findByVmId(Long vmId) throws SQLException {
        return uniqueResult(
                        "select tb_cr from ComputeResourceEntity tb_cr inner join tb_cr.virtualMachines tb_vm where tb_vm.id=:vmId",
                        "vmId", vmId);
    }

    /**
     * 查询当前域内符合条件的计算节点
     * 
     * @param domainId
     * @param minCpu
     * @param minMemory
     * @return
     * @throws SQLException Sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourceEntity> queryByPoolIdAndMinCPUAndMinMemoryExcludVmCr(Long poolId, Integer minCpu,
                    Integer minMemory, Long crId) throws SQLException {
        return list(
                        "select distinct tb_cr from ComputeResourceEntity tb_cr "
                                        + "where tb_cr.computeResourcePool.id=:poolId and tb_cr.cpuAvailable>=:minCpu and tb_cr.memoryAvailable>=:minMemory and tb_cr.id<>:crId ",
                        Arrays.asList(new String[] { "poolId", "minCpu", "minMemory", "crId" }), Arrays
                                        .asList(new Object[] { poolId, minCpu, minMemory, crId }));
    }

    /**
     * 获取域的计算节点数量
     * @param id
     * @return
     * @throws SQLException sql异常
     */
    public Integer getComputeCountByDomainId(Long id) throws SQLException {
        return Integer.valueOf(uniqueResultObject("select count(tb_cr.id) from ComputeResourceEntity tb_cr inner join tb_cr.computeResourcePool tb_crp inner join tb_crp.domains tb_d where tb_d.id=:domainId","domainId",id).toString());
    }
}
