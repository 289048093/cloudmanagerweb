/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 26, 2012  4:48:31 PM
 */
package com.cloudking.cloudmanagerweb.service.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanager.core.util.CapacityUnit;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.ComputeResourcePoolDAO;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.ResourceOrderDAO;
import com.cloudking.cloudmanagerweb.dao.StorageResourceDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourcePoolEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;
import com.cloudking.cloudmanagerweb.vo.DashboardVO;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.StorageResourceVO;
import com.cloudking.cloudmanagerweb.vo.WorkOrderVO;

/**
 * @author CloudKing
 */
@Service("dashboardService")
public class DashboardService extends BaseService {

    /**
     * resourceOrderDAO
     */
    @Resource
    protected ResourceOrderDAO resourceOrderDAO;

    /**
     * workOrderDAO
     */
    @Resource
    protected WorkOrderDAO workOrderDAO;

    /**
     * storageResourceDAO
     */
    @Resource
    private StorageResourceDAO storageResourceDAO;

    /**
     * ComputeResourceDAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;

    /**
     * computeResourcePoolDAO
     */
    @Resource
    private ComputeResourcePoolDAO computeResourcePoolDAO;

    /**
     * 订单DAO
     */
    @Resource
    private transient PortalOrderDAO portalOrderDAO;

