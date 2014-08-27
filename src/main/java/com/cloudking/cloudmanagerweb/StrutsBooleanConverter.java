/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

/**
 * Struts Boolean类型转换器
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
public class StrutsBooleanConverter extends StrutsTypeConverter {
    /**
     * String到Boolean的转换
     */
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values[0] == null || values[0].trim().equals("")) {
            return null;
        }
        return Boolean.parseBoolean(values[0]);
    }

    /**
     * 
     * Boolean到String的转换
     */
    public String convertToString(Map context, Object o) {
        return o.toString();
    }
}
