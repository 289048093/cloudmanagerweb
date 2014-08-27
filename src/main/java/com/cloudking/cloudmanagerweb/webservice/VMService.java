/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 10, 2012  12:08:31 PM
 */
package com.cloudking.cloudmanagerweb.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.BeanUtils;

import com.cloudking.cloudmanager.core.virtualization.Status;
import com.cloudking.cloudmanager.core.virtualization.VirtualMachine;
import com.cloudking.cloudmanager.core.virtualization.VmUtils;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.PortalOrderDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserBinVirtualMachineOrderDAO;
import com.cloudking.cloudmanagerweb.dao.PortalUserDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.VMSnapshotDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderCategoryDAO;
import com.cloudking.cloudmanagerweb.dao.WorkOrderDAO;
import com.cloudking.cloudmanagerweb.entity.PortalOrderEntity;
import com.cloudking.cloudmanagerweb.entity.PortalUserEntity;
import com.cloudking.cloudmanagerweb.entity.VMSnapshotEntity;
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderCategoryEntity;
import com.cloudking.cloudmanagerweb.entity.WorkOrderEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.util.VMUtil;
import com.cloudking.cloudmanagerweb.vo.PortalUserBinVirtualMachineVO;
import com.cloudking.cloudmanagerweb.vo.VMSnapshotVO;
import com.cloudking.cloudmanagerweb.vo.VirtualMachineVO;

/**
 * 虚拟机WebService
 * 
 * @author CloudKing
 */
public class VMService {
    /**
     * portalUserDAO
     */
    @Resource
    PortalUserDAO portalUserDAO;
    
    /**
     * workOrderDAO
     */
    @Resource
    WorkOrderDAO workOrderDAO;
    
    /**
     * 
     */
    @Resource
    PortalOrderDAO portalOrderDAO;
    
    /**
     * workOrderCategoryDAO
     */
    @Resource
    WorkOrderCategoryDAO workOrderCategoryDAO;

    /**
     * 快照备份DAO
     */
    @Resource
    VMSnapshotDAO vMSnapshotDAO;

    /**
     * machineTypeDAO
     */
    @Resource
    MachineTypeDAO machineTypeDAO;
    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * templateDAO
     */
    @Resource
    TemplateDAO templateDAO;

    /**
     * portalUserBinVirtualMachineOrderDAO
     */
    @Resource
    PortalUserBinVirtualMachineOrderDAO portalUserBinVirtualMachineOrderDAO;

    /**
     * virtualMachineDAO
     */
    @Resource
    VirtualMachineDAO virtualMachineDAO;

