/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.entity.WorkOrderAttachmentEntity;

/**
 * workOrderAttachmentDAO
 * 
 * @author CloudKing
 * 
 */
@Repository("workOrderAttachmentDAO")
public class WorkOrderAttachmentDAO extends BaseDAO<WorkOrderAttachmentEntity> {

    /**
     * 根据工单id查询附件
     * 
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<WorkOrderAttachmentEntity> queryAttachmentsByWorkOrderId(Long workOrderId) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select tb_woa from WorkOrderAttachmentEntity tb_woa inner join tb_woa.workOrder tb_wo where  tb_wo.id=:workOrderId");
        return (List<WorkOrderAttachmentEntity>) list(queryQL.toString(), "workOrderId", workOrderId);
    }

    /**
     * 根据工单id查询附件
     * 
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public WorkOrderAttachmentEntity queryAttachementByWorkOrderIdAndFileName(Long workOrderId, String fileName)
                    throws SQLException {
        return uniqueResult(
                        "select tb_woa from WorkOrderAttachmentEntity tb_woa left join tb_woa.workOrder tb_wo where tb_woa.fileName=:fileName and tb_wo.id=:workOrderId",
                        Arrays.asList(new String[] { "fileName", "workOrderId" }), Arrays.asList(new Object[] {
                                        fileName, workOrderId }));
    }

    /**
     * 删除指定域下所有工单附件
     * 
     * @param id
     * @throws Exception
     *             所有异常
     */
    public void deleteByDomainId(Long domainId) throws Exception {
        deleteByJPQL(
                        "delete WorkOrderAttachmentEntity tb_woa where tb_woa.workOrder.id in("
                        + "select distinct(tb_wo.id) from WorkOrderEntity tb_wo where tb_wo.receiveDomain.id=:domainId or tb_wo.sendDomain.id=:domainId )",
                        Arrays.asList("domainId"), Arrays.asList((Object) domainId));
    }
    
    /**
     * 查询指定域下所有工单序列号
     * 
     * @param id
     * @throws Exception
     *             所有异常
     */
    @SuppressWarnings("unchecked")
    public List<WorkOrderAttachmentEntity> queryWorkOrderAttachmentsByDomainId(Long domainId) throws Exception {
        return (List<WorkOrderAttachmentEntity>) list(
                        "select tb_woa from WorkOrderAttachmentEntity tb_woa inner join tb_woa.workOrder tb_wo " 
                        + "where tb_wo.receiveDomain.id=:domainId or tb_wo.sendDomain.id=:domainId ",
                        Arrays.asList("domainId"), Arrays.asList((Object) domainId));
    }
}
