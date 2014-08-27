/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cloudking.cloudmanagerweb.util.LogUtil;

/**
 * 基本的DAO，包括90%的数据库操作
 * 
 * @author CloudKing
 * @param <T>
 *            BaseEntity的子类
 */
@SuppressWarnings("unchecked")
public abstract class BaseDAO<T extends BaseEntity> {
    /**
     * JPA实体管理器
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * entityClass
     */
    private Class<T> entityClass;

    /**
     * 默认的构造方法
     */
    public BaseDAO(){
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * 保存
     * 
     * @param t
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public void insert(final T t) throws SQLException {
        em.persist(t);
    }

    /**
     * 更新
     * 
     * @param t
     * @throws SQLException
     *             所有SQL异常
     */
    public void update(final T t) throws SQLException {
        em.merge(t);
    }

    /**
     * 修改根据hql names 为 null 就表示不要参数
     * 
     * @param
     * @throws SQLException
     *             所有SQL异常
     */
    public int updateByJPQL(final String jpql, final List<String> argNames, final List<Object> args)
                    throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 修改根据native sql
     * 
     * @param t
     * @throws SQLException
     *             所有SQL异常
     */
    public int updateBySQL(final String sql, final List<String> argNames, final List<Object> args) throws SQLException {
        Query query = em.createNativeQuery(sql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 增加根据hql names 为 null 就表示不要参数
     * 
     * @param
     * @throws SQLException
     *             所有SQL异常
     */
    public int insertByJPQL(final String jpql, final List<String> argNames, final List<Object> args)
                    throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 增加根据native sql
     * 
     * @param t
     * @throws SQLException
     *             所有SQL异常
     */
    public int insertBySQL(final String sql, final List<String> argNames, final List<Object> args) throws SQLException {
        Query query = em.createNativeQuery(sql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 根据对象删除
     * 
     * @param t
     * @throws SQLException
     *             所有SQL异常
     */
    public void delete(final T t) throws SQLException {
        em.remove(t);
    }

    /**
     * 根据ID删除
     * 
     * @param id
     * @throws SQLException
     *             所有SQL异常
     */
    public void deleteById(final Long id) throws SQLException {
        T t = get(id);
        if (t != null) {
            em.remove(t);
        }
    }

    /**
     * 删除根据hql names 为 null 就表示不要参数
     * 
     * @param
     * @throws SQLException
     *             所有SQL异常
     */
    public int deleteByJPQL(final String jpql, final List<String> argNames, final List<Object> args)
                    throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 删除根据native sql
     * 
     * @param t
     * @throws SQLException
     *             所有SQL异常
     */
    public int deleteBySQL(final String sql, final List<String> argNames, final List<Object> args) throws SQLException {
        Query query = em.createNativeQuery(sql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.executeUpdate();
    }

    /**
     * 删除全部
     * 
     * @param entitys
     * @throws SQLException
     *             所有SQL异常
     */
    public void deleteCollection(final Collection<T> entitys) throws SQLException {
        for (Object element : entitys) {
            em.remove(element);
        }
    }

    /**
     * 查找
     * 
     * @param id
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public T get(final Long id) throws SQLException {
        if (id == null) {
            return null;
        }
        return (T) em.find(entityClass, id);
    }

    /**
     * 查找 延迟加载
     * 
     * @param id
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public T load(final Long id) throws SQLException {
        if (id == null) {
            return null;
        }
        return (T) em.getReference(entityClass, id);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public T uniqueResult(final String jpql) throws SQLException {
        return (T) uniqueResultObject(jpql);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public T uniqueResult(final String jpql, final String name, final Object value) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add(name);
        List<Object> args = new ArrayList<Object>();
        args.add(value);
        return uniqueResult(jpql, argNames, args);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public T uniqueResult(final String jpql, final List<String> argNames, final List<Object> args) throws SQLException {
        return (T) uniqueResultObject(jpql, argNames, args);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObject(final String jpql) throws SQLException {
        return uniqueResultObject(jpql, (List<String>) null, (List<Object>) null);

    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObject(final String jpql, final String name, final Object value) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add(name);
        List<Object> args = new ArrayList<Object>();
        args.add(value);
        return uniqueResultObject(jpql, argNames, args);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObject(final String jpql, final List<String> argNames, final List<Object> args)
                    throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            //忽略
            if (LogUtil.isDebugEnabled()) {
                LogUtil.debug(e);
            }
        }
        return null;
    }

    /**
     * 根据SQL查询
     * 
     * @param sql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObjectBySQL(final String sql) throws SQLException {
        return uniqueResultObjectBySQL(sql, (List<String>) null, (List<Object>) null);

    }

    /**
     * 根据SQL查询
     * 
     * @param sql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObjectBySQL(final String sql, final String name, final Object value) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add(name);
        List<Object> args = new ArrayList<Object>();
        args.add(value);
        return uniqueResultObjectBySQL(sql, argNames, args);
    }

    /**
     * 根据SQL查询
     * 
     * @param sql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public Object uniqueResultObjectBySQL(final String sql, final List<String> argNames, final List<Object> args)
                    throws SQLException {
        Query query = em.createNativeQuery(sql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            //忽略
            if (LogUtil.isDebugEnabled()) {
                LogUtil.debug(e);
            }
        }
        return null;
    }

    /**
     * 查询所有
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public List<T> list() throws SQLException {
        return listOrderBy(null);
    }

    /**
     * 查询所有，后面的orderbyStr，不需要跟order by ， 比如查询所有UserEntity，并且根据ID 降序，那么就是 dao.list("id desc"), 根据多个字段就用排序就用list("id desc
     * ,name desc")；
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public List<T> listOrderBy(final String orderbyStr) throws SQLException {
        String jpql = " from " + entityClass.getSimpleName();
        if (orderbyStr != null) {
            jpql = "  from " + entityClass.getSimpleName() + " order by " + orderbyStr;
        }
        return list(jpql, null);
    }

    /**
     * 根据JPQL查询
     * 
     * @param jpql
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List list(final String jpql) throws SQLException {
        return list(jpql, null);
    }

    /**
     * 带一个参数的JPQL查询
     * 
     * @param jpql
     * @param value
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List list(final String jpql, final Object value) throws SQLException {
        Object[] values = { value };
        return list(jpql, values);
    }

    /**
     * 带多个参数的JPQL查询
     * 
     * @param JPQL
     * @param values
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List list(final String jpql, final String name, final Object value) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add(name);
        List<Object> args = new ArrayList<Object>();
        args.add(value);
        return list(jpql, argNames, args);
    }

    /**
     * 带需带参数的JPQL查询
     * 
     * @param jpql
     * @param values
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List list(final String jpql, final Object[] values) throws SQLException {
        Query query = em.createQuery(jpql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.getResultList();
    }

    /**
     * 指定参数名的查询
     * 
     * @param jpql
     * @param argNames
     * @param args
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List list(final String jpql, final List<String> argNames, final List<Object> args) throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.getResultList();
    }

    /**
     * 根据SQL查询
     * 
     * @param jpql
     * @param argNames
     * @param args
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List listBySQL(final String sql) throws SQLException {
        return listBySQL(sql, null, null);
    }

    /**
     * 根据SQL查询
     * 
     * @param sql
     * @param argNames
     * @param args
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List listBySQL(final String sql, final List<String> argNames, final List<Object> args) throws SQLException {
        Query query = em.createNativeQuery(sql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        return query.getResultList();
    }

    /**
     * 分页的JPQL查询
     * 
     * @param jpql
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final Integer start, final Integer limit) throws SQLException {
        String jpql = "from " + entityClass.getSimpleName();
        return pageQuery(jpql, (Object) null, start, limit);

    }

    /**
     * 分页的JPQL查询
     * 
     * @param jpql
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final String jpql, final Integer start, final Integer limit) throws SQLException {
        return pageQuery(jpql, new ArrayList<Object>(), start, limit);

    }

    /**
     * 带一个参数的分页JPQL查询
     * 
     * @param jpql
     * @param value
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final String jpql, final Object value, final Integer start, final Integer limit)
                    throws SQLException {
        List<Object> values = new ArrayList<Object>();
        if (value != null) {
            values.add(value);
        }
        return pageQuery(jpql, values, start, limit);

    }

    /**
     * 带很多参数的分页查询
     * 
     * @param jpql
     * @param values
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final String jpql, final List<Object> values, final Integer start, final Integer limit)
                    throws SQLException {
        Query query = em.createQuery(jpql);
        for (int i = 0; i < values.size(); i++) {
            query.setParameter(i, values.get(i));
        }
        query.setFirstResult(start).setMaxResults(limit);
        return query.getResultList();
    }

    /**
     * 指定参数名的分页查询
     * 
     * @param jpql
     * @param argNames
     * @param args
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final String jpql, final String name, final Object value, final Integer start,
                    final Integer limit) throws SQLException {
        List<String> argNames = new ArrayList<String>();
        argNames.add(name);
        List<Object> args = new ArrayList<Object>();
        args.add(value);
        return pageQuery(jpql, argNames, args, start, limit);

    }

    /**
     * 指定参数名的分页查询
     * 
     * @param jpql
     * @param argNames
     * @param args
     * @param start
     * @param limit
     * @return
     * @throws SQLException
     *             所有SQL异常
     */
    public List pageQuery(final String jpql, final List<String> argNames, final List<Object> args, final Integer start,
                    final Integer limit) throws SQLException {
        Query query = em.createQuery(jpql);
        if (argNames != null && args != null) {
            if (argNames.size() != args.size()) {
                throw new SQLException("Length of paramNames array must match length of values array");
            }
            for (int i = 0; i < argNames.size(); i++) {
                query.setParameter(argNames.get(i), args.get(i));
            }
        }
        query.setFirstResult(start).setMaxResults(limit);
        return query.getResultList();

    }

    /**
     * 清空EntityManager
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public void clear() throws SQLException {
        em.clear();
    }

    /**
     * 关闭EntityManager
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public void close() throws SQLException {
        em.close();
    }

    /**
     * 刷新EntityManager
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public void flush() throws SQLException {
        em.flush();
    }

    /**
     * 返回EntitiyManager
     * 
     * @throws SQLException
     *             所有SQL异常
     */
    public EntityManager getEntitiyManager() throws SQLException {
        return em;
    }
}
