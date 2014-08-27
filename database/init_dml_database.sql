

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user`(ID_,PASSWORD_,USERNAME_,REALNAME_,DELETEFLAG_,ADDTIME_) VALUES(1,'a66abb5684c45962d887564f08346e8d','admin','系统管理员',0,'2012-01-01'); 

-- ----------------------------
-- Records of tb_menu
-- ----------------------------

INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('1', '主页', '主页', 'dashboardManager/dashboard!query.action', 'images/ck_dashboard.png', '000');

INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('2', '基础设施管理', '基础设施管理','', '', '001');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('3', '机房', '机房', 'machineRoomManager/machineRoom!query.action', 'images/ck_room.png', '001000');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('4', '机架', '机架', 'machineRackManager/machineRack!query.action', 'images/ck_rack.png', '001001');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('17', '网络', '网络', 'netWorkManager/netWork!query.action', 'images/ck_network.png', '001002');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('5', '计算节点', '计算节点', 'computeResourceManager/computeResource!query.action', 'images/ck_res.png', '001003');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('6', '存储资源', '存储资源',  'storageResourceManager/storageResource!query.action', 'images/ck_infra.png', '001004');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('7', '备份存储', '备份存储',  'backupStorageManager/backupStorage!query.action', 'images/ck_infra.png', '001005');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('8', '计算节点池', '计算节点池', 'computeResourcePoolManager/computeResourcePool!query.action', 'images/ck_host.png', '001006');
		

INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('9', '应用管理', '应用管理','', '', '002');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('10', '域', '域',  'domainManager/domain!query.action', 'images/ck_project.png', '002001');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('11', '虚拟机模板', '虚拟机模板', 'templateManager/template!query.action', 'images/ck_template.png', '002002');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('12', '虚拟机配置', '虚拟机配置', 'machineTypeManager/machineType!query.action', 'images/ck_machineType.png', '002003');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('13', '虚拟机管理', '虚拟机管理', 'virtualMachineManager/virtualMachine!query.action', 'images/ck_instance.png', '002004');

INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('14', '系统管理', '系统管理','', '', '003');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('15', '用户', '用户', 'userManager/user!query.action', 'images/ck_account.png', '003001');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('16', '全局属性设置', '全局属性设置', 'syscfgManager/syscfg!query.action', 'images/ck_setting.png', '003002');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('20', '操作记录', '操作记录', 'eventLogManager/eventLog!query.action', 'images/ck_account.png', '003003');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('21', '报警记录', '报警记录', 'warnLogManager/warnLog!query.action', 'images/ck_account.png', '003004');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('22', '我接收的工单', '我接收的工单', 'receiveWorkorderManager/receiveWorkorder!queryReceiveWorkOrder.action', 'images/ck_order_receive.png', '003005');
		INSERT INTO `tb_menu`(ID_,DESC_,NAME_,URL_,IMG_,CODE_)  VALUES ('23', '我发送的工单', '我发送的工单', 'sendWorkorderManager/sendWorkorder!querySendWorkOrder.action', 'images/ck_order_send.png', '003006');
-- ----------------------------
-- Records of tb_domain
-- ----------------------------
INSERT INTO `tb_domain`(ID_,ADDTIME_,AVAILABLESTORAGECAPACITY_,CODE_,CPU_AVAILABEL_NUM_,CPU_TOTAL_NUM_,DESC_,MEMORY_AVAILABLE_CAPACITY_,MEMORY_CAPACITY_,NAME_,STORAGECAPACITY_,BACKUPSTORAGE_CAPACITY_,AVAILABLE_BACKUPSTORAGE_CAPACITY_,USER_ID_) VALUES ('1', '2012-11-01 09:45:58', '0', '00', '0', '0', '根域', '0', '0', '云景云平台', '0','0','0', '1');
-- ----------------------------
-- Records of tb_domain_bid_user
-- ----------------------------


-- ----------------------------
-- Records of tb_domain_bid_menu
-- ----------------------------
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '1');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '2');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '3');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '4');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '5');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '6');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '7');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '8');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '9');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '10');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '11');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '12');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '13');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '14');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '15');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '16');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '17');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '20');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '21');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '22');
INSERT INTO `tb_domain_bid_menu`(DOMAIN_ID_,MENU_ID_) VALUES ('1', '23');

-- ----------------------------
-- Records of tb_rights
-- ---------------------------- 
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('1', '浏览信息面板', 'query', 'dashboardManager/dashboard!query.action', '1');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('2', '浏览机房', '浏览机房', 'machineRoomManager/machineRoom!query.action', '3');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('3', '添加机房', '添加机房', 'machineRoomManager/machineRoom!add.action', '3');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('4', '更新机房', '更新机房', 'machineRoomManager/machineRoom!update.action', '3');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('5', '删除机房', '删除机房', 'machineRoomManager/machineRoom!delete.action', '3');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('6', '浏览机柜', '浏览机柜', 'machineRackManager/machineRack!query.action', '4');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('7', '添加机柜', '添加机柜', 'machineRackManager/machineRack!add.action', '4');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('8', '更新机柜', '更新机柜', 'machineRackManager/machineRack!update.action', '4');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('9', '删除机柜', '删除机柜', 'machineRackManager/machineRack!delete.action', '4');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('10', '浏览计算节点', '浏览计算节点', 'computeResourceManager/computeResource!query.action', '5');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('11', '添加计算节点', '添加计算节点', 'computeResourceManager/computeResource!add.action', '5');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('12', '更新计算节点', '更新计算节点', 'computeResourceManager/computeResource!update.action', '5');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('13', '删除计算节点', '删除计算节点', 'computeResourceManager/computeResource!delete.action', '5');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('14', '浏览存储资源', '浏览存储资源', 'storageResourceManager/storageResource!query.action', '6');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('15', '添加存储资源', '添加存储资源', 'storageResourceManager/storageResource!add.action', '6');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('16', '更新存储资源', '更新存储资源', 'storageResourceManager/storageResource!update.action', '6');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('17', '删除存储资源', '删除存储资源', 'storageResourceManager/storageResource!delete.action', '6');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('18', '浏览计算节点池', '浏览计算节点池', 'computeResourcePoolManager/computeResourcePool!query.action', '8');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('19', '添加计算节点池', '添加计算节点池', 'computeResourcePoolManager/computeResourcePool!add.action', '8');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('20', '更新计算节点池', '更新计算节点池', 'computeResourcePoolManager/computeResourcePool!update.action', '8');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('21', '删除计算节点池', '删除计算节点池', 'computeResourcePoolManager/computeResourcePool!delete.action', '8');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('22', '浏览网络', '浏览网络', 'netWorkManager/netWork!query.action', '17');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('23', '添加网络', '添加网络', 'netWorkManager/netWork!add.action', '17');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('24', '更新网络', '更新网络', 'netWorkManager/netWork!update.action', '17');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('25', '删除网络', '删除网络', 'netWorkManager/netWork!delete.action', '17');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('26', '浏览域', '浏览域', 'domainManager/domain!query.action', '10');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('27', '添加域', '添加域', 'domainManager/domain!add.action', '10');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('28', '更新域', '更新域', 'domainManager/domain!update.action', '10');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('29', '删除域', '删除域', 'domainManager/domain!delete.action', '10');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('30', '浏览模板', '浏览模板', 'templateManager/template!query.action', '11');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('31', '添加模板', '添加模板', 'templateManager/template!add.action', '11');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('32', '更新模板', '更新模板', 'templateManager/template!update.action', '11');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('33', '删除模板', '删除模板', 'templateManager/template!delete.action', '11');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('34', '浏览配置', '浏览配置', 'machineTypeManager/machineType!query.action', '12');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('35', '添加配置', '添加配置', 'machineTypeManager/machineType!add.action', '12');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('36', '更新配置', '更新配置', 'machineTypeManager/machineType!update.action', '12');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('37', '删除配置', '删除配置', 'machineTypeManager/machineType!delete.action', '12');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('38', '浏览虚拟机', '浏览虚拟机', 'virtualMachineManager/virtualMachine!query.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('39', '添加虚拟机', '添加虚拟机', 'virtualMachineManager/virtualMachine!add.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('40', '更新虚拟机', '更新虚拟机', 'virtualMachineManager/virtualMachine!update.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('41', '删除虚拟机', '删除虚拟机', 'virtualMachineManager/virtualMachine!delete.action', '13');
 
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('42', '浏览用户', '浏览用户', 'userManager/user!query.action', '15');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('43', '添加用户', '添加用户', 'userManager/user!add.action', '15');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('44', '更新用户', '更新用户', 'userManager/user!update.action', '15');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('45', '删除用户', '删除用户', 'userManager/user!delete.action', '15');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('46', '设置用户', '设置用户', 'domainManager/domain!updateDomainUser.action', '15');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('47', '浏览系统配置', '浏览系统配置', 'syscfgManager/syscfg!query.action', '16');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('48', '更新系统配置', '更新系统配置', 'syscfgManager/syscfg!update.action', '16');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('61', '虚拟机卷管理', '虚拟机卷管理', 'virtualMachineManager/virtualMachine!addVolumn.action', '13');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('62', '计算节点池设置计算节点', '计算节点池设置计算节点', 'computeResourcePoolManager/computeResourcePool!updateComputeResources.action', '8');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('63', '域设置计算节点池', '域设置计算节点池', 'domainManager/domain!updateResourcePools.action', '10');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('64', '模版删除并删除文件', '模版删除并删除文件', 'templateManager/template!deleteWithFile.action', '11');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('65', '操作记录', '操作记录', 'eventLogManager/eventLog!query.action', '20');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('66', '完成工单', '完成工单', 'receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action', '22');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('67', '关闭工单', '关闭工单', 'receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action', '22');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('68', '上报工单', '上报工单', 'receiveWorkorderManager/receiveWorkorder!reportWorkOrder.action', '22');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('69', '下发工单', '下发工单', 'receiveWorkorderManager/receiveWorkorder!dispatchWorkOrder.action', '22');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('70', '浏览工单', '浏览工单', 'receiveWorkorderManager/receiveWorkorder!view.action', '22');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('71', '关闭工单', '关闭工单', 'sendWorkorderManager/sendWorkorder!closeWorkOrder.action', '23');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('72', '浏览工单', '浏览工单', 'sendWorkorderManager/sendWorkorder!view.action', '23');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('73', '查看我接收的工单', '查看我接收的工单', 'receiveWorkorderManager/receiveWorkorder!queryReceiveWorkOrder.action', '22');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('74', '查看我发送的工单', '查看我发送的工单', 'sendWorkorderManager/sendWorkorder!querySendWorkOrder.action', '23');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('75', '虚拟机迁移', '虚拟机迁移', 'virtualMachineManager/virtualMachine!moveVm.action', '13');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('76', '浏览备份存储', '浏览备份存储', 'backupStorageManager/backupStorage!query.action', '7');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('77', '添加备份存储', '添加备份存储', 'backupStorageManager/backupStorage!add.action', '7');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('78', '更新备份存储', '更新备份存储', 'backupStorageManager/backupStorage!update.action', '7');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('79', '删除备份存储', '删除备份存储', 'backupStorageManager/backupStorage!delete.action', '7');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('80', '备份快照虚拟机', '备份快照虚拟机', 'virtualMachineManager/virtualMachine!backup.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('81', '还原快照虚拟机', '还原快照虚拟机', 'virtualMachineManager/virtualMachine!restore.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('82', '删除快照虚拟机', '删除快照虚拟机', 'virtualMachineManager/virtualMachine!deleteSnapshot.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('83', '删除卷', '删除卷', 'virtualMachineManager/virtualMachine!deleteVolumn.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('84', '开机', '开机', 'virtualMachineManager/virtualMachine!startup.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('85', '关机', '关机', 'virtualMachineManager/virtualMachine!shutdown.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('86', '强制关机', '强制关机', 'virtualMachineManager/virtualMachine!forceShutdown.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('87', '挂起', '挂起', 'virtualMachineManager/virtualMachine!suspend.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('88', '恢复', '恢复', 'virtualMachineManager/virtualMachine!resume.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('89', '完全备份', '完全备份', 'virtualMachineManager/virtualMachine!backup2Storage.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('90', '完全备份恢复', '完全备份恢复', 'virtualMachineManager/virtualMachine!restore2Storage.action', '13');
INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('91', '删除完全备份', '删除完全备份', 'virtualMachineManager/virtualMachine!deleteBackupVmData.action', '13');

INSERT INTO `tb_rights`(ID_,DESC_,NAME_,URL_,MENU_ID_) VALUES ('92', '浏览报警记录', '浏览报警记录', 'warnLogManager/warnLog!query.action', '21');

-- ----------------------------
-- Records of tb_domain_bid_rights
-- ----------------------------
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '1');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '2');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '3');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '4');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '5');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '6');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '7');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '8');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '9');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '10');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '11');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '12');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '13');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '14');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '15');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '16');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '17');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '18');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '19');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '20');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '21');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '22');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '23');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '24');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '25');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '26');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '27');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '28');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '29');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '30');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '31');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '32');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '33');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '34');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '35');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '36');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '37');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '38');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '39');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '40');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '41');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '42');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '43');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '44');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '45');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '46');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '47');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '48');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '61');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '62');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '63');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '64');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '65');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '66');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '67');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '68');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '69');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '70');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '71');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '72');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '73');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '74');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '75');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '76');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '77');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '78');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '79');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '80');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '81');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '82');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '83');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '84');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '85');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '86');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '87');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '88');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '89');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '90');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '91');
INSERT INTO `tb_domain_bid_rights`(DOMAIN_ID_,RIGHTS_ID_) VALUES ('1', '92');

-- ----------------------------
-- Records of tb_property
-- ----------------------------
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('1', '3', '设置系统备份间隔时间(天)', 'mysql_backup_days', '^\\d{1,3}$','备份间隔时间必须小于1000', 'Integer', '3');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('2', '5', '保存mysql数据库备份的最大数量', 'mysql_backup_num', '^[1-9]$','备份次书必须为1-9之间的整数', 'Integer', '5');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('3', '0', '是否开启邮件发送功能', 'email_enable', '.*','值必须为true或false', 'Boolean', 'false');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('4', '', '邮件服务器用户名', 'email_username', '.*','', 'String', '');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('5', '', '邮件服务器密码', 'email_password', '.*','', 'String', '');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('6', '', '邮件服务器地址', 'email_host', '.*','', 'String', '');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('7', '', '邮件服务器端口', 'email_port', '^\\d{2,5}$','端口号必须为有效整数', 'Integer', '');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('8', '5', '邮件服务器发送地址', 'email_from', '[0-9a-zA-Z_\\.]+@\\w+\\.\\w+','邮箱格式不对，格式必须为xxxx@xx.xx', 'String', '');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('9', '10', '删除多少天以前的操作记录', 'delete_eventlog_days', '^\\d{1,5}$','天数必须为小于100000的数字', 'Integer', '10');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('10', '5', '工单超时上报时间(天)', 'workorder_report_days', '^([1-9]|[1-9]\\d|[1-2]\\d{2}|3[0-5]\\d|36[0-5])$','工单超时上报时间介于1-365天', 'Integer', '5');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('11', '5', '报警多少次后发送邮件给管理员', 'warn_count_send_email', '^[1-9]\\d{0-3}$','次数必须为1-10000的数字', 'Integer', '5');
INSERT INTO `tb_property`(ID_,DEFAULTVALUE_,DESC_,KEY_,REGEX_,ERRORMSG_,TYPE_,VALUE_) VALUES ('12', '10', '删除多少天以前的报警记录', 'delete_warnlog_days', '^\\d{1,5}$','天数必须为小于100000的数字', 'Integer', '10');

-- ----------------------------
-- Records of tb_workordercategory
-- ----------------------------
INSERT INTO `tb_workordercategory`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('1', '新建虚机', '新建虚机', null);
-- INSERT INTO `tb_workordercategory`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('2', '修改虚机', '修改虚机', null);
INSERT INTO `tb_workordercategory`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('3', '删除虚机', '删除虚机', null);
INSERT INTO `tb_workordercategory`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('4', '资源申请单', '资源申请单', null);
INSERT INTO `tb_workordercategory`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('5', '普通工单', '普通工单', null);

-- ----------------------------
-- 工单解决情况
-- ----------------------------
INSERT INTO `tb_workordersolution`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('1', '未解决','工单还没有解决', null);
INSERT INTO `tb_workordersolution`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('2', '已解决','工单已经解决了', null);
INSERT INTO `tb_workordersolution`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('3', '工单重复','已经有同样的工单', null);
INSERT INTO `tb_workordersolution`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('4', '工单描述不完整','工单的描述不完整', null);
INSERT INTO `tb_workordersolution`(ID_,NAME_,DESC_,BUILDIN_) VALUES ('5', '问题不能重现','工单里面的问题不能重现', null); 
