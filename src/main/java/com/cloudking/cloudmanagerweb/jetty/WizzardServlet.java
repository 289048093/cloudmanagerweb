/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 9, 2012  1:43:03 PM
 */
package com.cloudking.cloudmanagerweb.jetty;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cloudking.cloudmanager.core.network.NetworkMode;
import com.cloudking.cloudmanager.core.network.VirtualNetwork;
import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanagerweb.JPAEventException;
import com.cloudking.cloudmanagerweb.JettyListener;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.DateUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.RRDUtil;

/**
 * @author CloudKing
 */
public class WizzardServlet extends HttpServlet {

    /**
     * serivalVersionUID
     */
    private static final long serialVersionUID = -1196288564454183611L;
    /**
     * 文件编码
     */
    private static final String CODE = "UTF-8";
    /**
     * ddl_sql文件存放地址
     */
    private static final String DDL_SQL_FILE_PATH = "/WEB-INF/classes/ddl.sql";
    /**
     * dml_sql文件存放地址
     */
    private static final String DML_SQL_FILE_PATH = "/WEB-INF/classes/dml.sql";
    /**
     * 数据库驱动
     */
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    /**
     * ddl regx
     */
    private static final String DDL_REGEX = "(?i)(?:(DROP)|(CREATE))[^;]+;";
    /**
     * dml regx
     */
    private static final String DML_REGEX = "(?i)INSERT.+;\\s*";

    /**
     * 项目路径
     */
    private String proPath;

    /**
     * @throws IOException
     *             IO异常
     * @throws ServletException
     *             servlet异常
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @throws IOException
     *             IO异常
     * @throws ServletException
     *             servlet异常
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String path = req.getRequestURI();
        String action = path.substring(1, path.lastIndexOf("."));
        if (action.equals("dbSet")) {
            session.setAttribute("dbURL", req.getParameter("dbURL"));
            session.setAttribute("username", req.getParameter("username"));
            session.setAttribute("password", req.getParameter("password"));
            resp.sendRedirect("homepath.html");
            return;
        }
        if (action.equals("trafficmonitorHomeSet")) {
            session.setAttribute("homeURI", req.getParameter("homeURI"));
            resp.sendRedirect("rootdomain.html");
            return;
        }
        if (action.equals("rootDomainSet")) {
            session.setAttribute("rootDomainName", req.getParameter("rootDomainName"));
            resp.sendRedirect("network.html");
            return;
        }
        if (action.equals("network")) {
            session.setAttribute("name4AddNetwork", req.getParameter("name4AddNetwork"));
            session.setAttribute("cidr4AddNetwork", req.getParameter("cidr4AddNetwork"));
            session.setAttribute("startIP4AddNetwork", req.getParameter("startIP4AddNetwork"));
            session.setAttribute("endIP4AddNetwork", req.getParameter("endIP4AddNetwork"));
            session.setAttribute("type4AddNetwork", req.getParameter("type4AddNetwork"));
            resp.sendRedirect("roomrack.html");
            return;
        }

        if (action.equals("roomAndRack")) {
            session.setAttribute("machineRoomName", req.getParameter("machineRoomName"));
            session.setAttribute("machineRackName", req.getParameter("machineRackName"));
            resp.sendRedirect("compute.html");
            return;
        }
        if (action.equals("compute")) {
            session.setAttribute("name4AddCompute", req.getParameter("name4AddCompute"));
            session.setAttribute("ip4AddCompute", "ip4AddCompute");
            resp.sendRedirect("pool.html");
            return;
        }
        if (action.equals("pool")) {
            session.setAttribute("name4AddPool", req.getParameter("name4AddPool"));
            resp.sendRedirect("storage.html");
            return;
        }
        if (action.equals("storage")) {
            session.setAttribute("name4AddStorage", req.getParameter("name4AddStorage"));
            session.setAttribute("ip4AddStorage", req.getParameter("ip4AddStorage"));
            session.setAttribute("warn4AddStorage", req.getParameter("warn4AddStorage"));
            try {
                setOver(session);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            turnToCloudManager(resp);
            return;
        }
    }

    /**
     * 设置完成后跳转到云平台
     * 
     * @param resp
     * @throws IOException
     *             io异常
     */
    private void turnToCloudManager(HttpServletResponse resp) throws IOException {
        //连接tomcat8080端口
        Socket socket = null;
        while (true) {
            try {
                Thread.sleep(3000);
                socket = new Socket("localhost", 8080);
                if (socket == null) {
                    continue;
                }
                if (socket.isConnected()) {
                    break;
                }
            } catch (Exception e) {
                LogUtil.info("tomca正在启动。。。");
            }
        }
        InetAddress addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        resp.sendRedirect("http://" + ip + ":8080/cloudmanager_web");
        try {
            JettyListener.getServer().stop();
        } catch (Exception e) {
            LogUtil.error("jetty stop...");
        }
    }

