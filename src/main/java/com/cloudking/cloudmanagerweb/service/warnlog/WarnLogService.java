/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.warnlog;

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
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.WarnLogDAO;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.entity.WarnLogEntity;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;
import com.cloudking.cloudmanagerweb.vo.WarnLogVO;

/**
 * 事件service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("warnLogService")
public class WarnLogService extends BaseService {
    /**
     * 事件DAO
     */
    @Resource
    private WarnLogDAO warnLogDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<WarnLogVO> cloudContext) throws Exception {
        //结果集
        List<WarnLogVO> queryResult = new ArrayList<WarnLogVO>();
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        warnLogDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext
                                        .getStringParam("qEquipmentIdentity"), startDate, endDate));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<WarnLogEntity> objs = warnLogDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getStringParam("qEquipmentIdentity"), startDate, endDate, cloudContext.getPageInfo());
            WarnLogVO warnLogVO = null;
            for (WarnLogEntity obj : objs) {
                warnLogVO = new WarnLogVO();
                BeanUtils.copyProperties(obj, warnLogVO);
                queryResult.add(warnLogVO);
            }
        }
        cloudContext.addParam("warns", queryResult);

        List<MachineRoomEntity> roomEntitys = machineRoomDAO.list();
        List<MachineRoomVO> machineRoomVOs = new ArrayList<MachineRoomVO>();
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity e : roomEntitys) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(e, machineRoomVO);
            machineRoomVOs.add(machineRoomVO);
        }
        cloudContext.addParam("rooms", machineRoomVOs);

        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        if (cloudContext.getLongParam("qRoom") != null) {
            List<MachineRackEntity> machineRackEntitys = machineRoomDAO.get(cloudContext.getLongParam("qRoom"))
                            .getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
        }
        cloudContext.addParam("racks", machineRacks);
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRackByRoom(CloudContext<WarnLogVO> cloudContext) throws Exception {
        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        MachineRoomEntity machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomId"));
        if (machineRoomEntity != null) {
            List<MachineRackEntity> machineRackEntitys = machineRoomEntity.getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
        }
        cloudContext.addParam("racks", machineRacks);
    }

    /**
     * 导出文件
     * 
     * @param cloudContext
     * @throws Exception
     *             异常
     */
    public void exportData(CloudContext<WarnLogVO> cloudContext) throws Exception {
        String fileName = String.format("warnlog_%1$s", DateUtil.format(new Date(), "yyyyMMdd_HHmmss"));
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        ZipOutputStream fw = new ZipOutputStream(bais);
        ZipEntry entry = new ZipEntry(fileName + ".txt");
        fw.putNextEntry(entry);
        fw.write("序号     描述      时间 \r\n".getBytes());
        String startDate = cloudContext.getStringParam("startDate");
        String endDate = cloudContext.getStringParam("endDate");
        List<WarnLogEntity> objss = warnLogDAO.queryNoPage(cloudContext.getStringParam("qName"), cloudContext
                        .getStringParam("qEquipmentIdentity"), startDate, endDate);
        int index = 1;
        for (WarnLogEntity wlEntity : objss) {
            fw.write((index++ + "    " + wlEntity.getDesc() + "    "
                            + DateUtil.format(wlEntity.getAddTime(), "yyyy-MM-dd HH:mm:ss") + "\r\n").getBytes());
        }
        fw.closeEntry();
        fw.close();
        byte[] ba = bais.toByteArray();
        ByteArrayInputStream fis = new ByteArrayInputStream(ba);
        cloudContext.addParam("baStream", fis);
        cloudContext.addParam("zipName", fileName + ".zip");
    }

    /**
     * 查找机柜下面的设备
     * 
     * @param cloudContext
     */
    public void querEquipmentByRack(CloudContext<WarnLogVO> cloudContext) {
        
    }
}
