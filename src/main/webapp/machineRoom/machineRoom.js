var editmachineRoomWinTitle = '编辑机房'
var confirmDeleteWinTitle = '删除机房';
var newmachineRoomWinTitle = '新建机房';
var addOrUpdateHtml = 'machineRoom/addOrUpdateWindow_.jsp';
// 当前操作的machineRoomID
var currentId = 0;
$(function() {
			/*
			 * 点击新建机房按钮，弹出窗口
			 */
			$('#ckNewmachineRoom').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newmachineRoomWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deletemachineRoom();
							return;
						}
						if (isAdd()) {
							if (!validate()) {
								return;
							}
							addmachineRoom();
							return;
						}
						if (isUpdate()) {
							if (!validate()) {
								return;
							}
							updatemachineRoom();
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
				title : editmachineRoomWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 设置名字是否可以修改
	var url = "machineRoomManager/machineRoom!initAddOrUpdate.action";
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
					// 更新存放值
					if (isUpdate()) {
						var dataVo = data.cloudContext.params.dataVo;
						$('#id').val(dataVo.id);
						$('#name').val(dataVo.name);
						$('#desc').text(dataVo.desc);
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newmachineRoomWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editmachineRoomWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建机房操作
 */
function addmachineRoom() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'machineRoomManager/machineRoom!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑机房操作
 */
function updatemachineRoom() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'machineRoomManager/machineRoom!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除机房
 */
function deletemachineRoom() {
	location = "machineRoomManager/machineRoom!delete.action?cloudContext.vo.id="
			+ currentId;
	ck_showProcessingImg();
}
