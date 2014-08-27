// 项目业务代码开始*************************

// 打印
function printPage() {
	// 隐藏不需要打印的按钮
	$(".printWrapper").hide();
	$(".ck_search_toggle").hide();
	$(".ck_search").hide();
	$(".ck_content_header_btn_wrapper").hide();
	bdhtml = window.document.body.innerHTML;
	// 备份页面
	var htmlBak = bdhtml;
	sprnstr = "<!--startprint-->";
	eprnstr = "<!--endprint-->";
	prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + 17);
	prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
	window.document.body.innerHTML = prnhtml;
	window.print();
	// 还原页面
	window.document.body.innerHTML = htmlBak;
	$(".printWrapper").show();
	$(".ck_search_toggle").show();
	$(".ck_search").show();
	$(".ck_content_header_btn_wrapper").show();
}

// 搜索条伸缩
$(".ck_search_toggle").live("click", function() {
			$(this).parent().find(".ck_search_context_wrapper").toggle("slow");
		});

// 菜单伸缩
$(".ck_menu_header").live("click", function() {
			var code = $(this).attr("code");
			console.log("code is :" + code);
			$(".ck_menu_item").each(function() {
						var tmpCode = $(this).attr("code");
						if (tmpCode.indexOf(code) == 0) {
							$(this).toggle("slow");
						}
					});
			$(".ck_active_menu").each(function() {
						var tmpCode = $(this).attr("code");
						if (tmpCode.indexOf(code) == 0) {
							$(this).toggle("slow");
						}
					});
		});

$(document).ready(function() {
			// 显示有权限的按钮
			$("[chkRightsUrl]").each(function(i) {
						if (rightsUrls.indexOf($(this).attr("chkRightsUrl")) != -1) {
							$(this).css("display", "block");
						} else {
							$(this).css("display", "none");
						}
					});
			// 表格操作图标剧中
			// $(".ck_table_operation").each(function() {
			// var tb_width = 0;
			// $(this).children().each(function() {
			// var style = $(this).attr("style");
			// if (style) {
			// if (style.indexOf("block") > 0) {
			// tb_width += $(this).width();
			// }
			// }
			// });
			// $(this).css("width", tb_width);
			// });

			// 编辑个人信息
			$(".ck_edit_self_wrapper").live("click", function() {
						ck_pop_win({
									width : 400,
									title : "编辑用户",
									url : "user/updateBySelf_.jsp",
									submitId : "ck_update_user_info"
								}, tackleUserInfoManagement);
					});
		});

/**
 * 处理用户信息管理
 */
function tackleUserInfoManagement() {
	// 初始化数据
	$.ajax({
				type : 'post',
				async : false,
				url : 'userManager/user!initAddOrUpdate.action',
				data : {
					"cloudContext.vo.id" : $("#ck_userid").val(),
					"cloudContext.params.updateFlag" : true
				},
				success : function(data) {
					// 更新存放值
					var dataVo = data.cloudContext.params.dataVo;
					$('#id').val(dataVo.id);
					$('#username').val(dataVo.username);
					$('#realname').val(dataVo.realname);
					$('#email').val(dataVo.email);
					$('#cellPhone').val(dataVo.cellPhone);
					$('#telPhone').val(dataVo.telPhone);
					$('#username').attr("disabled", true);
					$('#realname').attr("disabled", true);
				}
			});
	// 监听更新
	$("#ck_update_user_info").live("click", function() {
				if (!validate()) {
					return;
				}
				var form = $('#selfInfoForm');
				form.attr('action', 'userManager/user!updateSelf.action');
				form.submit();
			});
}
// 项目业务代码结束*************************

/**
 * 弹出窗口
 * 
 * @params title 窗口标题
 * @params sub_title 窗口副标题
 * @params url 内容页面url地址(template/template.html)
 * @params confirm 确定按钮文字
 * @params cancel 取消按钮文字
 * @params width 窗口宽度
 * @params content 窗口文字内容，直接展示文字，不加载页面
 * @params callback 回调函数，用以填充数据
 * @params submitId 提交按钮Id
 * 
 * 使用方法： ck_pop_win( { width:400, title:'编辑模板', url:'template/template.html' },
 * function(){} );
 */
