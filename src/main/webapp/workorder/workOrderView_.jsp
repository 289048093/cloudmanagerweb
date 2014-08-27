<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
		<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$("#operationContentWrapper").hide();
				$("#operationHeaderWrapper").toggle(function(){
					$("#operationContentWrapper").show();
					$("#hide_flag").html(">>");
				},function(){
					$("#operationContentWrapper").hide();
					$("#hide_flag").html("<<");
				})
			});
		</script>
	<style type="text/css">
<!--
body {
	font-family: '微软雅黑'
}

.tr_header {
	background-color: #ccc;
	font-weight: bold
}

.td_header {
	background-color: #EBEBEB;
	width: 15%;
	font-size: 12px;
	padding: 5px 5px;
}

.td_context {
	width: 35%;
	font-size: 12px;
}

.context_td {
	padding-left: 40px;
	padding-top: 10px;
	padding-bottom: 10px;
	font-size: 12px;
}

#btnPrint{
	margin-top: 10px;
	float: right;
	padding: 5px 20px;
	font-size: 12px;
	display: block;
	background-color: #E0EEE0;
}

#btnPrint:hover,#operationHeaderWrapper:hover {
	cursor: pointer;
	background-color: #EE8262;
} 
-->
</style>
<style type="text/css" media="print">
.noprint {
	display: none;
}
</style>

	<body style="text-align: center;">
		<div style="margin: 0px auto; padding: 0px auto; width: 980px;">
			<div class="noprint">
				<span id="btnPrint" onclick="javascript:window.print()">打印</span>
			</div>
			<table width="980" border="1" style="margin-bottom: 20px;">
				<thead>
					<th colspan="4"
						style="background-color: #EBEBEB; height: 50px; font-size: 14px;">
						云平台工单浏览
					</th>
				</thead>
				<tbody>
					<tr width="100%">
						<td width="100%" colspan="4" class="tr_header">
							基本信息
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							流水号
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.serialNumber}
						</td>
						<td class="td_header">
							状态
						</td>
						<td class="td_context">
							<s:if test="%{cloudContext.params.workOrder.status==1}">
			 					处理中
			 				</s:if>
							<s:elseif test="%{cloudContext.params.workOrder.status==2}">
			 					已完成
			 				</s:elseif>
							<s:elseif test="%{cloudContext.params.workOrder.status==3}">
			 					已关闭
			 				</s:elseif>
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							标题
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.title}
						</td>
						<td class="td_header">
							工单类别
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.categoryName}
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							创建域
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.sendDomainName}
						</td>
						<td class="td_header">
							创建人员
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.sendUserName}
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							接收域
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.receiveDomainName}
						</td>
						<td class="td_header">
							接收人员
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.receiverName}
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							创建时间
						</td>
						<td class="td_context">
							<s:date name="#request.cloudContext.params.workOrder.createTime"
								format="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td class="td_header">
							到期时间
						</td>
						<td class="td_context">
							<s:date name="#request.cloudContext.params.workOrder.dueDate"
								format="yyyy-MM-dd HH:mm:ss" />
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							工单方案
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.solutionName}
						</td>
						<td class="td_header">
							工单解决备注
						</td>
						<td class="td_context">
							${cloudContext.params.workOrder.solveMsg}
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							解决时间
						</td>
						<td class="td_context">
							<s:date
								name="#request.cloudContext.params.workOrder.solutionTime"
								format="yyyy-MM-dd HH:mm:ss" />
						</td>
						<td class="td_header">
							关闭时间
						</td>
						<td class="td_context">
							<s:date name="#request.cloudContext.params.workOrder.closeTime"
								format="yyyy-MM-dd HH:mm:ss" />
						</td>
					</tr>
					<tr width="100%">
						<td width="100%" colspan="4" class="tr_header">
							工单内容
						</td>
					</tr>
					<tr width="100%">
						<td colspan="4" class="context_td">
							${cloudContext.params.workOrder.content}
						</td>
					</tr>
					<tr width="100%">
						<td width="100%" colspan="4" class="tr_header"
							id="operationHeaderWrapper">
							操作流程
							<a id='hide_flag'><<</a>
						</td>
					</tr>
					<tr width="100%" id="operationContentWrapper">
						<td colspan="4" class="context_td">
							<s:if
								test="#request.cloudContext.params.workOrderActions.size()>0">
								<s:iterator
									value="#request.cloudContext.params.workOrderActions"
									var="workOrderAction" status="st">
									<b style="color: #326ca6;"><s:property
											value="#request.workOrderAction.action" /> </b>
									<br />
									<s:property value="#request.workOrderAction.content" />
									<br />
									<!--  操作人：<s:property value="#request.workOrderAction.actionUserName" />
									<br />-->
									<font color="#777"><s:date
											name="#request.workOrderAction.createTime"
											format="yyyy-MM-dd HH:mm:ss" /> </font>
									<br /><br />
								</s:iterator>
							</s:if>
						</td>
					</tr>
				</tbody>

			</table>
		</div>
	</body>
</html>
