/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.entity.PortalOrderEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 属性DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("portalOrderDAO")
public class PortalOrderDAO extends BaseDAO<PortalOrderEntity> {
    /**
     * 获取总数据数
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(Long domainId, Long templateId, Long machineTypeId, String status, Date applyTimeFrom,
                    Date applyTimeTo) throws SQLException {
        StringBuilder queryQL = new StringBuilder("select count(*) from PortalOrderEntity as _order  ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        //模板
        if (templateId != null) {
            queryQL.append(" inner join _order.template as _template ");
        }
        //机型
        if (machineTypeId != null) {
            queryQL.append(" inner join _order.machineType as _machineType ");
        }

        queryQL.append("  inner join _order.domain as _domain  where _domain.id=:_domainId ");
        paramNames.add("_domainId");
        paramValues.add(domainId);

        //模板
        if (templateId != null) {
            queryQL.append(" and  _template.id=:qTemplate ");
            paramNames.add("qTemplate");
            paramValues.add(templateId);
        }
        //机型
        if (machineTypeId != null) {
            queryQL.append(" and _machineType.id=:qMachineType ");
            paramNames.add("qMachineType");
            paramValues.add(machineTypeId);
        }

        //status
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  _order.status =:qStatus ");
            paramNames.add("qStatus");
            paramValues.add(status);
        }

        // applyTimeFrom 
        if (applyTimeFrom != null) {
            queryQL.append(" and  _order.applyTime >=:qApplyTimeFrom ");
            paramNames.add("qApplyTimeFrom");
            paramValues.add(applyTimeFrom);
        }
        // applyTimeTo 
        if (applyTimeTo != null) {
            queryQL.append(" and  _order.applyTime <=:qApplyTimeTo ");
            paramNames.add("qApplyTimeTo");
            paramValues.add(applyTimeTo);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 分页查询订单
     * 
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<PortalOrderEntity> query(Long domainId, Long templateId, Long machineTypeId, String status,
                    Date applyTimeFrom, Date applyTimeTo, PageInfo pageInfo) throws SQLException {
        //查询记录条目
        StringBuilder queryQL = new StringBuilder("select _order from PortalOrderEntity as _order   ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        //模板
        if (templateId != null) {
            queryQL.append(" inner join _order.template as _template ");
        }
        //机型
        if (machineTypeId != null) {
            queryQL.append(" inner join _order.machineType as _machineType ");
        }

        queryQL.append("  inner join _order.domain as _domain  where _domain.id=:_domainId ");
        paramNames.add("_domainId");
        paramValues.add(domainId);

        //模板
        if (templateId != null) {
            queryQL.append(" and  _template.id=:qTemplate ");
            paramNames.add("qTemplate");
            paramValues.add(templateId);
        }
        //机型
        if (machineTypeId != null) {
            queryQL.append(" and _machineType.id=:qMachineType ");
            paramNames.add("qMachineType");
            paramValues.add(machineTypeId);
        }
        //status
        if (!StringUtil.isBlank(status)) {
            queryQL.append(" and  _order.status =:qStatus ");
            paramNames.add("qStatus");
            paramValues.add(status);
        }

        // applyTimeFrom 
        if (applyTimeFrom != null) {
            queryQL.append(" and  _order.applyTime >=:qApplyTimeFrom ");
            paramNames.add("qApplyTimeFrom");
            paramValues.add(applyTimeFrom);
        }
        // applyTimeTo 
        if (applyTimeTo != null) {
            queryQL.append(" and  _order.applyTime <=:qApplyTimeTo ");
            paramNames.add("qApplyTimeTo");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(applyTimeTo);
            calendar.add(Calendar.DATE, 1);
            paramValues.add(calendar.getTime());
        }
        //排序
        queryQL.append("  order by _order.applyTime desc");

        List<PortalOrderEntity> resultSet = (List<PortalOrderEntity>) pageQuery(queryQL.toString(), paramNames,
                        paramValues, pageInfo.getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<PortalOrderEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                            .getStart(), pageInfo.getLimit());

        }
        return resultSet;
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
    public List<Object[]> queryByUserID(String userId, Integer start, Integer limit) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_wo.ID_,tb_wo.SERIALNUMBER_ as serialnumber,tb_wo.TITLE_,tb_wo.CONTENT_,tb_wo.SOLVEMSG_,tb_wo.CREATETIME_,tb_wo.STATUS_,tb_d.NAME_ "
                                        + " from tb_workorder tb_wo "
                                        + " inner join tb_portalorder tb_po on tb_po.ID_=tb_wo.REFID_  "
                                        + " inner join tb_domain tb_d on tb_wo.RECEIVE_DOMAIN_ID_=tb_d.ID_  "
                                        + " inner join tb_portaluser tb_pu on tb_po.APPLICANT_ID_=tb_pu.ID_  "
                                        + " where tb_pu.USERID_=:userId");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);

        //排序
        queryQL.append(" order by tb_wo.CREATETIME_ desc LIMIT " + start + "," + limit);

        List<Object[]> resultSet = (List<Object[]>) listBySQL(queryQL.toString(), paramNames, paramValues);
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && start > 1) {
            resultSet = (List<Object[]>) listBySQL(queryQL.toString(), paramNames, paramValues);
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
    public Integer getQueryCountByUserID(String userId) throws SQLException {

        StringBuilder queryQL = new StringBuilder("select count(tb_wo.ID_) from tb_workorder tb_wo "
                        + " inner join tb_portalorder tb_po on tb_po.ID_=tb_wo.REFID_  "
                        + " inner join tb_domain tb_d on tb_wo.RECEIVE_DOMAIN_ID_=tb_d.ID_  "
                        + " inner join tb_portaluser tb_pu on tb_po.APPLICANT_ID_=tb_pu.ID_  "
                        + " where tb_pu.USERID_=:userId");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        return Integer.parseInt(uniqueResultObjectBySQL(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 根据工单的refId查找对应的订单人姓名
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object> queryPortalUsernameAndCountsByRefId(Long workOrderId, String nameKeyWord) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select DISTINCT(tb_user.REALNAME_),COUNT(tb_vm.NAME_) from tb_portaluser  tb_user "
                                        + " inner join tb_portalorder as tb_porder on tb_porder.APPLICANT_ID_=tb_user.ID_"
                                        + " inner join tb_workorder as tb_worder on tb_worder.REFID_=tb_porder.ID_"
                                        + " left join tb_portaluserbinvirtualmachine as tb_vmBinUser on tb_vmBinUser.PORTALUSER_ID_=tb_user.ID_ "
                                        + " left join tb_virtualmachine as tb_vm on tb_vm.ID_=tb_vmBinUser.VIRTUALMACHINE_ID "
                                        + " where  tb_worder.ID_=:workOrderId  and tb_vm.NAME_ like :nameKeyWord");
        List<Object> result = listBySQL(queryQL.toString(), Arrays.asList("workOrderId", "nameKeyWord"), Arrays.asList(
                        (Object) workOrderId, (Object) "%" + nameKeyWord + "%"));
        return result;
    }

}
