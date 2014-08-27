<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.CloudContext"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
	<head>
		<meta name="code" content="002002">
		<title>模版管理</title>
		<script type="text/javascript" src="template/template.js"></script>
		<script type="text/javascript" src="template/validate.js"></script>
	</head>
	<body>

		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					模板
				</div>
				<div class="ck_content_header_btn_wrapper">
					<div class="ck_content_header_btn" id="ckNewTemplate"
						chkRightsUrl="templateManager/template!add.action">
						新建模板
						<input type="hidden" value="<%=basePath%>" id="basePath" />
					</div>
					<div class="ck_content_header_btn" id="ckUploadTemplate">
						上传文件
					</div>
				</div>

			</div>

			<div class="ck_content_body">
				<s:form action="templateManager/template!query.action"
					theme="simple" method="post">
					<!-- 搜索框 -->

					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							模板：
							<input type="text" name="cloudContext.params.qName"
								value="${cloudContext.params.qName[0]}" id="qName" />
							域：
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
							范围：
							<select id="queryScope" name="cloudContext.params.queryAll">
								<option value="false"
									<s:if test="!cloudContext.params.queryAll[0]">selected="selected"</s:if>>
									显示当前域下的机型
								</option>
								<option value="true"
									<s:if test="cloudContext.params.queryAll[0]">selected="selected"</s:if>>
									显示上级域下的机型
								</option>
							</select>
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
										<th>
											序号
										</th>
										<th>
											模板
										</th>
										<th>
											状态
										</th>
										<th>
											文件名
										</th>
										<th>
											域
										</th>
										<th>
											描述
										</th>
										<th>
											操作
										</th>
									</thead>
									<tbody>
										<s:if test="#request.cloudContext.params.templates.size()>0">
											<s:iterator value="#request.cloudContext.params.templates"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td>
														<s:property value="#request.item.name" />
													</td>
													<td align="center">
														<s:if
															test="@com.cloudking.cloudmanagerweb.util.Constant@TEMPLATE_STATUS_OK==#item.status">
														正常
													</s:if>
														<s:elseif
															test="@com.cloudking.cloudmanagerweb.util.Constant@TEMPLATE_STATUS_DOWNLOADING==#item.status">
														正在下载文件	
													</s:elseif>
														<s:elseif
															test="@com.cloudking.cloudmanagerweb.util.Constant@TEMPLATE_STATUS_DOWNLOAD_ERROR==#item.status">
														正在下载文件	
													</s:elseif>
													</td>
													<td title="<s:property value="#item.fileName" />">
														<s:property value="#item.fileName" />
													</td>
													<td title="<s:property value="#item.domainName" />">
														<s:property value="#item.domainName" />
													</td>
													<td title="<s:property value="#item.desc" />">
														<s:property value="#item.desc" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
															<s:if
																test="@com.cloudking.cloudmanagerweb.util.Constant@TEMPLATE_STATUS_DOWNLOADING!=#item.status && #item.selfTemplate">
																<a href="javascript:initUpdate(${item.id })"
																	class="ck_modify" title="编辑"
																	chkRightsUrl="templateManager/template!update.action"></a>
																<a href="javascript:initDelete(${item.id })"
																	class="ck_del" title="删除"
																	chkRightsUrl="templateManager/template!delete.action"></a>
																<a href="javascript:initDeleteWithFile(${item.id })"
																	class="ck_del2" title="删除并删除镜像文件"
																	chkRightsUrl="templateManager/template!deleteWithFile.action"></a>
															</s:if>

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
							<pg:pager url="templateManager/template!query.action"
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
				</s:form>
			</div>
		</div>

	</body>
</html>
