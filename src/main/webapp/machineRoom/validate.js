/**
 * 表单验证
 * 
 * @type
 */
var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
			/**
			 * 用户名验证
			 */
			$('#name').live('blur', function() {
						var name = $('#name').val();
						var $errorMsg = $('#name').siblings(errorBoxSelecter);
						var errorMsg = '';
						if (name == '') {
							errorMsg = '用户名不能为空';
						} else if (name.length > 20) {
							errorMsg = '长度不能超过20';
						} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
							errorMsg = '用户名由字母或数字或者下划线组成';
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
				return success;
			};
		});