<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.cloudking.cloudmanagerweb.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.cloudking.cloudmanagerweb.CloudContext"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="003003">
		<script language="JavaScript" type="text/javascript"
			src="js/My97DatePicker/WdatePicker.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="eventLog/eventLog.js"></script>
		<%
		    CloudContext cloudContext = (CloudContext) request.getAttribute("cloudContext");
		    String startDate = cloudContext.getStringParam("startDate");
		    startDate = startDate == null ? "" : startDate;
		    String endDate = cloudContext.getStringParam("endDate");
		    endDate = endDate == null ? "" : endDate;
		%>
	</head>
	<body>

		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					操作记录
				</div>
				<div class="ck_content_header_btn_wrapper">
					<div class="ck_content_header_btn" id="ckNewEvent"
						onclick="exportData()">
						导出
					</div>
				</div>
			</div>

			<div class="ck_content_body">
				<s:form action="eventLogManager/eventLog!query.action"
					theme="simple" method="post">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							描述：
							<input type="text" name="cloudContext.params.qName"
								value="${cloudContext.params.qName[0]}" id="qName" />
							域：
							<select name="cloudContext.params.qDomain" id="qDomain">
								<option value="">
									--请选择--
								</option>
								<s:iterator var="domain"
									value="#request.cloudContext.params.domains">
									<option value="<s:property value="#request.domain.name"/>"
										${domain.name==cloudContext.params.qDomain[0]?
										"selected='selected'":""}>
										<s:iterator begin="1" end="#request.domain.code.length()-2">
											&nbsp;
										</s:iterator>
										<s:property value="#request.domain.name" />
									</option>
								</s:iterator>
							</select>
							开始时间：
							<input type="text" class="Wdate" id="d4331"
								name="cloudContext.params.startDate" value="<%=startDate%>"
								style="width: 135px;"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'d4332\')}'})" />
							结束时间：
							<input type="text" class="Wdate" id="d4332"
								name="cloudContext.params.endDate" value="<%=endDate%>"
								style="width: 135px;"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'d4331\');}',maxDate:'%y-%M-%d'})" />
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
							</div>
							<div class="ck_table_body_wrapper">
								<table cellpadding="0" cellspacing="0" class="ck_table_style"
									rules="cols">
									<thead>
										<th>
											序号
										</th>
										<th>
											用户
										</th>
										<th>
											域
										</th>
										<th>
											描述
										</th>
										<th>
											时间
										</th>
									</thead>
									<tbody>
										<s:if test="#request.cloudContext.params.events.size()>0">
											<s:iterator value="#request.cloudContext.params.events"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td>
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td>
														<s:property value="#request.item.userName" />
													</td>
													<td>
														<s:property value="#request.item.domainName" />
													</td>
													<td>
														<s:property value="#request.item.desc" />
													</td>
													<td>
														<s:date name="#request.item.addTime"
															format="yyyy-MM-dd HH:mm:ss" />
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr class="odd">
												<td class="ck_table_first_grid_odd" colspan="6"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>

							<pg:pager url="eventLogManager/eventLog!query.action"
								items="${cloudContext.pageInfo.dataCount}"
								export="currentPageNumber=pageNumber"
								maxPageItems="${cloudContext.pageInfo.eachPageData}">
								<pg:param name="pagesize"
									value="${cloudContext.pageInfo.eachPageData }" />
								<pg:param name="cloudContext.params.qDomain"
									value="${cloudContext.params.qDomain[0] }" />
								<pg:param name="cloudContext.params.startDate"
									value="${startDate }" />
								<pg:param name="cloudContext.params.endDate" value="${endDate }" />
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
				</s:form>
			</div>
		</div>

	</body>
</html>
