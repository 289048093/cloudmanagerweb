<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="001001">
		<title>机柜管理</title>
		<script type="text/javascript" src="machineRack/machineRack.js"></script>
		<script type="text/javascript" src="machineRack/validate.js"></script>
	</head>
	<body>
		
			<!-- content begin -->
			<div class="ck_content_wrapper">

				<div class="ck_content_header">
					<div class="ck_content_header_text">
						机架
					</div>
					<div class="ck_content_header_btn_wrapper">
						<div class="ck_content_header_btn" id="ckNewmachineRack" chkRightsUrl="machineRackManager/machineRack!add.action">
							新建机架
						</div>
					</div>

				</div>

				<div class="ck_content_body" id="ck_message_container">
					<s:form action="machineRackManager/machineRack!query.action"
			theme="simple" method="post">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							名字：
							<input type="text" name="cloudContext.params.qName" value="${cloudContext.params.qName[0]}" id="qName"/>
							机房：
							<select name="cloudContext.params.qRoom" id="qRoom">
								<option value="">
									--请选择--
								</option>
								<s:iterator value="#request.cloudContext.params.machineRooms"
									var="item">
									<option value="${item.id }" ${item.id==qRoom?"selected='selected'":"" }>
										${item.name }
									</option>
								</s:iterator>
							</select>
							<input type="submit" value="搜索" class="ck_search"/>
						</div>
					</div>
					<div class="ck_search_toggle">
						<img class="ck_toggle_img" src="images/ck_search_toggle.png" alt="搜索" title="搜索"/>
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
										<th width="15">
											序号
										</th>
										<th width="25">
											机架
										</th>
										<th width="25">
											机房
										</th>
										<th width="40">
											描述
										</th>
										<th width="20">
											操作
										</th>
									</thead>
									<tbody>
										<s:if
											test="#request.cloudContext.params.machineRacks.size()>0">
											<s:iterator value="#request.cloudContext.params.machineRacks"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td title="<s:property value="#request.item.name" />">
														<s:property value="#request.item.name" />
													</td>
													<td>
														<s:property value="#request.item.roomName" />
													</td>
													<td title="<s:property value="#request.item.desc" />">
														<s:property value="#request.item.desc" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
														<a href="javascript:initUpdate(${item.id })" class="ck_modify" title="编辑" chkRightsUrl="machineRackManager/machineRack!update.action"></a>
														<a href="javascript:initDelete(${item.id })" class="ck_del" title="删除" chkRightsUrl="machineRackManager/machineRack!delete.action"></a>
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
							<pg:pager url="machineRackManager/machineRack!query.action"
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
										<s:select onchange="javascript:$('form').submit()" id="eachPageData" 
											list="#{'20':'20', '40':'40', '60':'60'}"
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
