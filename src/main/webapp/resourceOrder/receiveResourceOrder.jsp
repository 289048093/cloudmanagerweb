<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="orderID" content="12">
		<title>接收的订单</title>
		<script type="text/javascript"
			src="resourceOrder/receiveResourceOrder.js"></script>
		<script type="text/javascript">
			$(function(){
				$("#qStatus").val($("#qStatus").attr("valueFag"));
				});
		</script>
	</head>
	<body>

		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					我接收的资源申请工单
				</div>
			</div>
			<div class="ck_content_body">
				<s:form
					action="receiveResourceOrderManager/receiveResourceOrder!query.action"
					theme="simple" method="post">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							标题：
							<input type="text" name="cloudContext.params.qTitle"
								value="${cloudContext.params.qTitle[0]}" id="qTitle">
							状态：
							<select name="cloudContext.params.qStatus" id="qStatus"
								valueFag="${cloudContext.params.qStatus[0]}">
								<option value="">
									--请选择--
								</option>
								<option
									value="<s:property value="@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_ACCEPING"/>">
									申请中
								</option>
								<option
									value="<s:property value="@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_AGREE"/>">
									同意
								</option>
								<option
									value="<s:property value="@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_REJECT"/>">
									拒绝
								</option>
							</select>
							范围：
							<select name="cloudContext.params.qDomain" id="qDomain">
								<option value="">
									--请选择--
								</option>
								<s:iterator value="#request.cloudContext.params.domains"
									var="domain">
									<option value='<s:property value="#request.domain.id"/>'
										${domain.id==cloudContext.params.qDomain[0]?
										"selected='selected'":""}>
										<s:property value="#request.domain.name" />
									</option>
								</s:iterator>
							</select>
							<input type="submit" value="搜索" class="ck_search" />
						</div>
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
											存储(G)
										</th>
										<th width="15">
											cpu(C)
										</th>
										<th width="15">
											内存(M)
										</th>
										<th width="15">
											申请时间
										</th>
										<th width="15">
											状态
										</th>
										<th width="15">
											申请人
										</th>
										<th width="25">
											操作
										</th>
									</thead>
									<tbody>
										<s:if
											test="#request.cloudContext.params.resourceOrders.size()>0">
											<s:iterator
												value="#request.cloudContext.params.resourceOrders"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td>
														<s:property value="#request.item.title" />
													</td>
													<td>
														<s:if test="#request.item.storageCapacity==null">--</s:if>
														<s:else>
															<s:property value="#request.item.storageCapacity" />
														</s:else>
													</td>
													<td>
														<s:if test="#request.item.cpu==null">--</s:if>
														<s:else>
															<s:property value="#request.item.cpu" />
														</s:else>
													</td>
													<td>
														<s:if test="#request.item.memory==null">--</s:if>
														<s:else>
															<s:property value="#request.item.memory" />
														</s:else>
													</td>
													<td align="center">
														<s:date name="#request.item.createTime"
															format="yyyy-MM-dd HH:mm:ss" />
													</td>
													<td align="center">
														<s:if
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_ACCEPING">
															 申请中
														</s:if>
														<s:elseif
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_AGREE">
															同意
														</s:elseif>
														<s:elseif
															test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_REJECT">
															拒绝
														</s:elseif>
														<s:else>
															未知
														</s:else>
													</td>
													<td>
														<s:property value="#request.item.senderRealName" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
															<s:if
																test="#request.item.status==@com.cloudking.cloudmanagerweb.util.Constant@RESOURCEORDER_ACCEPING">
																<a href="javascript:initAgree(${item.id })"
																	class="ck_approve" title="通过"
																	chkRightsUrl="receiveResourceOrderManager/receiveResourceOrder!agree.action"></a>
																<a href="javascript:initReject(${item.id })"
																	class="ck_reject" title="拒绝"
																	chkRightsUrl="receiveResourceOrderManager/receiveResourceOrder!reject.action"></a>
															</s:if>
															<a
																href="receiveResourceOrderManager/receiveResourceOrder!view.action?cloudContext.vo.id=${item.id }"
																target="_blank" class="ck_view" title="浏览"
																chkRightsUrl="receiveResourceOrderManager/receiveResourceOrder!view.action"></a>
														</div>
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr class="odd">
												<td class="ck_table_first_grid_odd" colspan="5"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>
							<pg:pager url="receiveResourceOrderManager/receiveResourceOrder!query.action"
								items="${cloudContext.pageInfo.dataCount}"
								export="currentPageNumber=pageNumber"
								maxPageItems="${cloudContext.pageInfo.eachPageData}">
								<pg:param name="pagesize"
									value="${cloudContext.pageInfo.eachPageData }" />
								<pg:param name="cloudContext.params.qTitle"
									value="${cloudContext.params.qTitle[0]}" />
								<pg:param name="cloudContext.params.qStatus"
									value="${cloudContext.params.qStatus[0]}" />
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
