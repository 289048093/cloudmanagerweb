$(document).ready(function() {
	renderSystemChartOnPageLoad();
	renderComputePoolChartOnPageLoad();
	renderStoragePoolChartOnPageLoad();
});

/**
 * 页面首次渲染时 - 展示系统资源图表
 * 
 * @param {}
 *            _this
 */
function renderSystemChartOnPageLoad(){
	// Cpu数据
	var cpuTotalNum = $("#cpuTotalNum").text();
	var cpuUsedNum = $("#cpuUsedNum").text();
	var cpuValue = 0;
	if (parseFloat(cpuTotalNum) > 0) {
		cpuValue = parseInt(Math.round(parseFloat(cpuUsedNum)/ parseFloat(cpuTotalNum) * 100));
	}
	renderChart("canvas_cpu","ie_chart_cpu",cpuValue);
	
	// 内存数据
	var memoryCapacity = $("#memoryCapacity").text();
	var memoryUsedCapacity = $("#memoryUsedCapacity").text();
	var memoryValue = 0;
	if (parseFloat(memoryCapacity) > 0) {
		memoryValue = parseInt(Math.round(parseFloat(memoryUsedCapacity)/ parseFloat(memoryCapacity) * 100));
	}
	renderChart("canvas_memory","ie_chart_memory",memoryValue);

	// 存储数据
	var storageCapacity = $("#storageCapacity").text();
	var usedStorageCapacity = $("#usedStorageCapacity").text();
	var storageValue = 0;
	if (parseFloat(storageCapacity) > 0) {
		storageValue = parseInt(Math.round(parseFloat(usedStorageCapacity)/ parseFloat(storageCapacity) * 100));
	}
	renderChart("canvas_storage","ie_chart_storage",storageValue);
}

/**
 * 页面首次渲染时 - 展示计算节点池图表
 * 
 * @param {}
 *            _this
 */
function renderComputePoolChartOnPageLoad(){
	
	var totalCpu = $("#PoolCpuTotalNum").text();
	var usedCpu = $("#PoolCpuUsedNum").text();
	var cpuValue = 0;
	if (parseFloat(totalCpu) > 0) {
		cpuValue = parseInt(Math.round(parseFloat(usedCpu)/ parseFloat(totalCpu) * 100));
	}
	renderChart("canvas_pool_cpu","ie_chart_pool_cpu",cpuValue);

	var totalMemory = $("#poolMemoryCapacity").text();
	var usedMemory = $("#poolMemoryUsedCapacity").text();
	var memoryValue = 0;
	if (parseFloat(totalMemory) > 0) {
		memoryValue = parseInt(Math.round(parseFloat(usedMemory)/ parseFloat(totalMemory) * 100));
	}
	renderChart("canvas_pool_memory","ie_chart_pool_memory",memoryValue);
}

/**
 * 页面首次渲染时 - 展示存储节点池图表
 * 
 * @param {}
 *            _this
 */
function renderStoragePoolChartOnPageLoad(){
	var poolStorageCapacity = $("#poolStorageCapacity").text();
	var poolUsedStorageCapacity = $("#poolUsedStorageCapacity").text();
	var storageValue = 0;
	if (parseFloat(poolStorageCapacity) > 0) {
		storageValue = parseInt(Math.round(parseFloat(poolUsedStorageCapacity)/ parseFloat(poolStorageCapacity) * 100));
	}
	renderChart("canvas_pool_stoarge","ie_chart_pool_stoarge",storageValue); 
}


/*
 * 存储池图表切换
 */
function queryStoragePoolChartOnPoolChange(_this) {
	var storageId = _this.value;
	$.ajax({
		type : 'post',
		async : true,
		url : "dashboardManager/dashboard!queryStoragePoolChartOnPoolChange.action",
		data : {
			"cloudContext.params.storageId" : storageId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				$("#poolStorageCapacity").text(data.cloudContext.params.poolStorageCapacity);
				$("#poolUsedStorageCapacity").text(data.cloudContext.params.poolUsedStorageCapacity);
				$("#poolAvailableStorageCapacity").text(data.cloudContext.params.poolAvailableStorageCapacity);
				
				var storageValue = 0;
				if (parseFloat(data.cloudContext.params.poolStorageCapacity) > 0) {
					storageValue = parseInt(Math.round(parseFloat(data.cloudContext.params.poolUsedStorageCapacity/ parseFloat(data.cloudContext.params.poolStorageCapacity) * 100)));
				}
				renderChart("canvas_pool_stoarge","ie_chart_pool_stoarge",storageValue);

			} else {
				var errorMsgList = data.cloudContext.errorMsgList;
				if (errorMsgList) {
					var errorMsg = "";
					for (var i = 0; i < errorMsgList.length; i++) {
						errorMsg += errorMsgList[i];
					}
					if (errorMsg != "") {
						alert(errorMsg);
					}
				}
			}

		}
	});
}

