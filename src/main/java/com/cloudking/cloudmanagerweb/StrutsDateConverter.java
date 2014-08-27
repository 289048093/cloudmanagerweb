/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import com.cloudking.cloudmanagerweb.util.LogUtil;
/**
 * Struts Date类型转换器
 * 
 * @author CloudKing
 */
public class StrutsDateConverter extends StrutsTypeConverter {
    /**
     * 默认的日期转换格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 可接手的日期格式
     */
    public final DateFormat[] acceptDateFormats = {
                    new SimpleDateFormat(DEFAULT_DATE_FORMAT),
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") };

    /**
     * String 到 日期的转换
     */
    @SuppressWarnings("unchecked")
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values[0] == null || values[0].trim().equals("")) {
            return null;
        }
        for (DateFormat format : acceptDateFormats) {
            try {
                return format.parse(values[0]);
            } catch (Exception e) {
                LogUtil.warn(e);
            }
        }
        return null;
    }

    /**
     * 日期 到 String的转换
     */
    @SuppressWarnings("unchecked")
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            try {
                return acceptDateFormats[0].format((Date) o);
            } catch (RuntimeException e) {
                return "";
            }
        }
        return "";
    }
}
