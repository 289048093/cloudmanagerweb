var editComputeResourcePoolWinTitle = '编辑计算节点池';
var confirmDeleteWinTitle = '删除计算节点池';
var newComputeResourcePoolWinTitle = '新建计算节点池';
var setResourceWinTitle = '资源配置';
var addOrUpdateHtml = 'computeResourcePool/addOrUpdateWindow_.jsp';
var computeResourceManagerHtml = 'computeResourcePool/computeResourcemanager_.jsp';
var baseURL = 'computeResourcePoolManager/computeResourcePool';
// 当前操作的ID
var currentId = 0;
var zTree = null;
var resources = null;
var computeResourcesHasVm = new Array();
var statusSuccessReturn = true; // 状态顺利获取标记位
$(function() {
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewComputeResourcePool').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newComputeResourcePoolWinTitle,
									url : addOrUpdateHtml
								}, initActionWindow);
					});

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteComputeResourcePool();
							return;
						}
						if (isSetResource()) {
							$("#s1 option").attr("selected", true);
							setResource();
							return;
						}
						if (!validate()) {
							return;
						}
						var zTree = $.fn.zTree.getZTreeObj("treeDiv");
						if (zTree) {
							var checkedNodes = zTree.getCheckedNodes(true);
							if (checkedNodes.length > 0) {
								var domainIDs = new Array();
								for (var i in checkedNodes) {
									domainIDs.push(checkedNodes[i].dId);
								}
								$('#domainIDs').val(domainIDs);
							}
						}

						if (isAdd()) {
							addComputeResourcePool();
							return;
						}
						if (isUpdate()) {
							updateComputeResourcePool();
							return;
						}

						// 虚拟机部分
						if (isVmDelete()) {
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
						if (isVnc()) {
							vnc();
							return;
						}
						if (isVmUpdate()) {
							if (!vm_validate()) {
								return;
							}
							updateVirtualMachine();
							return;
						}
						if (isBackup()) {
							backupVM();
							return;
						}
						if (isRestore()) {
							restoreVM();
						}
					});
		});
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
 * 初始化更新
 */
function initUpdate(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : editComputeResourcePoolWinTitle,
				url : addOrUpdateHtml
			}, initActionWindow);
}
/**
 * 初始化计算节点资源池
 * 
 * @param {}
 *            id
 */
function initComputeResource(id) {
	currentId = id;
	ck_pop_win({
				width : 420,
				title : setResourceWinTitle,
				url : computeResourceManagerHtml
			}, initUpdateComputeResource);
}

function initUpdateComputeResource() {
	// 设置名字是否可以修改
	var url = baseURL + '!queryResource.action';
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : url,
				data : {
					"cloudContext.vo.id" : currentId
				},
				success : addResourceDataToTable
			});
}
/**
 * 初始化更新窗体
 */
