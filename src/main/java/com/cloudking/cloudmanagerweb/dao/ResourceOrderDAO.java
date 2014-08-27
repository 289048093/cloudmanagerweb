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
import com.cloudking.cloudmanagerweb.entity.ResourceOrderEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * ResourceOrderDAO
 * 
 * @author CloudKing
 */
@Repository("resourceOrderDAO")
public final class ResourceOrderDAO extends BaseDAO<ResourceOrderEntity> {

    /**
     * 对于发送则要查询的工单
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryForSend(String title, String status, Long sendDomainId, Long domainID, PageInfo pageInfo)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_ro,tb_domain.user.realname  from ResourceOrderEntity tb_ro inner join  tb_ro.sendDomain tb_domain "
                                        + " where   tb_domain.id=:domainID");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("domainID");
        paramValues.add(domainID);
        // 标题
        if (!StringUtil.isBlank(title)) {
            queryQL.append(" and  tb_ro.title like :title ");
            paramNames.add("title");
            paramValues.add("%" + title + "%");
        }
        // 状态
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  tb_ro.status=:status ");
            paramNames.add("status");
            paramValues.add(status);
        }
        // 状态
        if (sendDomainId != null) {
            queryQL.append(" and  tb_ro.sendDomain.id=:sendDomainId ");
            paramNames.add("sendDomainId");
            paramValues.add(sendDomainId);
        }
        //排序
        queryQL.append("  order by tb_ro.createTime asc");

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
    public Integer getQueryCountForSend(String title, String status, Long sendDomainId, Long domainID)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from ResourceOrderEntity tb_ro  inner join tb_ro.sendDomain tb_domain"
                                        + " where  tb_domain.id=:domainID");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("domainID");
        paramValues.add(domainID);
        // 标题
        if (!StringUtil.isBlank(title)) {
            queryQL.append(" and  tb_ro.title like :title ");
            paramNames.add("title");
            paramValues.add("%" + title + "%");
        }
        // 状态
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  tb_ro.status=:status ");
            paramNames.add("status");
            paramValues.add(status);
        }
        // 状态
        if (sendDomainId != null) {
            queryQL.append(" and  tb_ro.sendDomain.id=:sendDomainId ");
            paramNames.add("sendDomainId");
            paramValues.add(sendDomainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }


    /**
     * 获取处理中的资源单数据
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> getHandlingOrder(Long domainID, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_ro,tb_domain.user.realname  from ResourceOrderEntity tb_ro   join tb_ro.receiveDomain tb_domain"
                                        + "  where tb_ro.status=:status and tb_domain.id=:domainID");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();

        paramNames.add("status");
        paramValues.add(Constant.RESOURCEORDER_ACCEPING);

        paramNames.add("domainID");
        paramValues.add(domainID);

        //排序
        queryQL.append("  order by tb_ro.createTime asc");

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
     * 对于接收者要查询的
     * 
     * @param name
     * @param start
     * @param limit
     * @param receiveDomainId
     *            接收者，当前登录的用户某个域接收的工单
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryForRecive(String title, String status, Long receiveDomainId, Long domainID,
                    PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_ro,tb_ro.sendDomain.user.realname from ResourceOrderEntity tb_ro   join tb_ro.receiveDomain tb_domain"
                                        + "  where   tb_domain.id=:domainID");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("domainID");
        paramValues.add(domainID);
        // 标题
        if (!StringUtil.isBlank(title)) {
            queryQL.append(" and  tb_ro.title like :title ");
            paramNames.add("title");
            paramValues.add("%" + title + "%");
        }
        // 状态
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  tb_ro.status=:status ");
            paramNames.add("status");
            paramValues.add(status);
        }
        // 状态
        if (receiveDomainId != null) {
            queryQL.append(" and  tb_ro.receiveDomain.id=:receiveDomainId ");
            paramNames.add("receiveDomainId");
            paramValues.add(receiveDomainId);
        }
        //排序
        queryQL.append("  order by tb_ro.createTime asc");

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
    public Integer getQueryCountForRecive(String title, String status, Long receiveDomainId, Long domainID)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from ResourceOrderEntity tb_ro inner join tb_ro.receiveDomain tb_domain"
                                        + "  where   tb_domain.id=:domainID");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("domainID");
        paramValues.add(domainID);
        // 标题
        if (!StringUtil.isBlank(title)) {
            queryQL.append(" and  tb_ro.title like :title ");
            paramNames.add("title");
            paramValues.add("%" + title + "%");
        }
        // 状态
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  tb_ro.status=:status ");
            paramNames.add("status");
            paramValues.add(status);
        }
        // 状态
        if (receiveDomainId != null) {
            queryQL.append(" and  tb_ro.receiveDomain.id=:receiveDomainId ");
            paramNames.add("receiveDomainId");
            paramValues.add(receiveDomainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }
}
