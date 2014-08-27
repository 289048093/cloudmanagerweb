var editDomainWinTitle = '编辑域';
var confirmDeleteWinTitle = '删除域';
var newDomainWinTitle = '新建域';
var setUsersWinTitle = '设置用户';
var updatePoolsWinTitle = '设置资源池';
var guaidWinTitle = "创建向导";
var addOrUpdateHtml = 'domain/addOrUpdateWindow_.jsp';
var setUsersWindow = 'domain/setUsersWindow_.jsp';
var poolManagerHtml = 'domain/poolManager_.jsp';
var baseURL = 'domainManager/domain';
// 当前操作的templateID
var currentId = 0;
var currentCode = null;
var rootNode = null;

var superDomainStorageAvailableCapacity = null;
var superDomainBackupStorageAvailableCapacity = null;
var superDomainCpuAvailableCapacity = null;
var superDomainMemoryAvailableCapacity = null;
var usedStorage = null;
var usedBackupStorage = null;
var usedCpu = null;
var usedMemory = null;
var currentStorage = null;
var currentCpu = null;
var currentMemory = null;

var poolArr = new Array();

$(function() {
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewDomain').live('click', function() {
						ck_pop_win({
									width : 500,
									title : newDomainWinTitle,
									url : addOrUpdateHtml
								}, initActionWindow);
					});

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						if (isUpdatePools()) {
							updatePools();
						}
						if (!validate()) {
							return;
						}
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteDomain();
							return;
						}
						if (isAdd()) {
							addDomain();
							return;
						}
						if (isUpdate()) {
							updateDomain();
							return;
						}
						if (isSetUsers()) {
							setUsers();
							return;
						}
						if (isGuaid()) {
							guaidSubmit();
						}
					});
		});

/**
 * 初始化删除
 */
function initDelete(id) {
	currentId = id;
	ck_pop_win({
				width : 500,
				title : confirmDeleteWinTitle,
				content : "该操作将会删除域下所有工单,是否继续?"
			});
}
/**
 * 初始化更新
 */
function initUpdate(id) {
	currentId = id;
	ck_pop_win({
				width : 500,
				title : editDomainWinTitle,
				url : addOrUpdateHtml
			}, initActionWindow);
}

/**
 * 初始化设置用户
 * 
 * @param {}
 *            id
 */
function initSetUsers(id) {
	currentId = id;
	ck_pop_win({
				width : 500,
				title : setUsersWinTitle,
				scroll : true,
				url : 'domain/setUsersWindow_.jsp'
			}, initActionWindow);

}
/**
 * js调资源池设置
 * 
 * @param {}
 *            id
 */
function initResourcePools(id, code) {
	currentId = id;
	currentCode = code;
	ck_pop_win({
				width : 500,
				title : updatePoolsWinTitle,
				url : poolManagerHtml
			}, initResourcePoolsData);
}

function initResourcePoolsData() {
	var url = baseURL + '!initPools.action';
	var params = {
		"cloudContext.vo.code" : currentCode,
		"cloudContext.vo.id" : currentId
	};
	$.ajax({
				url : url,
				data : params,
				async : false,
				type : 'post',
				success : addPoolData2Window
			});
}
/**
 * 添加资源池数据到页面
 * 
 * @param {}
 *            data
 */