    /**
     * 完成安装向导
     * 
     * @param session
     * @throws IOException
     *             io异常
     * @throws DocumentException
     *             dom4j异常
     */
    private void setOver(HttpSession session) throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        String cfguri = WizzardServlet.class.getClassLoader().getResource("project-config.xml").getPath();
        Document doc = reader.read(new InputStreamReader(
                        new BufferedInputStream(new FileInputStream(new File(cfguri))), CODE));
        proPath = doc.selectSingleNode("/" + PropertyManager.WEBAPP_ROOT_PATH).getText().trim();
        setDB(session);
        setProjectCfg(session);
        setStruts(session);
        try {
            execDBData(session);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param session
     * @throws IOException
     *             IO异常
     * @throws ClassNotFoundException
     *             驱动未找到
     */
    private void execDBData(HttpSession session) throws IOException, ClassNotFoundException {
        File sqlFile = new File(proPath + DDL_SQL_FILE_PATH);
        String sqlStr = FileUtils.readFileToString(sqlFile, "UTF-8");
        Connection con = null;
        PreparedStatement presmt = null;
        String sql = "";
        Pattern pattern = Pattern.compile(DDL_REGEX);
        Matcher matcher = pattern.matcher(sqlStr);
        String dbURL = (String) session.getAttribute("dbURL");
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
//        try {
//            Class.forName(DB_DRIVER);
//            con = DriverManager
//                            .getConnection(dbURL.substring(0, dbURL.lastIndexOf('/')) + "/mysql", username, password);
//            while (matcher.find()) {
//                sql = matcher.group();
//                presmt = con.prepareStatement(sql);
//                if (sql.matches(".*creat.*database.*")) {
//                    presmt.execute();
//            con = DriverManager.getConnection(dbURL, username, password);
//                    continue;
//                }
//                presmt.execute();
//            }
//            //执行dml语句脚本
//            sqlFile = new File(proPath + DML_SQL_FILE_PATH);
//            sqlStr = FileUtils.readFileToString(sqlFile, "UTF-8");
//            pattern = Pattern.compile(DML_REGEX);
//            matcher = pattern.matcher(sqlStr);
//            while (matcher.find()) {
//                sql = matcher.group().trim();
//                presmt = con.prepareStatement(sql);
//                presmt.execute();
//            }
//            setRoomAndRack(con, presmt, session);
//            setNetwork(con, presmt, session);
//            setCompute(con, presmt, session);
//            setPool(con, presmt, session);
//            setStorage(con, presmt, session);
//            setRootDomain(con, presmt, session);
            //继续tomcat的线程启动
//            JettyListener.tomcatContinue();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeDB(con, presmt);
//        }
    }

    /**
     * 关闭数据库连接
     * 
     * @param con
     * @param presmt
     */
    private void closeDB(Connection con, PreparedStatement presmt) {
        try {
            presmt.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            con.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 设置Domain
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setRootDomain(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "update tb_domain set NAME_=? and STORAGECAPACITY_=? and AVAILABLESTORAGECAPACITY_=? where ID_=?";
        presmt = con.prepareStatement(sql);
        presmt.setString(1, (String) session.getAttribute("rootDomainName"));
        presmt.setLong(2, (Long) session.getAttribute("storageCapacity"));
        presmt.setLong(3, (Long) session.getAttribute("storageCapacity"));
        presmt.setLong(4, Constant.ROOT_DOMAIN_ID);
        presmt.executeUpdate();
    }

    /**
     * 设置机房或者机柜
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setRoomAndRack(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "insert into tb_machineroom (ADDTIME_, DESC_, IDENTITYNAME_, NAME_) values (?, ?, ?, ?)";
        String identityName = ProjectUtil.createMachineRoomIdentityName();
        String roomName = (String) session.getAttribute("machineRoomName");
        presmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, roomName);
        presmt.setString(3, identityName);
        presmt.setString(4, roomName);
        File roomDir = new File(getRRDDir(), identityName);
        presmt.execute();
        ResultSet rs = presmt.getGeneratedKeys();
        Long roomId = null;
        if (rs.next()) {
            roomId = rs.getLong(1);
        }
        if (roomDir.exists()) {
            //存在，并且不是目录，就删除，再添加
            if (!roomDir.isDirectory()) {
                if (roomDir.delete()) {
                    roomDir.mkdirs();
                }
            }
        } else {
            roomDir.mkdirs();
        }
        sql = "insert into tb_machinerack (ADDTIME_, DESC_, IDENTITYNAME_, MACHINEROOM_ID_, NAME_, WARN4ROOM_) values (?, ?, ?, ?, ?, ?)";
        String rackIdentityName = ProjectUtil.createMachineRackIdentityName();
        String rackName = (String) session.getAttribute("machineRackName");
        presmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, rackName);
        presmt.setString(3, rackIdentityName);
        presmt.setLong(4, roomId);
        presmt.setString(5, rackName);
        presmt.setBoolean(6, false);
        File rackDir = new File(roomDir, rackIdentityName);
        presmt.execute();
        rs = presmt.getGeneratedKeys();
        Long rackId = null;
        if (rs.next()) {
            rackId = rs.getLong(1);
        }
        if (rackDir.exists()) {
            //存在，并且不是目录，就删除，再添加
            if (!rackDir.isDirectory()) {
                if (rackDir.delete()) {
                    rackDir.mkdirs();
                }
            }
        } else {
            rackDir.mkdirs();
        }
        session.setAttribute("rackId", rackId);
        session.setAttribute("rackRrdPath", rackDir.getAbsolutePath());
    }

    /**
     * 计算节点添加
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setCompute(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "insert into tb_computeresource(ADDTIME_,DESC_,IDENTITYNAME_,NAME_,RRDPATH_,WARN4RACK_,COMPUTERESOURCEPOOL_ID_,MACHINERACK_ID_,IP_,CPU_,MEMORY_) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
        String ip = (String) session.getAttribute("ip4AddCompute");
        String computeName = (String) session.getAttribute("name4AddCompute");
        String identityName = ProjectUtil.createComputeResourceIdentityName();
        String rrdPath = (String) session.getAttribute("rackRrdPath") + File.separator + identityName;
        Long rackId = (Long) session.getAttribute("rackId");
        Long poolId = (Long) session.getAttribute("poolId");
        presmt = con.prepareStatement(sql);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, computeName);
        presmt.setString(3, identityName);
        presmt.setString(4, computeName);
        presmt.setString(5, rrdPath);
        presmt.setBoolean(6, false);
        presmt.setLong(7, poolId);
        presmt.setLong(8, rackId);
        presmt.setString(9, ip);

        //core
//        try {
//            // 查看是否存在计算机，如果存在获取主机的CPU和内存
//            NodeStat nodeStat = NodeStat.getNodeInfo(ip);
//            if (nodeStat == null) {
//                LogUtil.error(String.format("初始化向导：IP为【%1$s】的节点不存在!", ip));
//            } else {
//                presmt.setInt(10, nodeStat.getCpus());
//                presmt.setInt(11, ProjectUtil.kByteToMega(nodeStat.getTotalMem()));
//            }

//        } catch (Exception e) {
//            LogUtil.error(e);
//            return;
//        }
        presmt.execute();

        //创建rrd文件
        try {
            RRDUtil.createComputeResourceRRDS(rrdPath);
        } catch (IOException e) {
            throw new JPAEventException("创建RRD文件出错");
        }
    }

    /**
     * 设置资源池
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setPool(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "insert into tb_computeResourcePool (ADDTIME_, DESC_, NAME_) values (?, ?, ?)";
        String poolName = (String) session.getAttribute("name4AddPool");
        presmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, poolName);
        presmt.setString(3, poolName);
        presmt.execute();
        ResultSet rs = presmt.getGeneratedKeys();
        if (rs.next()) {
            session.setAttribute("poolId", rs.getLong(1));
        }
    }

    /**
     * 网络设置
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setNetwork(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "insert into tb_network(ADDTIME_,NAME_,DESC_,REALNAME_,STARTIP_,ENDIP_,TYPE_,CIDR_) values(?,?,?,?,?,?,?,?)";
        String networkName = (String) session.getAttribute("name4AddNetwork");
        String realName = ProjectUtil.createNetworkName();
        String startIp = (String) session.getAttribute("startIp4AddNetwork");
        String endIp = (String) session.getAttribute("endIp4AddNetwork");
        String type = (String) session.getAttribute("type4AddNetwork");
        String cird = (String) session.getAttribute("cird4AddNetwork");
        presmt = con.prepareStatement(sql);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, networkName);
        presmt.setString(3, networkName);
        presmt.setString(4, realName);
        presmt.setString(5, startIp);
        presmt.setString(6, endIp);
        presmt.setString(7, type);
        presmt.setString(8, cird);
        presmt.execute();
        //调用core
//        String computeIp = (String) session.getAttribute("ip4AddCompute");
        try {
            VirtualNetwork.createNetwork(realName, cird, startIp, endIp,
                            type.equalsIgnoreCase("nat") ? NetworkMode.nat : NetworkMode.route);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e);
        }
    }

    /**
     * 设置存储
     * 
     * @param con
     * @param presmt
     * @param session
     * @throws SQLException
     *             sql异常
     */
    private void setStorage(Connection con, PreparedStatement presmt, HttpSession session) throws SQLException {
        String sql = "insert into tb_storageresource(ADDTIME_,DESC_,IDENTITYNAME_,IP_,NAME_,POOLNAME_,RRDPATH_,WARN4RACK_,DOMAIN_ID_,MACHINERACK_ID_,CAPACITY_ "
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
        String storageName = (String) session.getAttribute("name4AddStroage");
        String storageIp = (String) session.getAttribute("ip4AddStorage");
        String poolName = (String) session.getAttribute("name4AddPool");
        String identityName = ProjectUtil.createStorageResourceIdentityName();
        String rrdPath = (String) session.getAttribute("rackRrdPath") + File.separator + identityName;
        Long rackId = (Long) session.getAttribute("rackId");
        presmt = con.prepareStatement(sql);
        presmt.setString(1, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        presmt.setString(2, storageName);
        presmt.setString(3, identityName);
        presmt.setString(4, storageIp);
        presmt.setString(5, storageName);
        presmt.setString(6, poolName);
        presmt.setString(7, rrdPath);
        presmt.setBoolean(8, false);
        presmt.setLong(9, Constant.ROOT_DOMAIN_ID);
        presmt.setLong(10, rackId);

        String computeIp = (String) session.getAttribute("ip4AddCompute");

        Storage storage = null;
        //调用core
        try {
            storage = Storage.createStorage(poolName, computeIp);
            Long capacity = storage.getAvailable();
            session.setAttribute("storageCapacity", capacity);
        } catch (Exception e) {
            LogUtil.error(e);
            return;
        }

        //创建rrd文件
        try {
            RRDUtil.createStorageResourceRRDS(rrdPath);
        } catch (IOException e) {
            throw new JPAEventException("创建RRD文件出错", e);
        }
    }

    /**
     * 设置数据库
     * 
     * @param session
     *            session
     * @throws DocumentException
     *             dom4j解析异常
     * @throws IOException
     *             io异常
     */
    private void setDB(HttpSession session) throws DocumentException, IOException {
        File persistenceXML = new File(proPath + "/WEB-INF/classes/META-INF/persistence.xml");
        String dbURL = (String) session.getAttribute("dbURL");
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new InputStreamReader(new BufferedInputStream(new FileInputStream(persistenceXML)),
                        CODE));
        Node sessionFactory = doc
                        .selectSingleNode("/*[name()='persistence']/*[name()='persistence-unit']/*[name()='properties']");
        Node node = sessionFactory.selectSingleNode("*[name()='property'][@name='hibernate.connection.url']");
        ((Element) node).attributeValue("value", dbURL);
        node = sessionFactory.selectSingleNode("*[name()='property'][@name='hibernate.connection.username']");
        ((Element) node).attributeValue("value", username);
        node = sessionFactory.selectSingleNode("*[name()='property'][@name='hibernate.connection.password']");
        ((Element) node).attributeValue("value", password);
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
                        persistenceXML)), CODE), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.close();
    }

