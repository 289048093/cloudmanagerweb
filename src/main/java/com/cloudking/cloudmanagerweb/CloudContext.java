/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudking.cloudmanagerweb.util.LogUtil;

/**
 * CloudContext是整个项目的参数容器，所有前台到后台的的参数 或则后台到前台的参数都是通过此对象进行传递的，里面封装了很多实用的方法， 比如获取request的参数等等
 * 
 * @author CloudKing
 * @param <T>
 *            BaseVO的子类
 */
public class CloudContext<T extends BaseVO> {
    /**
     * 其他参数
     */
    private Map<String, Object> params = new HashMap<String, Object>();
    /**
     * 错误列表
     */
    private List<String> errorMsgList = new ArrayList<String>();
    /**
     * 警告列表
     */
    private List<String> warnMsgList = new ArrayList<String>();
    /**
     * 成功列表
     */
    private List<String> successMsgList = new ArrayList<String>();

    /**
     * 已登录用户
     */
    private LoginedUser loginedUser;
    /**
     * vo
     */
    private T vo;
    /**
     * 分页
     */
    private PageInfo pageInfo = new PageInfo();

    /**
     * 默认构造方法
     */
    protected CloudContext(){

    }

    /**
     * 获取分页信息对象
     * 
     * @return
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * 设置分页信息对象
     * 
     * @param pageInfo
     */
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * 获取错误的Msg信息列表
     * 
     * @return
     */
    public List<String> getErrorMsgList() {
        return errorMsgList;
    }

    /**
     * 添加错误的Msg到错误信息列表里
     * 
     * @param errorStr
     */
    public void addErrorMsg(String errorStr) {
        this.errorMsgList.add(errorStr);
    }

    /**
     * 添加警告的Msg到警告信息列表里
     * 
     * @param msg
     */
    public void addWarnMsg(String msg) {
        this.warnMsgList.add(msg);
    }

    /**
     * 获取警告的Msg信息列表
     * 
     * @return
     */
    public List<String> getWarnMsgList() {
        return warnMsgList;
    }

    /**
     * 添加成功的Msg到成功信息列表里
     * 
     * @param msg
     */
    public void addSuccessMsg(String msg) {
        this.successMsgList.add(msg);
    }

    /**
     * 获取成功的Msg信息列表
     * 
     * @return
     */
    public List<String> getSuccessMsgList() {
        return successMsgList;
    }

    /**
     * 是否成功
     * 
     * @return
     */
    public Boolean getSuccess() {
        return errorMsgList.isEmpty() && warnMsgList.isEmpty();
    }

    /**
     * 是否警告
     * 
     * @return
     */
    public Boolean getWarn() {
        return !warnMsgList.isEmpty();
    }

    /**
     * 是否错误
     * 
     * @return
     */
    public Boolean getError() {
        return !errorMsgList.isEmpty();
    }

    /**
     * 是否成功忽略警告
     * 
     * @return
     */
    public Boolean getSuccessIngoreWarn() {
        return errorMsgList.isEmpty();
    }

    /**
     * 获得当前泛型的VO对象
     * 
     * @return
     */
    public T getVo() {
        return vo;
    }

    /**
     * 设置当前泛型的VO对象
     * 
     * @param vo
     */
    public void setVo(T vo) {
        this.vo = vo;
    }

    /**
     * @return the params
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * 添加值到params
     * 
     * @param key
     * @param value
     */
    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    /**
     * 移除对象
     * 
     * @param key
     * @return
     */
    public Object removeParam(String key) {
        return params.remove(key);
    }

    /**
     * 返回Object
     * 
     * @param key
     * @return
     */
    public Object getObjectParam(String key) {
        return params.get(key);
    }

    /**
     * 返回数组
     * 
     * @param key
     * @return
     */
    public String[] getStrArrayParam(String key) {
        Object obj = params.get(key);
        if (obj instanceof String[]) {
            String[] arrayTmp = (String[]) obj;
            return arrayTmp;
        }
        return null;
    }

    /**
     * 返回单个String
     * 
     * @param key
     * @return
     */
    public String getStringParam(String key) {
        Object obj = params.get(key);
        if (obj instanceof String[]) {
            String[] params = (String[]) obj;
            if (params.length > 0) {
                return params[0];
            }
        }
        if (obj instanceof String) {
            return (String) obj;
        }

        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 返回单个Byte
     * 
     * @param key
     * @return
     */
    public Byte getByteParam(String key) {
        String tmp = getStringParam(key);
        try {
            return Byte.parseByte(tmp);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 返回单个Integer
     * 
     * @param key
     * @return
     */
    public Integer getIntegerParam(String key) {
        String tmp = getStringParam(key);
        try {
            return Integer.parseInt(tmp);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 返回单个Long
     * 
     * @param key
     * @return
     */
    public Long getLongParam(String key) {
        String tmp = getStringParam(key);
        try {
            return Long.parseLong(tmp);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 返回单个Float
     * 
     * @param key
     * @return
     */
    public Float getFloatParam(String key) {
        String tmp = getStringParam(key);
        try {
            return Float.parseFloat(tmp);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 返回单个Double
     * 
     * @param key
     * @return
     */
    public Double getDoubleParam(String key) {
        String tmp = getStringParam(key);
        try {
            return Double.parseDouble(tmp);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 返回单个Boolean
     * 
     * @param key
     * @return
     */
    public Boolean getBooleanParam(String key) {
        String tmp = getStringParam(key);
        return Boolean.parseBoolean(tmp);
    }

    /**
     * 清除VO
     */
    public void clearVO() {
        try {
            Class<T> clazz = (Class<T>) vo.getClass();
            this.setVo(clazz.newInstance());
        } catch (Exception e) {
            //此异常直接记录即可，不影响使用。
            LogUtil.error(e);
        }
    }

    /**
     * 清除PageINfo
     */
    public void clearPageInfo() {
        this.setPageInfo(new PageInfo());
    }

    /**
     * 清除VO和PageInfo
     */
    public void clearVoAndPageInfo() {
        this.clearVO();
        this.clearPageInfo();
    }

    /**
     * @param loginedUser
     *            the loginedUser to set
     */
    public void setLoginedUser(LoginedUser loginedUser) {
        this.loginedUser = loginedUser;
    }

    /**
     * 获取用户
     * 
     * @param name
     * @param value
     */
    public LoginedUser getLoginedUser() {
        return loginedUser;
    }
}