function addPoolData2Window(data) {
	var pools = data.cloudContext.params.computeResourcePools;
	var domain = data.cloudContext.params.domain;
	domainAvailableCpu = domain.cpuAvailableNum;
	domainAvailableMemory = domain.memoryAvailableCapacity;
	var containPools = new Array();
	var $table = $('#pools');
	if (!pools.length || pools.length == 0) {
		$table.empty();
		$table.append('<tr><td><div>无相应记录</div></td></tr>');
		return;
	}
	for (var i in pools) {
		poolArr[pools[i].name] = pools[i];
		if (pools[i].domainId == currentId) {// 如果是新增，默认全选，继承上级资源池
			$('#s1').append('<option value="' + pools[i].id + '">'
					+ pools[i].name + '</option>');
			containPools.push(pools[i].id);
		} else {
			$('#s2').append('<option value="' + pools[i].id + '">'
					+ pools[i].name + '</option>');
		}
	}
	$('#prevPools').val(containPools);
	$("#s1 option:first,#s2 option:first").attr("selected", true);
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
		$('#name').attr("disabled", true);
		$('#treeDiv').hide();
		url += "?cloudContext.vo.id=" + currentId
				+ "&cloudContext.params.updateFlag=true";
	}
	// 设置用户
	if (isSetUsers()) {
		url += '?cloudContext.vo.id=' + currentId
				+ "&cloudContext.params.setUsersFlag=true";
	}
	if (isGuaid()) {
		url += "?cloudContext.params.guaidFlag=true";
	}
	// AJAX获取数据
	$.ajax({
		// type : 'post',
		async : false,
		url : url,
		success : function(data) {
			if (isSetUsers() || isGuaid()) {
				addInitUsers(data);
			}
			// 初始化资源池
			if (isAdd() || isUpdate()) {
				// 更新存放值
				if (isUpdate()) {
					var dataVo = data.cloudContext.params.dataVo;
					currentStorage = parseInt(dataVo.storageCapacity);
					currentBackupStorage = parseInt(dataVo.backupStorageCapacity);
					currentCpu = parseInt(dataVo.cpuTotalNum);
					currentMemory = dataVo.memoryCapacity;
					$('#domainId').val(currentId);
					$('#domainId').val(dataVo.id);
					$('#name').val(dataVo.name);
					$('#desc').val(dataVo.desc);
					$('#storageCapacity').val(dataVo.storageCapacity);
					$('#backupStorageCapacity')
							.val(dataVo.backupStorageCapacity);
					$('#cpuTotalNum').val(dataVo.cpuTotalNum);
					$('#memoryCapacity').val(dataVo.memoryCapacity);
					$('#superDomainDiv')
							.html(data.cloudContext.params.superDomain);
					var isRootDomain = eval(data.cloudContext.params.rootDomain);
					// 如果是根域则不能修改自己的资源大小
					if (isRootDomain) {
						$('#isRootDomain').val(true);
						$('#storageCapacity').attr('disabled', true);
						$('#backupStorageCapacity').attr('disabled', true);
						$('#cpuTotalNum').attr('disabled', true);
						$('#memoryCapacity').attr('disabled', true);
					} else {// 否则计算上级可用资源大小，和自己已用资源大小
						$('#isRootDomain').val(false);
						var code = dataVo.code;
						var superDomainNode = new Object();
						superDomainNode.id = code.substring(0, code.length - 2);
						checkZTreeNode(null, null, superDomainNode);
					}
					usedStorage = parseInt(dataVo.storageCapacity)
							- parseInt(dataVo.availableStorageCapacity);
					usedBackupStorage = parseInt(dataVo.backupStorageCapacity)
							- parseInt(dataVo.availableBackupStorageCapacity);
					usedCpu = parseInt(dataVo.cpuTotalNum)
							- parseInt(dataVo.cpuAvailableNum);
					usedMemory = parseInt(dataVo.memoryCapacity)
							- parseInt(dataVo.memoryAvailableCapacity);
				}
				if (isAdd()) {
					var domains = eval(data.cloudContext.params.domains);
					initSuperDomainRadioScript(domains, "treeDiv");
					rootNode.checked = true;
					$.fn.zTree.getZTreeObj("treeDiv").refresh();
					var node = new Object();
					node.id = data.cloudContext.vo.code;// 初始化根域 资源情况
					checkZTreeNode(null, null, node);
				}
				updateResourceRangeDisplay();
			}
			if (isGuaid()) {
				// TODO ..
				// createArrayDataToTable(computeResourcePools,
				// $('#guaidPoolTable'), 'computeResourcePoolIds');
			}
			// 资源可用范围显示

		}
	});
}

/**
 * 资源可用范围显示
 */
