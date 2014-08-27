var editComputeResourceWinTitle = '编辑计算节点'
var confirmDeleteWinTitle = '删除计算节点';
var newComputeResourceWinTitle = '新建计算节点';
var addOrUpdateHtml = 'computeResource/addOrUpdateWindow_.jsp';
// 当前操作的computeResourceID
var baseURL = "computeResourceManager/computeResource"
var currentId = 0;
$(function() {
			/*
			 * 点击新建资源按钮，弹出窗口
			 */
			$('#ckNewComputeResource').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newComputeResourceWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteComputeResource();
							return;
						}
						if (!validate()) {
							return;
						}
						if (isAdd()) {
							addComputeResource();
							return;
						}
						if (isUpdate()) {
							updateComputeResource();
							return;
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
				title : editComputeResourceWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 设置设备是否可以修改
	var url = baseURL + "!initAddOrUpdate.action";
	// 修改
	if (isUpdate()) {
		// 设备不能改
		$('#username').parents('tr').remove();
		$('#password').parents('tr').remove();
		$('#name').attr("disabled", true);
		$('#type').attr("disabled", true);
		// $('#machineRoomID').attr("disabled", true);
		// $('#machineRackID').attr("disabled", true);
		$('#ip').attr("disabled", true);
		url += "?cloudContext.vo.id=" + currentId
				+ "&cloudContext.params.updateFlag=true";
	}
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		success : function(data) {
			// 机房
			var machineRooms = data.cloudContext.params.machineRooms;
			if (machineRooms.length == 0) {
				alert("没有机房，请先添加机房！");
				ck_remove_win();
				return;
			}
			$("#machineRoomID").empty();
			$("#machineRoomID").append("<option>--请选择--</option>")
			for (var i = 0; i < machineRooms.length; i++) {
				$("#machineRoomID").append("<option value='"
						+ machineRooms[i].id + "'>" + machineRooms[i].name
						+ "</option>");
			}
			// 更新存放值
			if (isUpdate()) {
				// 机架
				var machineRacks = data.cloudContext.params.machineRacks;
				if (machineRacks.length == 0) {
					alert("没有机柜，请先添加机柜！");
					return;
				}
				for (var i = 0; i < machineRacks.length; i++) {
					$("#machineRackID").append("<option value='"
							+ machineRacks[i].id + "'>" + machineRacks[i].name
							+ "</option>");
				}
				var dataVo = data.cloudContext.params.dataVo;
				$('#id').val(dataVo.id);
				$('#name').val(dataVo.name);
				$('#desc').text(dataVo.desc);
				$('#ip').val(dataVo.ip);
				$('#machineRoomID').val(data.cloudContext.params.machineRoomID);
				$('#machineRackID').val(data.cloudContext.params.machineRackID);
				$('#type').val(dataVo.type);
				if (dataVo.warn4Rack) {
					$('#checkWarn').attr('checked', 'checked');
					$('#warnDiv input').attr("disabled", false);
					var data = dataVo.warn4Rack.split(';');
					var cpuRate = parseFloat(data[0]) * 100;
					var memRate = parseFloat(data[1]) * 100;
					$('#cpuRate4Warn').val(cpuRate);
					$('#memoryRate4Warn').val(memRate);
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
	return title == newComputeResourceWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editComputeResourceWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建资源操作
 */
function addComputeResource() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action' + createQueryStr());
	if ($('#checkWarn').attr('checked')) {
		$('#warn4RackID').val(parseInt($('#cpuRate4Warn').val()) / 100 + ';'
				+ parseInt($('#memoryRate4Warn').val()) / 100);
	} else {
		$('#warn4RackID').val('');
	}
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑资源操作
 */
function updateComputeResource() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action' + createQueryStr());
	if ($('#checkWarn').attr('checked')) {
		$('#warn4RackID').val(parseInt($('#cpuRate4Warn').val()) / 100 + ';'
				+ parseInt($('#memoryRate4Warn').val()) / 100);
	} else {
		$('#warn4RackID').val('');
	}
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除资源
 */
function deleteComputeResource() {
	location = baseURL + "!delete.action" + createQueryStr()
			+ "&cloudContext.vo.id=" + currentId;
	ck_showProcessingImg();
}

/**
 * 根据机房查找机架，级联操作
 */
function queryRackByRoom(selectID, roomID, msg) {
	$("#" + selectID).empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
	if (roomID == "" || roomID == null) {
		return;
	}
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : baseURL
						+ "!queryRackByRoom.action?cloudContext.params.roomID="
						+ roomID,
				success : function(data) {
					// 配置
					var machineRacks = data.cloudContext.params.machineRacks;
					if (machineRacks.length == 0 && msg != "") {
						alert(msg);
						return;
					}

					for (var i = 0; i < machineRacks.length; i++) {
						$("#" + selectID).append("<option value='"
								+ machineRacks[i].id + "'>"
								+ machineRacks[i].name + "</option>");
					}
				}
			});
}

/**
 * 
 */
function createQueryStr() {
	var nowPage = parseInt($.trim($("#nowPage").text()));
	var eachPageData = parseInt($.trim($("#eachPageData").val()));
	var pager_offset = (nowPage - 1) * eachPageData;
	var pagesize = eachPageData;
	var qName = $("#qName").val();
	var qRack = $("#qRack").val();
	var qRoom = $("#qRoom").val();
	var qType = $("#qType").val();
	var queryStr = "?pager.offset=" + pager_offset + "&pagesize=" + pagesize
			+ "&cloudContext.params.qName=" + qName
			+ "&cloudContext.params.qRack=" + qRack
			+ "&cloudContext.params.qRoom=" + qRoom
			+ "&cloudContext.params.qType=" + qType;
	return queryStr;
}