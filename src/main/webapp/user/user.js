var editUserWinTitle = '编辑用户'
var confirmDeleteWinTitle = '删除用户';
var newUserWinTitle = '新建用户';
var addOrUpdateHtml = 'user/addOrUpdateWindow_.jsp';
var baseURL = 'userManager/user';
// 当前操作的ID
var currentId = 0;
var rootNode = null;
// 域ID
var domainCodes = new Array();
$(function() {
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewUser').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newUserWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						if (!validate()) {
							return;
						}
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteUser();
							return;
						}
						if (isAdd()) {
							addUser();
							return;
						}
						if (isUpdate()) {
							updateUser();
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
				title : editUserWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
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
		$('#username').attr("disabled", true);
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
						$('#username').val(dataVo.username);
						$('#realname').val(dataVo.realname);
						$('#email').val(dataVo.email);
						$('#cellPhone').val(dataVo.cellPhone);
						$('#telPhone').val(dataVo.telPhone);
						$('#password').siblings('font').remove();
						$('#repassword').siblings('font').remove();
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newUserWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editUserWinTitle;
}
/**
 * 是否删除
 */
function isDelete() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmDeleteWinTitle;
}

/**
 * 新建模版操作
 */
function addUser() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateUser() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteUser() {
	location = baseURL + '!delete.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
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
	var qDomain = $('#qDomain').val();
	return 'pagesize=' + eachPageData + '&cloudContext.params.qName=' + qName
			+ '&cloudContext.params.qDomain=' + qDomain + '&pager.offset='
			+ offset;
}