function updateResourceRangeDisplay() {
	if (isAdd()) {
		$('#storageRange').html('范围:' + '0~'
				+ superDomainStorageAvailableCapacity);
		$('#backupStorageRange').html('范围:' + '0~'
				+ superDomainBackupStorageAvailableCapacity);
		$('#cpuRange').html('范围:' + '0~' + superDomainCpuAvailableCapacity);
		$('#memoryRange').html('范围:' + '0~'
				+ superDomainMemoryAvailableCapacity);
	} else if (isUpdate()) {
		$('#storageRange').html('范围:' + usedStorage + '~'
				+ (currentStorage + superDomainStorageAvailableCapacity));
		$('#backupStorageRange')
				.html('范围:'
						+ usedBackupStorage
						+ '~'
						+ (currentBackupStorage + superDomainBackupStorageAvailableCapacity));
		$('#cpuRange').html('范围:' + usedCpu + '~'
				+ (currentCpu + superDomainCpuAvailableCapacity));
		$('#memoryRange').html('范围:' + usedMemory + '~'
				+ (currentMemory + superDomainMemoryAvailableCapacity));
	}
}
/**
 * 添加初始化的用户数据
 * 
 * @param {}
 *            data
 */
function addInitUsers(data) {
	var users = data.cloudContext.params.users;
	if (isSetUsers()) {
		$('#domainId').val(currentId);
	}
	createArrayDataToTable(users, $('#users'), 'userId');
	var manager = data.cloudContext.params.manager;
	if (manager) {
		$('#users').append('<tr><td colspan="2">当前管理员：' + manager.realname
				+ '</td></tr>');
	}
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newDomainWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editDomainWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}
/**
 * 是否是设置用户窗口
 */
function isSetUsers() {
	return $('.ck_pop_win_title').html() == setUsersWinTitle;
}

/**
 * 是否是更新资源池
 * 
 * @return {}
 */
function isUpdatePools() {
	return $('.ck_pop_win_title').html() == updatePoolsWinTitle;
}
/**
 * 是否是创建向导
 */
function isGuaid() {
	return $('.ck_pop_win_title').html() == guaidWinTitle;
}

/**
 * 新建模版操作
 */
