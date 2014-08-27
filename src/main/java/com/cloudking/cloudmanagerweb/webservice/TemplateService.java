/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 10, 2012  12:08:31 PM
 */
package com.cloudking.cloudmanagerweb.webservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;

import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.TemplateVO;

/**
 * 模板WebService
 * 
 * @author CloudKing
 */
public class TemplateService {

    /**
     * templateDAO
     */
    @Resource
    TemplateDAO templateDAO;

    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * 获取域所有的模板
     * 
     * @return
     */
    public String queryAllTemplate(Long domainId) {
        try {
            List<TemplateVO> result = new ArrayList<TemplateVO>();
            DomainEntity domainEntity = domainDAO.get(domainId);
            if (domainEntity == null) {
                return ProjectUtil.objectToJSON(result);
            }
            List<TemplateVO> templates = querySuperDomainTemplates(domainEntity.getCode());
            return ProjectUtil.objectToJSON(templates);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "[]";
    } 

    /**
     * 查询所有上级域下的模版（包括自己）
     * 
     * @param templates
     * @param domainCode
     * @throws SQLException
     *             sql异常
     */
    private List<TemplateVO> querySuperDomainTemplates(String domainCode)
                    throws SQLException {
        List<TemplateVO> templates = new ArrayList<TemplateVO>();
        List<TemplateEntity> tmpTemplateEntitys = templateDAO.queryByDomainCode(domainCode);
        TemplateVO vo = null;
        for (TemplateEntity entity : tmpTemplateEntitys) {
            vo = new TemplateVO();
            BeanUtils.copyProperties(entity, vo);
            templates.add(vo);
        }
        while (domainCode.length() > 2) {
            domainCode = domainCode.substring(0, domainCode.length() - 2);
            tmpTemplateEntitys = templateDAO.queryByDomainCode(domainCode);
            for (TemplateEntity entity : tmpTemplateEntitys) {
                vo = new TemplateVO();
                BeanUtils.copyProperties(entity, vo);
                templates.add(vo);
            }
        }
        return templates;
    }
}
