var editVirtualMachineWinTitle = '编辑虚拟机'
var confirmDeleteWinTitle = '删除虚拟机';
var confirmStartupWinTitle = '启动虚拟机';
var confirmShutdownWinTitle = '虚拟机关机';
var confirmRebootWinTitle = '重启虚拟机';
var confirmForceShutdown = '虚拟机强制关机';
var confirmSuspendWinTitle = '挂起虚拟机';
var confirmResumeWinTitle = '恢复虚拟机';
var volumnManagementTitle = '卷管理';
var downloadClientTitle = "您可能未安装客户端,是否下载客户端?";
var vncWinTitle = "启动Vnc";
var newVirtualMachineWinTitle = '新建虚拟机';
var backupTitle = "请输入备份快照描述：";
var restoreTitle = "还原快照";
var backup2StorageTitle = "请输入备份虚拟机描述";
var restore2StorageTitle = "还原虚机";
var addOrUpdateHtml = 'virtualMachine/addOrUpdateWindow_.jsp';
var baseURL = 'virtualMachineManager/virtualMachine';
var volumnManagementHtml = 'virtualMachine/volumnManagementWindow_.jsp';
var backupHtml = 'virtualMachine/backup_.jsp';
var restoreHtml = 'virtualMachine/restore_.jsp';
var allSuperDomainTemplates = null;
var currentDomainTemplates = null;
var allSuperDomainMachineTypes = null;
var currentDomainMachineTypes = null;
var statusSuccessReturn = true; // 状态顺利获取标记位
// 当前操作的templateID
var currentId = 0;
$(function() {
			$('#qRoom').change();
			$('#qRack').change();
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewVirtualMachine').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newVirtualMachineWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})
			$('#vncDownLoad').click(function() {
						location = baseURL + '!vncDownLoad.action';
					});

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (!validate()) {
							return;
						}
						if (isDelete()) {
							deleteVirtualMachine();
							return;
						}
						if (isStartup()) {
							startupVirtualMachine();
							return;
						}
						if (isShutdown()) {
							shutdownVirtualMachine();
							return;
						}
						if (isReboot()) {
							rebootVirtualMachine();
							return;
						}
						if (isSuspend()) {
							suspendVirtualMachine();
							return;
						}
						if (isResume()) {
							resumeVirtualMachine();
							return;
						}
						if (isAdd()) {
							addVirtualMachine();
							return;
						}
						if (isVnc()) {
							vnc();
							return;
						}
						if (isUpdate()) {
							updateVirtualMachine();
							return;
						}
						if (isBackup()) {
							backupVM();
							return;
						}
						if (isRestore()) {
							restoreVM();
							return;
						}
						if (isForceShutdown()) {
							forceShutdown();
							return;
						}
						if (isDownloadClient()) {
							downloadClient();
							return;
						}
						if (isMoveVm()) {
							moveVm();
							return;
						}
						if (isBackup2Storage()) {
							backup2Storage();
							return;
						}
						if (isRestore2Storage()) {
							restoreVM2Storage();
							return;
						}
					});
			$(".ck_cancel").live("click", function() {
						if (isDownloadClient()) {
							if ($('#noPrompt').attr('checked')) {
								setCookie('noPrompt', true);
							}
							return;
						}
					});
			// 延迟1秒获取状态
			setTimeout(function() {
						queryStatusVarAjax();
					}, 1000);
			setInterval(queryStatusVarAjax, 30000);
		});

/**
 * 点击更多虚拟机操作
 */
var clickId = null;
$(".ck_op_more").live("click", function() {
	var vmId = $(this).attr("vmId");
	var position = $(this).position();
	if (clickId == vmId) {
		$(".ck_vm_actual_operation[vmId=" + vmId + "]").hide();
		clickId = null;
	} else {
		$(".ck_vm_actual_operation[vmId=" + clickId + "]").hide();
		$(".ck_vm_actual_operation[vmId=" + vmId + "]").css("left",
				position.left + 30).css("top", position.top + $(this).height())
				.show();
		clickId = vmId;
	}
});

$(document).bind("click", function(e) {
			var target = $(e.target);
			if (target.closest(".ck_op_more").length == 0) {
				$(".ck_vm_actual_operation").hide();
			}
		});

/**
 * 定时发送请求获取虚拟机状态
 */
