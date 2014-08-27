<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					标题
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.title" id="title" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					存储:
				</td>
				<td>
					<input type="text" name="cloudContext.vo.storageCapacity"
						maxlength="20" id="storageCapacity"
						onkeydown="return keydown(event)" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					cpu:
				</td>
				<td>
					<input type="text" name="cloudContext.vo.cpu" id="cpu"
						maxlength="20" onkeydown="return keydown(event)" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					内存:
				</td>
				<td>
					<input type="text" name="cloudContext.vo.memory" id="memory"
						maxlength="20" onkeydown="return keydown(event)" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					描述
				</td>
				<td>
					<textarea rows="" cols="" name="cloudContext.vo.content"
						id="content"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>