    /**
     * 查询我的虚拟机
     * 
     * @return
     */
    public String queryMyVirtualMachine(String username, int start, int limit) {
        try {
            String userId = StringUtil.encrypt(username, Constant.PORTAL_USERNAME_SALT); //经过编码后的用户名
            //查询总记录数目
            int totalCount = virtualMachineDAO.getQueryCountByPortalUserID(userId);
            if (totalCount > 0) {
                List<VirtualMachineEntity> vmList = (List<VirtualMachineEntity>) virtualMachineDAO.queryByPortalUserID(
                                userId, start, limit);
                List<PortalUserBinVirtualMachineVO> vos = new ArrayList<PortalUserBinVirtualMachineVO>();
                PortalUserBinVirtualMachineVO vo = null;
                for (VirtualMachineEntity entity : vmList) {
                    vo = new PortalUserBinVirtualMachineVO();
                    vo.setMachineTypeArgs("CPU(核)=" + entity.getMachineType().getCpu() + " 内存(M)="
                                    + entity.getMachineType().getMemory() + " 硬盘=" + entity.getMachineType().getDisk());
                    vo.setCpu(entity.getMachineType().getCpu());
                    vo.setMemory(entity.getMachineType().getMemory());
                    vo.setDisk(entity.getMachineType().getDisk());
                    vo.setCreatedFlag(entity.getCreatedFlag());
                    vo.setCreatedResultMsg(entity.getCreatedResultMsg());
                    vo.setDesc(entity.getDesc());
                    vo.setDisk(entity.getMachineType().getDisk());
                    vo.setDomainName(entity.getDomain().getName());
                    vo.setId(entity.getId());
                    if (entity.getIp() != null) {
                        vo.setIp(entity.getIp());
                    } else {
                        vo.setIp("-");
                    }
                    vo.setMemory(entity.getMachineType().getMemory());
                    vo.setName(entity.getName());
                    vo.setNetwork(entity.getNetWork().getName());
                    vo.setTemplateName(entity.getTemplate().getName());
                    vo.setVmName(entity.getVmName());
                    //获取虚拟机状态
                    vo.setStatus(getVirtualMachineStatus(entity).toString());
                    vos.add(vo);
                }
                return "{success:true,data:" + ProjectUtil.objectToJSON(vos) + ",totalRecord:" + totalCount + "}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:true,data:{},totalRecord:0}";
    }
    
    /**
     * 申请删除
     * 
     * @param vmID
     *            虚拟机Id
     * @param username
     *            用户唯一身份标识
     * @return
     */
    public String deleteVirtualMachine(Long vmID, String username) {
        try {
            //验证身份
            String userId = StringUtil.encrypt(username, Constant.PORTAL_USERNAME_SALT);
            PortalUserEntity portalUserEntity = portalUserDAO.getUserByUserID(userId);
            if(portalUserEntity == null){
                return "{success:false,msg:'用户不存在'}";
            }
            
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.getByPortal(userId, vmID);
            if(virtualMachineEntity == null){
                return "{success:false,msg:'虚拟机不存在'}";
            }
            //创建订单
            PortalOrderEntity portalOrderEntity = new PortalOrderEntity();
            portalOrderEntity.setApplicant(portalUserEntity);
            portalOrderEntity.setApplyMsg("用户：" + portalUserEntity.getRealname() + "申请删除名称为["+virtualMachineEntity.getName()+"]的虚拟机");
            portalOrderEntity.setRefId(virtualMachineEntity.getId());
            portalOrderDAO.insert(portalOrderEntity);
            //创建工单
            WorkOrderCategoryEntity workOrderCatagory = workOrderCategoryDAO.get(Constant.WORK_ORDER_CATEGORY_DELETE_VM);
            if (workOrderCatagory == null) {
                return "{success:false,msg:'网络超时，请稍候再试或联系管理员'}";
            }

            WorkOrderEntity workOrder = new WorkOrderEntity();
            workOrder.setSerialNumber(ProjectUtil.createWorkOrderSerialNumber());
            workOrder.setCategory(workOrderCatagory);
            workOrder.setReceiveDomain(virtualMachineEntity.getDomain());
            workOrder.setReceiver(virtualMachineEntity.getDomain().getUser());
            workOrder.setRefId(portalOrderEntity.getId());
            workOrder.setTitle(workOrderCatagory.getName());
            workOrder.setContent("用户：" + portalUserEntity.getRealname() + "申请删除名称为["+virtualMachineEntity.getName()+"]的虚拟机");
            workOrder.setCreateTime(new Date());
            workOrder.setStatus(Constant.WORKORDER_ACCEPING);
            workOrderDAO.insert(workOrder);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络超时，请稍候再试或联系管理员'}";
    }

    /**
     * 获取虚拟机实例
     * 
     * @param vmID
     *            虚拟机Id
     * @param username
     *            用户唯一身份标识
     * @return
     */
    private VirtualMachineEntity getVM(Long vmID, String username) {
        try {
            //验证身份
            String userId = StringUtil.encrypt(username, Constant.PORTAL_USERNAME_SALT); //经过编码后的用户名
            //查询总记录数目
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.getByPortal(userId, vmID);
            return virtualMachineEntity;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }
    
    /**
     * 查询虚拟机状态
     * 
     * @param virtualMachineEntity
     *            业务虚拟机实体类
     * @return default Status.error
     */
    private Status getVirtualMachineStatus(VirtualMachineEntity virtualMachineEntity) {
        Status status = Status.unknow;
        if (virtualMachineEntity.getCreatedFlag().equalsIgnoreCase(Constant.VM_CREATE_SUCCESS)) {
            try {
                VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(), virtualMachineEntity
                                .getComputeResource().getIp());
                if(virtualMachine == null){
                    return status;
                }
                status = virtualMachine.getStatus();
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
        return status;
    }

    /**
     * 异步查询虚拟机状态
     * 
     * @param vmIds
     *            虚拟机Id
     * @return default Status.error
     */
    public String queryVirtualMachineStatusOverAjax(String vmIds) {
        try {
            if (StringUtil.isBlank(vmIds)) {
                return "{success:false,msg:'虚拟机不存在'}";
            }
            List<VirtualMachineEntity> virtualMachines = virtualMachineDAO.queryByVMIDs(vmIds);
            List<VirtualMachineVO> resultState = new ArrayList<VirtualMachineVO>();
            VirtualMachineVO virtualMachineVO = null;
            for (VirtualMachineEntity virtualMachine : virtualMachines) {
                virtualMachineVO = new VirtualMachineVO();
                //放入基本属性
                virtualMachineVO.setId(virtualMachine.getId());
                virtualMachineVO.setCreatedFlag(virtualMachine.getCreatedFlag());
                virtualMachineVO.setCreatedResultMsg(virtualMachine.getCreatedResultMsg().replaceAll("\""," ").replaceAll("'"," "));
                if (virtualMachine.getOperateFailFlag() != null) {
                    virtualMachineVO.setOperateFailFlag(virtualMachine.getOperateFailFlag());
                } else {
                    virtualMachineVO.setOperateFailFlag(false);
                }
                //成功创建并处于非特殊操作状态的虚拟机
                if (Constant.VM_CREATE_SUCCESS.equals(virtualMachine.getCreatedFlag())) {
                    Status status = getVirtualMachineStatus(virtualMachine);
                    if (status != null) {
                        virtualMachineVO.setStatus(status.toString());
                    } else {
                        virtualMachineVO.setStatus(Status.unknow.toString());
                    }
                } else {
                    //虚拟机创建失败，虚拟机创建中，正在创建快照,正在还原快照,正在删除快照
                    virtualMachineVO.setStatus(Status.unknow.toString());
                }
                resultState.add(virtualMachineVO);
            }
            return "{success:true,data:"+ JSONUtil.serialize(resultState)+"}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'虚拟机不存在'}";
        }
    }

    /**
     * 备份
     */
    public String insertSnapshot(Long vmId, String snapshotDesc) {
        try {
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(vmId);
            if (virtualMachineEntity == null) {
                return "{success:false,msg:'虚拟机不存在'}";
            }
            String snapshotName = ProjectUtil.createSnapshotName();
            VMSnapshotEntity snapshotEntity = new VMSnapshotEntity();
            snapshotEntity.setName(snapshotName);
            snapshotEntity.setVirtualMachine(virtualMachineEntity);
            snapshotEntity.setAddTime(new Date());
            snapshotEntity.setDesc(snapshotDesc);
            snapshotEntity.setOperateFlag("正在创建中");
            vMSnapshotDAO.insert(snapshotEntity);
            //设置虚机状态
            virtualMachineEntity.setCreatedFlag(Constant.CREATE_SNAPSHOT_FLAG);
            virtualMachineEntity.setCreatedResultMsg("快照正在创建");
            virtualMachineDAO.update(virtualMachineEntity);
            //后台线程创建快照
            VMUtil.snapshotOperate(Constant.CREATE_SNAPSHOT_FLAG, virtualMachineEntity.getId(), snapshotEntity.getId());
            return "{success:true,msg:'快照正在创建！'}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'网络异常'}";
        }

    }

    /**
     * 还原快照
     */
    public String restoreSnapshot(Long vmId, Long snapshotId) {
        try {
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(vmId);
            if (virtualMachineEntity == null) {
                return "{success:false,msg:'虚拟机不存在'}";
            }
            VMSnapshotEntity snapshot = vMSnapshotDAO.get(snapshotId);
            snapshot.setOperateFlag("正在还原中");
            vMSnapshotDAO.update(snapshot);
            virtualMachineEntity.setCreatedFlag(Constant.RESTORE_SNAPSHOT_FLAG);
            virtualMachineEntity.setCreatedResultMsg("虚机系统正在还原");
            virtualMachineDAO.update(virtualMachineEntity);
            VMUtil.snapshotOperate(Constant.RESTORE_SNAPSHOT_FLAG, virtualMachineEntity.getId(), snapshotId);
            return "{success:true,msg:'虚机系统正在还原中!'}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'网络异常'}";
        }
    }

    /**
     * 初始化还原
     */
    public String initRestoreSnapshot(Long vmId) {
        try {
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(vmId);
            if (virtualMachineEntity == null) {
                return "{success:false,msg:'虚拟机不存在'}";
            }
            List<VMSnapshotEntity> snapshotEntitys = virtualMachineEntity.getVmSnapshots();
            VMSnapshotVO snapshotVO = null;
            List<VMSnapshotVO> snapshotVOs = new ArrayList<VMSnapshotVO>();
            for (VMSnapshotEntity entity : snapshotEntitys) {
                snapshotVO = new VMSnapshotVO();
                BeanUtils.copyProperties(entity, snapshotVO);
                snapshotVOs.add(snapshotVO);
            }
            return "{success:true,data:" + JSONUtil.serialize(snapshotVOs)+ "}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'网络异常'}";
        }
    }

    /**
     * 删除快照
     * 
     * @param vmId
     *            虚拟机Id
     * @param snapshotId
     *            快照Id
     */
    public String deleteSnapshot(Long vmId, Long snapshotId) {
        try {
            VirtualMachineEntity virtualMachineEntity = virtualMachineDAO.get(vmId);
            if (virtualMachineEntity == null) {
                return "{success:false,msg:'虚拟机不存在'}";
            }
            VMSnapshotEntity snapshot = vMSnapshotDAO.get(snapshotId);
            snapshot.setOperateFlag("正在删除中");
            vMSnapshotDAO.update(snapshot);
            virtualMachineEntity.setCreatedFlag(Constant.DELETE_SNAPSHOT_FLAG);
            virtualMachineEntity.setCreatedResultMsg("快照正在删除中");
            virtualMachineDAO.update(virtualMachineEntity);
            VMUtil.snapshotOperate(Constant.DELETE_SNAPSHOT_FLAG, virtualMachineEntity.getId(), snapshotId);
            return "{success:true,msg:'快照正在删除中!'}";
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'网络异常'}";
        }
    }

    /**
     * vnc
     */
    public String vnc(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                if (getVirtualMachineStatus(virtualMachineEntity) == Status.running) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());

                    String ipAddress = virtualMachineEntity.getComputeResource().getIp();
                    int port = VmUtils.getvncPort(virtualMachineEntity.getVmName(), virtualMachine.getHostIp());
                    return "{success:true,hostname:'" + ipAddress + "',port:" + port + "}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
            return "{success:false,msg:'网络异常'}";
        }

    }

    /**
     * 开机
     */
    public String start(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                Status status = getVirtualMachineStatus(virtualMachineEntity);
                if (status == Status.shutoff || status == Status.defined) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());
                    virtualMachine.start();
                    return "{success:true,msg:'正在开机,请稍候查看状态。 '}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络异常'}";
    }

    /**
     * 重启
     */
    public String reboot(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                Status status = getVirtualMachineStatus(virtualMachineEntity);
                if (status == Status.running) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());
                    virtualMachine.reboot();
                    return "{success:true,msg:'正在重启，请稍候查看状态。'}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络异常'}";
    }

