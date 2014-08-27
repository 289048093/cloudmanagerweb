var validate = null;
var success = true;
$(function() {
			var errorBoxSelecter = '.errorMsg';
			/**
			 * 名称验证
			 */
			$('#name').live('blur', function() {
						var name = $('#name').val();
						var $errorMsg = $('#name').siblings(errorBoxSelecter);
						var errorMsg = ''
						if (name == '') {
							errorMsg = '名称不能为空';
						} else if (name.length > 20) {
							errorMsg = '名称长度不能超过20';
						} else if (!/^[\w\u4e00-\u9fa5]+$/.test(name)) {
							errorMsg = '名称由字母或数字中文或者下划线组成';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					});
			/**
			 * 上级域验证
			 */
			$('#superDomainDiv').live('blur', function() {
						$('#useToValidate').blur();
					});
			$('#useToValidate').live('blur', function() {
						if (!isAdd()) {
							return;
						}
						var $errorMsg = $('#useToValidate')
								.siblings(errorBoxSelecter);
						var errorMsg = ''
						var zTree = $.fn.zTree.getZTreeObj("treeDiv");
						var nodes = zTree.getCheckedNodes(true);
						if (nodes.length == 0) {
							errorMsg = '必须选择一个上级';
						}
						success = (errorMsg == '' && success);
						$errorMsg.html(errorMsg);
					})
			$('#storageCapacity').live('blur', function() {
				var capacity = $('#storageCapacity').val();
				var avaiCap = isUpdate()
						? (currentStorage + superDomainStorageAvailableCapacity)
						: superDomainStorageAvailableCapacity;
				var $errorMsg = $('#storageCapacity')
						.siblings(errorBoxSelecter);
				var errorMsg = ''
				if (capacity == '') {
					errorMsg = '存储大小不能为空';
				} else if (!/^\d+$/.test(capacity)) {
					errorMsg = '存储大小必须为一个自然数';
				} else if (capacity > avaiCap) {
					errorMsg = '上级可用存储不够，可设最大存储为：' + avaiCap + 'G';
				} else if (capacity < usedStorage) {
					errorMsg = '不能小于当前已用存储：' + usedStorage + 'G';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			$('#backupStorageCapacity').live('blur', function() {
				var capacity = $('#backupStorageCapacity').val();
				var avaiCap = isUpdate()
						? (currentBackupStorage + superDomainBackupStorageAvailableCapacity)
						: superDomainBackupStorageAvailableCapacity;
				var $errorMsg = $('#backupStorageCapacity')
						.siblings(errorBoxSelecter);
				var errorMsg = ''
				if (capacity == '') {
					errorMsg = '存储大小不能为空';
				} else if (!/^\d+$/.test(capacity)) {
					errorMsg = '存储大小必须为一个自然数';
				} else if (capacity > avaiCap) {
					errorMsg = '上级可用存储不够，可设最大存储为：' + avaiCap + 'G';
				} else if (capacity < usedBackupStorage) {
					errorMsg = '不能小于当前已用存储：' + usedBackupStorage + 'G';
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			$('#cpuTotalNum').live('blur', function() {
				var cpuVal = $('#cpuTotalNum').val();
				var available = isUpdate()
						? (currentCpu + superDomainCpuAvailableCapacity)
						: superDomainCpuAvailableCapacity;
				var $errorMsg = $('#cpuTotalNum').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (cpuVal == '') {
					errorMsg = 'cpu大小不能为空';
				} else if (!/^\d+$/.test(cpuVal)) {
					errorMsg = 'cpu大小必须为一个自然数';
				} else if (cpuVal > available) {
					errorMsg = '该上级可用cpu核数不够，可设最大cpu核数为：' + available;
				} else if (cpuVal < usedCpu) {
					errorMsg = '不能小于当前已用cpu核数：' + usedCpu;
				}
				success = (errorMsg == '' && success);
				$errorMsg.html(errorMsg);
			});
			$('#memoryCapacity').live('blur', function() {
				var capacity = $('#memoryCapacity').val();
				var availabe = isUpdate()
						? (currentMemory + superDomainMemoryAvailableCapacity)
						: superDomainMemoryAvailableCapacity;
				var $errorMsg = $('#memoryCapacity').siblings(errorBoxSelecter);
				var errorMsg = ''
				if (capacity == '') {
					errorMsg = '内存大小不能为空';
				} else if (!/^\d+$/.test(capacity)) {
					errorMsg = '内存大小必须为一个自然数';
				} else if (capacity > availabe) {
					errorMsg = '该上级可用内存不够，当前最大可设内存为：' + availabe;
				} else if (capacity < usedMemory) {
					errorMsg = '不能小于当前已用内存：：' + usedMemory + 'M';
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
	if (kc == 9 || kc == 8 || kc == 46 || (kc >= 37 && kc <= 40)
			|| kc >= 48 && kc <= 57) {
		return true;
	}
	return false;
}