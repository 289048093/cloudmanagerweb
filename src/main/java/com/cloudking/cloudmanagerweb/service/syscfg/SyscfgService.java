/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.syscfg;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.SimpleTrigger;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.PropertyManager;
import com.cloudking.cloudmanagerweb.dao.SyscfgDAO;
import com.cloudking.cloudmanagerweb.entity.PropertyEntity;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.PropertyVO;

/**
 * 全局设置service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("syscfgService")
public class SyscfgService extends BaseService {
    /**
     * 系统设置DAO
     */
    @Resource
    private SyscfgDAO syscfgDAO;
    /**
     * 调度器Factory
     */
    @Resource
    private StdScheduler schedulerFactory;

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<PropertyVO> cloudContext) throws Exception {
        //结果集
        List<PropertyVO> queryResult = new ArrayList<PropertyVO>();
        //总数据数
        List<PropertyEntity> propertyEntitys = syscfgDAO.list();
        cloudContext.getPageInfo().setDataCount(propertyEntitys.size());
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            PropertyVO propertyVO = null;
            for (PropertyEntity propertyEntity : propertyEntitys) {
                propertyVO = new PropertyVO();
                BeanUtils.copyProperties(propertyEntity, propertyVO);
                queryResult.add(propertyVO);
            }
        }
        cloudContext.addParam("syscfgs", queryResult);
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<PropertyVO> cloudContext) throws Exception {
        PropertyManager.getInstance().setDBProperty(cloudContext.getVo().getKey(), cloudContext.getVo().getValue());
        checkMysqlBackUpProperty(cloudContext.getVo().getKey(), cloudContext.getVo().getValue());
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<PropertyVO> cloudContext) throws Exception {
        //如果是修改，就加载属性
        PropertyEntity propertyEntity = syscfgDAO.get(cloudContext.getVo().getId());
        PropertyVO propertyVO = new PropertyVO();
        BeanUtils.copyProperties(propertyEntity, propertyVO);
        cloudContext.addParam("dataVo", propertyVO);
    }

    /**
     * 检查是否修改mysql备份信息，如果是修改，就设置spring
     * 
     * @throws Exception
     *             所有异常
     */
    private void checkMysqlBackUpProperty(String key, String value) throws Exception {
        if (PropertyManager.DB_MYSQL_BACKUP_DAYS_KEY.equals(key)) {
            Long intervalValue = Long.parseLong(value) * 24 * 1000 * 60;
            //重新加载配置
            SimpleTrigger databaseBackUpTrigger = (SimpleTrigger) schedulerFactory.getTrigger("databaseBackUpTrigger",
                            StdScheduler.DEFAULT_GROUP);
            databaseBackUpTrigger.setRepeatInterval(intervalValue);
            schedulerFactory.rescheduleJob(databaseBackUpTrigger.getName(), databaseBackUpTrigger.getGroup(), databaseBackUpTrigger);
            //修改applicationContextXML中的quartz的定时参数
            ProjectUtil.updateMysqlBackUpDaysInXml(intervalValue.toString());
        }
    }
}
