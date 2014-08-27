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
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 资源池DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("computeResourcePoolDAO")
public final class ComputeResourcePoolDAO extends BaseDAO<ComputeResourcePoolEntity> {

    /**
     * 通过名称查找
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourcePoolEntity getByName(String name) throws SQLException {
        return uniqueResult("from ComputeResourcePoolEntity where name = :name", "name", name);
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
    public List<Object[]> query(String name, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select tb_crp,(select count(tb_vm.id) from ComputeResourcePoolEntity tb_crp2 "
                                        + "left join tb_crp2.computeResources tb_cr2 left join tb_cr2.virtualMachines tb_vm "
                                        + "where tb_crp2.id=tb_crp.id)"
                                        + " from ComputeResourcePoolEntity tb_crp where 1=1 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  tb_crp.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //排序
        queryQL.append(" order by tb_crp.addTime asc");
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
    public Integer getQueryCount(String name) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from ComputeResourcePoolEntity where 1=1 ");
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
     * 清空资源池下的资源
     * 
     * @param id
     * @throws SQLException
     *             sql异常
     */
    public void clearResource(final Long id) throws SQLException {
        updateBySQL(
                        "update tb_computeresource set COMPUTERESOURCEPOOL_ID_ = null where COMPUTERESOURCEPOOL_ID_ = :computeResourcePoolID",
                        Arrays.asList(new String[] { "computeResourcePoolID" }), Arrays.asList(new Object[] { id }));
    }

    /**
     * 查询资源池和其所属域
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourcePoolEntity> queryPoolByDomainCode(String domainCode) throws SQLException {
        return list(
                        "select pool_ from ComputeResourcePoolEntity pool_ left join pool_.domains domain_ where domain_.code=:domainCode",
                        Arrays.asList(new String[] { "domainCode" }), Arrays.asList(new Object[] { domainCode }));
    }

    /**
     * 查询指定域所有的资源池
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourcePoolEntity> queryResourcePoolByCurrentDomain(Long domainId) throws SQLException {
        return list(
                        "select pool_ from ComputeResourcePoolEntity pool_ inner join pool_.domains domain_ where domain_.id=:domainId",
                        "domainId", domainId);
    }

    /**
     * 查询指定域所有的资源池
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourcePoolEntity> queryResourcePoolInSpecifyDomain(String domainIds) throws SQLException {
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
        return list("select DISTINCT(pool_) from ComputeResourcePoolEntity pool_ inner join pool_.domains domain_ where domain_.id in("
                        + domainIdsBuilder.toString() + ")");
    }

    /**
     * 插入pool和domain中间表
     * 
     * @param poolId
     * @param domainId
     * @throws SQLException
     *             sal异常
     */
    public void insertPoolBidDomain(Long poolId, Long domainId) throws SQLException {
        insertBySQL("insert into tb_domain_bid_pool(POOL_ID_,DOMAIN_ID_) values(:poolId,:domainId)", Arrays
                        .asList(new String[] { "poolId", "domainId" }), Arrays
                        .asList(new Object[] { poolId, domainId }));
    }

    /**
     * 清楚资源池下绑定的域名
     * 
     * @param poolId
     * @throws SQLException
     *             sql异常
     */
    public void clearDomainByPoolId(Long poolId) throws SQLException {
        deleteBySQL("delete from tb_domain_bid_pool where POOL_ID_=:poolId", Arrays.asList(new String[] { "poolId" }),
                        Arrays.asList(new Object[] { poolId }));
    }

