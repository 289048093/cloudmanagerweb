/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import com.cloudking.cloudmanagerweb.jetty.ContextUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.jetty.PropertyManager;

/**
 * 启动之后的监听器
 * 
 * @author CloudKing
 * 
 */
public class JettyListener implements ServletContextListener {
    /**
     * jetty server
     */
    private static Server server;

    /**
     * 上下文销毁
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * 上下文初始化
     */
    public void contextInitialized(ServletContextEvent event) {
//        Thread jetty = new JettyServer();
//        jetty.start();
//        tomcatWait();
    }

    /**
     * 线程阻塞，启动jetty
     */
    public static synchronized void tomcatWait() {
        try {
            JettyListener.class.wait();
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
    }

    /**
     * 
     */
    public static synchronized void tomcatContinue() {
        JettyListener.class.notify();
    }

    /**
     * jetty 线程
     * 
     * @author CloudKing
     */
    private class JettyServer extends Thread {
        /**
         * run
         */
        @Override
        public void run() {
            //初始化PropertyManager
            try {
                PropertyManager.getInstance().initProperty();
            } catch (Exception e1) {
                LogUtil.error(e1);
                e1.printStackTrace();
            }
            // 初始化服务  
            server = new Server();
            // 配置连接器
            Connector httpConnector = new SelectChannelConnector();
            httpConnector.setPort(Integer.parseInt(PropertyManager.getInstance().getXMLProperty(
                            PropertyManager.SERVER_START_PORT)));
            server.addConnector(httpConnector);

            // 配置上下文
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            WebAppContext ctx = new WebAppContext(ContextUtil.getWebRoot(), "/");
            try {
                //设置web.xml
                Resource webXml = Resource.newResource(ctx.getWar() + File.separator + "WEB-INF" + File.separator
                                + "web.xml");
                ctx.setOverrideDescriptor(webXml.getURL().toString());
            } catch (MalformedURLException e) {
                LogUtil.error(e);
            } catch (IOException e) {
                LogUtil.error(e);
            }
            contexts.addHandler(ctx);
            server.setHandler(contexts);
            // 启动
            try {
                server.start();
                server.join();
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
    }

    public static Server getServer() {
        return server;
    }

}