    /**
     * domainDAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * 页面首次渲染时执行的查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<DashboardVO> cloudContext) throws Exception {
        querySystemChartsInLoginDomain(cloudContext);
        queryHandlingWorkOrderInLoginDomain(cloudContext);
        //管理员界面图表
        if (cloudContext.getLoginedUser().getUsername().equals(Constant.ADMINISTRATOR)) {
            queryComputePoolChartInFirstRender(cloudContext);
            queryStoragePoolChartInFirstRender(cloudContext);
        }
    }

    /**
     * 查询登录域处理中的工单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    private void queryHandlingWorkOrderInLoginDomain(CloudContext<DashboardVO> cloudContext) throws Exception {
        //搜索条件
        Long domainId = cloudContext.getLoginedUser().getDomainID();
        queryHandlingWorkOrderInSpecifyDomain(domainId, cloudContext);
    }

    /**
     * 分页查询处理中的工单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public void queryHandlingWorkOrderOnPageChange(CloudContext<DashboardVO> cloudContext) throws Exception {
        //Json分页查询
        Integer targetPage = cloudContext.getIntegerParam("targetPage");
        if (targetPage != null) {
            cloudContext.getPageInfo().setNowPage(targetPage);
        }
        Long domainId = cloudContext.getLongParam("domainId");
        if (domainId == null) {
            //查询所有域
            queryHandlingWorkOrderInAllDomain(cloudContext);
        } else {
            //查询指定域
            queryHandlingWorkOrderInSpecifyDomain(domainId, cloudContext);
        }
    }

    /**
     * 域切换查询工单
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    public void queryHandlingWorkOrderOnDomainChange(CloudContext<DashboardVO> cloudContext) throws Exception {
        //Json分页查询
        Integer targetPage = cloudContext.getIntegerParam("targetPage");
        if (targetPage != null) {
            cloudContext.getPageInfo().setNowPage(targetPage);
        }
        Long domainId = cloudContext.getLongParam("domainId");
        if (domainId == null) {
            //查询所有域
            queryHandlingWorkOrderInAllDomain(cloudContext);
        } else {
            //查询指定域
            queryHandlingWorkOrderInSpecifyDomain(domainId, cloudContext);
        }
    }

    /**
     * 指定域下的工单通用类
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    @SuppressWarnings("unused")
    private void queryHandlingWorkOrderInAllDomain(CloudContext<DashboardVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        cloudContext.getPageInfo().setEachPageData(Constant.DASHBOARDDATACOUNT);
        //总数据数
        cloudContext.getPageInfo().setDataCount(workOrderDAO.getHandlingWorkOrderInAllDoaminsCount(domainIds));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            //设置分页信息
            int eachPageData = cloudContext.getPageInfo().getEachPageData();
            int totalRecord = cloudContext.getPageInfo().getDataCount();
            cloudContext.getPageInfo().setPageCount(
                            totalRecord % eachPageData == 0 ? totalRecord / eachPageData : totalRecord / eachPageData
                                            + 1);

            List<Object[]> objects = workOrderDAO.getHandlingWorkOrderInAllDomains(domainIds, cloudContext
                            .getPageInfo());

            WorkOrderVO workOrderVO = null;
            WorkOrderEntity workOrderEntity = null;
            int attachmentCount = 0;
            for (Object[] object : objects) {
                workOrderVO = new WorkOrderVO();
                workOrderEntity = (WorkOrderEntity) object[0];
                attachmentCount = Integer.parseInt(object[1].toString());
                BeanUtils.copyProperties(workOrderEntity, workOrderVO);
                workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
                workOrderVO.setCategoryId(workOrderEntity.getCategory().getId());
                if (workOrderEntity.getSendDomain() == null) {
                    workOrderVO.setSendDomainName("系统");
                } else {
                    workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
                }
                workOrderVO.setAttachmentCount(attachmentCount);
                workOrderVOs.add(workOrderVO);
            }
        }
        cloudContext.addParam("workOrders", workOrderVOs);
        //设置分页，避免表格冲突
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(cloudContext.getPageInfo(), pageInfo);
        cloudContext.addParam("woPageInfo", pageInfo);
    }

    /**
     * 指定域下的资源订单通用类
     * 
     * @param context
     * @throws Exception
     *             所有异常
     */
    @SuppressWarnings("unused")
    private void queryHandlingWorkOrderInSpecifyDomain(Long domainId, CloudContext<DashboardVO> cloudContext)
                    throws Exception {

        cloudContext.getPageInfo().setEachPageData(Constant.DASHBOARDDATACOUNT);
        //总数据数
        cloudContext.getPageInfo().setDataCount(workOrderDAO.getHandlingWorkOrderInSpecifyDoaminCount(domainId));
        List<WorkOrderVO> workOrderVOs = new ArrayList<WorkOrderVO>();
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            //设置分页信息
            int eachPageData = cloudContext.getPageInfo().getEachPageData();
            int totalRecord = cloudContext.getPageInfo().getDataCount();
            cloudContext.getPageInfo().setPageCount(
                            totalRecord % eachPageData == 0 ? totalRecord / eachPageData : totalRecord / eachPageData
                                            + 1);

            List<Object[]> objects = workOrderDAO.getHandlingWorkOrderInSpecifyDomain(domainId, cloudContext
                            .getPageInfo());
            WorkOrderVO workOrderVO = null;
            WorkOrderEntity workOrderEntity = null;
            int attachmentCount = 0;
            for (Object[] object : objects) {
                workOrderVO = new WorkOrderVO();
                workOrderEntity = (WorkOrderEntity) object[0];
                attachmentCount = Integer.parseInt(object[1].toString());
                BeanUtils.copyProperties(workOrderEntity, workOrderVO);
                workOrderVO.setCategoryName(workOrderEntity.getCategory().getName());
                workOrderVO.setCategoryId(workOrderEntity.getCategory().getId());
                if (workOrderEntity.getSendDomain() == null) {
                    workOrderVO.setSendDomainName("系统");
                } else {
                    workOrderVO.setSendDomainName(workOrderEntity.getSendDomain().getName());
                }
                workOrderVO.setAttachmentCount(attachmentCount);
                workOrderVOs.add(workOrderVO);
            }
        }
        cloudContext.addParam("workOrders", workOrderVOs);
        //设置分页，避免表格冲突
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(cloudContext.getPageInfo(), pageInfo);
        cloudContext.addParam("woPageInfo", pageInfo);
    }

    /**
     * 用户所有域计算节点池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void queryComputePoolChartInFirstRender(CloudContext<DashboardVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        int poolCpuTotalNum = 0;
        int poolCpuAvailableNum = 0;
        int poolCpuUsedNum = 0;

        int poolMemoryCapacity = 0;
        int poolMemoryAvailableCapacity = 0;
        int poolMemoryUsedCapacity = 0;

        //查询计算节点池
        List<ComputeResourcePoolEntity> computeResourcePoolEntitys = computeResourcePoolDAO
                        .queryResourcePoolInSpecifyDomain(domainIds);
        List<ComputeResourcePoolVO> computeResourcePoolVOs = new ArrayList<ComputeResourcePoolVO>();
        ComputeResourcePoolVO computeResourcePoolVO = null;
        for (ComputeResourcePoolEntity computeResourcePoolEntity : computeResourcePoolEntitys) {
            computeResourcePoolVO = new ComputeResourcePoolVO();
            BeanUtils.copyProperties(computeResourcePoolEntity, computeResourcePoolVO);
            computeResourcePoolVOs.add(computeResourcePoolVO);
        }

        //统计节点池资源数据
        Object[] objects = computeResourcePoolDAO.queryComputePoolChartInSpecifyDomains(domainIds);
        if (objects != null && objects.length > 0) {
            poolCpuTotalNum = objects[0] == null ? 0 : downCastDecimalToInt(objects[0].toString());
            poolCpuAvailableNum = objects[1] == null ? 0 : downCastDecimalToInt(objects[1].toString());
            poolCpuUsedNum = poolCpuTotalNum - poolCpuAvailableNum;

            poolMemoryCapacity = objects[2] == null ? 0 : downCastDecimalToInt(objects[2].toString());
            poolMemoryAvailableCapacity = objects[3] == null ? 0 : downCastDecimalToInt(objects[3].toString());
            poolMemoryUsedCapacity = poolMemoryCapacity - poolMemoryAvailableCapacity;
        }

        cloudContext.addParam("poolCpuTotalNum", poolCpuTotalNum);
        cloudContext.addParam("poolCpuAvailableNum", poolCpuAvailableNum);
        cloudContext.addParam("poolCpuUsedNum", poolCpuUsedNum);
        cloudContext.addParam("poolMemoryCapacity", poolMemoryCapacity);
        cloudContext.addParam("poolMemoryAvailableCapacity", poolMemoryAvailableCapacity);
        cloudContext.addParam("poolMemoryUsedCapacity", poolMemoryUsedCapacity);
        cloudContext.addParam("computeResourcePool", computeResourcePoolVOs);
    }

    /**
     * 查询计算节点池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryComputePoolChartOnPoolChange(CloudContext<DashboardVO> cloudContext) throws Exception {
        Long poolId = cloudContext.getLongParam("poolId");
        if (poolId == null) {
            queryComputePoolChartInAllDomains(cloudContext);
        } else {
            int poolCpuTotalNum = 0;
            int poolCpuAvailableNum = 0;
            int poolCpuUsedNum = 0;

            int poolMemoryCapacity = 0;
            int poolMemoryAvailableCapacity = 0;
            int poolMemoryUsedCapacity = 0;

            Object[] objects = computeResourcePoolDAO.queryComputePoolChartByPoolId(poolId);
            if (objects != null && objects.length > 0) {
                poolCpuTotalNum = objects[0] == null ? 0 : downCastDecimalToInt(objects[0].toString());
                poolCpuAvailableNum = objects[1] == null ? 0 : downCastDecimalToInt(objects[1].toString());
                poolCpuUsedNum = poolCpuTotalNum - poolCpuAvailableNum;

                poolMemoryCapacity = objects[2] == null ? 0 : downCastDecimalToInt(objects[2].toString());
                poolMemoryAvailableCapacity = objects[3] == null ? 0 : downCastDecimalToInt(objects[3].toString());
                poolMemoryUsedCapacity = poolMemoryCapacity - poolMemoryAvailableCapacity;
            }
            cloudContext.addParam("poolCpuTotalNum", poolCpuTotalNum);
            cloudContext.addParam("poolCpuAvailableNum", poolCpuAvailableNum);
            cloudContext.addParam("poolCpuUsedNum", poolCpuUsedNum);
            cloudContext.addParam("poolMemoryCapacity", poolMemoryCapacity);
            cloudContext.addParam("poolMemoryAvailableCapacity", poolMemoryAvailableCapacity);
            cloudContext.addParam("poolMemoryUsedCapacity", poolMemoryUsedCapacity);
        }
    }

    /**
     * 查询计算节点池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryComputePoolChartInAllDomains(CloudContext<DashboardVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        int poolCpuTotalNum = 0;
        int poolCpuAvailableNum = 0;
        int poolCpuUsedNum = 0;

        int poolMemoryCapacity = 0;
        int poolMemoryAvailableCapacity = 0;
        int poolMemoryUsedCapacity = 0;

        Object[] objects = computeResourcePoolDAO.queryComputePoolChartInSpecifyDomains(domainIds);
        if (objects != null && objects.length > 0) {
            poolCpuTotalNum = objects[0] == null ? 0 : downCastDecimalToInt(objects[0].toString());
            poolCpuAvailableNum = objects[1] == null ? 0 : downCastDecimalToInt(objects[1].toString());
            poolCpuUsedNum = poolCpuTotalNum - poolCpuAvailableNum;

            poolMemoryCapacity = objects[2] == null ? 0 : downCastDecimalToInt(objects[2].toString());
            poolMemoryAvailableCapacity = objects[3] == null ? 0 : downCastDecimalToInt(objects[3].toString());
            poolMemoryUsedCapacity = poolMemoryCapacity - poolMemoryAvailableCapacity;
        }
        cloudContext.addParam("poolCpuTotalNum", poolCpuTotalNum);
        cloudContext.addParam("poolCpuAvailableNum", poolCpuAvailableNum);
        cloudContext.addParam("poolCpuUsedNum", poolCpuUsedNum);
        cloudContext.addParam("poolMemoryCapacity", poolMemoryCapacity);
        cloudContext.addParam("poolMemoryAvailableCapacity", poolMemoryAvailableCapacity);
        cloudContext.addParam("poolMemoryUsedCapacity", poolMemoryUsedCapacity);
    }

    /**
     * 查询存储池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryStoragePoolChartOnPoolChange(CloudContext<DashboardVO> cloudContext) throws Exception {
        Long storageId = cloudContext.getLongParam("storageId");
        if (storageId == null) {
            queryStoragePoolChartInAllDomains(cloudContext);
        } else {
            Long poolStorageCapacity = 0L;
            Long poolUsedStorageCapacity = 0L;
            Long poolAvailableStorageCapacity = 0L;
            //统计节点池资源数据
            Object[] objects = storageResourceDAO.sumStorageResourceChartById(storageId);
            if (objects != null && objects.length > 0) {
                poolStorageCapacity = objects[0] == null ? 0L : Long.parseLong(objects[0].toString());
                poolAvailableStorageCapacity = objects[1] == null ? 0L : Long.parseLong(objects[1].toString());
                poolUsedStorageCapacity = poolStorageCapacity - poolAvailableStorageCapacity;
            }
            cloudContext.addParam("poolStorageCapacity", ProjectUtil.kByteToGiga(poolStorageCapacity));
            cloudContext
                            .addParam("poolAvailableStorageCapacity", ProjectUtil
                                            .kByteToGiga(poolAvailableStorageCapacity));
            cloudContext.addParam("poolUsedStorageCapacity", ProjectUtil.kByteToGiga(poolUsedStorageCapacity));
        }
    }

    /**
     * 查询用户所有域存储池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryStoragePoolChartInAllDomains(CloudContext<DashboardVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        //如果是查询所有存储节点池
        Long poolStorageCapacity = 0L;
        Long poolUsedStorageCapacity = 0L;
        Long poolAvailableStorageCapacity = 0L;
        //统计节点池资源数据
        Object[] objects = storageResourceDAO.sumStorageResourceChartInSpecifyDomains(domainIds);
        if (objects != null && objects.length > 0) {
            poolStorageCapacity = objects[0] == null ? 0L : Long.parseLong(objects[0].toString());
            poolAvailableStorageCapacity = objects[1] == null ? 0L : Long.parseLong(objects[1].toString());
            poolUsedStorageCapacity = poolStorageCapacity - poolAvailableStorageCapacity;
        }
        cloudContext.addParam("poolStorageCapacity", ProjectUtil.kByteToGiga(poolStorageCapacity));
        cloudContext.addParam("poolAvailableStorageCapacity", ProjectUtil.kByteToGiga(poolAvailableStorageCapacity));
        cloudContext.addParam("poolUsedStorageCapacity", ProjectUtil.kByteToGiga(poolUsedStorageCapacity));
    }

    /**
     * 获取当前用户所有域Ids
     */
    private String getDomainIdsFromLoginUser(CloudContext<DashboardVO> cloudContext) {
        String result = "";
        List<DomainVO> domains = cloudContext.getLoginedUser().getDomains();
        if (domains == null || domains.size() < 1) {
            cloudContext.addErrorMsg("登录超时");
        }
        StringBuilder domainIdsBuilder = new StringBuilder();
        for (DomainVO domain : domains) {
            domainIdsBuilder.append(domain.getId() + ",");
        }
        if (domainIdsBuilder.length() > 0) {
            domainIdsBuilder = domainIdsBuilder.delete(domainIdsBuilder.length() - 1, domainIdsBuilder.length());
            result = domainIdsBuilder.toString();
        }
        return result;
    }

    /**
     * 查询系统CPU，内存，存储
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void querySystemChartsInLoginDomain(CloudContext<DashboardVO> cloudContext) throws Exception {
        if (cloudContext.getLoginedUser() == null) {
            cloudContext.addErrorMsg("用户未登录");
            return;
        }
        Long domainId = cloudContext.getLoginedUser().getDomainID();

        DomainEntity domainEntity = domainDAO.get(domainId);
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }
        querySystemChartsInSpecifyDomain(domainEntity, cloudContext);
    }

    /**
     * 域切换查询系统图表
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void querySystemChartOnDomainChange(CloudContext<DashboardVO> cloudContext) throws Exception {
        Long domainId = cloudContext.getLongParam("domainId");
        if (domainId == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }
        DomainEntity domainEntity = domainDAO.get(domainId);
        if (domainEntity == null) {
            cloudContext.addErrorMsg("域不存在");
            return;
        }
        querySystemChartsInSpecifyDomain(domainEntity, cloudContext);
    }

    /**
     * 查询指定域系统资源图表数据
     * 
     * @param domainEntity
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void querySystemChartsInSpecifyDomain(DomainEntity domainEntity, CloudContext<DashboardVO> cloudContext)
                    throws Exception {
        //查找最高可申请的存储配额
        Long maxAvailableStorage = 0L;
        Storage storage = null;
        for (StorageResourceEntity storageResourceEntity : storageResourceDAO.queryStorageInSpecifyDomain(domainEntity
                        .getId())) {
            try {
                storage = Storage.getStorage(storageResourceEntity.getPoolName());
                storage.setUnit(CapacityUnit.GB);
                maxAvailableStorage = (storage.getAvailable() > maxAvailableStorage) ? storage.getAvailable() : maxAvailableStorage;
            } catch (Exception e) {
                maxAvailableStorage = 0L;
            }
        }

        ComputeResourceEntity computeResourceEntity = computeResourceDAO.getOptimalCpuCompute(domainEntity.getId());

        //cpu
        cloudContext.addParam("cpuTotalNum", domainEntity.getCpuTotalNum());
        cloudContext.addParam("cpuAvailableNum", domainEntity.getCpuAvailableNum());
        cloudContext.addParam("cpuUsedNum", domainEntity.getCpuTotalNum() - domainEntity.getCpuAvailableNum());
        if (computeResourceEntity != null) {
            cloudContext.addParam("maxAvailableCpuForApply", Math.min(Math.min(computeResourceEntity.getCpu(),
                            computeResourceEntity.getCpuAvailable()), domainEntity.getCpuAvailableNum()));
        } else {
            cloudContext.addParam("maxAvailableCpuForApply", 0);
        }

        //内存
        cloudContext.addParam("memoryCapacity", domainEntity.getMemoryCapacity());
        cloudContext.addParam("memoryUsedCapacity", domainEntity.getMemoryCapacity()
                        - domainEntity.getMemoryAvailableCapacity());
        cloudContext.addParam("memoryAvailableCapacity", domainEntity.getMemoryAvailableCapacity());
        computeResourceEntity = computeResourceDAO.getOptimalMemoryCompute(domainEntity.getId());
        if (computeResourceEntity != null) {
            cloudContext.addParam("maxAvailableMemoryForApply", Math.min(Math.min(computeResourceEntity.getMemory(),
                            computeResourceEntity.getMemoryAvailable()), domainEntity.getMemoryAvailableCapacity()));
        } else {
            cloudContext.addParam("maxAvailableMemoryForApply", 0);
        }

        //存储
        cloudContext.addParam("storageCapacity", ProjectUtil.kByteToGiga(domainEntity.getStorageCapacity()));
        cloudContext.addParam("usedStorageCapacity", ProjectUtil.kByteToGiga(domainEntity.getStorageCapacity()
                        - domainEntity.getAvailableStorageCapacity()));
        cloudContext.addParam("availableStorageCapacity",  ProjectUtil.kByteToGiga(domainEntity.getAvailableStorageCapacity()));
        StorageResourceEntity storageResourceEntity = storageResourceDAO.getMaxDiskStorage();
        if (storageResourceEntity != null) {
            cloudContext.addParam("maxAvailableStorageForApply",  ProjectUtil.kByteToGiga(Math.min(storageResourceEntity.getAvailableCapacity(),
                            domainEntity.getAvailableStorageCapacity())));
        } else {
            cloudContext.addParam("maxAvailableStorageForApply", 0);
        }
    }

    /**
     * 查询存储节点池图表数据
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void queryStoragePoolChartInFirstRender(CloudContext<DashboardVO> cloudContext) throws Exception {
        String domainIds = getDomainIdsFromLoginUser(cloudContext);
        if (StringUtil.isBlank(domainIds)) {
            return;
        }
        Long poolStorageCapacity = 0L;
        Long poolUsedStorageCapacity = 0L;
        Long poolAvailableStorageCapacity = 0L;
        //查询存储池
        List<StorageResourceEntity> storageResourceEntitys = storageResourceDAO
                        .queryStorageResourceInSpecifyDomains(domainIds);
        List<StorageResourceVO> storageResourceVOs = new ArrayList<StorageResourceVO>();
        StorageResourceVO storageResourceVO = null;
        for (StorageResourceEntity storageResourceEntity : storageResourceEntitys) {
            storageResourceVO = new StorageResourceVO();
            BeanUtils.copyProperties(storageResourceEntity, storageResourceVO);
            storageResourceVO.setCapacity(ProjectUtil.kByteToGiga(storageResourceVO.getCapacity()).longValue());
            storageResourceVO.setAvailableCapacity(ProjectUtil.kByteToGiga(storageResourceVO.getAvailableCapacity()).longValue());
            storageResourceVOs.add(storageResourceVO);
        }
        //统计节点池资源数据
        Object[] objects = storageResourceDAO.sumStorageResourceChartInSpecifyDomains(domainIds);
        if (objects != null && objects.length > 0) {
            poolStorageCapacity = objects[0] == null ? 0L : Long.parseLong(objects[0].toString());
            poolAvailableStorageCapacity = objects[1] == null ? 0L : Long.parseLong(objects[1].toString());
            poolUsedStorageCapacity = poolStorageCapacity - poolAvailableStorageCapacity;
        }
        cloudContext.addParam("poolStorageCapacity",  ProjectUtil.kByteToGiga(poolStorageCapacity));
        cloudContext.addParam("poolAvailableStorageCapacity",  ProjectUtil.kByteToGiga(poolAvailableStorageCapacity));
        cloudContext.addParam("poolUsedStorageCapacity",  ProjectUtil.kByteToGiga(poolUsedStorageCapacity));
        cloudContext.addParam("storagePool", storageResourceVOs);
    }

    /**
     * 将字符串类型的小数转为整型
     */
    @SuppressWarnings("unused")
    public static int downCastDecimalToInt(String str) {
        int value = 0;
        try {
            if (StringUtil.isBlank(str)) {
                return value;
            }
            if (str.indexOf(".") > 0) {
                return Integer.parseInt(str.substring(0, str.indexOf(".")));
            }
            return Integer.parseInt(str);
        } catch (Exception e) {
            LogUtil.error(e);
            return value;
        }
    }

}
