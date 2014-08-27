/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import org.apache.log4j.Logger;

/**
 * 日志工具类
 * 
 * @author CloudKing
 */
public final class LogUtil {
    /**
     * 默认构造方法
     */
    private LogUtil(){

    }

    /**
     * 获取InfoLogger
     * 
     * @return
     */
    public static Logger getInfoLogger() {
        return Logger.getLogger("cloudmanagerwebInfoLogger");
    }

    /**
     * 获取WarnLogger
     * 
     * @return
     */
    public static Logger getWarnLogger() {
        return Logger.getLogger("cloudmanagerwebWarnLogger");
    }

    /**
     * 获取ErrorLogger
     * 
     * @return
     */
    public static Logger getErrorLogger() {
        return Logger.getLogger("cloudmanagerwebErrorLogger");
    }

    /**
     * 获取DebugLogger
     * 
     * @return
     */
    public static Logger getDebugLogger() {
        return Logger.getLogger("cloudmanagerwebDebugLogger");
    }

    /**
     * 获取FatalLogger
     * 
     * @return
     */
    public static Logger getFatalLogger() {
        return Logger.getLogger("cloudmanagerwebFatalLogger");
    }

    /**
     * 错误信息
     * 
     * @param message
     */
    public static void error(String message) {
        getErrorLogger().error(message);
    }

    /**
     * 错误信息
     * 
     * @param message
     */
    public static void error(Throwable throwable) {
        getErrorLogger().error("", throwable);
    }

    /**
     * 错误信息
     * 
     * @param message
     */
    public static void error(String message, Throwable throwable) {
        getErrorLogger().error(message, throwable);
    }

    /**
     * 调试信息
     * 
     * @param message
     */
    public static void debug(Throwable throwable) {
        getDebugLogger().debug("", throwable);
    }

    /**
     * 调试信息
     * 
     * @param message
     */
    public static void debug(String message) {
        getDebugLogger().debug(message);
    }

    /**
     * 调试信息
     * 
     * @param message
     */
    public static void debug(String message, Throwable throwable) {
        getDebugLogger().debug(message, throwable);
    }

    /**
     * info信息
     * 
     * @param message
     */
    public static void info(Throwable throwable) {
        getInfoLogger().info("", throwable);
    }

    /**
     * info信息
     * 
     * @param message
     */
    public static void info(String message) {
        getInfoLogger().info(message);
    }

    /**
     * info信息
     * 
     * @param message
     */
    public static void info(String message, Throwable throwable) {
        getInfoLogger().info(message, throwable);
    }

    /**
     * 失败信息
     * 
     * @param message
     */
    public static void fatal(String message) {
        getFatalLogger().fatal(message);
    }

    /**
     * 失败信息
     * 
     * @param message
     */
    public static void fatal(Throwable throwable) {
        getFatalLogger().fatal("", throwable);
    }

    /**
     * 失败信息
     * 
     * @param message
     */
    public static void fatal(String message, Throwable throwable) {
        getFatalLogger().fatal(message, throwable);
    }

    /**
     * 警告信息
     * 
     * @param message
     */
    public static void warn(String message) {
        getWarnLogger().warn(message);
    }

    /**
     * 警告信息
     * 
     * @param message
     */
    public static void warn(Throwable throwable) {
        getWarnLogger().warn(null, throwable);
    }

    /**
     * 警告信息
     * 
     * @param message
     */
    public static void warn(String message, Throwable throwable) {
        getWarnLogger().warn(message, throwable);
    }

    /**
     * 是否开启Debug
     * 
     * @param message
     */
    public static boolean isDebugEnabled() {
        return Logger.getRootLogger().isDebugEnabled();
    }

    /**
     * 是开启Trace
     * 
     * @param message
     */
    public static boolean isTraceEnabled() {
        return Logger.getRootLogger().isTraceEnabled();
    }

    /**
     * 是开启Info
     * 
     * @param message
     */
    public static boolean isInfoEnabled() {
        return Logger.getRootLogger().isInfoEnabled();
    }

}
