<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" id="addAndUpdateTab">
			<tr>
				<td width=20%>
					计算节点池：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" maxlength="20"
						id="name" />
					<font color="red">*</font><span class="errorMsg"></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
					<input type="hidden" name="cloudContext.params.domainIDs"
						id="domainIDs" />
				</td>
			</tr>
			<tr>
				<td>
					cpu超配比例(倍)：
				</td>
				<td>
					<input type="text" value="1.5" name="cloudContext.vo.cpuRate"
						id="cpuRate" maxlength="5" onkeypress="return keydown(event)" />
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					内存超配比例(倍)：
				</td>
				<td>
					<input type="text" value="1.5" name="cloudContext.vo.memoryRate"
						id="memoryRate" maxlength="5" onkeypress="return keydown(event)" />
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					描述：
				</td>
				<td>
					<textarea rows="2" cols="20" name="cloudContext.vo.desc" id="desc"
						onkeydown="return descKeydown(event,$(this));"></textarea>
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					域：
				</td>
				<td>
					<div id="domainDiv" style="float: left; width: 83%;">
						<ul class=ztree id="treeDiv">
						</ul>
					</div>
					<font color="red" style="margin-left: 20px;">*</font><span
						class="errorMsg"></span>
					<input type="hidden" id="useToValidate">
				</td>
			</tr>
		</table>
	</form>
</div>
