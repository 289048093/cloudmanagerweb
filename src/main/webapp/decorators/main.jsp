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
<decorator:usePage id="decoratedPage" />
<html>
	<head>
		<title><decorator:title default="云景云平台" /></title>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="shortcut icon" href="images/favicon.ico"
			type="image/x-icon" />
		<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/jquery.lightbox_me.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<!-- 样式 -->
		<link rel="stylesheet" href="style/dataTable/demo_page.css"
			type="text/css" />
		<link rel="stylesheet" href="style/dataTable/demo_table_jui.css"
			type="text/css" />
		<link rel="stylesheet"
			href="style/dataTable/jquery-ui-1.8.4.custom.css" type="text/css" />
		<link rel="stylesheet" href="style/style.css" type="text/css" />
		<decorator:head />
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
			}
		</script>
		<s:if test="#request.cloudContext.successMsgList.size()>0">
		</s:if>
	</head>
	<body>
		<s:set name="menus" value="#session.userLogin.menus" />
		<div class="ck_container">
			<div class="ck_header_wrapper">
				<div class="ck_logo">
					<img src="images/ck_logo.png" alt="cloud manager"
						title="cloud manager" />
				</div>
				<div class="ck_userinfo_wrapper">
					<div class="ck_personal_info_wrapper">
						你好,&nbsp;
						<span class="ck_userinfo"> <s:hidden name="ck_userid"
								value="%{#session.userLogin.id}" /> <s:property
								value="#session.userLogin.realname" /> </span>!&nbsp;&nbsp;
						<div>
						</div>
					</div>
					<div class="ck_edit_self_wrapper">
						<span class="ck_edit_self_info" title="编辑信息"></span>
					</div>
					<div class="ck_logout_wrapper">
						<a href="userManager/user!logout.action" title="注销"
							class="ck_logout"></a>
					</div>
				</div>
			</div>

			<div class="ck_section_wrapper">
				<!-- menu begin -->
				<div class="ck_menu_wrapper">

					<s:iterator value="#menus" var="menu">
						<%
						    //获取当次循环菜单编号,判断菜单是否高亮
						        String currentCode = TagUtils.getStack(pageContext).findString("#menu.code");
						        String code = decoratedPage.getProperty("meta.code") == "" ? "" : decoratedPage
						                        .getProperty("meta.code");
						%>

						<s:if test="#menu.code.length()<4">
							<!--首页-->
							<s:if test="#menu.url.length()>5">
								<div
									onclick="javascript:location.href='<%=basePath%><s:property value="#menu.url"/>'"
									class="<%=currentCode.equals(code) ? "ck_active_menu" : "ck_menu_home"%>">
									<div class="ck_menu_icon">
										<img src="<s:property value="#menu.img"/>" />
									</div>
									<div class="ck_menu_text">
										<s:property value="#menu.name" />
									</div>
									<div class="ck_menu_spliter"></div>
								</div>
							</s:if>
							<!-- 如果是非首页的普通分类 -->
							<s:else>
								<div class="ck_menu_header"
									code="<s:property value="#menu.code" />">
									<div class="ck_menu_header_text">
										<s:property value="#menu.name" />
									</div>
								</div>
							</s:else>
						</s:if>
						<s:else>
							<div
								onclick="javascript:location.href='<%=basePath%><s:property value="#menu.url"/>'"
								code="<s:property value="#menu.code" />"
								class="<%=currentCode.equals(code) ? "ck_active_menu" : "ck_menu_item"%>">
								<div class="ck_menu_icon">
									<img src="<s:property value="#menu.img"/>" />
								</div>
								<div class="ck_menu_text">
									<s:property value="#menu.name" />
								</div>
								<div class="ck_menu_spliter"></div>
							</div>
						</s:else>
					</s:iterator>
				</div>
				<!--startprint-->
				<decorator:body />
				<!--endprint-->
				<div class="ck_clear"></div>
			</div>
		</div>
		<div class="ck_clear"></div>
		<div class="ck_footer_wrapper">
			<div class="ck_footer_text">
				版权所有 © 2012 深圳市云景科技有限公司. 保留所有权利. 深圳市云景科技有限公司 粤ICP备11012869号
			</div>
		</div>
	</body>
</html>