/*
 * 计算节点池图表切换
 */
function queryComputePoolChartOnPoolChange(_this) {
	var poolId = _this.value;
	$.ajax({
		type : 'post',
		async : true,
		url : "dashboardManager/dashboard!queryComputePoolChartOnPoolChange.action",
		data : {
			"cloudContext.params.poolId" : poolId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {

				$("#PoolCpuTotalNum").text(data.cloudContext.params.poolCpuTotalNum);
				$("#PoolCpuUsedNum").text(data.cloudContext.params.poolCpuUsedNum);
				$("#PoolCpuAvailableNum").text(data.cloudContext.params.poolCpuAvailableNum);
				var cpuValue = 0;
				if (parseFloat(data.cloudContext.params.poolCpuTotalNum) > 0) {
					cpuValue = parseInt(Math.round(parseFloat(data.cloudContext.params.poolCpuUsedNum/ parseFloat(data.cloudContext.params.poolCpuTotalNum) * 100)));
				}
				renderChart("canvas_pool_cpu","ie_chart_pool_cpu",cpuValue);

				$("#poolMemoryCapacity").text(data.cloudContext.params.poolMemoryCapacity);
				$("#poolMemoryUsedCapacity").text(data.cloudContext.params.poolMemoryUsedCapacity);
				$("#poolMemoryAvailableCapacity").text(data.cloudContext.params.poolMemoryAvailableCapacity);
				var memoryValue = 0;
				if (parseFloat(data.cloudContext.params.poolCpuTotalNum) > 0) {
					memoryValue = parseInt(Math.round(parseFloat(data.cloudContext.params.poolMemoryUsedCapacity/ parseFloat(data.cloudContext.params.poolMemoryCapacity) * 100)));
				}
				renderChart("canvas_pool_memory","ie_chart_pool_memory",memoryValue);

			} else {
				var errorMsgList = data.cloudContext.errorMsgList;
				if (errorMsgList) {
					var errorMsg = "";
					for (var i = 0; i < errorMsgList.length; i++) {
						errorMsg += errorMsgList[i];
					}
					if (errorMsg != "") {
						alert(errorMsg);
					}
				}
			}

		}
	});
}

/*
 * 系统资源域切换
 */
function querySystemChartOnDomainChange(_this) {
	var domainId = _this.value;
	if(!domainId){
		return;
	}
	$.ajax({
		type : 'post',
		async : true,
		url : "dashboardManager/dashboard!querySystemChartOnDomainChange.action",
		data : {
			"cloudContext.params.domainId" : domainId
		},
		success : function(data) {
			if (data.cloudContext.successIngoreWarn) {
				// Cpu数据
				$("#cpuTotalNum").text(data.cloudContext.params.cpuTotalNum);
				$("#cpuAvailableNum").text(data.cloudContext.params.cpuAvailableNum);
				$("#cpuUsedNum").text(data.cloudContext.params.cpuUsedNum);
				$("#maxAvailableCpuForApply").text(data.cloudContext.params.maxAvailableCpuForApply);
				var cpuTotalNum = data.cloudContext.params.cpuTotalNum;
				var cpuUsedNum = data.cloudContext.params.cpuUsedNum;
				var cpuValue = 0;
				if (parseFloat(cpuTotalNum) > 0) {
					cpuValue = parseInt(Math.round(parseFloat(cpuUsedNum)
							/ parseFloat(cpuTotalNum) * 100));
				}
				renderChart("canvas_cpu","ie_chart_cpu",cpuValue);
				
				// 内存数据
				$("#memoryCapacity").text(data.cloudContext.params.memoryCapacity);
				$("#memoryUsedCapacity").text(data.cloudContext.params.memoryUsedCapacity);
				$("#memoryAvailableCapacity").text(data.cloudContext.params.memoryAvailableCapacity);
				$("#maxAvailableMemoryForApply").text(data.cloudContext.params.maxAvailableMemoryForApply);
				var memoryCapacity = data.cloudContext.params.memoryCapacity;
				var memoryUsedCapacity = data.cloudContext.params.memoryUsedCapacity;
				var memoryValue = 0;
				if (parseFloat(memoryCapacity) > 0) {
					memoryValue = parseInt(Math
							.round(parseFloat(memoryUsedCapacity)
									/ parseFloat(memoryCapacity) * 100));
				}
				renderChart("canvas_memory","ie_chart_memory",memoryValue);

				// 存储数据
				$("#storageCapacity").text(data.cloudContext.params.storageCapacity);
				$("#usedStorageCapacity").text(data.cloudContext.params.usedStorageCapacity);
				$("#availableStorageCapacity").text(data.cloudContext.params.availableStorageCapacity);
				$("#maxAvailableStorageForApply").text(data.cloudContext.params.maxAvailableStorageForApply);
				var storageCapacity = data.cloudContext.params.storageCapacity;
				var usedStorageCapacity = data.cloudContext.params.usedStorageCapacity; 
				var storageValue = 0;
				if (parseFloat(storageCapacity) > 0) {
					storageValue = parseInt(Math
							.round(parseFloat(usedStorageCapacity)
									/ parseFloat(storageCapacity) * 100));
				}
				renderChart("canvas_storage","ie_chart_storage",storageValue);
			} else {
				var errorMsgList = data.cloudContext.errorMsgList;
				if (errorMsgList) {
					var errorMsg = "";
					for (var i = 0; i < errorMsgList.length; i++) {
						errorMsg += errorMsgList[i];
					}
					if (errorMsg != "") {
						alert(errorMsg);
					}
				}
			}

		}
	});
}

