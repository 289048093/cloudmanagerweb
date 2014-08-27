var editVirtualMachineWinTitle_ = '编辑虚拟机'
var confirmDeleteWinTitle_ = '删除虚拟机';
var confirmStartupWinTitle_ = '启动虚拟机';
var confirmShutdownWinTitle_ = '虚拟机关机';
var confirmRebootWinTitle_ = '重启虚拟机';
var confirmSuspendWinTitle_ = '挂起虚拟机';
var confirmResumeWinTitle_ = '恢复虚拟机';
var volumnManagementTitle_ = '卷管理';
var vncWinTitle_ = "启动Vnc";
var newVirtualMachineWinTitle_ = '新建虚拟机';
var backupTitle_ = "请输入备份描述：";
var restoreTitle_ = "还原虚机";
var addOrUpdateHtml_ = 'virtualMachine/addOrUpdateWindow_.jsp';
var baseURL_ = 'virtualMachineManager/virtualMachine';
var volumnManagementHtml_ = 'virtualMachine/volumnManagementWindow_.jsp';
var backupHtml_ = 'virtualMachine/backup_.jsp';
var restoreHtml_ = 'virtualMachine/restore_.jsp';
var resources_ = null;
var allSuperDomainTemplates_ = null;
var currentDomainTemplates_ = null;
var allSuperDomainMachineTypes_ = null;
var currentDomainMachineTypes_ = null;
var statusSuccessReturn_ = true; // 状态顺利获取标记位
// 当前操作的templateID
var currentId_ = 0;

/**
 * 查询虚拟机状态
 */
function queryStatusVarAjax() {
	// 拼装虚拟机Id
	var vmIds = "";
	$(".virtualMachineIdKeeper").each(function() {
				var vmId = $(this).val();
				if (vmId != null && vmId != "" && vmId != 'undefine') {
					vmIds += vmId + ",";
				}
			});
	if (!statusSuccessReturn_) {
		return;
	}
	// 发送请求
	if (vmIds != null && vmIds != "" && vmIds != 'undefine') {
		vmIds = vmIds.substring(0, vmIds.lastIndexOf(","));
		statusSuccessReturn_ = false;
		$.ajax({
					type : 'post',
					async : false,
					url : "virtualMachineManager/virtualMachine!queryVirtualMachineStatusByVmId.action",
					data : {
						"cloudContext.params.vmIds" : vmIds
					},
					success : function(data) {
						if (data.cloudContext.successIngoreWarn) {
							var results = data.cloudContext.params.result;
							if (!results || results.length == 0) {
								return;
							}
							statusSuccessReturn_ = true;
							for (var i = 0; i < results.length; i++) {
								var result = results[i];
								var vmId = result.id;
								var vmCreateFlag = result.createdFlag;
								var statusMsg = result.createdResultMsg;
								var operateFailFlag = result.operateFailFlag;
								var status = result.status;
								$(".virtualMachineIdKeeper").each(function() {
									var id = $(this).val();
									if (id == vmId) {
										var statusNode = $(this).siblings("td[class='ck_vm_img']");
										var operationNode = $(this).siblings("td[class='ck_table_operation']");
										refreshStatus(id, status, vmCreateFlag,operateFailFlag,statusMsg, statusNode,operationNode);
									}
								});
							}
						}
					}
				});
	}
}

/**
 * 刷新状态
 * 
 * @param {}
 *            vmCreateFlag 虚拟机是否创建成功
 * @param {}
 *            operateFailFlag 虚拟机是否创建成功
 * @param {}
 *            id 虚拟机Id
 * @param {}
 *            statusNode 状态节点td
 * @param {}
 *            operationNode 虚拟机操作节点
 * @param {}
 *            status 虚拟机状态
 * @param {}
 *            status 状态提示码
 */
