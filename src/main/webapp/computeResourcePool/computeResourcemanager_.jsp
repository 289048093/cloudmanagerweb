<%@ taglib prefix="s" uri="/struts-tags"%>
<div>
	<form action="" method="post" id="crForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					<s:token></s:token>
					<input type="hidden" name="cloudContext.vo.id" id="resourcePoolId" />
					<table width="288" border="0" cellpadding="0" cellspacing="0"
						id="crSelectTable">
						<thead>
							<tr>
								<td>
									已有资源
								</td>
								<td></td>
								<td>
									可用资源
								</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="100">
									<input type="hidden" name="cloudContext.params.prevResourceIds"
										id="prevResourceIds" />
									<select name="cloudContext.params.resourceIds" size="10"
										multiple="multiple" id="s1" style="width: 180px;min-height: 250px;">
									</select>
								</td>
								<td width="37" align="center"> 
									<input type="button" name="remove" id="remove" value="<<添加" class="ck_move_btn"/>
									<br /> 
									<input type="button" name="add" id="add" value="移除>>" class="ck_move_btn"/>
									<br />
								</td>
								<td width="100">
									<select name="s2" size="10" multiple="multiple" id="s2"
										style="width: 180px;min-height: 250px;">
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</form>
</div>