    /**
     * 拿到池下的计算节点数
     * 
     * @param poolId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] sumComputeResourceWithCpuAndMemByPoolId(Long poolId) throws SQLException {
        return (Object[]) uniqueResultObject(
                        "select count(tb_cr.id),sum(tb_cr.cpu),sum(tb_cr.cpuAvailable),sum(tb_cr.memory),sum(tb_cr.memoryAvailable) "
                                        + " from ComputeResourcePoolEntity tb_crp "
                                        + "inner join tb_crp.computeResources tb_cr where tb_crp.id=:poolId", Arrays
                                        .asList(new String[] { "poolId" }), Arrays.asList(new Object[] { poolId }));
    }

    /**
     * 查询域计算节点池图表数据
     * 
     * @param domainId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] queryComputePoolChart(Long domainId) throws SQLException {
        return (Object[]) uniqueResultObject(
                        "select sum(tb_cr.cpu*tb_crp.cpuRate),sum(tb_cr.cpuAvailable),sum(tb_cr.memory*tb_crp.memoryRate),sum(tb_cr.memoryAvailable) "
                                        + " from ComputeResourcePoolEntity tb_crp "
                                        + " inner join tb_crp.domains tb_domains "
                                        + " inner join tb_crp.computeResources tb_cr where tb_domains.id=:domainId",
                        Arrays.asList(new String[] { "domainId" }), Arrays.asList(new Object[] { domainId }));
    }

    /**
     * 获取池下的计算节图表数据
     * 
     * @param poolId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] queryComputePoolChartByPoolId(Long poolId) throws SQLException {
        return (Object[]) uniqueResultObject(
                        "select sum(tb_cr.cpu*tb_crp.cpuRate),sum(tb_cr.cpuAvailable),sum(tb_cr.memory*tb_crp.memoryRate),sum(tb_cr.memoryAvailable) "
                                        + " from ComputeResourcePoolEntity tb_crp "
                                        + "inner join tb_crp.computeResources tb_cr where tb_crp.id=:poolId", Arrays
                                        .asList(new String[] { "poolId" }), Arrays.asList(new Object[] { poolId }));
    }

    /**
     * 用户所有域下获取池下的计算节图表数据
     * 
     * @param poolId
     * @return
     * @throws SQLException
     *             sql
     */
    public Object[] queryComputePoolChartInSpecifyDomains(String domainIds) throws SQLException {
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
        return (Object[]) uniqueResultObjectBySQL("select sum(tb_cr.CPU_*tb_crp.CPU_RATE_),sum(tb_cr.CPU_AVAILABLE_),sum(tb_cr.MEMORY_*tb_crp.MEMORY_RATE_),sum(tb_cr.MEMORY_AVAILABLE_) "
                        + " from ("
                        + " select * from  tb_computeresourcepool tb_crp2 where tb_crp2.ID_ in("
                        + " select DISTINCT(tb_crp3.ID_) from  tb_domain_bid_pool tb_d_p "
                        + " inner join tb_computeresourcepool tb_crp3 on tb_crp3.ID_=tb_d_p.POOL_ID_"
                        + " inner join tb_domain tb_domain on tb_domain.ID_=tb_d_p.DOMAIN_ID_ and  tb_domain.ID_ in("
                        + domainIdsBuilder.toString()
                        + ")"
                        + " )"
                        + ") tb_crp inner join tb_computeresource tb_cr on tb_cr.COMPUTERESOURCEPOOL_ID_=tb_crp.ID_ ");
    }

    /**
     * 查询资源池和其所属域
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<ComputeResourcePoolEntity> queryPoolWithVmCountByDomainCode(String domainCode) throws SQLException {
        return list(
                        "select pool_ "
                                        + "from ComputeResourcePoolEntity pool_ left join pool_.domains domain_ where domain_.code like :domainCode",
                        Arrays.asList(new String[] { "domainCode" }), Arrays.asList(new Object[] { domainCode + '%' }));
    }

    /**
     * 查找虚拟机的所在资源池
     * 
     * @param vmId
     * @return
     * @throws SQLException
     *             sql异常
     */
    public ComputeResourcePoolEntity findByVmId(Long vmId) throws SQLException {
        return uniqueResult(
                        "select tb_crp from ComputeResourcePoolEntity tb_crp inner join tb_crp.computeResources tb_cr inner join tb_cr.virtualMachines tb_vm "
                                        + "where tb_vm.id=:vmId", "vmId", vmId);
    }

    /**
     * 查询资源池数量
     * @param id
     * @return
     * @throws SQLException sql异常
     */
    public Integer getPoolCountByDomainId(Long id) throws SQLException {
        return Integer.valueOf(uniqueResultObject("select count(tb_crp.id) from ComputeResourcePoolEntity tb_crp inner join tb_crp.domains tb_d where tb_d.id=:domainId","domainId",id).toString());
    }
}
