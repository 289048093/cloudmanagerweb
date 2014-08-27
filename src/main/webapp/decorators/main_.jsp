<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
	<head>
		<title><decorator:title default="云景云平台" />
		</title>
		<base href="<%=basePath%>">
		<link rel="stylesheet" href="style/style.css" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<decorator:head />
	</head>
	<body>
		<decorator:body />
	</body>
</html>
