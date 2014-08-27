/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */

package com.cloudking.cloudmanagerweb;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

/**
 * Token拉按揭期
 * 
 * @author CloudKing
 * 
 */
public class StrutsTokenInterceptor extends MethodFilterInterceptor {

    /**
     * 无效返回的试图
     */
    public static final String INVALID_TOKEN_CODE = "invalid.token";

    /**
     * 
     */
    private static final long serialVersionUID = -6680894220590585506L;

    /**
     * @see com.opensymphony.xwork2.interceptor.MethodFilterInterceptor#doIntercept(com.opensymphony.xwork2.ActionInvocation)
     * @throws Exception
     *             所有异常
     */
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Intercepting invocation to check for valid transaction token.");
        }

        //see WW-2902: we need to use the real HttpSession here, as opposed to the map
        //that wraps the session, because a new wrap is created on every request
        HttpSession session = ServletActionContext.getRequest().getSession(true);

        synchronized (session) {
            //如果没有token参数，就不做token验证
            if (TokenHelper.getTokenName() != null) {
                if (!TokenHelper.validToken()) {
                    return handleInvalidToken(invocation);
                }
            }
            return handleValidToken(invocation);
        }
    }

    /**
     * Determines what to do if an invalid token is provided. If the action implements {@link ValidationAware}
     * 
     * @param invocation
     *            the action invocation where the invalid token failed
     * @return the return code to indicate should be processed
     * @throws Exception
     *             when any unexpected error occurs.
     */
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token", invocation
                        .getInvocationContext().getLocale(),
                        "The form has already been processed or no token was supplied, please try again.",
                        new Object[0]);

        if (action instanceof ValidationAware) {
            ((ValidationAware) action).addActionError(errorMessage);
        } else {
            log.warn(errorMessage);
        }

        return INVALID_TOKEN_CODE;
    }

    /**
     * Called when a valid token is found. This method invokes the action by can be changed to do something more
     * interesting.
     * 
     * @param invocation
     *            the action invocation
     * @throws Exception
     *             when any unexpected error occurs.
     */
    protected String handleValidToken(ActionInvocation invocation) throws Exception {
        return invocation.invoke();
    }
}
