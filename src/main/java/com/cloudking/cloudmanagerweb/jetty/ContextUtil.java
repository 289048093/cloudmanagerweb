/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 9, 2012  11:49:01 AM
 */
package com.cloudking.cloudmanagerweb.jetty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import com.cloudking.cloudmanagerweb.util.LogUtil;

/**
 * @author CloudKing
 */
public class ContextUtil {

    /**
     * ckmonitor.jar
     */
    private static final String WIZZARD_JAR = "wizzard.jar";

    /**
     * 构造方法
     */
    private ContextUtil(){
    }

    /**
     * 得到Root目录
     * 
     * @return
     */
    public static String getAppRoot() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        if (url == null) {
            url = ContextUtil.class.getClassLoader().getResource("");
        }
        if (url == null) {
            url = ContextUtil.class.getResource("");
        }
        if (url != null) {
            String tempFile = url.toString();
            tempFile = tempFile.replaceAll("jar:file:", "");
            int index = tempFile.indexOf(WIZZARD_JAR);
            if (index != -1) {
                tempFile = tempFile.substring(0, index);
            }
            return tempFile;
        } else {
            return null;
        }
    }

    /**
     * 得到webRoot目录
     * 
     * @return
     */
    public static String getWebRoot() {
        File classDir = new File(ContextUtil.class.getClassLoader().getResource("").getFile());
        return new File(classDir.getParent()).getParent() + File.separator + "jetty";
//        return getAppRoot() + File.separator + "webRoot";
    }

    /**
     * 从Jar里面返回资源
     * 
     * @param filename
     * @return
     */
    public static InputStream getInputStreamFromJar(String filename) {
        //如果类路径没有，就查找当前目录
        InputStream is = ContextUtil.class.getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            try {
                is = new FileInputStream(getAppRoot() + File.separator + filename);
            } catch (FileNotFoundException e) {
                LogUtil.error(getAppRoot() + File.separator + filename + " is not exists");
            }
        }
        return is;
    }
}
