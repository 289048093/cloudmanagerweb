/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.quartz;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cloudking.cloudmanager.core.backup.BackupStorage;
import com.cloudking.cloudmanager.core.compute.Compute;
import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanager.core.virtualization.VirtualMachine;
import com.cloudking.cloudmanagerweb.PropertyManager;
import com.cloudking.cloudmanagerweb.dao.BackupStorageDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.EventLogDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.ResourceOrderDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.VMBackupDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.WarnLogDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderActionDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.entity.BackupStorageEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.entity.VMBackupEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.WarnLogEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderActionEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.MailUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.util.ServiceWarnNumWrapper;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.util.VMUtil;

/**
 * @author CloudKing 任务调度器
 */
public class CloudKingScheduler {

    /**
     * 存储资源DAO
     */
    @Resource
    StorageResourceDAO storageResourceDAO;

    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * 工单DAO
     */
    @Resource
    WorkOrderDAO workOrderDAO;

    /**
     * workOrderActionDAO
     */
    @Resource
    WorkOrderActionDAO workOrderActionDAO;
    /**
     * 计算资源资源DAO
     */
    @Resource
    ComputeResourceDAO computeResourceDAO;
    /**
     * 门户订单DAO
     */
    @Resource
    PortalOrderDAO portalOrderDAO;

    /**
     * 门户订单DAO
     */
    @Resource
    ResourceOrderDAO resourceOrderDAO;
    /**
     * 操作记录DAO
     */
    @Resource
    private EventLogDAO eventLogDAO;
    /**
     * 备份存储DAO
     */
    @Resource
    private BackupStorageDAO backupStorageDAO;
    /**
     * 虚机备份DAO
     */
    @Resource
    private VMBackupDAO vMBackupDAO;
    /**
     * 虚机DAO
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;
    /**
     * 报警DAO
     */
    @Resource
    private WarnLogDAO warnLogDAO;

    /**
     * 监控任务
     * 
     * @throws Exception
     *             所有异常
     */
    public void quartzCkDatabaseBackUpTask() throws Exception {
        //
        String persistenceXML = CloudKingScheduler.class.getClassLoader().getResource(
                        "META-INF" + File.separator + "persistence.xml").getPath();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new InputStreamReader(new BufferedInputStream(new FileInputStream(new File(
                        persistenceXML))), "UTF-8"));
        Node properties = doc
                        .selectSingleNode("/*[name()='persistence']/*[name()='persistence-unit']/*[name()='properties']");
        String username = ((Element) properties.selectSingleNode("*[@name='hibernate.connection.username']"))
                        .attributeValue("value");
        String password = ((Element) properties.selectSingleNode("*[@name='hibernate.connection.password']"))
                        .attributeValue("value");
        String databaseURL = ((Element) properties.selectSingleNode("*[@name='hibernate.connection.url']"))
                        .attributeValue("value");
        String databaseName = databaseURL.substring(databaseURL.lastIndexOf('/') + 1, databaseURL.indexOf("?"));

