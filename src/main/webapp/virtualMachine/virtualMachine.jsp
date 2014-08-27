<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.cloudking.cloudmanagerweb.CloudContext"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>

<html>
	<head>
		<meta name="code" content="002004">
		<title>虚拟机管理</title>
		<s:set var="backupNormal" value="@com.cloudking.cloudmanagerweb.util.Constant@VM_BACKUP_NORMAL_MSG"></s:set>
		<SCRIPT type="text/javascript">
			<%
		    CloudContext cloudContext = (CloudContext) request.getAttribute("cloudContext");
		    String qCompute = cloudContext.getStringParam("qCompute");
		    out.println(String.format("var qCompute=\"%1$s\";",qCompute==null?"-1":qCompute));
		    
		    String qRack = cloudContext.getStringParam("qRack");
		    out.println(String.format("var qRack=\"%1$s\";",qRack==null?"-1":qRack));
			%>
			var migrating = <s:property value="@com.cloudking.cloudmanagerweb.util.Constant@VM_OPERATE_MIGRATING"/>;
			var backup2Storage = "<s:property value="@com.cloudking.cloudmanagerweb.util.Constant@CREATE_BACKUP_FLAG"/>";
			var backupNormal = '${backupNormal}';
		</SCRIPT>
		<script type="text/javascript" src="virtualMachine/virtualMachine.js"></script>
		<script type="text/javascript" src="virtualMachine/validate.js"></script>
		<script type="text/javascript" src="virtualMachine/moveVm.js"></script>
		<script type="text/javascript"
			src="virtualMachine/backupAndRestore2Storage.js"></script>
	</head>
	<body>
		<!-- content begin -->
		<div class="ck_content_wrapper">
			<div class="ck_content_header">
				<div class="ck_content_header_text">
					虚拟机
				</div>
				<div class="ck_content_header_btn_wrapper">
					<div class="ck_content_header_btn" id="ckNewVirtualMachine">
						新建虚拟机
					</div>
					<div class="ck_content_header_btn" id="vncDownLoad">
						vnc下载
					</div>
					<div class="ck_content_header_btn" id="loopStart" onclick="loopStart();">循环测试</div>
					<div class="ck_content_header_btn" id="loopStop" onclick="loopStop();">循环测试停止</div>
				</div>
			</div>

			<div class="ck_content_body" id="ck_message_container">
				<s:form action="virtualMachineManager/virtualMachine!query.action"
					theme="simple" method="post">
					<!-- 搜索框 -->
					<div class="ck_search_context_wrapper">
						<div class="ck_search_inner_wrapper">
							虚拟机：
							<input type="text" name="cloudContext.params.qName"
								value="${cloudContext.params.qName[0]}" id="qName" />
							机房：
							<select name="cloudContext.params.qRoom" id="qRoom"
								onchange="javascript:queryRackByRoom('qRack',$(this).val(),'');">
								<option value="">
									--请选择--
								</option>
								<s:iterator value="#request.cloudContext.params.machineRooms"
									var="item">
									<option value="${item.id }"
										${item.id==cloudContext.params.qRoom[0]? "selected='selected'":"" }>
										${item.name }
									</option>
								</s:iterator>
							</select>
							机架：
							<select name="cloudContext.params.qRack" id="qRack"
								onchange="queryComputeByRack('qCompute',$(this).val(),'');">
								<option value="">
									--请选择--
							</select>
							设备：
							<select name="cloudContext.params.qCompute" id="qCompute">
								<option value="">
									--请选择--
							</select>
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
											序号
										</th>
										<th width="10">
											虚拟机
										</th>
										<th width="15">
											IP
										</th>
										<th width="15">
											vnc端口
										</th>
										<th width="15">
											计算资源IP
										</th>
										<th width="15">
											域
										</th>
										<th width="10">
											状态
										</th>
										<th width="10">
											使用者
										</th>
										<th width="10">
											有效期限
										</th>
										<th width="15">
											描述
										</th>
										<th width="25">
											操作
										</th>
									</thead>
									<tbody>
										<s:if
											test="#request.cloudContext.params.virtualMachines.size()>0">
											<s:iterator
												value="#request.cloudContext.params.virtualMachines"
												var="item" status="st">
												<tr
													class='${st.index%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<s:hidden name="id" id="id" value="%{#request.item.id}"></s:hidden>
													<input type="hidden" class="virtualMachineIdKeeper"
														value="<s:property value="#request.item.id" />" />
													<s:hidden name="createFlag" id="createFlag"
														value="%{#request.item.createdFlag}"></s:hidden>
													<td align="center">
														<s:property
															value="#request.st.count+(#request.cloudContext.pageInfo.nowPage-1)*#request.cloudContext.pageInfo.eachPageData" />
													</td>
													<td title="<s:property value="#request.item.name" />">
														<s:property value="#request.item.name" />
													</td>
													<td title="<s:property value="#request.item.ip" />">
														<s:property value="#request.item.ip" />
													</td>
													<td id="port_<s:property value="#request.item.id" />">
													</td>
													<td id="crIP_${item.id}"
														title="<s:property value="#request.item.computeResourceIP" />">
														<s:property value="#request.item.computeResourceIP" />
													</td>
													<td title="<s:property value="#request.item.domainName" />">
														<s:property value="#request.item.domainName" />
													</td>
													<td align="center" vmId="${item.id }" class="ck_vm_img">
														<!-- 创建中的虚拟机 -->
														<s:if test="#request.item.createdFlag=='vmCreating'">
															<img src="images/loading_media.gif" width="14"
																height="14" title="创建中" alt="创建中" />
														</s:if>
														<!-- 创建成功的虚拟机 -->
														<s:elseif
															test="#request.item.createdFlag=='vmCreateSuccess'">
															<!-- 创建成功的虚拟机 定义状态-->
															<s:if test="#request.item.status=='defined'">
																<img src="images/ck_vm_status_defined.png" width="14"
																	height="14" title="已定义" alt="已定义" />
															</s:if>
															<!-- 创建成功的虚拟机 未定义状态-->
															<s:elseif test="#request.item.status=='undefine'">
																<img src="images/ck_vm_status_undefine.png" width="14"
																	height="14" title="未定义" alt="未定义" />
															</s:elseif>
															<!-- 创建成功的虚拟机 运行状态-->
															<s:elseif test="#request.item.status=='running'">
																<img src="images/ck_vm_status_running.png" width="14"
																	height="14" title="运行中" alt="运行中" />
															</s:elseif>
															<!-- 创建成功的虚拟机 暂停状态-->
															<s:elseif test="#request.item.status=='paused'">
																<img src="images/ck_vm_status_stop.png" width="14"
																	height="14" title="已挂起" alt="已挂起" />
															</s:elseif>
															<!-- 创建成功的虚拟机 关机状态-->
															<s:elseif test="#request.item.status=='shutoff'">
																<img src="images/ck_vm_status_shutoff.png" width="14"
																	height="14" title="已关机" alt="已关机" />
															</s:elseif>
															<s:else>
																<img src='images/loading_media.gif' alt='加载中'
																	title='加载中' width='14' height='14' />
															</s:else>
														</s:elseif>
														<!--其他状态的虚拟机 -->
														<s:else>
															<img src="images/ck_vm_status_failed.png" width="14"
																height="14"
																title="<s:property value="#request.item.createdResultMsg"/>"
																alt="<s:property value="#request.item.createdResultMsg"/>" />
														</s:else>
													</td>
													<td title="<s:property value="#request.item.owner" />">
														<s:property value="#request.item.owner" />
													</td>
													<td>
														<s:if test="#request.item.dueTime==null">永不过期</s:if>
														<s:else>
															<s:date name="#request.item.dueTime" format="yyyy-MM-dd" />
														</s:else>
													</td>
													<td title="<s:property value="#request.item.desc" />">
														<s:property value="#request.item.desc" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
															<!-- 创建成功的虚拟机 - 公共属性 -->
															<div class="ck_vm_common_operation" vmId="${item.id }">
																<s:if
																	test="#request.item.createdFlag==@com.cloudking.cloudmanagerweb.util.Constant@VM_CREATE_SUCCESS">
																	<a href="javascript:initUpdate(${item.id })" title="编辑"
																		chkRightsUrl="virtualMachineManager/virtualMachine!update.action">编辑</a>
																</s:if>
															</div>
															<div class="ck_vm_statusMsg_wrapper" vmId="${item.id }">
																<s:if
																	test="#request.item.createdFlag==@com.cloudking.cloudmanagerweb.util.Constant@VM_CREATE_SUCCESS && #request.item.operateFailFlag">
																	<img src="images/ck_vm_snapshot_failed.png" width="16"
																		height="16" style="float: left;"
																		title="<s:property value="#request.item.createdResultMsg"/>"
																		alt="<s:property value="#request.item.createdResultMsg"/>" />
																</s:if>
															</div>
															<div id="ck_vm_operation_dropdown">
																<div class="ck_more_wrapper">
																	<div class="ck_op_more" vmId="${item.id }">
																		更多操作
																	</div>
																</div>
																<div class="ck_vm_actual_operation" vmId="${item.id }">
																	<!-- 创建中的虚拟机 -->
																	<s:if
																		test="#request.item.createdFlag==@com.cloudking.cloudmanagerweb.util.Constant@VM_CREATING">
																	</s:if>
																	<!-- 创建失败的虚拟机 -->
																	<s:elseif
																		test="#request.item.createdFlag==@com.cloudking.cloudmanagerweb.util.Constant@VM_CREATE_FAILED">
																		<a href="javascript:initDelete(${item.id })"
																			title="删除"
																			chkRightsUrl="virtualMachineManager/virtualMachine!delete.action"></a>
																	</s:elseif>
																	<!-- 创建成功的虚拟机 -->
																	<s:elseif
																		test="#request.item.createdFlag==@com.cloudking.cloudmanagerweb.util.Constant@VM_CREATE_SUCCESS">
																		<a href="javascript:initDelete(${item.id })"
																			title="删除"‘’‘’
																			chkRightsUrl="virtualMachineManager/virtualMachine!delete.action">删除</a>
																		<a style="display: block; margin-right: 3px;"
																			href="javascript:initBackupVM(${item.id })"
																			title="创建快照">创建快照</a>
																		<a style="display: block;"
																			href="javascript:initRestoreVM(${item.id })"
																			title="还原快照">还原快照</a>
																		<!-- 创建成功的虚拟机 定义状态-->
																		<s:if test="#request.item.status=='defined'">
																			<a style="display: block;"
																				href="javascript:initStartup(${item.id })"
																				title="启动">启动</a>
																		</s:if>
																		<!-- 创建成功的虚拟机 未定义状态-->
																		<s:elseif test="#request.item.status=='undefine'">
																		</s:elseif>
																		<!-- 创建成功的虚拟机 运行状态-->
																		<s:elseif test="#request.item.status=='running'">
																			<a style="display: block;"
																				href="javascript:initReboot(${item.id })" title="重启">重启</a>
																			<a style="display: block;"
																				href="javascript:initSuspend(${item.id })"
																				title="挂起">挂起</a>
																			<a style="display: block;"
																				href="javascript:initShutdown(${item.id })"
																				title="关机">关机</a>
																			<a style="display: block;"
																				href="javascript:initForceShutdown(${item.id })"
																				title="强制关机">强制关机</a>
																			<a style="display: block;"
																				href="javascript:initVnc(${item.id })" title="远程连接">远程连接</a>
																		</s:elseif>
																		<!-- 创建成功的虚拟机 暂停状态-->
																		<s:elseif test="#request.item.status=='paused'">
																			<a style="display: block;"
																				href="javascript:initVolumn(${item.id })"
																				title="卷管理"
																				chkRightsUrl="virtualMachineManager/virtualMachine!addVolumn.action">卷管理</a>
																			<a style="display: block;"
																				href="javascript:initResume(${item.id })" title="恢复">恢复</a>
																			<a style="display: block;"
																				href="javascript:initVnc(${item.id })" title="远程连接">远程连接</a>
																			<a style="display: block;"
																				href="clientVncView('${item.computeResourceIP }','${item.port }')"
																				title="客户端远程">客户端远程</a>
																		</s:elseif>
																		<!-- 创建成功的虚拟机 关机状态-->
																		<s:elseif test="#request.item.status=='shutoff'">
																			<a style="display: block;"
																				href="javascript:initVolumn(${item.id })"
																				title="卷管理"
																				chkRightsUrl="virtualMachineManager/virtualMachine!addVolumn.action">卷管理</a>
																			<a style="display: block;"
																				href="javascript:initStartup(${item.id })"
																				title="启动">启动</a>
																			<a style="display: block;"
																				href="javascript:initBackup2Storage(${item.id })"
																				title="备份虚机">备份虚机</a>
																			<a style="display: block;"
																				href="javascript:initRestore2Storage(${item.id })"
																				title="还原虚机">还原虚机</a>
																		</s:elseif>
																	</s:elseif>
																</div>
															</div>
														</div>
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr class="odd">
												<td class="ck_table_first_grid_odd" colspan="11"
													align="center">
													没有找到相应的记录
												</td>
											</tr>
										</s:else>
									</tbody>
								</table>
							</div>
							<pg:pager url="virtualMachineManager/virtualMachine!query.action"
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
										<s:hidden id="eachPageData"
											name="cloudContext.pageInfo.eachPageData"></s:hidden>
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
