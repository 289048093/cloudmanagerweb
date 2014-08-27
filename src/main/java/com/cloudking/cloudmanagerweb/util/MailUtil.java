/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cloudking.cloudmanagerweb.PropertyManager;

/**
 * 邮件工具
 * 
 * @author CloudKing
 */
public final class MailUtil {

    /**
     * 默认构造方法
     */
    private MailUtil(){

    }

    /**
     * 发送邮件
     * 
     */

    public static void sendMail(String subject, String content, String email) {
        //先判断是否开启发邮件功能，如果未开启，直接退出。
        Boolean enable = Boolean.parseBoolean(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_EMAIL_ENABLE));
        if (!enable) {
            return;
        }
        //邮件服务器发送代码。
        try {
            JavaMailSenderImpl javaMailSenderImpl = getMailSender();
            MimeMessage msg = javaMailSenderImpl.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(msg, false, "utf-8");
            String from = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_FROM);
            if (StringUtil.isBlank(from)) {
                throw new MessagingException("发送地址不能为空");
            }
            if (StringUtil.isBlank(email)) {
                throw new MessagingException("收信地址不能为空");
            }
            if (StringUtil.isBlank(subject)) {
                throw new MessagingException("主题不能为空");
            }
            if (StringUtil.isBlank(content)) {
                throw new MessagingException("内容不能为空");
            }
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, false);
            javaMailSenderImpl.send(msg);
        } catch (IllegalArgumentException e) {
            LogUtil.warn(e.getMessage());
        } catch (MessagingException e) {
            LogUtil.warn(e.getMessage());
        } catch (Exception e) {
            LogUtil.warn(e.getMessage());
        }

    }

    /**
     * 获取邮件发送器
     * 
     * 发送邮件错误
     * 
     * @return
     * @throws MessagingException
     *             邮件发送异常
     */
    private static JavaMailSenderImpl getMailSender() throws MessagingException {
        String password = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_PASSWORD);
        String username = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_USERNAME);
        String host = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_HOST);
        String port = PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_PORT);
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        if (StringUtil.isBlank(password)) {
            throw new MessagingException("邮件密码为空");
        }
        if (StringUtil.isBlank(username)) {
            throw new MessagingException("邮件用户名为空");
        }
        if (StringUtil.isBlank(host)) {
            throw new MessagingException("主机为空");
        }
        if (StringUtil.isBlank(port)) {
            throw new MessagingException("端口为空");
        }
        JavaMailSenderImpl javaMailSenderImpl = (JavaMailSenderImpl) ProjectUtil.getSpringBean("mailSender");
        javaMailSenderImpl.setPassword(PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_PASSWORD));
        javaMailSenderImpl.setUsername(PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_USERNAME));
        javaMailSenderImpl.setHost(PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_HOST));
        javaMailSenderImpl.setPort(Integer.parseInt(PropertyManager.getInstance().getDbProperty(
                        PropertyManager.DB_EMAIL_PORT)));
        javaMailSenderImpl.setJavaMailProperties(javaMailProperties);
        return javaMailSenderImpl;
    }
}
