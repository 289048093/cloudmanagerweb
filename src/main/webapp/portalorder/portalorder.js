var currentOrderId = null;

/**
 * 通过审核
 * 
 */
function setOrderApproved(orderId) {
	ck_pop_win({
				width : 400,
				title : "是否确认通过审核?",
				url : 'portalorder/addOrUpdateWindow_.jsp',
				submitId : "confirm_to_approved"
			}, function() {
				// AJAX获取数据
				$.ajax({
					type : 'post',
					async : false,
					url : "orderManager/order!initAddOrUpdate.action",
					success : function(data) {
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
						currentOrderId = orderId;
					}
				});
			});

}


//通过订单审核
$("#confirm_to_approved").live("click",function() {
		var orderId = currentOrderId;
		var handleMsg = $("#handleMsg").val();
		var netWork = $("#netWork").val();
		var desc = $("#desc").val();
		var autoFlag = $(".ck_radio:checked").val();
		var computeResourcePool = $("#computeResourcePool").val();
		var computeResourceNode = $("#computeResourceNode").val();
		
		if(currentOrderId==null){
			alert("没有指定操作订单列");
			return;
		}
		
		if (netWork == "") {
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
				url : "orderManager/order!setOrderApproved.action",
				data : {
					"cloudContext.params.orderId" : orderId,
					"cloudContext.params.handleMsg" : handleMsg,
					"cloudContext.params.netWork" : netWork,
					"cloudContext.params.desc" : desc,
					"cloudContext.params.customComputeResourceFlag" : autoFlag,
					"cloudContext.params.computeResourcePool" : computeResourcePool,
					"cloudContext.params.computeResourceID" : computeResourceNode
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
 * 审核拒绝
 * 
 */
function setOrderRejected(orderId) {
	ck_pop_win({
		width : 400,
		title : "是否确认审核拒绝?",
		content : '操作备注：<textarea cols="50" rows="10" id="handleMsg"></textarea>',
		submitId : "confirm_to_reject"
	});
	$("#confirm_to_reject").click(function() {
				var handleMsg = $("#handleMsg").val();
				// AJAX获取数据
				$.ajax({
							type : 'post',
							async : false,
							url : "orderManager/order!setOrderRejected.action",
							data : {
								"cloudContext.params.orderId" : orderId,
								"cloudContext.params.handleMsg" : handleMsg
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
}

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
		if(currentOrderId != null){
			queryResourceByPoolAndCpuMemory(poolId, currentOrderId);
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
function queryResourceByPoolAndCpuMemory(poolId,currentOrderId) {
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : "orderManager/order!queryResourceByPoolAndCpuMemory.action",
				data : {
					"cloudContext.params.poolId" : poolId,
					"cloudContext.params.orderId" : currentOrderId
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
