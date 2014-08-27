<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>错误</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
	</head>
	<body>
		<div class="ck_container">
			<div class="ck_error_wrapper">
				<div class="ck_error_header">
					<div class="ck_error_title">
						<s:if test="cloudContext.errorMsgList.size()>0">
							<s:iterator value="cloudContext.errorMsgList" var="item"
								status="st">
								${item }
							</s:iterator>
						</s:if>
					</div>
					<div class="ck_error_desc">
					</div>
					<hr>
				</div>
				<div class="ck_error_tips">
					<ul>
						<li>
							<%
							    String referer = request.getHeader("Referer");
							    out.println(String.format("返回界面<a href='%1$s'>点击此处</a>", referer));
							%>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>
