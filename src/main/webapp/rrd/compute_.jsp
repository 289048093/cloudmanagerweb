<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.util.RRDUtil"%>
<%@page import="com.cloudking.cloudmanagerweb.util.DateUtil"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="refresh" content="60">
		<title>计算节点监控</title>
		<script language="JavaScript" type="text/javascript"
			src="js/My97DatePicker/WdatePicker.js"></script>
		<%
		    String startDate = request.getParameter("startDate");
		    String endDate = request.getParameter("endDate");
		    if (StringUtil.isBlank(startDate)) {
		        Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.HOUR, -4);
		        startDate = DateUtil.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
		    }
		    if (StringUtil.isBlank(endDate)) {
		        endDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		    }
		%>
	</head>

	<body>
		<div class="ck_container" style="width:100%;min-height:660px;">
			<div class="ck_align_center">
				<div class="item_wrapper">
					<div class="item_title">
						CPU情况
					</div>
					<div class="item_search">
						<form action="rrd/compute_.jsp?id=${param.id }" method="post">
							时间范围：
							<input type="text" class="Wdate" id="d4331" name="startDate"
								value="<%=startDate%>"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'d4332\')}'})" />
							<input type="text" class="Wdate" id="d4332" name="endDate"
								value="<%=endDate%>"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'d4331\');}',maxDate:'%y-%M-%d'})" />
							<input type="submit" value="提交">
						</form>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							CPU的用户情况
						</div>
						<div class="sub_item_body">
							<img alt="CPU的用户情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.CPU_USER_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							CPU的系统情况
						</div>
						<div class="sub_item_body">
							<img alt="CPU的系统情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.CPU_SYSTEM_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							CPU的IO情况
						</div>
						<div class="sub_item_body">
							<img alt="CPU的IO情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.CPU_IOWAIT_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							CPU的空闲情况
						</div>
						<div class="sub_item_body">
							<img alt="CPU的空闲情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.CPU_IDLE_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
				</div>


				<div class="item_wrapper">
					<div class="item_title">
						内存情况
					</div>
					<div class="item_search">
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							内存剩余情况
						</div>
						<div class="sub_item_body">
							<img alt="内存剩余情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.MEM_FREE_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							内存使用情况
						</div>
						<div class="sub_item_body">
							<img alt="内存使用情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.MEM_USED_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
					<div class="sub_item_wrapper">
						<div class="sub_item_title">
							内存总体情况
						</div>
						<div class="sub_item_body">
							<img alt="内存总体情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=compute&cloudContext.params.dataFlag=<%=RRDUtil.MEM_TOTAL_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
				</div>

			</div>
		</div>

	</body>
</html>