function queryStatusVarAjax() {
	// 拼装虚拟机Id
	var vmIds = "";
	$(".virtualMachineIdKeeper").each(function() {
				var vmId = $(this).val();
				if (vmId) {
					vmIds += vmId + ",";
				}
			});
	if (!vmIds && !statusSuccessReturn) {
		return;
	}
	// 发送请求
	vmIds = vmIds.substring(0, vmIds.lastIndexOf(","));
	statusSuccessReturn = false;
	$.ajax({
		type : 'post',
		async : true,
		url : baseURL + "!queryVirtualMachineStatusByVmId.action",
		data : {
			"cloudContext.params.vmIds" : vmIds
		},
		error : function(data) {
			statusSuccessReturn = true;
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				var results = data.cloudContext.params.result;
				if (!results || results.length == 0) {
					return;
				}
				statusSuccessReturn = true;
				for (var i = 0; i < results.length; i++) {
					var result = results[i];
					var vmId = result.id;
					var vmCreateFlag = result.createdFlag;
					var statusMsg = result.createdResultMsg;
					var operateFailFlag = result.operateFailFlag;
					var status = result.status;
					var computeResourceIP = result.computeResourceIP;
					var port = result.port;
					$(".virtualMachineIdKeeper").each(function() {
						var id = $(this).val();
						if (id == vmId) {
							var statusNode = $(".ck_vm_img[vmId=" + vmId + "]");
							var dropDownOpNode = $(".ck_vm_actual_operation[vmId="
									+ vmId + "]");
							var commonOpNode = $(".ck_vm_common_operation[vmId="
									+ vmId + "]");
							var statusOpFailedMsgNode = $(".ck_vm_statusMsg_wrapper[vmId="
									+ vmId + "]");
							refreshStatus(id, status, vmCreateFlag,
									operateFailFlag, statusMsg, statusNode,
									dropDownOpNode, commonOpNode,
									statusOpFailedMsgNode, computeResourceIP,
									port);
							$('#port_' + vmId).html(result.port
									? result.port
									: '--');
						}
					});
				}
			}
		}
	});
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
 *            dropDownOpNode 虚拟机操作节点
 * @param {}
 *            status 虚拟机状态
 * @param {}
 *            status 状态提示码
 */
