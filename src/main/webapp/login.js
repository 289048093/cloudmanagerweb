
$(function(){
	/**
	 * 获取验证码
	 */
	$('#verifyCode').live("click",function(){
		this.src='VerifyCode?'+Math.random();
	});
	
	/**
	 * 聚焦
	 */
	$('#username').focus();
});