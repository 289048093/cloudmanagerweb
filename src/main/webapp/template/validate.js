var validate = null;
var success = true;
$(function() {
	var errorBoxSelecter = '.errorMsg';
	$('#name').live('blur', function() {
				var name = $('#name').val();
				var $errorMsg = $('#name').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (name == '') {
					errorMsg = '用户名不能为空';
				} else if (name.length > 20) {
					errorMsg = '名称长度必须小于20';
				} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
					errorMsg = '名称必须为数字、字母、下划线或者汉字组成';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#url').live('blur', function() {
		if ($('#type').val() != '2') {
			return;
		}
		var url = $('#url').val();
		var $errorMsg = $('#url').siblings(errorBoxSelecter);
		var errorMsg = '';
		if (url == '') {
			errorMsg = 'url不能为空';
		} else if (!/[http:\/\/]?([\w-]+\.)+[\w-]+(\/[\w-.\/?%&=]*)?/.test(url)) {
			errorMsg = 'url地址错误';
		}
		success = (errorMsg == '' && success);
		$errorMsg.html(errorMsg);
	});
	$('#username').live('blur', function() {
				var val = $('#username').val();
				var $errorMsg = $('#username').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (val == '') {
					errorMsg = '模版用户名不能为空';
				} else if (!/^\w+$/.test(val)) {
					errorMsg = '模版用户名必须由字母数字下划线组成';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#password').live('blur', function() {
				var val = $('#password').val();
				var $errorMsg = $('#password').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (val == '') {
					errorMsg = '模版密码不能为空';
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
		$('#addOrUpdateForm textarea').blur();
		if ($('#type').val() == 1) {
			var fileName = $('#fileName').val();
			var $errorMsg = $('#fileName').siblings(errorBoxSelecter);
			var errorMsg = '';
			if (!fileName) {
				errorMsg = '没有本地镜像';
				success = false;
			}
			$errorMsg.html(errorMsg);
		}
		return success;
	};
});