function refreshStatus(id, status, vmCreateFlag, operateFailFlag, statusMsg,
		statusNode, dropDownOpNode, commonOpNode, statusOpFailedMsgNode,
		computeResourceIP, port) {
	var operations = "";
	var statusImg = ""; // 状态html
	var statusOpFailedMsg = "";
	if (vmCreateFlag == "vmCreateFailed") {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='"
				+ statusMsg + "'  title='" + statusMsg
				+ "'  width='14' height='14'/>";
		operations = '<a href="javascript:initDelete('
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
	} else if (vmCreateFlag == migrating) {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='正在迁移' title='正在迁移' width='14' height='14'/>";
	} else if (vmCreateFlag == backup2Storage) {
		statusImg = "<img src='images/ck_vm_status_failed.png' alt='正在备份' title='正在备份' width='14' height='14'/>";
	} else if (vmCreateFlag == "vmCreateSuccess") {
		var commonOpNodeHtml = '<a href="javascript:initUpdate('
				+ id
				+ ')" title="编辑" chkRightsUrl="virtualMachineManager/virtualMachine!update.action">编辑</a>';
		commonOpNode.html(commonOpNodeHtml);
		// 创建成功的虚拟机公有操作
		operations = '<a href="javascript:initDelete('
				+ id
				+ ')"  title="删除" chkRightsUrl="virtualMachineManager/virtualMachine!delete.action">删除</a>'
				+ '<a style="display: block;margin-right: 3px;" href="javascript:initBackupVM('
				+ id + ')"   title="创建快照">创建快照</a>'
				+ '<a style="display: block;" href="javascript:initRestoreVM('
				+ id + ')"  title="还原快照">还原快照</a>';
		if (status == "defined") {
			statusImg = "<img src='images/ck_vm_status_defined.png' alt='已定义' title='已定义' width='14' height='14'/>";
			operations += '<a style="display: block;"   href="javascript:initStartup('
					+ id + ')" title="启动">启动</a>';
		} else if (status == "undefine") {
			statusImg = "<img src='images/ck_vm_status_undefine.png' alt='未定义' title='未定义' width='14' height='14'/>";
		} else if (status == "running") {
			statusImg = "<img src='images/ck_vm_status_running.png' alt='运行中' title='运行中' width='14' height='14'/>";
			operations += '<a style="display: block;" href="javascript:initMoveVm('
					+ id
					+ ')" title="迁移" chkRightsUrl="virtualMachineManager/virtualMachine!moveVm.action">迁移</a>'
					+ '<a style="display: block;" href="javascript:initReboot('
					+ id
					+ ')"  title="重启">重启</a>'
					+ '<a style="display: block;"  href="javascript:initSuspend('
					+ id
					+ ')" title="挂起">挂起</a>'
					+ '<a style="display: block;"  href="javascript:initShutdown('
					+ id
					+ ')"  title="关机">关机</a>'
					+ '<a style="display: block;"  href="javascript:initForceShutdown('
					+ id
					+ ')"  title="强制关机">强制关机</a>'
					+ '<a style="display: block;" href="javascript:initBackup2Storage('
					+ id
					+ ')" title="备份">备份虚机</a>'
					+ '<a style="display: block;" href="javascript:initRestore2Storage('
					+ id + ')" title="还原">还原虚机</a>'
					+ '<a style="display: block;" href="javascript:initVnc('
					+ id
					+ ')"  title="浏览器远程">浏览器远程</a>'
					+ '<a style="display: block;" href="javascript:clientVncView(\''
					+ computeResourceIP
					+ '\',\''
					+ port
					+ '\')" title="客户端远程">客户端远程</a>';
		} else if (status == "paused") {
			statusImg = "<img src='images/ck_vm_status_stop.png' alt='已挂起' title='已挂起' width='14' height='14'/>";
			operations += '<a style="display: block;" href="javascript:initVolumn('
					+ id
					+ ')"   title="卷管理" 	chkRightsUrl="virtualMachineManager/virtualMachine!setVolumn.action">卷管理</a>'
					+ '<a style="display: block;" href="javascript:initResume('
					+ id
					+ ')" title="恢复">恢复</a>'
					+ '<a style="display: block;" href="javascript:initBackup2Storage('
					+ id
					+ ')" title="备份">备份虚机</a>'
					+ '<a style="display: block;" href="javascript:initRestore2Storage('
					+ id + ')" title="还原">还原虚机</a>'
					+ '<a style="display: block;" href="javascript:initVnc('
					+ id
					+ ')"  title="浏览器远程">浏览器远程</a>'
					+ '<a style="display: block;" href="javascript:clientVncView(\''
					+ computeResourceIP
					+ '\',\''
					+ port
					+ '\')" title="客户端远程">客户端远程</a>';
		} else if (status == "shutoff") {
			statusImg = "<img src='images/ck_vm_status_shutoff.png' alt='已关机' title='已关机' width='14' height='14'/>";
			operations += '<a style="display: block;" href="javascript:initMoveVm('
					+ id
					+ ')" title="迁移" chkRightsUrl="virtualMachineManager/virtualMachine!moveVm.action">迁移</a>'
					+ '<a style="display: block;" href="javascript:initVolumn('
					+ id
					+ ')"   title="卷管理" 	chkRightsUrl="virtualMachineManager/virtualMachine!setVolumn.action">卷管理</a>'
					+ '<a style="display: block;" href="javascript:initStartup('
					+ id
					+ ')" title="启动">启动</a>'
					+ '<a style="display: block;" href="javascript:initBackup2Storage('
					+ id
					+ ',true)" title="备份">备份虚机</a>'
					+ '<a style="display: block;" href="javascript:initRestore2Storage('
					+ id + ',true)" title="还原">还原虚机</a>';
		} else if (status == "unknow") {
			statusImg = "<img src='images/ck_vm_status_unknow.png' alt='虚拟机状态不正确，或者计算节点连接失败，请联系管理员' title='虚拟机状态不正确，或者计算节点连接失败，请联系管理员' width='14' height='14'/>";
		}

		if (operateFailFlag) {
			statusOpFailedMsg = '<img src="images/ck_vm_snapshot_failed.png" width="14 height="14" style="float:" left; title="'
					+ statusMsg + '" alt="' + statusMsg + '" />';
		}
	}
	// 刷新节点
	$('#crIP_' + id).html(computeResourceIP);
	$('#crIP_' + id).attr('title', computeResourceIP);
	statusNode.html(statusImg);
	dropDownOpNode.html(operations);
	statusOpFailedMsgNode.html(statusOpFailedMsg);

	// 显示有权限的按钮
	$("[chkRightsUrl]").each(function(i) {
				if (rightsUrls.indexOf($(this).attr("chkRightsUrl")) != -1) {
					$(this).css("display", "block");
				}
			});
}
/**
 * 客户端远程连接
 * 
 * @param {}
 *            computeResourceIP
 * @param {}
 *            port
 */