/**
 * 渲染图表
 * 
 * @param {}
 *            targetId 非ie图表Id
 * @param {}
 *            ieTargetId ie图表Id
 * @param {}
 *            value 图表数字
 */
function renderChart(targetId,ieTargetId, value) {
	// 检测ie
	if (browseDetector() != "msie") {
		// 仪表盘
		var imgArr = ["images/ck_dashboard_chart_bg.png",
				"images/ck_dashboard_chart_handler.png",
				"images/ck_dashboard_chart_ball.png"];
		// 生成仪表盘
		dashBoard(value, targetId, imgArr);
	} else {
		progressBar({
					width : 110,
					height : 120,
					value : value,
					renderTo : ieTargetId
				});
	}
}

/**
 * script :仪表盘
 * 
 * @param: index 指数（0-100）
 * @param: canvasId 画布id
 * @param: imgPath[bg,pointer,ball] 存放背景，指针，圆球的图片路径 history: 2012-3-16 使用方式 var
 *         imgArr = [ "images/ck_dashboard_chart_bg.png",
 *         "images/ck_dashboard_chart_handler.png",
 *         "images/ck_dashboard_chart_ball.png" ]; //生成仪表盘
 *         dashBoard(90,"canvas_cpu",imgArr);
 */
function dashBoard(index, canvasId, imgPath) {

	var c = document.getElementById(canvasId);
	var cxt = c.getContext("2d");
	var rotateAngel = 0;

	var sources = {
		"bg" : imgPath[0],
		"pointer" : imgPath[1],
		"ball" : imgPath[2]
	};
	loadImages(sources, function(img) {

				var movingNumber = 0; // 从零刻度开始移动
				var maxIndex = index + 10; // 指针穿越指定值后回滚效果
				var outOfReach = 0; // 判断是否已到达最高点
				// 指针移动效果
				var intervalId = setInterval(function() {
							if (outOfReach > 0) {
								movingNumber--;
							} else {
								movingNumber++;
							}
							if (outOfReach < 1
									? (maxIndex > movingNumber)
									: (maxIndex <= movingNumber)) {
								rotateAngel = indexToAngel(movingNumber);
								cxt.drawImage(img.bg, 0, 0, img.bg.width,
										img.bg.height); // 绘制背景
								// 指针图片
								cxt.beginPath();
								cxt.save();
								cxt.translate(c.width / 2, c.height / 2); // 定位坐标轴原点
								cxt.rotate(rotateAngel * Math.PI / 180); // 按指定角度旋转指针
								cxt.drawImage(img.pointer, -img.pointer.width
												/ 2, 0, img.pointer.width,
										img.pointer.height);
								cxt.fill();
								cxt.restore();
								cxt.closePath();

								// 绘制小球
								cxt.drawImage(img.ball, c.width / 2
												- img.ball.width / 2, c.height
												/ 2 - img.ball.height / 2,
										img.ball.width, img.ball.height);

								// 绘制文字
								cxt.fillStyle = "#fff";
								cxt.font = 'bold 12px verdana';
								var textPaddingLeft = index > 9 ? (index > 99
										? 20
										: 15) : 10;

								cxt.fillText(index + "%", c.width / 2
												- textPaddingLeft, c.height / 2
												+ 35);
							} else {
								if (outOfReach > 0) {
									clearInterval(intervalId);
								} else {
									outOfReach++;
									maxIndex -= 10;
								}

							}

						}, 20);

			});

	// 指数与角度换算器
	// 2.647是第个角度变化率，45是数值0时的角度
	function indexToAngel(data) {
		return parseInt(data * 2.647 + 45);

	}

	// 图片加载器
	function loadImages(sources, callback) {
		var images = {};
		var loadedImages = 0;
		var numImages = 0;
		for (var src in sources) {
			numImages++;
		}
		for (var src in sources) {
			images[src] = new Image();
			images[src].onload = function() {
				if (++loadedImages >= numImages) {
					callback(images);
				}
			};
			images[src].src = sources[src];
		}
	}
}

