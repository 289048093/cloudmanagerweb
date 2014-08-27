/**
 * 资源申请单分页请求
 * 
 * @param {}
 *            page
 */
function woApplyPager(targetPage){
	if(!targetPage){
		return;
	}
	var dominId = $("#ck_res_domains").val();
	$.ajax({
		type : 'post',
		async : true,
		url : "dashboardManager/dashboard!queryHandlingWorkOrderOnPageChange.action",
		data : {
			"cloudContext.params.targetPage" : targetPage,
			"cloudContext.params.domainId" : dominId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				renderWorkOrderData(data);
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
 
/**
 * 接收的工单域切换
 * 
 * @param {}
 *            _this
 */
function queryWorkOrderOnDomainChange(_this){
	var domainId = _this.value;
	$.ajax({
		type : 'post',
		async : true,
		url : "dashboardManager/dashboard!queryHandlingWorkOrderOnDomainChange.action",
		data : {
			"cloudContext.params.domainId" : domainId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				 renderWorkOrderData(data);
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

// 拼装资源订单表格数据
function renderWorkOrderData(data){
	var order = data.cloudContext.params.workOrders;
	var domHtml = "";
	var statusHtml = "";
	if(order){
		for(var i=0; i<order.length; i++){
			if(order[i].status==1){
				statusHtml = "处理中";
			}else if(order[i].status==2){
				statusHtml = "已处理";
			}else if(order[i].status==3){
				statusHtml = "已关闭";
			}
			var attachmentHtml = "";
			if(order[i].attachmentCount>0){
				attachmentHtml = '<a href="javascript:viewAttachment('+order[i].id+','+order[i].categoryId+')" target="_blank" class="ck_attachment" title="查看附件[${item.attachmentCount }个]" '
								+'style="display: block !important" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!viewAttachment.action"></a>';
			}
			var opHtml = "";
			if(order[i].status==1){
				if(order[i].categoryId==5){
					opHtml += 	'<a href="javascript:approveWorkOrder('+order[i].id+','+order[i].categoryId+')" class="ck_approve" title="解决" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>'
								+'<a href="javascript:closeWorkOrder('+order[i].id+','+order[i].categoryId+')" class="ck_del2" title="关闭" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>'
								+ attachmentHtml
								+'<a href="receiveWorkorderManager/receiveWorkorder!view.action?workOrderId='+order[i].id+'" target="_blank" class="ck_view" title="浏览"'
									+' chkRightsUrl="receiveWorkorderManager/receiveWorkorder!view.action"></a>';
				}else{
					opHtml += 	'<a href="javascript:approveWorkOrder('+order[i].id+','+order[i].categoryId+')" class="ck_approve" title="通过" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>'
								+'<a href="javascript:rejectWorkOrder('+order[i].id+','+order[i].categoryId+')" class="ck_reject" title="拒绝" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>'
								+'<a href="javascript:closeWorkOrder('+order[i].id+','+order[i].categoryId+')" class="ck_del2" title="关闭" chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>'
								+ attachmentHtml
								+'<a href="receiveWorkorderManager/receiveWorkorder!view.action?workOrderId='+order[i].id+'" target="_blank" class="ck_view" title="浏览"'
									+' chkRightsUrl="receiveWorkorderManager/receiveWorkorder!view.action"></a>';
				}
			}
			domHtml += '<tr>'
						+'<td title="'+order[i].title+'">'+order[i].title+'</td>'
						+'<td title="'+emptyText(order[i].categoryName)+'">'+emptyText(order[i].categoryName)+'</td>'
						+'<td title="'+emptyText(order[i].sendDomainName)+'">'+emptyText(order[i].sendDomainName)+'</td>'
						+'<td title="'+emptyText(order[i].content)+'">'+emptyText(order[i].content)+'</td>'
						+'<td title="'+statusHtml+'">'+statusHtml+'</td>'
						+'<td title="'+formatTime(order[i].createTime)+'">'+formatTime(order[i].createTime)+'</td>'
						+'<td class="ck_table_operation_td">' 
							+'<div class="ck_table_operation">'
								+opHtml
							+'</div>'
						+'</td>'
					+'</tr>';
		}
	}
	$("#woApplyFormBody").html(domHtml);
		// 刷新分页数据
		refreshWoPager(data.cloudContext.params.woPageInfo);
		// 显示有权限的按钮
		$("[chkRightsUrl]").each(function(i) {
					if (rightsUrls.indexOf($(this).attr("chkRightsUrl")) != -1) {
						$(this).css("display", "block");
					}
				});
}
// 刷新分页数据
function refreshWoPager(pageInfo){
	var currentPage = pageInfo.nowPage;
	var totalPage = pageInfo.pageCount;
	var nextPage = currentPage + 1;
	var prePage = currentPage -1;
	var firstPage = 1;
	var totalRecord = pageInfo.dataCount;
	
	$("#rsNowPage").text(currentPage);
	$("#rsTotalPage").text(totalPage);
	$("#rsTotalCount").text(totalRecord);
	
	$("#ck_res_footer_div .ck_pager_inactive").removeClass("ck_pager_inactive");
	
	//只有一页
	if(totalPage == 1){
		$("#ck_table_paging_next_rs").html('<a href="javascript:void(0)">下一页</a>');
		$("#ck_table_paging_last_rs").html('<a href="javascript:void(0)">尾页</a>');
		$("#ck_table_paging_next_rs a").addClass("ck_pager_inactive");
		$("#ck_table_paging_last_rs a").addClass("ck_pager_inactive");
		
		$("#ck_table_paging_pre_rs").html('<a href="javascript:void(0)">上一页</a>');
		$("#ck_table_paging_first_rs").html('<a href="javascript:void(0);">首页</a>');
		$("#ck_table_paging_pre_rs a").addClass("ck_pager_inactive");
		$("#ck_table_paging_first_rs a").addClass("ck_pager_inactive");
	}
	// 尾页
	else if(currentPage==totalPage){
		$("#ck_table_paging_first_rs").html('<a href="javascript:void(0);" onclick="woApplyPager('+firstPage+')">首页</a> ');
		$("#ck_table_paging_pre_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+prePage+')">上一页</a>');
		$("#ck_table_paging_next_rs").html('<a href="javascript:void(0)">下一页</a>');
		$("#ck_table_paging_last_rs").html('<a href="javascript:void(0)">尾页</a>');
		$("#ck_table_paging_next_rs a").addClass("ck_pager_inactive");
		$("#ck_table_paging_last_rs a").addClass("ck_pager_inactive");
	}
	// 首页
	else if(currentPage==1){
		$("#ck_table_paging_last_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+totalPage+')">尾页</a>');
		$("#ck_table_paging_next_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+nextPage+')">下一页</a>');
		$("#ck_table_paging_pre_rs").html('<a href="javascript:void(0)">上一页</a>');
		$("#ck_table_paging_first_rs").html('<a href="javascript:void(0);">首页</a>');
		$("#ck_table_paging_pre_rs a").addClass("ck_pager_inactive");
		$("#ck_table_paging_first_rs a").addClass("ck_pager_inactive");
	}
	// 中间页
	else{
		$("#ck_table_paging_last_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+totalPage+')">尾页</a>');
		$("#ck_table_paging_next_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+nextPage+')">下一页</a>');
		$("#ck_table_paging_pre_rs").html('<a href="javascript:void(0)" onclick="woApplyPager('+prePage+')">上一页</a>');
		$("#ck_table_paging_first_rs").html('<a href="javascript:void(0);" onclick="woApplyPager('+firstPage+')">首页</a> ');
	}
	
	
}