function clientVncView(computeResourceIP, port) {
	if (isIE()) {
		try {// 支持
			var xmlhttp = new ActiveXObject("VNCActiveX.VNCConsole");
			location = 'cvvnc:' + computeResourceIP + ':' + port; // 打开客户端
		} catch (e) {// 不支持
			showDownloadClientWin();
			location = 'cvvnc:' + computeResourceIP + ':' + port; // 打开客户端
		}
	} else {
		showDownloadClientWin();
		location = 'cvvnc:' + computeResourceIP + ':' + port; // 打开客户端
	}
}
/**
 * 下载客户端弹出框
 */
function showDownloadClientWin() {
	var noPrompt = getCookie('noPrompt');
	if (!noPrompt) {
		ck_pop_win({
					width : 400,
					title : downloadClientTitle,
					content : "<input type='checkBox' id='noPrompt' />不再提示"
				});
	}
}
/**
 * 弹出框下载客户端
 */
function downloadClient() {
	if ($('#noPrompt').attr('checked')) {
		setCookie('noPrompt', true);
	}
	location = 'virtualMachineManager/virtualMachine!vncDownLoad.action';
	ck_remove_win();
}
/**
 * 设置cookie
 * 
 * @param {}
 *            name
 * @param {}
 *            value
 * @param {}
 *            expires
 * @param {}
 *            secure
 */
function setCookie(name, value, expires, secure) {
	var str = name + "=" + encodeURI(value);// 不要忘了在对应getCookie函数里面加上decodeURI方法
	if (expires) {
		str += "; expires=" + expires.toGMTString();
	}
	if (secure) {
		str += "; secure";
	}
	document.cookie = str;
}
/**
 * 获取cookie
 * 
 * @param {}
 *            cookieName
 * @return {}
 */
function getCookie(cookieName) {
	var re = new RegExp("\\b" + cookieName + "=([^;]*)\\b");
	var arr = re.exec(document.cookie);
	return arr ? arr[1] : '';
}

/**
 * 是否是IE
 * 
 * @return {}
 */
function isIE() {
	return navigator.userAgent.indexOf("MSIE") > 0;
}

/**
 * 初始化删除
 */
function initDelete(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmDeleteWinTitle,
				content : "是否确认删除?"
			});
}
/**
 * 初始化启动
 */
function initStartup(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmStartupWinTitle,
				content : "是否确认启动?"
			});
}
/**
 * 初始化关机
 */
function initShutdown(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmShutdownWinTitle,
				content : "是否确认关机?"
			});
}
/**
 * 初始化vnc
 */
function initVnc(id) {
	currentId = id;
	vnc();
}

/**
 * 初始化重启
 */
function initReboot(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmRebootWinTitle,
				content : "是否确认重启?"
			});
}

/**
 * 初始化挂起
 */
function initSuspend(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmSuspendWinTitle,
				content : "是否确认挂起?"
			});
}
/**
 * 初始化恢复
 */