        String dbBackUpDir = null;
        try {
            dbBackUpDir = ProjectUtil.getDBBackUPDir().getPath();
        } catch (RuntimeException e1) {
            LogUtil.error("获取数据库备份文件夹地址失败！", e1);
            return;
        }
        String dbBackUpFile = dbBackUpDir + File.separator + "mysqlBackup_"
                        + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".sql";
        Process process = null;
        if (ProjectUtil.isWindows()) {
            process = Runtime
                            .getRuntime()
                            .exec(
                                            "cmd /c mysqldump -u"
                                                            + username
                                                            + " -p"
                                                            + password
                                                            + " --default-character-set=utf8 --disable-keys=false  --opt --single-transaction  "
                                                            + databaseName + " > " + dbBackUpFile);
        } else if (ProjectUtil.isLinux()) {
            String processStr = "mysqldump --user=" + username + " --password=" + password
                            + " --default-character-set=utf8 --disable-keys=false --opt --single-transaction "
                            + " --databases " + databaseName + " > " + dbBackUpFile;
            process = Runtime.getRuntime().exec(new String[] { "bash", "-c", processStr });
        }
        if (process != null && process.waitFor() != 0) {
            LogUtil.warn("备份指令有问题，或者没权限执行");
            return;
        }
        //判断是否超过限定备份次数
        String dbNumStr = null;
        try {
            dbNumStr = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_MYSQL_BACKUP_NUM_KEY);
        } catch (Exception e) {
            LogUtil.error("数据库没有此属性记录：备份次数", e);
            return;
        }
        Long backUpNum = Long.parseLong(dbNumStr);
        File dbBackUpDirFile = new File(dbBackUpDir);
        String[] backUpFiles = dbBackUpDirFile.list(new SuffixFileFilter(".sql"));
        int fileNum = backUpFiles.length;
        if (fileNum > backUpNum) {
            Arrays.sort(backUpFiles);
            for (String file : backUpFiles) {
                File deleteFile = new File(dbBackUpDir + File.separator + file);
                if (deleteFile.delete()) {
                    fileNum--;
                }
                if (fileNum == backUpNum) {
                    break;
                }
            }
        }
    }

    /**
     * 每日任务
     * 
     * @throws Exception
     *             所有异常
     */
    public void quartzEveryDayTask() throws Exception {
        deleteEventLog();
        deleteWarnLog();
        reportWorkOrderTask();
    }

    /**
     * 删除报警记录
     * 
     * @throws SQLException
     *             sql异常
     */
    private void deleteWarnLog() throws SQLException {
        Integer days = Integer.valueOf(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_DELETE_WARNLOG_DAYS_KEY));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        warnLogDAO.deleteByAddTime(calendar.getTime());
    }

    /**
     * 删除操作记录
     * 
     * @throws SQLException
     *             sql异常
     */
    private void deleteEventLog() throws SQLException {
        Integer days = Integer.valueOf(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_DELETE_EVENTLOG_DAYS_KEY));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        eventLogDAO.deleteByAddTime(calendar.getTime());
    }

    /**
     * 工单超时上报
     * 
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings( { "unused", "unchecked" })
    private void reportWorkOrderTask() throws SQLException {
        Integer maxDay = Integer.valueOf(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.WORKORDER_REPORT_DAYS));
        Date now = new Date();
        //查找符合条件的工单[1.状态为处理中 2.类别为非普通工单 3.创建时间[reporttime]与当前时间相差大于用户定义超时天数]
        String woSql = "select tb_wo.ID_ as woId,tb_sdomain.ID_ as sdId from tb_workorder tb_wo "
                        + " inner join tb_domain tb_rdomain on tb_rdomain.ID_=tb_wo.RECEIVE_DOMAIN_ID_  "
                        + " left join tb_domain tb_sdomain on tb_sdomain.CODE_=substring(tb_rdomain.CODE_,1,length(tb_rdomain.CODE_)-2) "
                        + " where tb_wo.STATUS_=:woStatus "
                        + " and datediff(now(),tb_wo.REPORTTIME_)>:maxDay and tb_wo.CATEGORY_ID_<>:categoryId ";

        List<Object[]> objects = (List<Object[]>) workOrderDAO.listBySQL(woSql, Arrays.asList("woStatus", "maxDay",
                        "categoryId"), Arrays.asList((Object) Constant.WORKORDER_ACCEPING, (Object) maxDay,
                        (Object) Constant.WORK_ORDER_CATEGORY_COMMON));
        if (objects == null) {
            return;
        }

        List<WorkOrderEntity> workOrderEntitys = new ArrayList<WorkOrderEntity>();
        List<DomainEntity> superDomains = new ArrayList<DomainEntity>();
        Long domainId = null;
        for (Object[] object : objects) {
            domainId = object[1] == null ? null : Long.parseLong(object[1].toString());
            //排除没有上级域的工单
            if (domainId == null) {
                continue;
            } else {
                //排除所有上级域都不存在管理员的工单
                DomainEntity loopDomainEntity = loopFindSuitableSuperDomain(domainId);
                if (loopDomainEntity == null) {
                    continue;
                }
                WorkOrderEntity workOrderEntity = workOrderDAO.get(Long.parseLong(object[0].toString()));
                superDomains.add(loopDomainEntity);
                workOrderEntitys.add(workOrderEntity);
            }
        }
        //上报
        for (int i = 0; i < workOrderEntitys.size(); i++) {
            workOrderEntitys.get(i).setReceiveDomain(superDomains.get(i));
            workOrderEntitys.get(i).setReportTime(now);
            workOrderEntitys.get(i).setReceiver(superDomains.get(i).getUser());
            workOrderEntitys.get(i).setUpdateTime(now);

            WorkOrderActionEntity actionEntity = new WorkOrderActionEntity();
            actionEntity.setAction("工单上报");
            actionEntity.setContent("工单超时上报");
            actionEntity.setCreateTime(now);
            actionEntity.setWorkOrder(workOrderEntitys.get(i));
            workOrderActionDAO.insert(actionEntity);
            workOrderDAO.update(workOrderEntitys.get(i));
        }
    }

    /**
     * 循环查找存在管理员的上级域
     * 
     * @return null表示已到顶级域并且无管理员
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unused")
    private DomainEntity loopFindSuitableSuperDomain(Long domainId) throws SQLException {
        DomainEntity domainEntity = domainDAO.get(domainId);
        if (domainEntity.getUser() == null) {
            domainEntity = domainDAO.querySuperDomainBySubDomainCode(domainEntity.getCode());
            while (domainEntity.getUser() == null && domainEntity.getCode().length() > 2) {
                domainEntity = loopFindSuitableSuperDomain(domainEntity.getId());
            }
        }
        return domainEntity;
    }

    /**
     * 每分钟任务
     */
    public void quartzEveryMinuteTask() {
        try {
            warnLogAndRrdTask();
            checkAutoBackup();
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 报警和rrdTask任务
     * 
     * @throws Exception
     *             所有异常
     */
    private void warnLogAndRrdTask() throws Exception {
        //如果不是linux直接返回
        if (!ProjectUtil.isLinux()) {
            return;
        }
        /*
         * 插入所有计算节点的值
         */
        List<ComputeResourceEntity> computeList = computeResourceDAO.list();
        Compute compute = null;
        for (ComputeResourceEntity computeResourceEntity : computeList) {
            compute = Compute.getCompute(computeResourceEntity.getIp());
            if (compute == null) {
                continue;
            }
            RRDUtil.insertComputeResourceData(compute, computeResourceEntity.getRrdPath());
            try {
                checkComputeWarn(compute, computeResourceEntity);
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
        /*
         * 插入所有存储节点的值
         */
        List<StorageResourceEntity> storageList = storageResourceDAO.list();
        Storage storage = null;
        for (StorageResourceEntity storageResourceEntity : storageList) {
            storage = Storage.getStorage(storageResourceEntity.getPoolName());
            if (storage == null) {
                continue;
            }
            RRDUtil.insertStorageResourceData(storage, storageResourceEntity.getRrdPath());
            try {
                checkStorageWarn(storage, storageResourceEntity);
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }

        List<BackupStorageEntity> backupStorageList = backupStorageDAO.list();
        BackupStorage backupStorage = null;
        for (BackupStorageEntity bsEntity : backupStorageList) {
            backupStorage = BackupStorage.getStorage(bsEntity.getIp());
            if (storage == null) {
                continue;
            }
            RRDUtil.insertBackupStorageResourceData(backupStorage, bsEntity.getRrdPath());
            try {
                checkBackupStorageWarn(backupStorage, bsEntity);
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
        //释放rootDomain，使下次获取时能取到最新的根域信息
        ProjectUtil.releaseRootDomainAndUser();
    }

    /**
     * 检查备份存储的报警
     * 
     * @param backupStorage
     * @param bsEntity
     * @throws SQLException sql异常
     */
    private void checkBackupStorageWarn(BackupStorage backupStorage, BackupStorageEntity bsEntity) throws SQLException {
        String warnStr = bsEntity.getWarn4Rack();
        if (StringUtil.isBlank(warnStr)) {
            return;
        }
        double warnStorageRate = Double.parseDouble(warnStr);
        if (warnStorageRate <= 0) {
            return;
        }
        double storageRate = 1 - (Double.valueOf(backupStorage.getAvailable()) / backupStorage.getCapacity());
        if (storageRate >= warnStorageRate) {
            Date warnDate = new Date();
            UserEntity rootDomainUser = ProjectUtil.getRootDomainUser();
            //发邮件
            String desc = String.format("备份存储资源【%1$s】存储使用率为：%2$s%%,超过报警值：%3$s%%,报警时间:%4$s", bsEntity
                            .getName(), ProjectUtil.decimalFormat(storageRate * 100, "#.00"),
                            (int) (warnStorageRate * 100), DateUtil.format(warnDate, "yyyy-MM-dd HH:mm:ss"));
            //判断是否到达发邮件次数
            ServiceWarnNumWrapper swnw = ServiceWarnNumWrapper.getInstance();
            String storageWarnIdentity = ProjectUtil.generateStorageWarnIdentity(bsEntity
                            .getIdentityName());
            Integer warnCount = swnw.plusOne(storageWarnIdentity).get(storageWarnIdentity);
            Integer warnCountLimit = Integer.parseInt(PropertyManager.getInstance().getDbProperty(
                            PropertyManager.DB_WARN_COUNT_SEND_EMAIL));

            if (warnCount > warnCountLimit) {
                if (rootDomainUser != null) {
                    MailUtil.sendMail("备份存储资源报警", desc, rootDomainUser.getEmail());
                }
                swnw.remove(storageWarnIdentity);
            }
            //插入报警日志
            WarnLogEntity warnLogEntity = new WarnLogEntity();
            warnLogEntity.setAddTime(warnDate);
            warnLogEntity.setDesc(desc);
            warnLogEntity.setEquipmentIdentity(storageWarnIdentity);
            warnLogDAO.insert(warnLogEntity);
        }
    }

    /**
     * 计算节点报警
     * 
     * @param compute
     * @param crEntity
     * @throws SQLException
     *             sql异常
     */
    private void checkComputeWarn(Compute compute, ComputeResourceEntity crEntity) throws SQLException {
        String warnStr = crEntity.getWarn4Rack();
        if (StringUtil.isBlank(warnStr)) {
            return;
        }
        String[] datas = warnStr.split(";");
        double warnCPURate = Double.parseDouble(datas[0]);
        if (warnCPURate > 0) {
            //cpu使用率=1-(空闲cpu/（用户cpu+系统cpu+io等待cpu+空闲cpu
            double usedCPURate = 1 - (Double.valueOf(compute.getIdleCpu()) / (compute.getUserCpu()
                            + compute.getSystemCpu() + compute.getIdleCpu() + compute.getIowaitCpu()));
            if (usedCPURate >= warnCPURate) {
                insertComputeCPUWarn(crEntity, usedCPURate, warnCPURate);
            }
        }
        double warnMenRate = Double.parseDouble(datas[1]);
        if (warnMenRate > 0) {
            double usedMenRate = Double.valueOf(compute.getUsedMem()) / (compute.getUsedMem() + compute.getTotalMem());
            if (usedMenRate >= warnMenRate) {
                insertComputeMenWarn(crEntity, usedMenRate, warnMenRate);
            }
        }
    }

    /**
     * 检查存储报警
     * 
     * @param storage
     * @param storageResourceEntity
     * @throws VirtualizationException
     *             虚拟化异常
     * @throws SQLException
     *             sql异常
     */
    private void checkStorageWarn(Storage storage, StorageResourceEntity storageResourceEntity)
                    throws VirtualizationException, SQLException {
        String warnStr = storageResourceEntity.getWarn4Rack();
        if (StringUtil.isBlank(warnStr)) {
            return;
        }
        double warnStorageRate = Double.parseDouble(warnStr);
        if (warnStorageRate <= 0) {
            return;
        }
        double storageRate = 1 - (Double.valueOf(storage.getAvailable()) / storage.getCapacity());
        if (storageRate >= warnStorageRate) {
            Date warnDate = new Date();
            UserEntity rootDomainUser = ProjectUtil.getRootDomainUser();
            //发邮件
            String desc = String.format("存储资源【%1$s】存储使用率为：%2$s%%,超过报警值：%3$s%%,报警时间:%4$s", storageResourceEntity
                            .getName(), ProjectUtil.decimalFormat(storageRate * 100, "#.00"),
                            (int) (warnStorageRate * 100), DateUtil.format(warnDate, "yyyy-MM-dd HH:mm:ss"));
            //判断是否到达发邮件次数
            ServiceWarnNumWrapper swnw = ServiceWarnNumWrapper.getInstance();
            String storageWarnIdentity = ProjectUtil.generateStorageWarnIdentity(storageResourceEntity
                            .getIdentityName());
            Integer warnCount = swnw.plusOne(storageWarnIdentity).get(storageWarnIdentity);
            Integer warnCountLimit = Integer.parseInt(PropertyManager.getInstance().getDbProperty(
                            PropertyManager.DB_WARN_COUNT_SEND_EMAIL));

            if (warnCount > warnCountLimit) {
                if (rootDomainUser != null) {
                    MailUtil.sendMail("存储资源报警", desc, rootDomainUser.getEmail());
                }
                swnw.remove(storageWarnIdentity);
            }
            //插入报警日志
            WarnLogEntity warnLogEntity = new WarnLogEntity();
            warnLogEntity.setAddTime(warnDate);
            warnLogEntity.setDesc(desc);
            warnLogEntity.setEquipmentIdentity(storageWarnIdentity);
            warnLogDAO.insert(warnLogEntity);
        }
    }

    /**
     * 计算节点cpu报警
     * 
     * @param crEntity
     * @param usedCPURate
     * @param warnCPURate
     * @throws SQLException
     *             sql异常
     */
    private void insertComputeCPUWarn(ComputeResourceEntity crEntity, double usedCPURate, double warnCPURate)
                    throws SQLException {
        //发邮件
        UserEntity rootDomainUser = ProjectUtil.getRootDomainUser();
        Date warnDate = new Date();
        String desc = String.format("计算节点【%1$s】cpu使用率为：%2$s%%,超过报警值：%3$s%%,报警时间:%4$s", crEntity.getName(), ProjectUtil
                        .decimalFormat(usedCPURate * 100, "#.00"), (int) (warnCPURate * 100), DateUtil.format(warnDate,
                        "yyyy-MM-dd HH:mm:ss"));
        //判断是否到达发邮件次数
        ServiceWarnNumWrapper swnw = ServiceWarnNumWrapper.getInstance();
        String computeWarnIdentity = ProjectUtil.generateComputeCPUWarnIdentity(crEntity.getIdentityName());
        Integer warnCount = swnw.plusOne(computeWarnIdentity).get(computeWarnIdentity);
        Integer warnCountLimit = Integer.parseInt(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_WARN_COUNT_SEND_EMAIL));

        if (warnCount > warnCountLimit) {
            if (rootDomainUser != null) {
                MailUtil.sendMail("计算节点cpu报警", desc, rootDomainUser.getEmail());
            }
            swnw.remove(computeWarnIdentity);
        }
        //插入报警日志
        WarnLogEntity warnLogEntity = new WarnLogEntity();
        warnLogEntity.setAddTime(warnDate);
        warnLogEntity.setDesc(desc);
        warnLogEntity.setEquipmentIdentity(crEntity.getIdentityName());
        warnLogDAO.insert(warnLogEntity);
    }

    /**
     * 计算节点内存报警
     * 
     * @param crEntity
     * @param usedMenRate
     * @param warnMenRate
     * @throws SQLException
     *             sql异常
     */
    private void insertComputeMenWarn(ComputeResourceEntity crEntity, double usedMenRate, double warnMenRate)
                    throws SQLException {
        //发邮件
        UserEntity rootDomainUser = ProjectUtil.getRootDomainUser();
        Date warnDate = new Date();
        String desc = String.format("计算节点【%1$s】内存使用率为：%2$s%%,超过报警值：%3$s%%,报警时间:%4$s", crEntity.getName(), ProjectUtil
                        .decimalFormat(usedMenRate * 100, "#.00"), (int) (warnMenRate * 100), DateUtil.format(warnDate,
                        "yyyy-MM-dd HH:mm:ss"));
        //判断是否到达发邮件次数
        ServiceWarnNumWrapper swnw = ServiceWarnNumWrapper.getInstance();
        String computeWarnIdentity = ProjectUtil.generateComputeMemIdentity(crEntity.getIdentityName());
        Integer warnCount = swnw.plusOne(computeWarnIdentity).get(computeWarnIdentity);
        Integer warnCountLimit = Integer.parseInt(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_WARN_COUNT_SEND_EMAIL));

        if (warnCount > warnCountLimit) {
            if (rootDomainUser != null) {
                MailUtil.sendMail("计算节点内存报警", desc, rootDomainUser.getEmail());
            }
            swnw.remove(computeWarnIdentity);
        }
        //插入报警日志
        WarnLogEntity warnLogEntity = new WarnLogEntity();
        warnLogEntity.setAddTime(warnDate);
        warnLogEntity.setDesc(desc);
        warnLogEntity.setEquipmentIdentity(crEntity.getIdentityName());
        warnLogDAO.insert(warnLogEntity);
    }

    /**
     * 检查自动备份
     * 
     * @throws SQLException
     *             sql异常
     */
    private void checkAutoBackup() throws SQLException {
        String hql = "select tb_vm from VirtualMachineEntity tb_vm where tb_vm.backupTimeMark is not null and tb_vm.backupTimeMark <> ''";
        List<?> queryList = ProjectUtil.getEntityManager().createQuery(hql).getResultList();
        VirtualMachineEntity vmEntity = null;
        for (Object obj : queryList) {
            vmEntity = (VirtualMachineEntity) obj;
            checkBackupVm(vmEntity);
        }
    }

    /**
     * 检查并备份虚机
     * 
     * @param vmEntity
     * @param buEntity
     * @throws SQLException
     *             sql异常
     */
    private void checkBackupVm(VirtualMachineEntity vmEntity) throws SQLException {
        String backupTimeMark = vmEntity.getBackupTimeMark();
        //没有设置的不备份
        if (StringUtil.isBlank(backupTimeMark)) {
            return;
        }
        //没到备份时间
        if (!ProjectUtil.isBackupTime(vmEntity.getBackupTimeMark())) {
            return;
        }
        //备份
        insertBackupVM(vmEntity);
    }

    /**
     * 开始备份
     * 
     * @throws SQLException
     *             sql异常
     */
    private void insertBackupVM(VirtualMachineEntity virtualMachineEntity) throws SQLException {
        String backupDesc = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + ",系统自动备份";
        if (!virtualMachineEntity.getCreatedFlag().equals(Constant.VM_CREATE_SUCCESS)) {
            LogUtil.error(String.format("虚机【%1$s】系统备份：状态不正确", virtualMachineEntity.getName()));
            return;
        }
        //
        BackupStorageEntity bsEntity = backupStorageDAO.obtainMaxBackupStorage();
        if (bsEntity == null) {
            LogUtil.error(String.format("虚拟机【%1$s】系统备份：没有可用的备份存储！", virtualMachineEntity.getName()));
            return;
        }
        //删除以前的老备份  TODO ... 删除后备份失败？
        deleteOldBackup(virtualMachineEntity);
        String backupName = String.format("vm_%1$s_backup", virtualMachineEntity.getName());
        VMBackupEntity vMBackupEntity = new VMBackupEntity();
        vMBackupEntity.setBackupStorage(bsEntity);
        vMBackupEntity.setName(backupName);
        vMBackupEntity.setVirtualMachine(virtualMachineEntity);
        vMBackupEntity.setAddTime(new Date());
        vMBackupEntity.setDesc(backupDesc);
        vMBackupEntity.setCapacity(0L);//先置为0，防止哪空间是空指针，拿到存储后赋值
        vMBackupEntity.setOperateFlag("正在备份中");
        vMBackupDAO.insert(vMBackupEntity);
        //防止vmutil线程安全问题
        vMBackupDAO.getEntitiyManager().flush();
        //设置vMSnapshotDAO   
        virtualMachineEntity.setCreatedFlag(Constant.CREATE_BACKUP_FLAG);
        virtualMachineEntity.setCreatedResultMsg("正在创建备份");
        virtualMachineDAO.update(virtualMachineEntity);
        virtualMachineDAO.getEntitiyManager().flush();
        //插入日志
        DomainEntity domainEntity = virtualMachineEntity.getDomain();
        virtualMachineDAO.insertBySQL("insert into tb_eventlog(USER_ID_,DOMAIN_NAME_,DESC_,ADDTIME_)"
                        + " values(:userId,:domainName,:desc,:time)", Arrays.asList(new String[] { "userId",
                        "domainName", "desc", "time" }), Arrays.asList(new Object[] {
                        domainEntity.getUser().getId(),
                        domainEntity.getName(),
                        String
                                        .format("虚拟机【%1$s】系统自动备份【%2$s】", virtualMachineEntity.getName(), vMBackupEntity
                                                        .getDesc()), new Date() }));
        //后台线程创建备份
        VMUtil.backupOperate(Constant.CREATE_BACKUP_FLAG, virtualMachineEntity.getId(), vMBackupEntity.getId());
    }

    /**
     * 删除老备份
     * 
     * @param vmEntity
     * @param em
     * @throws SQLException
     *             sql异常
     */
    private void deleteOldBackup(VirtualMachineEntity vmEntity) throws SQLException {
        List<VMBackupEntity> vMBackups = vmEntity.getVmBackups();
        if (vMBackups.isEmpty()) {
            return;
        }
        VMBackupEntity backupEntity = vMBackups.get(0);
        BackupStorageEntity bsEntity = backupEntity.getBackupStorage();
        //调用core
        try {
            VirtualMachine vm = VirtualMachine.getVM(vmEntity.getVmName(), vmEntity.getComputeResource().getIp());
            if (vm == null) {
                LogUtil.error(String.format("系统备份:虚拟机【%1$s】不存在", vmEntity.getName()));
            } else {
                vm.deleteBackUp(bsEntity.getIp(), backupEntity.getName());
            }
        } catch (VirtualizationException e) {
            LogUtil.warn(e);
            return;
        } catch (Exception e) {
            LogUtil.warn(e);
            return;
        }
        //更新资源
        bsEntity.setAvailableCapacity(bsEntity.getAvailableCapacity() + backupEntity.getCapacity());
        backupEntity.setVirtualMachine(null);
        backupStorageDAO.update(bsEntity);
        vMBackups.remove(backupEntity);
        vmEntity.setVmBackups(vMBackups);
        virtualMachineDAO.update(vmEntity);
        vMBackupDAO.delete(vMBackupDAO.getEntitiyManager().merge(backupEntity));
    }
}
