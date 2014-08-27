/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 负责创建Action的基类，所有的action都继承改类。 createAction()返回的action的excute方法 不会执行拦截器。
 * proxy的excute方法会执行拦截器。
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unchecked")
public abstract class BaseActionTest {
//    private static final String WEB_APP_ROOT_KEY = "openlab.root";
//    private static final String CONFIG_LOCATIONS = "applicationContext-test.xml";
//    private static final String STRUTS_ACTION_MAPPING_KEY = "struts.actionMapping";
//    protected ApplicationContext applicationContext;
//    protected MockServletContext servletContext;
//    protected ActionMapping mapping;
//    protected MockHttpServletRequest request;
//    protected MockHttpServletResponse response;
//    protected MockHttpSession session;
//    private Dispatcher dispatcher;
//    protected ActionProxy proxy;
//
//    /**
//     * 初始化配置信息
//     * 
//     * @throws Exception
//     */
//    @BeforeSuite
//    public void initial() throws Exception {
////        if (applicationContext == null) {
////            //设置log文件输出目录
////            System.setProperty(WEB_APP_ROOT_KEY, Thread.currentThread().getContextClassLoader().getResource("")
////                    .getPath());
////
////            //初始化一个servlet上下文
////            servletContext = new MockServletContext();
////            servletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, CONFIG_LOCATIONS);
////
////            //spring的上下文
////            applicationContext = (new ContextLoader()).initWebApplicationContext(servletContext);
////
////            //
//////            servletContext.setContextPath(contextPath)
////
////            // Struts JSP support servlet (for Freemarker)
////            new JspSupportServlet().init(new MockServletConfig(servletContext));
////        }
////        //初始化Property配置
////        PropertyManager.getInstance().initProperty();
////        UrlInterceptorManager.getInstance().initExcludeAuthUrls();
////        //初始化数据库
////        if (!Boolean.parseBoolean(PropertyManager.getInstance().getXMLProperty(
////                PropertyManager.OPENLAB_TESTCONFIG_INITED))) {
////            initDataBase();
////        }
////
////        // 相当于web.xml的FilterDispatcher ，所有的请求都是通过这个调度器调度的。
////        //params相当于 <init-param></init-param>的值
////        HashMap<String, String> params = new HashMap<String, String>();
////        dispatcher = new Dispatcher(servletContext, params);
////        dispatcher.init();
////        Dispatcher.setInstance(dispatcher);
//    }
//
//    /**
//     * 获取DAO
//     */
//    protected <T> T getDao(Class<T> clazz, String daoName) throws Exception {
//        return (T) applicationContext.getBean("userDAO");
//    }
//
//    /**
//     * 初始化数据库信息
//     * 
//     * @throws Exception
//     */
//    private void initDataBase() throws Exception {
////        //dao随便获取
////        UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");
////        Session session = userDAO.obtainSession();
////        session.beginTransaction();
////        try {
////            RoleEntity roleEntity = new RoleEntity();
////            RoleEntity systemRole = roleEntity;
////            roleEntity.setName("系统管理员");
////            roleEntity.setDesc("系统管理员");
////            roleEntity.setPersistence(Constant.ROLE_PERSISTENCE_YES);
////            session.save(roleEntity);
////            roleEntity = new RoleEntity();
////            roleEntity.setName("运维主管");
////            roleEntity.setDesc("运维主管");
////            roleEntity.setPersistence(Constant.ROLE_PERSISTENCE_YES);
////            session.save(roleEntity);
////            roleEntity = new RoleEntity();
////            roleEntity.setName("主机管理员");
////            roleEntity.setDesc("主机管理员");
////            roleEntity.setPersistence(Constant.ROLE_PERSISTENCE_YES);
////            session.save(roleEntity);
////            roleEntity = new RoleEntity();
////            roleEntity.setName("数据库管理员");
////            roleEntity.setDesc("数据库管理员");
////            roleEntity.setPersistence(Constant.ROLE_PERSISTENCE_YES);
////            session.save(roleEntity);
////            roleEntity = new RoleEntity();
////            roleEntity.setName("网络管理员");
////            roleEntity.setDesc("网络管理员");
////            roleEntity.setPersistence(Constant.ROLE_PERSISTENCE_YES);
////            session.save(roleEntity);
////            UserEntity userEntity = null;
////            Set<UserEntity> users = new HashSet<UserEntity>();
////            for (int i = 0; i < 600; i++) {
////                userEntity = new UserEntity();
////                userEntity.setUsername(String.format("test%1$d", i));
////                userEntity.setEmail(String.format("test%1$d@test.com", i));
////                userEntity.setPassword(StringUtil.encrypt(userEntity.getUsername(), userEntity.getUsername()));
////                userEntity.setMobilePhone(String.format("%1$d", i));
////                userEntity.setSex(Constant.USER_SEX_MAN);
////                userEntity.setTelPhone(String.format("%1$d", i));
////                userEntity.setState(Constant.USER_NORMAL_STATE);
////                userEntity.setAddTime(new Date());
////                users.add(userEntity);
////                session.save(userEntity);
////            }
////            systemRole.setUsers(users);
////            session.save(woCategoryEntity);
////            session.getTransaction().commit();
////            PropertyManager.getInstance().setXMLProperty(PropertyManager.TRAFFICMONITOR_TESTCONFIG_INITED, "true");
////        } catch (Exception e) {
////            PropertyManager.getInstance().setXMLProperty(PropertyManager.TRAFFICMONITOR_TESTCONFIG_INITED, "false");
////            session.getTransaction().rollback();
////            throw e;
////        } finally {
////            session.close();
////        }
//    }
//
//    /**
//     * 创建一个action，带request参数，和session ，不会执行拦截器
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected <T> T createAction(Class<T> clazz, String namespace, String name,
//                    String method, Map<String, String> requestParams,
//                    Map<String, Object> sessionAttrs) throws Exception {
//        ActionProxy actionProxy = createActionProxy(namespace, name, method,
//                        requestParams, sessionAttrs);
//        return (T) actionProxy.getAction();
//    }
//
//    /**
//     * 创建一个action，只带request 参数 ，不会执行拦截器
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected <T> T createAction(Class<T> clazz, String namespace, String name,
//                    String method, Map<String, String> requestParams)
//                    throws Exception {
//        return createAction(clazz, namespace, name, method, requestParams, null);
//    }
//
//    /**
//     * 创建一个action，不带request,session参数 ，不会执行拦截器
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected <T> T createAction(Class<T> clazz, String namespace, String name,
//                    String method) throws Exception {
//        return createAction(clazz, namespace, name, method, null, null);
//    }
//
//    /**
//     * 创建一个ActionProxy 带request参数，和session， 会执行拦截器。
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected ActionProxy createActionProxy(String namespace, String name,
//                    String method, Map<String, String> requestParams,
//                    Map<String, Object> sessionAttrs) throws Exception {
//
////        request = new MockHttpServletRequest();
////
////        //设置requestURI
////        String strutsExtension = Dispatcher.getInstance().getConfigurationManager().getConfiguration().getContainer()
////                .getInstance(String.class, StrutsConstants.STRUTS_ACTION_EXTENSION);
////        request.setRequestURI(namespace + "/" + name + "!" + method + "." + strutsExtension);
////
////        //Action注入参数需要
////        Map<String, Object> params = new TreeMap<String, Object>();
////
////        //设置request参数
////        if (requestParams != null && !requestParams.isEmpty()) {
////            request.setParameters(requestParams);
////            params.putAll(requestParams);
////        }
////
////        //设置session参数
////        session = (MockHttpSession) request.getSession();
////        if (sessionAttrs != null && !sessionAttrs.isEmpty()) {
////            for (Map.Entry<String, Object> entry : sessionAttrs.entrySet()) {
////                session.setAttribute(entry.getKey(), entry.getValue());
////            }
////        }
////
////        //设置mapping 
////        mapping = dispatcher.getContainer().getInstance(ActionMapper.class).getMapping(request,
////                dispatcher.getConfigurationManager());
////
////        //设置注入参数的容器
////        mapping.setParams(params);
////        request.setAttribute(STRUTS_ACTION_MAPPING_KEY, mapping);
////
////        //额外上下文，保证request的参数能被注入
////        Map<String, Object> extraContext = dispatcher.createContextMap(request, response, mapping, servletContext);
////
////        //创建一个指定action(由于namespace，name指定)对象
////        proxy = dispatcher.getContainer().getInstance(ActionProxyFactory.class).createActionProxy(namespace, name,
////                method, extraContext, true, false);
////
////        request.setAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY, proxy.getInvocation().getStack());
////
////        //设置action的上下文
////        ServletActionContext.setContext(proxy.getInvocation().getInvocationContext());
////
////        // by default, don't pass in any request parameters
////        proxy.getInvocation().getInvocationContext().setParameters(new HashMap<String, Object>());
////
////        response = new MockHttpServletResponse();
////        ServletActionContext.setRequest(request);
////        ServletActionContext.setResponse(response);
////        ServletActionContext.setServletContext(servletContext);
////        return proxy;
//        return null;
//    }
//
//    /**
//     * 创建一个ActionProxy， 会执行拦截器。 ，只带request 参数 .
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected ActionProxy createActionProxy(String namespace, String name,
//                    String method, Map<String, String> requestParams)
//                    throws Exception {
//        return createActionProxy(namespace, name, method, requestParams, null);
//    }
//
//    /**
//     * 创建一个ActionProxy， 会执行拦截器。 不带request,session参数
//     * 
//     * @param clazz
//     * @param namespace
//     * @param name
//     * @param method
//     * @return
//     * @throws Exception
//     */
//    protected ActionProxy createActionProxy(String namespace, String name,
//                    String method) throws Exception {
//        return createActionProxy(namespace, name, method, null, null);
//    }
    
    public static void main(String[] args) throws Exception {
        FileUtils.copyFile(new File(""), new File(""));
    }
}
