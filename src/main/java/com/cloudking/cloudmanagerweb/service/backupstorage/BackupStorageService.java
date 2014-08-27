/**
 * 
 */
package com.cloudking.cloudmanagerweb.service.backupstorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.backup.BackupStorage;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.BackupStorageDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.VMBackupDAO;
import com.cloudking.cloudmanagerweb.entity.BackupStorageEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.BackupStorageVO;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;

/**
 * 存储资源service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("backupStorageService")
public class BackupStorageService extends BaseService {

    /**
     * 资源DAO
     */
    @Resource
    private BackupStorageDAO backupStorageDAO;

    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;

    /**
     * 机架DAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * 备份DAO
     */
    @Resource
    private VMBackupDAO vMBackupDAO;

    /**
     * 搜索资源
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        //结果集
        List<BackupStorageVO> queryResult = new ArrayList<BackupStorageVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        backupStorageDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext
                                        .getLongParam("qRoom"), cloudContext.getLongParam("qRack")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> resultSet = backupStorageDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLongParam("qRoom"), cloudContext.getLongParam("qRack"), cloudContext.getPageInfo());
            BackupStorageVO backupStorageVO = null;
            BackupStorageEntity storageResult = null;
            for (Object[] result : resultSet) {
                backupStorageVO = new BackupStorageVO();
                storageResult = (BackupStorageEntity) result[0];
                BeanUtils.copyProperties(storageResult, backupStorageVO);
                BackupStorage backupStorage = null;
                //调用core
                try {
                    backupStorage = BackupStorage.getStorage(storageResult.getIp());
                    backupStorageVO.setRealAvailableCapacity(ProjectUtil.kByteToGiga(backupStorage.getAvailable())
                                    .longValue());
                } catch (Exception e) {
                    cloudContext.addWarnMsg(String.format("获取存【%1$s】储容量失败", storageResult.getName()) + e.getMessage());
                    LogUtil.warn(e);
                    backupStorageVO.setRealAvailableCapacity(0L);
                }
                backupStorageVO.setCapacity(ProjectUtil.kByteToGiga(backupStorageVO.getCapacity()).longValue());
                backupStorageVO.setAvailableCapacity(ProjectUtil.kByteToGiga(backupStorageVO.getAvailableCapacity())
                                .longValue());
                backupStorageVO.setMachineRackName((String) result[1]);
                backupStorageVO.setMachineRoomName((String) result[2]);
                queryResult.add(backupStorageVO);
            }
        }
        cloudContext.addParam("backupStorages", queryResult);
        //获取机房
        List<MachineRoomVO> machineRooms = new ArrayList<MachineRoomVO>();
        List<MachineRoomEntity> machineRoomEntitys = machineRoomDAO.listOrderBy("addTime");
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity machineRoomEntity : machineRoomEntitys) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
            machineRooms.add(machineRoomVO);
        }
        cloudContext.addParam("machineRooms", machineRooms);
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
        cloudContext.addParam("machineRacks", machineRacks);
    }

    /**
     * 新增
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insert(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        MachineRackEntity machineRackEntity = machineRackDAO.load(cloudContext.getLongParam("rackID"));
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("选择的机架不存在！");
            return;
        }
        BackupStorageEntity backupStorageEntity = backupStorageDAO.getByNameAndRack(cloudContext.getVo().getName(),
                        machineRackEntity.getId());
        if (backupStorageEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】机架下面的【%2$s】已经存在", machineRackEntity.getName(), cloudContext
                            .getVo().getName()));
            return;
        }
        BackupStorageEntity storageGetByIp = backupStorageDAO.findStorageByIp(cloudContext.getVo().getIp());
        if (storageGetByIp != null) {
            cloudContext.addErrorMsg(String.format("此IP下已经存在存储【%1$s】", storageGetByIp.getName()));
            return;
        }
        backupStorageEntity = new BackupStorageEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), backupStorageEntity);
        backupStorageEntity.setAddTime(new Date());
        backupStorageEntity.setMachineRack(machineRackEntity);
        backupStorageEntity.setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                        .getVo().getWarn4Rack());
        BackupStorage backupStorage = null;
        try {
            //调用core
            backupStorage = BackupStorage.addBackupStorage(cloudContext.getVo().getIp(), cloudContext.getVo()
                            .getUsername(), cloudContext.getVo().getPassword());
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        }
        backupStorageEntity.setIdentityName(ProjectUtil.createBackupStoragePoolName());
        //设置rrd文件路径
        backupStorageEntity.setRrdPath(ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + machineRackEntity.getMachineRoom().getIdentityName() + File.separator
                        + machineRackEntity.getIdentityName() + File.separator + backupStorageEntity.getIdentityName());

        DomainEntity rootDomain = domainDAO.findByCode(Constant.ROOT_DOMAIN_CODE);
        //成功
        backupStorageEntity.setCapacity(backupStorage.getCapacity());
        backupStorageEntity.setAvailableCapacity(backupStorage.getAvailable());

        //创建rrd文件
        try {
            RRDUtil.createBackupStorageResourceRRDS(backupStorageEntity.getRrdPath());
        } catch (RuntimeException e) {
            LogUtil.error("创建RRD文件出错", e);
            RRDUtil.deleteBackupStorageResourceRRDS(backupStorageEntity.getRrdPath());
            cloudContext.addErrorMsg("创建RRD文件出错!");
        }
        //成功
        backupStorageDAO.insert(backupStorageEntity);
        //更新根域备份存储大小
        Long capacity = ProjectUtil.kByteToGiga(backupStorage.getCapacity()).longValue();
        Long availableCapacity = ProjectUtil.kByteToGiga(backupStorage.getAvailable()).longValue();
        Long rootStorageCapacity = rootDomain.getBackupStorageCapacity();
        rootStorageCapacity = (rootStorageCapacity == null ? 0 : rootStorageCapacity) + capacity;
        rootDomain.setBackupStorageCapacity(rootStorageCapacity);
        Long availabaleCapacity = rootDomain.getAvailableBackupStorageCapacity();
        rootDomain.setAvailableBackupStorageCapacity(availabaleCapacity == null ? 0 : availabaleCapacity
                        + availableCapacity);
        domainDAO.update(rootDomain);

        cloudContext.addSuccessMsg("添加成功!");
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void update(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        //查找机架
        MachineRackEntity machineRackEntity = machineRackDAO.load(cloudContext.getLongParam("rackID"));
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("选择的机架不存在！");
            return;
        }
        BackupStorageEntity backupStorageEntity = backupStorageDAO.get(cloudContext.getVo().getId());
        if (backupStorageEntity == null) {
            cloudContext.addErrorMsg("资源不存在,请刷新后重试");
            return;
        }
        MachineRoomEntity destRoomEntity = machineRackEntity.getMachineRoom();
        String destRrdPath = ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + destRoomEntity.getIdentityName() + File.separator + machineRackEntity.getIdentityName()
                        + File.separator + backupStorageEntity.getIdentityName();
        //移动rrd文件
        try {
            RRDUtil.moveRRDFile(backupStorageEntity.getRrdPath(), destRrdPath);
        } catch (Exception e) {
            cloudContext.addErrorMsg("rrd文件迁移错误");
            LogUtil.error(e);
            return;
        }
        backupStorageEntity.setMachineRack(machineRackEntity);
        backupStorageEntity.setRrdPath(destRrdPath);
        backupStorageEntity.setDesc(cloudContext.getVo().getDesc());
        backupStorageEntity.setMachineRack(machineRackEntity);
        backupStorageEntity.setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                        .getVo().getWarn4Rack());
        backupStorageDAO.update(backupStorageEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void delete(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        BackupStorageEntity backupStorageEntity = backupStorageDAO.get(cloudContext.getVo().getId());
        if (backupStorageEntity == null) {
            cloudContext.addErrorMsg("存储资源不存在!");
            return;
        }
        Integer backupCount = vMBackupDAO.countByBackupStorageId(backupStorageEntity.getId());
        if (backupCount > 0) {
            cloudContext.addErrorMsg("备份存储上还存在虚机备份，请先删除虚机备份文件");
            return;
        }
        DomainEntity rootDomain = domainDAO.findByCode(Constant.ROOT_DOMAIN_CODE);
        Long rootAvailableCapacity = rootDomain.getAvailableBackupStorageCapacity();
        Long avaiCapacity = ProjectUtil.kByteToGiga(backupStorageEntity.getAvailableCapacity()).longValue();
        if (rootAvailableCapacity < avaiCapacity) {
            cloudContext.addErrorMsg("删除失败：根域备份存储可用空间小于当前要删除的备份存储可用空间!请先收回空间后再删除");
            return;
        }
        try {
            //掉用core 
            BackupStorage.deleteBackupStorage(backupStorageEntity.getIp());
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
        }

        RRDUtil.deleteBackupStorageResourceRRDS(backupStorageEntity.getRrdPath());
        //提示信息
        backupStorageDAO.delete(backupStorageEntity);
        //更新根域备份存储大小
        Long rootStorageCapacity = rootDomain.getBackupStorageCapacity();
        Long capacity = ProjectUtil.kByteToGiga(backupStorageEntity.getCapacity()).longValue();
        rootDomain.setBackupStorageCapacity(rootStorageCapacity - capacity);
        rootDomain.setAvailableBackupStorageCapacity(rootAvailableCapacity - avaiCapacity);

        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 初始化新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        //获取机房
        List<MachineRoomVO> machineRooms = new ArrayList<MachineRoomVO>();
        List<MachineRoomEntity> machineRoomEntitys = machineRoomDAO.listOrderBy("addTime");
        MachineRoomVO machineRoomVO = null;
        for (MachineRoomEntity machineRoomEntity : machineRoomEntitys) {
            machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
            machineRooms.add(machineRoomVO);
        }
        cloudContext.addParam("machineRooms", machineRooms);
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            BackupStorageEntity backupStorageEntity = backupStorageDAO.get(cloudContext.getVo().getId());
            if (backupStorageEntity == null) {
                cloudContext.addErrorMsg("资源不存在！");
                return;
            }
            BackupStorageVO backupStorageVO = new BackupStorageVO();
            BeanUtils.copyProperties(backupStorageEntity, backupStorageVO);
            cloudContext.addParam("dataVo", backupStorageVO);
            cloudContext.addParam("machineRackID", backupStorageEntity.getMachineRack().getId());
            cloudContext.addParam("machineRoomID", backupStorageEntity.getMachineRack().getMachineRoom().getId());
            //获取机架
            List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
            List<MachineRackEntity> machineRackEntitys = backupStorageEntity.getMachineRack().getMachineRoom()
                            .getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
            cloudContext.addParam("machineRacks", machineRacks);
        }
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRackByRoom(CloudContext<BackupStorageVO> cloudContext) throws Exception {
        //获取机架
        List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
        MachineRoomEntity machineRoomEntity = machineRoomDAO.load(cloudContext.getLongParam("roomID"));
        if (machineRoomEntity != null) {
            List<MachineRackEntity> machineRackEntitys = machineRoomEntity.getMachineRacks();
            MachineRackVO machineRackVO = null;
            for (MachineRackEntity machineRackEntity : machineRackEntitys) {
                machineRackVO = new MachineRackVO();
                BeanUtils.copyProperties(machineRackEntity, machineRackVO);
                machineRacks.add(machineRackVO);
            }
        }
        cloudContext.addParam("machineRacks", machineRacks);
    }
}
