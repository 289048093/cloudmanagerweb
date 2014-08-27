var currentWorkOrderId = null;
var currentWorkOrderCategoryId = null;
/*
 * 新建普通工单
 */
$('#ckNewWorkOrder').live('click', function() {
	var position = $(this).position();
	var height = $(this).height() +5;
	$.ajax({
		type : 'post',
		async : false,
		url : "sendWorkorderManager/sendWorkorder!queryWorkOrderCategoryForSend.action",
		success : function(data) {
			var categorys = data.cloudContext.params.categorys;
			if (categorys.length === 0) {
				return;
			}
			var domHtml = "<ul>";
			for (var i = 0; i < categorys.length; i++) {
				domHtml += '<li categoryId="'+ categorys[i].id +'"><a href="javascript:void(0)">'+ categorys[i].name+'</a></li>';
			}
			domHtml += "</ul>";
			$(".ck_dropDown").html(domHtml);
			$(".ck_dropDown").css("left",position.left+5).css("top",position.top+height).toggle("slow");
		}
	});
	
});

$(document).bind("click",function(e){
  var target  = $(e.target);
  if(target.closest("#ckNewWorkOrder").length == 0){
       $(".ck_dropDown").empty().hide();
  }
 }) 
 
 /**
  *查看附件
 */
 function viewAttachment(workOrderId,categoryId){
 	currentWorkOrderId = workOrderId;
 	currentWorkOrderCategoryId = categoryId;
 	ck_pop_win({
		width : 400,
		title : '附件列表',
		content : '加载中',
		noButton:true
	}, loadAttachment);
 }
 /**
  *显示附件
 */
 function loadAttachment(){
 	$.ajax({
		type : 'post',
		async : false,
		url : "sendWorkorderManager/sendWorkorder!initAttachments.action",
		data : {
			"cloudContext.params.workOrderId" : currentWorkOrderId
		},
		success : function(data) {
			var attachements = data.cloudContext.params.attachements;
			if (attachements.length == 0) {
				$("#ck_pop_win_form").html("没有附件");
				return;
			}
			var domHtml = "<ul>";
			var opHtml = "";
			for (var i = 0; i < attachements.length; i++) {
				if(attachements[i].workOrderStatus==1){
					opHtml = "<a style='margin-right:10px;' title='下载' href='javascript:downloadAttachment("+attachements[i].id+")' >下载</a>"
							 +"<a  title='下载' href='javascript:deleteAttachment("+attachements[i].id+")' >删除</a>";
				}else{
					opHtml = "<a title='下载' href='javascript:downloadAttachment("+attachements[i].id+")' >下载</a>";
				}
				domHtml += "<li class='attachmentLi' woaId="+attachements[i].id+">"
							+ "<div style='float:left'>"+attachements[i].fileName +"</div>"
							+ "<div style='float:right'>"+opHtml+"</div>"
						+ "</div></li><br/>";
			}
			domHtml += "</ul>";
			$("#ck_pop_win_form").html(domHtml);
		}
	});
 }
 
 function downloadAttachment(id) {
	window.open("sendWorkorderManager/sendWorkorder!downloadWorkOrderAttachment.action?cloudContext.params.attachmentId="+id)
}

 function deleteAttachment(attachmentId) {
 	var answer = confirm("确认删除该附件?");
	if(!answer){
		return;
	}
	$.ajax({
		type : 'post',
		async : true,
		url : "sendWorkorderManager/sendWorkorder!deleteAttachment.action",
		data : {
			"cloudContext.params.attachmentId" : attachmentId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				 $(".attachmentLi[woaId="+attachmentId+"]").remove();
				 alert("操作成功");
			} else {
				var errorMsgList = data.cloudContext.errorMsgList;
				if (errorMsgList) {
					var errorMsg = "";
					for (var i = 0; i < errorMsgList.length; i++) {
						errorMsg += errorMsgList[i];
					}
					if (errorMsg != "") {
						alert(errorMsg);
					}
				}
			}

		}
	});
}

$(".ck_dropDown li").live("click",function(){
	var value = $(this).attr("categoryId");
	//资源工单
	if(value==4){
		$(".ck_dropDown").hide();
		ck_pop_win({
						width : 400,
						title : '新建资源工单',
						url : 'workorder/addOrUpdateWindowForSend_.jsp'
					}, listenSubmitClickForRes);
	}
	//新建普通工单
	else if(value==5){
		$(".ck_dropDown").hide();
		ck_pop_win({
						width : 400,
						title : '新建普通工单',
						url : 'workorder/addOrUpdateWindowForSendNormalOrder_.jsp',
						submitId:"ck_confirm_new_common_workorder"
					}, listenSubmitClickForNormalOrder);
	}
});
/**
 * 初始化更新窗体
 */
