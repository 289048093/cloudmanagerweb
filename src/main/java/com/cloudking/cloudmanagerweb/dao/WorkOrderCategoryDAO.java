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
import com.cloudking.cloudmanagerweb.entity.WorkOrderCategoryEntity;
import com.cloudking.cloudmanagerweb.util.Constant;

/**
 * @author CloudKing
 * 
 */
@Repository("workOrderCategoryDAO")
public class WorkOrderCategoryDAO extends BaseDAO<WorkOrderCategoryEntity> {

    /**
     * 获得可创建的工单类别
     * 
     * @throws SQLException
     *             SQLException
     * @return
     */
    public List<WorkOrderCategoryEntity> listWorkOrderCategoryForSend() throws SQLException {
        return list(" from  WorkOrderCategoryEntity as tb_cate where tb_cate.id in(:commonWOId,:resWOId)", Arrays
                        .asList("commonWOId", "resWOId"), Arrays.asList((Object) Constant.WORK_ORDER_CATEGORY_COMMON,
                        (Object) Constant.WORK_ORDER_CATEGORY_NEW_RESOURCEORDER));

    }

}
