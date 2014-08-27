/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import com.cloudking.cloudmanagerweb.util.LogUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 异常捕获拦截器
 * 
 * @author CloudKing
 * 
 */
public class ExceptionInterceptor extends AbstractInterceptor {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 拦截器的拦截方法
     * 
     * @throws Exception
     *             抛出所有异常
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            String result = invocation.invoke();
            return result;
        } catch (JPAEventException e) {
            BaseAction action = (BaseAction) invocation.getAction();
            action.getCloudContext().addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return "jpaError";
        } catch (Exception e) {
            LogUtil.error(e);
            return "systemerror";
        }
    }

}
