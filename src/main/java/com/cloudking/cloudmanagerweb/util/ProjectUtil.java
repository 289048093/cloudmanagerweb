/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cloudking.cloudmanagerweb.PropertyManager;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MenuVO;

/**
 * 当前项目有关的工具类
 * 
 * @author CloudKing
 */
public final class ProjectUtil {
    /**
     * spring上下文
     */
    private static WebApplicationContext applicationContext;
    /**
     * 根域
     */
    private static DomainEntity rootDomainEntity;
    /**
     * 根域管理员
     */
    private static UserEntity rootDomainUserEntity;

    /**
     * 默认构造方法
     */
    private ProjectUtil(){

    }

    /**
     * 小数格式化
     * 
     * @param srcNum
     * @return
     */
    public static String decimalFormat(double srcNum, String formatStr) {
        DecimalFormat format = new DecimalFormat(formatStr);
        return format.format(srcNum);
    }

    /**
     * 生成存储报警标识
     * 
     * @param storageIdentity
     * @return
     */
    public static String generateStorageWarnIdentity(String storageIdentity) {
        return String.format("%1$s_%2$s", storageIdentity, "storage");
    }

    /**
     * 生成计算节点cpu报警标识
     * 
     * @param computeIdentity
     * @return
     */
    public static String generateComputeCPUWarnIdentity(String computeIdentity) {
        return String.format("%1$s_%2$s", computeIdentity, "cpu");
    }

    /**
     * 生成计算节点内存报警标识
     * 
     * @param computeIdentity
     * @return
     */
    public static String generateComputeMemIdentity(String computeIdentity) {
        return String.format("%1$s_%2$s", computeIdentity, "memory");
    }

    /**
     * 获取根域
     * 
     * @return
     */
    public static DomainEntity getRootDomain() {
        if (rootDomainEntity != null) {
            return rootDomainEntity;
        }
        EntityManager em = getEntityManager();
        rootDomainEntity = em.find(DomainEntity.class, Constant.ROOT_DOMAIN_ID);
        rootDomainUserEntity = rootDomainEntity.getUser();
        return rootDomainEntity;
    }

    /**
     * 释放rootDomain;
     */
    public static void releaseRootDomainAndUser() {
        rootDomainEntity = null;
        rootDomainUserEntity = null;
    }

    /**
     * 获取根域管理员
     * 
     * @return
     */
    public static UserEntity getRootDomainUser() {
        if (rootDomainEntity == null) {
            getRootDomain();
        }
        return rootDomainUserEntity;
    }

    /**
     * 初始化spring
     * 
     * @param servletContext
     */
    public static void initSpringContext(ServletContext servletContext) {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        applicationContext = wac;
    }

    /**
     * 获取bean
     * 
     * @param key
     * @return
     */
    public static Object getSpringBean(String key) {
        return applicationContext.getBean(key);
    }

    /**
     * 比对域，计算节点所有，节点可用最小数
     * 
     * @param one
     * @param tow
     * @param three
     * @return
     */
    public static int getMinNumber(int one, int tow, int three) {
        int min = 0;
        if (one < tow) {
            min = one;
        } else {
            min = tow;
        }
        return min < three ? min : three;
    }

