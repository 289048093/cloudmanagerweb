/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.impl.StdScheduler;

import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.virtualization.VmUtils;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.FaultTolerantEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;

/**
 * spring加载之后的监听器
 * 
 * @author CloudKing
 * 
 */
public class PostSpringListener implements ServletContextListener {

    /**
     * 上下文销毁
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * 上下文初始化
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            ProjectUtil.initSpringContext(event.getServletContext());
            AuthorityInterceptor.initRightsUrl();
            checkProperty();
            triggerQuartz();
            checkFaultTolerant();
            /**
             * 启动CloudKing队列
             */
            CloudKingQueue.start();
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 检查系统属性
     * 
     * @throws Exception
     *             所有异常
     */
    private void checkProperty() throws Exception {
        String backUpDays = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_MYSQL_BACKUP_DAYS_KEY);
        Map<Boolean, String> resultMap = ProjectUtil.getXmlProperty("applicationContext.xml",
                        "/*[name()='beans']/*[name()='bean']/*[@name='repeatInterval']/*[name()='value']");
        if (resultMap.keySet().iterator().next()) {
            String xmlValue = resultMap.values().iterator().next();
            if (!xmlValue.equals(backUpDays)) {
                ProjectUtil.updateMysqlBackUpDaysInXml(backUpDays);
            }
        }
    }

    /**
     * 触发quartz
     * 
     * @throws Exception
     *             所有异常
     */
    private void triggerQuartz() throws Exception {
        StdScheduler stdScheduler = (StdScheduler) ProjectUtil.getSpringBean("schedulerFactory");
        stdScheduler.start();
    }

    /**
     * 容错检查
     * 
     * @throws Exception
     *             所有异常
     */
    private void checkFaultTolerant() throws Exception {
        EntityManager em = ProjectUtil.getEntityManager();
        em.getTransaction().begin();
        try {
            List<?> ftEntitys = em.createQuery("from FaultTolerantEntity").getResultList();
            if (ftEntitys.size() == 0) {
                return;
            }
            for (Object obj : ftEntitys) {
                FaultTolerantEntity ft = (FaultTolerantEntity) obj;
                if (ft.getType().equals(Constant.VM_FAULT_TOLERANT)) {
                    checkVMFaultTonlerant(em, ft);
                }
                if (ft.getType().equals(Constant.TEMPLATE_FAULT_TOTERANT)) {
                    checkTemplateFaultTonlerant(em, ft);
                }
                em.remove(ft);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            LogUtil.error(e);
        } finally {
            em.close();
        }
    }

    /**
     * 检查模版状态
     * 
     * @param em
     * @param ft
     */
    private void checkTemplateFaultTonlerant(EntityManager em, FaultTolerantEntity ft) {
        TemplateEntity templateEntity = em.find(TemplateEntity.class, ft.getRefid());
        if (templateEntity.getStatus().equals(Constant.TEMPLATE_STATUS_DOWNLOADING)) {
            File template = new File(ProjectUtil.getTemplateDir(), templateEntity.getFileName());
            template.deleteOnExit();
            em.remove(templateEntity);
        }
    }

    /**
     * 检查虚拟机状态并处理
     * 
     * @param em
     * @param ft
     * @throws SQLException
     *             sql异常
     * @throws VirtualizationException
     *             底层异常
     */
    private void checkVMFaultTonlerant(EntityManager em, FaultTolerantEntity ft) throws SQLException,
                    VirtualizationException {
        Long vmId = ft.getRefid();
        VirtualMachineEntity vmEntity = em.find(VirtualMachineEntity.class, vmId);
        if (vmEntity != null) {
            StorageResourceEntity storageTmp = null;
            Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            //供后面做资源数据操作
            Map<StorageResourceEntity, Set<VolumnEntity>> storageVolumnsMap = new HashMap<StorageResourceEntity, Set<VolumnEntity>>();
            List<VolumnEntity> volumns = vmEntity.getVolumns();
            Set<VolumnEntity> volumnTmps = null;
            Set<String> volumnNames = null;
            for (VolumnEntity v : volumns) {
                storageTmp = v.getStorageResource();
                if (storageVolumnsMap.get(storageTmp) != null) {
                    map.get(storageTmp.getPoolName()).add(v.getName());
                    storageVolumnsMap.get(storageTmp).add(v);
                } else {
                    volumnNames = new HashSet<String>();
                    volumnTmps = new HashSet<VolumnEntity>();
                    volumnNames.add(v.getName());
                    volumnTmps.add(v);
                    map.put(storageTmp.getPoolName(), volumnNames);
                    storageVolumnsMap.put(storageTmp, volumnTmps);
                }
            }
            //如果没删除卷则说明虚拟机存在  调用core
            Boolean exist = false;
            try {
                exist = !VmUtils.deleteUnavailableVolume(vmEntity.getVmName(), vmEntity.getComputeResource().getIp(),
                                map);
            } catch (VirtualizationException e) {
                LogUtil.warn(e);
            } catch (Exception e) {
                LogUtil.warn(e);
            }
            if (exist) {
                vmEntity.setCreatedFlag(Constant.VM_CREATE_SUCCESS);
                em.merge(vmEntity);
            } else {
                //更存储
                Long sizeTmp = null;
                Long totalSize = 0L;
                for (Entry<StorageResourceEntity, Set<VolumnEntity>> e : storageVolumnsMap.entrySet()) {
                    storageTmp = e.getKey();
                    sizeTmp = getTotalVolumnSize(e.getValue());
                    if (sizeTmp == null) {
                        continue;
                    }
                    totalSize += sizeTmp;
                    storageTmp.setAvailableCapacity(storageTmp.getAvailableCapacity() + sizeTmp);
                    em.merge(storageTmp);
                }
                MachineTypeEntity machineType = vmEntity.getMachineType();
                //更新计算节点
                ComputeResourceEntity compute = vmEntity.getComputeResource();
                compute.setCpuAvailable(compute.getCpuAvailable() + machineType.getCpu());
                compute.setMemoryAvailable(compute.getMemoryAvailable() + machineType.getMemory());
                em.merge(compute);
                //更新域资源情况
                DomainEntity domain = vmEntity.getDomain();
                domain.setCpuAvailableNum(domain.getCpuAvailableNum() + machineType.getCpu());
                domain.setMemoryAvailableCapacity(domain.getMemoryAvailableCapacity() + machineType.getMemory());
                domain.setAvailableStorageCapacity(domain.getAvailableStorageCapacity() + totalSize);
                em.merge(domain);
                em.remove(vmEntity);
            }
        }
    }

    /**
     * 计算卷集合的总大小
     * 
     * @param vols
     * @return
     */
    private Long getTotalVolumnSize(Set<VolumnEntity> vols) {
        if (vols.size() == 0) {
            return null;
        }
        Long size = 0L;
        for (VolumnEntity v : vols) {
            size += v.getSize();
        }
        return size;
    }
}
