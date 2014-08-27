/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.network.NetworkMode;
import com.cloudking.cloudmanager.core.network.VirtualNetwork;
import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.ComputeResourceDAO;
import com.cloudking.cloudmanagerweb.dao.NetWorkDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.entity.NetWorkEntity;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.NetWorkVO;

/**
 * 网络service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("netWorkService")
public class NetWorkService extends BaseService {
    /**
     * 网络DAO
     */
    @Resource
    private NetWorkDAO netWorkDAO;
    /**
     * 计算节点DAO
     */
    @Resource
    private ComputeResourceDAO computeResourceDAO;
    /**
     * 虚拟机DAO
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;

    /**
     * 添加网络
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<NetWorkVO> cloudContext) throws Exception {
        //验证
        NetWorkEntity netWorkEntity = netWorkDAO.getByName(cloudContext.getVo().getName());
        if (netWorkEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        netWorkEntity = new NetWorkEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), netWorkEntity);
        netWorkEntity.setAddTime(new Date());
        netWorkEntity.setRealname(ProjectUtil.createNetworkName());

        //调用core
        try {
            VirtualNetwork.createNetwork(netWorkEntity.getRealname(), netWorkEntity.getCidr(), netWorkEntity
                            .getStartIP(), netWorkEntity.getEndIP(),
                            netWorkEntity.getType().equalsIgnoreCase("nat") ? NetworkMode.nat : NetworkMode.route);
        } catch (VirtualizationException e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.warn(e);
            return;
        } catch (Exception e) {
            cloudContext.addErrorMsg(e.getMessage());
            LogUtil.error(e);
            return;
        }
        netWorkDAO.insert(netWorkEntity);
        //提示信息
        cloudContext.addSuccessMsg("添加成功!");
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<NetWorkVO> cloudContext) throws Exception {
        //结果集
        List<NetWorkVO> queryResult = new ArrayList<NetWorkVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(netWorkDAO.getQueryCount(cloudContext.getStringParam("qName")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<NetWorkEntity> netWorkEntitys = netWorkDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getPageInfo());
            NetWorkVO netWorkVO = null;
            for (NetWorkEntity netWorkEntity : netWorkEntitys) {
                netWorkVO = new NetWorkVO();
                BeanUtils.copyProperties(netWorkEntity, netWorkVO);
                queryResult.add(netWorkVO);
            }
        }
        cloudContext.addParam("netWorks", queryResult);
    }

    /**
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<NetWorkVO> cloudContext) throws Exception {
        NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getVo().getId());
        if (netWorkEntity == null) {
            cloudContext.addErrorMsg("网络不存在，请刷新后重试！");
            return;
        }

        //查找是否有虚拟机在使用此网络
        Integer vmInNetWork = virtualMachineDAO.queryVmCountInSpecifyNetWork(netWorkEntity.getId());
        if (vmInNetWork > 0) {
            cloudContext.addErrorMsg("尚有虚拟机使用此网络!");
            return;
        }
        //调用core
        try {
            VirtualNetwork.getVnetwork(netWorkEntity.getRealname()).delete();
        } catch (VirtualizationException e) {
            LogUtil.warn(e);
            cloudContext.addErrorMsg(e.getMessage());
            return;
        }

        netWorkDAO.delete(netWorkEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<NetWorkVO> cloudContext) throws Exception {
        NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getVo().getId());
        if (netWorkEntity == null) {
            cloudContext.addErrorMsg("网络不存在！");
            return;
        }
        netWorkEntity.setDesc(cloudContext.getVo().getDesc());
        netWorkDAO.update(netWorkEntity);
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
    public void initAddOrUpdate(CloudContext<NetWorkVO> cloudContext) throws Exception {
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            NetWorkEntity netWorkEntity = netWorkDAO.get(cloudContext.getVo().getId());
            if (netWorkEntity == null) {
                cloudContext.addErrorMsg("网络不存在！");
                return;
            }
            NetWorkVO netWorkVO = new NetWorkVO();
            BeanUtils.copyProperties(netWorkEntity, netWorkVO);
            cloudContext.addParam("dataVo", netWorkVO);
        }
    }
}
