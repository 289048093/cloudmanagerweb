<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					名称：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" id="name"
						maxlength="20" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					网段：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.cidr" id="cidr"
						value="192.168.1.0/24" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					开始IP：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.startIP"
						value="192.168.1.1" id="startIP" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					结束IP
				</td>
				<td>
					<input type="text" name="cloudContext.vo.endIP"
						value="192.168.1.254" id="endIP" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.type" value="Nat" />
				</td>
			</tr>
			<!-- <tr>
				<td>
					网络类型：
				</td>
				<td>
					<select name="cloudContext.vo.type" id="type">
						<option value="Nat">
							Nat
						</option>
						<option value="Bridge">
							Bridge
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>  -->
			<tr>
				<td>
					描述：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc"
						onkeydown="return descKeydown(event,$(this));"></textarea>
					<span class='errorMsg'></span>
				</td>
			</tr>
		</table>
	</form>
</div>
