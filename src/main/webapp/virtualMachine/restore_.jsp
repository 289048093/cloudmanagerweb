<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="restoreForm">
		<input type="hidden" name="cloudContext.vo.id" id="restoreVMId" />
		<input type="hidden" name="cloudContext.params.backupId" id="backupId" />
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<thead>
				<tr>
					<td>
						日期 
						<s:token></s:token>
					</td>
					<td>
						描述 
					</td>
					<td>
						状态
					</td>
				</tr>
			</thead>
			<tbody id="vmBackupData">

			</tbody>
		</table>
	</form>
</div>
