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
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.MachineTypeVO;

/**
 * 机型WebService
 * 
 * @author CloudKing
 */
public class MachineTypeService {
    /**
     * machineTypeDAO
     */
    @Resource
    MachineTypeDAO machineTypeDAO;
    /**
     * domainDAO
     */
    @Resource
    DomainDAO domainDAO;

    /**
     * 获取域下所有的机型
     * 
     * @return
     */
    public String queryAllMachineType(Long domainId) {
        try {
            List<MachineTypeVO> result = new ArrayList<MachineTypeVO>();
            DomainEntity domainEntity = domainDAO.get(domainId);
            if (domainEntity == null) {
                return ProjectUtil.objectToJSON(result);
            }
            result = querySuperDomainMachinetypes(domainEntity.getCode());
            return ProjectUtil.objectToJSON(result);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return "[]";
    } 

    /**
     * 查询所有上级域下的机型（包括自己）
     * 
     * @param machineTypes
     * @param domainCode
     * @throws SQLException
     *             sql异常
     */
    private List<MachineTypeVO> querySuperDomainMachinetypes(String domainCode) throws SQLException {
        List<MachineTypeVO> machineTypes = new ArrayList<MachineTypeVO>();
        List<MachineTypeEntity> tmpEntitys = machineTypeDAO.queryByDomainCode(domainCode);
        MachineTypeVO vo = null;
        for (MachineTypeEntity entity : tmpEntitys) {
            vo = new MachineTypeVO();
            BeanUtils.copyProperties(entity, vo);
            machineTypes.add(vo);
        }
        while(domainCode.length() > 2) {
            domainCode = domainCode.substring(0, domainCode.length() - 2);
            tmpEntitys = machineTypeDAO.queryByDomainCode(domainCode);
            for (MachineTypeEntity entity : tmpEntitys) {
                vo = new MachineTypeVO();
                BeanUtils.copyProperties(entity, vo);
                machineTypes.add(vo);
            }
        }
        return machineTypes;
    }
}
