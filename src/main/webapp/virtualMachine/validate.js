var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
			$('#name').live('blur', function() {
						var name = $('#name').val();
						var $errorMsg = $('#name').siblings(errorBoxSelecter);
						var errorMsg = ''
						if (name == '') {
							errorMsg = '名称不能为空';
						} else if (name.length > 20) {
							errorMsg = '虚拟机长度必须小于20';
						} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
							errorMsg = '虚拟机必须为数字、字母、下划线或者汉字组成';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#machineTypeID').live('blur', function() {
						var val = $('#machineTypeID').val();
						var $errorMsg = $('#machineTypeID')
								.siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = '配置必须选择！';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#templateID').live('blur', function() {
						var val = $('#templateID').val();
						var $errorMsg = $('#templateID')
								.siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = '模版必须选择！';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#netWork').live('blur', function() {
						var val = $('#netWork').val();
						var $errorMsg = $('#netWork')
								.siblings(errorBoxSelecter);
						var errorMsg = '';
						if (val == '') {
							errorMsg = '网络必须选择！';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#computeResourceNode').live('blur', function() {
				if ($('#computeResourcePool').val() == '') {
					return;
				}
				var val = $('#computeResourceNode').val();
				var $errorMsg = $('#computeResourceNode')
						.siblings(errorBoxSelecter);
				var errorMsg = '';
				if (val == '') {
					errorMsg = '必须选择一个计算节点！';
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
			}
		})
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