var editMachineTypeWinTitle = '编辑配置'
var confirmDeleteWinTitle = '删除配置';
var newMachineTypeWinTitle = '新建配置';
var addOrUpdateHtml = 'machinetype/addOrUpdateWindow_.jsp';
// 当前操作的machineTypeID
var currentId = 0;
var baseURL = "machineTypeManager/machineType";
$(function() {
			/*
			 * 点击新建配置按钮，弹出窗口
			 */
			$('#ckNewMachineType').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newMachineTypeWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteMachineType();
							return;
						}
						if (isAdd()) {
							addMachineType();
							return;
						}
						if (isUpdate()) {
							updateMachineType();
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
				title : editMachineTypeWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 设置名字是否可以修改
	var url = baseURL + "!initAddOrUpdae.action";
	// 修改
	if (isUpdate()) {
		// 名字不能改
		$('#name').attr("disabled", true);
		$('#cpu').attr("disabled", true);
		$('#memory').attr("disabled", true);
		$('#disk').attr("disabled", true);
		url += "?cloudContext.vo.id=" + currentId
				+ "&cloudContext.params.updateFlag=true";
	}
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : url,
				success : function(data) {
					var dataVo = data.cloudContext.params.dataVo;
					// 域初始化数据
					$('#domains').empty();
					var domains = data.cloudContext.params.domains;
					var checked = ''
					for (var i in domains) {
						if (domains[i].id == dataVo.domainId) {
							checked = 'checked="checked"';
						}
						$('#domains').append('<option value="' + domains[i].id
								+ '" ' + checked + '>' + domains[i].name
								+ '</option>');
					}
					// 更新存放值
					if (isUpdate()) {
						$('#id').val(dataVo.id);
						$('#name').val(dataVo.name);
						$('#desc').val(dataVo.desc);
						$('#cpu').val(dataVo.cpu);
						$('#memory').val(dataVo.memory);
						$('#disk').val(dataVo.disk);
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newMachineTypeWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editMachineTypeWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建配置操作
 */
function addMachineType() {
	if (checkCPUMemoryStorageItem()) {
		var form = $('#addOrUpdateForm');
		form.attr('action', baseURL + '!add.action');
		form.submit();
		ck_showProcessingImg();
	} else {
		alert("标记星号为必填项!");
	}
}

/**
 * 编辑配置操作
 */
function updateMachineType() {
	if (checkCPUMemoryStorageItem()) {
		var form = $('#addOrUpdateForm');
		form.attr('action', baseURL + '!update.action');
		form.submit();
		ck_showProcessingImg();
	} else {
		alert("标记星号为必填项!");
	}
}
/**
 * 删除配置
 */
function deleteMachineType() {
	location = baseURL + "!delete.action?cloudContext.vo.id=" + currentId;
	ck_showProcessingImg();
}

/**
 * 检查cpu,memory,storage项是否正确填写
 */
function checkCPUMemoryStorageItem() {
	var cpu = $("#cpu").val();
	var memory = $("#memory").val();
	var storage = $("#disk").val();
	if (cpu.length > 0 && memory.length > 0 && storage.length > 0) {
		return true;
	} else {
		return false;
	}
}
