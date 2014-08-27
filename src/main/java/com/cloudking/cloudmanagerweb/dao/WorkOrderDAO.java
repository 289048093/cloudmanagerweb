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
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * @author CloudKing
 * 
 */
@Repository("workOrderDAO")
public class WorkOrderDAO extends BaseDAO<WorkOrderEntity> {

    /**
     * 指定域下的处理中的我的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getHandlingWorkOrderInSpecifyDoaminCount(Long domainId) throws SQLException {
        String queryQL = "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain"
                        + " where  tb_domain.id=:domainId and tb_wo.status=:status ";
        return Integer.parseInt(uniqueResultObject(queryQL, Arrays.asList("domainId", "status"),
                        Arrays.asList((Object) domainId, (Object) Constant.WORKORDER_ACCEPING)).toString());
    }

    /**
     * 查询我发送的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer querySendWorkOrder(String qTitle, Long qCategory, String startDate, String endDate, Long domainId,Long receiveDomainId)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.sendDomain tb_domain where tb_domain.id=:domainId ");
        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //域
        nameArgs.add("domainId");
        valueArgs.add(domainId);

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }

        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(receiveDomainId!=null){
            queryQL.append(" and tb_wo.receiveDomain.id=:receiveDomainId ");
            nameArgs.add("receiveDomainId");
            valueArgs.add(receiveDomainId);
        }

        return Integer.parseInt(uniqueResultObject(queryQL.toString(), nameArgs, valueArgs).toString());
    }

    /**
     * 查询我接收的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer queryReceiveWorkOrder(String qTitle, Long qCategory, String startDate, String endDate, Long domainId,Long sendDomainId)
                    throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain where tb_domain.id=:domainId ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //域
        nameArgs.add("domainId");
        valueArgs.add(domainId);

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(sendDomainId!=null){
            queryQL.append(" and tb_wo.sendDomain.id = :sendDomainId ");
            nameArgs.add("sendDomainId");
            valueArgs.add(sendDomainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), nameArgs, valueArgs).toString());
    }

    /**
     * 用户所有域下的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer queryReceiveWorkOrderInAllDoaminsCount(String qTitle, Long qCategory, String startDate,
                    String endDate, String domainIds,Long sendDomainId) throws SQLException {
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

        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain where tb_domain.id in ("
                                        + domainIdsBuilder.toString() + ") ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(sendDomainId!=null){
            queryQL.append(" and tb_wo.sendDomain.id=:sendDomainId ");
            nameArgs.add("sendDomainId");
            valueArgs.add(sendDomainId);
        }

        return Integer.parseInt(uniqueResultObject(queryQL.toString(), nameArgs, valueArgs).toString());
    }

    /**
     * 用户所有域下我发送的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer querySendWorkOrderInAllDoaminsCount(String qTitle, Long qCategory, String startDate, String endDate,
                    String domainIds,Long receiveDomainId) throws SQLException {
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

        StringBuilder queryQL = new StringBuilder(
                        "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.sendDomain tb_domain where tb_domain.id in ("
                                        + domainIdsBuilder.toString() + ") ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }

        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(receiveDomainId!=null){
            queryQL.append(" and tb_wo.receiveDomain.id=:receiveDomainId ");
            nameArgs.add("receiveDomainId");
            valueArgs.add(receiveDomainId);
        }

        return Integer.parseInt(uniqueResultObject(queryQL.toString(), nameArgs, valueArgs).toString());
    }

    /**
     * 查询我接收的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryReceiveWorkOrder(String qTitle, Long qCategory, String startDate, String endDate,
                    Long domainId,Long sendDomainId, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_wo,(select count(tb_woa.id) as acount from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id)"
                                        + " from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain where tb_domain.id=:domainId ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //域
        nameArgs.add("domainId");
        valueArgs.add(domainId);

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(sendDomainId!=null){
            queryQL.append(" and tb_wo.sendDomain.id = :sendDomainId");
            nameArgs.add("sendDomainId");
            valueArgs.add(sendDomainId);
        }

        queryQL.append(" order by tb_wo.createTime desc");

        List<Object[]> resultSet = pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;

    }

    /**
     * 查询我发送的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> querySendWorkOrder(String qTitle, Long qCategory, String startDate, String endDate,
                    Long domainId,Long receiveDomainId, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_wo,(select count(tb_woa.id) from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id) as acount "
                                        + " from WorkOrderEntity tb_wo inner join tb_wo.sendDomain tb_domain "
                                        + " where  tb_domain.id=:domainId ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //域
        nameArgs.add("domainId");
        valueArgs.add(domainId);

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }

        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }

        if(receiveDomainId!=null){
            queryQL.append(" and tb_wo.receiveDomain.id=:receiveDomainId ");
            nameArgs.add("receiveDomainId");
            valueArgs.add(receiveDomainId);
        }
        queryQL.append(" order by tb_wo.createTime desc");

        List<Object[]> resultSet = pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;

    }

    /**
     * 用户域下查询我接收的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryReceiveWorkOrderInAllDoamins(String qTitle, Long qCategory, String startDate,
                    String endDate, String domainIds,Long sendDomainId, PageInfo pageInfo) throws SQLException {

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

        StringBuilder queryQL = new StringBuilder(
                        "select tb_wo,(select count(tb_woa.id) from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id)  as acount "
                                        + " from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain where tb_domain.id in ("
                                        + domainIdsBuilder.toString() + ") ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }
        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(sendDomainId!=null){
            queryQL.append(" and tb_wo.sendDomain.id = :sendDomainId ");
            nameArgs.add("sendDomainId");
            valueArgs.add(sendDomainId);
        }

        queryQL.append(" order by tb_wo.createTime desc");

        List<Object[]> resultSet = pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;

    }

    /**
     * 用户域下查询我发送的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> querySendWorkOrderInAllDoamins(String qTitle, Long qCategory, String startDate,
                    String endDate, String domainIds,Long receiveDomainId, PageInfo pageInfo) throws SQLException {

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

        StringBuilder queryQL = new StringBuilder(
                        " select tb_wo ,(select count(tb_woa.id) as acount from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id) "
                                        + " from WorkOrderEntity tb_wo  inner join tb_wo.sendDomain tb_domain "
                                        + " where tb_domain.id in (" + domainIdsBuilder.toString() + ") ");

        List<String> nameArgs = new ArrayList<String>();
        List<Object> valueArgs = new ArrayList<Object>();

        //标题
        if (!StringUtil.isBlank(qTitle)) {
            queryQL.append(" and tb_wo.title like :qTitle ");
            nameArgs.add("qTitle");
            valueArgs.add("%" + qTitle + "%");
        }

        //类别 
        if (qCategory != null) {
            queryQL.append(" and tb_wo.category.id=:qCategory");
            nameArgs.add("qCategory");
            valueArgs.add(qCategory);
        }

        if (!StringUtil.isBlank(startDate)) {
            queryQL.append(" and  tb_wo.createTime >= :startDate ");
            nameArgs.add("startDate");
            valueArgs.add(DateUtil.parseDateTime(startDate));
        }
        if (!StringUtil.isBlank(endDate)) {
            queryQL.append(" and  tb_wo.createTime <= :endDate ");
            nameArgs.add("endDate");
            valueArgs.add(DateUtil.parseDateTime(endDate));
        }
        if(receiveDomainId!=null){
            queryQL.append(" and tb_wo.receiveDomain.id=:receiveDomainId ");
            nameArgs.add("receiveDomainId");
            valueArgs.add(receiveDomainId);
        }

        queryQL.append(" order by tb_wo.createTime desc");

        List<Object[]> resultSet = pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), nameArgs, valueArgs, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;

    }

    /**
     * 指定域下处理中的我接收的工单
     * 
     * @param name
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> getHandlingWorkOrderInSpecifyDomain(Long domainId, PageInfo pageInfo) throws SQLException {
        String queryQL = "select tb_wo,(select count(tb_woa.id) as acount from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id) "
                        + " from WorkOrderEntity tb_wo join tb_wo.receiveDomain tb_domain "
                        + " where  tb_domain.id=:domainId  and tb_wo.status=:status "
                        + " order by tb_wo.createTime desc ";
        List<Object[]> resultSet = pageQuery(queryQL, Arrays.asList("domainId", "status"), Arrays.asList(
                        (Object) domainId, (Object) Constant.WORKORDER_ACCEPING), pageInfo.getStart(), pageInfo
                        .getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL, Arrays.asList("domainId", "status"), Arrays.asList(
                            (Object) domainId, (Object) Constant.WORKORDER_ACCEPING), pageInfo.getStart(), pageInfo
                            .getLimit());
        }
        return resultSet;
    }

    /**
     * 用户所有域下的处理中的我接收的工单
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getHandlingWorkOrderInAllDoaminsCount(String domainIds) throws SQLException {

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

        String queryQL = "select count(*)  from WorkOrderEntity tb_wo  inner join tb_wo.receiveDomain tb_domain"
                        + " where  tb_domain.id in (" + domainIdsBuilder.toString() + ") and tb_wo.status=:status ";

        return Integer.parseInt(uniqueResultObject(queryQL, Arrays.asList("status"),
                        Arrays.asList((Object) Constant.WORKORDER_ACCEPING)).toString());
    }

    /**
     * 用户所有域下处理中的我接收的工单
     * 
     * @param name
     * @param start
     * @param limit
     * @return List<Object[]> Object[0] 是ResourceEntity,Object[1] 是机架名,Object[2] 是机房名,
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> getHandlingWorkOrderInAllDomains(String domainIds, PageInfo pageInfo) throws SQLException {
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

        String queryQL = "select tb_wo ,(select count(tb_woa.id) as acount from WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id= tb_wo.id) "
                        + " from WorkOrderEntity tb_wo join tb_wo.receiveDomain tb_domain"
                        + " where tb_wo.status=:status and tb_domain.id in ("
                        + domainIdsBuilder.toString()
                        + ") "
                        + " order by tb_wo.createTime desc";

        List<Object[]> resultSet = pageQuery(queryQL, Arrays.asList("status"), Arrays
                        .asList((Object) Constant.WORKORDER_ACCEPING), pageInfo.getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL, Arrays.asList("status"), Arrays
                            .asList((Object) Constant.WORKORDER_ACCEPING), pageInfo.getStart(), pageInfo.getLimit());
        }
        return resultSet;
    }
}
