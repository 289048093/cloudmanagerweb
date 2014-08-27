var currentWorkOrderId = null;
var currentWorkOrderCategoryId = null;
/**
 * 工单-解决
 * @param {} workOrderId
 * @param {} categoryId
 */
function solveCommonWorkOrder(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "解决工单",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_solve_cwo" 
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : "receiveWorkorderManager/receiveWorkorder!initAddOrUpdateForVm.action",
					data: {"cloudContext.params.workOrderId":workOrderId},
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
 * 解决工单
 */
$("#confirm_to_solve_cwo").live("click",function() {
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
				url : "receiveWorkorderManager/receiveWorkorder!updateCommonWorkOrderSovle.action",
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

/**
 * 工单-关闭
 * @param {} workOrderId
 * @param {} categoryId
 */
function closeCommonWorkOrder(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "关闭工单",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_close_cwo" 
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : "receiveWorkorderManager/receiveWorkorder!initAddOrUpdateForVm.action",
					data: {"cloudContext.params.workOrderId":workOrderId},
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
 * 解决工单
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
				url : "receiveWorkorderManager/receiveWorkorder!updateCommonWorkOrderClose.action",
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
