/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.domain;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourcePoolDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.MenuDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.RightsDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderAttachmentDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRackEntity;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.entity.MenuEntity;
import com.cloudking.cloudmanagerweb.entity.RightsEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderAttachmentEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.MailUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;
import com.cloudking.cloudmanagerweb.vo.ComputeResourceVO;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MachineRackVO;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;
import com.cloudking.cloudmanagerweb.vo.MenuVO;
import com.cloudking.cloudmanagerweb.vo.RightsVO;
import com.cloudking.cloudmanagerweb.vo.UserVO;

/**
 * 域service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("domainService")
public class DomainService {

    /**
     * domainDAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * menuDAO
     */
    @Resource
    private MenuDAO menuDAO;
    /**
     * rightsDAO
     */
    @Resource
    private RightsDAO rightsDAO;

    /**
     * workOrderDAO
     */
    @Resource
    private WorkOrderDAO workOrderDAO;

    /**
     * workOrderAttachmentDAO
     */
    @Resource
    private WorkOrderAttachmentDAO workOrderAttachmentDAO;

    /**
     * userDAO
     */
    @Resource
    private UserDAO userDAO;
    /**
     * 计算节点资源池DAO
     */
    @Resource
    private ComputeResourcePoolDAO computeResourcePoolDAO;
    /**
     * 存储节点DAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;
    /**
     * 资源DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;
    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;
    /**
     * 机柜DAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 网络DAO
     */
    @Resource
    private NetWorkDAO netWorkDAO;
    /**
     * 虚机dao
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;
    /**
     * 模版DAO
     */
    @Resource
    private TemplateDAO templateDAO;
    /**
     * 机型DAO
     */
    @Resource
    private MachineTypeDAO machineTypeDAO;

    /**
     * 添加域
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<DomainVO> cloudContext) throws Exception {
        DomainEntity superDomainEntity = domainDAO.findByCode(cloudContext.getStringParam("superDomainCode"));
        DomainEntity domainEntity = new DomainEntity();
        if (!setSuperDomainAndCurrentDomain(superDomainEntity, domainEntity, cloudContext)) {
            return;
        }

        //创建文件夹
        File dir = new File(ProjectUtil.getTemplateDir() + File.separator + domainEntity.getCode());
        dir.mkdir();
        dir.setWritable(true, false);

        domainDAO.insert(domainEntity);
        domainDAO.update(superDomainEntity);
        if (cloudContext.getSuccess()) {
            saveDefaultRightsToDomain(domainEntity);
            DomainVO domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            cloudContext.addParam("addSuccessDomain", domainVO);
            //提示信息
            cloudContext.addSuccessMsg("添加成功!");
        }
    }

    /**
     * 为新域创建默认权限菜单
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void saveDefaultRightsToDomain(DomainEntity domainEntity) throws Exception {
        //获取默认域菜单
        List<MenuEntity> menuList = menuDAO.getDefaultDomainMenu();
        List<RightsEntity> rightsList = rightsDAO.getDefaultDomainRights();
        domainEntity.setMenu(menuList);
        domainEntity.setRights(rightsList);
        domainDAO.update(domainEntity);
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<DomainVO> cloudContext) throws Exception {
        List<DomainEntity> domains = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        //结果集
        List<DomainVO> queryResult = new ArrayList<DomainVO>();
        List<Object[]> objss = domainDAO.queryManageAndDescendantDomainByUserId(cloudContext.getLoginedUser().getId());
        DomainVO domainVO = null;
        for (Object[] objs : objss) {
            domainVO = new DomainVO();
            domainVO.setId(Long.parseLong(objs[0].toString()));
            domainVO.setAvailableStorageCapacity(ProjectUtil.kByteToGiga(Long.parseLong(objs[1].toString()))
                            .longValue());
            domainVO.setCode((String) objs[2]);
            domainVO.setCpuAvailableNum((Integer) objs[3]);
            domainVO.setCpuTotalNum((Integer) objs[4]);
            domainVO.setDesc((String) objs[5]);
            domainVO.setMemoryAvailableCapacity(Integer.parseInt(objs[6].toString()));
            domainVO.setMemoryCapacity(Integer.parseInt(objs[7].toString()));
            domainVO.setName((String) objs[8]);
            domainVO.setStorageCapacity(ProjectUtil.kByteToGiga(Long.parseLong(objs[9].toString())).longValue());
            domainVO.setBackupStorageCapacity(Long.parseLong(objs[10].toString()));
            domainVO.setAvailableBackupStorageCapacity(Long.parseLong(objs[11].toString()));
            domainVO.setUsername(objs[12] == null ? "无" : (String) objs[12]);
            domainVO.setVmNum(Integer.parseInt(objs[14].toString()));
            domainVO.setRootDomain(isRootDomain(domains, domainVO.getCode()));
            queryResult.add(domainVO);
        }
        cloudContext.addParam("domains", queryResult);
    }

    /**
     * 判断是否是根域(如果其上级的code在当前用户所属的域中没有找到，则是根域）
     * 
     * @param domains
     * @param code
     * @return
     */
    private Boolean isRootDomain(List<DomainEntity> domains, String code) {
        code = code.substring(0, code.length() - 2);
        for (DomainEntity e : domains) {
            if (code.lastIndexOf(e.getCode()) != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取菜单Json数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryMenuJsonTree(CloudContext<DomainVO> cloudContext) throws Exception {
        List<MenuEntity> allMenuList = menuDAO.queryAllMenu();
        List<MenuEntity> domainMenuList = menuDAO.queryMenuOnThisDomain(cloudContext.getVo().getId());

        List<MenuVO> menuVOS = new ArrayList<MenuVO>();
        MenuVO menuVO = null;
        for (MenuEntity tmpAllMenuEntity : allMenuList) {
            menuVO = new MenuVO();
            if (domainMenuList.contains(tmpAllMenuEntity)) {
                menuVO.setHasMenuFlag(true);
            } else {
                menuVO.setHasMenuFlag(false);
            }
            BeanUtils.copyProperties(tmpAllMenuEntity, menuVO);
            menuVOS.add(menuVO);
        }
        cloudContext.addParam("menu", ProjectUtil.generateMenuForZtree(menuVOS));
    }

    /**
     * 获取指定菜单权限
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRightsByMenu(CloudContext<DomainVO> cloudContext) throws Exception {
        List<RightsEntity> allRightsList = rightsDAO.queryAllRightsOnThisMenu(cloudContext.getLongParam("menuId"));
        List<RightsEntity> domainRightsList = rightsDAO.queryDomainRightsOnThisMenu(
                        cloudContext.getLongParam("menuId"), cloudContext.getLongParam("domainId"));

        List<RightsVO> rightsVOS = new ArrayList<RightsVO>();
        RightsVO rightsVO = null;
        for (RightsEntity rightsEntity : allRightsList) {
            rightsVO = new RightsVO();
            if (domainRightsList.contains(rightsEntity)) {
                rightsVO.setHasRights(true);
            } else {
                rightsVO.setHasRights(false);
            }
            BeanUtils.copyProperties(rightsEntity, rightsVO);
            rightsVOS.add(rightsVO);
        }
        cloudContext.addParam("rights", rightsVOS);
    }

    /**
     * 保存授权
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void saveAuthorization(CloudContext<DomainVO> cloudContext) throws Exception {
//        String menuId = cloudContext.getStringParam("menuId");
//        String menuChecked = cloudContext.getStringParam("menuChecked");
//        String righgtsIds = cloudContext.getStringParam("righgtsIds");
//        String righgtsMenus = cloudContext.getStringParam("righgtsMenus");
//        String righgtsChecked = cloudContext.getStringParam("righgtsChecked");
//        Long domainId = cloudContext.getLongParam("domainId");
        //设置权限

        //
    }

    /**
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<DomainVO> cloudContext) throws Exception {
        DomainEntity domainEntity = domainDAO.get(cloudContext.getVo().getId());
        if (domainEntity == null) {
            cloudContext.addErrorMsg("删除的域不存在，请刷新后重试！");
            return;
        }
        List<DomainEntity> childrenDomains = domainDAO.queryBySuperDomainCode(domainEntity.getCode());
        if (childrenDomains.size() > 0) {
            cloudContext.addErrorMsg(String.format("【%1$s】存在子域，请先删除子域", domainEntity.getName()));
            return;
        }
        //判断是否存在虚拟机
        Integer vmCount = virtualMachineDAO.getVmCountByDomainId(domainEntity.getId());
        if (!vmCount.equals(0)) {
            cloudContext.addErrorMsg(String.format("【%1$s】存在虚拟机，请先删除虚拟机", domainEntity.getName()));
            return;
        }
        Integer templateCount = templateDAO.getCountByDomainId(domainEntity.getId());
        if (templateCount > 0) {
            cloudContext.addErrorMsg(String.format("【%1$s】存在模版，请先删除模版", domainEntity.getName()));
            return;
        }
        //判断机型
        Integer mtCount = machineTypeDAO.getCountByDomainId(domainEntity.getId());
        if (mtCount > 0) {
            cloudContext.addErrorMsg(String.format("【%1$s】存在配置，请先删除配置", domainEntity.getName()));
            return;
        }
        //删除关联工单
        List<WorkOrderAttachmentEntity> workOrderAttachmentEntitys = workOrderAttachmentDAO
                        .queryWorkOrderAttachmentsByDomainId(domainEntity.getId());
        for (WorkOrderAttachmentEntity workOrderAttachmentEntity : workOrderAttachmentEntitys) {
            String serialNumber = workOrderAttachmentEntity.getWorkOrder().getSerialNumber();
            File woaDirectoryFile = new File(ProjectUtil.getWorkOrderAttachmentDir(), serialNumber);
            File woaFile = new File(woaDirectoryFile, workOrderAttachmentEntity.getUuidName());
            woaFile.delete();
            woaDirectoryFile.delete();
        }
        workOrderAttachmentDAO.deleteByDomainId(domainEntity.getId());

        //删除文件夹
        File dir = new File(ProjectUtil.getTemplateDir() + File.separator + domainEntity.getCode());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        dir.delete();

        //更新父域的存储空间
        String superDomainCode = domainEntity.getCode().substring(0, domainEntity.getCode().length() - 2);
        DomainEntity superDomainEntity = domainDAO.findByCode(superDomainCode);
        superDomainEntity.setAvailableStorageCapacity(superDomainEntity.getAvailableStorageCapacity()
                        + domainEntity.getStorageCapacity());
        superDomainEntity.setCpuAvailableNum(superDomainEntity.getCpuAvailableNum() + domainEntity.getCpuTotalNum());
        superDomainEntity.setMemoryAvailableCapacity(superDomainEntity.getMemoryAvailableCapacity()
                        + domainEntity.getMemoryCapacity());
        domainDAO.update(superDomainEntity);
        domainDAO.delete(domainEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<DomainVO> cloudContext) throws Exception {
        DomainEntity domainEntity = domainDAO.get(cloudContext.getVo().getId());
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在,刷新后重试！");
            return;
        }
        //如果是根域，就只要更新描述
        String code = domainEntity.getCode();
        List<DomainEntity> domains = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        if (isRootDomain(domains, code)) {//TODO .. 优化判断是否为根域
            domainEntity.setDesc(cloudContext.getVo().getDesc());
            domainDAO.update(domainEntity);
            cloudContext.addSuccessMsg("修改成功!");
            return;
        }
        //获取修改后的资源与修改前的资源差
        Long storageRange = cloudContext.getVo().getStorageCapacity() - domainEntity.getStorageCapacity();
        Long backupStorageRange = cloudContext.getVo().getBackupStorageCapacity()
                        - domainEntity.getBackupStorageCapacity();
        Integer cpuRange = cloudContext.getVo().getCpuTotalNum() - domainEntity.getCpuTotalNum();
        Integer memoryRange = cloudContext.getVo().getMemoryCapacity() - domainEntity.getMemoryCapacity();
        //更新本域
        domainEntity.setStorageCapacity(cloudContext.getVo().getStorageCapacity());
        domainEntity.setAvailableStorageCapacity(domainEntity.getAvailableStorageCapacity() + storageRange);
        domainEntity.setBackupStorageCapacity(cloudContext.getVo().getBackupStorageCapacity());
        domainEntity.setAvailableBackupStorageCapacity(domainEntity.getAvailableBackupStorageCapacity()
                        + backupStorageRange);
        domainEntity.setCpuTotalNum(cloudContext.getVo().getCpuTotalNum());
        domainEntity.setCpuAvailableNum(domainEntity.getCpuAvailableNum() + cpuRange);
        domainEntity.setMemoryCapacity(cloudContext.getVo().getMemoryCapacity());
        domainEntity.setMemoryAvailableCapacity(domainEntity.getMemoryAvailableCapacity() + memoryRange);
        domainEntity.setDesc(cloudContext.getVo().getDesc());
        domainDAO.update(domainEntity);
        //更新上级域空间
        code = code.substring(0, code.length() - 2);
        DomainEntity superDomain = domainDAO.findByCode(code);
        if (superDomain == null) {
            cloudContext.addErrorMsg("上级不存在");
            return;
        }
        superDomain.setAvailableStorageCapacity(superDomain.getAvailableStorageCapacity() - storageRange);
        superDomain.setAvailableBackupStorageCapacity(superDomain.getAvailableBackupStorageCapacity()
                        - backupStorageRange);
        superDomain.setCpuAvailableNum(superDomain.getCpuAvailableNum() - cpuRange);
        superDomain.setMemoryAvailableCapacity(superDomain.getMemoryAvailableCapacity() - memoryRange);
        domainDAO.update(superDomain);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 初始化新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<DomainVO> cloudContext) throws Exception {
        DomainVO domainVO = null;
        DomainEntity loginedDomain = domainDAO.get(cloudContext.getLoginedUser().getDomainID());
        if (cloudContext.getBooleanParam("setUsersFlag") || cloudContext.getBooleanParam("guaidFlag")) {
            Long id = cloudContext.getVo().getId();
            UserEntity containUserEntity = null;
            if (id != null && id != 0) {//
                DomainEntity domainEntity = domainDAO.get(id);
                containUserEntity = domainEntity.getUser();
            }
            List<UserEntity> userEntitys = userDAO.queryNoDelete();
            List<UserVO> userVOs = new ArrayList<UserVO>();
            UserVO userVO = null;
            for (UserEntity userEntity : userEntitys) {
                userVO = new UserVO();
                BeanUtils.copyProperties(userEntity, userVO);
                if (containUserEntity != null && containUserEntity.equals(userEntity)) {
                    userVO.setDomainId(id);
                }
                userVOs.add(userVO);
            }
            cloudContext.addParam("users", userVOs);
            if (cloudContext.getBooleanParam("setUsersFlag")) {//设置用户,如果是向导继续
                return;
            }
        }

        //初始化域列表
        List<DomainEntity> domains = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        for (DomainEntity domainEntity : domains) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            domainVO.setAvailableStorageCapacity(ProjectUtil.kByteToGiga(domainVO.getAvailableStorageCapacity())
                            .longValue());
            domainVO.setStorageCapacity(ProjectUtil.kByteToGiga(domainVO.getStorageCapacity()).longValue());
            domainVOs.add(domainVO);
        }
        String domainsZtreeStr = ProjectUtil.generateDomainForZtree(domainVOs);
        cloudContext.addParam("domains", domainsZtreeStr);
        cloudContext.getVo().setCode(cloudContext.getLoginedUser().getDomainCode());

        //如果是修改，就加载属性
        String superDomainCode = loginedDomain.getCode();//先等于当前域的code，如果是修改则再更改
        if (cloudContext.getBooleanParam("updateFlag")) {
            DomainEntity currentDomain = domainDAO.get(cloudContext.getVo().getId());
            if (currentDomain == null) {
                cloudContext.addErrorMsg("域不存在！");
                return;
            }
            if (!Constant.ROOT_DOMAIN_CODE.equals(currentDomain.getCode())) {
                superDomainCode = currentDomain.getCode().substring(0, currentDomain.getCode().length() - 2);
                DomainEntity superDomainEntity = domainDAO.findByCode(superDomainCode);
                cloudContext.addParam("superDomain", superDomainEntity.getName());
                cloudContext.addParam("rootDomain", !domains.contains(superDomainEntity));
            } else {
                cloudContext.addParam("superDomain", "此域为最高级");
                cloudContext.addParam("rootDomain", true);
            }
            domainVO = new DomainVO();
            BeanUtils.copyProperties(currentDomain, domainVO);
            domainVO.setAvailableStorageCapacity(ProjectUtil.kByteToGiga(domainVO.getAvailableStorageCapacity())
                            .longValue());
            domainVO.setStorageCapacity(ProjectUtil.kByteToGiga(domainVO.getStorageCapacity()).longValue());
            cloudContext.addParam("dataVo", domainVO);
        }
    }

    /**
     * 设置用户
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void updateDomainUsers(CloudContext<DomainVO> cloudContext) throws Exception {
        Long id = cloudContext.getLongParam("userId");
        if (id != null) {
            UserEntity oldManager = userDAO.findByDomainId(cloudContext.getVo().getId());
            UserEntity newManager = userDAO.get(id);
            DomainEntity domain = domainDAO.get(cloudContext.getVo().getId());
            domainDAO.addDomainBidUser(cloudContext.getVo().getId(), id);
            if (oldManager != null && oldManager.equals(newManager)) {
                cloudContext.addSuccessMsg("设置成功!");
                return;
            }
            if (oldManager != null) {
                MailUtil.sendMail("取消管理员", String.format("【%1$s】取消了您的域【%2$s】管理员身份", cloudContext.getLoginedUser()
                                .getRealname(), domain.getName()), oldManager.getEmail());
            }
            MailUtil.sendMail("设置管理员", String.format("【%1$s】设置您为域【%2$s】的管理员", cloudContext.getLoginedUser()
                            .getRealname(), domain.getName()), newManager.getEmail());
        } else {
            cloudContext.addErrorMsg("用户id为空");
        }
        cloudContext.addSuccessMsg("设置成功!");
    }

    /**
     * 选择上级时，查找域下的资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             sql异常
     */
    public void queryDomainPoolAndStorageCapacity(CloudContext<DomainVO> cloudContext) throws Exception {
        String superDomainCode = cloudContext.getStringParam("superDomainCode");
        DomainEntity superDomain = domainDAO.findByCode(superDomainCode);
        if (superDomain == null) {
            cloudContext.addErrorMsg("上级域不存在");
            return;
        }
//        initPool(superDomainCode, cloudContext);
        BeanUtils.copyProperties(superDomain, cloudContext.getVo());
        cloudContext.getVo().setAvailableStorageCapacity(
                        ProjectUtil.kByteToGiga(cloudContext.getVo().getAvailableStorageCapacity()).longValue());
        cloudContext.getVo().setStorageCapacity(
                        ProjectUtil.kByteToGiga(cloudContext.getVo().getStorageCapacity()).longValue());
    }

    /**
     * 自定义资源池(向导用）
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void addPool(CloudContext<DomainVO> cloudContext) throws Exception {
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
        //获取资源
        queryResource(cloudContext);

        //初始化域列表
        List<DomainEntity> domainEntitys = domainDAO.list();
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity domainEntity : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            domainVOs.add(domainVO);
        }
        String domainsZtreeStr = ProjectUtil.generateDomainForZtree(domainVOs);
        cloudContext.addParam("domains", domainsZtreeStr);
    }

    /**
     * 查询资源。
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryResource(CloudContext<DomainVO> cloudContext) throws SQLException {
        List<ComputeResourceVO> computeResourceVOs = new ArrayList<ComputeResourceVO>();
        List<Object[]> objses = computeResourceDAO.queryNoPage(cloudContext.getStringParam("qResourceByName"),
                        cloudContext.getLongParam("qResourceByRoom"), cloudContext.getLongParam("qResourceByRack"));
        ComputeResourceVO computeResourceVO = null;
        for (Object[] objs : objses) {
            ComputeResourceEntity computeResourceEntity = (ComputeResourceEntity) objs[0];
            computeResourceVO = new ComputeResourceVO();
            BeanUtils.copyProperties(computeResourceEntity, computeResourceVO);
            ComputeResourcePoolEntity computeResourcePoolEntity = (ComputeResourcePoolEntity) objs[1];
            if (computeResourcePoolEntity != null) {//已经配置的资源，在配置其他资源池时不显示
                continue;
            }
            computeResourceVOs.add(computeResourceVO);
        }
        cloudContext.addParam("computeResources", computeResourceVOs);
    }

    /**
     * 根据上级域id生成当前域的Code
     * 
     * @param superDomainId
     * @return
     * @throws SQLException
     *             sqlException
     */
    private String createCode(DomainEntity superDomainEntity, CloudContext<DomainVO> cloudContext) throws SQLException {
        //验证
        DomainEntity domainEntity = domainDAO.findBySuperCodeAndName(superDomainEntity.getCode(), cloudContext.getVo()
                        .getName());
        if (domainEntity != null) {
            cloudContext.addErrorMsg(String.format("同级域中【%1$s】已经存在", cloudContext.getVo().getName()));
            return null;
        }
        String superDomainCode = superDomainEntity.getCode();
        //拼code
        String code = null;
        List<DomainEntity> childrenDomain = domainDAO.queryChildrenDomainByCode(superDomainCode);
        if (childrenDomain.size() == 0) {
            code = superDomainCode + "00";
        } else {
            DomainEntity lastDomain = childrenDomain.get(childrenDomain.size() - 1);
            String lastChildDomainCode = lastDomain.getCode();
            int currentLevelDomainCount = Integer.parseInt(lastChildDomainCode
                            .substring(lastChildDomainCode.length() - 2));
            if (currentLevelDomainCount >= 100) {
                cloudContext.addErrorMsg("当前级别域已满，请重新选择！");
                return null;
            }
            DecimalFormat dfmt = new DecimalFormat("00");
            code = superDomainCode + dfmt.format(currentLevelDomainCount + 1);
        }
        return code;
    }

    /**
     * 初始化资源池和存储
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void generatPools(String superDomainCode, CloudContext<DomainVO> cloudContext) throws Exception {
        List<ComputeResourcePoolEntity> childPools = null;
        if (cloudContext.getBooleanParam("updateFlag")) {//如果是更新需要找出当过前域下的池勾选上
            childPools = domainDAO.get(cloudContext.getVo().getId()).getComputeResourcePools();
        }
        List<ComputeResourcePoolEntity> superPools = computeResourcePoolDAO.queryPoolByDomainCode(superDomainCode);
        List<ComputeResourcePoolVO> computeResourcePoolVOs = new ArrayList<ComputeResourcePoolVO>();
        ComputeResourcePoolVO computeResourcePoolVO = null;
        for (ComputeResourcePoolEntity computeResourcePoolEntity : superPools) {
            computeResourcePoolVO = new ComputeResourcePoolVO();
            BeanUtils.copyProperties(computeResourcePoolEntity, computeResourcePoolVO);
            //如果是修改则检测是否为当前域下的资源池
            if (cloudContext.getBooleanParam("updateFlag") && childPools.contains(computeResourcePoolEntity)) {
                computeResourcePoolVO.setDomainId(cloudContext.getVo().getId());
                computeResourcePoolVOs.add(computeResourcePoolVO);
            }
            if (!computeResourcePoolVOs.contains(computeResourcePoolVO)) {
                computeResourcePoolVOs.add(computeResourcePoolVO);
            }
        }
        cloudContext.addParam("computeResourcePools", computeResourcePoolVOs);
    }

    /**
     * 向导模块查询机柜
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryRackByRoom(CloudContext<DomainVO> cloudContext) throws Exception {
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
        cloudContext.addParam("cascadeData", machineRacks);
    }

    /**
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    @SuppressWarnings("unused")
    public void insert4Guaid(CloudContext<DomainVO> cloudContext) throws Exception {
        DomainEntity superDomainEntity = domainDAO.findByCode(cloudContext.getStringParam("superDomainCode"));
        DomainEntity domainEntity = new DomainEntity();
        if (!setSuperDomainAndCurrentDomain(superDomainEntity, domainEntity, cloudContext)) {
            return;
        }
        domainDAO.insert(domainEntity);
        domainDAO.update(superDomainEntity);
        //资源池
        String[] computeResourcePoolIdStrs = (String[]) cloudContext.getObjectParam("computeResourcePoolIds");
        if (computeResourcePoolIdStrs != null) {
            ComputeResourcePoolEntity pool = null;
            for (String idStr : computeResourcePoolIdStrs) {
                if (idStr.equals("-1")) {
                    pool = insertPool4Guaid(cloudContext);
                    if (pool == null) {//为空即创建失败，返回
                        return;
                    }
                    //绑定当前域名和所有上级域
                    computeResourcePoolDAO.insertPoolBidDomain(pool.getId(), domainEntity.getId());
                    String code = domainEntity.getCode();
                    List<DomainEntity> superDomains = new ArrayList<DomainEntity>();
                    getSuperDomains(code, superDomains);
                    for (DomainEntity domain : superDomains) {
                        computeResourcePoolDAO.insertPoolBidDomain(pool.getId(), domain.getId());
                    }
                } else {
                    Long id = Long.parseLong(idStr);
                    pool = computeResourcePoolDAO.get(Long.parseLong(idStr));
                    computeResourcePoolDAO.insertPoolBidDomain(pool.getId(), domainEntity.getId());
                }
                computeResourcePoolDAO.update(pool);
            }
        }
        MachineRackEntity machineRackEntity = null;

        //更新用户
        String[] idStrs = (String[]) cloudContext.getObjectParam("userIds");
        if (idStrs != null) {
            for (String idStr : idStrs) {
                domainDAO.addDomainBidUser(domainEntity.getId(), Long.parseLong(idStr));
            }
        }
        cloudContext.addSuccessMsg("创建成功");
    }

    /**
     * 拿到所有的上级域名
     * 
     * @param code
     * @param domains
     * @throws SQLException
     *             sql异常
     */
    private void getSuperDomains(String code, List<DomainEntity> domains) throws SQLException {
        code = code.substring(0, code.length() - 2);
        if (code.length() > 2) {
            domains.add(domainDAO.findByCode(code));
            getSuperDomains(code, domains);
        } else {
            domains.add(domainDAO.findByCode(code));
        }
    }

    /**
     * 向导添加资源池
     * 
     * @param cloudContext
     * @return
     * @throws Exception
     *             所有异常
     */
    private ComputeResourcePoolEntity insertPool4Guaid(CloudContext<DomainVO> cloudContext) throws Exception {
        ComputeResourcePoolEntity computeResourcePoolEntity = generatePool4Guaid(cloudContext);
        if (computeResourcePoolEntity == null) {//为空即创建失败，返回
            return null;
        }
        computeResourcePoolDAO.insert(computeResourcePoolEntity);
        //设置资源
        String[] resourceIds = (String[]) cloudContext.getObjectParam("resourceIds");
        if (resourceIds == null) {
            return computeResourcePoolEntity;
        }
        MachineRackEntity machineRackEntity = null;
        ComputeResourceEntity computeResourceEntity = null;
        for (String resourceIdStr : resourceIds) {
            if (resourceIdStr.indexOf(",") != -1) {
                if (machineRackEntity == null) {//如果有自定义资源则获取机房机柜
                    machineRackEntity = insertRoomAndRack4GuaidResource(cloudContext);
                }
                computeResourceEntity = generateComputeResource4Guaid(resourceIdStr, cloudContext);
                computeResourceEntity.setMachineRack(machineRackEntity);
                computeResourceEntity.setComputeResourcePool(computeResourcePoolEntity);
                computeResourceDAO.insert(computeResourceEntity);
            } else {
                computeResourceEntity = computeResourceDAO.get(Long.parseLong(resourceIdStr));
                computeResourceEntity.setComputeResourcePool(computeResourcePoolEntity);
                computeResourceDAO.update(computeResourceEntity);
            }
        }
        return computeResourcePoolEntity;
    }

    /**
     * 为向导自定义资源生成所需的机柜实体，并绑定相应机房关系，并插入数据库
     * 
     * @param cloudContext
     * @return
     * @throws Exception
     *             所有异常
     */
    private MachineRackEntity insertRoomAndRack4GuaidResource(CloudContext<DomainVO> cloudContext) throws Exception {
        //自定义资源所属机房机柜 判断是否是自定义的
        Long roomId = cloudContext.getLongParam("roomId4AddResource");
        MachineRoomEntity machineRoomEntity = null;
        if (roomId.equals(-1L)) {
            String roomName = cloudContext.getStringParam("addRoomName4GuaidPool");
            //判断是否是与添加存储的自定义机房同名，如果同名则视为同一个机房
            if (roomName.equals(cloudContext.getStringParam("addRoomName4GuaidStorage"))
                            && cloudContext.getObjectParam("room4AddStorage") != null) {
                machineRoomEntity = (MachineRoomEntity) cloudContext.getObjectParam("room4AddStorage");
            } else {
                machineRoomEntity = generateRoom4Guaid(roomName);
                if (machineRoomEntity == null) {
                    cloudContext.addErrorMsg("添加资源的自定义机房名称已经存在！");
                    return null;
                }
            }
        } else {
            machineRoomEntity = machineRoomDAO.get(roomId);
        }
        Long rackId = cloudContext.getLongParam("rackId4AddResource");
        MachineRackEntity machineRackEntity = null;
        if (rackId.equals(-1L)) {
            String rackName = cloudContext.getStringParam("addRackName4GuaidPool");
            //判断是否是与添加存储的自定义机柜同名，如果同名则视为同一个机柜
            if (rackName.equals(cloudContext.getStringParam("addRackName4GuaidStorage"))
                            && cloudContext.getObjectParam("rack4AddStorage") != null) {
                machineRackEntity = (MachineRackEntity) cloudContext.getObjectParam("rack4AddStorage");
            } else {
                machineRackEntity = generateRack4Guaid(rackName);
                machineRackEntity.setMachineRoom(machineRoomEntity);
                machineRackDAO.insert(machineRackEntity);
            }
        } else {
            machineRackEntity = machineRackDAO.get(rackId);
        }
        return machineRackEntity;
    }

    /**
     * 向导创建资源池
     * 
     * @param cloudContext
     * @return
     * @throws Exception
     *             所有异常
     */
    private ComputeResourcePoolEntity generatePool4Guaid(CloudContext<DomainVO> cloudContext) throws Exception {
        String poolName = cloudContext.getStringParam("addPoolName");
        //验证
        ComputeResourcePoolEntity computeResourcePoolEntity = computeResourcePoolDAO.getByName(poolName);
        if (computeResourcePoolEntity != null) {
            cloudContext.addErrorMsg(String.format("自定义资源池【%1$s】已经存在", cloudContext.getVo().getName()));
            return null;
        }

        computeResourcePoolEntity = new ComputeResourcePoolEntity();
        computeResourcePoolEntity.setName(poolName);
        computeResourcePoolEntity.setDesc(poolName);
        computeResourcePoolEntity.setAddTime(new Date());
        return computeResourcePoolEntity;
    }

    /**
     * 向导新建资源
     * 
     * @param dataStr
     * @return
     * @throws Exception
     *             所有异常
     */
    private ComputeResourceEntity generateComputeResource4Guaid(String dataStr, CloudContext<DomainVO> cloudContext)
                    throws Exception {
        String[] datas = dataStr.split(",");
        ComputeResourceEntity computeResourceEntity = computeResourceDAO.getByIp(datas[1]);
        if (computeResourceEntity != null) {
            cloudContext.addErrorMsg(String.format("此ip下已经存在计算节点【%1$s】", computeResourceEntity.getName()));
            return null;
        }
        if (!ProjectUtil.pingIp(datas[1])) {
            cloudContext.addErrorMsg(String.format("ip【%1$s】不存在主机，计算节点添加失败", datas[1]));
            return null;
        }
        computeResourceEntity = new ComputeResourceEntity();
        computeResourceEntity.setIp(datas[1]);
        computeResourceEntity.setName(datas[0]);
        computeResourceEntity.setDesc(datas[0]);
        computeResourceEntity.setWarn4Rack(null);
        computeResourceEntity.setIdentityName(ProjectUtil.createComputeResourceIdentityName());
        computeResourceEntity.setAddTime(new Date());

        /*
         * 调用core
         */
        // 查看是否存在计算机，如果存在获取主机的CPU和内存
        //TODO  ....
//        Set<String> ipSet = new HashSet<String>();
//        ipSet.add(computeResourceEntity.getIp());
//        NodeStat nodeStat = NodeStat.getNodeInfo(computeResourceEntity.getIp());
//        if (nodeStat == null) {
//            cloudContext.addErrorMsg(String.format("IP为【%1$s】的节点不存在!", computeResourceEntity.getIp()));
//            //TODO 如果此主机是否创建节点。。。
//            return null;
//        } else {
//            computeResourceEntity.setCpu(nodeStat.getCpus());
//            computeResourceEntity.setMemory(ProjectUtil.kByteToMega(nodeStat.getTotalMem()));
//        }
//
//        //设置虚拟网络
//        VirtualNetwork virtualNetWork = new VirtualNetwork();
//        List<NetWorkEntity> networks = netWorkDAO.list();
//        NetworkParameter networkParameter = null;
//        for (NetWorkEntity network : networks) {
//            networkParameter = new NetworkParameter(network.getName(), network.getCidr(), network.getStartIP(), network
//                            .getEndIP());
//            if (network.getType().equalsIgnoreCase("nat")) {
//                virtualNetWork.defineByNat(networkParameter, computeResourceEntity.getIp());
//            } else if (network.getType().equalsIgnoreCase("bridge")) {
//                virtualNetWork.defineByBridge(networkParameter, computeResourceEntity.getIp(), "eth0");
//            }
//        }
        return computeResourceEntity;
    }

    /**
     * 向导生成DomainEntity
     * 
     * @param cloudContext
     * @return
     * @throws Exception
     *             generateDomain
     */
    private Boolean setSuperDomainAndCurrentDomain(DomainEntity superDomainEntity, DomainEntity domainEntity,
                    CloudContext<DomainVO> cloudContext) throws Exception {
        if (superDomainEntity == null) {
            cloudContext.addErrorMsg("上级不存存在，请刷新后重试");
            return false;
        }
        //单位转换
        cloudContext.getVo().setStorageCapacity(ProjectUtil.gigaToKByte(cloudContext.getVo().getStorageCapacity()));
        cloudContext.getVo().setAvailableStorageCapacity(
                        ProjectUtil.gigaToKByte(cloudContext.getVo().getStorageCapacity()));
        //判断上级存储空间是否够用
        if (superDomainEntity.getAvailableStorageCapacity() < cloudContext.getVo().getStorageCapacity()) {
            cloudContext.addErrorMsg("上级存储空间不足，可用空间为：" + superDomainEntity.getAvailableStorageCapacity() + "G");
            return false;
        }
        //判断上级存备份储空间是否够用
        if (superDomainEntity.getAvailableBackupStorageCapacity() < cloudContext.getVo().getBackupStorageCapacity()) {
            cloudContext.addErrorMsg("上级备份存储空间不足，可用空间为：" + superDomainEntity.getAvailableBackupStorageCapacity() + "G");
            return false;
        }
        //判断上级cpu是否够用
        if (superDomainEntity.getCpuAvailableNum() < cloudContext.getVo().getCpuTotalNum()) {
            cloudContext.addErrorMsg("上级cpu可用数不足，可用cpu核数为：" + superDomainEntity.getCpuAvailableNum());
            return false;
        }
        //判断上级内存是否够用
        if (superDomainEntity.getMemoryAvailableCapacity() < cloudContext.getVo().getMemoryCapacity()) {
            cloudContext.addErrorMsg("上级内存空间不足，可用内存为：" + superDomainEntity.getMemoryAvailableCapacity());
            return false;
        }
        String code = createCode(superDomainEntity, cloudContext);
        if (code == null) {//创建出错，返回
            return false;
        }
        cloudContext.getVo().setCode(code);
        if (cloudContext.getVo().getName() != null) {//名字为空表示修改
            BeanUtils.copyProperties(cloudContext.getVo(), domainEntity);
            domainEntity.setAddTime(new Date());
        }
        domainEntity.setStorageCapacity(cloudContext.getVo().getStorageCapacity());
        domainEntity.setAvailableStorageCapacity(domainEntity.getStorageCapacity());
        domainEntity.setBackupStorageCapacity(cloudContext.getVo().getBackupStorageCapacity());
        domainEntity.setAvailableBackupStorageCapacity(cloudContext.getVo().getBackupStorageCapacity());
        domainEntity.setCpuTotalNum(cloudContext.getVo().getCpuTotalNum());
        domainEntity.setCpuAvailableNum(domainEntity.getCpuTotalNum());
        domainEntity.setMemoryCapacity(cloudContext.getVo().getMemoryCapacity());
        domainEntity.setMemoryAvailableCapacity(domainEntity.getMemoryCapacity());
        domainDAO.insert(domainEntity);
        //更新上级域的空间
        superDomainEntity.setAvailableStorageCapacity(superDomainEntity.getAvailableStorageCapacity()
                        - domainEntity.getStorageCapacity());
        superDomainEntity.setAvailableBackupStorageCapacity(superDomainEntity.getAvailableBackupStorageCapacity()
                        - domainEntity.getBackupStorageCapacity());
        superDomainEntity.setCpuAvailableNum(superDomainEntity.getCpuAvailableNum() - domainEntity.getCpuTotalNum());
        superDomainEntity.setMemoryAvailableCapacity(superDomainEntity.getMemoryAvailableCapacity()
                        - domainEntity.getMemoryCapacity());
        return true;
    }

    /**
     * 向导新建机房
     * 
     * @param cloudContext
     * @return
     * @throws SQLException
     *             dql异常
     */
    private MachineRoomEntity generateRoom4Guaid(String name) throws SQLException {
        MachineRoomEntity machineRoomEntity = machineRoomDAO.getByName(name);
        if (machineRoomEntity != null) {//如果已经存在，则返回空
            return null;
        }
        machineRoomEntity = new MachineRoomEntity();
        machineRoomEntity.setName(name);
        machineRoomEntity.setDesc(name);//默认描述和名称一样
        machineRoomEntity.setAddTime(new Date());
        machineRoomEntity.setIdentityName(ProjectUtil.createMachineRoomIdentityName());
        machineRoomDAO.insert(machineRoomEntity);
        return machineRoomEntity;
    }

    /**
     * 向导新建机柜
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    private MachineRackEntity generateRack4Guaid(String name) throws SQLException {
        MachineRackEntity machineRackEntity = new MachineRackEntity();
        machineRackEntity.setName(name);
        machineRackEntity.setDesc(name);//默认描述和名称一样
        machineRackEntity.setAddTime(new Date());
        machineRackEntity.setWarn4Room(false);//默认不自动报警
        machineRackEntity.setIdentityName(ProjectUtil.createMachineRackIdentityName());
        return machineRackEntity;
    }

    /**
     * 设置资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void updatePools(CloudContext<DomainVO> cloudContext) throws Exception {
        String prevPoolIdsStr = cloudContext.getStringParam("prevPoolIds");
        String[] poolIds = (String[]) cloudContext.getObjectParam("poolIds");
        Long domainId = cloudContext.getVo().getId();
        DomainEntity domain = domainDAO.get(domainId);
        if (domain == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }
        List<String> prevPoolIds = new ArrayList<String>();
        if (!StringUtil.isBlank(prevPoolIdsStr)) {
            for (String idStr : prevPoolIdsStr.split(",")) {
                prevPoolIds.add(idStr);
            }
        }
        if (poolIds != null) {
            for (String idStr : poolIds) {
                //如果以前有的则从Pools去除，最后剩下的就是要删除的，以前没有包含的池，则是新增的，
                if (prevPoolIds.contains(idStr)) {
                    prevPoolIds.remove(idStr);
                } else {
                    computeResourcePoolDAO.insertPoolBidDomain(Long.parseLong(idStr), domainId);
                }
            }
        }
        StringBuilder poolsHasVm = new StringBuilder();
        if (prevPoolIds.size() > 0) {//删除剩下的pool
            Integer vmCount = null;
            Long poolId = null;
            ComputeResourcePoolEntity pool = null;
            for (String idStr : prevPoolIds) {
                poolId = Long.parseLong(idStr);
                pool = computeResourcePoolDAO.get(poolId);
                vmCount = virtualMachineDAO.getvmCountByPoolIdAndDomainId(poolId, domainId);
                if (vmCount > 0) {
                    poolsHasVm.append(pool.getName()).append(",");
                    continue;
                }
                //清除后代域与池的关系
                List<DomainEntity> descendantDomains = domainDAO.queryDescendantDomainByCode(domain.getCode());
                for (DomainEntity e : descendantDomains) {
                    domainDAO.clearPoolWithDomain(poolId, e.getId());
                }
            }
        }
        if (poolsHasVm.length() > 0) {
            cloudContext.addWarnMsg(poolsHasVm.replace(poolsHasVm.length() - 1, poolsHasVm.length(), " ").append(
                            "存在虚拟机,不能删除!").toString());
        } else {
            cloudContext.addSuccessMsg("设置成功");
        }
    }

    /**
     * 初始化资源池
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initPools(CloudContext<DomainVO> cloudContext) throws Exception {
        String code = cloudContext.getVo().getCode();
        DomainEntity domain = domainDAO.findByCode(code);
        Long id = cloudContext.getVo().getId();
        DomainEntity superDomain = domainDAO.findByCode(code.substring(0, code.length() - 2));
        if (superDomain == null) {
            cloudContext.addErrorMsg("上级不存在，请刷新后重试！");
            return;
        }
        List<ComputeResourcePoolEntity> childPools = computeResourcePoolDAO.queryPoolByDomainCode(code);
        List<ComputeResourcePoolEntity> pools = computeResourcePoolDAO.queryPoolWithVmCountByDomainCode(superDomain
                        .getCode());
        List<ComputeResourcePoolVO> computeResourcePoolVOs = new ArrayList<ComputeResourcePoolVO>();
        ComputeResourcePoolVO computeResourcePoolVO = null;
        Integer vmCount = null;
        for (ComputeResourcePoolEntity pool : pools) {
            computeResourcePoolVO = new ComputeResourcePoolVO();
            BeanUtils.copyProperties(pool, computeResourcePoolVO);
            if (computeResourcePoolVOs.contains(computeResourcePoolVO)) {
                continue;
            }
            vmCount = virtualMachineDAO.getvmCountByPoolIdAndDomainId(pool.getId(), domain.getId());
            computeResourcePoolVO.setVmNum(vmCount);
            //如果是修改则检测是否为当前域下的资源池
            if (childPools.contains(pool)) {
                computeResourcePoolVO.setDomainId(id);
            }
            queryCrpResource(computeResourcePoolVO);
            computeResourcePoolVO.setTotalVirtualCpu((int) (computeResourcePoolVO.getTotalCpu() * computeResourcePoolVO
                            .getCpuRate()));
            computeResourcePoolVO
                            .setTotalVirtualMemory((int) (computeResourcePoolVO.getTotalMemory() * computeResourcePoolVO
                                            .getMemoryRate()));
            computeResourcePoolVOs.add(computeResourcePoolVO);
        }
        DomainVO domainVO = new DomainVO();
        BeanUtils.copyProperties(domain, domainVO);
        cloudContext.addParam("domain", domainVO);
        cloudContext.addParam("computeResourcePools", computeResourcePoolVOs);
    }

    /**
     * 查询资源池的资源情况
     * 
     * @param computeResourcePoolVO
     * @throws SQLException
     *             sql异常
     */
    private void queryCrpResource(ComputeResourcePoolVO computeResourcePoolVO) throws SQLException {
        Object[] crObjs = computeResourcePoolDAO.sumComputeResourceWithCpuAndMemByPoolId(computeResourcePoolVO.getId());
        computeResourcePoolVO.setComputeResourceNum(crObjs[0] == null ? 0 : Integer.valueOf(crObjs[0].toString()));
        computeResourcePoolVO.setTotalCpu(crObjs[1] == null ? 0 : Integer.valueOf(crObjs[1].toString()));
        computeResourcePoolVO.setAvailableCpu(crObjs[2] == null ? 0 : Integer.valueOf(crObjs[2].toString()));
        computeResourcePoolVO.setTotalMemory(crObjs[3] == null ? 0 : Integer.valueOf(crObjs[3].toString()));
        computeResourcePoolVO.setAvailableMemory(crObjs[4] == null ? 0 : Integer.valueOf(crObjs[4].toString()));
    }
}
