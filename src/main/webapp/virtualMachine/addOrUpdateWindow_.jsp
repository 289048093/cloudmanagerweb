<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addOrUpdateForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					虚拟机 ：
					<s:token></s:token>
				</td>
				<td>
					<input type="text" name="cloudContext.vo.name" id="name" />
					<font color="red">*</font><span class='errorMsg'></span>
					<input type="hidden" name="cloudContext.vo.id" id="vmId" />
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
					配置 ：
				</td>
				<td>
					<select name="cloudContext.params.machineTypeID" id="machineTypeID"
						onchange="changeMachineType($(this).val());">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					模板 ：
				</td>
				<td>
					<select name="cloudContext.params.templateID" id="templateID"
						onchange="changeTemplate($(this).val());">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					网络 ：
				</td>
				<td>
					<select name="cloudContext.params.netWork" id="netWork">
						<option value="">
							--请选择--
						</option>
					</select>
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					自动备份 ：
				</td>
				<td>
					<input type="hidden" name="cloudContext.vo.backupTimeMark"
						id="backupTimeMark" />
					<div>
						<input type="checkbox" id="checkBackup" />
						<div style="display: inline;" id="backupDateDiv">
							<select id="backupMonth">
								<option value="">
									--
								</option>
							</select>
							月
							<select id="backupDay">
								<option value="">
									--
								</option>
							</select>
							日&nbsp; 星期
							<select id="backupWeek">
								<option value="">
									--
								</option>
							</select>
							<select id="backupHour">
							</select>
							时
							<select id="backupMinute">
							</select>
							分
						</div>
						<span class='errorMsg'></span>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					计算节点：
				</td>
				<td>
					自动:
					<input type="radio"
						name="cloudContext.params.customComputeResourceFlag"
						checked="checked" value="false" class="ck_radio" />
					手动:
					<input type="radio"
						name="cloudContext.params.customComputeResourceFlag" value="true"
						class="ck_radio" />
					<table width=90%>
						<tr class="ck_hidden" id="computeResourcePoolTr">
							<td style="width: 30%;">
								计算节点池：
							</td>
							<td>
								<select name="cloudContext.params.computeResourcePool"
									id="computeResourcePool">
									<option value="">
										--请选择--
									</option>
								</select>
								<font color="red">*</font><span class='errorMsg'></span>
							</td>
						</tr>
						<tr class="ck_hidden" id="computeResourceNodeTr">
							<td>
								计算节点：
							</td>
							<td>
								<select name="cloudContext.params.computeResourceID"
									id="computeResourceNode">
									<option value="">
										--请选择--
									</option>
								</select>
								<font color="red">*</font><span class='errorMsg'></span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					描述 ：
				</td>
				<td>
					<textarea rows="5" cols="20" name="cloudContext.vo.desc" id="desc"
						onkeypress="return descKeydown(event,$(this));"></textarea>
					<span class='errorMsg'></span>
				</td>
			</tr>
		</table>
	</form>
</div>
<SCRIPT type="text/javascript">
	$(function(){
		for(var i=1;i<=12;i++){
			$('#backupMonth').append('<option value="'+i+'">'+(i<10?('0'+i):i)+'</option>');
		}
		for(var i=1;i<=31;i++){
			$('#backupDay').append('<option value="'+i+'">'+(i<10?('0'+i):i)+'</option>');
		}
		$('#backupWeek').append('<option value="'+2+'">'+"一"+'</option>');
		$('#backupWeek').append('<option value="'+3+'">'+"二"+'</option>');
		$('#backupWeek').append('<option value="'+4+'">'+"三"+'</option>');
		$('#backupWeek').append('<option value="'+5+'">'+"四"+'</option>');
		$('#backupWeek').append('<option value="'+6+'">'+"五"+'</option>');
		$('#backupWeek').append('<option value="'+7+'">'+"六"+'</option>');
		$('#backupWeek').append('<option value="'+1+'">'+"日"+'</option>');
		for(var i=0;i<=23;i++){
			$('#backupHour').append('<option value="'+i+'">'+(i<10?('0'+i):i)+'</option>');
		}
		for(var i=0;i<=59;i++){
			$('#backupMinute').append('<option value="'+i+'">'+(i<10?('0'+i):i)+'</option>');
		}
		$('#backupDateDiv select').attr("disabled", true);
		$('#checkBackup').live('click',function(){
			if($('#checkBackup').attr('checked')){
				$('#backupDateDiv select').attr("disabled", false);
			}else{
				$('#backupDateDiv select').attr("disabled", true);
			}
		});
	});
</SCRIPT>
