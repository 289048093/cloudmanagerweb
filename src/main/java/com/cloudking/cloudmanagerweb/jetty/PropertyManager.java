/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 9, 2012  11:39:11 AM
 */
package com.cloudking.cloudmanagerweb.jetty;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cloudking.cloudmanagerweb.util.LogUtil;

/**
 * @author CloudKing
 * 
 */
public final class PropertyManager {
    /**
     * 开始端口号
     */
    public static final String SERVER_START_PORT = "cloudmanagerweb/jetty/startPort";
    /**
     * 配置中项目地址
     */
    public static final String WEBAPP_ROOT_PATH = "cloudmanagerweb/webApp/rootPath";
    /**
     * 单例对象
     */
    private static PropertyManager instance = new PropertyManager();
    /**
     * xml配置文件集合
     */
    private static Map<String, String> xmlProperty = new ConcurrentHashMap<String, String>();
    /**
     * project-config.xml的 document
     */
    private static Document document;

    /**
     * 默认的构造方法
     */
    private PropertyManager(){

    }

    /**
     * 获取单例对象
     * 
     * @return
     */
    public static PropertyManager getInstance() {
        return instance;
    }

    /**
     * 初始化property
     * 
     * @throws Exception
     */
    public void initProperty() {
        try {
            initXMLConfig();
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 获取XMl属性
     * 
     * @param key
     * @return
     */
    public String getXMLProperty(String key) {
        return xmlProperty.get(key);
    }

    /**
     * 初始化xml配置
     * 
     * @throws Exception
     *             抛出异常
     */
    private void initXMLConfig() throws Exception {
        SAXReader reader = new SAXReader(false);
        InputStream is = null;
        try {
            is = ContextUtil.getInputStreamFromJar("project-config.xml");
            document = reader.read(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        Element serverStartPort = (Element) document.selectSingleNode(SERVER_START_PORT);
        xmlProperty.put(SERVER_START_PORT, serverStartPort.getTextTrim());

        String webRoot = new File(new File(PropertyManager.class.getClassLoader().getResource("").getPath())
                        .getParent()).getParent();
        xmlProperty.put(WEBAPP_ROOT_PATH, webRoot);

    }
}