function refreshStatus(id, status, vmCreateFlag, operateFailFlag,
		statusMsg, statusNode, operationNode) {
	var operations = "";
	var statusImg = ""; // 状态html
	if (vmCreateFlag == "vmCreateFailed") {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='"
				+ statusMsg + "'  title='" + statusMsg
				+ "'  width='14' height='14'/>";
		operations = '<a href="javascript:initVmDelete('
				+ id
				+ ')" class="ck_del" title="删除" chkRightsUrl="virtualMachineManager/virtualMachine!delete.action"></a>';
	} else if (vmCreateFlag == "vmCreating") {
		statusImg = "<img src='images/loading_media.gif' alt='创建中' title='创建中' width='14' height='14'/>";
	} else if (vmCreateFlag == "snapshotCreating") {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='正在创建快照' title='正在创建快照' width='14' height='14'/>";
	} else if (vmCreateFlag == "snapshotRestoring") {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='正在还原快照' title='正在还原快照' width='14' height='14'/>";
	} else if (vmCreateFlag == "snapshotDeleting") {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='正在删除快照' title='正在删除快照' width='14' height='14'/>";
	} else if (vmCreateFlag == "vmCreateSuccess") {
		// 创建成功的虚拟机公有操作
		operations = '<a href="javascript:initVmUpdate('
				+ id
				+ ')"	class="ck_modify" title="编辑" chkRightsUrl="virtualMachineManager/virtualMachine!update.action"></a>'
				+ '<a href="javascript:initVmDelete('
				+ id
				+ ')" class="ck_del" title="删除" chkRightsUrl="virtualMachineManager/virtualMachine!delete.action"></a>'
				+ '<a style="display: block;" href="javascript:initVolumn('
				+ id
				+ ')" class="ck_setVolumn" title="卷管理" 	chkRightsUrl="virtualMachineManager/virtualMachine!setVolumn.action"></a>'
				+ '<a style="display: block;margin-right: 3px;" href="javascript:initBackupVM('
				+ id + ')" class="ck_vm_bakup" title="备份"></a>'
				+ '<a style="display: block;" href="javascript:initRestoreVM('
				+ id + ')" class="ck_vm_restore" title="还原"></a>';
		if (status == "defined") {
			statusImg = "<img src='images/ck_vm_status_defined.png' alt='已定义' title='已定义' width='14' height='14'/>";
			operations += '<a style="display: block;" class="ck_vm_start" href="javascript:initStartup('
					+ id + ')" title="启动"></a>';
		} else if (status == "undefine") {
			statusImg = "<img src='images/ck_vm_status_undefine.png' alt='未定义' title='未定义' width='14' height='14'/>";
		} else if (status == "running") {
			statusImg = "<img src='images/ck_vm_status_running.png' alt='运行中' title='运行中' width='14' height='14'/>";
			operations += '<a style="display: block;" class="ck_vm_reboot" href="javascript:initReboot('
					+ id
					+ ')"  title="重启"></a>'
					+ '<a style="display: block;" class="ck_vm_suspend" href="javascript:initSuspend('
					+ id
					+ ')" title="挂起"></a>'
					+ '<a style="display: block;" class="ck_vm_halt" href="javascript:initShutdown('
					+ id
					+ ')"  title="关机"></a>'
					+ '<a style="display: block;" href="javascript:initVnc('
					+ id + ')"  class="ck_vm_vnc" title="远程连接"></a>';
		} else if (status == "paused") {
			statusImg = "<img src='images/ck_vm_status_stop.png' alt='已挂起' title='已挂起' width='14' height='14'/>";
			operations += '<a style="display: block;" href="javascript:initResume('
					+ id
					+ ')" class="ck_resume" title="恢复"></a>'
					+ '<a style="display: block;" href="javascript:initVnc('
					+ id + ')" class="ck_vm_vnc" title="远程连接"></a>';
		} else if (status == "shutoff") {
			statusImg = "<img src='images/ck_vm_status_shutoff.png' alt='已关机' title='已关机' width='14' height='14'/>";
			operations += '<a style="display: block;" class="ck_vm_start" href="javascript:initStartup('
					+ id + ')" title="启动"></a>';
		} else if (status == "unknow") {
			statusImg = "<img src='images/ck_vm_status_unknow.png' alt='虚拟机状态不正确，或者计算节点连接失败，请联系管理员' title='虚拟机状态不正确，或者计算节点连接失败，请联系管理员' width='14' height='14'/>";
		}
		
		if (operateFailFlag) {
			operations += '<img src="images/ck_vm_snapshot_failed.png" width="14 height="14" style="float: left;" title="'
					+ statusMsg + '" alt="' + statusMsg + '" />';
		}
	}
	// 刷新节点
	statusNode.html(statusImg);
	operationNode.html(operations);
	// 显示有权限的按钮
	$("[chkRightsUrl]").each(function(i) {
				if (rightsUrls.indexOf($(this).attr("chkRightsUrl")) != -1) {
					$(this).css("display", "block");
				}
			});
}

