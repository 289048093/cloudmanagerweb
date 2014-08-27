var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
	$('#name').live('blur', function() {
				var name = $('#name').val();
				var $errorMsg = $('#name').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (name == '') {
					errorMsg = '名字不能为空';
				} else if (name.length > 20) {
					errorMsg = '名字长度必须小于20';
				} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
					errorMsg = '名称必须为数字、字母、下划线或者汉字组成';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});

	$('#machineRoomID').live('blur', function() {
				var type = $('#machineRoomID').val();
				var $errorMsg = $('#machineRoomID').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (type == '') {
					errorMsg = '机房必须选择';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#machineRackID').live('blur', function() {
				var type = $('#machineRackID').val();
				var $errorMsg = $('#machineRackID').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (type == '') {
					errorMsg = '机架必须选择';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	var ipPattern = /^(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3})(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/;
	$('#ip').live('blur', function() {
				var startIP = $('#ip').val();
				var $errorMsg = $('#ip').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (startIP == '') {
					errorMsg = 'IP不能为空';
				} else if (!ipPattern.test(startIP)) {
					errorMsg = 'IP格式错误';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#username').live('blur', function() {
				var username = $('#username').val();
				var $errorMsg = $('#username').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (username == '') {
					errorMsg = '名字不能为空';
				} else if (username.length > 20) {
					errorMsg = '名字长度必须小于20';
				} else if (!/^[\w\u4e00-\u9fa5]+$/.test(username)) {
					errorMsg = '名称必须为数字、字母、下划线或者汉字组成';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#password').live('blur', function() {
				var password = $('#password').val();
				var $errorMsg = $('#password').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (password == '') {
					errorMsg = '密码不能为空';
				} else if (password.length > 20) {
					errorMsg = '密码长度必须小于20';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#desc').live('blur', function() {
				var val = $('#desc').val();
				var $errorMsg = $('#desc').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (val.length > 250) {
					errorMsg = '描述内容长度不能超过250！';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	validate = function() {
		success = true;
		$('#addOrUpdateForm input').blur();
		$('#addOrUpdateForm select').blur();
		$('#addOrUpdateForm textarea').blur();
		return success;
	};
});
/**
 * 数字输入框限制 keyCode(tab[9], .[190], 0-9[48-57], backspace[8], delete[46],
 * 方向[37-40])
 * 
 * @param {}
 *            event
 * @return {Boolean}
 */
function keydown(event) {// 数字输入框限制
	var kc = event.keyCode;
	if (kc == 190 || kc == 8 || kc == 46 || (kc >= 37 && kc <= 40) || kc >= 48
			&& kc <= 57) {
		return true;
	}
	return false;
}