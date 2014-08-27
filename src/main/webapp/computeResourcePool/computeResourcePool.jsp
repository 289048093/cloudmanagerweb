<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="001006">
		<title>计算资源池</title>
		<script type="text/javascript"
			src="computeResourcePool/computeResourcePool.js"></script>
		<script type="text/javascript" src="computeResourcePool/validate.js"></script>
		<script type="text/javascript"
			src="computeResourcePool/computeResourcemanager.js"></script>
		<script type="text/javascript"
			src="computeResourcePool/virtualMachine.js"></script>
		<link rel="stylesheet" href="style/zTreeStyle/zTreeStyle.css"
			type="text/css">
		<link rel="stylesheet" href="style/computeResourcePool.css"
			type="text/css">
		<script type="text/javascript" src="js/jquery.ztree.all-3.2.min.js"></script>
		<script type="text/javascript">
			var addSuccessCrpId = ${cloudContext.params.addSuccessCrpId==null?false:cloudContext.params.addSuccessCrpId};
			$(function (){
					if(addSuccessCrpId){
						initComputeResource(addSuccessCrpId);
					}
				}
			);
		</script>
		<style>
.ck_table_body_wrapper table tbody tr:hover {
	color: #666;
}
</style>
	</head>
	<body>
		<span class="ck_hidden" id="loginedUserName"><s:property
				value="#session.userLogin.username" /> </span>
		<s:form
			action="computeResourcePoolManager/computeResourcePool!query.action"
			theme="simple" method="post">
			<!-- content begin -->
			<div class="ck_content_wrapper">

				<div class="ck_content_header">
					<div class="ck_content_header_text">
						计算节点池
					</div>
					<div class="ck_content_header_btn_wrapper">
						<div class="ck_content_header_btn" id="ckNewComputeResourcePool"
							chkRightsUrl="computeResourcePoolManager/computeResourcePool!add.action">
							新建计算节点池
						</div>
					</div>

				</div>
				<div class="ck_content_body">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							计算节点池：
							<input type="text" name="cloudContext.params.qName"
								value="${cloudContext.params.qName[0]}" id="qName" />
							<input type="submit" value="搜索" class="ck_search" />
						</div>
					</div>
					<div class="ck_search_toggle">
						<img class="ck_toggle_img" src="images/ck_search_toggle.png"
							alt="搜索" title="搜索" />
					</div>
					<!--表格展示框-->
					<div id="ck_table_wrapper">
						<div class="ck_table">
							<div class="ck_table_header_wrapper">
								<div class="printWrapper">
									<a href="javascript:void(0)" onclick="javascript:printPage()">打印</a>
								</div>
							</div>
							<div class="ck_table_body_wrapper">
								<table cellpadding="0" cellspacing="0" class="ck_table_style"
									rules="cols">
									<thead>
										<th width="5">

										</th>
										<th width="15">
											计算节点池
										</th>
										<th width="15">
											计算节点数
										</th>
										<th width="15">
											物理cpu(核数)
										</th>
										<th width="15">
											总虚拟cpu(核数)
										</th>
										<th width="15">
											剩余虚拟cpu(核数)
										</th>
										<th width="15">
											物理内存(M)
										</th>
										<th width="15">
											总虚拟内存(M)
										</th>
										<th width="15">
											剩余虚拟内存(M)
										</th>
										<th width="15">
											cpu超配系数
										</th>
										<th width="15">
											内存超配系数
										</th>
										<th width="10">
											虚机数
										</th>
										<th width="20">
											描述
										</th>
										<th width="20">
											操作
										</th>
									</thead>
									<tbody>
										<s:if
											test="#request.cloudContext.params.computeResourcePools.size()>0">
											<s:iterator
												value="#request.cloudContext.params.computeResourcePools"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center" class="ck_pool_toggle"
														poolId="<s:property value="#request.item.id" />">
														<a style="display: block"
															href="javascript:loadComputeResource(${item.id })"
															title="设置计算节点">+</a>
													</td>
													<td id="crpNameTd_${item.id }" title="<s:property value="#request.item.name" />">
														<s:property value="#request.item.name" />
													</td>
													<td id="crpCrNumTd_${item.id }" title="<s:property value="#request.item.computeResourceNum" />">
														<s:property value="#request.item.computeResourceNum" />
													</td>
													<td id="crpTotalCpuTd_${item.id }" title="<s:property value="#request.item.totalCpu" />">
														<s:property value="#request.item.totalCpu" />
													</td>
													<td id="crpTotalVirtualCpuTd_${item.id }" title="<s:property value="#request.item.totalVirtualCpu" />">
														<s:property value="#request.item.totalVirtualCpu" />
													</td>
													<td id="crpAviCpuTd_${item.id }" title="<s:property value="#request.item.availableCpu" />">
														<s:property value="#request.item.availableCpu" />
													</td>
													<td id="crpTotalMemoryTd_${item.id }" title="<s:property value="#request.item.totalMemory" />">
														<s:property value="#request.item.totalMemory" />
													</td>
													<td id="crpTotalVirtualMemoryTd_${item.id }" title="<s:property value="#request.item.totalVirtualMemory" />">
														<s:property value="#request.item.totalVirtualMemory" />
													</td>
													<td id="crpAviMemoryTd_${item.id }" title="<s:property value="#request.item.availableMemory" />">
														<s:property value="#request.item.availableMemory" />
													</td>
													<td id="crpCpuRateTd_${item.id }" title="<s:property value="#request.item.cpuRate" />">
														<s:property value="#request.item.cpuRate" />
													</td>
													<td id="crpMemoryRateTd_${item.id }" title="<s:property value="#request.item.memoryRate" />">
														<s:property value="#request.item.memoryRate" />
													</td>
													<td id="crpVmNumTd_${item.id }" title="<s:property value="#request.item.vmNum" />">
														<s:property value="#request.item.vmNum" />
													</td>
													<td title="<s:property value="#request.item.desc" />">
														<s:property value="#request.item.desc" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
															<a href="javascript:initUpdate(${item.id })"
																chkRightsUrl="computeResourcePoolManager/computeResourcePool!update.action"
																class="ck_modify" title="编辑"></a>
															<a href="javascript:initDelete(${item.id })"
																chkRightsUrl="computeResourcePoolManager/computeResourcePool!delete.action"
																class="ck_del" title="删除"></a>
															<a href="javascript:initComputeResource(${item.id })"
																chkRightsUrl="computeResourcePoolManager/computeResourcePool!updateComputeResources.action"
																class="ck_setRes" title="计算节点管理"></a>
														</div>
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr class="odd">
												<td class="ck_table_first_grid_odd" colspan="14"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>
							<pg:pager
								url="computeResourcePool/computeResourcePool!query.action"
								items="${cloudContext.pageInfo.dataCount}"
								export="currentPageNumber=pageNumber"
								maxPageItems="${cloudContext.pageInfo.eachPageData}">
								<pg:param name="pagesize"
									value="${cloudContext.pageInfo.eachPageData }" />
								<pg:param name="cloudContext.params.qName"
									value="${cloudContext.params.qName[0]}" />
								<div class="ck_table_footer_wrapper">
									<div class="ck_table_footer_show_result_text_wrapper">
										当前第 ${currentPageNumber }
										页&nbsp;共${cloudContext.pageInfo.dataCount }条记录
										<span>&nbsp;每页显示</span>
										<s:select onchange="javascript:$('form').submit()"
											id="eachPageData" list="#{'20':'20', '40':'40', '60':'60'}"
											name="cloudContext.pageInfo.eachPageData">
										</s:select>

									</div>
									<div class="ck_table_footer_paging_wrapper">

										<div id="ck_table_paging_first"
											class="ck_table_paging_item ck_paging_disable">
											<pg:first>
												<a href="${pageUrl}">首页</a>
											</pg:first>
										</div>
										<div id="ck_table_paging_pre" class="ck_table_paging_item">
											<pg:prev>
												<a href="${pageUrl }">上一页</a>
											</pg:prev>
										</div>
										<div id="ck_table_paging_pageNumber"
											class="ck_table_paging_item">
											<s:if test="cloudContext.pageInfo.dataCount==0">
												<font color="red" id="nowPage">1</font>
											</s:if>
											<s:else>
												<pg:pages>
													<%
													    if (currentPageNumber == pageNumber) {
													%>
													<font color="red" id="nowPage">${pageNumber }</font>
													<%
													    } else {
													%>
													<a href="${pageUrl }">${pageNumber }</a>
													<%
													    }
													%>
												</pg:pages>
											</s:else>
										</div>
										<div id="ck_table_paging_next" class="ck_table_paging_item">
											<pg:next>
												<a href="${pageUrl }">下一页</a>
											</pg:next>
										</div>
										<div id="ck_table_paging_last" class="ck_table_paging_item">
											<pg:last>
												<a href="${pageUrl }">尾页</a>
											</pg:last>
										</div>
							</pg:pager>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<!-- 计算节点收缩层开始 -->
		<div class="ck_computeResource_wrapper" style="display: none">
			<table cellpadding="0" cellspacing="0" rules="cols" border="0"
				style="margin-left: 10px">
				<thead>
					<tr style="background: #ABC;">
						<th width="3%">

						</th>
						<th width="20%">
							设备名称
						</th>
						<th width="20%">
							机房
						</th>
						<th width="10%">
							机架(C)
						</th>
						<th width="20%">
							IP
						</th>
						<th width="20%">
							可用cpu(C)
						</th>
						<th width="20%">
							可用内存(M)
						</th>
						<th width="10%">
							虚拟机数量
						</th>
						<th width="10%">
							操作
						</th>
					</tr>
				</thead>
				<tbody class="ck_computeResource_body">
					<tr>
						<td colspan="7">
							-
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 计算节点收缩层结束 -->

		<!-- 虚拟机收缩层开始 -->
		<div class="ck_virtualMachine_wrapper" style="display: none">
			<table cellpadding="0" cellspacing="0" rules="cols" border="0"
				style="margin-left: 13px">
				<thead>
					<tr style="background: #ABC;">
						<th width="20%">
							名称
						</th>
						<th width="10%">
							所有者
						</th>
						<th width="10%">
							审批者
						</th>
						<th width="15%">
							有效期限
						</th>
						<th width="20%">
							虚拟机IP
						</th>
						<s:if test="#session.userLogin.username=='admin'">
							<th width="20%">
								计算节点IP
							</th>
						</s:if>
						<th width="8%">
							状态
						</th>
						<th width="20%">
							备注
						</th>
						<th width="20%">
							操作
						</th>
					</tr>
				</thead>
				<tbody class="ck_virtualMachine_body">
					<tr>
						<td colspan="8">
							-
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- 虚拟机收缩层结束 -->
	</body>
</html>
