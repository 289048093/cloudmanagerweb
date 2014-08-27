/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 17, 2012  2:17:57 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.storage.Volume;
import com.cloudking.cloudmanager.core.virtualization.VirtualMachine;
import com.cloudking.cloudmanagerweb.CloudKingQueue;
import com.cloudking.cloudmanagerweb.CloudKingQueueData;
import com.cloudking.cloudmanagerweb.entity.BackupStorageEntity;
import com.cloudking.cloudmanagerweb.entity.ComputeResourceEntity;
import com.cloudking.cloudmanagerweb.entity.FaultTolerantEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.StorageResourceEntity;
import com.cloudking.cloudmanagerweb.entity.VMBackupEntity;
import com.cloudking.cloudmanagerweb.entity.VMSnapshotEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.VolumnEntity;

/**
 * 镜像工具类
 * 
 * @author CloudKing
 */
public final class VMUtil {
    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors
                    .newFixedThreadPool(10);

    /**
     * 默认构造方法
     */
    private VMUtil(){
    }

    /**
     * 创建虚拟机
     * 
     * @param vmId
     *            虚拟机Id
     * @param vmName
     *            虚拟机名称
     * @param cpu
     *            虚拟机CPU核数
     * @param memory
     *            虚拟机内存 单位：KB
     * @param volume
     *            真实卷
     * @params imagePath 映像路径
     * @param networkName
     *            网络名
     * @param hostIp
     *            物理主机Ip
     */
    public static void createVirtualMachine(Long vmId, Long faultID,
                    String vmName, Integer cpu, Long memory, Volume volume,
                    String imagePath, String networkName, String hostIp,
                    Map<String, String> emailInfoMap) {
        VMCreateExecutor vMCreateExecutor = new VMCreateExecutor();
        vMCreateExecutor.setVmId(vmId);
        vMCreateExecutor.setVmName(vmName);
        vMCreateExecutor.setCpu(cpu);
        vMCreateExecutor.setMemory(memory);
        vMCreateExecutor.setHostIp(hostIp);
        vMCreateExecutor.setVolume(volume);
        vMCreateExecutor.setImagePath(imagePath);
        vMCreateExecutor.setNetworkName(networkName);
        vMCreateExecutor.setFaultTolerantId(faultID);
        vMCreateExecutor.setEmailInfoMap(emailInfoMap);
        try {
            CloudKingQueue.put(vMCreateExecutor);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
    }

    /**
     * 快照操作
     * 
     * @param vmId
     * @param snapshotId
     * @param vm
     */
    public static void snapshotOperate(String executeFlag, Long vmId,
                    Long snapshotId) {
        VMsnapshotExecutor snapshotExecutor = new VMsnapshotExecutor(
                        executeFlag, vmId, snapshotId);
        executorService.execute(snapshotExecutor);
    }

    /**
     * 虚机备份操作
     * 
     * @param createSnapshotFlag
     * @param longParam
     * @param longParam2
     */
    public static void backupOperate(String executeFlag, Long vmId, Long backupId) {
        VMBackupExecutor backupExecutor = new VMBackupExecutor(executeFlag,
                        vmId,backupId);
        executorService.execute(backupExecutor);
    }

    /**
     * 虚拟机迁移
     * 
     * @param vmId
     * @param crId
     */
    public static void migrateOperate(Long vmId, Long crId) {
        VMMigrateExecutor vmMigrateExecutor = new VMMigrateExecutor(vmId, crId);
        executorService.execute(vmMigrateExecutor);
    }

    /**
     * 拷贝执行器
     * 
     * @author CloudKing
     */
    private static class VMCreateExecutor extends CloudKingQueueData {
        /**
         * 容错记录Id
         */
        private Long faultTolerantId;

        /**
         * 虚机id
         */
        private Long vmId;
        /**
         * 虚拟机名字
         */
        private String vmName;
        /**
         * CPU
         */
        private Integer cpu;
        /**
         * 内存
         */
        private Long memory;

        /**
         * hostIp
         */
        private String hostIp;
        /**
         * 卷
         */
        private Volume volume;
        /**
         * 镜像文件路径
         */
        private String imagePath;
        /**
         * 网络
         */
        private String networkName;

        /**
         * 邮件信息
         */
        private Map<String, String> emailInfoMap;

        public void setVmId(Long vmId) {
            this.vmId = vmId;
        }

        public void setVmName(String vmName) {
            this.vmName = vmName;
        }

        public void setCpu(Integer cpu) {
            this.cpu = cpu;
        }

        public void setMemory(Long memory) {
            this.memory = memory;
        }

        public void setHostIp(String hostIp) {
            this.hostIp = hostIp;
        }

        public void setVolume(Volume volume) {
            this.volume = volume;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setNetworkName(String networkName) {
            this.networkName = networkName;
        }

        public void setFaultTolerantId(Long faultTolerantId) {
            this.faultTolerantId = faultTolerantId;
        }

        public void setEmailInfoMap(Map<String, String> emailInfoMap) {
            this.emailInfoMap = emailInfoMap;
        }

        /**
         * 发送邮件
         * 
         * @param emailInfoMap
         * @param ip
         * @param dueTime
         * @throws Exception
         *             所有异常
         */
        private void sendMail(Map<String, String> emailInfoMap, String ip,
                        Date dueTime) throws Exception {
            StringBuilder contentBuiler = new StringBuilder();
            contentBuiler.append(emailInfoMap.get("content"));
            contentBuiler.append("\r\nIP地址：" + ip);
            if (dueTime == null) {
                contentBuiler.append("\r\n到期时间：永不过期");
            } else {
                SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                contentBuiler.append("\r\n到期时间：" + format.format(dueTime));
            }
            MailUtil.sendMail(emailInfoMap.get("subject"),
                            contentBuiler.toString(), emailInfoMap.get("email"));
        }

        /**
         * 执行CloudKingQueueData的方法
         * 
         * @throws Exception
         *             所有异常
         */
        @Override
        public void execute() throws Exception {
            EntityManager em = null;
            EntityTransaction transaction = null;
            try {
                em = ProjectUtil.getEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                //调用core
                VirtualMachine virtualMachine = VirtualMachine.createVM(vmName,
                                cpu, memory, volume, imagePath, networkName,
                                hostIp);
                //设置虚拟机状态
                VirtualMachineEntity vmEntity = em.find(
                                VirtualMachineEntity.class, vmId);
                if (vmEntity != null) {
                    vmEntity.setIp(virtualMachine.getIp());
                    vmEntity.setCreatedFlag(Constant.VM_CREATE_SUCCESS);
                    vmEntity.setCreatedResultMsg("创建成功");
                    //删除容错记录
                    FaultTolerantEntity ftEntity = em.find(
                                    FaultTolerantEntity.class, faultTolerantId);
                    em.remove(ftEntity);

                    //发送邮件
                    if (emailInfoMap != null) {
                        StringBuilder sql = new StringBuilder(
                                        "select tb_ubv.DUETIME_ from tb_portaluserbinvirtualmachine tb_ubv "
                                                        + " inner join tb_virtualmachine tb_vm on tb_vm.ID_=tb_ubv.VIRTUALMACHINE_ID and tb_vm.ID_="
                                                        + vmEntity.getId());
                        Query query = em.createNativeQuery(sql.toString());
                        Date dueTime = (Date) query.getSingleResult();
                        sendMail(emailInfoMap, vmEntity.getIp(), dueTime);
                    }
                }
                transaction.commit();
            } catch (VirtualizationException e) {
                LogUtil.warn(e);
                //设置创建失败消息
//                transaction.begin();
                VirtualMachineEntity vmEntity = em.find(
                                VirtualMachineEntity.class, vmId);
                if (vmEntity != null) {
                    vmEntity.setCreatedFlag(Constant.VM_CREATE_FAILED);
                    vmEntity.setCreatedResultMsg(e.getMessage());
                    em.merge(vmEntity);
                }
                transaction.commit();
            } catch (Exception e) {
                LogUtil.error(e);
                //设置创建失败消息
//                transaction.begin();
                VirtualMachineEntity vmEntity = em.find(
                                VirtualMachineEntity.class, vmId);
                if (vmEntity != null) {
                    vmEntity.setCreatedFlag(Constant.VM_CREATE_FAILED);
                    vmEntity.setCreatedResultMsg(e.getMessage());
                    em.merge(vmEntity);
                }
                transaction.commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }
    }

    /**
     * 
     * @author CloudKing
     */
    private static class VMsnapshotExecutor implements Runnable {
        /**
         * vmId
         */
        private Long vmId;
        /**
         * 快照id
         */
        private Long snapshotId;

        /**
         * 执行标记
         */
        private String executeFlag;

        /**
         * 
         * @param vmId
         * @param snapshotId
         * @param vm
         */
        public VMsnapshotExecutor(String executeFlag, Long vmId, Long snapshotId){
            this.vmId = vmId;
            this.snapshotId = snapshotId;
            this.executeFlag = executeFlag;
        }

        /**
         * run
         */
        @Override
        public void run() {
            EntityManager em = ProjectUtil.getEntityManager();
            VirtualMachineEntity vmEntity = null;
            try {
                em.getTransaction().begin();
                vmEntity = em.find(VirtualMachineEntity.class, vmId);
                VMSnapshotEntity snapshotEntity = em.find(
                                VMSnapshotEntity.class, snapshotId);
                vmEntity.setCreatedResultMsg("创建成功");
                String snapshotName = snapshotEntity.getName();
                String snapshotDesc = snapshotEntity.getDesc();
                VirtualMachine vm = VirtualMachine.getVM(vmEntity.getVmName(),
                                vmEntity.getComputeResource().getIp());
                snapshotEntity.setOperateFlag(null);
                if (executeFlag.equals(Constant.CREATE_SNAPSHOT_FLAG)) {
                    try {
                        vm.createSnapshot(snapshotName, snapshotDesc);
                        em.merge(snapshotEntity);
                    } catch (VirtualizationException e) {
                        LogUtil.error(e);
                        vmEntity.setOperateFailFlag(true);
                        vmEntity.setCreatedResultMsg(DateUtil.format(
                                        new Date(), "yyyy-MM-dd HH:mm:ss")
                                        + " "
                                        + snapshotDesc
                                        + "：快照创建失败！"
                                        + e.getMessage());
                        em.remove(snapshotEntity);
                    }
                } else if (executeFlag.equals(Constant.RESTORE_SNAPSHOT_FLAG)) {
                    try {
                        vm.revertSnapshot(snapshotName);
                    } catch (VirtualizationException e) {
                        LogUtil.error(e);
                        vmEntity.setOperateFailFlag(true);
                        vmEntity.setCreatedResultMsg(DateUtil.format(
                                        new Date(), "yyyy-MM-dd HH:mm:ss")
                                        + " "
                                        + snapshotDesc
                                        + "：快照还原失败！"
                                        + e.getMessage());
                    }
                    em.merge(snapshotEntity);
                } else if (executeFlag.equals(Constant.DELETE_SNAPSHOT_FLAG)) {
                    try {
                        vm.deleteSnapshot(snapshotName);
                        em.remove(snapshotEntity);
                    } catch (VirtualizationException e) {
                        LogUtil.error(e);
                        vmEntity.setOperateFailFlag(true);
                        vmEntity.setCreatedResultMsg(DateUtil.format(
                                        new Date(), "yyyy-MM-dd HH:mm:ss")
                                        + " "
                                        + snapshotDesc
                                        + "：快照删除失败！"
                                        + e.getMessage());
                        snapshotEntity.setOperateFlag("快照删除失败");
                        em.merge(snapshotEntity);
                    }
                }
                vmEntity.setCreatedResultMsg(Constant.VM_NORMAL_MSG);
                vmEntity.setOperateFailFlag(false);
            } catch (Exception e) {
                LogUtil.error(e);
            } finally {
                if (vmEntity != null) {
                    vmEntity.setCreatedFlag(Constant.VM_CREATE_SUCCESS);
                    em.merge(vmEntity);
                    em.getTransaction().commit();
                }
                if (em != null) {
                    em.close();
                }
            }
        }
    }

    /**
     * 虚拟机迁移
     * 
     * @author CloudKing
     */
    private static class VMMigrateExecutor implements Runnable {
        /**
         * 虚拟机ID
         */
        private Long vmId;
        /**
         * 计算节点ID
         */
        private Long crId;

        /**
         * 构造方法
         * 
         * @param vmId
         *            虚拟机ID
         * @param crId
         *            计算节点ID
         */
        public VMMigrateExecutor(Long vmId, Long crId){
            this.vmId = vmId;
            this.crId = crId;
        }

        /**
         * 线程RUN方法
         */
        @Override
        public void run() {
            EntityManager em = ProjectUtil.getEntityManager();
            VirtualMachineEntity vmEntity = null;
            ComputeResourceEntity crEntity = null;
            MachineTypeEntity mtEntity = null;
            em.getTransaction().begin();
            crEntity = em.find(ComputeResourceEntity.class, crId);
            vmEntity = em.find(VirtualMachineEntity.class, vmId);
            mtEntity = vmEntity.getMachineType();
            if (crEntity.getCpuAvailable() < mtEntity.getCpu()) {
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd HH:mm:ss")
                                + " "
                                + Constant.VM_MIGRATE_FAIL_MSG + "迁移后的计算节点内存不足");
                LogUtil.info("迁移后的计算节点cpu不足");
            }
            if (crEntity.getMemoryAvailable() < mtEntity.getMemory()) {
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd HH:mm:ss")
                                + " "
                                + Constant.VM_MIGRATE_FAIL_MSG + "迁移后的计算节点内存不足");
                LogUtil.info("迁移后的计算节点内存不足");
            }
            //先减去迁移后的计算节点资源，防止迁移期间资源变动，使得迁移后目标计算节点资源不够
            crEntity.setCpuAvailable(crEntity.getCpuAvailable()
                            - mtEntity.getCpu());
            crEntity.setMemoryAvailable(crEntity.getMemoryAvailable()
                            - mtEntity.getMemory());
            em.merge(crEntity);
            //**调用core
            try {
                //调用core
                VirtualMachine vm = VirtualMachine.getVM(vmEntity.getVmName(),
                                vmEntity.getComputeResource().getIp());
                vm.migrate(crEntity.getIp());
                //之前的计算节点资源恢复
                ComputeResourceEntity crOldEntity = vmEntity
                                .getComputeResource();
                crOldEntity.setCpuAvailable(crOldEntity.getCpuAvailable()
                                + mtEntity.getCpu());
                crOldEntity.setMemoryAvailable(crOldEntity.getMemoryAvailable()
                                + mtEntity.getMemory());
                em.merge(crOldEntity);
                //绑定新的计算节点
                vmEntity.setComputeResource(crEntity);
                vmEntity.setCreatedResultMsg(Constant.VM_NORMAL_MSG);
                vmEntity.setOperateFailFlag(false);
                em.merge(vmEntity);
            } catch (VirtualizationException e) {
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd HH:mm:ss")
                                + " "
                                + Constant.VM_MIGRATE_FAIL_MSG + e.getMessage());
                //迁移出错把迁移目标计算节点的资源加回来
                crEntity.setCpuAvailable(crEntity.getCpuAvailable()
                                + mtEntity.getCpu());
                crEntity.setMemoryAvailable(crEntity.getMemoryAvailable()
                                + mtEntity.getMemory());
                em.merge(crEntity);
                LogUtil.warn(e);
            } catch (Exception e) {
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd HH:mm:ss")
                                + " "
                                + Constant.VM_MIGRATE_FAIL_MSG + e.getMessage());
                //迁移出错把迁移目标计算节点的资源加回来
                crEntity.setCpuAvailable(crEntity.getCpuAvailable()
                                + mtEntity.getCpu());
                crEntity.setMemoryAvailable(crEntity.getMemoryAvailable()
                                + mtEntity.getMemory());
                em.merge(crEntity);
                LogUtil.error(e);
            } finally {
                vmEntity.setCreatedFlag(Constant.VM_CREATE_SUCCESS);
                em.merge(vmEntity);
                em.getTransaction().commit();
                em.close();
            }
        }
    }

    /**
     * 虚拟机备份操作线程
     * 
     * @author CloudKing
     */
    private static class VMBackupExecutor extends Thread {
        /**
         * 操作标志
         */
        private String executeFlag;
        /**
         * 虚机id
         */
        private Long vmId;
        /**
         *虚机备份的Id
         */
        private Long backupId;

        /**
         * 构造方法
         * 
         * @param executeFlag
         * @param vmId
         * @param backupId
         */
        public VMBackupExecutor(String executeFlag, Long vmId,Long backupId){
            this.executeFlag = executeFlag;
            this.vmId = vmId;
            this.backupId = backupId;
        };

        /**
         * 重写run方法
         */
        @Override
        public void run() {
            super.run();
            EntityManager em = ProjectUtil.getEntityManager();
            VirtualMachineEntity vmEntity = null;
            BackupStorageEntity bsEntity = null;
            VMBackupEntity buEntity = null;
            em.getTransaction().begin();
            try {
                vmEntity = em.find(VirtualMachineEntity.class, vmId);
                buEntity = em.find(VMBackupEntity.class, backupId);
                bsEntity = buEntity.getBackupStorage();
                ComputeResourceEntity crEntity = vmEntity.getComputeResource();
                //备份
                if (executeFlag.equals(Constant.CREATE_BACKUP_FLAG)) {
                    backupVM(em, vmEntity, crEntity.getIp(), bsEntity, buEntity);
                }
                //还原
                if (executeFlag.equals(Constant.RESTORE_BACKUP_FLAG)) {
                    restoreVM(em, vmEntity, crEntity.getIp(), buEntity);
                }
                vmEntity.setOperateFailFlag(false);
                vmEntity.setCreatedResultMsg(Constant.VM_NORMAL_MSG);
                buEntity.setOperateFlag(null);
            } catch (VirtualizationException e) {
                em.getTransaction().rollback();
                LogUtil.warn(e);
                em.getTransaction().begin();
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd") + e.getMessage());
            } catch (Exception e) {
                em.getTransaction().rollback();
                LogUtil.error(e);
                em.getTransaction().begin();
                vmEntity.setOperateFailFlag(true);
                vmEntity.setCreatedResultMsg(DateUtil.format(new Date(),
                                "yyyy-MM-dd") + e.getMessage());
            } finally {
                vmEntity.setCreatedFlag(Constant.VM_CREATE_SUCCESS);
                em.merge(vmEntity);
                buEntity.setOperateFlag(Constant.VM_BACKUP_NORMAL_MSG);
                em.merge(buEntity);
                em.getTransaction().commit();
                em.close();
            }
        }

        /**
         * 备份虚拟机
         * 
         * @param em
         * @param vmEntity
         * @param crIP
         * @param bsEntity
         * @param buEntity
         * @throws VirtualizationException
         *             底层虚拟化异常
         */
        private void backupVM(EntityManager em, VirtualMachineEntity vmEntity,
                        String crIP, BackupStorageEntity bsEntity,
                        VMBackupEntity buEntity) throws VirtualizationException {
            VirtualMachine virtualMachine = null;
            Long capacity = null;
            //调用core
            virtualMachine = VirtualMachine.getVM(vmEntity.getVmName(), crIP);
            capacity = virtualMachine.backup(bsEntity.getIp(),
                            buEntity.getName());
            //**调用core结束
            //设置资源
            buEntity.setCapacity(capacity);
            buEntity.setBackupStorage(bsEntity);
            em.merge(buEntity);
            bsEntity.setAvailableCapacity(bsEntity.getAvailableCapacity()
                            - capacity);
            em.merge(bsEntity);
        }

        /**
         * 还原虚拟机
         * 
         * @param vmEntity
         * @param crIP
         * @throws VirtualizationException
         * @throws IllegalAccessException
         *             帅选存储异常
         */
        private void restoreVM(EntityManager em, VirtualMachineEntity vmEntity,
                        String crIP, VMBackupEntity backupEntity)
                        throws VirtualizationException, IllegalAccessException {
            StorageResourceEntity storageEntity = obtainMaxStorage(em);
            VirtualMachine virtualMachine = null;
            virtualMachine = VirtualMachine.getVM(vmEntity.getVmName(), crIP);
            BackupStorageEntity backupStorageEntity = backupEntity
                            .getBackupStorage();
            //调用Core
            Set<Volume> backupVols = virtualMachine.recovery(
                            backupEntity.getName(),
                            backupStorageEntity.getIp(),
                            storageEntity.getPoolName());

            //清除原来的卷和快照
            String imageVolumnName = clearOldVolumAndSnapshot(em, vmEntity,
                            storageEntity);
            //生成新的卷计算存储大小,更新虚机的卷
            generateVolumn(em, vmEntity, storageEntity, backupVols,
                            imageVolumnName);
        }

        /**
         * 获取最大的存储
         * 
         * @param em
         * @param capacity
         * @return
         */
        private StorageResourceEntity obtainMaxStorage(EntityManager em) {
            List<?> storages = em
                            .createQuery("from StorageResourceEntity  "
                                            + " order by availableCapacity ASC")
                            .getResultList();
            return storages.isEmpty() ? null : (StorageResourceEntity) storages
                            .get(0);
        }

        /**
         * 清除原来的卷和快照
         * 
         * @param em
         * @param vmEntity
         * @param storageEntity
         * @return
         */
        private String clearOldVolumAndSnapshot(EntityManager em,
                        VirtualMachineEntity vmEntity,
                        StorageResourceEntity storageEntity) {
            Long volumnsSize = 0L;
            String imageVolumnName = null;
            List<VolumnEntity> vols = vmEntity.getVolumns();
            Map<StorageResourceEntity, Long> storageAndVolSizeMap = new HashMap<StorageResourceEntity, Long>();
            //清空以前的卷;
            for (VolumnEntity v : vols) {
                if (v.getImageVolumnFlag().equals(
                                Constant.IMAGE_VOLUMN_FALG_TRUE)) {
                    imageVolumnName = v.getName();
                }
                storageEntity = v.getStorageResource();
                storageAndVolSizeMap
                                .put(storageEntity,
                                                storageAndVolSizeMap
                                                                .get(storageEntity) == null ? v
                                                                .getSize()
                                                                : storageAndVolSizeMap
                                                                                .get(storageEntity)
                                                                                + v.getSize());
                volumnsSize += v.getSize();
                em.remove(v);
            }
            //清除快照
            List<VMSnapshotEntity> snapshots = vmEntity.getVmSnapshots();
            for (VMSnapshotEntity ss : snapshots) {
                em.remove(ss);
            }
            //计算存储大小
            for (Entry<StorageResourceEntity, Long> e : storageAndVolSizeMap
                            .entrySet()) {
                storageEntity = e.getKey();
                storageEntity.setAvailableCapacity(storageEntity
                                .getAvailableCapacity() + e.getValue());
                em.merge(storageEntity);
            }
            return imageVolumnName;
        }

        /**
         * 生成新的卷并更新存储大小
         * 
         * @param em
         * @param storageEntity
         * @param backupVols
         * @param imageVolumnName
         */
        private void generateVolumn(EntityManager em,
                        VirtualMachineEntity vmEntity,
                        StorageResourceEntity storageEntity,
                        Set<Volume> backupVols, String imageVolumnName) {
            Long volumnsSize = 0L;
            List<VolumnEntity> vols = new ArrayList<VolumnEntity>();
            VolumnEntity volumnEntity = null;
            for (Volume v : backupVols) {
                volumnsSize += ProjectUtil.kByteToGiga(v.getCapacity());
                volumnEntity = new VolumnEntity();
                volumnEntity.setImageVolumnFlag(v.getVolumeName().equals(
                                imageVolumnName) ? Constant.IMAGE_VOLUMN_FALG_TRUE
                                : Constant.IMAGE_VOLUMN_FALG_FALSE);
                volumnEntity.setSize(ProjectUtil.kByteToGiga(v.getCapacity())
                                .longValue());
                volumnEntity.setAddTime(new Date());
                volumnEntity.setName(v.getVolumeName());
                volumnEntity.setStorageResource(storageEntity);
                volumnEntity.setVirtualMachine(vmEntity);
                vols.add(volumnEntity);
                em.merge(volumnEntity);
            }
            vmEntity.setVolumns(vols);
            em.merge(vmEntity);
            storageEntity.setAvailableCapacity(storageEntity
                            .getAvailableCapacity() - volumnsSize);
        }
    }
}
