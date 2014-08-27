var currentWorkOrderId = null;
var currentWorkOrderCategoryId = null;



/**
 * 工单-虚拟机申请-通过
 * @param {} workOrderId
 * @param {} categoryId
 */
function newVirtualMachine(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "通过并创建虚拟机",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_new_vm" 
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : "receiveWorkorderManager/receiveWorkorder!initAddOrUpdateForVm.action",
					data: {"cloudContext.params.workOrderId":workOrderId},
					success : function(data) {
						$(".ck_to_be_hide").show();
						// 网络
						var netWorks = data.cloudContext.params.netWorks;
						if (netWorks.length == 0) {
							$("#netWork").siblings(".errorMsg").text("没有可用网络");
						}
						for (var i = 0; i < netWorks.length; i++) {
							$("#netWork").append("<option value='"
									+ netWorks[i].id + "'>" + netWorks[i].name
									+ "</option>");
						}
						// 解决情况
						var solutions = data.cloudContext.params.solutions;
						if (solutions.length == 0) {
							$("#workOrderSolutions").siblings(".errorMsg").text("没有解决情况");
						}
						for (var i = 0; i < solutions.length; i++) {
							if(solutions[i].id==2){
								$("#workOrderSolutions").append("<option value='"
									+ solutions[i].id + "' selected >" + solutions[i].name
									+ "</option>");
							}else{
								$("#workOrderSolutions").append("<option value='"
										+ solutions[i].id + "'>" + solutions[i].name
										+ "</option>");
							}
						}
						$("#workOrderSolutions").attr("disabled","disabled");
						
						//默认虚拟机名称
						$("#virtualMachineName").val(data.cloudContext.params.defaultVirtualMachineName);
						currentWorkOrderId = workOrderId;
						currentWorkOrderCategoryId = categoryId;
					}
				});
			});
}

/**
 * 创建虚拟机
 */
$("#confirm_to_new_vm").live("click",function() {
		var workOrderId = currentWorkOrderId;
		var virtualMachineName = $("#virtualMachineName").val();
		var workOrderSolutions = $("#workOrderSolutions").val();
		var handleMsg = $("#handleMsg").val();
		var netWork = $("#netWork").val();
		var desc = $("#desc").val();
		var autoFlag = $(".ck_radio:checked").val();
		var computeResourcePool = $("#computeResourcePool").val();
		var computeResourceNode = $("#computeResourceNode").val();
		
		if(!virtualMachineName){
			alert("请指定虚拟机名称");
			return;
		}
		if(!workOrderSolutions){
			alert("请指定解决情况");
			return;
		}
		
		if(!currentWorkOrderId){
			alert("没有指定操作订单列");
			return;
		}
		
		if (!netWork) {
			alert("请指定网络");
			return;
		}
		
		if(autoFlag=='true'){
			if(computeResourcePool =="" || computeResourceNode =="" || computeResourceNode==null){
				alert("请指定计算节点");
				return;
			}
		}
		
		$.ajax({
				type : 'post',
				async : false,
				url : "receiveWorkorderManager/receiveWorkorder!newVirtualMachine.action",
				data : {
					"cloudContext.params.workOrderId" : workOrderId,
					"cloudContext.params.handleMsg" : handleMsg,
					"cloudContext.params.netWork" : netWork,
					"cloudContext.params.desc" : desc,
					"cloudContext.params.customComputeResourceFlag" : autoFlag,
					"cloudContext.params.computeResourcePool" : computeResourcePool,
					"cloudContext.params.computeResourceID" : computeResourceNode,
					"cloudContext.params.workOrderSolutions" : workOrderSolutions,
					"cloudContext.params.virtualMachineName" : virtualMachineName,
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
 * 工单-删除虚拟机-通过
 * @param {} workOrderId
 * @param {} categoryId
 */
function delVirtualMachineApprove(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "确认通过该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_approve_delVm" 
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
							if(solutions[i].id==2){
								$("#workOrderSolutions").append("<option value='"
									+ solutions[i].id + "' selected >" + solutions[i].name
									+ "</option>");
							}else{
								$("#workOrderSolutions").append("<option value='"
										+ solutions[i].id + "'>" + solutions[i].name
										+ "</option>");
							}
						}
						$("#workOrderSolutions").attr("disabled","disabled");
						currentWorkOrderId = workOrderId;
						currentWorkOrderCategoryId = categoryId;
					}
				});
			});
}

/**
 * 确定删除虚拟机
 */
