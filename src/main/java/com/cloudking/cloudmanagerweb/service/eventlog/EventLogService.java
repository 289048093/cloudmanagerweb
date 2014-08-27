/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.eventlog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.EventLogDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.EventLogEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.EventLogVO;

/**
 * 事件service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("eventLogService")
public class EventLogService extends BaseService {
    /**
     * 事件DAO
     */
    @Resource
    private EventLogDAO eventLogDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<EventLogVO> cloudContext) throws Exception {
        //结果集
        List<EventLogVO> queryResult = new ArrayList<EventLogVO>();
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        eventLogDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext.getLoginedUser()
                                        .getId(), cloudContext.getStringParam("qDomain"), startDate, endDate));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objss = eventLogDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLoginedUser().getId(), cloudContext.getStringParam("qDomain"), startDate, endDate,
                            cloudContext.getPageInfo());
            EventLogVO eventLogVO = null;
            UserEntity user = null;
            for (Object[] objs : objss) {
                eventLogVO = new EventLogVO();
                BeanUtils.copyProperties(objs[0], eventLogVO);
                eventLogVO.setUserName(((UserEntity) objs[1]).getRealname());
                queryResult.add(eventLogVO);
            }
        }
        cloudContext.addParam("events", queryResult);

        List<DomainEntity> domains = domainDAO.queryDescendantDomainByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domains) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOs);
    }

    /**
     * 导出文件
     * 
     * @param cloudContext
     * @throws Exception
     *             异常
     */
    public void exportData(CloudContext<EventLogVO> cloudContext) throws Exception {
        String fileName = String.format("log_%1$s", DateUtil.format(new Date(), "yyyyMMdd_HHmmss"));
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        ZipOutputStream fw = new ZipOutputStream(bais);
        ZipEntry entry = new ZipEntry(fileName + ".txt");
        fw.putNextEntry(entry);
        fw.write("序号    用户    域     描述      时间 \r\n".getBytes());
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        List<Object[]> objss = eventLogDAO.queryNoPage(cloudContext.getStringParam("qName"), cloudContext
                        .getLoginedUser().getId(), cloudContext.getStringParam("qDomain"), startDate, endDate);
        EventLogEntity elEntity = null;
        UserEntity userEntity = null;
        int index = 1;
        for (Object[] objs : objss) {
            elEntity = (EventLogEntity) objs[0];
            userEntity = (UserEntity) objs[1];
            fw.write((index++ + "    " + userEntity.getRealname() + "   " + elEntity.getDomainName() + "    "
                            + elEntity.getDesc() + "    "
                            + DateUtil.format(elEntity.getAddTime(), "yyyy-MM-dd HH:mm:ss") + "\r\n").getBytes());
        }
        fw.closeEntry();
        fw.close();
        byte[] ba = bais.toByteArray();
        ByteArrayInputStream fis = new ByteArrayInputStream(ba);
        cloudContext.addParam("baStream", fis);
        cloudContext.addParam("zipName", fileName + ".zip");
    }
}
