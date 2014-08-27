/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cloudking.cloudmanagerweb.entity.PropertyEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;

/**
 * Property管理对象
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
public final class PropertyManager {
    /**
     * xml配置：项目的home目录
     */
    public static final String XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME = "cloudmanagerweb/cloudmanagerwebHome";

    /**
     * 域默认菜单节点位置
     */
    public static final String XML_CLOUDMANAGERWEB_DEFAULTDOMAINMENU = "cloudmanagerweb/defaultDomainMenu";
    /**
     * 默认域大类菜单Id
     */
    public static final String XML_CLOUDMANAGERWEB_DEFAULTDOMAINCATEGORYMENU = "cloudmanagerweb/defaultDomainCategoryMenu";
    /**
     * 域默认权限节点位置
     */
    public static final String XML_CLOUDMANAGERWEB_DEFAULTDOMAINRIGHTS = "cloudmanagerweb/defaultDomainRights";

    /**
     * 主机名
     */
    public static final String XML_CLOUDMANAGERWEB_FTP_HOSTNAME = "cloudmanagerweb/ftp/hostname";

    /**
     * 端口
     */
    public static final String XML_CLOUDMANAGERWEB_FTP_PORT = "cloudmanagerweb/ftp/port";
    /**
     * 用户名
     */
    public static final String XML_CLOUDMANAGERWEB_FTP_USERNAME = "cloudmanagerweb/ftp/username";
    /**
     * 密码
     */
    public static final String XML_CLOUDMANAGERWEB_FTP_PASSWORD = "cloudmanagerweb/ftp/password";
    /**
     * 报警几次后发送邮件
     */
    public static final String DB_WARN_COUNT_SEND_EMAIL = "warn_count_send_email";
    /**
     * mysql备份天数KEY
     */
    public static final String DB_MYSQL_BACKUP_DAYS_KEY = "mysql_backup_days";
    /**
     * mysql最大备份数
     */
    public static final String DB_MYSQL_BACKUP_NUM_KEY = "mysql_backup_num";
    /**
     * email是否开启
     */
    public static final String DB_EMAIL_ENABLE = "email_enable";
    /**
     * email用户名
     */
    public static final String DB_EMAIL_USERNAME = "email_username";
    /**
     * email密码
     */
    public static final String DB_EMAIL_PASSWORD = "email_password";
    /**
     * emailhost
     */
    public static final String DB_EMAIL_HOST = "email_host";
    /**
     * emailport
     */
    public static final String DB_EMAIL_PORT = "email_port";
    /**
     * email_from
     */
    public static final String DB_EMAIL_FROM = "email_from";
    /**
     * 删除操作记录的天数
     */
    public static final String DB_DELETE_EVENTLOG_DAYS_KEY = "delete_eventlog_days";
    /**
     * 删除报警记录的天数
     */
    public static final String DB_DELETE_WARNLOG_DAYS_KEY = "delete_warnlog_days";
    /**
     * 工单超时上报时间天数
     */
    public static final String WORKORDER_REPORT_DAYS = "workorder_report_days";

    /**
     * 存放xml里面属性的集合
     */
    private static Map<String, String> xmlMap = new ConcurrentHashMap<String, String>();
    /**
     * 存放db里面属性的集合
     */
    private static Map<String, String> dbMap = new ConcurrentHashMap<String, String>();
    /**
     * project-config.xml文件
     */
    private static final File PROJECT_CONFIG_FILE = new File(Thread.currentThread().getContextClassLoader()
                    .getResource("project-config.xml").getFile());
    /**
     * PropertyManager 的单例对象
     */
    private static PropertyManager instance = new PropertyManager();
    /**
     * project-config.xml文件的Document对象
     */
    private Document document;

    /**
     * 默认的构造方法
     */
    private PropertyManager(){

    }

    /**
     * 获取PropertyManager对象
     * 
     * @return
     */
    public static PropertyManager getInstance() {
        return instance;
    }

    /**
     * 保存XmlProperties
     */
    private synchronized void saveXmlProperties() {
        // Write data out to a temporary file first.
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PROJECT_CONFIG_FILE), "UTF-8"));
            OutputFormat prettyPrinter = OutputFormat.createPrettyPrint();
            XMLWriter xmlWriter = new XMLWriter(writer, prettyPrinter);
            xmlWriter.write(document);
        } catch (Exception e) {
            LogUtil.fatal(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    LogUtil.fatal(e1.getMessage(), e1);
                }
            }
        }

    }

    /**
     * 初始化xml的配置文件到该Manager
     * 
     * @throws Exception
     *             所有异常
     */
    public void initXMLConfig() throws Exception {
        SAXReader reader = new SAXReader(false);
        document = reader.read(PROJECT_CONFIG_FILE);

        //cloudmanagerweb/cloudmanagerwebHome
        Element homeDir = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME);
        xmlMap.put(XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME, homeDir.getTextTrim());
        //cloudmanagerweb/defaultDomainMenu
        Element defaultDoMainMenu = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_DEFAULTDOMAINMENU);
        xmlMap.put(XML_CLOUDMANAGERWEB_DEFAULTDOMAINMENU, defaultDoMainMenu.getTextTrim());
        //cloudmanagerweb/defaultDomainRights
        Element defaultDoMainRights = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_DEFAULTDOMAINRIGHTS);
        xmlMap.put(XML_CLOUDMANAGERWEB_DEFAULTDOMAINRIGHTS, defaultDoMainRights.getTextTrim());
        //cloudmanagerweb/ftp/hostname
        Element ftpHostname = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_FTP_HOSTNAME);
        xmlMap.put(XML_CLOUDMANAGERWEB_FTP_HOSTNAME, ftpHostname.getTextTrim());
        //cloudmanagerweb/ftp/port
        Element ftpPort = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_FTP_PORT);
        xmlMap.put(XML_CLOUDMANAGERWEB_FTP_PORT, ftpPort.getTextTrim());
        //cloudmanagerweb/ftp/username
        Element ftpUsername = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_FTP_USERNAME);
        xmlMap.put(XML_CLOUDMANAGERWEB_FTP_USERNAME, ftpUsername.getTextTrim());
        //cloudmanagerweb/ftp/password
        Element ftpPassword = (Element) document.selectSingleNode(XML_CLOUDMANAGERWEB_FTP_PASSWORD);
        xmlMap.put(XML_CLOUDMANAGERWEB_FTP_PASSWORD, ftpPassword.getTextTrim());

    }

    /**
     * 初始化db的数据到该Manager
     * 
     * @throws Exception
     *             所有异常
     */
    public static void initDbProperty() throws Exception {
        EntityManager em = ProjectUtil.getEntityManager();
        try {
            Query query = em.createQuery("from PropertyEntity order by key desc");
            List<PropertyEntity> propertyEntities = query.getResultList();
            for (PropertyEntity propertyEntity : propertyEntities) {
                dbMap.put(propertyEntity.getKey(), propertyEntity.getValue());
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * 设置数据库的值
     * 
     * @param key
     * @param value
     * @throws Exception
     *             所有异常
     */
    public synchronized void setDBProperty(String key, String value) throws Exception {
        EntityManager em = ProjectUtil.getEntityManager();
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            Query query = em.createQuery("from PropertyEntity where key=:key");
            query.setParameter("key", key);
            PropertyEntity propertyEntity = (PropertyEntity) query.getSingleResult();
            if (propertyEntity == null) {
                return;
            }
            propertyEntity.setValue(value);
            em.merge(propertyEntity);
            transaction.commit();
            dbMap.put(key, value);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * 获取数据库的值
     * 
     * @param key
     * @return
     */
    public String getDbProperty(String key) {
        EntityManager em = ProjectUtil.getEntityManager();
        try {
            //先判断dbProperty是否存在，不存在再判断数据库是否存在
            if (dbMap.get(key) == null) {
                Query query = em.createQuery("from PropertyEntity  where key=:key ");
                query.setParameter("key", key);
                PropertyEntity propertyEntity = (PropertyEntity) (query.getSingleResult());
                if (propertyEntity != null) {
                    dbMap.put(key, propertyEntity.getValue());
                }
            }
            return dbMap.get(key);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * ，设置xml文件
     * 
     * @param key
     * @param value
     * @throws Exception
     *             所有异常
     */
    public synchronized void setXMLProperty(String key, String value) throws Exception {
        xmlMap.put(key, value);
        Element keyElement = (Element) document.selectSingleNode(key);
        if (keyElement != null) {
            keyElement.setText(value.trim());
            saveXmlProperties();
        }
    }

    /**
     * 获取XML属性
     * 
     * @param key
     * @return
     */
    public synchronized String getXMLProperty(String key) {
        if (xmlMap.get(key) == null) {
            try {
                SAXReader reader = new SAXReader(false);
                document = reader.read(PROJECT_CONFIG_FILE);
                Element elementTmp = (Element) document.selectSingleNode(key);
                xmlMap.put(key, elementTmp.getTextTrim());
            } catch (DocumentException e) {
                LogUtil.fatal(e);
            }
        }
        return xmlMap.get(key);
    }

}
