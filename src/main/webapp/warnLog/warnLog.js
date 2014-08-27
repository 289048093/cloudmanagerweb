var baseURL = "warnLogManager/warnLog";
$(function() {

		});
function exportData() {
	location = baseURL + "!exportData.action?cloudContext.params.qName="
			+ $('#qName').val() + "&cloudContext.params.qEquipmentIdentity="
			+ $('#qEquipmentIdentity').val()
			+ "&cloudContext.params.startDate=" + $('#d4331').val()
			+ "&cloudContext.params.endDate=" + $('#d4332').val();
}
/**
 * 根据机房查找机架，级联操作
 */
function queryRackByRoom(selectID, roomId, msg) {
	$("#" + selectID).empty();
	$("#" + selectID).append("<option value=''>--请选择--</option>");
	if (roomId == "" || roomId == null) {
		return;
	}
	// AJAX获取数据
	$.ajax({
				type : 'post',
				async : false,
				url : baseURL
						+ "!queryRackByRoom.action?cloudContext.params.roomId="
						+ roomId,
				success : function(data) {
					// 配置
					var machineRacks = data.cloudContext.params.racks;
					if (machineRacks.length == 0 && msg != "") {
						alert(msg);
						return;
					}

					for (var i = 0; i < machineRacks.length; i++) {
						$("#" + selectID).append("<option value='"
								+ machineRacks[i].id + "'>"
								+ machineRacks[i].name + "</option>");
					}
				}
			});
}
/**
 * 机柜改变
 * 
 * @param {}
 *            rackId
 */
function changeRack(rackId) {
	
}
/**
 * 设备类型改变
 * 
 * @param {}
 *            type
 */
function changeType(type) {
	if (!$('#qRack').val()) {
		return;
	}
}
