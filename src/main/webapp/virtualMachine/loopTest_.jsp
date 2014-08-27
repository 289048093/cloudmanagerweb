<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="loopForm">
		<table>
			<tr>
				<td>
					虚机Id:
				</td>
				<td>
					<input type="text" name="cloudContext.vo.id" id="loopVMId" />
				</td>
			<tr />
			<tr>
				<td colspan="2">
				<input type="button" value="备份循环" id="backupLoop">
				<input type="button" value="还原循环" id="restoreLoop">
				</td>
			<tr />
			<tr>
				<td>
					202Id:
				</td>
				<td>
					<input type="text" name="cloudContext.params.cr202Id" id="loopCr202Id" />
				</td>
			<tr />
			<tr>
				<td>
					204Id:
				</td>
				<td>
					<input type="text" name="cloudContext.params.cr204Id" id="loopCr204Id" />
				</td>
			<tr />
			<tr>
				<td colspan="2">
				<input type="button" value="迁移循环" id="migrateLoop">
				</td>
			<tr />
		</table>

	</form>
</div>
