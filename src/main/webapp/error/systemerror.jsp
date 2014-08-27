<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>系统错误</title>
<link rel="stylesheet" href="style/style.css" type="text/css" />
</head>
<body>
	<div class="ck_container">
		<div class="ck_error_wrapper">
			<div class="ck_error_header">
				<div class="ck_error_title">系统错误 !</div>
				<div class="ck_error_desc"></div>
				<hr>
			</div>
			<div class="ck_error_tips">
				<ul>
					<li>
						<%
						    String referer = basePath
						                    + "/dashboardManager/dashboard!query.action";
						    out.println(String.format("返回首页<a href='%1$s'>点击此处</a>", referer));
						%>
					</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>
