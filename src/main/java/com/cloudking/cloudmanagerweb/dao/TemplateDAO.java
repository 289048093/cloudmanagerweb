/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cloudking.cloudmanagerweb.BaseDAO;
import com.cloudking.cloudmanagerweb.PageInfo;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 虚拟机模版DAO
 * 
 * @author CloudKing
 * 
 */
@SuppressWarnings("unchecked")
@Repository("templateDAO")
public final class TemplateDAO extends BaseDAO<TemplateEntity> {

    /**
     * 根据名字和域获取Template
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public TemplateEntity getByNameAndDomain(String name, Long domainID) throws SQLException {
        return uniqueResult("from TemplateEntity tb_tem where tb_tem.name=:name and tb_tem.domain.id=:domainID", Arrays
                        .asList("name", "domainID"), Arrays.asList((Object) name, (Object) domainID));
    }

    /**
     * 查询指定域下的模板
     * 
     * @param domainId
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<TemplateEntity> queryByDomain(Long domainId) throws SQLException {
        return (List<TemplateEntity>) list(
                        "select _template from TemplateEntity as _template inner join _template.domain as _domain where _domain.id=:domainId order by _template.addTime asc",
                        "domainId", domainId);
    }

    /**
     * 获取分页数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> query(String name, Long domainId, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select template_,domain_ from TemplateEntity template_ left join template_.domain domain_  where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  template_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //域
        if (domainId != null) {
            queryQL.append(" and  template_.domain.id=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        //排序
        queryQL.append(" order by domain_.code asc");
        List<Object[]> resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 获取分页数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<Object[]> queryAllPrev(String name, String domainCode, PageInfo pageInfo) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select template_,domain_ from TemplateEntity template_ left join template_.domain domain_  where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  template_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //域
        if (!StringUtil.isBlank(domainCode)) {
            queryQL.append(" and :domainCode like concat(template_.domain.code,'%') ");
            paramNames.add("domainCode");
            paramValues.add(domainCode);
        }
        //排序
        queryQL.append(" order by domain_.code asc");
        List<Object[]> resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo
                        .getStart(), pageInfo.getLimit());
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && pageInfo.getNowPage() > 1) {
            pageInfo.setNowPage(pageInfo.getNowPage() - 1);
            resultSet = (List<Object[]>) pageQuery(queryQL.toString(), paramNames, paramValues, pageInfo.getStart(),
                            pageInfo.getLimit());
        }
        return resultSet;
    }

    /**
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String name, Long domainId) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(template_.id) from TemplateEntity template_ left join template_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  template_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //域
        if (domainId != null) {
            queryQL.append(" and  template_.domain.id=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 获取分页数据
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCountAllPrev(String name, String domainCode) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(template_.id) from TemplateEntity template_ left join template_.domain domain_ where 1=1");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  template_.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //域
        if (!StringUtil.isBlank(domainCode)) {
            queryQL.append(" and :domainCode like concat(template_.domain.code,'%') ");
            paramNames.add("domainCode");
            paramValues.add(domainCode);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 查找出已经使用了的镜像文件
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<String> queryUsedFileNamesByDomainCode(String domainCode) throws SQLException {
        return list("select tb_t.fileName from TemplateEntity  tb_t where  tb_t.domain.code=:domainCode", "domainCode",
                        domainCode);
    }

    /**
     * @param domainCode
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<TemplateEntity> queryByDomainCode(String domainCode) throws SQLException {
        return (List<TemplateEntity>) list(
                        "select _template from TemplateEntity as _template inner join _template.domain as _domain where _domain.code=:domainCode order by _template.addTime asc",
                        "domainCode", domainCode);
    }

    /**
     * 根据名字和域获取Template
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public TemplateEntity getByNameAndDomainCode(String name, String domainCode) throws SQLException {
        return uniqueResult("from TemplateEntity tb_tem where tb_tem.name=:name and tb_tem.domain.code=:domainCode",
                        Arrays.asList("name", "domainCode"), Arrays.asList((Object) name, (Object) domainCode));
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getCountByDomainId(Long id) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(tb_t.id) from TemplateEntity tb_t where tb_t.domain.id=:domainId ", "domainId",
                        id).toString());
    }
}
