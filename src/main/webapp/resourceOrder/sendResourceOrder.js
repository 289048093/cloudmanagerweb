var editSendResourceOrderWinTitle = '编辑申请资源工单'
var confirmDeleteWinTitle = '删除申请资源工单';
var newSendResourceOrderWinTitle = '新建申请资源工单';
var addOrUpdateHtml = 'resourceOrder/addOrUpdateWindowForSend_.jsp';
var baseURL = 'sendResourceOrderManager/sendResourceOrder';
// 当前操作的ID
var currentId = 0;
$(function() {
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewSendResourceOrder').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newSendResourceOrderWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteSendResourceOrder();
							return;
						}
						if (!validate()) {
							return;
						}
						if (isAdd()) {
							addSendResourceOrder();
							return;
						}
						if (isUpdate()) {
							updateSendResourceOrder();
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
				title : editSendResourceOrderWinTitle,
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
		$('#title').attr("disabled", true);
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
						var dataVo = data.cloudContext.vo;
						if (dataVo.status != accepting) {
							$('#storageCapacity').attr("disabled", true);
							$('#cpu').attr("disabled", true);
							$('#memory').attr("disabled", true);
						}
						$('#id').val(dataVo.id);
						$('#title').val(dataVo.title);
						$('#content').val(dataVo.content);
						$('#storageCapacity').val(dataVo.storageCapacity);
						$('#cpu').val(dataVo.cpu);
						$('#memory').val(dataVo.memory);
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newSendResourceOrderWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editSendResourceOrderWinTitle;
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
function addSendResourceOrder() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateSendResourceOrder() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteSendResourceOrder() {
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
	var qTitle = $("#qTitle").val();
	var qStatus = $("#qStatus").val();
	return 'pagesize=' + eachPageData + '&cloudContext.params.qTitle=' + qTitle
			+ '&cloudContext.params.qStatus=' + qStatus + '&pager.offset='
			+ offset;
}