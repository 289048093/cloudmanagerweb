<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="backupForm">
		<input type="hidden" name="cloudContext.vo.id" id="backupVMId" />
		<input type="hidden" name="cloudContext.vo.backupId" id="backupId" />
		<s:token></s:token>
		<textarea rows="5" cols="20" name="cloudContext.params.desc" id="desc"
			onkeydown="return descKeydown(event,$(this));"></textarea>
		<span class='errorMsg'></span>
	</form>
</div>
