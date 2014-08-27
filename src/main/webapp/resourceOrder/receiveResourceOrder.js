var confirmAgreeWinTitle = '同意申请资源工单';
var confirmRejectWinTitle = '拒绝申请资源工单';
var addOrUpdateHtml = 'resourceOrder/addOrUpdateWindowForReceive_.jsp';
var baseURL = 'receiveResourceOrderManager/receiveResourceOrder';
// 当前操作的ID
var currentId = 0;
$(function() {
			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (isAgree()) {
							agreeReceiveResourceOrder();
							return;
						}
						if (isReject()) {
							rejectReceiveResourceOrder();
							return;
						}
					});
		});
/**
 * 初始化同意
 */
function initAgree(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : confirmAgreeWinTitle,
				content : "是否确认同意"
			});
}

/**
 * 是否同意
 */
function isAgree() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmAgreeWinTitle;
}

/**
 * 同意
 */
function agreeReceiveResourceOrder() {
	location = baseURL + '!agree.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}
/**
 * 拒绝
 */
function rejectReceiveResourceOrder() {
	location = baseURL + '!reject.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId;
	ck_showProcessingImg();
}

/**
 * 初始化拒绝
 */
function initReject(id) {
	currentId = id;
	ck_pop_win({
		width : 400,
		title : confirmRejectWinTitle,
		content : '拒绝原因：<textarea cols="50" rows="10" id="rejectMsg"></textarea>'
	});
}

/**
 * 是否拒绝
 */
function isReject() {
	var title = $('.ck_pop_win_title').html();
	return title == confirmRejectWinTitle;
}

/**
 * 拒绝
 */
function rejectReceiveResourceOrder() {
	location = baseURL + '!reject.action?' + createQueryParam()
			+ '&cloudContext.vo.id=' + currentId + '&cloudContext.params.rejectMsg='
			+ $("#rejectMsg").val();
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