<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="ck_pop_html_wrapper">
	<form action="#" method="post" id="selfInfoForm">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>
					用户名
					<s:token></s:token>
					<input type="hidden" name="cloudContext.vo.id" id="id" />
				</td>
				<td>
					<input type="text" name="cloudContext.vo.username" id="username" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					姓名
				</td>
				<td>
					<input type="text" name="cloudContext.vo.realname" id="realname"
						disabled="disabled" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					电子邮箱：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.email" id="email" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					电话：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.telPhone" id="telPhone" />
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					手机：
				</td>
				<td>
					<input type="text" name="cloudContext.vo.cellPhone" id="cellPhone" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					旧密码
				</td>
				<td>
					<input type="password" name="cloudContext.params.oldPassword"
						id="oldPassword" />
					<font color="red">*</font><span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					新密码
				</td>
				<td>
					<input type="password" name="cloudContext.vo.password"
						id="password" />
					<span class='errorMsg'></span>
				</td>
			</tr>
			<tr>
				<td>
					再次确认新密码
				</td>
				<td>
					<input type="password" id="repassword" />
					<span class='errorMsg'></span>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
<!--
	$(function(){
		var errorBoxSelecter = '.errorMsg';
		$('#oldPassword').live('blur', function() {
				var pwd = $('#oldPassword').val();
				var $errorMsg = $('#oldPassword').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (pwd == '') {
					errorMsg = '原密码不能为空';
				} else if (pwd.length < 6) {
					errorMsg = '密码长度不能少于六位';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			/**
	 * 密码验证
	 */
	$('#password').live('blur', function() {
				var pwd = $('#password').val();
				var $errorMsg = $('#password').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (pwd == '') {
					if ((isUpdate()||isUpdateSelf())) {
						return;
					}
					errorMsg = '密码不能为空';
				} else if (pwd.length < 6) {
					errorMsg = '密码长度不能少于六位';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			/**
			 * 确认密码验证
			 */
			$('#repassword').live('blur', function() {
				var $errorMsg = $('#repassword').siblings(errorBoxSelecter);
				var rePwd = $('#repassword').val();
				var pwd = $('#password').val();
				var errorMsg = '';
				if (rePwd == '') {
					if ((isUpdate()||isUpdateSelf())) {
						return;
					}
					errorMsg = '确认密码不能为空';
				} else if (rePwd != pwd) {
					errorMsg = '两次密码输入不一致';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			validate = function() {
				success = true;
				$('#addOrUpdateForm input').blur();
				$('#addOrUpdateForm select').blur();
				$('#selfInfoForm input').blur();
				return success;
			}
		});
		function isUpdateSelf(){
			return $('.ck_pop_win_title').html()=='编辑用户';
		}
//-->
</script>
