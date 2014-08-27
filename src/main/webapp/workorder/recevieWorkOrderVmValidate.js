var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
	$('#name').live('blur', function() {
				var name = $('#name').val();
				var $errorMsg = $('#name').siblings(errorBoxSelecter);
				var errorMsg = ''
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
				var errorMsg = ''
				if (type == '') {
					errorMsg = '机房必须选择';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#machineRackID').live('blur', function() {
				var type = $('#machineRackID').val();
				var $errorMsg = $('#machineRackID').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (type == '') {
					errorMsg = '机架必须选择';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	var ipPattern = /^(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3})(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/
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
	validate = function() {
		success = true;
		$('#addOrUpdateForm input').blur();
		$('#addOrUpdateForm select').blur();
		return success;
	}
})