$("#confirm_to_approve_delVm").live("click",function() {
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
				url : "receiveWorkorderManager/receiveWorkorder!deleteVirtualMachine.action",
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
 * 工单-删除虚拟机-拒绝
 * @param {} workOrderId
 * @param {} categoryId
 */
function delVirtualMachineReject(workOrderId,categoryId) {

	ck_pop_win({
				width : 400,
				title : "确认拒绝该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_reject_delVm" 
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
 * 取消删除虚拟机
 */
$("#confirm_to_reject_delVm").live("click",function() {
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
		
		if(!handleMsg || handleMsg=='' ){
			alert("请输入拒绝理由");
			return;
		}
		
		$.ajax({
				type : 'post',
				async : false,
				url : "receiveWorkorderManager/receiveWorkorder!rejectDelVirtualMachine.action",
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
 * 工单-删除虚拟机-关闭
 * @param {} workOrderId
 * @param {} categoryId
 */
function closeVirtualMachineReject(workOrderId,categoryId) {

	ck_pop_win({
				width : 400,
				title : "确认关闭该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_close_delVm" 
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
 * 关闭删除虚拟机
 */
$("#confirm_to_close_delVm").live("click",function() {
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
		
		if(!handleMsg || handleMsg=='' ){
			alert("请输入拒绝理由");
			return;
		}
		
		$.ajax({
				type : 'post',
				async : false,
				url : "receiveWorkorderManager/receiveWorkorder!closeDelVirtualMachine.action",
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
 * 工单-虚拟机申请-拒绝
 * @param {} workOrderId
 * @param {} categoryId
 */
function rejectNewVirtualMachine(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "确认拒绝该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_reject_newVm" 
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
 * 取消新建虚拟机
 */
$("#confirm_to_reject_newVm").live("click",function() {
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
		
		if(!handleMsg || handleMsg=='' ){
			alert("请输入拒绝理由");
			return;
		}
		
		$.ajax({
				type : 'post',
				async : false,
				url : "receiveWorkorderManager/receiveWorkorder!rejectNewVirtualMachine.action",
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
 * 工单-虚拟机申请-关闭
 * @param {} workOrderId
 * @param {} categoryId
 */
function closeNewVirtualMachine(workOrderId,categoryId) {
	ck_pop_win({
				width : 400,
				title : "确认关闭该工单吗?",
				url : 'workorder/addOrUpdateWindowVm_.jsp',
				submitId : "confirm_to_close_newVm" 
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
 * 关闭新建虚拟机
 */
$("#confirm_to_close_newVm").live("click",function() {
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
				url : "receiveWorkorderManager/receiveWorkorder!closeNewVirtualMachine.action",
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

// 查询计算节点
$(".ck_radio").live("change", function() {
			var manualFlag = $(this).val();
			if (manualFlag == "true") {
				queryResourcePool();
			} else {
				$("#addOrUpdateForm .ck_hidden").hide();
				$("#computeResourcePool").empty();
				$("#computeResourceNode").empty();
			}
});
$("#computeResourcePool").live("change", function() {
	var poolId = $(this).val();
	if(poolId!="" && poolId != null && poolId !='undefined'){
		if(currentWorkOrderId != null){
			queryResourceByPoolAndCpuMemory(poolId, currentWorkOrderId);
		}
	}
});

//查询计算资源池
function queryResourcePool() {
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : "virtualMachineManager/virtualMachine!queryResourcePoolByCurrentDomain.action",
		success : function(data) {
			var computeResourcePools = data.cloudContext.params.computeResourcePools;
			if (computeResourcePools.length == 0) {
				return;
			}
			$("#computeResourcePool").empty();
			$("#computeResourcePool")
					.append("<option value=''>--请选择--</option>");
			for (var i = 0; i < computeResourcePools.length; i++) {
				$("#computeResourcePool").append("<option value='"
						+ computeResourcePools[i].id + "'>"
						+ computeResourcePools[i].name + "</option>");
			}
		}
	});
	$("#computeResourcePoolTr").toggle();
}


// 获取指定资源池下的计算节点，并且cpu内存参数大于cpu,memeory
function queryResourceByPoolAndCpuMemory(poolId,currentWorkOrderId) {
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : "receiveWorkorderManager/receiveWorkorder!queryResourceByPoolAndCpuMemory.action",
				data : {
					"cloudContext.params.poolId" : poolId,
					"cloudContext.params.workOrderId" : currentWorkOrderId
				},
				success : function(data) {
					var computeResources = data.cloudContext.params.computeResources;
					if (computeResources.length == 0) {
						return;
					}
					$("#computeResourceNode").html("<option value=''>--请选择--</option>");
					for (var i = 0; i < computeResources.length; i++) {
						$("#computeResourceNode").append("<option value='"
								+ computeResources[i].id + "'>"
								+ computeResources[i].name + "</option>");
					}
				}
			});
	$("#computeResourceNodeTr").toggle();
}
