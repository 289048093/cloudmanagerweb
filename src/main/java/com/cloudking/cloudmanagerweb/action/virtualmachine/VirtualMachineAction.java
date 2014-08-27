/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.virtualmachine;

import java.io.FileInputStream;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.virtualmachine.VirtualMachineService;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.util.VMUtil;
import com.cloudking.cloudmanagerweb.vo.VirtualMachineVO;

/**
 * 虚拟机Action
 * 
 * @author CloudKing
 * 
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/virtualMachineManager")
@Results( {
                @Result(name = "success", type = "dispatcher", location = "/virtualMachine/virtualMachine.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/virtualMachine/virtualMachine.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/virtualMachine/virtualMachine.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "stream", type = "stream", params = { "contentType", "application/octet-stream",
                                "inputName", "fis", "allowCaching", "false", "encode", "true", "contentDisposition",
                                "attachment;filename=%{#request.cloudContext.params.vncName}" }),
                @Result(name = "vncSuccess", type = "redirect", location = "/vnc/index.jsp?port=%{cloudContext.params.port}&hostname=%{cloudContext.params.hostname}&vmName=%{cloudContext.params.vmName}") })
public class VirtualMachineAction extends BaseAction<VirtualMachineVO> {
    /**
     * 
     */
    private static final long serialVersionUID = -3717080712894187487L;
    /**
     * 虚拟机service
     */
    @Resource
    private transient VirtualMachineService virtualMachineService;

    /**
     * 文件下载流
     */
    private transient FileInputStream fis;

    public FileInputStream getFis() {
        return fis;
    }

    /**
     * 默认的action处理方法
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/virtualMachine")
    public String execute() throws Exception {
        // TODO Auto-generated method stub
        return INPUT;
    }

    /**
     * 查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        virtualMachineService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 删除卷
     * 
     * @throws Exception
     *             所有异常
     */
    public String deleteVolumn() throws Exception {
        virtualMachineService.deleteVolumn(cloudContext);
        return JSON;
    }

    /**
     * 添加卷
     * 
     * @throws Exception
     *             所有异常
     */
    public String addVolumn() throws Exception {
        virtualMachineService.insertVolumn(cloudContext);
        return JSON;
    }

    /**
     * 添加虚拟机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            virtualMachineService.insert(cloudContext);
        }
        return JUMP;
    }

    /**
     * 查询虚拟机卷
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryVolumnByVmId() throws Exception {
        virtualMachineService.queryVolumnByVmId(cloudContext);
        return JSON;
    }

    /**
     * 异步查询虚拟机状态，并返回相应状态
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryVirtualMachineStatusByVmId() throws Exception {
        virtualMachineService.queryVirtualMachineStatusByVmId(cloudContext);
        return JSON;
    }

    /**
     * 查询当前域拥有的资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryResourcePoolByCurrentDomain() throws Exception {
        cloudContext.addParam("domainId", getLoginedUser().getDomainID());
        virtualMachineService.queryResourcePoolByCurrentDomain(cloudContext);
        return JSON;
    }

    /**
     * 查询指定资源池下的计算节点，并要求cpu内存参数大于指定值
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryResourceByPoolAndCpuMemory() throws Exception {
        virtualMachineService.queryResourceByPoolAndCpuMemory(cloudContext);
        return JSON;
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String delete() throws Exception {
        virtualMachineService.delete(cloudContext);
        return JUMP;
    }

    /**
     * 启动
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String startup() throws Exception {
        virtualMachineService.startup(cloudContext);
        return JUMP;
    }

    /**
     * 重启
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String reboot() throws Exception {
        virtualMachineService.reboot(cloudContext);
        return JUMP;
    }

    /**
     * 关机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String shutdown() throws Exception {
        virtualMachineService.shutdown(cloudContext);
        return JUMP;
    }

    /**
     * 挂起
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String suspend() throws Exception {
        virtualMachineService.suspend(cloudContext);
        return JUMP;
    }

    /**
     * 恢复
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String resume() throws Exception {
        virtualMachineService.resume(cloudContext);
        return JUMP;
    }

    /**
     * vnc
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String vnc() throws Exception {
        getSession().removeAttribute("GUAC_CONFIGS");
        virtualMachineService.vnc(cloudContext);
        return cloudContext.getSuccessIngoreWarn() ? "vncSuccess" : ERROR;
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String update() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            virtualMachineService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 初始化新怎或则修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdate() throws Exception {
        virtualMachineService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * vnc下载
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String vncDownLoad() throws Exception {
        virtualMachineService.vncDownLoad(cloudContext);
        fis = (FileInputStream) cloudContext.getObjectParam("vncFis");
        return "stream";
    }

    /**
     * 备份系统
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String backup() throws Exception {
        virtualMachineService.insertSnapshot(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            //后台线程创建快照
            VMUtil.snapshotOperate(Constant.CREATE_SNAPSHOT_FLAG, cloudContext.getLongParam("vmId"), cloudContext
                            .getLongParam("snapshotId"));
            cloudContext.addSuccessMsg("快照正在创建！");
        }
        return JUMP;
    }

    /**
     * 还原系统
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String restore() throws Exception {
        virtualMachineService.restore(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            VMUtil.snapshotOperate(Constant.RESTORE_SNAPSHOT_FLAG, cloudContext.getLongParam("vmId"), cloudContext
                            .getLongParam("snapshotId"));
            cloudContext.addSuccessMsg("虚机系统正在还原中!");
        }
        return JUMP;
    }

    /**
     * 初始化还原窗口
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initRestore() throws Exception {
        virtualMachineService.initRestore(cloudContext);
        return JSON;
    }

    /**
     * 删除快照
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String deleteSnapshot() throws Exception {
        virtualMachineService.deleteSnapshot(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            VMUtil.snapshotOperate(Constant.DELETE_SNAPSHOT_FLAG, cloudContext.getLongParam("vmId"), cloudContext
                            .getLongParam("snapshotId"));
            cloudContext.addSuccessMsg("快照正在删除中!");
        }
        return JUMP;
    }

    /**
     * 获取所有上级域的模版
     * 
     * @return
     */
    public String queryAllSuperDomainTemplate() {
        virtualMachineService.queryAllSuperDomainTemplate(cloudContext);
        return JSON;
    }

    /**
     * 获取所有上级域的机型
     * 
     * @return
     */
    public String queryAllSuperDomainMachineTypes() {
        virtualMachineService.queryAllSuperDomainMachineTypes(cloudContext);
        return JSON;
    }

    /**
     * 查询机柜，级联操作
     * 
     * @return
     */
    public String queryRackByRoom() {
        virtualMachineService.queryRackByRoom(cloudContext);
        return JSON;
    }

    /**
     * 查询计算节点，级联操作
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryComputeByRack() throws Exception {
        virtualMachineService.queryComputeByRack(cloudContext);
        return JSON;
    }

    /**
     * 新增时域切换查询数据
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryData4ChangeDomain() throws Exception {
        virtualMachineService.queryData4ChangeDomain(cloudContext);
        return JSON;
    }

    /**
     * 强制关机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String forceShutdown() throws Exception {
        virtualMachineService.forceShutdown(cloudContext);
        return JUMP;
    }

    /**
     * 初始化迁移的计算节点数据
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initMoveVm() throws Exception {
        virtualMachineService.initMoveVm(cloudContext);
        return JSON;
    }

    /**
     * 虚拟机迁移
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String moveVm() throws Exception {
        virtualMachineService.updateMigrateVm(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            VMUtil.migrateOperate(cloudContext.getLongParam("vmId"), cloudContext.getLongParam("crId"));
            cloudContext.addSuccessMsg("正在迁移！");
        }
        return JUMP;
    }

    /**
     * 备份虚拟机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String backup2Storage() throws Exception {
        virtualMachineService.insertBackup2Storage(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            //后台线程创建备份
            VMUtil.backupOperate(Constant.CREATE_BACKUP_FLAG, cloudContext.getLongParam("vmId"), cloudContext
                            .getLongParam("backupId"));
            cloudContext.addSuccessMsg("正在创建备份中！");
        }
        return JUMP;
    }

    /**
     * 初始化还原窗口
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initRestore2Storage() throws Exception {
        virtualMachineService.initRestore2Storage(cloudContext);
        return JSON;
    }

    /**
     * 还原虚机
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String restore2Storage() throws Exception {
        virtualMachineService.restore2Storage(cloudContext);
        if (cloudContext.getSuccessIngoreWarn()) {
            VMUtil.backupOperate(Constant.RESTORE_BACKUP_FLAG, cloudContext.getLongParam("vmId"),cloudContext.getLongParam("backupId"));
            cloudContext.addSuccessMsg("虚机正在还原中!");
        }
        return JUMP;
    }

    /**
     * 删除虚机备份
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String deleteBackupVmData() throws Exception {
        virtualMachineService.deleteBackupVmData(cloudContext);
//        VMUtil.backupOperate(Constant.DELETE_SNAPSHOT_FLAG, cloudContext.getLongParam("vmId"));
//        cloudContext.addSuccessMsg("虚拟备份正在删除中!");
        return JUMP;
    }

    /**
     * 表单参数验证 (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {
        super.validate();
        HttpServletRequest req = getRequest();
        String action = req.getRequestURI();
        Boolean isAdd = action.lastIndexOf("add.action") != -1;
        if (isAdd) {
            String name = req.getParameter("cloudContext.vo.name");
            String machineTypeID = req.getParameter("cloudContext.params.machineTypeID");
            String templateID = req.getParameter("cloudContext.params.templateID");
            String netWork = req.getParameter("cloudContext.params.netWork");
            String manual = req.getParameter("cloudContext.params.customComputeResourceFlag");
            String computeResourceID = req.getParameter("cloudContext.params.computeResourceID");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("名称必须为长度20以内的字母数字下划线或中文组成! \\r");
            }
            if (StringUtil.isBlank(machineTypeID)) {
                cloudContext.addErrorMsg("配置必须选择! \\r");
            }
            if (StringUtil.isBlank(templateID)) {
                cloudContext.addErrorMsg("模版必须选择! \\r");
            }
            if (StringUtil.isBlank(netWork)) {
                cloudContext.addErrorMsg("网络必须选择! \\r");
            }
            if (StringUtil.isBlank(manual)) {
                cloudContext.addErrorMsg("必须选择一种计算节点选择方式! \\r");
            } else if (manual.equals("true") && StringUtil.isBlank(computeResourceID)) {
                cloudContext.addErrorMsg("必须选择一个计算节点! \\r");
            }
        }
    }

    //*************************
    //*********循环测试代码*******
    //*********循环测试代码*******
    //*********循环测试代码*******
    //*********循环测试代码*******
    //*********循环测试代码*******
    //*********循环测试代码*******
    //*************************
    //*************************
    //*************************
    /**
     * flag
     */
    private static Boolean migrateLoopFlag = false;
    /**
     * flag
     */
    private static Boolean backupLoopFlag = false;
    /**
     * flag
     */
    private static Boolean restoreLoopFlag = false;

    /**
     * 迁移循环
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String migrateLoop() throws Exception {
//        Long vmId = 10L;
        Long cr202 = cloudContext.getLongParam("cr202Id");
        Long cr204 = cloudContext.getLongParam("cr204Id");
//        cloudContext.getVo().setId(vmId);
        MigrateLoopThread migrateLoopThread = new MigrateLoopThread(cr202, cr204);
        migrateLoopThread.start();
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * stop
     * 
     * @return
     */
    public String migrateLoopStop() {
        migrateLoopFlag = false;
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * 备份循环
     * 
     * @return
     */
    public String backupLoop() {
        backupLoopFlag = true;
//        Long vmId = 10L;
//        cloudContext.getVo().setId(vmId);
        BackupLoopThread backupLoopThread = new BackupLoopThread();
        backupLoopThread.start();
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * stop
     * 
     * @return
     */
    public String backupLoopStop() {
        backupLoopFlag = false;
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * restoreLoop
     * 
     * @return
     */
    public String restoreLoop() {
//        Long vmId = 10L;
//        cloudContext.getVo().setId(vmId);
        restoreLoopFlag = true;
        RestoreLoopThread restoreLoopThread = new RestoreLoopThread();
        restoreLoopThread.start();
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * restoreLoopStore
     * 
     * @return
     */
    public String restoreLoopStop() {
        restoreLoopFlag = false;
        cloudContext.addSuccessMsg("操作成功");
        return JUMP;
    }

    /**
     * init
     * 
     * @return
     */
    public String initLoopStop() {
        cloudContext.addParam("migrate", migrateLoopFlag);
        cloudContext.addParam("backup", backupLoopFlag);
        cloudContext.addParam("restore", restoreLoopFlag);
        return JSON;
    }

    /**
     * 迁移线程
     * 
     * @author CloudKing
     */
    private class MigrateLoopThread extends Thread {
        /**
         * 204
         */
        private Long cr204;
        /**
         * 202
         */
        private Long cr202;

        /**
         * 
         * @param cr202
         * @param cr204
         */
        public MigrateLoopThread(Long cr202, Long cr204){
            this.cr202 = cr202;
            this.cr204 = cr204;
        }

        /**
         * override
         */
        @Override
        public void run() {
            super.run();
            migrateLoopFlag = true;
            Long currentCr = cr204;
            int errorCount = 0;
            while (migrateLoopFlag) {
                cloudContext.addParam("computeResourceId", currentCr);
                try {
                    moveVm();
                    if (!cloudContext.getSuccessIngoreWarn()) {
                        LogUtil.error(Arrays.toString(cloudContext.getErrorMsgList().toArray()));
                        errorCount++;
                        cloudContext.getErrorMsgList().clear();
                        if (errorCount > 50) {
                            return;
                        }
                    } else {
                        errorCount = 0;
                        MigrateLoopThread.sleep(30000);
                    }
                    currentCr = currentCr == cr202 ? cr204 : cr202;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtil.error(e);
                    errorCount++;
                }
            }
        }
    }

    /**
     * 备份线程
     * 
     * @author CloudKing
     */
    private class BackupLoopThread extends Thread {
        /**
         * override
         */
        @Override
        public void run() {
            super.run();
            int errorCount = 0;
            cloudContext.addParam("desc", "备份循环测试");
            while (backupLoopFlag) {
                try {
                    backup2Storage();
                    if (!cloudContext.getSuccessIngoreWarn()) {
                        LogUtil.error(Arrays.toString(cloudContext.getErrorMsgList().toArray()));
                        errorCount++;
                        cloudContext.getErrorMsgList().clear();
                        if (errorCount > 50) {
                            return;
                        }
                    } else {
                        errorCount = 0;
                    }
                    BackupLoopThread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtil.error(e);
                    errorCount++;
                }
            }
        }
    }

    /**
     * restore
     * 
     * @author CloudKing
     */
    private class RestoreLoopThread extends Thread {
        /**
         * 
         */
        @Override
        public void run() {
            super.run();
            int errorCount = 0;
            while (restoreLoopFlag) {
                try {
                    restore2Storage();
                    if (!cloudContext.getSuccessIngoreWarn()) {
                        LogUtil.error(Arrays.toString(cloudContext.getErrorMsgList().toArray()));
                        errorCount++;
                        cloudContext.getErrorMsgList().clear();
                        if (errorCount > 50) {
                            return;
                        }
                    } else {
                        errorCount = 0;
                    }
                    RestoreLoopThread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    LogUtil.error(e);
                    errorCount++;
                }
            }
        }
    }
//###################################测试代码结束
//###################################测试代码结束
//###################################测试代码结束
//###################################测试代码结束
//###################################测试代码结束
//###################################测试代码结束
//###################################测试代码结束
}
