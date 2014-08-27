/**
 * 初始化备份虚机
 * 
 * @param {}
 *            id
 */
function initBackup2Storage(id, shutoff) {
	if (!shutoff) {
		var result = confirm("虚机为非关机状态,非关机状态下备份可能会造成备份不完整,是否继续？");
		if (result != true) {
			return;
		}
	}
	currentId = id;
	ck_pop_win({
				width : 400,
				title : backup2StorageTitle,
				url : backupHtml
			}, function() {
			});
}

/**
 * 还原系统
 * 
 * @param {}
 *            id
 */
function initRestore2Storage(id, shutoff) {
	currentId = id;
	ck_pop_win({
				width : 400,
				title : restore2StorageTitle,
				noButton : true,
				// scroll : true,
				url : restoreHtml
			}, initRestore2StorageWindow);
}

/**
 * 初始化还原备份虚机
 */
function initRestore2StorageWindow() {
	$.ajax({
				type : 'post',
				async : false,
				url : baseURL + "!initRestore2Storage.action",
				data : {
					"cloudContext.vo.id" : currentId
				},
				success : function(data) {
					var backups = data.cloudContext.params.vMBackups;
					addBackupVMToRestor(backups);
				}
			});
}
/**
 * 添加备份数据到页面
 * 
 * @param {}
 *            backups
 */
function addBackupVMToRestor(backups) {
	$('#vmBackupData').siblings('thead').remove();;
	if (!backups || backups.length == 0) {
		$('#vmBackupData').append("<tr><td  colspan='3'>当前虚拟机无备份记录</td></tr>");
		return;
	}
	for (var i in backups) {
		var backup = backups[i];
		var operateFlag = backup.operateFlag;
		var operateDesc = (operateFlag == null ? "正常" : operateFlag);
		var btn = "";
		if (operateFlag == null || operateFlag == backupNormal) {// 为空说明状态正常
			btn = "<a href='javascript:;' onclick='restoreVM2Storage("
					+ backup.id + ")'>还原</a>"
					+ "&nbsp;<a href='javascript:;' onclick='deleteBackupData("
					+ backup.id + ")'>删除</a>";
		}
		$('#vmBackupData').append("<tr><td>" + backup.addTime + "</td><td>"
				+ backup.desc + "</td><td>" + operateDesc + "</td><td>" + btn
				+ "</td></tr>");
	}
}
/**
 * 备份提交
 */
function backup2Storage() {
	$('#backupForm').attr('action', baseURL + '!backup2Storage.action');
	$('#backupVMId').val(currentId);
	$('#backupForm').submit();
}
/**
 * 还原虚机
 */
function restoreVM2Storage(backupId) {
	$('#restoreForm').attr('action', baseURL + '!restore2Storage.action');
	$('#restoreVMId').val(currentId);
	$('#backupId').val(backupId);
	$('#restoreForm').submit();
	ck_showProcessingImg();
}
/**
 * 删除备份记录
 * 
 * @param {}
 *            backupId
 */
function deleteBackupData(backupId) {
	location = baseURL
			+ "!deleteBackupVmData.action?cloudContext.params.backupId="
			+ backupId + "&cloudContext.vo.id=" + currentId;
	ck_showProcessingImg();
}

/**
 * 是否是备份虚机
 */
function isBackup2Storage() {
	return $('.ck_pop_win_title').html() == backup2StorageTitle;
}
/**
 * 是否是还原虚机
 * 
 * @return {}
 */
function isRestore2Storage() {
	return $('.ck_pop_win_title').html() == restore2StorageTitle;
}