<!doctype html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<html>
	<head>
		<base href="<%=basePath%>" />
		<title>登录</title>
		<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
		<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript" src="login.js"></script>
		<link rel="stylesheet" href="style/login.css" type="text/css" />
		<link rel="stylesheet" href="style/style.css" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" charset="utf-8">
			$(function(){
				var position = $(".ck_logo_img").position();
				$(".ck_login_form").css("left",position.left)
					.css("top",position.top+$(".ck_logo_img").height()+10);
					
				if('${cloudContext.errorMsgList}'!=''){
					ck_message({
						type:'error', //or error
						text:'${cloudContext.errorMsgList}',
						renderTo:'ck_footer'	
					});
				}
				
				$.ajax({
			        url : "userManager/user!queryDomainForLogin.action",  
			        type : "post",  
			        dataType : "json",
			        success : function(data){
			        	var domains = data.cloudContext.params.domains;
			        	var domainNameTmp="";
			        	var codeLen=0;
			        	for(var p in domains){
			        		codeLen=domains[p].code.length/2-1;
			        		domainNameTmp="";
			        		for(var i=0;i<codeLen;i++){
			        			domainNameTmp+="&nbsp;";
			        		}
			        		domainNameTmp+=domains[p].name;
			        		$(".ck_domain").append( "<option value='"+ domains[p].id + "' class='ck_option'>"+ domainNameTmp +"</option>");
			        	}
			        }  
			    }); 
			});
		</script>
	</head>
	<body>
		<div class="ck_container">

			<div class="ck_logo_wrapper">
				<div class="ck_logo_img"></div>
			</div>

			<div class="ck_body">
				<div class="ck_login_form">
					<div class="ck_login_form_title">
						云管理平台登录
					</div>
					<div class="ck_spliter_h"></div>

					<s:form theme="simple" action="userManager/user!login.action"
						method="post" id='loginForm'>
						<div class="ck_login_body">
							<div class="ck_input_label">
								用户名
							</div>
							<s:token></s:token>
							<s:textfield name="cloudContext.vo.username" id="username"></s:textfield>
							<div class="ck_input_label">
								密码
							</div>
							<s:password name="cloudContext.vo.password" id="password"></s:password>
							<div class="ck_input_label">
								验证码
							</div>
							<input type="text" name="cloudContext.params.checkCode"
								maxlength="4" id="checkCode">
							<img id="verifyCode" src="VerifyCode?Math.random()" />

						</div>
						<div class="ck_spliter_h"></div>
						<div class="ck_action">
							<input type="reset" class="ck_cancel" value="重置" />
							<input type="submit" id="ck_confirm" value="登录" />
						</div>
					</s:form>

				</div>
			</div>

			<div class="ck_footer" id="ck_footer">
				<div class="ck_copyright">
				</div>
			</div>
		</div>
	</body>
</html>