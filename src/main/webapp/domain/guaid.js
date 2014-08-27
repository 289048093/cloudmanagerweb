var actionDiv = null;
/**
 * 点击向导按钮，弹出窗口
 */
$('#ckGuaid').live('click', function() {
			ck_pop_win({
						width : 800,
						title : guaidWinTitle,
						url : 'domain/addGuaid_.jsp'
					}, addGuaidCallBack);
			actionDiv = new Array();
			actionDiv.push('guaidPoolDiv');
		})
/**
 * 向导创建回调函数
 */
function addGuaidCallBack() {
	$(".ck_guaid_menu ul li").live("click", function() {
		// 置换活动页菜单标签
		$(".ck_guaid_current_menu").removeClass("ck_guaid_current_menu");
		$(this).addClass("ck_guaid_current_menu");
		// 置换活动页面内容
		$(".ck_guaid_current_content").removeClass("ck_guaid_current_content");
		$("#" + $(this).attr("target")).addClass("ck_guaid_current_content");
		// 转换导航图片条上的进度
		$(".ck_guaid_in_chart ." + $(this).attr("target"))
				.removeClass("ck_process_unactive")
				.addClass("ck_process_active");
		// 显示主DIV
		$("#" + $(this).attr("target")).children().hide();
		$("#" + $(this).attr("target")).children().eq(0).show();
	});
	initActionWindow();
	initGuaidData();
	$(".ck_guaid_menu ul li").eq(0).click(function() {
				actionDiv.push('guaidBaseInfoDiv');
			});
	$(".ck_guaid_menu ul li").eq(1).click(function() {
				actionDiv.push('guaidPoolDiv');
			});
	$(".ck_guaid_menu ul li").eq(2).click(function() {
				actionDiv.push('guaidStorageResourceDiv');
			});
	$(".ck_guaid_menu ul li").eq(0).click();
}

/**
 * 自定义资源池
 */
function addPool() {
	$('#guaidPoolDiv').hide();
	$('#addPoolAction').show();
	$('#addPoolDiv').show();
	actionDiv.push('addPoolDiv');
}

function initGuaidData() {
	// 设置名字是否可以修改
	var url = baseURL + '!initAddPool.action';
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		success : function(data) {
			// 资源搜索初始化
			addRoomData(data, $('#qResourceByRoom'));
			addRoomData(data, $('#roomID4AddStorage'));
			var resources = data.cloudContext.params.resources;
			for (var i in resources) {

			}
			addResourceDataToTable(data);
			// 域ZTree初始化
			var domains = eval(data.cloudContext.params.domains);
			initSuperDomainRadioScript(domains, "superDomainTree4GuaidBaseInfo");
			rootNode.checked = true;
			$.fn.zTree.getZTreeObj("superDomainTree4GuaidBaseInfo").refresh();
		}
	});
}

function addRoomData(data, $select) {
	var machineRooms = data.cloudContext.params.machineRooms;
	for (var i in machineRooms) {
		$select.append("<option value='" + machineRooms[i].id + "'>"
				+ machineRooms[i].name + "</option>");
	}
	$select.append("<option value='-1'>自定义</option>");
}

/**
 * 将查询到的数据加入到窗口的table中
 * 
 * @param {}
 *            data
 */
function addResourceDataToTable(data) {
	var resources = data.cloudContext.params.computeResources;
	$('#resources').empty();
	if (resources.length == 0) {
		$('#resources').append('<tr><td>无数据</td></tr>');
		if ($('#qResourceByRack').val() != '') {
			$('#resources')
					.append('<tr><td><input type="button" value="自定义资源" onclick="addResource();"  id="resourceBtn"/></td></tr>');
		}
		return;
	}
	var checked = null;
	for (var i in resources) {
		checked = ''
		if (resources[i].computeResourcePoolId == currentId) {
			checked = "checked='checked'";
		}
		$('#resources')
				.append('<tr><td><input type="checkBox" name="cloudContext.params.resourceIds" value="'
						+ resources[i].id
						+ '"'
						+ checked
						+ '/>'
						+ resources[i].name + '</td></tr>');
	}
	if ($('#qResourceByRack').val() != '') {
		$('#resources')
				.append('<tr><td><input type="button" value="自定义资源" onclick="addResource();" id="resourceBtn"/></td></tr>');
	}
}

/**
 * 级联操作（机房级联机柜）
 */
