/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 10, 2012  12:08:31 PM
 */
package com.cloudking.cloudmanagerweb.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;

/**
 * 域WebService
 * 
 * @author CloudKing
 */
public class DomainService {

    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * 获取所有的域
     * 
     * @return
     */
    public String queryAllDomain() {
        try {
            List<DomainEntity> domains = domainDAO.listOrderBy("code");
            List<DomainVO> result = new ArrayList<DomainVO>();
            DomainVO domainVO = null;
            for (DomainEntity domainEntity : domains) {
                domainVO = new DomainVO();
                domainVO.setId(domainEntity.getId());
                domainVO.setName(domainEntity.getName());
                result.add(domainVO);
            }
            return ProjectUtil.objectToJSON(result);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "[]";
    }
}
