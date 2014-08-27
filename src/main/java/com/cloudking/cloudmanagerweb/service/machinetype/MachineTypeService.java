/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.machinetype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MachineTypeDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MachineTypeEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MachineTypeVO;

/**
 * 配置service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("machineTypeService")
public class MachineTypeService extends BaseService {
    /**
     * 配置DAO
     */
    @Resource
    private MachineTypeDAO machineTypeDAO;

    /**
     * 虚机DAO
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;

    /**
     * domainDAO
     */
    @Resource
    private DomainDAO domainDAO;

    /**
     * userDAO
     */
    @Resource
    private UserDAO userDAO;

    /**
     * 添加机型
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<MachineTypeVO> cloudContext) throws Exception {
        //验证
        MachineTypeEntity machineTypeEntity = machineTypeDAO.getByName(cloudContext.getVo().getName());
        if (machineTypeEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        DomainEntity domain = domainDAO.get(cloudContext.getVo().getDomainId());
        machineTypeEntity = new MachineTypeEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), machineTypeEntity);
        machineTypeEntity.setAddTime(new Date());
        machineTypeEntity.setDomain(domain);
        machineTypeDAO.insert(machineTypeEntity);
        //提示信息
        cloudContext.addSuccessMsg("添加成功!");
        insertEventLog(String.format("创建配置【%1$s】", machineTypeEntity.getName()), domain.getId(), cloudContext);
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<MachineTypeVO> cloudContext) throws Exception {
        Long loginedUserId = cloudContext.getLoginedUser().getId();
        UserEntity loginedUser = userDAO.get(loginedUserId);
        Long currentDomainId = cloudContext.getLongParam("qDomain");
        DomainEntity currentDomain = null;
        Set<MachineTypeVO> queryResult = new HashSet<MachineTypeVO>();
        List<DomainEntity> domains = loginedUser.getDomains();
        if (currentDomainId != null) {
            currentDomain = domainDAO.get(currentDomainId);
            //总数据数
            queryTemplate(currentDomain, queryResult, domains, cloudContext);
        } else {
            for (DomainEntity e : domains) {
                //总数据数
                queryTemplate(e, queryResult, domains, cloudContext);
            }
        }
        List<MachineTypeVO> list = new ArrayList<MachineTypeVO>(queryResult);
        Collections.sort(list);
        //结果集
        cloudContext.addParam("machineTypes", list);
        //查询域列表
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOs);
    }

    /**
     * 根据指定域查询模版
     * 
     * @param currentDomain
     * @param queryResult
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void queryTemplate(DomainEntity currentDomain, Set<MachineTypeVO> queryResult, List<DomainEntity> domains,
                    CloudContext<MachineTypeVO> cloudContext) throws Exception {
        if (cloudContext.getBooleanParam("queryAll")) {
            cloudContext.getPageInfo().setDataCount(
                            machineTypeDAO.getQueryCountAllPrev(cloudContext.getStringParam("qName"), currentDomain
                                            .getCode()));
        } else {
            cloudContext.getPageInfo().setDataCount(
                            machineTypeDAO.getQueryCount(cloudContext.getStringParam("qName"), currentDomain.getId()));
        }
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objses = machineTypeDAO.query(cloudContext.getStringParam("qName"), currentDomain.getId(),
                            cloudContext.getPageInfo());
            if (cloudContext.getBooleanParam("queryAll")) {
                objses = machineTypeDAO.queryAllPrev(cloudContext.getStringParam("qName"), currentDomain.getCode(),
                                cloudContext.getPageInfo());
            }
            MachineTypeVO machineTypeVO = null;
            MachineTypeEntity machineType = null;
            DomainEntity domainEntity = null;
            for (Object[] objs : objses) {
                machineTypeVO = new MachineTypeVO();
                machineType = (MachineTypeEntity) objs[0];
                domainEntity = (DomainEntity) objs[1];
                BeanUtils.copyProperties(machineType, machineTypeVO);
                machineTypeVO.setDomainName(domainEntity.getName());
                //如果是查询当前域下的
                machineTypeVO.setDomainId(domainEntity.getId());
                if (cloudContext.getBooleanParam("queryAll")) {
                    machineTypeVO.setSelfMachineType(domains.contains(domainEntity));
                } else {
                    machineTypeVO.setSelfMachineType(true);
                }
                queryResult.add(machineTypeVO);
            }
        }
    }

    /**
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<MachineTypeVO> cloudContext) throws Exception {
        MachineTypeEntity machineType = machineTypeDAO.get(cloudContext.getVo().getId());
        if (machineType == null) {
            cloudContext.addErrorMsg("配置不存在");
            return;
        }
        if (!userDAO.findByMachineTypeId(machineType.getId()).getId().equals(cloudContext.getLoginedUser().getId())) {
            cloudContext.addErrorMsg("只能删除自己的配置");
            return;
        }
        if (virtualMachineDAO.getVmCountByMachineType(machineType.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，有虚拟机使用此配置");
            return;
        }
        machineTypeDAO.deleteById(cloudContext.getVo().getId());
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog(String.format("删除模版【%1$s】", machineType.getName()), machineType.getDomain().getId(),
                        cloudContext);
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<MachineTypeVO> cloudContext) throws Exception {
        MachineTypeEntity machineTypeEntity = machineTypeDAO.get(cloudContext.getVo().getId());
        if (machineTypeEntity == null) {
            cloudContext.addErrorMsg("配置不存在！");
            return;
        }
        if (!machineTypeEntity.getDomain().getId().equals(cloudContext.getLoginedUser().getDomainID())) {
            cloudContext.addErrorMsg("只能修改当前域下的机型");
            return;
        }
        machineTypeEntity.setDesc(cloudContext.getVo().getDesc());
        machineTypeDAO.update(machineTypeEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdae(CloudContext<MachineTypeVO> cloudContext) throws Exception {
        MachineTypeVO machineTypeVO = new MachineTypeVO();
        machineTypeVO.setDomainId(cloudContext.getLoginedUser().getDomainID());
        List<DomainEntity> domainEntitys = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity e : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(e, domainVO);
            domainVOs.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOs);
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            MachineTypeEntity machineTypeEntity = machineTypeDAO.get(cloudContext.getVo().getId());
            if (machineTypeEntity == null) {
                cloudContext.addErrorMsg("配置不存在！");
                return;
            }
            machineTypeVO = new MachineTypeVO();
            BeanUtils.copyProperties(machineTypeEntity, machineTypeVO);
        }
        cloudContext.addParam("dataVo", machineTypeVO);
    }
}
