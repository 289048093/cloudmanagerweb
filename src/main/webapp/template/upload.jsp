<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.cloudking.cloudmanagerweb.PropertyManager"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
		<SCRIPT type="text/javascript">
			$(function(){
				// AJAX获取数据DOmain
				$.ajax({
						type : 'post',
						async : false,
						url : "template!initUpload.action",
						success : function(data) {
							// 域初始化数据
							var domains = data.cloudContext.params.domains;
							for (var i in domains) {
								$('#domain').append('<option value="'
										+ domains[i].code + '">'
										+ domains[i].name + '</option>');
							}
						}
					});
			});
		</SCRIPT>
	</head>
	<body>
		<table width="100%">
			<tr>
				<td align="center">
					<select id="domain">

					</select>
				</td>
			</tr>
			<tr>
				<td align="center">
					<jsp:plugin codebase="." archive="FileFtpApplet.jar"
						code="FTPUploadAppletUI.class" name="uploadFile" width="377"
						height="147" type="applet">
						<jsp:params>
							<jsp:param name="hostname"
								value="<%=PropertyManager.getInstance().getXMLProperty(
                                            PropertyManager.XML_CLOUDMANAGERWEB_FTP_HOSTNAME)%>" />
							<jsp:param name="port"
								value="<%=PropertyManager.getInstance().getXMLProperty(PropertyManager.XML_CLOUDMANAGERWEB_FTP_PORT)%>" />
							<jsp:param name="username"
								value="<%=PropertyManager.getInstance().getXMLProperty(
                                            PropertyManager.XML_CLOUDMANAGERWEB_FTP_USERNAME)%>" />
							<jsp:param name="password"
								value="<%=PropertyManager.getInstance().getXMLProperty(
                                            PropertyManager.XML_CLOUDMANAGERWEB_FTP_PASSWORD)%>" />
						</jsp:params>
						<jsp:fallback>您的浏览器不支持JAVA Applet，请更换浏览器，推荐用Chrome或Firefox</jsp:fallback>
					</jsp:plugin>
				</td>
			</tr>
		</table>
	</body>
</html>
