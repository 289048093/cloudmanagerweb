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
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 用户DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("userDAO")
public final class UserDAO extends BaseDAO<UserEntity> {
    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public UserEntity findUserByUsername(String username) throws SQLException {
        return uniqueResult("from UserEntity where username=:username", "username", username);
    }

    /**
     * 根据用户名和密码查询用户
     * 
     * @param username
     *            用户名
     * @param password
     *            md5加密密码
     * @return 当前登录用户
     * @throws SQLException
     *             sql异常
     */
    public UserEntity findUserByUsernameAndPassword(String username, String password) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add("username");
        argNames.add("password");
        List<Object> args = new ArrayList<Object>();
        args.add(username);
        args.add(password);
        List<UserEntity> list = list(
                        " select tb_user  from UserEntity   tb_user where tb_user.username=:username and tb_user.password=:password ",
                        argNames, args);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
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
    public List<UserEntity> query(String realname, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder(
                        "select distinct user_ from UserEntity user_ where user_.deleteFlag = 0 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(realname)) {
            queryQL.append(" and  user_.realname like :realname ");
            paramNames.add("realname");
            paramValues.add("%" + realname + "%");
        }
        //排序
        queryQL.append(" order by user_.addTime asc");
        List<UserEntity> resultSet = (List<UserEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<UserEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
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
    public Integer getQueryCount(String realname) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(distinct user_) from UserEntity user_  where user_.deleteFlag = 0 ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(realname)) {
            queryQL.append(" and  user_.realname like :realname ");
            paramNames.add("realname");
            paramValues.add("%" + realname + "%");
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 删除该用户绑定的所有域
     * 
     * @param id
     * @throws SQLException
     *             sql异常
     */
    public void deleteDomainBidUser(final Long id) throws SQLException {
        updateByJPQL("update DomainEntity set user.id = null where user.id = :userId", Arrays
                        .asList(new String[] { "userId" }), Arrays.asList(new Object[] { id }));
    }

    /**
     * 查询没有删除的用户
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<UserEntity> queryNoDelete() throws SQLException {
        return list("from UserEntity where deleteFlag = 0");
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    public UserEntity findByTemplateId(Long templateId) throws SQLException {
        return uniqueResult(
                        "select tb_u from UserEntity tb_u inner join tb_u.domains tb_d inner join tb_d.templates tb_t where tb_t.id=:templateId",
                        "templateId", templateId);
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    public UserEntity findByMachineTypeId(Long mtId) throws SQLException {
        return uniqueResult(
                        "select tb_u from UserEntity tb_u inner join tb_u.domains tb_d inner join tb_d.machineTypes tb_mt where tb_mt.id=:mtId",
                        "mtId", mtId);
    }

    /**
     * @param domainId
     * @return
     * @throws SQLException sql异常
     */
    public UserEntity findByDomainId(Long domainId) throws SQLException {
        return uniqueResult("select tb_u from UserEntity tb_u inner join tb_u.domains tb_d where tb_d.id=:domainId", "domainId", domainId);
    }
}
