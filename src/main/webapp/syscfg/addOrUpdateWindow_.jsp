<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					名称
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.key" id="displayKey" />
					<font color="red">*</font>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					值
				</td>
				<td>
					<input type="text" name="cloudContext.vo.value" id="value" />
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					描述
				</td>
				<td>
					<input type="text" name="cloudContext.vo.desc" id="desc" />
				</td>
			</tr>
		</table>
	</form>
</div>

