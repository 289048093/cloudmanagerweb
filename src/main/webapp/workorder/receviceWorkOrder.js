/**
 * 完成工单 - 通过
 * @param {} workOrderId
 * @param {} workOrderCategoryId
 */
function approveWorkOrder(workOrderId,categoryId){
	if(!workOrderId || !categoryId){
		return;
	}
	//判断处理类型,内置固定变量 - 参照constant变量表
	//新建虚拟机
	if(categoryId == 1){
		newVirtualMachine(workOrderId,categoryId);
	}
	//修改虚拟机
	else if(categoryId == 2){
		
	}
	//删除虚拟机
	else if(categoryId == 3){
		delVirtualMachineApprove(workOrderId,categoryId);
	} 
	//资源申请
	else if(categoryId == 4){
		approveResourceOrder(workOrderId,categoryId);
	}
	//普通工单
	else if(categoryId == 5){
		solveCommonWorkOrder(workOrderId,categoryId);
	}
}

/**
 * 完成工单 - 取消
 * @param {} workOrderId
 * @param {} workOrderCategoryId
 */
function rejectWorkOrder(workOrderId,categoryId){
	if(!workOrderId || !categoryId){
		return;
	}
	//判断处理类型,内置固定变量 - 参照constant变量表
	//新建虚拟机
	if(categoryId == 1){
		rejectNewVirtualMachine(workOrderId,categoryId);
	}
	//修改虚拟机
	else if(categoryId == 2){
		
	}
	//删除虚拟机
	else if(categoryId == 3){
		delVirtualMachineReject(workOrderId,categoryId);
	} 
	//资源申请
	else if(categoryId == 4){
		rejectResourceOrder(workOrderId,categoryId);
	} 
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
	//判断处理类型,内置固定变量 - 参照constant变量表
	//新建虚拟机
	if(categoryId == 1){
		closeNewVirtualMachine(workOrderId,categoryId);
	}
	//修改虚拟机
	else if(categoryId == 2){
		
	}
	//删除虚拟机
	else if(categoryId == 3){
		closeVirtualMachineReject(workOrderId,categoryId);
	} 
	//资源申请
	else if(categoryId == 4){
		closeResourceOrder(workOrderId,categoryId);
	} 
	//普通工单
	else if(categoryId == 5){
		closeCommonWorkOrder(workOrderId,categoryId);
	}
}
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
				opHtml = "<a title='下载' href='javascript:downloadAttachment("+attachements[i].id+")' >下载</a>";
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