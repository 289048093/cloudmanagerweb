var editTemplateWinTitle = '编辑模板'
var confirmDeleteWinTitle = '删除模板';
var deleteWithFileWinTitle = '删除模板并删除镜像文件';
var newTemplateWinTitle = '新建模板';
var addOrUpdateHtml = 'template/addOrUpdateWindow_.jsp';
var baseURL = "templateManager/template";
// 当前操作的templateID
var currentId = 0;

$(function() {
	/*
	 * 点击新建模板按钮，弹出窗口
	 */
	$('#ckNewTemplate').live('click', function() {
				ck_pop_win({
							width : 400,
							title : newTemplateWinTitle,
							url : addOrUpdateHtml
						}, initAddOrUpdateWindow);
			})
	$('#ckUploadTemplate').live('click', function() {
		var basePath=$("#basePath").val();
		window.open(basePath+"template/upload.jsp");
	})
	/*
	 * 点击弹出窗口的确定按钮操作
	 */
	$('.ck_confirm').live('click', function() {
				var title = $('.ck_pop_win_title').html();
				if (isDelete()) {
					deleteTemplate();
					return;
				}
				if (isDeleteWithFile()) {
					deleteWithFile();
					return;
				}
				if (isAdd()) {
					if (!validate()) {
						return;
					}
					addTemplate();
					return;
				}
				if (isUpdate()) {
					if (!validate()) {
						return;
					}
					updateTemplate();
					return;
				}
			});
	/**
	 * 状态改变操作
	 */
	$('#type').live('change', function() {
				var type = $('#type').val();
				if (type == 2) {
					$('#fileURL_tr').show();
					$('#fileName_tr').hide();
					return;
				}
				if (type == 1) {
					$('#fileURL_tr').hide();
					$('#fileName_tr').show();
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
				title : editTemplateWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 设置名字是否可以修改
	var url = baseURL + "!initAddOrUpdate.action";
	// 修改
	if (isUpdate()) {
		// 名字不能改
		$('#name').attr("disabled", true);
		$('#type').attr("disabled", true);
		$('#domains').attr("disabled", true);
		$('#fileName').attr("disabled", true);
		$('#url').attr("disabled", true);
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
						if (domains[i].code == dataVo.domainCode) {
							checked = 'checked="checked"';
						}
						$('#domains').append('<option value="'
								+ domains[i].code + '" ' + checked + '>'
								+ domains[i].name + '</option>');
					}
					// 文件列表
					addImgData(data);
					// 更新存放值
					if (isUpdate()) {
						$('#id').val(dataVo.id);
						$('#name').val(dataVo.name);
						$('#desc').text(dataVo.desc);
						$('#username').val(dataVo.username);
						$('#password').val(dataVo.password);
						$('#type').val(dataVo.type).change();
						$('#fileName').append("<option>" + dataVo.fileName
								+ "</option>");
						$('#url').val(dataVo.url);
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newTemplateWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editTemplateWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建模板操作
 */
function addTemplate() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'templateManager/template!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模板操作
 */
function updateTemplate() {
	var form = $('#addOrUpdateForm');
	form.attr('action', 'templateManager/template!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模板
 */
function deleteTemplate() {
	location = "templateManager/template!delete.action?cloudContext.vo.id="
			+ currentId;
	ck_showProcessingImg();
}
/**
 * 切换域 查询镜像
 * 
 * @param {}
 *            code
 */
function selectDomain() {
	var code = $('#domains').val();
	var url = baseURL + "!queryData4ChangeDomain.action";
	var params = {
		"cloudContext.vo.domainCode" : code
	}
	$.ajax({
				url : url,
				type : 'post',
				data : params,
				success : addImgData
			});
}
/**
 * 将镜像加入窗口
 * 
 * @param {}
 *            data
 */
function addImgData(data) {
	var fileList = data.cloudContext.params.fileList;
	$('#fileName option').remove();
	for (var i in fileList) {
		$('#fileName').append('<option>' + fileList[i] + '</option>');
	}
	if (!$('#fileName').val() && isAdd()) {
		$("#fileName").siblings(".errorMsg").html("没有本地镜像");
	} else {
		$("#fileName").siblings(".errorMsg").html("");
	}
}
/**
 * 弹出 删除并删除镜像文件 窗口
 * 
 * @param {}
 *            id
 */
function initDeleteWithFile(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : deleteWithFileWinTitle,
				content : "是否要删除模版并删除镜像文件？"
			}, initAddOrUpdateWindow);
}
/**
 * 是否是 删除并删除镜像文件 操作
 */
function isDeleteWithFile() {
	return $('.ck_pop_win_title').html() == deleteWithFileWinTitle;
}

function deleteWithFile() {
	location = baseURL + "!delete.action?cloudContext.vo.id=" + currentId
			+ "&cloudContext.params.deleteFile=" + true;
	ck_showProcessingImg();
}