/**
 * 初始化删除
 */
function initVmDelete(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmDeleteWinTitle_,
				content : "是否确认删除?"
			});
}
/**
 * 初始化启动
 */
function initStartup(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmStartupWinTitle_,
				content : "是否确认启动?"
			});
}
/**
 * 初始化关机
 */
function initShutdown(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmShutdownWinTitle_,
				content : "是否确认关机?"
			});
}
/**
 * 初始化vnc
 */
function initVnc(id) {
	currentId_ = id;
	vnc();
}

/**
 * 初始化重启
 */
function initReboot(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmRebootWinTitle_,
				content : "是否确认重启?"
			});
}

/**
 * 初始化挂起
 */
function initSuspend(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmSuspendWinTitle_,
				content : "是否确认挂起?"
			});
}
/**
 * 初始化恢复
 */
function initResume(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : confirmResumeWinTitle_,
				content : "是否确认恢复?"
			});
}
/**
 * 备份系统
 * 
 * @param {}
 *            id
 */
function initBackupVM(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : backupTitle_,
				url : backupHtml_
			}, function() {
			});
}
/**
 * 还原系统
 * 
 * @param {}
 *            id
 */
function initRestoreVM(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : restoreTitle_,
				url : restoreHtml_,
				noButton : true
			}, initRestoreWindow);
}
/**
 * 初始化还原系统窗口
 */
function initRestoreWindow() {
	$.ajax({
				type : 'post',
				async : false,
				url : baseURL_ + "!initRestore.action",
				data : {
					"cloudContext.vo.id" : currentId_
				},
				success : function(data) {
					var snapshots = data.cloudContext.params.snapshots;
					addBackupDataToRestore(snapshots);
				}
			});
}
/**
 * 将备份信息加载到窗口
 */
function addBackupDataToRestore(snapshots) {
	if (!snapshots || snapshots.length == 0) {
		$('#vmSnapshots').append("<tr><td  colspan='3'>当前虚拟机无备份记录</td></tr>")
		return;
	}
	for (var i in snapshots) {
		var snapshot = snapshots[i];
		var operateDesc = (snapshot.operateFlag == null
				? "正常"
				: snapshot.operateFlag);
		var btn = "";
		if (snapshot.operateFlag == null) {// 为空说明状态正常
			btn = "<a href='javascript:;' onclick='restoreVM("
					+ snapshot.id
					+ ")'>还原</a>&nbsp;<a href='javascript:;' onclick='deleteSnapshot("
					+ snapshot.id + ")'>删除</a>";
		}
		$('#vmSnapshots').append("<tr><td>" + snapshot.addTime + "</td><td>"
				+ snapshot.desc + "</td><td>" + operateDesc + "</td><td>" + btn
				+ "</td></tr>");
	}
}
/**
 * 初始化更新
 */
function initVmUpdate(id) {
	currentId_ = id;
	ck_pop_win({
				width : 400,
				title : editVirtualMachineWinTitle_,
				url : addOrUpdateHtml_
			}, initAddOrUpdateWindow_);
}

