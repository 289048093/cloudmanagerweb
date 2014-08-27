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

			$('#cpu').live('blur', function() {
						var val = $('#cpu').val();
						var $errorMsg = $('#cpu').siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = 'cpu必须选择';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#memory').live('blur', function() {
						var val = $('#memory').val();
						var $errorMsg = $('#memory').siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = '内存必须选择';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#disk').live('blur', function() {
						var val = $('#disk').val();
						var $errorMsg = $('#disk').siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = '硬盘必须选择';
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