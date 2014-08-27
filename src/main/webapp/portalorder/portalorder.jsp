<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="001000">
		<script type="text/javascript" src="portalorder/portalorder.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="js/My97DatePicker/WdatePicker.js"></script>
	</head>
	<body>

		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					虚拟机申请单管理
				</div>
				<div class="ck_content_header_btn_wrapper">
				</div>

			</div>

			<div class="ck_content_body">
				<s:form action="orderManager/order!query.action" theme="simple"
					method="post">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							<table>
								<tr>
									<td>
										配置：
									</td>
									<td>
										<select name="cloudContext.params.qMachineType"
											id="qMachineType">
											<option value="">
												--请选择--
											</option>
											<s:iterator value="#request.cloudContext.params.machineType"
												var="item">
												<option value="${item.id }"
													${item.id==cloudContext.params.qMachineType[0]?
													"selected='selected'":"" }>
													${item.name }
												</option>
											</s:iterator>
										</select>
									</td>
									<td>
										模板：
									</td>
									<td>
										<select name="cloudContext.params.qTemplate" id="qTemplate">
											<option value="">
												--请选择--
											</option>
											<s:iterator value="#request.cloudContext.params.template"
												var="item">
												<option value="${item.id }"
													${item.id==cloudContext.params.qTemplate[0]?
													"selected='selected'":"" }>
													${item.name }
												</option>
											</s:iterator>
										</select>
									</td>
								</tr>
								<tr>

									<td>
										状态：
									</td>
									<td>
										<select name="cloudContext.vo.status"
											value="${cloudContext.vo.status}" id="qStatus">
											<option value="">
												--请选择--
											</option>
											<option value="1" ${cloudContext.vo.status==1?
												"selected='selected'":"" }>
												审核中
											</option>
											<option value="2" ${cloudContext.vo.status==2?
												"selected='selected'":"" }>
												审核通过
											</option>
											<option value="3" ${cloudContext.vo.status==3?
												"selected='selected'":"" }>
												审核拒绝
											</option>
										</select>

									</td>
									<td>
										申请时间：
									</td>
									<td>
										<input type="text" class="Wdate" id="qAppTimeFrom"
											name="cloudContext.vo.applyTime"
											value="<s:date name="cloudContext.vo.applyTime" format="yyyy-MM-dd"/>"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'qAppTimeTo\')}'})" />
									</td>
									<td>
										-
										<input type="text" class="Wdate" id="qAppTimeTo"
											name="cloudContext.vo.applyTimeTo"
											value="<s:date name="cloudContext.vo.applyTimeTo" format="yyyy-MM-dd"/>"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'qAppTimeFrom\');}',maxDate:'%y-%M-%d'})" />
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
										<th width="10">
											申请人
										</th>
										<th width="15">
											配置
										</th>
										<th width="15">
											模板
										</th>
										<th width="15">
											区域
										</th>
										<th width="10">
											状态
										</th>
										<th width="15">
											申请时间
										</th>
										<th width="15">
											有效期限
										</th>
										<th width="15">
											描述
										</th>
										<th width="10">
											操作
										</th>
									</thead>
									<tbody>
										<s:if test="#request.cloudContext.params.order.size()>0">
											<s:iterator value="#request.cloudContext.params.order"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td>
														<s:property value="#request.item.applicantName" />
													</td>
													<td>
														<s:property value="#request.item.machineTypeArgs" />
													</td>
													<td>
														<s:property value="#request.item.templateName" />
													</td>
													<td>
														<s:property value="#request.item.domainName" />
													</td>
													<td>
														<s:if
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_HANDLING">
									审核中
								</s:if>
														<s:elseif
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_APPROVED">
									审核通过
								</s:elseif>
														<s:elseif
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_REJECT">
									审核拒绝
								</s:elseif>
														<s:else>
									未知状态
								</s:else>
													</td>
													<td>
														<s:date name="#request.item.applyTime" format="yyyy-MM-dd" />
													</td>
													<td>
														<s:if
															test="#request.item.dueTimeType==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_DUETIME_TYPE_NEVERDUE">永不过期</s:if>
														<s:elseif
															test="#request.item.dueTimeType==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_DUETIME_TYPE_MONTH">30天</s:elseif>
														<s:elseif
															test="#request.item.dueTimeType==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_DUETIME_TYPE_YEAR">365天</s:elseif>
														<s:else>未知</s:else>
													</td>
													<td>
														<s:property value="#request.item.applyMsg" />
													</td>
													<td class="ck_table_operation_td">

														<div class="ck_table_operation">
															<s:if
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@PORTAL_ORDER_HANDLING">
																<a href="javascript:setOrderApproved(${item.id })"
																	class="ck_approve" title="通过"
																	chkRightsUrl="orderManager/order!setOrderApproved.action"></a>
																<a href="javascript:setOrderRejected(${item.id })"
																	class="ck_reject" title="拒绝"
																	chkRightsUrl="orderManager/order!setOrderRejected.action"></a>
															</s:if>
														</div>
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr class="odd">
												<td class="ck_table_first_grid_odd" colspan="10"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>
							<pg:pager url="orderManager/order!query.action"
								items="${cloudContext.pageInfo.dataCount}"
								export="currentPageNumber=pageNumber"
								maxPageItems="${cloudContext.pageInfo.eachPageData}">
								<pg:param name="pagesize"
									value="${cloudContext.pageInfo.eachPageData }" />
								<pg:param name="cloudContext.vo.status"
									value="${cloudContext.vo.status}" />
								<pg:param name="cloudContext.vo.applyTime"
									value="${cloudContext.vo.applyTime}" />
								<pg:param name="cloudContext.vo.applyTimeTo"
									value="${cloudContext.vo.applyTimeTo}" />
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
				</s:form>
			</div>
		</div>
	</body>
</html>