function queryCascadeDataBySelectID(selectID, param, msg) {
	$('#resources').empty();
	// 先隐藏自定义机房机柜窗口
	if (selectID == 'qResourceByRack') {
		$('#addRoomDiv4GuaidPool').hide();
		$('#addRackDiv4GuaidPool').hide();
	}
	if (selectID == 'addStorageRackID') {
		$('#addRoomDiv4GuaidStorage').hide();
		$('#addRackDiv4GuaidStorage').hide();
	}
	if (param == '-1') {
		$("#" + selectID).empty();
		$("#" + selectID).append("<option value=''>--请选择--</option>");
		$("#" + selectID).append("<option value='-1'>自定义</option>");
		if (selectID == 'qResourceByRack') {
			$('#addRoomDiv4GuaidPool').show();
		}
		if (selectID == 'addStorageRackID') {
			$('#addRoomDiv4GuaidStorage').show();
		}
		return;
	}
	$("#" + selectID).empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
	if (param == '') {
		if (selectID == 'qResourceByRack') {
			queryResource();
		}
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
					$("#" + selectID).append("<option value='-1'>自定义</option>");
				}
			});
	queryResource();
}
/**
 * 自定义资源池中机柜chang事件
 * 
 * @param {}
 *            val
 */
function changeRack(val) {
	if (val == '-1') {
		$('#addRackDiv4GuaidPool').show();
		$('#resources').empty();
		$('#resources')
				.append('<tr><td><input type="button" value="自定义资源" onclick="addResource();"  id="resourceBtn"/></td></tr>');
		return;
	}
	$('#addRackDiv4GuaidPool').hide();
	$('#addResourceDiv').hide();
	if ($('#qResourceByRoom').val != -1) {
		queryResource()
	}
}
/**
 * 当点击
 * 
 * @param {}
 *            val
 */
function changeRack4GuaidStorage(val) {
	if (val == '-1') {
		$('#addRackDiv4GuaidStorage').show();
		return;
	}
	$('#addRackDiv4GuaidStorage').hide();
}

/**
 * 点击自定义资源按钮
 */
function addResource() {
	var val = $('#resourceBtn').val();
	$('#addResourceDiv').show();
	$('#resourceBtn').hide();
}
/**
 * 确定添加资源
 */
function addResourceOk() {
	addResourceAndContinue();
	$('#addResourceDiv').hide();
	$('#resourceBtn').show();
}
/**
 * 添加资源并继续
 */
function addResourceAndContinue() {
	var addResourceName = $('#name4AddResources').val();
	var addResourceIp = $('#ip4AddResource').val();
	$('<tr><td><input type="checkBox" name="cloudContext.params.resourceIds" value="'
			+ addResourceName
			+ ","
			+ addResourceIp
			+ '" checked="checked"/>'
			+ addResourceName + '</td></tr>').insertBefore($('#resources tr')
			.last());
}
/**
 * 取消自定义资源
 */
function cancelAddResource() {
	$('#addResourceDiv').hide();
	$('#resourceBtn').show();
}
/**
 * 返回
 */
function actionBack() {
	var prevAction = actionDiv.pop();
	$('#' + prevAction).hide();
	$('#' + actionDiv[actionDiv.length - 1]).show();
}

/**
 * 查询资源
 */
function queryResource() {
	$('#resources').empty();
	var url = baseURL + '!queryResource.action';
	$.ajax({type : 'post',
		url : url,
		asyn : false,
		data : {
			'cloudContext.params.qResourceByRoom' : $('#qResourceByRoom').val(),
			'cloudContext.params.qResourceByRack' : $('#qResourceByRack').val()
		},
		success : addResourceDataToTable
	});
}

/**
 * 完成自定义资源池
 */
function addPoolOk() {
	// TODO 表单验证
	var $pools = $('#guaidPoolTable').find(':checkbox');
	if ($pools.length == 0) {
		$('#guaidPoolTable').empty();
	}
	$('#guaidPoolTable')
			.append('<tr><td><input type="checkBox" name="cloudContext.params.computeResourcePoolIds" value="-1" checked="checked"></td><td>'
					+ $('#addPoolName').val() + '</td></tr>');
	actionBack();
}
/**
 * 确定添加存储资源
 */
function addStorageOk() {
	// TODO 表单验证
	var addStorageName = $('#name4AddStorage').val();
	$('#guaidStorageResources')
			.append('<tr><td><input type="checkBox" name="cloudContext.params.storageResourceIds" value="-1" checked="checked"></td><td>'
					+ addStorageName + '</td></tr>');
	actionBack();
}
/**
 * 自定义存储资源窗口
 */
function addStorageResource() {
	$('#guaidStorageResourceDiv').hide();
	$('#addStorageResourceAction').show();
	actionDiv.push("addStorageResourceAction");
}

function guaidSubmit() {
	var $form = $('#guaidForm');
	$form.attr('action', baseURL + '!guaidInsert.action');
	var zTree = $.fn.zTree.getZTreeObj("superDomainTree4GuaidBaseInfo");
	var nodes = zTree.getCheckedNodes(true);
	if (nodes.length > 0) {
		$('#superDomainCode').val(nodes[0].id);
	}
	$form.submit();
}