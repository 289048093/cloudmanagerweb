<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>404错误</title> 
    <link rel="stylesheet" href="style/style.css" type="text/css" />
  </head>
  <body>
    <div class="ck_container">
    	<div class="ck_error_wrapper">
    		<div class="ck_error_header">
    			<div class="ck_error_title">
    				404 错误 !
    			</div>
    			<div class="ck_error_desc">
    			</div>
    			<hr>
    		</div>
    		<div class="ck_error_tips">
    			<ul>
    				<li>你所请求的地址不存在，请确认再试！</li>
    			</ul>
    		</div>
    	</div>
    </div>
  </body>
</html>
