var editSyscfgWinTitle = '全局设置'
var recoverDefaultWinTitle = '恢复默认值'
var addOrUpdateHtml = 'syscfg/addOrUpdateWindow_.jsp';
var baseURL = 'syscfgManager/syscfg';
// 当前操作的ID
var currentId = 0;
var errorMSG = '';
var propertyRegex = null
var type = null;
$(function() {
			/*
			 * 点击弹出窗口的确定按钮操作
			 */
			$('.ck_confirm').live('click', function() {
						var title = $('.ck_pop_win_title').html();
						if (!validate()) {
							return;
						}
						updateSyscfg();
					});
		});
/**
 * 初始化更新
 */
function initUpdate(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : editSyscfgWinTitle,
				url : addOrUpdateHtml
			}, initAddOrUpdateWindow);
}

/**
 * 初始化更新窗体
 */
function initAddOrUpdateWindow() {
	// 修改
	// 名字不能改
	$('#displayKey').parents('tr').hide();
	$('#desc').parents('tr').hide();
	var url = baseURL + '!initAddOrUpdate.action?cloudContext.vo.id='
			+ currentId + '&cloudContext.params.updateFlag=true';
	// AJAX获取数据
	$.ajax({
		type : 'post',
		async : false,
		url : url,
		success : function(data) {
			// 更新存放值
			var dataVo = data.cloudContext.params.dataVo;
			$('#id').val(dataVo.id);
			$('#displayKey').val(dataVo.key);
			// $('#hiddenKey').val(dataVo.key);
			if (dataVo.key == pwdKey) {
				$('#value').val(dataVo.value.replace(/./g, '*'));
			} else {
				$('#value').val(dataVo.value);
			}
			$('#desc').val(dataVo.desc);
			propertyRegex = new RegExp(dataVo.regex);
			type = dataVo.type;
			errorMSG = dataVo.errorMSG;

			if (type == 'Boolean') {
				var $td = $('#value').parent();
				$td.prev().html(data.cloudContext.params.dataVo.desc);
				$td.empty();
				var yChecked = dataVo.value == "true"
						? 'checked="checked"'
						: '';
				var nChecked = dataVo.value == "false"
						? 'checked="checked"'
						: '';;
				$td
						.html('<input type="radio" name="cloudContext.vo.value" value="true" '
								+ yChecked
								+ '/>是&nbsp;'
								+ '<input type="radio" name="cloudContext.vo.value" value="false" '
								+ nChecked + '/>否');
			}
		}
	});
}

/**
 * 编辑模版操作
 */
function updateSyscfg() {
	var form = $('#addOrUpdateForm');
	form.attr('action', baseURL + '!update.action');
	form.submit();
	ck_showProcessingImg();
}
/**
 * 恢复默认
 * 
 * @param {}
 *            id
 */
function recoverDefault(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : recoverDefaultWinTitle,
				content : "是否要恢复默认值"
			});
}
/**
 * 表单验证
 */
$('#value').live('blur', validate);

function validate() {
	if (type == 'Boolean') {
		return true;
	}
	var val = $('#value').val();
	var errorBoxSelecter = '.errorMsg';
	var $errorMsg = $('#value').siblings(errorBoxSelecter);
	if (val == '') {
		$errorMsg.html('属性值不能为空');
		return false;
	}
	if (propertyRegex != null) {
		if (!propertyRegex.test(val)) {
			$errorMsg.html(errorMSG);
			return false;
		}
	} else {
		$errorMsg.html('未能获取服务器参数');
		return false;
	}
	$errorMsg.html('');
	return true;
}