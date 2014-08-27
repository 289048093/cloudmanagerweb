<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="000">
		<title>首页</title>
		<link rel="stylesheet" href="style/dashboard.css" type="text/css" />
		<script type="text/javascript" src="dashboard/dashboard.js"></script>
		<script type="text/javascript" src="dashboard/workOrderPager.js"></script>

		<script type="text/javascript" src="workorder/receviceWorkOrder.js"></script>
		<script type="text/javascript" src="workorder/receiveWorkOrderVm.js"></script>
		<script type="text/javascript"
			src="workorder/receiveWorkOrderResourceOrder.js"></script>
		<script type="text/javascript"
			src="workorder/recevieWorkOrderVmValidate.js"></script>
		<script type="text/javascript"
			src="workorder/receiveWorkOrderCommon.js"></script>

	</head>
	<body>
		<div class="ck_content_wrapper">
			<div class="ck_content_header">
				<div class="ck_content_header_text">
					信息面板
				</div>
			</div>
			<div class="ck_content_body">
				<div class="ck_resource_wrapper">
					<!-- 系统资源 -->
					<div class="ck_table">
						<div class="ck_table_header_wrapper">
							<div class="ck_talbe_title">
								系统资源
							</div>
							<div class="ck_domain_wrapper">
								<select onchange="querySystemChartOnDomainChange(this);">
									<s:if test="#request.cloudContext.loginedUser.domains.size()>0">
										<s:iterator value="#request.cloudContext.loginedUser.domains"
											var="loginUserDomain">
											<option
												value='<s:property value="#request.loginUserDomain.id"/>'
												<s:if test="#request.loginUserDomain.id==#request.cloudContext.loginedUser.domainID">selected="selected"</s:if>>
												<s:property value="#request.loginUserDomain.name" />
											</option>
										</s:iterator>
									</s:if>
								</select>
							</div>
						</div>
						<div class="ck_table_body_wrapper">
							<div class="ck_res_item_wrapper">
								<fieldset>
									<legend>
										CPU使用情况(C)
									</legend>
									<div class="ck_res_icon_wrapper">
										<div id="ie_chart_cpu"></div>
										<canvas id="canvas_cpu" width="120" height="120"></canvas>
									</div>
									<div class="ck_res_text_wrapper">
										<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
											<div>
												域总核数：
												<span id="cpuTotalNum"> <s:property
														value="#request.cloudContext.params.cpuTotalNum" /> </span>
											</div>
											<div>
												分配核数：
												<span id="cpuUsedNum"> <s:property
														value="#request.cloudContext.params.cpuUsedNum" /> </span>
											</div>
											<div>
												空闲核数：
												<span id="cpuAvailableNum"> <s:property
														value="#request.cloudContext.params.cpuAvailableNum" /> </span>
											</div>
											<div>
												单机可配：
												<span id="maxAvailableCpuForApply"> <s:property
														value="#request.cloudContext.params.maxAvailableCpuForApply" />
												</span>
											</div>
										</div>
									</div>
								</fieldset>
							</div>
							<div class="ck_res_item_wrapper">
								<fieldset style="padding: 6px 0px; margin: 0px;">
									<legend>
										内存使用情况(M)
									</legend>
									<div class="ck_res_icon_wrapper">
										<div id="ie_chart_memory"></div>
										<canvas id="canvas_memory" width="120" height="120"></canvas>
									</div>
									<div class="ck_res_text_wrapper">
										<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
											<div>
												域总内存：
												<span id="memoryCapacity"> <s:property
														value="#request.cloudContext.params.memoryCapacity" /> </span>
											</div>
											<div>
												已用内存：
												<span id="memoryUsedCapacity"> <s:property
														value="#request.cloudContext.params.memoryUsedCapacity" />
												</span>
											</div>
											<div>
												空闲内存：
												<span id="memoryAvailableCapacity"> <s:property
														value="#request.cloudContext.params.memoryAvailableCapacity" />
												</span>
											</div>
											<div>
												单机可配：
												<span id="maxAvailableMemoryForApply"> <s:property
														value="#request.cloudContext.params.maxAvailableMemoryForApply" />
												</span>
											</div>
										</div>
									</div>
								</fieldset>
							</div>
							<div class="ck_res_item_wrapper">
								<fieldset>
									<legend>
										存储使用情况(G)
									</legend>
									<div class="ck_res_icon_wrapper">
										<div id="ie_chart_storage"></div>
										<canvas id="canvas_storage" width="120" height="120"></canvas>
									</div>
									<div class="ck_res_text_wrapper">
										<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
											<div>
												域总存储：
												<span id="storageCapacity"> <s:property
														value="#request.cloudContext.params.storageCapacity" /> </span>
											</div>
											<div>
												已用容量：
												<span id="usedStorageCapacity"> <s:property
														value="#request.cloudContext.params.usedStorageCapacity" />
												</span>
											</div>
											<div>
												空闲容量：
												<span id="availableStorageCapacity"> <s:property
														value="#request.cloudContext.params.availableStorageCapacity" />
												</span>
											</div>
											<div>
												单机可配：
												<span id="maxAvailableStorageForApply"> <s:property
														value="#request.cloudContext.params.maxAvailableStorageForApply" />
												</span>
											</div>
										</div>
									</div>
								</fieldset>
							</div>

						</div>
					</div>
					<s:if
						test="#request.cloudContext.loginedUser.username==@com.cloudking.cloudmanagerweb.util.Constant@ADMINISTRATOR">
						<!-- 计算节点池 -->
						<div class="ck_crp_table">
							<div class="ck_table_header_wrapper">
								<div class="ck_talbe_title">
									计算节点池
								</div>
								<div class="ck_domain_wrapper">
									<select onchange="queryComputePoolChartOnPoolChange(this);">
										<option value="">
											--请选择--
										</option>
										<s:if
											test="#request.cloudContext.params.computeResourcePool.size()>0">
											<s:iterator
												value="#request.cloudContext.params.computeResourcePool"
												var="computeResourcePool">
												<option value='${computeResourcePool.id}'>
													${computeResourcePool.name}
												</option>
											</s:iterator>
										</s:if>

									</select>
								</div>
							</div>
							<div class="ck_table_body_wrapper">
								<div class="ck_res_crp_item_wrapper">
									<fieldset>
										<legend>
											CPU使用情况(C)
										</legend>
										<div class="ck_res_icon_wrapper">
											<div id="ie_chart_pool_cpu"></div>
											<canvas id="canvas_pool_cpu" width="120" height="120"></canvas>
										</div>
										<div class="ck_res_text_wrapper">
											<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
												<div>
													池总核数：
													<span id="PoolCpuTotalNum"> <s:property
															value="#request.cloudContext.params.poolCpuTotalNum" />
													</span>
												</div>
												<div>
													已用核数：
													<span id="PoolCpuUsedNum"> <s:property
															value="#request.cloudContext.params.poolCpuUsedNum" /> </span>
												</div>
												<div>
													空闲核数：
													<span id="PoolCpuAvailableNum"> <s:property
															value="#request.cloudContext.params.poolCpuAvailableNum" />
													</span>
												</div>
											</div>
										</div>
									</fieldset>
								</div>
								<div class="ck_res_crp_item_wrapper">
									<fieldset style="padding: 6px 0px; margin: 0px;">
										<legend>
											内存使用情况(M)
										</legend>
										<div class="ck_res_icon_wrapper">
											<div id="ie_chart_pool_memory"></div>
											<canvas id="canvas_pool_memory" width="120" height="120"></canvas>
										</div>
										<div class="ck_res_text_wrapper">
											<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
												<div>
													池总内存：
													<span id="poolMemoryCapacity"> <s:property
															value="#request.cloudContext.params.poolMemoryCapacity" />
													</span>
												</div>
												<div>
													已用内存：
													<span id="poolMemoryUsedCapacity"> <s:property
															value="#request.cloudContext.params.poolMemoryUsedCapacity" />
													</span>
												</div>
												<div>
													空闲内存：
													<span id="poolMemoryAvailableCapacity"> <s:property
															value="#request.cloudContext.params.poolMemoryAvailableCapacity" />
													</span>
												</div>
											</div>
										</div>
									</fieldset>
								</div>
							</div>
						</div>
						<!-- 存储池 -->
						<div class="ck_storage_table">
							<div class="ck_table_header_wrapper">
								<div class="ck_talbe_title">
									存储池
								</div>
								<div class="ck_domain_wrapper">
									<select onchange="queryStoragePoolChartOnPoolChange(this);">
										<option value="">
											--请选择--
										</option>
										<s:if test="#request.cloudContext.params.storagePool.size()>0">
											<s:iterator value="#request.cloudContext.params.storagePool"
												var="storagePool">
												<option value='${storagePool.id}'>
													${storagePool.name}
												</option>
											</s:iterator>
										</s:if>

									</select>
								</div>
							</div>
							<div class="ck_table_body_wrapper">
								<div class="ck_res_storage_item_wrapper">
									<fieldset>
										<legend>
											存储池使用情况(G)
										</legend>
										<div class="ck_res_icon_wrapper">
											<div id="ie_chart_pool_stoarge"></div>
											<canvas id="canvas_pool_stoarge" width="120" height="120"></canvas>
										</div>
										<div class="ck_res_text_wrapper">
											<div class="ck_res_inner_wrapper" style="padding-top: 35px;">
												<div>
													容量总计：
													<span id="poolStorageCapacity"> <s:property
															value="#request.cloudContext.params.poolStorageCapacity" />
													</span>
												</div>
												<div>
													已用存储：
													<span id="poolUsedStorageCapacity"> <s:property
															value="#request.cloudContext.params.poolUsedStorageCapacity" />
													</span>
												</div>
												<div>
													空闲存储：
													<span id="poolAvailableStorageCapacity"> <s:property
															value="#request.cloudContext.params.poolAvailableStorageCapacity" />
													</span>
												</div>
											</div>
										</div>
									</fieldset>
								</div>
							</div>
						</div>
					</s:if>
					<div class="ck_clear"></div>
					<!-- 工单开始 -->
					<div class="ck_table">
						<div class="ck_table_header_wrapper">
							<div class="ck_talbe_title">
								我接收的工单
							</div>
							<div class="ck_domain_wrapper">
								接收域：<select onchange="queryWorkOrderOnDomainChange(this);"
									id="ck_res_domains">
									<option value="">
										--请选择--
									</option>
									<s:if test="#request.cloudContext.loginedUser.domains.size()>0">
										<s:iterator value="#request.cloudContext.loginedUser.domains"
											var="loginUserDomain">
											<option
												value='<s:property value="#request.loginUserDomain.id"/>'
												<s:if test="#request.loginUserDomain.id==#request.cloudContext.loginedUser.domainID">selected="selected"</s:if>>
												<s:property value="#request.loginUserDomain.name" />
											</option>
										</s:iterator>
									</s:if>
								</select>
							</div>
						</div>
						<div class="ck_table_body_wrapper">
							<table cellpadding="0" cellspacing="0" class="ck_table_style"
								rules="cols">
								<thead>
									<th width="20">
										标题
									</th>
									<th width="15">
										类别
									</th>
									<th width="15">
										发送域
									</th>
									<th width="15">
										内容
									</th>
									<th width="15">
										状态
									</th>
									<th width="15">
										创建时间
									</th>
									<th width="15">
										操作
									</th>
								</thead>
								<tbody id="woApplyFormBody">
									<s:if test="#request.cloudContext.params.workOrders.size()>0">
										<s:iterator value="#request.cloudContext.params.workOrders"
											var="item" status="st">
											<tr class='receiveWorkOrderTr' id="${item.id }">
												<td title="<s:property value="#request.item.title" />">
													<s:property value="#request.item.title" />
												</td>
												<td
													title="<s:property value="#request.item.categoryName" />">
													<s:property value="#request.item.categoryName" />
												</td>
												<td
													title="<s:property value="#request.item.sendDomainName" />">
													<s:property value="#request.item.sendDomainName" />
												</td>
												<td title="<s:property value="#request.item.content" />">
													<s:property value="#request.item.content" />
												</td>
												<td align="center">
													<s:if
														test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_ACCEPING">
															处理中
														</s:if>
													<s:elseif
														test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_SOLVED">
															已处理
														</s:elseif>
													<s:elseif
														test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_CLOSED">
															已关闭
														</s:elseif>
												</td>
												<td
													title="<s:date name="#request.item.createTime" format="yyyy-MM-dd HH:mm:ss" />">
													<s:date name="#request.item.createTime"
														format="yyyy-MM-dd HH:mm:ss" />
												</td>

												<td class="ck_table_operation_td">
													<div class="ck_table_operation">
														<s:if
															test="#request.item.categoryId==@com.cloudking.cloudmanagerweb.util.Constant@WORK_ORDER_CATEGORY_COMMON">
															<s:if
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_ACCEPING">
																<a
																	href="javascript:approveWorkOrder(${item.id },${item.categoryId })"
																	class="ck_approve" title="解决"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>
																<a
																	href="javascript:closeWorkOrder(${item.id },${item.categoryId })"
																	class="ck_del2" title="关闭"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>
															</s:if>
															<s:elseif
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_SOLVED">
																<a
																	href="javascript:closeWorkOrder(${item.id },${item.categoryId })"
																	class="ck_del2" title="关闭"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>
															</s:elseif>
														</s:if>
														<s:else>
															<s:if
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_ACCEPING">
																<a
																	href="javascript:approveWorkOrder(${item.id },${item.categoryId })"
																	class="ck_approve" title="通过"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>
																<a
																	href="javascript:rejectWorkOrder(${item.id },${item.categoryId })"
																	class="ck_reject" title="拒绝"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!finishWorkOrder.action"></a>
																<a
																	href="javascript:closeWorkOrder(${item.id },${item.categoryId })"
																	class="ck_del2" title="关闭"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>
															</s:if>
															<s:elseif
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@WORKORDER_SOLVED">
																<a
																	href="javascript:closeWorkOrder(${item.id },${item.categoryId })"
																	class="ck_del2" title="关闭"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!closeWorkOrder.action"></a>
															</s:elseif>
														</s:else>
														<s:if test="#request.item.attachmentCount>0">
																<a
																	href="javascript:viewAttachment(${item.id },${item.categoryId })"
																	target="_blank" class="ck_attachment" title="查看附件[${item.attachmentCount }个]"
																	style="display: block !important"
																	chkRightsUrl="receiveWorkorderManager/receiveWorkorder!viewAttachment.action"></a>
															</s:if>
														<a
															href="receiveWorkorderManager/receiveWorkorder!view.action?workOrderId=${item.id }"
															target="_blank" class="ck_view" title="浏览"
															chkRightsUrl="receiveWorkorderManager/receiveWorkorder!view.action"></a>
													</div>
												</td>
											</tr>
										</s:iterator>
									</s:if>
									<s:else>
										<tr class="odd">
											<td class="ck_table_first_grid_odd" colspan="7"
												align="center">
												没有找到相应的记录
											</td>
										</tr>
									</s:else>
								</tbody>
							</table>
						</div>
						<div class="ck_table_footer_wrapper" id="ck_res_footer_div">
							<div class="ck_table_footer_show_result_text_wrapper">
								当前第
								<span id="rsNowPage">${cloudContext.params.woPageInfo.nowPage}</span>
								页&nbsp;共
								<span id="rsTotalPage">${cloudContext.params.woPageInfo.pageCount}</span>页
								&nbsp;
								<span id="rsTotalCount">${cloudContext.params.woPageInfo.dataCount}</span>条记录
							</div>
							<div class="ck_table_footer_paging_wrapper">
								<div id="ck_table_paging_first_rs"
									class="ck_table_paging_item ck_paging_disable">
									<s:if
										test="#request.cloudContext.params.woPageInfo.getNowPage()==1">
										<a href="javascript:void(0);" class="ck_pager_inactive">首页</a>
									</s:if>
									<s:else>
										<a href="javascript:void(0);" onclick="woApplyPager(1)">首页</a>
									</s:else>
								</div>
								<div id="ck_table_paging_pre_rs" class="ck_table_paging_item">
									<s:if
										test="#request.cloudContext.params.woPageInfo.getNowPage()>1">
										<a href="javascript:void(0);"
											onclick="woApplyPager(${cloudContext.params.woPageInfo.nowPage}-1)">上一页</a>
									</s:if>
									<s:else>
										<a href="javascript:void(0);" class="ck_pager_inactive">上一页</a>
									</s:else>
								</div>
								<div id="ck_table_paging_pageNumber"
									class="ck_table_paging_item">
								</div>
								<div id="ck_table_paging_next_rs" class="ck_table_paging_item">
									<s:if
										test="#request.cloudContext.params.woPageInfo.getNowPage() < #request.cloudContext.params.woPageInfo.getPageCount()">
										<a href="javascript:void(0);"
											onclick="woApplyPager(${cloudContext.params.woPageInfo.nowPage}+1)">下一页</a>
									</s:if>
									<s:else>
										<a href="javascript:void(0);" class="ck_pager_inactive">下一页</a>
									</s:else>
								</div>
								<div id="ck_table_paging_last_rs" class="ck_table_paging_item">
									<s:if
										test="#request.cloudContext.params.woPageInfo.getNowPage() == #request.cloudContext.params.woPageInfo.getPageCount()">
										<a href="javascript:void(0);" class="ck_pager_inactive">尾页</a>
									</s:if>
									<s:else>
										<a href="javascript:void(0);"
											onclick="woApplyPager(${cloudContext.params.woPageInfo.pageCount})">尾页</a>
									</s:else>
								</div>
							</div>

							<!-- 工单结束 -->
						</div>
					</div>
				</div>
			</div>
	</body>
</html>
