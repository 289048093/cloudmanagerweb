<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.cloudking.cloudmanagerweb.util.DateUtil"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>
<%
		    String startDate = request.getParameter("startDate");
		    if (StringUtil.isBlank(startDate)) {
		        Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.DAY_OF_MONTH,+1);
		        startDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
		    }
		    
		%>			
<div id="ck_pop_html_wrapper">
	<s:form action="#" method="post" id="addOrUpdateForm" enctype="multipart/form-data">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					标题:
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
					发送域：
				</td>
				<td>
					<select name="cloudContext.params.sendDomain" id="sendDomain">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					接收域：
				</td>
				<td>
					<select name="cloudContext.params.receiveDomain" id="receiveDomain">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					到期时间:
				</td>
				<td>
					<input type="text" class="Wdate" id="dueDate" name="cloudContext.vo.dueDate"
							value="<%=startDate%>"	onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr> 
			<tr>
				<td>
					内容:
				</td>
				<td>
					<textarea rows="" cols="20" name="cloudContext.vo.content"
						id="content"></textarea>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			  <tr>
				<td colspan="2">
					<s:file name="upload" label="附件"/>
				</td>
			</tr>
		</table>
	</s:form>
</div>

