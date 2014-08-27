<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					用户名
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.username" id="username" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					姓名
				</td>
				<td>
					<input type="text" name="cloudContext.vo.realname" id="realname" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					电子邮箱：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.email" id="email" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					电话：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.telPhone" id="telPhone" />
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					手机：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.cellPhone" id="cellPhone" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					密码
				</td>
				<td>
					<input type="password" name="cloudContext.vo.password"
						id="password" />
					<font color="red">*</font>
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					确认密码
				</td>
				<td>
					<input type="password" id="repassword" />
					<font color="red">*</font>
					<span class='errorMsg'></span>
				</td>
			</tr>
		</table>
	</form>
</div>
