/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件模板类
 * 
 * @author CloudKing
 */
public final class MailTemplateUtil {

    /**
     * 默认构造方法
     */
    private MailTemplateUtil(){

    }

    /**
     * 同意删除虚拟机
     * @param email
     * @param applicantName
     * @param handlerName
     * @param handleMsg
     * @return
     */
    @SuppressWarnings("unused")
    public static Map<String, String> getApproveDelVirtualMachineMailTemplate(String email, String applicantName,
                    String handlerName, String handleMsg) {
        Map<String, String> emailInfoMap = new HashMap<String, String>();

        emailInfoMap.put("subject", Constant.PORTAL_VM_EMAIL_SUBJECT);
        emailInfoMap.put("email", email);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(applicantName);
        contentBuilder.append(",您好!\r\n");
        contentBuilder.append("您申请的虚拟机删除订单已经通过！\r\n");
        contentBuilder.append("审批备注：");
        contentBuilder.append(handleMsg + "\r\n");
        contentBuilder.append("审批人：" + handlerName);
        emailInfoMap.put("content", contentBuilder.toString());
        return emailInfoMap;
    }
    
    /**
     * 获得拒绝申请虚拟机的邮件信息
     * 
     * @return
     */
    @SuppressWarnings("unused")
    public static Map<String, String> getRejectMailTemplate(String email, String applicantName,String content,
                    String handlerName, String handleMsg) {
        Map<String, String> emailInfoMap = new HashMap<String, String>();

        emailInfoMap.put("subject", Constant.PORTAL_VM_EMAIL_SUBJECT);
        emailInfoMap.put("email",email);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(applicantName);
        contentBuilder.append(",您好!\r\n");
        contentBuilder.append(content + "\r\n");
        contentBuilder.append("拒绝理由：");
        contentBuilder.append(handleMsg + "\r\n");
        contentBuilder.append("审批人：" + handlerName);
        emailInfoMap.put("content", contentBuilder.toString());
        return emailInfoMap;
    }
    
    /**
     * 获得同意创建虚拟机的邮件信息
     * 
     * @return
     */
    @SuppressWarnings("unused")
    public static Map<String, String> getApproveMailTemplate(String email, String applicantName,int cpu,int memeory,int disk,String osName,String loginUserName,String loginPwd) {
        Map<String, String> emailInfoMap = new HashMap<String, String>();
        emailInfoMap.put("subject", Constant.PORTAL_VM_EMAIL_SUBJECT);
        emailInfoMap.put("email", email);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(applicantName);
        contentBuilder.append(",您好!\r\n");
        contentBuilder.append("您申请的虚拟机订单已经通过审核！\r\n");
        contentBuilder.append("配置如下：");
        contentBuilder.append("CPU:" + cpu + "核,");
        contentBuilder.append("内存：" + memeory + "M,");
        contentBuilder.append("硬盘：" + disk + "G,");
        contentBuilder.append("操作系统：" + osName);
        contentBuilder.append("\r\n登录用户名：" + loginUserName);
        contentBuilder.append("\r\n登录密码：" + loginPwd);
        emailInfoMap.put("content", contentBuilder.toString());
        return emailInfoMap;
    }

}