    /**
     * 获取文件的HOME目录
     * 
     * @return
     */
    public static File getHomeDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME));
    }

    /**
     * 获取文件的Template目录
     * 
     * @return
     */
    public static File getTemplateDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME)
                        + File.separator + "template");
    }

    /**
     * 获取文件的Tmp目录
     * 
     * @return
     */
    public static File getTmpDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME)
                        + File.separator + "tmp");
    }

    /**
     * 获取工单附件目录
     * 
     * @return
     */
    public static File getWorkOrderAttachmentDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME)
                        + File.separator + "attachment");
    }

    /**
     * 获取RRD目录
     * 
     * @return
     */
    public static File getRRDDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME)
                        + File.separator + "rrd");
    }

    /**
     * 获取RRD文件
     * 
     * @return
     */
    public static File getRRDFile(String roomIdentityName, String rackIdentityName, String equipmentIdentityName) {
        return new File(getRRDDir().getAbsolutePath() + File.separator + roomIdentityName + File.separator
                        + rackIdentityName + File.separator + equipmentIdentityName + ".rrd");
    }

    /**
     * 获取文件的DB备份目录
     * 
     * @return
     */
    public static File getDBBackUPDir() {
        return new File(PropertyManager.getInstance().getXMLProperty(
                        PropertyManager.XML_CLOUDMANAGERWEB_CLOUDMANAGERWEBHOME)
                        + File.separator + "dbbackup");
    }

    /**
     * 生成模块菜单的json字符串，ztree使用
     */
    public static String generateDomainForZtree(Collection<DomainVO> domainVOs) {
        if (domainVOs == null) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        String parentCode = null;
        for (DomainVO domainVO : domainVOs) {
            parentCode = "";
            if (domainVO.getCode().length() != 2) {
                parentCode = domainVO.getCode().substring(0, domainVO.getCode().length() - 2);
            }
            result.append(String.format("{ id:'%1$s' , pId:'%2$s' , name:'%3$s' , dId:%4$d },", domainVO.getCode(),
                            parentCode, domainVO.getName(), domainVO.getId()));
        }
        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }
        result.append("]");
        return result.toString();
    }

    /**
     * 生成模块菜单的json字符串，ztree使用
     */
    public static String generateMenuForZtree(Collection<MenuVO> menuVOs) {
        if (menuVOs == null) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (MenuVO menuVO : menuVOs) {
            result.append(String.format("{id:'%1$s',name:'%2$s',checked:'%3$b'},", menuVO.getId(), menuVO.getName(),
                            menuVO.getHasMenuFlag()));
        }
        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }
        result.append("]");
        return result.toString();
    }

    /**
     * 生成机房名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createMachineRoomIdentityName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "mroom" + System.currentTimeMillis();
    }

    /**
     * 生成机架名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createMachineRackIdentityName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "mrack" + System.currentTimeMillis();
    }

    /**
     * 生成计算节点名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createComputeResourceIdentityName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "cr" + System.currentTimeMillis();
    }

    /**
     * 生成存储资源名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createStorageResourceIdentityName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "sr" + System.currentTimeMillis();
    }

    /**
     * 创建存储池名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createStoragePoolName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "sp" + System.currentTimeMillis();
    }

    /**
     * 创建备份存储名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createBackupStoragePoolName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "bs" + System.currentTimeMillis();
    }

    /**
     * 创建卷的名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createVolumnName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "v" + System.currentTimeMillis();
    }

    /**
     * 创建快照的名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createSnapshotName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "ss" + System.currentTimeMillis();
    }

    /**
     * 创建备份的名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createBackupName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "bu" + System.currentTimeMillis();
    }

    /**
     * 创建虚机名字
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createVmName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "vm" + System.currentTimeMillis();
    }

    /**
     * 创建网络名
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createNetworkName() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "nw" + System.currentTimeMillis();
    }

    /**
     * 工单序列号
     * 
     * @param storagePoolID
     * @return
     */
    public static synchronized String createWorkOrderSerialNumber() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            LogUtil.error(e);
        }
        return "wo" + System.currentTimeMillis();
    }

    /**
     * 千字节转换成M
     * 
     * @param bytee
     * @return
     */
    public static Integer kByteToMega(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return (int) Math.floor(bytee / 1024.0);
    }

    /**
     * MB转换千字节
     * 
     * @param bytee
     * @return
     */
    public static Long megaToKByte(Long mega) {
        if (mega == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return mega * 1024;
    }

    /**
     * 千字节转换成G
     * 
     * @param bytee
     * @return
     */
    public static Integer kByteToGiga(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return (int) Math.floor(bytee / 1024.0 / 1024.0);
    }

    /**
     * G转换成千字节kb
     * 
     * @param bytee
     * @return
     */
    public static Long gigaToKByte(Long giga) {
        if (giga == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return giga * 1024 * 1024;
    }

    /**
     * 字节转换成M
     * 
     * @param bytee
     * @return
     */
    public static Integer byteToMega(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return ((Long) (bytee / 1024 / 1024)).intValue();
    }

    /**
     * 位转换成M
     * 
     * @param bytee
     * @return
     */
    public static Integer bitToMega(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return ((Long) (bytee / 8 / 1024 / 1024)).intValue();
    }

    /**
     * 字节转换成G
     * 
     * @param bytee
     * @return
     */
    public static Integer byteToGiga(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return (int) (bytee / 1024 / 1024 / 1024);
    }

    /**
     * 位转换成M
     * 
     * @param bytee
     * @return
     */
    public static Integer bitToGiga(Long bytee) {
        if (bytee == null) {
            throw new IllegalArgumentException("传输的参数为空");
        }
        return ((Long) (bytee / 8 / 1024 / 1024 / 1024)).intValue();
    }

    /**
     * 判断是否能ping同ip
     * 
     * @param Ip
     * @return
     * @throws IOException
     *             IO异常
     */
    public static boolean pingIp(String ip) throws IOException {
        String cmd = "";
        if (isLinux()) {
            cmd = "ping " + ip + " -w 1 -c 1";
        } else if (isWindows()) {
            cmd = "ping " + ip + " -w 300 -n 1";
        } else {
            throw new IOException("操作系统不支持此操作");
        }
        Process p = Runtime.getRuntime().exec(cmd);
        InputStreamReader in = null;
        StringWriter wr = null;
        try {
            in = new InputStreamReader(p.getInputStream());
            wr = new StringWriter();
            char[] buffer = new char[1024 * 4];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                wr.write(buffer, 0, n);
            }
            String result = wr.toString();
            if (isLinux()) {
                return !result.contains("100% packet loss");
            } else if (isWindows()) {
                return !result.contains("timed out");
            } else {
                throw new IOException("操作系统不支持此操作");
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (wr != null) {
                in.close();
            }
        }
    }

    /**
     * 更新xml中mysql备份时间
     * 
     * @param intervalValue
     * @throws Exception
     *             所有异常
     */
    public static void updateMysqlBackUpDaysInXml(String intervalValue) throws Exception {
        SAXReader reader = new SAXReader();
        String applicationContextXML = ProjectUtil.class.getClassLoader().getResource("spring-quartz.xml").getPath();
        Document doc = reader.read(new InputStreamReader(new BufferedInputStream(new FileInputStream(new File(
                        applicationContextXML))), "UTF-8"));
        Node repeatInterval = doc
                        .selectSingleNode("/*[name()='beans']/*[name()='bean']/*[@name='repeatInterval']/*[name()='value']");
        repeatInterval.setText(String.valueOf(Long.parseLong(intervalValue) * 24 * 60 * 1000));
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
                        applicationContextXML)), "UTF-8"), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.close();
    }

    /**
     * 用dom4j拿根目录下的XML文件的节点内容
     * 
     * @param xmlName
     *            xml名称
     * @param xPath
     *            处理命名空间的xpath
     * @return
     * @throws Exception
     *             所有异常
     */
    public static Map<Boolean, String> getXmlProperty(String xmlName, String xPath) throws Exception {
        Map<Boolean, String> map = new HashMap<Boolean, String>();
        SAXReader reader = new SAXReader();
        URL url = ProjectUtil.class.getClassLoader().getResource(xmlName);
        if (url == null) {
            map.put(false, "xml文件未找到");
            return map;
        }
        String xml = url.getPath();
        Document doc = reader.read(new InputStreamReader(new BufferedInputStream(new FileInputStream(new File(xml))),
                        "UTF-8"));
        if (doc == null) {
            map.put(false, "xml文件读取错误！");
            return map;
        }
        Node node = doc.selectSingleNode(xPath);
        map.put(node != null, node == null ? "属性未找到" : node.getText());
        return map;
    }

    /**
     * 获取EntityManager对象
     * 
     * @return
     */
    public static EntityManager getEntityManager() {
        return ((EntityManagerFactory) getSpringBean("entityManagerFactory")).createEntityManager();
    }

    /**
     * 把对象转换成JSON字符串
     * 
     * @param obj
     * @throws JSONException
     *             json异常
     */
    public static String objectToJSON(Object obj) throws JSONException {
        return JSONUtil.serialize(obj, null, null, true, true);
    }

    /**
     * 是否为Linux
     * 
     * @return
     */
    public static Boolean isLinux() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 是否为windows
     * 
     * @return
     */
    public static Boolean isWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取文件后缀名
     * 
     * @param file
     * @return
     */
    public static String getFileSubffix(String name) {
        int index = name.lastIndexOf(".");
        if (index != -1) {
            return name.substring(index + 1, name.length());
        } else {
            return "";
        }

    }

    /**
     * 生成文件 附件文件
     * 
     * @param key
     * @return
     */
    public static File genrateAttachmentFile(String workOrderSerialNumber, String subffix) {
        String fileName = UUID.randomUUID().toString() + "." + subffix;
        File attachmentDir = new File(ProjectUtil.getWorkOrderAttachmentDir(), workOrderSerialNumber);
        File attachmentFile = new File(attachmentDir, fileName);
        return attachmentFile;
    }

    /**
     * 获取保存的文件 附件文件
     * 
     * @param key
     * @return
     */
    public static File getAttachmentFile(String workOrderSerialNumber, String filename) {
        File attachmentDir = new File(ProjectUtil.getWorkOrderAttachmentDir(), workOrderSerialNumber);
        File attachmentFile = new File(attachmentDir, filename);
        return attachmentFile;
    }

    /**
     * 是否到了备份时间
     * 
     * @param backupTimeStr
     * @return
     */
    public static Boolean isBackupTime(String backupTimeStr) {
        String[] strDatas = backupTimeStr.split(";");
        if (strDatas.length < 5) {
            throw new IllegalArgumentException("备份时间字符串格式错误");
        }
        Calendar now = Calendar.getInstance();
        Calendar backupTime = (Calendar) now.clone();
        int backupMonth = StringUtil.isBlank(strDatas[0]) ? backupTime.get(Calendar.MONTH) : Integer
                        .parseInt(strDatas[0]) - 1;
        int backupDay = StringUtil.isBlank(strDatas[1]) ? backupTime.get(Calendar.DATE) : Integer.parseInt(strDatas[1]);
        int backupWeek = StringUtil.isBlank(strDatas[2]) ? backupTime.get(Calendar.DAY_OF_WEEK) : Integer
                        .parseInt(strDatas[2]);
        int backupHour = Integer.parseInt(strDatas[3]);
        int backupMinute = Integer.parseInt(strDatas[4]);
        backupTime.set(backupTime.get(Calendar.YEAR), backupMonth, backupDay, backupHour, backupMinute);
        return backupWeek == now.get(Calendar.DAY_OF_WEEK) && now.equals(backupTime);
    }
}
