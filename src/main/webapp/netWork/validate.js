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

	$('#cidr').live('blur', function() {
		var cidr = $('#cidr').val();
		var cidrPattern = /^(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3})(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\/(3[0-2]|[1-2]\d|[1-9])$/
		var $errorMsg = $('#cidr').siblings(errorBoxSelecter);
		var errorMsg = '';
		if (cidr == '') {
			errorMsg = '网段格式不能为空';
		} else if (!cidrPattern.test(cidr)) {
			errorMsg = '网段格式错误';
		} else {
			var preFixCidr = cidr.substring(0, cidr.lastIndexOf('.'));
			var startIP = $('#startIP').val();
			var preFixSIP = startIP.substring(0, startIP.lastIndexOf('.'));
			var endIP = $('#endIP').val();
			var preFixEIP = endIP.substring(0, endIP.lastIndexOf('.'));
			if (preFixCidr != preFixSIP) {
				errorMsg = '网段必须和起始IP网段一样';
			} else if (preFixCidr != preFixEIP) {
				errorMsg = '网络必须和结束IP网段一样'
			}
		}
		success = (errorMsg == '' && success);
		$errorMsg.html(errorMsg);
	});
	var ipPattern = /^(((25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)\.){3})(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)$/
	$('#startIP').live('blur', function() {
				var startIP = $('#startIP').val();
				var $errorMsg = $('#startIP').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (startIP == '') {
					errorMsg = '起始IP不能为空';
				} else if (!ipPattern.test(startIP)) {
					errorMsg = '起始IP格式错误';
				} else {
					var cidr = $('#cidr').val();
					var preFixCidr = cidr.substring(0, cidr.lastIndexOf('.'));
					var preFixSIP = startIP.substring(0, startIP
									.lastIndexOf('.'));
					var endIP = $('#endIP').val();
					var preFixEIP = endIP.substring(0, endIP.lastIndexOf('.'));
					if (preFixCidr != preFixSIP) {
						errorMsg = '起始IP必须和网段一个网段';
					} else if (preFixCidr != preFixEIP) {
						errorMsg = '起始IP必须和结束IP一个网段'
					}
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
	$('#endIP').live('blur', function() {
				var endIP = $('#endIP').val();
				var $errorMsg = $('#endIP').siblings(errorBoxSelecter);
				var errorMsg = '';
				if (endIP == '') {
					errorMsg = '结束IP不能为空';
				} else if (!ipPattern.test(endIP)) {
					errorMsg = '结束IP格式错误';
				} else {
					var cidr = $('#cidr').val();
					var preFixCidr = cidr.substring(0, cidr.lastIndexOf('.'));
					var startIP = $('#startIP').val();
					var preFixSIP = startIP.substring(0, startIP
									.lastIndexOf('.'));
					var preFixEIP = endIP.substring(0, endIP.lastIndexOf('.'));
					if (preFixCidr != preFixSIP) {
						errorMsg = '结束IP必须和网段一个网段';
					} else if (preFixCidr != preFixEIP) {
						errorMsg = '结束IP必须和起始IP一个网段'
					}
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
	}
})