function listenSubmitClickForNormalOrder() {
	//初始化信息
	$.ajax({
		type : 'post',
		async : false,
		url : "sendWorkorderManager/sendWorkorder!initCommonOrderInfo.action",
		success : function(data) {
			var sendDomains = data.cloudContext.params.currentDomains;
			$("#sendDomain").empty();
			$("#sendDomain").append("<option>--请选择--</option>");
			for(var i in sendDomains){
				$("#sendDomain").append("<option value='"+sendDomains[i].id+"'>"+sendDomains[i].name+"</option>");
			}
			// 接收域
			var domains = data.cloudContext.params.domains;
			if (domains.length == 0) {
				$("#receiveDomain").siblings(".errorMsg").text("没有子域或子域没有管理员");
				return;
			}
			$("#receiveDomain").empty();
			$("#receiveDomain").append("<option>--请选择--</option>");
			for (var i = 0; i < domains.length; i++) {
				$("#receiveDomain").append("<option value='"
						+ domains[i].id + "'>" + domains[i].name
						+ "</option>");
			}
		}
	});
	//提交
	$('#ck_confirm_new_common_workorder').live('click', function() {
		var title= $("#title").val();
		var content = $("#content").val();
		var receiveDomain = $("#receiveDomain").val();
		if(!title){
			alert('标题不为空');
			return;
		}
		if(!content){
			alert('内容不为空');
			return;
		}
		if(!receiveDomain){
			alert('接收域不为空');
			return;
		}
		var title = $('.ck_pop_win_title').html();
		var form = $('#addOrUpdateForm');
		form.attr('action','sendWorkorderManager/sendWorkorder!insertCommonWorkOrder.action');
		form.submit();
		ck_showProcessingImg();
	});
}

/**
 * 完成工单 - 关闭
 * @param {} workOrderId
 * @param {} workOrderCategoryId
 */
function closeWorkOrder(workOrderId,categoryId){
	if(!workOrderId || !categoryId){
		return;
	}
	if(categoryId == 4){
		closeResourceOrder(workOrderId,categoryId);
	} 
	else if(categoryId == 5){
		insertCommonWorkOrderClose(workOrderId,categoryId);
	} 
}


/**
 * 工单-关闭-普通工单
 * @param {} workOrderId
 * @param {} categoryId
 */
function insertCommonWorkOrderClose(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "确认关闭该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_close_cwo" 
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : "receiveWorkorderManager/receiveWorkorder!initAddOrUpdateForVm.action",
					success : function(data) {
						$(".ck_to_be_hide").hide();
						// 解决情况
						var solutions = data.cloudContext.params.solutions;
						if (solutions.length == 0) {
							$("#workOrderSolutions").siblings(".errorMsg").text("没有解决情况");
						}
						for (var i = 0; i < solutions.length; i++) {
								$("#workOrderSolutions").append("<option value='"
										+ solutions[i].id + "'>" + solutions[i].name
										+ "</option>");
						}
						currentWorkOrderId = workOrderId;
						currentWorkOrderCategoryId = categoryId;
					}
				});
			});
}
/**
 * 工单-关闭-普通工单
 */
$("#confirm_to_close_cwo").live("click",function() {
		var workOrderId = currentWorkOrderId;
		var workOrderSolutions = $("#workOrderSolutions").val();
		var handleMsg = $("#handleMsg").val();
		 
		if(!workOrderSolutions){
			alert("请指定解决情况");
			return;
		}
		
		if(!currentWorkOrderId){
			alert("没有指定操作订单列");
			return;
		} 
		$.ajax({
				type : 'post',
				async : false,
				url : "sendWorkorderManager/sendWorkorder!insertCommonWorkOrderClose.action",
				data : {
					"cloudContext.params.workOrderId" : workOrderId,
					"cloudContext.params.handleMsg" : handleMsg,
					"cloudContext.params.workOrderSolutions" : workOrderSolutions,
					"cloudContext.params.categoryId" : currentWorkOrderCategoryId
				},
				success : function(data) { 
					if (data.cloudContext.successIngoreWarn) {
						alert("操作成功");
						window.location.reload();
					} else {
						var msgList = data.cloudContext.errorMsgList;
						var errorStr = "";
						for (var i = 0; i < msgList.length; i++) {
							errorStr += msgList[i];
						}
						alert(errorStr);
						window.location.reload();
					}
				}
			}); 
});