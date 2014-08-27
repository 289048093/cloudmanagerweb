<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					配置 ：
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
					域 ：
				</td>
				<td>
					<select name="cloudContext.vo.domainId" id="domains">
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					CPU：
				</td>
				<td>
					<select name="cloudContext.vo.cpu" id="cpu">
						<option value="">
							--请选择--
						</option>
						<option value="1">
							1
						</option>
						<option value="2">
							2
						</option>
						<option value="4">
							4
						</option>
						<option value="8">
							8
						</option>
					</select>
					<font color="red">*</font> 核数
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					内存：
				</td>
				<td>
					<select name="cloudContext.vo.memory" id="memory">
						<option value="">
							--请选择--
						</option>
						<option value="512">
							512
						</option>
						<option value="1024">
							1024
						</option>
						<option value="2048">
							2048
						</option>
						<option value="4096">
							4096
						</option>
					</select>
					<font color="red">*</font> M
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					硬盘：
				</td>
				<td>
					<select name="cloudContext.vo.disk" id="disk">
						<option value="">
							--请选择--
						</option>
						<option value="5">
							5
						</option>
						<option value="10">
							10
						</option>
						<option value="20">
							20
						</option>
						<option value="50">
							50
						</option>
						<option value="100">
							100
						</option>
					</select>
					<font color="red">*</font> G
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					描述：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>

