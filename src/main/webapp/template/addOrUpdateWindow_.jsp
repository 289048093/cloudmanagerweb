<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					模板 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" id="name" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					域 ：
				</td>
				<td>
					<select name="cloudContext.vo.domainCode" id="domains"
						onchange="selectDomain()">
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					镜像类型：
				</td>
				<td>
					<select id="type" name="cloudContext.vo.type">
						<option value="1" selected="selected">
							本地镜像
						</option>
						<option value="2">
							远程下载
						</option>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr style="display: none;" id="fileURL_tr">
				<td>
					URL：
				</td>
				<td>
					<input type="text" id="url" name="cloudContext.vo.url" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr id="fileName_tr">
				<td>
					选择文件：
				</td>
				<td>
					<select id="fileName" name="cloudContext.vo.fileName"></select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					模版用户名 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.username" id="username"
						maxlength="255" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					模版密码 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.password" id="password"
						maxlength="255" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					描述：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc" onkeydown="return descKeydown(event,$(this));"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>
