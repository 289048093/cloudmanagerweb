<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					设备 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" id="name" />
					<font color="red">*</font><span class="errorMsg"></span>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
			</tr>
			<tr>
				<td>
					机房 ：
				</td>
				<td>
					<select name="cloudContext.params.roomID" id="machineRoomID"
						onchange="javascript:queryRackByRoom('machineRackID',$(this).val(),'此机房下面没有机柜，请先添加！');">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					机架 ：
				</td>
				<td>
					<select name="cloudContext.params.rackID" id="machineRackID">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					IP地址 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.ip" id="ip" />
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					导致报警：
				</td>
				<td>
					<input type="hidden" name="cloudContext.vo.warn4Rack"
						id="warn4RackID" />
					<input type="checkbox" id="checkWarn" />
					<div style="display: inline;" id="warnDiv">
						&nbsp;&nbsp;&nbsp;&nbsp;cpu：
						<input type="text" value="0" style="width: 30px" maxlength="2"
							id="cpuRate4Warn" onkeypress="return keydown(event);"/>
						%&nbsp;&nbsp;&nbsp;&nbsp;内存：
						<input type="text" value="0" style="width: 30px" maxlength="2"
							id="memoryRate4Warn" onkeypress="return keydown(event);"/>
						%
					</div>
				</td>
			</tr>
			<tr>
				<td>
					用户名：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.username" id="username" />
					<font color="red">*</font><span class="errorMsg"></span>
				</td>
			</tr>
			<tr>
				<td>
					密码：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.password" id="password" />
					<font color="red">*</font><span class="errorMsg"></span>
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
<SCRIPT type="text/javascript">
	$(function(){
		$('#warnDiv input').attr("disabled", true);
		$('#checkWarn').live('click',function(){
			if($('#checkWarn').attr('checked')){
				$('#warnDiv input').attr("disabled", false);
			}else{
				$('#warnDiv input').attr("disabled", true);
			}
		});
	});
</SCRIPT>
