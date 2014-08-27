<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.util.RRDUtil"%>
<%@page import="com.cloudking.cloudmanagerweb.util.StringUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.cloudking.cloudmanagerweb.util.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="refresh" content="60">
		<title>存储节点监控</title>
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
						存储情况
					</div>
					<div class="item_search">
						<form action="rrd/backupstorage_.jsp?id=${param.id }" method="post">
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
							备份存储使用情况
						</div>
						<div class="sub_item_body">
							<img alt="存储使用情况"
								src="rrdManager/rrd!getImg.action?cloudContext.params.equipmentType=backupStorage&cloudContext.params.dataFlag=<%=RRDUtil.DISK_USED_TYPE%>&cloudContext.vo.id=${param.id }&cloudContext.params.startDate=${param.startDate}&cloudContext.params.endDate=${param.endDate}">
						</div>
					</div>
				</div>
			</div>
		</div>



	</body>
</html>
