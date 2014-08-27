var editNetWorkWinTitle = '编辑网络'
var confirmDeleteWinTitle = '删除网络';
var newNetWorkWinTitle = '新建网络';
var addOrUpdateHtml = 'netWork/addOrUpdateWindow_.jsp';
var baseURL = 'netWorkManager/netWork';
// 当前操作的ID
var currentId = 0;
$(function() {
			/*
			 * 点击新建模版按钮，弹出窗口
			 */
			$('#ckNewNetWork').live('click', function() {
						ck_pop_win({
									width : 400,
									title : newNetWorkWinTitle,
									url : addOrUpdateHtml
								}, initAddOrUpdateWindow);
					})

			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isDelete()) {
							deleteNetWork();
							return;
						}
						if (isAdd()) {
							if (!validate()) {
								return;
							}
							addNetWork();
							return;
						}
						if (isUpdate()) {
							if (!validate()) {
								return;
							}
							updateNetWork();
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
				title : editNetWorkWinTitle,
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
		// 不可修改项
		$('#name').attr("disabled", true);
		$('#startIP').attr("disabled", true);
		$('#endIP').attr("disabled", true);
		$('#cidr').attr("disabled", true);
		$('#type').attr("disabled", true);
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
						$('#startIP').val(dataVo.startIP);
						$('#endIP').val(dataVo.endIP);
						$('#cidr').val(dataVo.cidr);
						$('#type').val(dataVo.type);
						$('#desc').val(dataVo.desc);
					}
				}
			});
}

/**
 * 是否增加
 */
function isAdd() {
	var title = $('.ck_pop_win_title').html();
	return title == newNetWorkWinTitle;
}
/**
 * 是否修改
 */
function isUpdate() {
	var title = $('.ck_pop_win_title').html();
	return title == editNetWorkWinTitle;
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
function addNetWork() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!add.action');
	form.submit();
	ck_showProcessingImg();
}

/**
 * 编辑模版操作
 */
function updateNetWork() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 删除模版
 */
function deleteNetWork() {
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
	return 'pagesize=' + eachPageData + '&cloudContext.params.qName=' + qName
			+ '&pager.offset=' + offset;
}