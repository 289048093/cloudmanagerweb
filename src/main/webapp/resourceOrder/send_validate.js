var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
			$('#title').live('blur', function() {
						var name = $('#title').val();
						var $errorMsg = $('#title').siblings(errorBoxSelecter);
						var errorMsg = ''
						if (name == '') {
							errorMsg = '标题不能为空';
						} else if (name.length > 20) {
							errorMsg = '标题长度必须小于20';
						} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
							errorMsg = '标题必须为数字、字母、下划线或者汉字组成';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});

			$('#storageCapacity').live('blur', function() {
				var storage = $('#storageCapacity').val();
				var $errorMsg = $('#storageCapacity')
						.siblings(errorBoxSelecter);
				var errorMsg = ''
				if (!/^\d*$/.test(storage)) {
					errorMsg = '存储大小必须为自然数';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			$('#cpu').live('blur', function() {
						var cpu = $('#cpu').val();
						var $errorMsg = $('#cpu').siblings(errorBoxSelecter);
						var errorMsg = ''
						if (!/^\d*$/.test(cpu)) {
							errorMsg = 'cpu核数必须为自然数';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#memory').live('blur', function() {
						var storage = $('#storageCapacity').val();
						var cpu = $('#cpu').val();
						var memory = $('#memory').val();
						var $errorMsg = $('#memory').siblings(errorBoxSelecter);
						var errorMsg = ''
						if (!/^\d*$/.test(memory)) {
							errorMsg = '内存大小必须为自然数';
						} else if (storage == '' && cpu == '' && memory == '') {
							errorMsg = '存储,cpu,内存必须添一项';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			validate = function() {
				success = true;
				$('#addOrUpdateForm input').blur();
				return success;
			}
		})

/**
 * 数字输入框限制
 * 
 * @param {}
 *            event
 * @return {Boolean}
 */
function keydown(event) {// 数字输入框限制
	var kc = event.keyCode;
	if (kc == 9 || kc == 190 || kc == 8 || kc == 46 || (kc >= 37 && kc <= 40)
			|| kc >= 48 && kc <= 57) {
		return true;
	}
	return false;
}