/**
 * 初始化卷管理
 * 
 * @param vmId :
 *            当前虚拟机Id
 */
function initVolumn(vmId) {
	ck_pop_win({
				width : 400,
				title : volumnManagementTitle_,
				url : volumnManagementHtml_,
				submitId : "saveVolumnChange"
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : baseURL_ + "!queryVolumnByVmId.action",
					data : {
						"cloudContext.params.vmId" : vmId
					},
					success : function(data) {
						if (data.cloudContext.successIngoreWarn) {
							var volumnList = data.cloudContext.params.volumns;
							if (volumnList == 0) {
								return;
							}
							var volumnHtml = "";
							for (var i = 0; i < volumnList.length; i++) {
								var opHtml = "<a href='javascript:void(0)' onclick='delVolumn("
										+ volumnList[i].id
										+ ")'  title='删除' >删除</a>";
								if (volumnList[i].imageVolumnFlag == '1') {
									opHtml = "";
								}
								var imageVolumDisable = volumnList[i].imageVolumnFlag == '1'
										? " disabled='disabled' "
										: "";
								volumnHtml += '<tr volumnId='
										+ volumnList[i].id + '>' + '<td>'
										+ volumnList[i].name + '</td>' + '<td>'
										+ volumnList[i].size + '</td>' + '<td>'
										+ opHtml + '</td>' + '</tr>';
							}
							$("#addVolumnForm tbody").html(volumnHtml);
						} else {
							var errorList = data.cloudContext.errorMsgList;
							var errorStr = "";
							for (var i = 0; i < errorList.length; i++) {
								errorStr += errorList[i];
							}
							alert(errorStr);
						}
					}
				});

				$("#addVolumn").click(function() {
							$("#addVolumns_wrapper").show();
							$("#currentVolumns").hide();
						});
				$("#currentVolumnAddOp").click(function() {
							$("#volumnSize").val("");
							$("#addVolumns_wrapper").hide();
							$("#currentVolumns").show();
						});
				$("#saveVolumnChange").click(function() {
							addVolumn(vmId);
						});

			});
}

/**
 * 添加虚拟机卷
 * 
 * @param
 */
function addVolumn(vmId) {

	var addVolumnSize = $("#volumnSize").val();
	if (addVolumnSize == null || addVolumnSize == ""
			|| addVolumnSize == 'undefined') {
		alert("请输入添加卷大小");
		return;
	} else {
		if (!/^[0-9]+$/.test(addVolumnSize)) {
			alert("卷大小格式错误");
			return;
		}
	}

	$.ajax({
				async : false,
				url : baseURL_ + "!addVolumn.action",
				data : {
					"cloudContext.params.vmId" : vmId,
					"cloudContext.params.volumnSize" : addVolumnSize
				},
				success : function(data) {
					if (data.cloudContext.successIngoreWarn) {
						alert("添加成功");
						location.reload();
					} else {
						var errorMsgList = data.cloudContext.errorMsgList;
						var errorStr = "";
						for (var i = 0; i < errorMsgList.length; i++) {
							errorStr += errorMsgList[i];
						}
						alert(errorStr);
					}
				}
			});
}

/**
 * 删除虚拟机卷
 * 
 * @param
 */
