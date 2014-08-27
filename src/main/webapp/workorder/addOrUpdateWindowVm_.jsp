<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			
			<tr class="ck_to_be_hide">
				<td>
					虚拟机名称 ：
				</td>
				<td>
					<input id="virtualMachineName" type="text"/>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
					<td>
						工单解决情况：
					</td>
					<td>
						<select name="cloudContext.params.workOrderSolutions" id="workOrderSolutions">
							<option value="">
								--请选择--
							</option>
						</select>
						<font color="red">*</font><span class='errorMsg'></span>
					</td>
				</tr>
			<tr class="ck_to_be_hide">
				<td>
					网络 ：
				</td>
				<td>
					<select name="cloudContext.params.netWork" id="netWork">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr class="ck_to_be_hide">
				<td>
					计算节点：
				</td>
				<td>
					自动:<input type="radio"   name="cloudContext.params.customComputeResourceFlag" checked="checked" value="false" class="ck_radio"/> 
					手动:<input type="radio"   name="cloudContext.params.customComputeResourceFlag" value="true" class="ck_radio"/>
					<table width=90% >
						<tr class="ck_hidden" id="computeResourcePoolTr">
							<td style="width: 30%;">
								计算节点池：
							</td>
							<td>
								<select name="cloudContext.params.computeResourcePool" id="computeResourcePool">
									<option value="">
										--请选择--
									</option>
								</select>
								<font color="red">*</font><span class='errorMsg'></span>
							</td>
						</tr>
						<tr class="ck_hidden"  id="computeResourceNodeTr">
							<td>
								计算节点：
							</td>
							<td>
								<select name="cloudContext.params.computeResourceID" id="computeResourceNode">
									<option value="">
										--请选择--
									</option>
								</select>
								<font color="red">*</font><span class='errorMsg'></span>
							</td>
						</tr>
						</table>
				</td>
			</tr>
			<tr class="ck_to_be_hide">
				<td>
					虚机描述 ：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					审核备注：
				</td>
				<td>
					<textarea  rows="5" cols="20" id="handleMsg"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>
