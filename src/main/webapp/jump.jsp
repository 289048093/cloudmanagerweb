<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.apache.struts2.views.jsp.PropertyTag"%>
<%@page import="org.apache.struts2.views.jsp.TagUtils"%>
<%@page import="java.util.List"%>
<%@page import="com.cloudking.cloudmanagerweb.util.Constant"%>
<%@page import="com.cloudking.cloudmanagerweb.LoginedUser"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
	<head>
	<script type="text/javascript">
			var tipMsg="";
			var rightsUrls="";
			<%
				//提示信息
				List<String> successMsgList=(List<String>) TagUtils.getStack(pageContext).findValue("#request.cloudContext.successMsgList");
				List<String> warnMsgList=(List<String>) TagUtils.getStack(pageContext).findValue("#request.cloudContext.warnMsgList");
				List<String> errorMsgList=(List<String>) TagUtils.getStack(pageContext).findValue("#request.cloudContext.errorMsgList");
				if(successMsgList!=null && successMsgList.size()>0){
					for(int i=0;i<successMsgList.size();i++){
						out.println(String.format("tipMsg+=\"%1$s\"\n",successMsgList.get(i).replaceAll("\"","'")));
					}
				}
				if(warnMsgList!=null && warnMsgList.size()>0){
					for(int i=0;i<warnMsgList.size();i++){
						out.println(String.format("tipMsg+=\"%1$s\"\n",warnMsgList.get(i).replaceAll("\"","'")));
					}
				}
				if(errorMsgList!=null && errorMsgList.size()>0){
					for(int i=0;i<errorMsgList.size();i++){
						out.println(String.format("tipMsg+=\"%1$s\"\n",errorMsgList.get(i).replaceAll("\"","'")));
					}
				}
				//权限
				LoginedUser loginedUser=(LoginedUser)session.getAttribute(Constant.LOGINED_USER);
				if(loginedUser!=null){
					out.println(String.format("rightsUrls='%1$s';",loginedUser.getRightsUrls())); 
				}
			%>
			if(""!=tipMsg){
				alert(tipMsg);
				var url = location.toString();	
				if(url.lastIndexOf('!query.action')==-1){
					location = url.replace(/!.+\.action/,'!query.action');
				}
			}
		</script>
	</head>
	<body>
	</body>
</html>