function delVolumn(volumnId) {
	// AJAX获取数据
	$.ajax({
				async : false,
				url : baseURL_ + "!deleteVolumn.action",
				data : {
					"cloudContext.params.volumnId" : volumnId
				},
				success : function(data) {
					if (data.cloudContext.successIngoreWarn) {
						$("#addVolumnForm tbody tr[volumnId=" + volumnId + "]")
								.remove();
					} else {
						var errorMsgList = data.cloudContext.errorMsgList;
						var errorStr = "";
						for (var i = 0; i < errorMsgList.length; i++) {
							errorStr += errorMsgList[i];
						}
						alert(errorStr);
					}
				}
			});
}
/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow_() {
	// 设置名字是否可以修改
	var url = baseURL_ + '!initAddOrUpdate.action';
	// 修改
	if (isUpdate_()) {
		// 名字不能改
		$('#name').attr("disabled", true);
		$('#templateID').attr("disabled", true);
		$('#machineTypeID').attr("disabled", true);
		$('#netWork').attr("disabled", true);
		$(".ck_radio").attr("disabled", true);
		url += "?cloudContext.vo.id=" + currentId_
				+ "&cloudContext.params.updateFlag=true";
	}
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		success : function(data) {
			// 模板
			currentDomainTemplates_ = data.cloudContext.params.templates;
			if (currentDomainTemplates_) {
				for (var i = 0; i < currentDomainTemplates_.length; i++) {
					$("#templateID").append("<option value='"
							+ currentDomainTemplates_[i].id + "'>"
							+ currentDomainTemplates_[i].name + "</option>");
				}
			}
			$("#templateID").append("<option value='-1'>关联域所有模版</option>");
			// 配置
			currentDomainMachineTypes_ = data.cloudContext.params.machineTypes;
			if (currentDomainMachineTypes_) {
				for (var i = 0; i < currentDomainMachineTypes_.length; i++) {
					$("#machineTypeID")
							.append("<option cpu='"
									+ currentDomainMachineTypes_[i].cpu
									+ "' memory='"
									+ currentDomainMachineTypes_[i].memory
									+ "' value='"
									+ currentDomainMachineTypes_[i].id + "'>"
									+ currentDomainMachineTypes_[i].name
									+ "  |Cpu= "
									+ currentDomainMachineTypes_[i].cpu
									+ " |Memory= "
									+ currentDomainMachineTypes_[i].memory
									+ "</option>");
				}
			}
			$("#machineTypeID").append("<option value='-1'>关联域所有配置</option>");
			// 网络
			var netWorks = data.cloudContext.params.netWorks;
			if (netWorks.length == 0) {
				ck_message({
							type : 'warn',
							text : '没有网络,请先添加网络！',
							renderTo : 'ck_message_container',
							timeOut : 3000
						});
				ck_remove_win();
				return;
			}
			for (var i = 0; i < netWorks.length; i++) {
				$("#netWork").append("<option value='" + netWorks[i].id + "'>"
						+ netWorks[i].name + "</option>");
			}

			// 更新就存放值
			if (isUpdate_()) {
				var dataVo = data.cloudContext.params.dataVo;
				$('#id').val(dataVo.id);
				$('#name').val(dataVo.name);
				$('#desc').val(dataVo.desc);
				$('#templateID').val(data.cloudContext.params.templateID);
				$('#machineTypeID').val(data.cloudContext.params.machineTypeID);
				$('#netWork').val(data.cloudContext.params.netWorkID);
			}
			if (isAdd()) {
				var machineRooms = data.cloudContext.params.machineRooms;
				for (var i in machineRooms) {
					$('#winMachineRoomID').append('<option value="'
							+ machineRooms[i].id + '">' + machineRooms[i].name
							+ '</option>');
				}
			}
		}
	});
} 

/**
 * vnc
 */
function isVnc() {
	var title = $('.ck_pop_win_title').html();
	return title == vncWinTitle_;
}

/**
 * 是否修改
 */
function isVmUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editVirtualMachineWinTitle_;
}
/**
 * 是否删除
 */
function isVmDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle_;
}
/**
 * 关机
 * 
 * @return {}
 */
function isShutdown() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmShutdownWinTitle_;
}
/**
 * 开机
 * 
 * @return {}
 */
function isStartup() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmStartupWinTitle_;
}
/**
 * 挂起
 * 
 * @return {}
 */
function isSuspend() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmSuspendWinTitle_;
}
/**
 * 恢复
 * 
 * @return {}
 */
function isResume() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmResumeWinTitle_;
}
/**
 * 重启
 * 
 * @return {}
 */
function isReboot() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmRebootWinTitle_;
}
/**
 * 是否是备份系统
 * 
 * @return {}
 */