/**
 * script :基于css的进度条
 * 
 * @param: width *宽度
 * @param: height *高度
 * @param: colors 提供三种颜色,用以表述紧急情况,不填则为一种颜色,结构如
 *         colors["#71C671","#FFB90F","#EE5C42"],默认绿，黄，红,"#71C671","#FFB90F","#EE5C42"
 * @param: valueOnColorChange 颜色变化时的边界值,结构 valueOnColorChange[60,85,100]
 *         默认[60,85,100]
 * @param: borderColor 边框颜色,默认 #A1A1A1
 * @param: renderTo *渲染目标对象Id
 * @param: bgColor 背景颜色
 * @param: value *当前进度值,[0-100] history: 2012-3-16
 */
function progressBar(params) {
	var colors;
	var valueOnColorChange;
	var border;
	var bgColor;
	if (!params.bgColor) {
		bgColor = "#fff";
	}
	if (!params.colors) {
		colors = ["#71C671", "#FFB90F", "#EE5C42"];
	}
	if (!params.valueOnColorChange) {
		valueOnColorChange = [60, 85, 100];
	}
	if (!params.border) {
		border = "#A1A1A1";
	}
	var domHtml = "<div class='ck_progressBar_" + params.renderTo + "'>"
			+ "<div class='ck_progressBar_bg_" + params.renderTo + "'>"
			+ "<div class='ck_progressBar_empty_" + params.renderTo
			+ "'></div>" + "<div class='ck_progressBar_active_"
			+ params.renderTo + "' title='" + params.value + "'></div>"
			+ "</div>" + "</div>";
	$("#" + params.renderTo).html(domHtml);
	$(".ck_progressBar_bg_" + params.renderTo).css("width", params.width).css(
			"height", params.height).css("border", "1px solid " + border);

	// 动画
	var height = params.height * params.value / 100;
	var fakeValue = 0;
	var fakeHeight = 0;
	var suitColor; // 当前值应该的颜色
	var intervalid = setInterval(function() {
				if (fakeValue < params.value) {
					fakeValue++;
					fakeHeight = params.height * fakeValue / 100;
					// 查找当前值对应的配色方案
					for (var i = 0; i < 3; i++) {
						if (fakeValue <= valueOnColorChange[i]) {
							suitColor = colors[i];
							break;
						}
					}
					$(".ck_progressBar_active_" + params.renderTo).css("width",
							params.width).css("height", fakeHeight).css(
							"background", suitColor);
					$(".ck_progressBar_empty_" + params.renderTo).css("width",
							params.width).css("height",
							params.height - fakeHeight).css("background",
							bgColor);

				} else {
					// 查找当前值对应的配色方案
					for (var i = 0; i < 3; i++) {
						if (params.value <= valueOnColorChange[i]) {
							suitColor = colors[i];
							break;
						}
					}
					$(".ck_progressBar_active_" + params.renderTo).css("width",
							params.width).css("height", height).css(
							"background", suitColor);
					$(".ck_progressBar_empty_" + params.renderTo).css("width",
							params.width).css("height", params.height - height)
							.css("background", bgColor);
					clearInterval(intervalid);
					return;
				}

			}, 1);

}

/**
 * 去掉时间T
 * 
 * @param {}
 *            time
 */
function formatTime(time){
	if(!time){
		return "";
	}
	if(time.indexOf("T")>0){
		return time.replace(new RegExp("T","gm"), " ") 
	}
}

/**
 * 去掉时间T
 * 
 * @param {}
 *            time
 */
function subTime(time){
	if(!time){
		return "";
	}
	if(time.indexOf("T")>0){
		return time.substring(0,time.indexOf("T"));
	}
}

function emptyText(value){
	if(!value){
		return "";
	}
	return value;
}

/**
 * script: 浏览器检测 history: 2012-3-12 lianghr
 */
function browseDetector() {
	var OsObject = "";
	if (navigator.userAgent.indexOf("MSIE") > 0) {
		return "msie";
	}
	if (isFirefox = navigator.userAgent.indexOf("Firefox") > 0) {
		return "firefox";
	}
	if (isSafari = navigator.userAgent.indexOf("Safari") > 0) {
		return "safari";
	}
	if (isCamino = navigator.userAgent.indexOf("Camino") > 0) {
		return "camino";
	}
	if (isMozilla = navigator.userAgent.indexOf("Gecko/") > 0) {
		return "gecko";
	}

}