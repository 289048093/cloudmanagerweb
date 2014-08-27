<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<div class="ck_guaid_wrapper">
		<div class="ck_guaid_clear"></div>
		<!-- 
			选项卡菜单哉，target属性与下文中的内容页面相同,用以切换页面，
			当活动菜单切换时，需要为目标li添加ck_guaid_current_menu类，
			代码如js示例中的方法addGuaidCallBack()
		 -->
		<div class="ck_guaid_menu">
			<ul>
				<li target="ck_guaid_content_process_1" class="ck_guaid_process"
					id="poolNavigat">
					<div class="ck_progress_lable_text">
						基本信息
					</div>
				</li>
				<li target="ck_guaid_content_process_2" class="ck_guaid_process">
					<div class="ck_progress_lable_text">
						资源池
					</div>
				</li>
				<li target="ck_guaid_content_process_3" class="ck_guaid_process">
					<div class="ck_progress_lable_text">
						用户设置
					</div>
				</li>
			</ul>
		</div>
		<!-- 
			内容页，target属性与上文中的内容页面相同,用以切换页面，
			当活动菜单切换时，需要为目标li添加ck_guaid_current_content类，
			代码如js示例中的方法addGuaidCallBack()
		 -->
		<form action="#" method="post" id="guaidForm">
			<div class="ck_guaid_content">
				<!--	##############################################		-->
				<!--	################   基本信息  ##################		-->
				<!--	##############################################		-->
				<div id="ck_guaid_content_process_1" class="ck_guaid_content_item">
					<div id="guaidBaseInfoDiv">
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td>
									名称:
									<s:token></s:token>
								</td>
								<td>
									<input type="text" name="cloudContext.vo.name" id="name" />
									<font color="red">*</font><span class="errorMsg"></span>
								</td>
							</tr>
							<tr>
								<td>
									存储大小(G):
								</td>
								<td>
									<input type="text" name="cloudContext.vo.storageCapacity"
										id="storageCapacity" />
								</td>
							</tr>
							<tr>
								<td>
									上级:
								</td>
								<td>
									<div id="superDomainDiv4GuaidBaseInfo">
										<ul class='ztree' id="superDomainTree4GuaidBaseInfo"
											style="height: 100px; width: 480px; overflow: auto;">
										</ul>
									</div>
									<input type='hidden' id='useToValidate' />
									<span class="errorMsg"></span>
									<input type="hidden" name="cloudContext.params.superDomainCode"
										id="superDomainCode" />
								</td>
							</tr>
							<tr>
								<td>
									描述:
								</td>
								<td>
									<textarea rows="3" cols="20" name="cloudContext.vo.desc"
										id="desc"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>

				<!-- ##################################################### -->
				<!-- ####################  资源池    ###################### -->
				<!-- ##################################################### -->
				<div id="ck_guaid_content_process_2" class="ck_guaid_content_item">
					<div id="guaidPoolDiv">
						<table>
							<tbody id="guaidPoolTable"></tbody>
							<tfoot>
								<tr>
									<td colspan="2">
										<input type="button" value="自定义" id="addPoolBtn"
											onclick="addPool();" />
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
					<div id="addPoolAction" style="display: none">
						<div id="addPoolDiv">
							<table width="100%" cellpadding="0" cellspacing="0" border="0">
								<caption>
									自定义资源池
								</caption>
								<tbody>
									<tr>
										<td>
											名称
											<s:token></s:token>
										</td>
										<td>
											<input type="text" name="cloudContext.params.addPoolName"
												id="addPoolName" />
											<font color="red">*</font>
										</td>
									</tr>
									<tr>
										<td>
											资源
										</td>
										<td>
											<div id='resourceDiv' style='background: #F0F6E4;border: 1px solid #CCC;padding-left: 10px;padding-top: 10px;
width: 89%;'>
												<div>
													机房：
													<select id="qResourceByRoom"
														name="cloudContext.params.roomId4AddResource"
														onchange="javascript:queryCascadeDataBySelectID('qResourceByRack',$(this).val(),'');">
														<option value="">
															--请选择--
														</option>
													</select>
													<div id="addRoomDiv4GuaidPool" style="display: none;margin-left: 40px;">
														<table width="100%" cellpadding="0" cellspacing="0"
															border="0">
															<tbody>
																<tr>
																	<td>
																		名字 ：
																		<s:token></s:token>
																	</td>
																	<td>
																		<input type="text"
																			name="cloudContext.params.addRoomName4GuaidPool"
																			id="addRoomName4GuaidPool" />
																		<font color="red">*</font>
																	</td>
																</tr>
															</tbody>
														</table>
													</div>
													机架：
													<select id="qResourceByRack"
														name="cloudContext.params.rackId4AddResource"
														onchange="javascript:changeRack($(this).val());">
														<option value="">
															--请选择--
														</option>
													</select>
													<div id="addRackDiv4GuaidPool" style="display: none;margin-left: 40px;">
														<table width="100%" cellpadding="0" cellspacing="0"
															border="0">
															<tbody>
																<tr>
																	<td>
																		名字 ：
																	</td>
																	<td>
																		<input type="text"
																			name="cloudContext.params.addRackName4GuaidPool"
																			id="addRackName4GuaidPool" />
																		<font color="red">*</font>
																	</td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
												<table id='resources'>
												</table>
												<div id="addResourceDiv" style="display: none;background: #CCC;padding: 10px;margin-right: 10px;
margin-bottom: 10px;">
													<table width="100%" cellpadding="0" cellspacing="0"
														border="0">
														<caption style="float: left;">
															自定义资源：
														</caption>
														<tbody id='addResource4GuaidPool'>
															<tr>
																<td width="20%">
																	名字 ：
																	<s:token></s:token>
																</td>
																<td width="80%">
																	<input type="text"
																		name="cloudContext.params.name4AddResources"
																		id="name4AddResources" />
																	<font color="red">*</font>
																</td>
															</tr>
															<tr>
																<td>
																	IP地址 ：
																	<s:token></s:token>
																</td>
																<td>
																	<input type="text"
																		name="cloudContext.params.ip4AddResource"
																		id="ip4AddResource" />
																	<font color="red">*</font>
																</td>
															</tr>
														</tbody>
														<tfoot>
															<tr> 
																<td colspan="2" >
																	<input style="width:80px;" type="button" value='确定'
																		onclick="addResourceOk();" />
																	<input  style="width:80px;" type="button" value='确定并继续'
																		onclick="addResourceAndContinue();" />
																	<input  style="width:80px;" type="button" value='取消'
																		onclick="cancelAddResource();" />
																</td> 
															</tr>
														</tfoot>
													</table>
												</div>
										</td>
									</tr>
								</tbody>
							</table>
							<div style='float: right;margin-right: 50px;margin-top: 10px;'>
								<input type="button" value="确定" onclick="addPoolOk();" />
								<input type="button" value="返回" onclick="actionBack();" />
							</div>
						</div>
					</div>
				</div>
				<!--		#####################################################		-->
				<!--		#################  用户设置  #########################		-->
				<!--		#####################################################		-->
				<div id="ck_guaid_content_process_3" class="ck_guaid_content_item">
					<div id='setUsersDiv'>
						<table>
							<tbody id="users">
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

