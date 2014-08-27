<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.util.Constant"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta name="code" content="002001">
		<title>域管理</title>
		<script type="text/javascript" src="domain/domain.js"></script>
		<script type="text/javascript" src="domain/validate.js"></script>
		<script type="text/javascript" src="domain/guaid.js"></script>
		<script type="text/javascript" src="domain/poolManager.js"></script>
		<link rel="stylesheet" href="style/zTreeStyle/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript" src="js/jquery.ztree.all-3.2.min.js"></script>
		<script type="text/javascript" src="domain/ck_authorization.js"></script>
		<script type="text/javascript">
			var addSuccessDomainId = ${cloudContext.params.addSuccessDomain==null?false:cloudContext.params.addSuccessDomain.id };
			var addSuccessDomainCode = "${cloudContext.params.addSuccessDomain==null?false:cloudContext.params.addSuccessDomain.code }";
			var domainAvailableCpu = ${cloudContext.params.addSuccessDomain==null?false:cloudContext.params.addSuccessDomain.cpuAvailableNum };
			var domainAvailableMemory = ${cloudContext.params.addSuccessDomain==null?false:cloudContext.params.addSuccessDomain.memoryAvailableCapacity };
			$(function (){
				if(addSuccessDomainId){
					initResourcePools(addSuccessDomainId, addSuccessDomainCode);
				}
			});
			
		</script>
		<link rel="stylesheet"
			href="style/zTreeStyle//ck_ztree_authorization.css" type="text/css">
	</head>
	<body>
		<!-- content begin -->
		<div class="ck_content_wrapper">

			<div class="ck_content_header">
				<div class="ck_content_header_text">
					域
				</div>
				<div class="ck_content_header_btn_wrapper">
					<div class="ck_content_header_btn" id="ckNewDomain"
						chkRightsUrl="domainManager/domain!add.action">
						新建域
					</div>
					<!-- <div class="ck_content_header_btn" id="ckGuaid">
						向导
					</div> -->
				</div>

			</div>

			<div class="ck_content_body">
				<s:form action="domainManager/domain!query.action" theme="simple"
					method="post">
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
									align="center" rules="cols">
									<thead>
										<th width="15">
											域
										</th>
										<th width="15">
											所有者
										</th>
										<th width="15">
											总存储(G)
										</th>
										<th width="15">
											可用存储(G)
										</th>
										<th width="15">
											总备份存储(G)
										</th>
										<th width="15">
											可用备份存储(G)
										</th>
										<th width="15">
											总CPU(C)
										</th>
										<th width="15">
											可用CPU(C)
										</th>
										<th width="15">
											总内存(M)
										</th>
										<th width="15">
											可用内存(M)
										</th>
										<th width="15">
											虚机数
										</th>
										<th width="15">
											描述
										</th>
										<th width="15">
											操作
										</th>
									</thead>
									<tbody>
										<s:if test="#request.cloudContext.params.domains.size()>0">
											<s:iterator value="#request.cloudContext.params.domains"
												var="item" status="st">
												<s:set var="len" value="%{#item.code.length()}"></s:set>
												<tr
													class='${(len/2)%2==1?"ck_table_first_grid_odd":"ck_table_first_grid_even" }'>
													<td>
														<s:iterator begin="1"
															end="%{#request.item.code.length()/2}">&nbsp;</s:iterator>
														<s:property value="#request.item.name" />
													</td>
													<td>
														<s:property value="#request.item.username" />
													</td>
													<td>
														<s:property value="#request.item.storageCapacity" />
													</td>
													<td>
														<s:property value="#request.item.availableStorageCapacity" />
													</td>
													<td>
														<s:property value="#request.item.backupStorageCapacity" />
													</td>
													<td>
														<s:property value="#request.item.availableBackupStorageCapacity" />
													</td>
													<td>
														<s:property value="#request.item.cpuTotalNum" />
													</td>
													<td>
														<s:property value="#request.item.cpuAvailableNum" />
													</td>
													<td>
														<s:property value="#request.item.memoryCapacity" />
													</td>
													<td>
														<s:property value="#request.item.memoryAvailableCapacity" />
													</td>
													<td>
														<s:property value="#request.item.vmNum" />
													</td>
													<td title="<s:property value="#request.item.desc" />">
														<s:property value="#request.item.desc" />
													</td>
													<td class="ck_table_operation_td">
														<div class="ck_table_operation">
															<a href="javascript:initUpdate(${item.id })"
																class="ck_modify" title="编辑"
																chkRightsUrl="domainManager/domain!update.action"></a>
															<s:if test="!#request.item.rootDomain">
																<a href="javascript:initDelete(${item.id })"
																	class="ck_del" title="删除"
																	chkRightsUrl="domainManager/domain!delete.action"></a>
																<a href="javascript:initSetUsers(${item.id })"
																	class="ck_setUser" title="设置用户"
																	chkRightsUrl="domainManager/domain!updateDomainUser.action"></a>
																<a
																	href="javascript:initResourcePools(${item.id },'${item.code }')"
																	class="ck_setRes" title="计算节点资源池管理"
																	chkRightsUrl="domainManager/domain!updateResourcePools.action"></a>
															</s:if>
															<a href="javascript:initAuthorization(${item.id })"
																class="ck_setAuthorization" title="授权"></a>
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
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</body>
</html>
