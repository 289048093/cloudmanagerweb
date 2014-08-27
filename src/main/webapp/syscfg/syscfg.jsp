<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="003002">
		<title>系统设置</title>
		<s:set var="pwdKey"
			value="@com.cloudking.cloudmanagerweb.PropertyManager@DB_EMAIL_PASSWORD"></s:set>
		<script type="text/javascript" src="syscfg/syscfg.js"></script>
		<script type="text/javascript">
			var pwdKey = '${pwdKey}';
		</script>
	</head>
	<body>

		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					全局设置
				</div>
			</div>
			<div class="ck_content_body">
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
										描述
									</th>
									<th>
										当前值
									</th>
									<th>
										默认值
									</th>
									<th>
										操作
									</th>
								</thead>
								<tbody>
									<s:if test="#request.cloudContext.params.syscfgs.size()>0">
										<s:iterator value="#request.cloudContext.params.syscfgs"
											var="item" status="st">
											<tr
												class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
												<input type="hidden" value="${item.id }" />
												<td align="center">
													${st.count }
												</td>
												<td>
													<s:property value="#request.item.desc" />
												</td>
												<td>
													<s:if test="#request.item.type=='Boolean'">
														<s:if test="#request.item.value">是</s:if>
														<s:else>否</s:else>
													</s:if>
													<s:elseif
														test="#request.item.key==@com.cloudking.cloudmanagerweb.PropertyManager@DB_EMAIL_PASSWORD">
														<s:iterator begin="1" end="#request.item.value.length()">
															*
														</s:iterator>
													</s:elseif>
													<s:else>
														<s:property value="#request.item.value" />
													</s:else>
												</td>
												<td>
													<s:if test="#request.item.type=='Boolean'">
														<s:if test="#request.item.defaultValue">是</s:if>
														<s:else>否</s:else>
													</s:if>
													<s:else>
														<s:property value="#request.item.defaultValue" />
													</s:else>
												</td>
												<td class="ck_table_operation_td">
													<div class="ck_table_operation">
														<a
															href="javascript:initUpdate(${item.id},'${item.regex}' )"
															class="ck_modify" title="编辑"
															chkRightsUrl="syscfgManager/syscfg!update.action"></a>
														<a href="javascript:recoverDefault(${item.id })"
															class="ck_modify" title="恢复默认值">恢复默认值</a>
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
					</div>
				</div>
			</div>
		</div>

	</body>
</html>