    /**
     * 恢复
     */
    public String resume(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                Status status = getVirtualMachineStatus(virtualMachineEntity);
                if (status == Status.paused) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());
                    virtualMachine.resume();
                    return "{success:true,msg:'正在恢复中，请稍候查看状态。'}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络异常'}";
    }

    /**
     * 挂起
     */
    public String suspend(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                Status status = getVirtualMachineStatus(virtualMachineEntity);
                if (status == Status.running) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());
                    virtualMachine.suspend();
                    return "{success:true,msg:'正在挂起，请稍候查看状态。'}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络异常'}";
    }

    /**
     * 关机
     */
    public String shutdown(Long id, String userId) {
        try {
            VirtualMachineEntity virtualMachineEntity = getVM(id, userId);
            if (virtualMachineEntity != null) {
                Status status = getVirtualMachineStatus(virtualMachineEntity);
                if (status == Status.running) {
                    VirtualMachine virtualMachine = VirtualMachine.getVM(virtualMachineEntity.getVmName(),
                                    virtualMachineEntity.getComputeResource().getIp());
                    virtualMachine.shutdown();
                    return "{success:true,msg:'正在关机，请稍候查看状态。'}";
                } else {
                    return "{success:false,msg:'虚拟机状态不正确'}";
                }
            } else {
                return "{success:false,msg:'虚拟机不存在'}";
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "{success:false,msg:'网络异常'}";
    }

}
