/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.util.encoders.Hex;

/**
 * 字符串工具类
 * 
 * @author CloudKing
 */
public final class StringUtil {

    /**
     * 默认的构造方法
     */
    private StringUtil(){
    }

    /**
     * 判断字符串是否为null或则是否为""字符串
     * 
     * @param str
     *            要验证的字符处
     * @return
     */
    public static Boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * md5加密
     * 
     * @param str
     * @return
     */
    public static String encrypt(String username, String password) {
        StringBuffer sb = new StringBuffer();
        sb.append(username).append(password);
        try {
            if (sb.length() == 0) {
                return "";
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return new String(Hex.encode(md5.digest(sb.toString().getBytes("utf-8"))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 替换空格
     * 
     * @param s
     * @return
     */
    public static String replaceBlank(String s) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(s);
        return m.replaceAll("");
    }

    /**
     * 返回数组处理后的以逗号分隔数组元素的字符串
     * 
     * @param strs
     * @return
     */
    public static String getStringByComma(String... strs) {
        if (null != strs && strs.length > 0) {
            List<String> list = Arrays.asList(strs);
            return getStringByComma(list);
        } else {
            return null;
        }
    }

    /**
     * 返回数组处理后的以逗号分隔数组元素的字符串
     * 
     * @param strs
     * @return
     */
    public static String getStringByComma(Collection<String> strs) {
        if (null != strs && !strs.isEmpty()) {
            String s = strs.toString();
            s = s.substring(1, s.length() - 1);
            return replaceBlank(s);
        } else {
            return null;
        }
    }

    /**
     * 数字转换中文汉字
     */
    public static String getChineseNumber(int number) {
        String str = "一";
        switch (number) {
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            case 6:
                str = "六";
                break;
            case 7:
                str = "七";
                break;
            case 8:
                str = "八";
                break;
            case 9:
                str = "九";
                break;
            case 10:
                str = "十";
                break;
            default:
                str = "一";
                break;
        }
        return str;
    }

    /**
     * 生成GUID
     * 
     * @param str
     * @return
     */
    public static String createGUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 计算千分比
     */
    public static String permillage(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        if (p1 == 0.0) {
            return "0.00";
        }
        if (p2 == 0.0 && p1 != 0.0) {
            return "-";
        }
        DecimalFormat df1 = new DecimalFormat("0.00");
        str = df1.format(p3 * 1000);
        return str;
    }

    /**
     * 计算百分比
     */
    public static String percent(double p1, double p2) {
        String str;
        if (p1 == 0.0) {
            return "0.00";
        }
        double p3 = p1 / p2;
        DecimalFormat df1 = new DecimalFormat(".00");
        if (p3 * 100 < 1) {
            df1 = new DecimalFormat("0.00");
        }
        str = df1.format(p3 * 100);
        return str;
    }
    /**
     * 主字符串是否存在某个子字符串（忽略大小写）
     * 
     * @param mainStr
     * @param subStr
     * @return
     */
    public static Boolean containStringIgnoreCase(String mainStr, String subStr) {
        Pattern pattern = Pattern.compile(subStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mainStr);
        return matcher.find();
    }
}
