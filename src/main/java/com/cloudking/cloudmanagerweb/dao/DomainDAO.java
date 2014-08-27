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
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * DomainDAO
 * 
 * @author CloudKing
 */
@Repository("domainDAO")
public final class DomainDAO extends BaseDAO<DomainEntity> {

    /**
     * 
     * 按名称查询
     * 
     * @exception SQLException
     *                sql异常
     */
    public DomainEntity getByName(String name) throws SQLException {
        return uniqueResult("from DomainEntity where name=:name", "name", name);
    }

    /**
     * 获取数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<DomainEntity> query(String name, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder("from DomainEntity  where 1=1 ");
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
        queryQL.append("  order by code asc");
        return (List<DomainEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                        pageInfo.getLimit());
    }

    /**
     * 通过上级code查找下一级别的域
     * 
     * @param Code
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<DomainEntity> queryChildrenDomainByCode(String code) throws SQLException {
        return list("from DomainEntity where code like :code order by code", "code", code + "__");
    }

    /**
     * 获取后代节点
     * 
     * @param code
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<DomainEntity> queryDescendantDomainByCode(String code) throws SQLException {
        return list("from DomainEntity where code like :code order by code", "code", code + "%");
    }

    /**
     * 获取后代节点，和管理员，虚机数
     * 
     * @param code
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryDescendantDomainWithManager(String code) throws SQLException {
        return list(
                        "select tb_d,tb_u,(select count(tb_vm.id) from VirtualMachineEntity tb_vm inner join tb_vm.domain tb_d1 where tb_d1.id=tb_d.id) from DomainEntity tb_d left join tb_d.user tb_u where tb_d.code like :code order by tb_d.code",
                        "code", code + "%");
    }

    /**
     * 查找某个code上级符合name的域
     * 
     * @param domainEntity
     * @return
     * @throws SQLException
     *             sql异常
     */
    public DomainEntity findBySuperCodeAndName(final String superCode, final String name) throws SQLException {
        return uniqueResult("from DomainEntity where code like :code and name = :name", Arrays.asList(new String[] {
                        "code", "name" }), Arrays.asList(new Object[] { superCode + "__", name }));
    }

    
    /**
     * 根据域id获得存在管理员的域
     * 
     * @param domainEntity
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public DomainEntity getExistUserDomain(Long domainId) throws SQLException {
        return (DomainEntity)uniqueResultObject("select tb_domain from DomainEntity as tb_domain " 
                                            + " where tb_domain.user.id <> null  and tb_domain.id=:domainId",
                                            Arrays.asList("domainId"),Arrays.asList((Object)domainId));
    }

    /**
     * 通过父域code查询其下一等级的域
     * 
     * @param superDomainCode
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<DomainEntity> queryBySuperDomainCode(String superDomainCode) throws SQLException {
        return list("from DomainEntity where code like :code", "code", superDomainCode + "__");
    }
    
    /**
     * 通过子域code查询上级域
     * 
     * @param superDomainCode
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public DomainEntity querySuperDomainBySubDomainCode(String subDomainCode) throws SQLException {
        if(subDomainCode.length()>2){
            subDomainCode = subDomainCode.substring(0,subDomainCode.length()-2);
        }else{
            subDomainCode = "";
        }
        return (DomainEntity)uniqueResult("select tb_domain from DomainEntity tb_domain where tb_domain.code=:code", "code",subDomainCode);
    }

    /**
     * 通过code查找
     * 
     * @param code
     * @return
     * @throws SQLException
     *             sql异常
     */
    public DomainEntity findByCode(String code) throws SQLException {
        return uniqueResult("from DomainEntity where code = :code", "code", code);
    }

    /**
     * 插入用户到指定域
     * 
     * @param domainId
     * @param userId
     * @throws SQLException
     *             sql异常
     */
    public void addDomainBidUser(final Long domainId, final Long userId) throws SQLException {
        updateByJPQL("update DomainEntity set user.id=:userId where id=:domainId ", Arrays.asList(new String[] {
                        "domainId", "userId" }), Arrays.asList(new Object[] { domainId, userId }));
    }

    /**
     * 绑定domain和计算节点池关系
     * 
     * @param domainId
     * @param computeResourcePoolId
     * @throws SQLException
     *             sql异常
     */
    public void insertDomainBidComputeResourcePool(final Long domainId, final Long computeResourcePoolId)
                    throws SQLException {
        insertBySQL(
                        "insert into tb_domain_bid_computeresourcepool(DOMAIN_ID_,COMPUTERESOURCEPOOL_ID_) values(:domainId,:computeResourcePoolId)",
                        Arrays.asList(new String[] { "domainId", "computeResourcePoolId" }), Arrays
                                        .asList(new Object[] { domainId, computeResourcePoolId }));
    }

