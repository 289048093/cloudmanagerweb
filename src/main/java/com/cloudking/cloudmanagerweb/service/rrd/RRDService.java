/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.rrd;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.backup.BackupStorage;
import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.BaseVO;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.BackupStorageDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.entity.BackupStorageEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.util.ContextUtil;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 监控service
 * 
 * @author CloudKing
 */
@Service("rrdService")
@SuppressWarnings("unchecked")
public class RRDService extends BaseService {

    /**
     * 计算节点DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;

    /**
     * 存储节点DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;
    /**
     * 备份存储DAO
     */
    @Resource
    private BackupStorageDAO backupStorageDAO;

    /**
     * 获取图片
     * 
     * @param cloudContext
     * @return
     * @throws Exception
     *             所有异常
     */
    public void getImg(CloudContext<BaseVO> cloudContext) throws Exception {
        //参数准备
        String equipmentType = cloudContext.getStringParam("equipmentType");
        String dataFlag = cloudContext.getStringParam("dataFlag");

        String startDateStr = cloudContext.getStringParam("startDate");
        String endDateStr = cloudContext.getStringParam("startDate");
        Date startDate = null;
        Date endDate = null;
        if (!StringUtil.isBlank(startDateStr)) {
            startDate = DateUtil.parseDateTime(cloudContext.getStringParam("startDate"));
        }
        if (!StringUtil.isBlank(endDateStr)) {
            endDate = DateUtil.parseDateTime(cloudContext.getStringParam("endDate"));
        }
        byte[] img = loadNoRRDImg();
        Calendar calendar = Calendar.getInstance();

        /*
         * 判断时间
         */
        if (startDate == null && endDate == null) {
            //如果开始时间和结束时间没有就显示 ，当前时间，和前一天的数据
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            startDate = calendar.getTime();
        } else if (startDate == null && endDate != null) {
            //如果开始时间没有   但是  结束时间  有  ,则 开始时间为结束时间的前一天
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            startDate = calendar.getTime();
        } else if (startDate != null && endDate == null) {
            //如果开始时间有 但是 结束时间没有，则结束时间为当前时间
            endDate = calendar.getTime();
        }
        //如果此 endDate 在 startDate 参数之前，则返回小于 0 的值 .并且把时间重置 当前时间，和前一天的数据
        if (endDate != null && endDate.compareTo(startDate) < 0) {
            //如果开始时间和结束时间没有就显示 ，当前时间，和前一天的数据
            calendar = Calendar.getInstance();
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            startDate = calendar.getTime();
        }

        //逻辑判断画图
        if ("compute".equalsIgnoreCase(equipmentType)) {
            ComputeResourceEntity computeResourceEntity = computeResourceDAO.get(cloudContext.getVo().getId());
            if (computeResourceEntity != null) {
                if (dataFlag.startsWith("cpu")) {
                    img = RRDUtil.graphCPU(computeResourceEntity.getRrdPath(), dataFlag, String.format("【%1$s】计算节点",
                                    computeResourceEntity.getName()), startDate, endDate);
                } else if (dataFlag.startsWith("mem")) {
                    img = RRDUtil.graphMem(computeResourceEntity.getRrdPath(), dataFlag, String.format("【%1$s】计算节点",
                                    computeResourceEntity.getName()), startDate, endDate);
                }
            }
        } else if ("storage".equalsIgnoreCase(equipmentType)) {
            StorageResourceEntity storageResourceEntity = storageResourceDAO.get(cloudContext.getVo().getId());
            if (storageResourceEntity != null) {
                Storage storage = Storage.getStorage(storageResourceEntity.getPoolName());
                img = RRDUtil.graphDisk(storageResourceEntity.getRrdPath(), dataFlag, ProjectUtil.kByteToGiga(storage
                                .getCapacity()), String.format("【%1$s】存储节点", storageResourceEntity.getName()),
                                startDate, endDate);
            }
        } else if ("backupStorage".equalsIgnoreCase(equipmentType)) {
            BackupStorageEntity backupStorageEntity = backupStorageDAO.get(cloudContext.getVo().getId());
            if (backupStorageEntity != null) {
                BackupStorage backupStorage = BackupStorage.getStorage(backupStorageEntity.getIp());
                img = RRDUtil.graphDisk(backupStorageEntity.getRrdPath(), dataFlag, ProjectUtil
                                .kByteToGiga(backupStorage.getCapacity()), String.format("【%1$s】备份存储节点",
                                backupStorageEntity.getName()), startDate, endDate);
            }
        }
        cloudContext.addParam("img", img);

    }

    /**
     * 没有图片,加载一张空的图片
     * 
     * @param cloudContext
     */
    private byte[] loadNoRRDImg() {
        try {
            return FileUtils.readFileToByteArray(new File(ContextUtil.getWebRoot() + File.separator + "images"
                            + File.separator + "norrd.png"));
        } catch (IOException e) {
            LogUtil.error(String.format("加载【%1$s】失败", new File(ContextUtil.getWebRoot() + File.separator + "images"
                            + File.separator + "norrd.png").getAbsolutePath()));
        }
        return new byte[1];
    }
}
