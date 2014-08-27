var editmachineRackWinTitle = '编辑机架'
var confirmDeleteWinTitle = '删除机架';
var newmachineRackWinTitle = '新建机架';
var addOrUpdateHtml = 'machineRack/addOrUpdateWindow_.jsp';
// 当前操作的machineRackID
var currentId = 0;
$(function() {
			/*
			 * 点击新建机架按钮，弹出窗口
			 */
			$('#ckNewmachineRack').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newmachineRackWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deletemachineRack();
							return;
						}
						if (isAdd()) {
							if (!validate()) {
								return;
							}
							addmachineRack();
							return;
						}
						if (isUpdate()) {
							if (!validate()) {
								return;
							}
							updatemachineRack();
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
				title : editmachineRackWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 设置名字是否可以修改
	var url = "machineRackManager/machineRack!initAddOrUpdate.action";
	// 修改
	if (isUpdate()) {
		// 名字不能改
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
			// 配置
			var machineRooms = data.cloudContext.params.machineRooms;
			if (machineRooms.length == 0) {
				ck_message({
							type : 'warn',
							text : '没有机房，请先添加机房！',
							renderTo : 'ck_message_container',
							timeOut : 3000
						});

				ck_remove_win();
				return;
			}
			$("#machineRoomID").empty();
			$("#machineRoomID").append("<option>--请选择--</option>");
			for (var i = 0; i < machineRooms.length; i++) {
				$("#machineRoomID").append("<option value='"
						+ machineRooms[i].id + "'>" + machineRooms[i].name
						+ "</option>");
			}
			// 更新存放值
			if (isUpdate()) {
				var dataVo = data.cloudContext.params.dataVo;
				$('#id').val(dataVo.id);
				$('#name').val(dataVo.name);
				$('#desc').text(dataVo.desc);
				$('#machineRoomID').val(data.cloudContext.params.machineRoomID);
				$('#warn4RoomID').val(dataVo.warn4Room + "");

			}
		}
	});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newmachineRackWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editmachineRackWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建机架操作
 */
function addmachineRack() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'machineRackManager/machineRack!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑机架操作
 */
function updatemachineRack() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'machineRackManager/machineRack!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除机架
 */
function deletemachineRack() {
	location = "machineRackManager/machineRack!delete.action?cloudContext.vo.id="
			+ currentId;
	ck_showProcessingImg();
}
