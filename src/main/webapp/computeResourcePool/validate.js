/**
 * 表单验证
 * 
 * @type
 */
var validate = null;
var success = true;
var errorBoxSelecter = '.errorMsg';
$(function() {
			$('#name').live('blur', function() {// 验证名称
						var name = $('#name').val();
						var $errorMsg = $('#name').siblings(errorBoxSelecter);
						var errorMsg = '';
						if (name == '') {
							errorMsg = '名称不能为空';
						} else if (name.length > 20) {
							errorMsg = '名称长度不能大于20';
						} else if (!/^[\w\u4e00-\u9fa5]{0,20}$/.test(name)) {
							errorMsg = '名称由字母或数字或者下划线组成';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			$('#cpuRate').live('blur', function() {// 验证cpu使用率
						rateValid($('#cpuRate'));
					});
			$('#memoryRate').live('blur', function() {// 验证内存使用率
						rateValid($('#memoryRate'));
					});
			$('#domainDiv').live('blur', function() {// 验证是否选择了域
						$('#useToValidate').blur();
					});
			$('#useToValidate').live('blur', function() {// 用于域的验证
						var zTree = $.fn.zTree.getZTreeObj("treeDiv");
						var checkedNodes = zTree.getCheckedNodes(true);
						var $errorMsg = $('#useToValidate')
								.siblings(errorBoxSelecter);
						if (checkedNodes.length == 0) {
							$errorMsg.html('必须选择一个上级');
						} else {
							$errorMsg.html('');
						}
						success = (checkedNodes.length > 0 && success);
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
			function rateValid($node) {// cpu或者内存使用率验证
				var val = $node.val();
				var $errorMsg = $node.siblings(errorBoxSelecter);
				var errorMsg = '';
				if (val == '') {
					errorMsg = '超配比例不能为空';
				} else if (val.length > 5) {
					errorMsg = '超配比例长度不能超过5';
				} else if (!/^\d{1,2}(\.\d+)?$/.test(val)) {
					errorMsg = '超配比例必须为一个100以内的数';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			}
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