/**
 * 初始化更新窗体
 */
function listenSubmitClickForRes() {
	$.ajax({
		url:"sendWorkorderManager/sendWorkorder!initApplyResourceWorkerOrder.action",
		success:function(data){
			var domains = data.cloudContext.params.currentDomains;
			$('#sendDomain').empty();
			$('#sendDomain').append('<option>--请选择--</option>');
			for(var i in domains){
				$('#sendDomain').append('<option value="'+domains[i].id+'">'+domains[i].name+'</option>');
			}
		}
	});
	
	
	$('.ck_confirm').live('click', function() {
		if(!validate()){
			return;
		}
		var title = $('.ck_pop_win_title').html();
		var form = $('#addOrUpdateForm');
		form.attr('action','sendWorkorderManager/sendWorkorder!addResourceOrder.action');
		form.submit();
	});
}
					

/**
 * 工单-关闭-资源申请
 * @param {} workOrderId
 * @param {} categoryId
 */
function closeResourceOrder(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "确认关闭该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_close_res" 
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
 * 关闭资源申请
 */
$("#confirm_to_close_res").live("click",function() {
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
				url : "sendWorkorderManager/sendWorkorder!insertResourceOrderClose.action",
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