    /**
     * 绑定domain和存储资源池关系
     * 
     * @param domainId
     * @param computeResourcePoolId
     * @throws SQLException
     *             sql异常
     */
    public void insertDomainBidStorageResourcePool(final Long domainId, final Long storageResourcePoolId)
                    throws SQLException {
        insertBySQL(
                        "insert into tb_domain_bid_storageresourcepool(DOMAIN_ID_,STORAGERESOURCEPOOL_ID_) values(:domainId,:storageResourcePoolId)",
                        Arrays.asList(new String[] { "domainId", "storageResourcePoolId" }), Arrays
                                        .asList(new Object[] { domainId, storageResourcePoolId }));
    }

    /**
     * 删除域下指定的计算节点
     * 
     * @param domainId
     * @throws SQLException
     *             sql异常
     */
    public void deleteComputeResourPoolByDomainId(Long domainId) throws SQLException {
        deleteBySQL("delete from tb_domain_bid_pool where DOMAIN_ID_=:domainId ", Arrays
                        .asList(new String[] { "domainId" }), Arrays.asList(new Object[] { domainId }));
    }

    /**
     * 删除域下指定的存储节点
     * 
     * @param domainId
     * @throws SQLException
     *             sql异常
     */
    public void deleteStorageResourByDomainId(Long domainId) throws SQLException {
        deleteBySQL("delete from tb_domain_bid_storage where DOMAIN_ID_=:domainId ", Arrays
                        .asList(new String[] { "domainId" }), Arrays.asList(new Object[] { domainId }));
    }

    /**
     * 获取后代节点(不包括自己）
     * 
     * @param code
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<DomainEntity> queryExcludeMeDescendantDomainByCode(String code) throws SQLException {
        return list("from DomainEntity where code like :code order by code", "code", code + "_%");
    }

    /**
     * 删除资源池和域的关系
     * 
     * @param id
     * @param domainId
     * @throws SQLException
     *             sql异常
     */
    public void clearPoolWithDomain(Long poolId, Long domainId) throws SQLException {
        deleteBySQL("delete from tb_domain_bid_pool where DOMAIN_ID_=:domainId and POOL_ID_=:poolId", Arrays
                        .asList(new String[] { "domainId", "poolId" }), Arrays
                        .asList(new Object[] { domainId, poolId }));
    }

    /**
     * 查找用户的域
     * 
     * @param userId
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<DomainEntity> queryByUserId(Long userId) throws SQLException {
        return list("from DomainEntity tb_d where tb_d.user.id=:userId", "userId", userId);
    }

    /**
     * 
     * @param userId
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryManageAndDescendantDomainByUserId(Long userId) throws SQLException {
        return listBySQL(
                        "select tb_d.ID_,tb_d.AVAILABLESTORAGECAPACITY_,tb_d.CODE_,tb_d.CPU_AVAILABEL_NUM_,tb_d.CPU_TOTAL_NUM_,tb_d.DESC_,"
                                        + "tb_d.MEMORY_AVAILABLE_CAPACITY_,tb_d.MEMORY_CAPACITY_,tb_d.NAME_,tb_d.STORAGECAPACITY_,tb_d.BACKUPSTORAGE_CAPACITY_,tb_d.AVAILABLE_BACKUPSTORAGE_CAPACITY_,"
                                        + "tb_u.REALNAME_,tb_u.USERNAME_,"
                                        + "(select count(tb_vm.ID_) from tb_virtualmachine tb_vm inner join tb_domain tb_d1 on tb_vm.DOMAIN_ID_=tb_d1.ID_ where tb_d1.ID_=tb_d.ID_ ) "
                                        + "from tb_domain tb_d left join tb_user tb_u on tb_d.USER_ID_ = tb_u.ID_ where  tb_d.ID_  in "
                                        + "(select  distinct tb_1.ID_ from tb_domain as tb_1 ,"
                                        + "   (select code_ from tb_domain "
                                        + "       left join tb_user on tb_domain.USER_ID_ = tb_user.ID_ "
                                        + "               where tb_user.ID_ = :userId) as tb_2 "
                                        + " where  tb_1.code_ like concat(tb_2.code_,'%') )  order by tb_d.CODE_",
                        Arrays.asList("userId"), Arrays.asList((Object) userId));
    }

    /**
     * @param id
     * @return
     * @throws Exception
     *             所有异常
     */
    public int countByUserId(Long userId) throws Exception {
        return Integer.parseInt(uniqueResultObject(
                        "select count(t_d.id) from DomainEntity t_d where t_d.user.id=:userId", "userId", userId)
                        .toString());
    }
    /**
     * 获取自己的所有域及其后代域
     * 
     * @param userId
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<DomainEntity> queryDescendantDomainByUserId(Long userId) throws SQLException {
        return list("select distinct tb_d from DomainEntity tb_d,DomainEntity tb_d2 where tb_d2.code in "
                        + " (select tb_d3.code from DomainEntity tb_d3 where tb_d3.user.id=:userId) "
                        + " and tb_d.code like concat(tb_d2.code,'%') and tb_d2.user is not null order by tb_d.code ", "userId", userId);
    }
}