function initResume(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmResumeWinTitle,
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
	currentId = id;
	ck_pop_win({
				width : 400,
				title : backupTitle,
				url : backupHtml
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
	currentId = id;
	ck_pop_win({
				width : 400,
				title : restoreTitle,
				url : restoreHtml,
				scroll : true,
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
				url : baseURL + "!initRestore.action",
				data : {
					"cloudContext.vo.id" : currentId
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
		$('#vmBackupData').append("<tr><td  colspan='3'>当前虚拟机无备份记录</td></tr>");
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
					+ ")'>快照还原</a>&nbsp;<a href='javascript:;' onclick='deleteSnapshot("
					+ snapshot.id + ")'>删除</a>";
		}
		$('#vmBackupData').append("<tr><td>" + snapshot.addTime + "</td><td>"
				+ snapshot.desc + "</td><td>" + operateDesc + "</td><td>" + btn
				+ "</td></tr>");
	}
}
/**
 * 初始化更新
 */
function initUpdate(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : editVirtualMachineWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
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
				title : volumnManagementTitle,
				url : volumnManagementHtml,
				noButton : true,
				submitId : "saveVolumnChange"
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : baseURL + "!queryVolumnByVmId.action",
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
							$("#addVolumn").val('-');
							$("#currentVolumnAddOp").val('+');
						});
				$("#currentVolumnAddOp").click(function() {
							$("#volumnSize").val("");
							$("#addVolumns_wrapper").hide();
							$("#currentVolumns").show();
							$("#addVolumn").val('+');
							$("#currentVolumnAddOp").val('-');
						}).click();
				$("#addVolumnSubmit").click(function() {
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
				url : baseURL + "!addVolumn.action",
				data : {
					"cloudContext.params.vmId" : vmId,
					"cloudContext.params.volumnSize" : addVolumnSize
				},
				success : function(data) {
					if (data.cloudContext.successIngoreWarn) {
						ck_remove_win();
						alert("添加成功");
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
	var answer = confirm("是否确认删除该卷?");
	if (!answer) {
		return;
	}
	// AJAX获取数据
	$.ajax({
				async : false,
				url : baseURL + "!deleteVolumn.action",
				data : {
					"cloudContext.params.volumnId" : volumnId
				},
				success : function(data) {
					if (data.cloudContext.successIngoreWarn) {
						$("#addVolumnForm tbody tr[volumnId=" + volumnId + "]")
								.remove();
						alert("删除成功");
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
function initAddOrUpdateWindow() {
	// 设置名字是否可以修改
	var url = baseURL + '!initAddOrUpdate.action';
	// 修改
	if (isUpdate()) {
		// 名字不能改
		$('#name').attr("disabled", true);
		$('#templateID').attr("disabled", true);
		$('#machineTypeID').attr("disabled", true);
		$('#netWork').attr("disabled", true);
		$('#domains').attr("disabled", true);
		$(".ck_radio").attr("disabled", true);
		url += "?cloudContext.vo.id=" + currentId
				+ "&cloudContext.params.updateFlag=true";
	}
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		success : function(data) {
			// 域
			var domains = data.cloudContext.params.domains;
			var selected = '';
			if (isUpdate()) {
				var code = data.cloudContext.params.dataVo.domainCode;
			}
			$('#domains').empty();
			for (var i in domains) {
				if (isUpdate() && code == domains[i].code) {
					selected = 'selected="selected"';
				} else {
					selected = '';
				}
				$('#domains').append('<option value="' + domains[i].code + '" '
						+ selected + '>' + domains[i].name + '</option>');
			}
			// 配置 模板
			addTemplateAndMachineType(data);
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
			// 网络
			$("#netWork").empty();
			$("#netWork").append("<option>--请选择--</option>");
			for (var i = 0; i < netWorks.length; i++) {
				$("#netWork").append("<option value='" + netWorks[i].id + "'>"
						+ netWorks[i].name + "</option>");
			}

			// 更新就存放值
			if (isUpdate()) {
				var dataVo = data.cloudContext.params.dataVo;
				$('#vmId').val(dataVo.id);
				$('#name').val(dataVo.name);
				$('#desc').val(dataVo.desc);
				$('#backupDays').val(dataVo.backupDays);
				$('#templateID').html('<option>'
						+ data.cloudContext.params.vmTemplate.name
						+ '</option>');
				var currentDomainMachineType = data.cloudContext.params.vmMachineType;
				$('#machineTypeID').html("<option>"
						+ currentDomainMachineType.name + "  |Cpu= "
						+ currentDomainMachineType.cpu + " |Memory= "
						+ currentDomainMachineType.memory + "</option>");
				$('#netWork').val(data.cloudContext.params.netWorkID);
				var backupTimeMark = data.cloudContext.params.dataVo.backupTimeMark;
				if (backupTimeMark) {
					$('#checkBackup').attr('checked', 'checked');
					$('#backupDateDiv select').attr("disabled", false);
					var backupDataArr = backupTimeMark.split(';');
					$('#backupMonth').val(backupDataArr[0]);
					$('#backupDay').val(backupDataArr[1]);
					$('#backupWeek').val(backupDataArr[2]);
					$('#backupHour').val(backupDataArr[3]);
					$('#backupMinute').val(backupDataArr[4]);
				}
			}
			if (isAdd()) {
				var machineRooms = data.cloudContext.params.machineRooms;
				$('#winMachineRoomID').empty();
				$('#winMachineRoomID').append("<option>--请选择--</option>");
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
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newVirtualMachineWinTitle;
}

/**
 * vnc
 */
function isVnc() {
	var title = $('.ck_pop_win_title').html();
	return title == vncWinTitle;
}

/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editVirtualMachineWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}
/**
 * 关机
 * 
 * @return {}
 */
function isShutdown() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmShutdownWinTitle;
}
/**
 * 开机
 * 
 * @return {}
 */
function isStartup() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmStartupWinTitle;
}
/**
 * 挂起
 * 
 * @return {}
 */
function isSuspend() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmSuspendWinTitle;
}
/**
 * 恢复
 * 
 * @return {}
 */
function isResume() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmResumeWinTitle;
}
/**
 * 重启
 * 
 * @return {}
 */
function isReboot() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmRebootWinTitle;
}
/**
 * 是否是备份系统
 * 
 * @return {}
 */
function isBackup() {
	return $('.ck_pop_win_title').html() == backupTitle;
}
/**
 * 是否是还原虚机
 * 
 * @return {}
 */
function isRestore() {
	return $('.ck_pop_win_title').html() == restoreTitle;
}

function isForceShutdown() {
	return $('.ck_pop_win_title').html() == confirmForceShutdown;
}
function isDownloadClient() {
	return $('.ck_pop_win_title').html() == downloadClientTitle;
}
/**
 * 新建模版操作
 */
function addVirtualMachine() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action');
	if ($('#checkBackup').attr('checked')) {
		$('#backupTimeMark').val($('#backupMonth').val() + ';'
				+ $('#backupDay').val() + ';' + $('#backupWeek').val() + ';'
				+ $('#backupHour').val() + ';' + $('#backupMinute').val());
	}
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateVirtualMachine() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	if ($('#checkBackup').attr('checked')) {
		$('#backupTimeMark').val($('#backupMonth').val() + ';'
				+ $('#backupDay').val() + ';' + $('#backupWeek').val() + ';'
				+ $('#backupHour').val() + ';' + $('#backupMinute').val());
	}
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteVirtualMachine() {
	location = baseURL + '!delete.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 启动
 */
function startupVirtualMachine() {
	location = baseURL + '!startup.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 关机
 */
function shutdownVirtualMachine() {
	location = baseURL + '!shutdown.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 重启
 */
function rebootVirtualMachine() {
	location = baseURL + '!reboot.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 挂起
 */
function suspendVirtualMachine() {
	location = baseURL + '!suspend.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 恢复
 */
function resumeVirtualMachine() {
	location = baseURL + '!resume.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
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
	var url = baseURL + '!vnc.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	var form = $("<form method='post' style='display:none' target='_blank' action='"
			+ url + "'> </form>");
	form.appendTo("body");
	form.submit();
}

/**
 * 拼url参数
 * 
 * @return {}
 */
function createQueryParam() {
	var eachPageData = parseInt($.trim($("#eachPageData").val()));
	var nowPage = $.trim($("#nowPage").text());
	var offset = (nowPage - 1) * eachPageData;
	var qName = $("#qName").val();
	return 'pagesize=' + eachPageData + '&cloudContext.params.qName=' + qName
			+ '&pager.offset=' + offset;
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
		url : baseURL + "!queryResourcePoolByCurrentDomain.action",
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
				url : baseURL + "!queryResourceByPoolAndCpuMemory.action",
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
	form.attr('action', baseURL + '!backup.action');
	$("#backupVMId").val(currentId);
	form.submit();
	ck_showProcessingImg();
}
/**
 * 还原系统
 */
function restoreVM(snapshotId) {
	location = baseURL + '!restore.action?cloudContext.vo.id=' + currentId
			+ '&cloudContext.params.snapshotId=' + snapshotId;
	ck_showProcessingImg();
}
/**
 * 删除快照
 */
function deleteSnapshot(snapshotId) {
	location = baseURL
			+ "!deleteSnapshot.action?cloudContext.params.snapshotId="
			+ snapshotId + "&cloudContext.vo.id=" + currentId;
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
		if (!allSuperDomainTemplates)
			$.ajax({
				async : false,
				url : baseURL + "!queryAllSuperDomainTemplate.action",
				data : {
					"cloudContext.params.domainCode" : $('#domains').val()
				},
				success : function(data) {
					allSuperDomainTemplates = data.cloudContext.params.parentsDomainTemplate;
				}
			});
		$("#templateID").empty();
		$("#templateID").append("<option value=''>--请选择--</option>");
		for (var i in allSuperDomainTemplates) {
			$("#templateID").append("<option value='"
					+ allSuperDomainTemplates[i].id + "'>"
					+ allSuperDomainTemplates[i].name + "</option>");
		}
		$("#templateID").append("<option value='-2'>当前域模版</option>");
	} else if (val == '-2') {
		$("#templateID").empty();
		$("#templateID").append("<option value=''>--请选择--</option>");
		for (var i in currentDomainTemplates) {
			$("#templateID").append("<option value='"
					+ currentDomainTemplates[i].id + "'>"
					+ currentDomainTemplates[i].name + "</option>");
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
		if (!allSuperDomainMachineTypes)
			$.ajax({
				async : false,
				url : baseURL + "!queryAllSuperDomainMachineTypes.action",
				data : {
					"cloudContext.params.domainCode" : $('#domains').val()
				},
				success : function(data) {
					allSuperDomainMachineTypes = data.cloudContext.params.parentsDomainMachineTypes;
				}
			});
		$("#machineTypeID").empty();
		$("#machineTypeID").append("<option value=''>--请选择--</option>");
		for (var i in allSuperDomainMachineTypes) {
			$("#machineTypeID").append("<option cpu='"
					+ allSuperDomainMachineTypes[i].cpu + "' memory='"
					+ allSuperDomainMachineTypes[i].memory + "' value='"
					+ allSuperDomainMachineTypes[i].id + "'>"
					+ allSuperDomainMachineTypes[i].name + "  |Cpu= "
					+ allSuperDomainMachineTypes[i].cpu + " |Memory= "
					+ allSuperDomainMachineTypes[i].memory + "</option>");
		}
		$("#machineTypeID").append("<option value='-2'>当前域配置</option>");
	} else if (val == '-2') {
		$("#machineTypeID").empty();
		$("#machineTypeID").append("<option value=''>--请选择--</option>");
		for (var i in currentDomainMachineTypes) {
			$("#machineTypeID").append("<option cpu='"
					+ currentDomainMachineTypes[i].cpu + "' memory='"
					+ currentDomainMachineTypes[i].memory + "' value='"
					+ currentDomainMachineTypes[i].id + "'>"
					+ currentDomainMachineTypes[i].name + "  |Cpu= "
					+ currentDomainMachineTypes[i].cpu + " |Memory= "
					+ currentDomainMachineTypes[i].memory + "</option>");
		}
		$("#machineTypeID").append("<option value='-1'>所有上级域配置</option>");
	}
}

// ####################### 级联操作 #################################
/**
 * 级联操作
 */
function queryRackByRoom(selectID, param, msg) {
	$("#" + selectID).empty();
	$('#qCompute').empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
	$('#qCompute').append("<option value=''>--请选择--</option>");
	if (param == "" || param == null) {
		return;
	}
	var url = baseURL + "!queryRackByRoom.action?cloudContext.params.roomID="
			+ param;

	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : url,
				success : function(data) {
					// 配置
					var machineRacks = data.cloudContext.params.machineRacks;
					var errorMsg = data.cloudContext.errorMsgList;
					if (errorMsg && errorMsg.length > 0) {
						alert(errorMsg);
						return;
					}
					var selected = null;
					for (var i = 0; i < machineRacks.length; i++) {
						selected = (machineRacks[i].id == qRack
								? "selected='selected'"
								: "");
						$("#" + selectID).append("<option value='"
								+ machineRacks[i].id + "' " + selected + ">"
								+ machineRacks[i].name + "</option>");
					}
				}
			});
}

function queryComputeByRack(selectID, param, msg) {
	$("#" + selectID).empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
	$('#qType').val('');
	if (param == "" || param == null) {
		return;
	}
	var url = baseURL
			+ "!queryComputeByRack.action?cloudContext.params.rackID=" + param;
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : url,
				success : function(data) {
					// 配置
					var computes = data.cloudContext.params.computes;
					var errorMsg = data.cloudContext.errorMsgList;
					if (errorMsg && errorMsg.length > 0) {
						alert(errorMsg);
						return;
					}
					var selected = null;
					for (var i = 0; i < computes.length; i++) {
						selected = (computes[i].id == qCompute
								? "selected='selected'"
								: "");
						$("#" + selectID).append("<option value='"
								+ computes[i].id + "' " + selected + ">"
								+ computes[i].name + "</option>");
					}
				}
			});
}
/**
 * 新建时切换域
 */
function selectDomain() {
	var code = $('#domains').val();
	var url = baseURL + "!queryData4ChangeDomain.action";
	var params = {
		"cloudContext.params.domainCode" : code
	};
	$.ajax({
				url : url,
				type : 'post',
				data : params,
				success : addTemplateAndMachineType
			});
}

function addTemplateAndMachineType(data) {
	// 配置
	$("#machineTypeID").empty();
	$("#machineTypeID").append('<option value="">--请选择--</option>');
	currentDomainMachineTypes = data.cloudContext.params.machineTypes;
	if (currentDomainMachineTypes) {
		for (var i = 0; i < currentDomainMachineTypes.length; i++) {
			$("#machineTypeID").append("<option cpu='"
					+ currentDomainMachineTypes[i].cpu + "' memory='"
					+ currentDomainMachineTypes[i].memory + "' value='"
					+ currentDomainMachineTypes[i].id + "'>"
					+ currentDomainMachineTypes[i].name + "  |Cpu= "
					+ currentDomainMachineTypes[i].cpu + " |Memory= "
					+ currentDomainMachineTypes[i].memory + "</option>");
		}
	}
	$("#machineTypeID").append("<option value='-1'>关联域所有配置</option>");
	// 模版
	$("#templateID").empty();
	$("#templateID").append('<option value="">--请选择--</option>');
	currentDomainTemplates = data.cloudContext.params.templates;
	if (currentDomainTemplates) {
		for (var i = 0; i < currentDomainTemplates.length; i++) {
			$("#templateID").append("<option value='"
					+ currentDomainTemplates[i].id + "'>"
					+ currentDomainTemplates[i].name + "</option>");
		}
	}
	$("#templateID").append("<option value='-1'>关联域所有模版</option>");
}
/**
 * 强制关机
 * 
 * @param {}
 *            id
 */
function initForceShutdown(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmForceShutdown,
				content : "是否强制关机?"
			});
}
/**
 * 强制关机
 */
function forceShutdown() {
	location = baseURL + '!forceShutdown.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}

// /################循环测试
// /################循环测试
// /################循环测试
// /################循环测试
// /################循环测试
// /################循环测试
// /################循环测试
// /################循环测试

function loopStart() {
	ck_pop_win({
				width : 400,
				title : "循环测试<font color='red'>仅测试用</font>",
				noButton : true,
				url : 'virtualMachine/loopTest_.jsp'
			});
}
$('#backupLoop').live('click', function() {
			$('#loopForm').attr('action', baseURL + '!backupLoop.action');
			$('#loopForm').submit();
		});
$('#restoreLoop').live('click', function() {
			$('#loopForm').attr('action', baseURL + '!restoreLoop.action');
			$('#loopForm').submit();
		});
$('#migrateLoop').live('click', function() {
			$('#loopForm').attr('action', baseURL + '!migrateLoop.action');
			$('#loopForm').submit();
		});
function loopStop() {
	ck_pop_win({
				width : 400,
				title : "测试结束<font color='red'>仅测试用</font>",
				noButton : true,
				url : 'virtualMachine/loopTestStop_.jsp'
			}, function() {
				$.ajax({
							url : baseURL + '!initLoopStop.action',
							success : function(data) {
								var params = data.cloudContext.params;
								var migrate = eval(params.migrate) ? '是' : '否';
								var backup = eval(params.backup) ? '是' : '否';
								var restore = eval(params.restore) ? '是' : '否';
								$('#loopContent').html('正在迁移：' + migrate
										+ '<br/>' + '正在备份：' + backup + '<br/>'
										+ '正在还原：' + restore + '<br/>');
							}
						});
			});
}
$('#migrateStop').live('click', function() {
			location = baseURL + '!migrateLoopStop.action';
		});
$('#backupStop').live('click', function() {
			location = baseURL + '!backupLoopStop.action';
		});
$('#restoreStop').live('click', function() {
			location = baseURL + '!restoreLoopStop.action';
		});