    /**
     * 设置project-config.xml
     * 
     * @param session
     *            session
     * @throws DocumentException
     *             dom4j解析异常
     * @throws IOException
     *             io异常
     */
    private void setProjectCfg(HttpSession session) throws DocumentException, IOException {
        File projectCfgXML = new File(proPath + "/WEB-INF/classes/project-config.xml");
        String homeURI = (String) session.getAttribute("homeURI");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new InputStreamReader(new BufferedInputStream(new FileInputStream(projectCfgXML)),
                        CODE));
        Node root = doc.selectSingleNode("/cloudmanagerweb");
        Node cloudmanagerwebHome = root.selectSingleNode("cloudmanagerwebHome");
        cloudmanagerwebHome.setText(homeURI);
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(
                        projectCfgXML)), CODE), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.close();
    }

    /**
     * 设置struts.xml
     * 
     * @param session
     *            session
     * @throws DocumentException
     *             dom4j解析异常
     * @throws IOException
     *             io异常
     */
    private void setStruts(HttpSession session) throws DocumentException, IOException {
        File strutsXML = new File(proPath + "/WEB-INF/classes/struts.xml");
        String homeURI = (String) session.getAttribute("homeURI");
        SAXReader reader = new SAXReader();
        //修改struts临时文件路径
        Document doc = reader
                        .read(new InputStreamReader(new BufferedInputStream(new FileInputStream(strutsXML)), CODE));
        Element tmpDir = (Element) doc.selectSingleNode("/struts/constant[@name='struts.multipart.saveDir']");
        tmpDir.attribute("value").setValue(homeURI + "/data/tmp");
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new BufferedOutputStream(
                        new FileOutputStream(strutsXML)), CODE), OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.close();
    }

    /**
     * 获取rrd目录
     * 
     * @return
     */
    private static String getRRDDir() {
        SAXReader reader = new SAXReader(false);
        Document document = null;
        try {
            document = reader.read(new File(WizzardServlet.class.getClassLoader().getResource("project-config.xml")
                            .getFile()));
        } catch (DocumentException e1) {
            LogUtil.fatal(e1);
            e1.printStackTrace();
        }
        Element e = (Element) document.selectSingleNode("/cloudmanagerweb/cloudmanagerwebHome");
        return e.getTextTrim() + File.separator + "rrd";
    }
}
