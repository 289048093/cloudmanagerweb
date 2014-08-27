/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;

import com.cloudking.cloudmanagerweb.entity.RightsEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 用户验证拦截器
 * 
 * @author CloudKing
 * 
 */
public class AuthorityInterceptor extends AbstractInterceptor {
    /**
     * 
     */
    private static final long serialVersionUID = -6206584295411567633L;

    /**
     * 系统所有权限url
     * 
     * @throws Exception
     *             所有异常
     */
    private static Set<String> rightsUrls = new HashSet<String>();

    /**
     * @throws Exception
     *             所有异常
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final String xRequestedWith = "x-requested-with";
        final String xMLHttpRequest = "XMLHttpRequest";
        final String sessionError = "sessionError";
        final String rightsError = "rightsError";
        final String applicationXjson = "application/x-json";
        if (validateExclusiveUrl(invocation)) {
            return invocation.invoke();
        }
        if (validateLogined(invocation)) {
            if (validateRights(invocation)) {
                return invocation.invoke();
            } else {
                HttpServletRequest request = ServletActionContext.getRequest();
                HttpServletResponse response = ServletActionContext.getResponse();
                String header = request.getHeader(xRequestedWith);
                if (header != null && header.equalsIgnoreCase(xMLHttpRequest)) {
                    Map<String, String> maps = new HashMap<String, String>();
                    maps.put(rightsError, rightsError);
                    response.setContentType(applicationXjson);
                    response.getWriter().write(JSONUtil.serialize(maps));
                    response.getWriter().close();
                    return "rightsAjaxError";
                } else {
                    return rightsError;
                }
            }
        } else {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpServletResponse response = ServletActionContext.getResponse();
            String header = request.getHeader(xRequestedWith);
            if (header != null && header.equalsIgnoreCase(xMLHttpRequest)) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put(sessionError, sessionError);
                response.setContentType(applicationXjson);
                response.getWriter().write(JSONUtil.serialize(maps));
                response.getWriter().close();
                return "sessionAjaxError";
            } else {
                return rightsError;
            }
        }

    }

    /**
     * 验证不包含的URL
     * 
     * @param invocation
     */
    private Boolean validateExclusiveUrl(ActionInvocation invocation) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        if (url.startsWith("/")) {
            url = url.substring(1, url.length());
        }
        return !rightsUrls.contains(url);

    }

    /**
     * 验证session是否存在
     * 
     * @param invocation
     */
    private Boolean validateLogined(ActionInvocation invocation) {
        HttpServletRequest request = ServletActionContext.getRequest();
        LoginedUser loginedUser = (LoginedUser) request.getSession().getAttribute(Constant.LOGINED_USER);
        return loginedUser != null;
    }

    /**
     * 验证权限
     * 
     * @param invocation
     */
    private Boolean validateRights(ActionInvocation invocation) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String url = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        if (!rightsUrls.contains(url)) {
            return true;
        }
        LoginedUser loginedUser = (LoginedUser) request.getSession().getAttribute(Constant.LOGINED_USER);
        return loginedUser.containRights(url);
    }

    /**
     * 初始化系统权限url
     * 
     * @param servletContext
     */
    @SuppressWarnings("unchecked")
    public static void initRightsUrl() {
        EntityManager em = ProjectUtil.getEntityManager();
        try {
            Query query = em.createQuery("from RightsEntity order by id ASC");
            List<RightsEntity> rightsEntities = query.getResultList();
            for (RightsEntity rightsEntity : rightsEntities) {
                rightsUrls.add(rightsEntity.getUrl());
            }
            rightsUrls.size();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