function ck_pop_win(params, callback) {

	var title = ck_not_empty(params.title) ? params.title : '新窗口标题';
	var sub_title = ck_not_empty(params.sub_title) ? params.sub_title : '';
	var url = ck_not_empty(params.url) ? params.url : '新窗口内容';
	var confirm = ck_not_empty(params.confirm) ? params.confirm : '确定';
	var cancel = ck_not_empty(params.cancel) ? params.cancel : '取消';
	var width = ck_not_empty(params.width) ? params.width : 400;
	var content = ck_not_empty(params.content) ? params.content : '无';
	var scroll = ck_not_empty(params.scroll) ? params.scroll : false;

	$("#ck_pop_win").remove();

	// 自定义提交按钮id
	var confirmHtml = "";
	var cancelHtml = "";
	if (!params.noButton) {
		if (params.submitId) {
			confirmHtml = '<div id="' + params.submitId + '">' + confirm
					+ '</div>';
		} else {
			confirmHtml = '<a  class="ck_confirm">' + confirm + '</a>';
		}
		cancelHtml = "<div class='ck_cancel'>" + cancel + "</div>"
				+ confirmHtml;
	}
	var domHtml = '<div id="ck_pop_win">'
			+ '<div class="ck_pop_header_wrapper">'
			+ '<div class="ck_pop_header_title">'
			+ '<h3 class="ck_pop_win_title" >' + title + '</h3>'
			+ '<span class="ck_pop_win_sub_title">' + sub_title + '</span>'
			+ '</div>' + '<div class="ck_pop_header_close" ></div>'
			+ '<div class="ck_pop_spliter_h"></div>' + '</div>'
			+ '<div class="ck_pop_win_body">'
			+ ' <div id="ck_pop_win_form"></div>' + '</div>'
			+ '<div class="ck_pop_win_footer">'
			+ '<div class="ck_pop_spliter_h"></div>'
			+ '<div id="ck_pop_win_action">' + cancelHtml + '</div>'
			+ '</div> ';
	$("body").append(domHtml);
	if (params.submitId) {
		$("#" + params.submitId).addClass("ck_confirm_btn");
	}
	$("#ck_pop_win").css("width", width);

	if (ck_not_empty(params.content)) {
		$("#ck_pop_win_form").html(content);
		$('#ck_pop_win').lightbox_me({
					centered : true,
					onLoad : function() {
						$('.ck_pop_win_form').find('input:first').focus();
					}
				});
		if (callback) {
			callback();
		}
	} else {
		$("#ck_pop_win_form").load(url + '?t=' + new Date().getTime(),
				function() {
					$('#ck_pop_win').lightbox_me({
								centered : true,
								onLoad : function() {
									$('.ck_pop_win_form').find('input:first')
											.focus();
								}
							});
					if (callback) {
						callback();
					}

				});
	}
	if (scroll) {
		$("#ck_pop_win_form").addClass('ck_pop_win_form');
	}

	$(".ck_cancel").live("click", function() {
				$(".lb_overlay").remove();
				$("#ck_pop_win").remove();
			});
	$(".ck_pop_header_close").live("click", function() {
				$(".lb_overlay").remove();
				$("#ck_pop_win").remove();
			});
}

/**
 * 移除弹出层
 */
function ck_remove_win() {
	$(".lb_overlay").remove();
	$("#ck_pop_win").remove();
}

/**
 * 业务成功提交后的处理中图片显示
 */
function ck_showProcessingImg() {
	$("#ck_pop_win_form")
			.empty()
			.append("<img src='images/loading_media.gif' alt='处理中，请稍候' title='处理中，请稍候' width='32' height='32'/>&nbsp;&nbsp;&nbsp;&nbsp;处理中，请稍候...");
	$("#ck_pop_win_action").empty();
}

/*
 * 警告或错误提示层 用法 ck_message({ type:'warn', //or error text:'凭证错误',
 * renderTo:'ck_footer',timeOut:2000});
 */
function ck_message(params) {
	var type = ck_not_empty(params.type) ? params.type : 'warn';
	var text = ck_not_empty(params.text) ? params.text : '警告';

	$(".ck_message").remove();
	var domHtml = '<div class="ck_message">' + '<div class="ck_message_icon">'
			+ '<img src="./images/ck_message_' + type + '.png"/>' + '</div>'
			+ '<div class="ck_message_text">' + text + '</div>' + '</div>';
	if (ck_not_empty(params.renderTo)) {
		$("#" + params.renderTo + "").prepend(domHtml);
	} else {
		$("body").append(domHtml);
	}

	if (type == 'warn') {
		$(".ck_message").css("background", "#FC3").toggle().animate({
					height : 'toggle',
					opacity : 'toggle'
				}, "slow");
	} else {
		$(".ck_message").css("background", "#FFA07A").toggle().animate({
					height : 'toggle',
					opacity : 'toggle'
				}, "slow");
	}

	if (params.timeOut) {
		setTimeout(function() {
					$(".ck_message").animate({
								height : 'toggle',
								opacity : 'toggle'
							}, "slow");
				}, params.timeOut)
	}
}

/*
 * 非空校验
 */
function ck_not_empty(value) {
	var flag = false;
	if (value != null && value != 'undefined') {
		flag = true;
	}
	return flag;
}

/**
 * 验证是否支持HTML5
 */
function supportHtml5() {
	return typeof(Worker) !== "undefined";
}

/**
 * 描述输入框限制 keyCode(tab[9], .[190], 0-9[48-57], backspace[8], delete[46],
 * 方向[37-40])
 * 
 * @param {}
 *            event
 * @return {Boolean}
 */
function descKeydown(event, $desc) {// 数字输入框限制
	var kc = event.keyCode;
	if ($desc.val().length > 250) {
		if (kc && kc == 8 || kc == 46 || (kc >= 37 && kc <= 40)) {
			return ture;
		}
		$desc.blur();
		return false;
	}
	$desc.siblings(errorBoxSelecter).html('');
	return true;
}