function initActionWindow() {
	// 设置名字是否可以修改
	var url = baseURL + '!initAddOrUpdate.action';
	// 修改
	if (isUpdate()) {
		// 名字不能改
		$('#cpuRate').attr("disabled", true);
		$('#memoryRate').attr("disabled", true);
		$('#name').attr("disabled", true);
		url += "?cloudContext.vo.id=" + currentId
				+ "&cloudContext.params.updateFlag=true";
	}
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : url,
				success : function(data) {
					// 资源搜索初始化
					var machineRooms = data.cloudContext.params.machineRooms;
					for (var i in machineRooms) {
						$('#qResourceByRoom').append("<option value='"
								+ machineRooms[i].id + "'>"
								+ machineRooms[i].name + "</option>");
					}
					// 域ZTree初始化
					var domains = eval(data.cloudContext.params.domains);
					initMutilCheckScript(domains, 'treeDiv');

					// 更新存放值
					if (isUpdate()) {
						var dataVo = data.cloudContext.params.dataVo;
						$('#id').val(dataVo.id);
						$('#name').val(dataVo.name);
						$('#cpuRate').val(dataVo.cpuRate);
						$('#memoryRate').val(dataVo.memoryRate);
						$('#desc').val(dataVo.desc);
						var domains = eval(data.cloudContext.params.selectedDomains);
						for (var i in domains) {
							var node = zTree.getNodeByParam('dId',
									domains[i].id);
							if (node) {
								node.checked = true;
							}
						}
						zTree.refresh();
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newComputeResourcePoolWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editComputeResourcePoolWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}
/**
 * 是否是设置资源
 */
function isSetResource() {
	return $('.ck_pop_win_title').html() == setResourceWinTitle;
}

/**
 * 新建模版操作
 */
function addComputeResourcePool() {
//	$('#crManagerTab').show();
//	$('#addAndUpdateTab').hide();
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateComputeResourcePool() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteComputeResourcePool() {
	location = baseURL + '!delete.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 设置资源
 * 
 * @return {}
 */
function setResource() {
	var form = $('#crForm');
	form.attr('action', baseURL + '!updateComputeResources.action');
	$('#resourcePoolId').val(currentId);
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

/**
 * 级联操作
 */
function queryCascadeDataBySelectID(selectID, param, msg) {
	$("#" + selectID).empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
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
					var cascadeData = data.cloudContext.params.cascadeData;
					if (cascadeData.length == 0 && msg != "") {
						alert(msg);
						return;
					}
					for (var i = 0; i < cascadeData.length; i++) {
						$("#" + selectID).append("<option value='"
								+ cascadeData[i].id + "'>"
								+ cascadeData[i].name + "</option>");
					}
				}
			});
}
/**
 * 查询资源
 */
function queryResource() {
	$('#resources').empty();
	var $qRoom = $('#qResourceByRoom').val();
	var $qRack = $('#qResourceByRack').val();
	var $qName = $('#qResourceByName').val();
	var url = baseURL + '!queryResource.action';
	$.ajax({
				type : 'post',
				url : url,
				data : {
					'cloudContext.vo.id' : currentId,
					'cloudContext.params.qResourceByRoom' : $qRoom,
					'cloudContext.params.qResourceByRack' : $qRack,
					'cloudContext.params.qResourceByName' : $qName
				},
				success : addResourceDataToTable
			});
}
/**
 * 加载计算节点
 * 
 * @param poolId
 *            计算节点池Id
 */
function loadComputeResource(poolId) {
	if (!poolId) {
		return;
	}
	$.ajax({
		type : 'post',
		async : true,
		url : baseURL + "!queryComputeResourceByPoolId.action",
		data : {
			"cloudContext.params.poolId" : poolId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				var computeResources = data.cloudContext.params.computeResources;
				if (computeResources) {
					// 渲染展现
					showComputeResources(poolId, computeResources);
				}
			} else {
				var errorMsgList = data.cloudContext.errorMsgList;
				if (errorMsgList) {
					var errorMsg = "";
					for (var i = 0; i < errorMsgList.length; i++) {
						errorMsg += errorMsgList[i];
					}
					if (errorMsg != "") {
						alert(errorMsg);
					}
				}
			}

		}
	});

}

/**
 * 渲染展现对应计算节点池下的节点
 * 
 * @param {}
 *            computeResources
 */
function showComputeResources(poolId, computeResources) {
	currentId = poolId;
	$(".ck_computeResource_body").empty();
	$(".ck_computeResource_wrapper").hide();
	$(".ck_compute_tmp_tr").remove();

	if ($(".ck_pool_toggle[poolId=" + poolId + "] a").text() == "-") {
		$(".ck_pool_toggle[poolId=" + poolId + "] a").text("+");
		return;
	}
	$(".ck_pool_toggle").each(function() {
				$(this).find("a").text('+');
			});
	$(".ck_pool_toggle[poolId=" + poolId + "] a").text("-");
	// 遍历渲染
	var domHtml = '';
	var dataFlag = false;
	var even_odd_class = "ck_tr_even";
	for (var i = 0; i < computeResources.length; i++) {
		if (i % 2 == 0) {
			even_odd_class = "ck_tr_even";
		} else {
			even_odd_class = "ck_tr_odd";
		}
		dataFlag = true;
		domHtml += '<tr  id="removeCrTd_'
				+ computeResources[i].id
				+ '" class="'
				+ even_odd_class
				+ '" resourceId="'
				+ computeResources[i].id
				+ '"><td class="ck_compute_toggle" resourceId="'
				+ computeResources[i].id
				+ '">'
				// + '<a style="display:block"
				// href="javascript:loadVirtualMachine(' +
				// computeResources[i].id + ')" title="查看虚拟机">+</a></td>'
				+ '</td><td title='
				+ computeResources[i].name
				+ '>'
				+ computeResources[i].name
				+ '</td><td  title='
				+ computeResources[i].machineRoomName
				+ '>'
				+ computeResources[i].machineRoomName
				+ '</td><td  title='
				+ computeResources[i].machineRackName
				+ '>'
				+ computeResources[i].machineRackName
				+ '</td><td  title='
				+ computeResources[i].ip
				+ '>'
				+ computeResources[i].ip
				+ '</td><td title="'
				+ computeResources[i].cpuAvailable
				+ '">'
				+ computeResources[i].cpuAvailable
				+ '</td><td title="'
				+ computeResources[i].memoryAvailable
				+ '">'
				+ computeResources[i].memoryAvailable
				+ '</td><td  title='
				+ computeResources[i].vmNum
				+ '>'
				+ computeResources[i].vmNum
				+ '</td>'
				+ '<td><a class="ck_del" style="display:block" title="从池中移除节点" href="javascript:deleteComputeResource('
				+ computeResources[i].id + ')"></a></td></tr>';
	}
	if (!dataFlag) {
		domHtml += "<tr><td colspan='7'>没有计算节点</td></tr>";
	}
	$(".ck_computeResource_body").html(domHtml);
	// 查找目标节点
	var targetNode = $(".ck_pool_toggle[poolId=" + poolId + "]").parent();
	var currentNode = $(".ck_computeResource_wrapper");
	currentNode.css("width", targetNode.width() - 24).css("left",
			targetNode.position().left).css("top",
			targetNode.position().top + targetNode.height());
	targetNode.after("<tr class='ck_compute_tmp_tr'><td colspan='12'>"
			+ currentNode.html() + "</td></tr>");
}

/**
 * 查看虚拟机
 * 
 * @param {}
 *            resourceId
 */
function loadVirtualMachine(resourceId) {
	if (!resourceId) {
		return;
	}
	$.ajax({
				type : 'post',
				async : true,
				url : baseURL
						+ "!queryVirtualMachineByComputeResourceId.action",
				data : {
					"cloudContext.params.resourceId" : resourceId
				},
				success : function(data) {
					if (data.cloudContext.successIngoreWarn) {
						var virtualMachines = data.cloudContext.params.virtualMachines;
						if (virtualMachines) {
							// 渲染展现
							showVirtualMachines(resourceId, virtualMachines);
						}
					} else {
						var errorMsgList = data.cloudContext.errorMsgList;
						if (errorMsgList) {
							var errorMsg = "";
							for (var i = 0; i < errorMsgList.length; i++) {
								errorMsg += errorMsgList[i];
							}
							if (errorMsg != "") {
								alert(errorMsg);
							}
						}
					}

				}
			});
}

/**
 * 渲染展现对应计算节点下的虚拟机
 * 
 * @param {}
 *            computeResources
 */
function showVirtualMachines(resourceId, virtualMachines) {
	$(".ck_virtualMachine_body").empty();
	$(".ck_virtualMachine_wrapper").hide();
	$(".ck_virutalMachine_tmp_tr").remove();

	if ($(".ck_compute_toggle[resourceId=" + resourceId + "] a").text() == "--") {
		$(".ck_compute_toggle[resourceId=" + resourceId + "] a").text("+");
		return;
	}
	// 收缩功能
	$(".ck_compute_toggle").each(function() {
				$(this).find("a").text('+');
			});
	$(".ck_compute_toggle[resourceId=" + resourceId + "] a").text("--");

	// 检测是否是admin
	var loginedUserName = $("#loginedUserName").text();

	// 遍历渲染
	var domHtml = '';
	var dataFlag = false;
	var even_odd_class = "ck_tr_even";
	for (var i = 0; i < virtualMachines.length; i++) {
		if (i % 2 == 0) {
			even_odd_class = "ck_tr_even";
		} else {
			even_odd_class = "ck_tr_odd";
		}
		var adminHtml = "";
		if (loginedUserName == 'admin') {
			adminHtml = '<td title=' + virtualMachines[i].computeResourceIP
					+ '>' + virtualMachines[i].computeResourceIP + '</td>';
		}
		var dueTime = virtualMachines[i].dueTime == null
				? "永不过期"
				: (virtualMachines[i].dueTime).substring(0,
						(virtualMachines[i].dueTime).indexOf("T"));
		dataFlag = true;
		domHtml += '<tr class="'
				+ even_odd_class
				+ '">'
				+ '<input type="hidden" class="virtualMachineIdKeeper" value='
				+ virtualMachines[i].id
				+ ' />'
				+ '<td title='
				+ virtualMachines[i].name
				+ '>'
				+ virtualMachines[i].name
				+ '</td>'
				+ '<td title='
				+ virtualMachines[i].owner
				+ '>'
				+ virtualMachines[i].owner
				+ '</td>'
				+ '<td title='
				+ virtualMachines[i].creator
				+ '>'
				+ virtualMachines[i].creator
				+ '</td>'
				+ '<td title='
				+ dueTime
				+ '>'
				+ dueTime
				+ '</td>'
				+ '<td title='
				+ virtualMachines[i].ip
				+ '>'
				+ virtualMachines[i].ip
				+ '</td>'
				+ adminHtml
				+ '<td class="ck_vm_img" title='
				+ virtualMachines[i].status
				+ '><img src="images/loading_media.gif" width="14"height="14" title="加载中" alt="加载中" /></td>'
				+ '<td title=' + virtualMachines[i].desc + '>'
				+ virtualMachines[i].desc + '</td>'
				+ '<td class="ck_table_operation"></td>' + '</tr>';
	}
	if (!dataFlag) {
		if (adminHtml == 'admin') {
			domHtml += "<tr><td colspan='9'>没有虚拟机</td></tr>";
		} else {
			domHtml += "<tr><td colspan='8'>没有虚拟机</td></tr>";
		}
	}
	$(".ck_virtualMachine_body").html(domHtml);
	// 查找目标节点
	var targetNode = $(".ck_compute_toggle[resourceId=" + resourceId + "]")
			.parent();
	var currentNode = $(".ck_virtualMachine_wrapper");
	currentNode.css("width", targetNode.width() - 50).css("left",
			targetNode.position().left).css("top",
			targetNode.position().top + targetNode.height());
	targetNode.after("<tr class='ck_virutalMachine_tmp_tr'><td colspan='7'>"
			+ currentNode.html() + "</td></tr>");
	// 延迟1秒获取状态
	setTimeout(function() {
				queryStatusVarAjax();
			}, 1000);
	setInterval(queryStatusVarAjax, 30000);
}

/**
 * 池下移除计算节点
 * 
 * @param {}
 *            resourceId
 */
function deleteComputeResource(resourceId, $node) {
	if (!resourceId || !currentId) {
		return;
	}
	$.ajax({
				type : 'post',
				async : true,
				url : baseURL + "!deleteComputeResource.action",
				data : {
					"cloudContext.params.poolId" : currentId,
					"cloudContext.params.computeId" : resourceId
				},
				success : function(data) {
					var crp = data.cloudContext.vo;
					if (data.cloudContext.success) {
						// 警告信息
						if (!data.cloudContext.successIngoreWarn) {
							var warnList = data.cloudContext.warnMsgList;
							if (warnList) {
								var warnMsg = "";
								for (var i = 0; i < warnList.length; i++) {
									warnMsg += warnList[i];
								}
								if (warnMsg != "") {
									alert(warnMsg);
								}
							}
						}
						// 成功信息
						var successMsgList = data.cloudContext.successMsgList;
						if (successMsgList) {
							var successMsg = "";
							for (var i = 0; i < successMsgList.length; i++) {
								successMsg += successMsgList[i];
							}
						}
						$('#removeCrTd_' + resourceId).remove();
						updateCrpTdData(crp);
						// 移除成功删除的节点
						// $(".ck_tmpWrapper tr[resourceId=" + resourceId +
						// "]").remove();
					} else {
						var errorMsgList = data.cloudContext.errorMsgList;
						if (errorMsgList) {
							var errorMsg = "";
							for (var i = 0; i < errorMsgList.length; i++) {
								errorMsg += errorMsgList[i];
							}
							if (errorMsg != "") {
								alert(errorMsg);
							}
						}
					}
				}
			});
}
/**
 * 将查询到的数据加入到窗口的table中
 * 
 * @param {}
 *            data
 */
function addResourceDataToTable(data) {
	var resources = data.cloudContext.params.computeResources;
	var prevIds = new Array();
	if (resources.length == 0) {
		$('#crSelectTable').hide();
		$('#crSelectTable').parent().append('<tr><td>无计算节点数据</td></tr>');
	}
	var checked = null;
	for (var i in resources) {

		if (resources[i].computeResourcePoolId == currentId) {// 是当前域的
			$('#s1').append('<option value="' + resources[i].id + '">'
					+ resources[i].name + '</option>');
			prevIds.push(resources[i].id);
			if (resources[i].vmNum > 0) {
				computeResourcesHasVm[resources[i].name] = true;
			}
		} else {
			$('#s2').append('<option value="' + resources[i].id + '">'
					+ resources[i].name + '</option>');
		}
	}
	$('#prevResourceIds').val(prevIds);
	$("#s1 option:first,#s2 option:first").attr("selected", true);
}

/**
 * 初始化域ZTree（多选）
 * 
 * @param {}
 *            ztreeJSON
 */
function initMutilCheckScript(ztreeJSON, zTreeDiv) {
	var setting = {
		check : {
			chkboxType : {
				"Y" : "p",
				"N" : "s"
			},
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		}
	};
	$.fn.zTree.init($("#" + zTreeDiv), setting, ztreeJSON);
	zTree = $.fn.zTree.getZTreeObj(zTreeDiv);
	var ns = zTree.getNodes();
	firstNode = ns[0];
	zTree.expandAll(true);
	if (isUpdate()) {
		zTree.setChkDisabled(firstNode, true);
	}
	if (isAdd()) {
		firstNode.checked = true;
	}
	zTree.refresh();
}
/**
 * 更新资源池指定行的数据
 * 
 * @param crp
 */
function updateCrpTdData(crp) {
	$('#crpCrNumTd_' + crp.id).html(crp.computeResourceNum);
	$('#crpTotalCpuTd_' + crp.id).html(crp.totalCpu);
	$('#crpTotalMemoryTd_' + crp.id).html(crp.totalMemory);
	$('#crpAviCpuTd_' + crp.id).html(crp.availableCpu);
	$('#crpAviMemoryTd_' + crp.id).html(crp.availableMemory);
}
