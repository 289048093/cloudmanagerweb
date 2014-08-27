<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td width=20%>
					域：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" id="name"
						maxlength="20" />
					<font color="red">*</font><span class="errorMsg"></span>
					<input type="hidden" name="cloudContext.vo.id" id="domainId" />
					<input type="hidden" name="cloudContext.params.isRootDomain"
						id="isRootDomain">
				</td>
			</tr>
			<tr>
				<td>
					上级：
				</td>
				<td>
					<div id="superDomainDiv">
						<ul class='ztree' id="treeDiv"
							style="height: 100px; width: 350px; overflow: auto;">
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
					描述：
				</td>
				<td>
					<textarea rows="3" cols="20" name="cloudContext.vo.desc" id="desc"
						onkeydown="return descKeydown(event,$(this));"></textarea>
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					可用存储空间(G)：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.storageCapacity"
						id="storageCapacity" maxlength="20"
						onkeydown="return keydown(event)" />
					<span id="storageRange"></span>
					<span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					可用备份存储空间(G)：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.backupStorageCapacity"
						id="backupStorageCapacity" maxlength="20"
						onkeydown="return keydown(event)" />
					<span id="backupStorageRange"></span>
					<span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					可用cpu核数：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.cpuTotalNum"
						id="cpuTotalNum" maxlength="10" onkeydown="return keydown(event)" />
					<span id="cpuRange"></span>
					<span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					可用内存空间(M)：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.memoryCapacity"
						id="memoryCapacity" maxlength="20"
						onkeydown="return keydown(event)" />
					<span id="memoryRange"></span>
					<span class="errorMsg"></span>
				</td>
			</tr>
		</table>
	</form>
</div>