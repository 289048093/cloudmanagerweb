/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.io.File;
/**
 * 得到系统环境工具
 * 
 * @author CloudKing
 * 
 */
public final class ContextUtil {
    /**
     * WEB根目录
     */
    private static String webRoot = "";
    /**
     * 默认构造方法
     */
    private ContextUtil(){
    }
    
    /**
     * 设置根目录
     * @param webRoot
     */
    public static void setWebRoot(String webRoot) {
        ContextUtil.webRoot = webRoot.trim();
    }

    /**
     * 得到根目录
     * 
     * @return
     */
    public static String getWebRoot() {
        return webRoot;
    }

    /**
     * 得到classes目录
     * 
     * @return
     */
    public static String getClassesPath() {
        return webRoot + File.separator + "WEB-INF" + File.separator
                        + "classes";

    }

    /**
     * 得到临时目录
     * 
     * @return
     */
    public static String getTmpPath() {
        String tmp = System.getProperty("java.io.tmpdir");
        if (tmp == null) {
            tmp = getWebRoot() + File.separator + "tmp";
            File file = new File(tmp);
            if (!file.exists()) {
                try {
                    file.mkdir();
                } catch (RuntimeException e) {
                    LogUtil.error(e);
                }
            }
        }
        return tmp;
    }
}
