/**
 * 
 */
package com.cloudking.cloudmanagerweb.service.storageresource;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;
import com.cloudking.cloudmanagerweb.vo.StorageResourceVO;

/**
 * 存储资源service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("storageResourceService")
public class StorageResourceService extends BaseService {

    /**
     * 资源DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;

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
     * 域DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;

    /**
     * 搜索资源
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<StorageResourceVO> cloudContext) throws Exception {
        //结果集
        List<StorageResourceVO> queryResult = new ArrayList<StorageResourceVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(
                        storageResourceDAO.getQueryCount(cloudContext.getStringParam("qName"), cloudContext
                                        .getLongParam("qRoom"), cloudContext.getLongParam("qRack")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> resultSet = storageResourceDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getLongParam("qRoom"), cloudContext.getLongParam("qRack"), cloudContext.getPageInfo());
            StorageResourceVO storageResourceVO = null;
            StorageResourceEntity storageResult = null;
            for (Object[] result : resultSet) {
                storageResourceVO = new StorageResourceVO();
                storageResult = (StorageResourceEntity) result[0];
                BeanUtils.copyProperties(storageResult, storageResourceVO);
                Storage storage = null;
                //调用core
                try {
                    storage = Storage.getStorage(storageResult.getPoolName());
                    if (storage != null) {
                        storageResourceVO.setRealAvailableCapacity(ProjectUtil.kByteToGiga(storage.getAvailable())
                                        .longValue());
                    }
                } catch (VirtualizationException e) {
                    cloudContext.addErrorMsg(String.format("获取存【%1$s】储容量失败", storageResult.getName()) + e.getMessage());
                    LogUtil.warn(e);
                } catch (Exception e) {
                    cloudContext.addErrorMsg(String.format("获取存【%1$s】储容量失败", storageResult.getName()) + e.getMessage());
                    LogUtil.warn(e);
                }
                storageResourceVO.setCapacity(ProjectUtil.kByteToGiga(storageResourceVO.getCapacity()).longValue());
                storageResourceVO.setAvailableCapacity(ProjectUtil
                                .kByteToGiga(storageResourceVO.getAvailableCapacity()).longValue());
                storageResourceVO.setMachineRackName((String) result[1]);
                storageResourceVO.setMachineRoomName((String) result[2]);
                queryResult.add(storageResourceVO);
            }
        }
        cloudContext.addParam("storageResources", queryResult);
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
    public void insert(CloudContext<StorageResourceVO> cloudContext) throws Exception {
        MachineRackEntity machineRackEntity = machineRackDAO.load(cloudContext.getLongParam("rackID"));
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("选择的机架不存在！");
            return;
        }
        StorageResourceEntity storageResourceEntity = storageResourceDAO.getByNameAndRack(cloudContext.getVo()
                        .getName(), machineRackEntity.getId());
        if (storageResourceEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】机架下面的【%2$s】已经存在", machineRackEntity.getName(), cloudContext
                            .getVo().getName()));
            return;
        }
        StorageResourceEntity storageGetByIp = storageResourceDAO.findStorageByIp(cloudContext.getVo().getIp());
        if (storageGetByIp != null) {
            cloudContext.addErrorMsg(String.format("此IP下已经存在存储【%1$s】", storageGetByIp.getName()));
            return;
        }
        storageResourceEntity = new StorageResourceEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), storageResourceEntity);
        storageResourceEntity.setAddTime(new Date());
        storageResourceEntity.setMachineRack(machineRackEntity);
        String poolName = ProjectUtil.createStoragePoolName();
        storageResourceEntity.setPoolName(poolName);
        storageResourceEntity.setIdentityName(ProjectUtil.createStorageResourceIdentityName());
        storageResourceEntity
                        .setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                                        .getVo().getWarn4Rack());
        //设置rrd文件路径
        storageResourceEntity.setRrdPath(ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + machineRackEntity.getMachineRoom().getIdentityName() + File.separator
                        + machineRackEntity.getIdentityName() + File.separator
                        + storageResourceEntity.getIdentityName());

        Storage storage = null;
        //调用core
        try {
            storage = Storage.createStorage(poolName, storageResourceEntity.getIp());
        } catch (VirtualizationException e) {
            cloudContext.addErrorMsg("添加失败:" + e.getMessage());
            LogUtil.warn(e);
            return;
        } catch (Exception e) {
            cloudContext.addErrorMsg("添加失败:" + e.getMessage());
            LogUtil.warn(e);
            return;
        }
        DomainEntity rootDomain = domainDAO.findByCode(Constant.ROOT_DOMAIN_CODE);
        //成功
        Long capacity = storage.getCapacity();
        Long availableCapacity = storage.getAvailable();
        storageResourceEntity.setCapacity(capacity);
        storageResourceEntity.setAvailableCapacity(availableCapacity);
        storageResourceEntity.setDomain(rootDomain);//存储全部关联到根域，由根域分配空间给子域

        //更新根域存储大小
        Long rootStorageCapacity = rootDomain.getStorageCapacity();
        rootStorageCapacity = (rootStorageCapacity == null ? 0 : rootStorageCapacity) + capacity;
        rootDomain.setStorageCapacity(rootStorageCapacity);
        Long availabaleCapacity = rootDomain.getAvailableStorageCapacity();
        rootDomain.setAvailableStorageCapacity(availabaleCapacity == null ? 0 : availabaleCapacity + availableCapacity);
        domainDAO.update(rootDomain);

        //创建rrd文件
        try {
            RRDUtil.createStorageResourceRRDS(storageResourceEntity.getRrdPath());
        } catch (RuntimeException e) {
            LogUtil.error("创建RRD文件出错", e);
            RRDUtil.deleteStorageResourceRRDS(storageResourceEntity.getRrdPath());
            cloudContext.addErrorMsg("创建RRD文件出错!");
        }
        //成功
        storageResourceDAO.insert(storageResourceEntity);
        cloudContext.addSuccessMsg("添加成功!");
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void update(CloudContext<StorageResourceVO> cloudContext) throws Exception {
        //查找机架
        MachineRackEntity machineRackEntity = machineRackDAO.load(cloudContext.getLongParam("rackID"));
        if (machineRackEntity == null) {
            cloudContext.addErrorMsg("选择的机架不存在！");
            return;
        }
        StorageResourceEntity storageResourceEntity = storageResourceDAO.get(cloudContext.getVo().getId());
        if (storageResourceEntity == null) {
            cloudContext.addErrorMsg("资源不存在,请刷新后重试");
            return;
        }

        MachineRoomEntity destRoomEntity = machineRackEntity.getMachineRoom();
        String destRrdPath = ProjectUtil.getRRDDir().getAbsolutePath() + File.separator
                        + destRoomEntity.getIdentityName() + File.separator + machineRackEntity.getIdentityName()
                        + File.separator + storageResourceEntity.getIdentityName();
        //移动rrd文件
        try {
            RRDUtil.moveRRDFile(storageResourceEntity.getRrdPath(), destRrdPath);
        } catch (Exception e) {
            cloudContext.addErrorMsg("rrd文件迁移错误");
            LogUtil.error(e);
            return;
        }
        storageResourceEntity.setMachineRack(machineRackEntity);
        storageResourceEntity.setRrdPath(destRrdPath);
        storageResourceEntity.setDesc(cloudContext.getVo().getDesc());
        storageResourceEntity
                        .setWarn4Rack(StringUtil.isBlank(cloudContext.getVo().getWarn4Rack()) ? null : cloudContext
                                        .getVo().getWarn4Rack());
        storageResourceDAO.update(storageResourceEntity);
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
    public void delete(CloudContext<StorageResourceVO> cloudContext) throws Exception {
        StorageResourceEntity storageResourceEntity = storageResourceDAO.get(cloudContext.getVo().getId());
        if (storageResourceEntity == null) {
            cloudContext.addErrorMsg("存储资源不存在!");
            return;
        }

        List<VolumnEntity> volumnEntitys = storageResourceEntity.getVolumns();
        for (VolumnEntity v : volumnEntitys) {
            if (v.getVirtualMachine() != null) {
                cloudContext.addErrorMsg("此存储下尚有虚机存在，请先删除虚机！");
                return;
            }
        }
        DomainEntity rootDomain = domainDAO.findByCode(Constant.ROOT_DOMAIN_CODE);
        Long rootAvailableCapacity = rootDomain.getAvailableStorageCapacity();
        Long avaiCapacity = storageResourceEntity.getAvailableCapacity();
        if (rootAvailableCapacity < avaiCapacity) {
            //TODO 考虑存储迁移问题。。。
            cloudContext.addErrorMsg("删除失败：根域存储可用空间小于当前要删除的存储可用空间!请先收回存储空间后再删除");
            return;
        }

        try {
            //掉用core
            Storage storage = Storage.getStorage(storageResourceEntity.getPoolName());
            storage.delete();
        } catch (VirtualizationException e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        }
        //更新根域存储大小
        Long rootStorageCapacity = rootDomain.getAvailableStorageCapacity();
        Long capacity = storageResourceEntity.getCapacity();
        rootStorageCapacity = rootStorageCapacity - capacity;
        rootDomain.setStorageCapacity(rootStorageCapacity);
        rootDomain.setAvailableStorageCapacity(rootAvailableCapacity - capacity);

        //提示信息
        RRDUtil.deleteStorageResourceRRDS(storageResourceEntity.getRrdPath());
        domainDAO.update(rootDomain);
        storageResourceDAO.delete(storageResourceEntity);
        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 初始化新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<StorageResourceVO> cloudContext) throws Exception {
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
            StorageResourceEntity storageResourceEntity = storageResourceDAO.get(cloudContext.getVo().getId());
            if (storageResourceEntity == null) {
                cloudContext.addErrorMsg("资源不存在！");
                return;
            }
            StorageResourceVO storageResourceVO = new StorageResourceVO();
            BeanUtils.copyProperties(storageResourceEntity, storageResourceVO);
            cloudContext.addParam("dataVo", storageResourceVO);
            cloudContext.addParam("machineRackID", storageResourceEntity.getMachineRack().getId());
            cloudContext.addParam("machineRoomID", storageResourceEntity.getMachineRack().getMachineRoom().getId());
            //获取机架
            List<MachineRackVO> machineRacks = new ArrayList<MachineRackVO>();
            List<MachineRackEntity> machineRackEntitys = storageResourceEntity.getMachineRack().getMachineRoom()
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
    public void queryRackByRoom(CloudContext<StorageResourceVO> cloudContext) throws Exception {
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
