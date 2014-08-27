var validate = null;
var success = true;
$(function() {
	var errorBoxSelecter = '.errorMsg';
	/**
	 * 用户名验证
	 */
	$('#username').live('blur', function() {
				var username = $('#username').val();
				var $errorMsg = $('#username').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (username == '') {
					errorMsg = '用户名不能为空';
				} else if (username.length > 20) {
					errorMsg = '长度不能超过20';
				} else if (!/^\w+$/.test(username)) {
					errorMsg = '用户名由字母或数字或者下划线组成';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	/**
	 * 姓名验证
	 */
	$('#realname').live('blur', function() {
				var realname = $('#realname').val();
				var $errorMsg = $('#realname').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (realname == '') {
					errorMsg = '姓名不能为空';
				} else if (realname.length > 6) {
					errorMsg = '长度不能超过6';
				} else if (!/^[a-zA-Z\u4e00-\u9fa5]+$/.test(realname)) {
					errorMsg = '姓名为中文或字母';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	/**
	 * 邮箱验证
	 */
	$('#email').live('blur', function() {
				var val = $('#email').val();
				var $errorMsg = $('#email').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (val == '') {
					errorMsg = '邮箱不能为空';
				} else if (!/^([\w\.\-])+\@(([\w\-])+\.)+(\w{2,4})+$/.test(val)) {
					errorMsg = '邮箱地址无效！';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	/**
	 * 电话验证
	 */
	$('#telPhone').live('blur', function() {
				var val = $('#telPhone').val();
				var $errorMsg = $('#telPhone').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (val != '' && !/^(0\d{2,3}-?)?\d{5,9}$/.test(val)) {
					errorMsg = '电话号码无效！';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	/**
	 * 手机号码验证
	 */
	$('#cellPhone').live('blur', function() {
				var val = $('#cellPhone').val();
				var $errorMsg = $('#cellPhone').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (val == '') {
					errorMsg = '手机号码不能为空！';
				} else if (val && !/^(\+\d{2,4})?1[3\|5\|8]\d{9}$/.test(val)) {
					errorMsg = '手机号码无效！';
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
					if (isUpdate()) {
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
					if (isUpdate() && pwd == '') {
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
		return success;
	}
});