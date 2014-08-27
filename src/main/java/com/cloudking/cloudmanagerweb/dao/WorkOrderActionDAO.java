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
import com.cloudking.cloudmanagerweb.entity.WorkOrderActionEntity;

/**
 * workOrderActionDAO
 * 
 * @author CloudKing
 * 
 */
@Repository("workOrderActionDAO")
public class WorkOrderActionDAO extends BaseDAO<WorkOrderActionEntity> {

    /**
     * 根据 工单ID 和类型获得工单的操纵类的集合
     * @param workOrderId
     * @param type
     * @throws SQLException SQLException
     * @return
     */
    public List<WorkOrderActionEntity> listByWorkOrderIdAndType(Long workOrderId, String type) throws SQLException {
        return list(
                " from  WorkOrderActionEntity as _action where _action.workOrder.id=:workOrderId and _action.type=:type order by _action.createTime ASC",
                Arrays.asList("workOrderId", "type"), Arrays.asList((Object)workOrderId,(Object)type));

    }
}
