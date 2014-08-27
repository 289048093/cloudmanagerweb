/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cloudking.cloudmanagerweb.util.ContextUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;

/**
 * spring加载之前的监听器
 * 
 * @author CloudKing
 * 
 */
public class PreSpringListener implements ServletContextListener {
    /**
     * 上下文销毁
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * 上下文初始化
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            ContextUtil.setWebRoot(event.getServletContext().getRealPath("/"));
            PropertyManager.getInstance().initXMLConfig();
            createDirs();
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 创建目录
     */
    private void createDirs() {
        File homeDir = ProjectUtil.getHomeDir();
        if (!homeDir.exists()) {
            homeDir.mkdirs();
        }
        File templateDir = ProjectUtil.getTemplateDir();
        if (!templateDir.exists()) {
            templateDir.mkdirs();
        }
        File tmpDir = ProjectUtil.getTmpDir();
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File dbBackDIr = ProjectUtil.getDBBackUPDir();
        if (!dbBackDIr.exists()) {
            dbBackDIr.mkdirs();
        }
        File rrddir = ProjectUtil.getRRDDir();
        if (!rrddir.exists()) {
            rrddir.mkdirs();
        }
        File rootDomainTemplateDir = new File(templateDir, "00");
        if (!rootDomainTemplateDir.exists()) {
            rootDomainTemplateDir.mkdirs();
        }
        File workOrderAttachmentDir = new File(homeDir, "attachment");
        if (!workOrderAttachmentDir.exists()) {
            workOrderAttachmentDir.mkdirs();
        }
    }
}
