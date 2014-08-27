var moveVmTitle = "虚拟机迁移至:";
var moveVmHtml = 'virtualMachine/moveVm_.jsp';
/**
 * 初始化迁移窗口
 * 
 * @param {}
 *            id
 */
function initMoveVm(id) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : moveVmTitle,
				scroll : true,
				url : moveVmHtml
			}, initMoveVmData);
}

/**
 * 是否为迁移
 * 
 * @return {}
 */
function isMoveVm() {
	return $('.ck_pop_win_title').html() == moveVmTitle;
}
/**
 * 初始化迁移的资源数据
 */
function initMoveVmData() {
	$.ajax({
				url : baseURL + '!initMoveVm.action',
				data : {
					'cloudContext.vo.id' : currentId
				},
				success : addData2Table
			});
}

function addData2Table(data) {
	var computes = data.cloudContext.params.computeResources;
	if (!computes || computes.length == 0) {
		$('#computeResourceTb').append('<tr><td>无匹配计算节点</td></tr>');
		return;
	}
	for (var i in computes) {
		$('#computeResourceTb')
				.append('<tr><td><input type="radio" name="cloudContext.params.computeResourceId" value="'
						+ computes[i].id
						+ '"/>'
						+ computes[i].name
						+ '</td></tr>');
	}
}

function moveVm() {
	$('#moveVmForm').attr('action', baseURL + '!moveVm.action');
	$('#vmId').val(currentId);
	$('#moveVmForm').submit();
	ck_showProcessingImg();
}