function addDomain() {
	var form = $('#addOrUpdateForm');
	var zTree = $.fn.zTree.getZTreeObj("treeDiv");
	var nodes = zTree.getCheckedNodes(true);
	$('#superDomainCode').val(nodes[0].id);
	form.attr('action', baseURL + '!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateDomain() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 配置资源池
 */
function updatePools() {
	var msg = confirmPoolResource();
	if (msg) {
		if (!confirm(msg)) {
			return;
		}
	}
	var form = $('#updatePoolsForm');
	form.attr('action', baseURL + '!updatePools.action');
	$('#domainId').val(currentId);
	form.submit();
	ck_showProcessingImg();
}
/**
 * 确认资源池设置信息
 * @return {String}
 */
function confirmPoolResource() {
	var alloptions = $("#s1 option");
	if (alloptions.length == 0) {
		return "没有添加资源池,是否继续？";
	}
	var totalCpu = 0;
	var totalMemory = 0;
	for (var i = 0; i < alloptions.length; i++) {
		totalCpu += poolArr[alloptions.eq(i).html()].availableCpu;
		totalMemory += poolArr[alloptions.eq(i).html()].availableMemory;
	}
	if (totalCpu < domainAvailableCpu) {
		return "添加的资源池可用cpu小于域的cpu,是否继续？";
	}
	if (totalMemory < domainAvailableMemory) {
		return "添加的资源池可用内存小于域的内存,是否继续？";
	}
}
/**
 * 删除模版
 */
function deleteDomain() {
	location = baseURL + '!delete.action?cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 设置用户
 * 
 * @param {}
 *            ztreeJSON
 */
function setUsers() {
	var form = $('#setUsersForm');
	form.attr('action', baseURL + '!updateDomainUser.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 设置资源池
 */
function updateResourcePools() {
	var form = $('#updateResourcePoolsForm');
	form.attr('action', baseURL + '!updateResourcePools.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 初始化域ZTree
 * 
 * @param {}
 *            ztreeJSON
 */
function initSuperDomainRadioScript(ztreeJSON, zTreeId) {
	var setting = {
		check : {
			radioType : "all",
			chkStyle : "radio",
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onCheck : checkZTreeNode
		}
	};
	$.fn.zTree.init($("#" + zTreeId), setting, ztreeJSON);
	var zTree = $.fn.zTree.getZTreeObj(zTreeId);
	var ns = zTree.getNodes();
	rootNode = ns[0];
	zTree.expandAll(true);
}
/**
 * 选择上级
 * 
 * @param {}
 *            event
 * @param {}
 *            treeId
 * @param {}
 *            treeNode
 */
function checkZTreeNode(event, treeId, treeNode) {
	$.ajax({
		type : 'post',
		async : false,
		url : baseURL + "!querykDomainPoolAndStorageCapacity.action",
		data : {
			"cloudContext.params.superDomainCode" : treeNode.id,
			"cloudContext.vo.id" : currentId
		},
		success : function(data) {
			var errors = data.cloudContext.params.errorMsgList;
			if (errors) {
				alert(errors);
				return;
			}
			superDomainStorageAvailableCapacity = parseInt(data.cloudContext.vo.availableStorageCapacity);
			superDomainBackupStorageAvailableCapacity = parseInt(data.cloudContext.vo.availableBackupStorageCapacity);
			superDomainCpuAvailableCapacity = parseInt(data.cloudContext.vo.cpuAvailableNum);
			superDomainMemoryAvailableCapacity = parseInt(data.cloudContext.vo.memoryAvailableCapacity);
			if (isAdd()) {
				$('#storageCapacity').val(Math
						.floor(superDomainStorageAvailableCapacity / 2));
				$('#backupStorageCapacity').val(Math
						.floor(superDomainBackupStorageAvailableCapacity / 2));
				$('#cpuTotalNum').val(Math
						.floor(superDomainCpuAvailableCapacity / 2));
				$('#memoryCapacity').val(Math
						.floor(superDomainMemoryAvailableCapacity / 2));
			}
			var computeResourcePools = data.cloudContext.params.computeResourcePools;
			if (isGuaid()) {
				$('#guaidPoolTable').empty();
				// TODO ..
				// createArrayDataToTable(computeResourcePools,
				// $('#guaidPoolTable'), 'computeResourcePoolIds')
				return;
			}
		}
	});
	updateResourceRangeDisplay();
}
/**
 * 初始化设置资源池
 * 
 * @param {}
 *            id
 */
function SetResourcePools(id) {
	currentId = id;
	ck_pop_win({
				width : 500,
				title : updatePoolsWinTitle,
				url : 'domain/setResourcePoolWindow_.jsp'
			}, ActionWindow);
}

/**
 * 授权管理
 * 
 * @param {}
 *            id
 */
function Authorization(domainId) {
	ck_pop_win({
				width : 600,
				title : "授权管理",
				url : 'domain/Authorization_.jsp',
				submitId : 'ck_authorization_confirm'
			}, getMenu(domainId));
}

function createArrayDataToTable(array, $table, name) {
	if (!array.length || array.length == 0) {
		$table.append('<tr><td><div>无相应记录</div></td></tr>');
		return;
	}
	var checked = '';
	var disabled = '';
	var inputeType = 'checkBox';
	var text = null;
	for (var i in array) {
		text = array[i].name || array[i].realname;
		if (isAdd() || (isGuaid() && name != 'userId')
				|| array[i].domainId == currentId) {// 如果是新增，默认全选，继承上级资源池
			checked = ' checked="checked"';
		}
		if (isSetUsers() || (isGuaid() && name == 'userId')) {
			inputeType = 'radio';
		}
		if (isUpdate()
				&& (name == 'computeResourcePoolIds' || name == 'storageResourceIds')) {
			disabled = ' disabled="disabled"';
		}
		$table.append('<tr><td><input type="' + inputeType
				+ '" name="cloudContext.params.' + name + '" value="'
				+ array[i].id + '" ' + checked + disabled + '></td><td>' + text
				+ '</td></tr>');
		checked = '';
	}
}
