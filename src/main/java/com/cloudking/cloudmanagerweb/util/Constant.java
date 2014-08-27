/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

/**
 * 常量
 * 
 * @author CloudKing
 * 
 */
public interface Constant {
    /**
     * 登陆用户的信息
     */
    String LOGINED_USER = "userLogin";
    /**
     * 当前登录的用户所属的域
     */
    String LOGINED_USER_DOMAINS = "loginedUserDomains";
    /**
     * 验证码
     */
    String VERIFY_CODE = "verifyCode";
    /**
     * 用户正常状态
     */
    String USER_NORMAL_STATE = "1";
    /**
     * 用户删除状态
     */
    String USER_DELETE_STATE = "0";
    /**
     * 表示正常
     */
    Integer TEMPLATE_STATUS_OK = 1;
    /**
     * 表示正在下载文件
     */
    Integer TEMPLATE_STATUS_DOWNLOADING = 2;
    /**
     * 表示下载失败。
     */
    Integer TEMPLATE_STATUS_DOWNLOAD_ERROR = -2;
    /**
     * 本地
     */
    Integer TEMPLATE_TYPE_LOCAL = 1;
    /**
     * 下载。
     */
    Integer TEMPLATE_TYPE_DOWNLOAD = 2;

    /**
     * 根域code
     */
    String ROOT_DOMAIN_CODE = "00";
    /**
     * 根域id
     */
    Long ROOT_DOMAIN_ID = 1L;
    /**
     * portal 用户名加盐串
     */
    String PORTAL_USERNAME_SALT = "portal_username_salt";

    /**
     * 处理中的计算资源工单
     */
    String RESOURCEORDER_ACCEPING = "1";
    /**
     * 同意计算资源工单
     */
    String RESOURCEORDER_AGREE = "2";
    /**
     * 拒绝计算资源工单
     */
    String RESOURCEORDER_REJECT = "3";

    /**
     * 工单序列号格式
     */
    String RESOURCEORDER_SERIALIZABLE_NUMBER = "'RO'_yyyyMMdd_HHmmss";
    /**
     * 映像卷标记位 - 是
     */
    String IMAGE_VOLUMN_FALG_TRUE = "1";
    /**
     * 映像卷标记位 - 否
     */
    String IMAGE_VOLUMN_FALG_FALSE = "2";
    /**
     * 正在创建快照
     */
    String CREATE_SNAPSHOT_FLAG = "snapshotCreating";
    /**
     * 正在还原快照
     */
    String RESTORE_SNAPSHOT_FLAG = "snapshotRestoring";
    /**
     * 正在删除快照
     */
    String DELETE_SNAPSHOT_FLAG = "snapshotDeleting";
    /**
     * 虚拟机创建中
     */
    String VM_CREATING = "vmCreating";
    /**
     * 虚拟机创建成功
     */
    String VM_CREATE_SUCCESS = "vmCreateSuccess";
    /**
     * 虚拟机创建失败
     */
    String VM_CREATE_FAILED = "vmCreateFailed";
    /**
     * 订单过期类型 - 永不过期
     */
    String PORTAL_ORDER_DUETIME_TYPE_NEVERDUE = "1";
    /**
     * 订单过期类型 - 30天
     */
    String PORTAL_ORDER_DUETIME_TYPE_MONTH = "2";
    /**
     * 订单过期类型 - 360天
     */
    String PORTAL_ORDER_DUETIME_TYPE_YEAR = "3";
    /**
     * 管理员用户名
     */
    String ADMINISTRATOR = "admin";
    /**
     * 首页表格展示行数据
     */
    int DASHBOARDDATACOUNT = 5;
    /**
     * 虚拟机—容错
     */
    Integer VM_FAULT_TOLERANT = 1;

    /**
     * 模版容错机制
     */
    Integer TEMPLATE_FAULT_TOTERANT = 2;
    /**
     * 虚拟机订单回复标题
     */
    String PORTAL_VM_EMAIL_SUBJECT = "云平台订单申请回复通知";
    /**
     * 工单类型Id - 新建虚拟机
     */
    Long WORK_ORDER_CATEGORY_NEW_VM = 1L;
    /**
     * 工单类型Id - 修改虚拟机
     */
    Long WORK_ORDER_CATEGORY_UPDATE_VM = 2L;
    /**
     * 工单类型Id - 删除虚拟机
     */
    Long WORK_ORDER_CATEGORY_DELETE_VM = 3L;
    /**
     * 工单类型Id - 新建资源申请单
     */
    Long WORK_ORDER_CATEGORY_NEW_RESOURCEORDER = 4L;
    /**
     * 工单类型Id - 日常工单
     */
    Long WORK_ORDER_CATEGORY_COMMON = 5L;

    /**
     * 处理中工单
     */
    String WORKORDER_ACCEPING = "1";
    /**
     * 已处理工单
     */
    String WORKORDER_SOLVED = "2";
    /**
     * 关闭工单
     */
    String WORKORDER_CLOSED = "3";
    /**
     * 虚拟机操作状态，快照创建中；
     */
    String VM_OPERATE_SNAPSHOT_CREATING = "1";
    
    /**
     * 虚拟机操作状态：快照还原中
     */
    String VM_OPERATE_SNAPSHOT_RESTORING = "2";
    /**
     * 虚拟机操作状态:快照删除中
     */
    String VM_OPRATE_SNAPSHOT_DELETING = "3";
    /**
     * 虚拟机迁移中
     */
    String VM_OPERATE_MIGRATING = "4";
    /**
     * 虚拟机正常状态提示信息
     */
    String VM_NORMAL_MSG = "虚拟机正常状态!";
    /**
     * 虚拟机正常状态提示信息
     */
    String VM_BACKUP_NORMAL_MSG = "正常";
    /**
     * 虚拟机正常状态提示信息
     */
    String VM_BACKUP_FAIL_MSG = "备份错误";
    /**
     * 虚拟机迁移失败提示信息
     */
    String VM_MIGRATE_FAIL_MSG = "虚拟机迁移失败!";
    /**
     * 虚拟机正在迁移提示信息
     */
    String VM_MIGRATING_MSG = "虚拟机正在迁移!";
    
    /**
     * 正在创建备份
     */
    String CREATE_BACKUP_FLAG = "backupCreating";
    /**
     * 正在还原备份
     */
    String RESTORE_BACKUP_FLAG = "backupRestoring";
    /**
     * 正在删除备份
     */
    String DELETE_BACKUP_FLAG = "backupDeleting";
}