function isBackup() {
	return $('.ck_pop_win_title').html() == backupTitle_;
}
/**
 * 是否是还原虚机
 * 
 * @return {}
 */
function isRestore() {
	return $('.ck_pop_win_title').html() == restoreTitle_;
} 

/**
 * 编辑模版操作
 */
function updateVirtualMachine() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL_ + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteVirtualMachine() {
	location = baseURL_ + '!delete.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 启动
 */
function startupVirtualMachine() {
	location = baseURL_ + '!startup.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 关机
 */
function shutdownVirtualMachine() {
	location = baseURL_ + '!shutdown.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 重启
 */
function rebootVirtualMachine() {
	location = baseURL_ + '!reboot.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 挂起
 */
function suspendVirtualMachine() {
	location = baseURL_ + '!suspend.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 恢复
 */
function resumeVirtualMachine() {
	location = baseURL_ + '!resume.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	ck_showProcessingImg();
}
/**
 * 远程连接
 */
function vnc() {
	if (!supportHtml5()) {
		alert("VNC必须使用支持HTML5的浏览器，建议用Chrome或FireFox!");
		return;
	}
	var url = baseURL_ + '!vnc.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId_;
	var form = $("<form method='post' style='display:none' target='_blank' action='"
			+ url + "'> </form>");
	form.appendTo("body");
	form.submit();
}
  

// 查询计算节点
$(".ck_radio").live("change", function() {
			var manualFlag = $(this).val();
			if (manualFlag == "true") {
				queryResourcePool();
			} else {
				$("#addOrUpdateForm .ck_hidden").hide();
				$("#computeResourcePool").empty();
				$("#computeResourceNode").empty();
			}
		});
$("#computeResourcePool").live("change", function() {
	var poolId = $(this).val();
	if (poolId.length > 0) {
		var machineType = $("#machineTypeID").val();
		if (machineType.length > 0) {
			var optionNode = $("#machineTypeID option[value=" + machineType
					+ "]");
			var cpu = optionNode.attr("cpu");
			var memory = optionNode.attr("memory");
			if (cpu.length > 0 && memory.length > 0) {
				queryResourceByPoolAndCpuMemory(poolId, cpu, memory);
			}
		} else {
			alert("请选择配置后再操作!");
		}
	}
});

function queryResourcePool() {
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : baseURL_ + "!queryResourcePoolByCurrentDomain.action",
		success : function(data) {
			var computeResourcePools = data.cloudContext.params.computeResourcePools;
			if (computeResourcePools.length == 0) {
				return;
			}
			$("#computeResourcePool").empty();
			$("#computeResourcePool")
					.append("<option value=''>--请选择--</option>");
			for (var i = 0; i < computeResourcePools.length; i++) {
				$("#computeResourcePool").append("<option value='"
						+ computeResourcePools[i].id + "'>"
						+ computeResourcePools[i].name + "</option>");
			}
		}
	});
	$("#computeResourcePoolTr").toggle();
}

// 获取指定资源池下的计算节点，并且cpu内存参数大于cpu,memeory
function queryResourceByPoolAndCpuMemory(poolId, cpu, memory) {
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : baseURL_ + "!queryResourceByPoolAndCpuMemory.action",
				data : {
					"cloudContext.params.poolId" : poolId,
					"cloudContext.params.cpu" : cpu,
					"cloudContext.params.memory" : memory
				},
				success : function(data) {
					var computeResources = data.cloudContext.params.computeResources;
					if (computeResources.length == 0) {
						return;
					}
					$("#computeResourceNode").empty();
					$("#computeResourceNode")
							.append("<option value=''>--请选择--</option>");
					for (var i = 0; i < computeResources.length; i++) {
						$("#computeResourceNode").append("<option value='"
								+ computeResources[i].id + "'>"
								+ computeResources[i].name + "</option>");
					}
				}
			});
	$("#computeResourceNodeTr").toggle();
}
/**
 * 备份系统
 */
function backupVM() {
	var form = $("#backupForm");
	form.attr('action', baseURL_ + '!backup.action');
	$("#backupVMId").val(currentId_);
	form.submit();
	ck_showProcessingImg();
}
/**
 * 还原系统
 */
function restoreVM(snapshotId) {
	location = baseURL_ + '!restore.action?cloudContext.vo.id=' + currentId_
			+ '&cloudContext.params.snapshotId=' + snapshotId;
	ck_showProcessingImg();
}
/**
 * 删除快照
 */
function deleteSnapshot(snapshotId) {
	location = baseURL_
			+ "!deleteSnapshot.action?cloudContext.params.snapshotId="
			+ snapshotId + "&cloudContext.vo.id=" + currentId_;
	ck_showProcessingImg();
}
/**
 * 模版选择框切换
 * 
 * @param {}
 *            val
 */
function changeTemplate(val) {
	if (val == '-1') {
		if (!allSuperDomainTemplates_)
			$.ajax({
				async : false,
				url : baseURL_ + "!queryAllSuperDomainTemplate.action",
				success : function(data) {
					allSuperDomainTemplates_ = data.cloudContext.params.parentsDomainTemplate;
				}
			});
		$("#templateID").empty();
		$("#templateID").append("<option value=''>--请选择--</option>");
		for (var i in allSuperDomainTemplates_) {
			$("#templateID").append("<option value='"
					+ allSuperDomainTemplates_[i].id + "'>"
					+ allSuperDomainTemplates_[i].name + "</option>");
		}
		$("#templateID").append("<option value='-2'>当前域模版</option>");
	} else if (val == '-2') {
		$("#templateID").empty();
		$("#templateID").append("<option value=''>--请选择--</option>");
		for (var i in currentDomainTemplates_) {
			$("#templateID").append("<option value='"
					+ currentDomainTemplates_[i].id + "'>"
					+ currentDomainTemplates_[i].name + "</option>");
		}
		$("#templateID").append("<option value='-1'>所有上级域模版</option>");
	}
}
/**
 * 配置切换
 * 
 * @param {}
 *            val
 */
function changeMachineType(val) {
	if (val == '-1') {
		if (!allSuperDomainMachineTypes_)
			$.ajax({
				async : false,
				url : baseURL_ + "!queryAllSuperDomainMachineTypes.action",
				success : function(data) {
					allSuperDomainMachineTypes_ = data.cloudContext.params.parentsDomainMachineTypes;
				}
			});
		$("#machineTypeID").empty();
		$("#machineTypeID").append("<option value=''>--请选择--</option>");
		for (var i in allSuperDomainMachineTypes_) {
			$("#machineTypeID").append("<option cpu='"
					+ allSuperDomainMachineTypes_[i].cpu + "' memory='"
					+ allSuperDomainMachineTypes_[i].memory + "' value='"
					+ allSuperDomainMachineTypes_[i].id + "'>"
					+ allSuperDomainMachineTypes_[i].name + "  |Cpu= "
					+ allSuperDomainMachineTypes_[i].cpu + " |Memory= "
					+ allSuperDomainMachineTypes_[i].memory + "</option>");
		}
		$("#machineTypeID").append("<option value='-2'>当前域配置</option>");
	} else if (val == '-2') {
		$("#machineTypeID").empty();
		$("#machineTypeID").append("<option value=''>--请选择--</option>");
		for (var i in currentDomainMachineTypes_) {
			$("#machineTypeID").append("<option cpu='"
					+ currentDomainMachineTypes_[i].cpu + "' memory='"
					+ currentDomainMachineTypes_[i].memory + "' value='"
					+ currentDomainMachineTypes_[i].id + "'>"
					+ currentDomainMachineTypes_[i].name + "  |Cpu= "
					+ currentDomainMachineTypes_[i].cpu + " |Memory= "
					+ currentDomainMachineTypes_[i].memory + "</option>");
		}
		$("#machineTypeID").append("<option value='-1'>所有上级域配置</option>");
	}
}