<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.cloudking.cloudmanagerweb.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.cloudking.cloudmanagerweb.CloudContext"%>
<html>
	<head>
		<meta name="code" content="003005">
		<title>我接收的工单</title>
		<script type="text/javascript" src="workorder/receviceWorkOrder.js"></script>
		<script type="text/javascript" src="workorder/receiveWorkOrderVm.js"></script>
		<script type="text/javascript"
			src="workorder/receiveWorkOrderResourceOrder.js"></script>
		<script type="text/javascript"
			src="workorder/recevieWorkOrderVmValidate.js"></script>
		<script type="text/javascript"
			src="workorder/receiveWorkOrderCommon.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="js/My97DatePicker/WdatePicker.js"></script>
		<%
		    CloudContext cloudContext = (CloudContext) request.getAttribute("cloudContext");
		    String startDate = cloudContext.getStringParam("startDate");
		    startDate = startDate == null ? "" : startDate;
		    String endDate = cloudContext.getStringParam("endDate");
		    endDate = endDate == null ? "" : endDate;
		%>
	</head>
	<body>
		<s:form
			action="receiveWorkorderManager/receiveWorkorder!queryReceiveWorkOrder.action"
			theme="simple" method="post">
			<!-- content begin -->
			<div class="ck_content_wrapper">

				<div class="ck_content_header">
					<div class="ck_content_header_text">
						我接收的工单
					</div>
					<div class="ck_content_header_btn_wrapper">

					</div>

				</div>

				<div class="ck_content_body">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							<table id="search_table">
								<tr>
									<td>
										名称：
									</td>
									<td>
										<input type="text" name="cloudContext.params.qTitle"
											value="${cloudContext.params.qTitle[0]}" id="qTitle">
									</td>
									<td>
										工单类别：
									</td>
									<td>
										<select name="cloudContext.params.qCategory" id="qCategory">
											<option value="">
												--请选择--
											</option>
											<s:iterator value="#request.cloudContext.params.categorys"
												var="item">
												<option value="${item.id }"
													${item.id==cloudContext.params.qCategory[0]?
													"selected='selected'":"" }>
													${item.name }
												</option>
											</s:iterator>
										</select>
									</td>
									<td>
										发送域：
									</td>
									<td>
										<select name="cloudContext.params.qSendDomain" id="qSendDomain">
											<option value="">
												--请选择--
											</option>
											<s:iterator value="#request.cloudContext.params.sendDomains"
												var="item">
												<option value="${item.id }"
													${item.id==cloudContext.params.qSendDomain[0]?
													"selected='selected'":"" }>
													${item.name }
												</option>
											</s:iterator>
										</select>
									</td>
									<td>
										接收域：
									</td>
									<td>
										<select name="cloudContext.params.qDomain" id="qDomain">
											<option value="">
												--请选择--
											</option>
											<s:iterator value="#request.cloudContext.params.domains"
												var="item">
												<option value="${item.id }"
													${item.id==cloudContext.params.qDomain[0]?
													"selected='selected'":"" }>
													${item.name }
												</option>
											</s:iterator>
										</select>
									</td>
								</tr>
								<tr>
									<td>
										创建时间：
									</td>
									<td>
										<input type="text" class="Wdate" id="d4331"
											name="cloudContext.params.startDate" value="<%=startDate%>"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'d4332\')}'})" />
									</td>
									<td>
										至
									</td>
									<td>
										<input type="text" class="Wdate" id="d4332"
											name="cloudContext.params.endDate" value="<%=endDate%>"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'d4331\');}',maxDate:'%y-%M-%d'})" />
									</td>
									<td>
										<input type="submit" value="搜索" class="ck_search" />
									</td>
								</tr>
							</table>
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
											序号
										</th>
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
									<tbody>
										<s:if test="#request.cloudContext.params.workOrders.size()>0">
											<s:iterator value="#request.cloudContext.params.workOrders"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
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
															format="yyyy-MM-dd  HH:mm:ss" />
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
												<td class="ck_table_first_grid_odd" colspan="8"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>
							<pg:pager
								url="receiveWorkorderManager/receiveWorkorder!queryReceiveWorkOrder.action"
								items="${cloudContext.pageInfo.dataCount}"
								export="currentPageNumber=pageNumber"
								maxPageItems="${cloudContext.pageInfo.eachPageData}">
								<pg:param name="pagesize"
									value="${cloudContext.pageInfo.eachPageData }" />
								<pg:param name="cloudContext.params.qName"
									value="${cloudContext.params.qTitle[0]}" />
								<pg:param name="cloudContext.params.qCategory"
									value="${cloudContext.params.qCategory[0]}" />
								<pg:param name="cloudContext.params.qDomain"
									value="${cloudContext.params.qDomain[0]}" />
								<pg:param name="cloudContext.params.startDate"
									value="${startDate }" />
								<pg:param name="cloudContext.params.endDate" value="${endDate }" />
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
	</body>
</html>
