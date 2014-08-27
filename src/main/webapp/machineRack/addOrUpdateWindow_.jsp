<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					名字 ：
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
					机房 ：
				</td>
				<td>
					<select name="cloudContext.params.roomID" id="machineRoomID">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr class="ck_hidden">
				<td>
					导致机房报警：
				</td>
				<td>
					<select name="cloudContext.vo.warn4Room" id="warn4RoomID">
						<option value="false">
							否
						</option>
						<option value="true">
							是
						</option>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td>
					描述：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc"
						onkeydown="return descKeydown(event,$(this));"></textarea>
					<span class='errorMsg'></span>
				</td>
			</tr>
		</table>
	</form>
</div>
