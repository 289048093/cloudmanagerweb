<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
	<head>
		<meta name="mask" content="false" />
		<script type="text/javascript"
			src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
		<style type="text/css">
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

#btnPrint {
	margin-top: 10px;
	float: right;
	padding: 5px 20px;
	font-size: 12px;
	display: block;
	background-color: #E0EEE0;
}

#btnPrint :hover           ,#operationHeaderWrapper :hover {
	cursor: pointer;
	background-color: #EE8262;
}
</style>

		<style type="text/css" media="print">
.noprint {
	display: none;
}
</style>
		<script type="text/javascript">
	$(function(){ 
		$("#operationHeaderWrapper").toggle(function(){
			$("#operationContentWrapper").show();
			$("#hide_flag").html(">>");
		},function(){
			$("#operationContentWrapper").hide();
			$("#hide_flag").html("<<");
		})
	});
</script>
	</head>
	<body style="text-align: center;">
		<div style="margin: 0px auto; padding: 0px auto; width: 980px;">
			<div class="noprint">
				<span id="btnPrint" onclick="javascript:window.print()">打印</span>
			</div>
			<table width="980" border="1" style="margin-bottom: 20px;">
				<thead>
					<th colspan="4"
						style="background-color: #EBEBEB; height: 50px; font-size: 14px;">
						计算节点资源申请工单
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
							标题
						</td>
						<td class="td_context" colspan="3">
							${cloudContext.vo.title}
						</td>

					</tr>
					<tr width="100%">
						<td class="td_header">
							创建时间
						</td>
						<td class="td_context">
							<s:property value="#request.cloudContext.vo.createTime" />
						</td>
						<td class="td_header">
							工单状态
						</td>
						<td class="td_context">
							<s:if
								test="#request.cloudContext.vo.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_ACCEPING">
			 					 申请中
			 				</s:if>
							<s:elseif
								test="#request.cloudContext.vo.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_AGREE">
			 					同意
			 				</s:elseif>
							<s:elseif
								test="#request.cloudContext.vo.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_REJECT">
			 					拒绝
			 				</s:elseif>
							<s:else>
			 					未知
			 				</s:else>
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							创建人
						</td>
						<td class="td_context">
							${cloudContext.params.sender}【${cloudContext.params.sendDomain}】
						</td>
						<td class="td_header">
							处理人
						</td>
						<td class="td_context">
							${cloudContext.params.receiver}【${cloudContext.params.receiveDomain}】
						</td>
					</tr>
					<tr width="100%">
						<td class="td_header">
							修改时间
						</td>
						<td class="td_context">
							<s:property value="#request.cloudContext.vo.updateTime" />
						</td>
						<td class="td_header">
							结束时间
						</td>
						<td class="td_context">
							<s:property value="#request.cloudContext.vo.closeTime" />
						</td>
					</tr>
					<tr width="100%">
						<td width="100%" colspan="4" class="tr_header">
							工单内容
						</td>
					</tr>
					<tr width="100%">
						<td colspan="4" class="context_td">
							${cloudContext.vo.content}
						</td>
					</tr>
					<tr width="100%">
						<td width="100%" colspan="4" class="tr_header"
							id="operationHeaderWrapper">
							操作流程
							<a id='hide_flag'>>></a>
						</td>
					</tr>
					<tr width="100%" id="operationContentWrapper">
						<td colspan="4" class="context_td">
							${cloudContext.params.actionContent}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</body>
</html>
