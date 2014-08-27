/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 * 
 * @author CloudKing
 * 
 */
public final class DateUtil {
    /**
     * 默认构造方法
     */
    private DateUtil(){

    }

    /**
     * 格式化时间
     * 
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null || format == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 解析日期
     * 
     * @param source
     * @return
     */
    public static Date parseDate(String source) {
        if (source != null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(source);
            } catch (ParseException e) {
                LogUtil.error("转换格式错误");
            }
        }
        return null;

    }

    /**
     * 解析时间
     * 
     * @param source
     * @return
     */
    public static Date parseTime(String source) {
        if (source != null) {
            try {
                return new SimpleDateFormat("HH:mm:ss").parse(source);
            } catch (ParseException e) {
                LogUtil.error("转换格式错误");
            }
        }
        return null;

    }

    /**
     * 解析日期和时间
     * 
     * @param source
     * @return
     */
    public static Date parseDateTime(String source) {
        if (source != null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
            } catch (ParseException e) {
                LogUtil.error("转换格式错误");
            }
        }
        return null;

    }

    /**
     * 得到某月的第一天
     * 
     * @param monthFlag
     * @return
     */
    public static Date getMonthMinDay(Integer monthFlag) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthFlag);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH)); // 最小的一天
        return calendar.getTime();
    }

    /**
     * 得到某月的最后一天
     * 
     * @param monthFlag
     * @return
     */
    public static Date getMonthMaxDay(Integer monthFlag) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthFlag);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // 最大的一天
        return calendar.getTime();
    }
}
