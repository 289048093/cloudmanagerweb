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
							errorMsg = '名称长度必须小于20';
						} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
							errorMsg = '名称必须为数字、字母、下划线或者汉字组成';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#machineRoomID').live('blur', function() {
						var val = $('#machineRoomID').val();
						var $errorMsg = $('#machineRoomID')
								.siblings(errorBoxSelecter);
						var errorMsg = ''
						if (val == '') {
							errorMsg = '机房必须选择';
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