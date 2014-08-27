<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="addVolumnForm">
		<fieldset>
			<legend>
				<input type="button" id="currentVolumnAddOp" value="+"
					style="width: 25px;" />
				当前卷:
			</legend>
			<table width="100%" cellpadding="0" cellspacing="0" border="0"
				id="currentVolumns">
				<thead>
					<tr>
						<th style="text-align: left;">
							名称
						</th>
						<th style="text-align: left;">
							大小(G)
						</th>
						<th style="text-align: left;">
							操作
						</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</fieldset>
		<br />
		<fieldset>
			<legend>
				<input type="button" id="addVolumn" value="+" style="width: 25px;" />
				添加新卷:
			</legend>
			<ul id="addVolumns_wrapper" style="display: none">
				<li>
					<span>卷大小(G)</span>
					<span><input type="text" id="volumnSize" /><font
						color="red">*</font><span class='errorMsg'
						style="display: inline;"></span><a href="javascript:;"
						id="addVolumnSubmit">添加</a> </span>
				</li>
			</ul>
		</fieldset>





	</form>
</div>
