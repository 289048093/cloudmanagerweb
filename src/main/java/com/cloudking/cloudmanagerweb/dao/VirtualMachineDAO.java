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
import com.cloudking.cloudmanagerweb.entity.VirtualMachineEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.StringUtil;

/**
 * 虚拟机DAO
 * 
 * @author CloudKing
 * 
 */
@Repository("virtualMachineDAO")
public final class VirtualMachineDAO extends BaseDAO<VirtualMachineEntity> {

    /**
     * 根据名字获取VirtualMachine
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public VirtualMachineEntity getByNameAndDomain(String name, Long domainID) throws SQLException {
        return uniqueResult("from VirtualMachineEntity where name=:name and domain.id=:domainID",
                        Arrays.asList(new String[] { "name", "domainID" }),
                        Arrays.asList(new Object[] { name, domainID }));
    }

    /**
     * Portal虚机返回
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public VirtualMachineEntity getByPortal(String userID, Long vmID) throws SQLException {
        return uniqueResult("select _vm from VirtualMachineEntity as _vm  where _vm.id=("
                        + "SELECT  _userVm.virtualMachine.id FROM PortalUserBinVirtualMachineEntity as _userVm "
                        + " inner JOIN _userVm.portalUser as _appuser "
                        + " inner join _userVm.virtualMachine as _vm where _appuser.userId=:userId "
                        + " and _vm.id=:vmId )", Arrays.asList(new String[] { "userId", "vmId" }),
                        Arrays.asList(new Object[] { userID, vmID }));
    }

    /**
     * 统计属于指定网络的虚拟机个数
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer queryVmCountInSpecifyNetWork(Long netWorkId) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm inner join _vm.netWork as _netWork where _netWork.id=:netWorkId",
                        "netWorkId", netWorkId).toString());
    }

    /**
     * 统计属于指定计算节点的虚拟机个数
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer queryVmCountInSpecifyComputeResource(Long computeResourceId) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm inner join _vm.computeResource as _computeResource where _computeResource.id=:computeResourceId",
                        "computeResourceId", computeResourceId).toString());
    }

    /**
     * 根据配置返回虚机个数
     * 
     * @throws SQLException
     *             sql异常
     */
    public Integer getVmCountByMachineType(Long machineTypeID) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm inner join _vm.machineType as _machineType where _machineType.id=:machineTypeID",
                        "machineTypeID", machineTypeID).toString());
    }

    /**
     * 根据模板返回虚机个数
     * 
     * @throws SQLException
     *             sql异常
     */
    public Integer getVmCountByTemplate(Long templateID) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm inner join _vm.template as _template where _template.id=:templateID",
                        "templateID", templateID).toString());
    }

    /**
     * 根据模板返回虚机个数
     * 
     * @throws SQLException
     *             sql异常
     */
    public Integer getVmCountByComputeId(Long computeId) throws SQLException {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm where _vm.computeResource.id=:computeId",
                        "computeId", computeId).toString());
    }

    /**
     * 查询给定一组id编号的虚拟机虚拟机组
     * 
     * @param name
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<VirtualMachineEntity> queryVmInSpecifyVmIds(String vmIds) throws SQLException {
        StringBuilder vmBuilder = new StringBuilder();
        if (vmIds.length() > 0) {
            String[] tmpArr = vmIds.split(",");
            for (String str : tmpArr) {
                vmBuilder.append("'" + str.trim() + "',");
            }
        }
        if (vmBuilder.length() > 0) {
            vmBuilder = vmBuilder.delete(vmBuilder.length() - 1, vmBuilder.length());
        }
        return list("from VirtualMachineEntity as _vm where _vm.id in(" + vmBuilder.toString() + ")");
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
    public List<Object> query(String name, Long machineRoomID, Long machineRackID, Long computeId, Long domainId,
                    Long userId, PageInfo pageInfo) throws SQLException { //查询所有
        StringBuilder querySQL = new StringBuilder(
                        "SELECT _vm.ID_ ,_vm.NAME_,_vm.DESC_,_vm.VMNAME_,_vm.IP_, "
                                        + " _vm.CREATEDFLAG_,_vm.CREATEDRESULTMSG_,_vm.OPERATEFAILFLAG_,_vm.ADDTIME_, "
                                        + " _cr.IP_ as CIP_, _user.REALNAME_ , CASE WHEN _userVm.ID_ IS NOT NULL THEN _portalUser.REALNAME_ ELSE '管理员' END,  "
                                        + "  CASE WHEN _userVm.ID_ IS NOT NULL THEN _userVm.DUETIME_ ELSE NULL  END,  _domain.NAME_ as domainName"
                                        + " FROM tb_virtualmachine as _vm  "
                                        + " INNER JOIN tb_domain as _domain ON _domain.ID_ = _vm.DOMAIN_ID_ "
                                        + " INNER JOIN tb_computeresource as _cr ON _cr.ID_ = _vm.COMPUTERESOURCE_ID_ "
                                        + " INNER JOIN tb_machinerack as _rack ON _rack.ID_ = _cr.MACHINERACK_ID_ "
                                        + " INNER JOIN tb_machineroom as _room ON _room.ID_ = _rack.MACHINEROOM_ID_ "
                                        + " LEFT JOIN tb_user as _user ON _user.ID_ = _vm.CREATEUSER_ID_ "
                                        + " LEFT JOIN tb_portaluserbinvirtualmachine as _userVm ON _userVm.VIRTUALMACHINE_ID = _vm.ID_ "
                                        + " LEFT JOIN tb_portaluser as _portalUser ON _portalUser.ID_ = _userVm.PORTALUSER_ID_ "
                                        + " WHERE _domain.ID_ in (select tb_d2.ID_ from tb_domain as tb_d2 where tb_d2.USER_ID_=:userId) ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        // 名字 
        if (!StringUtil.isBlank(name)) {
            querySQL.append(" and  _vm.NAME_ like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        // 计算节点
        if (computeId != null) {
            querySQL.append(" and  _cr.ID_=:computeId ");
            paramNames.add("computeId");
            paramValues.add(computeId);
        }
        //机房
        if (machineRoomID != null) {
            querySQL.append(" and  _room.ID_=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            querySQL.append(" and  _rack.ID_=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        // 计算节点
        if (computeId != null) {
            querySQL.append(" and  _cr.ID_=:computeId ");
            paramNames.add("computeId");
            paramValues.add(computeId);
        }
        //资源
        if (domainId != null) {
            querySQL.append(" and _domain.ID_=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        //排序
        querySQL.append(" order by _vm.ADDTIME_ asc limit " + pageInfo.getStart() + "," + pageInfo.getLimit());
        List<Object> resultSet = (List<Object>) listBySQL(querySQL.toString(), paramNames, paramValues);
        return resultSet;
    }

    /**
     * 查询指定计算节点下的虚拟机
     * 
     * @param resourceId
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<Object> queryVirtualMachineByComputeResourceId(Long resourceId) throws SQLException { //查询所有
        StringBuilder querySQL = new StringBuilder(
                        "SELECT _vm.ID_ ,_vm.NAME_,_vm.DESC_,_vm.VMNAME_,_vm.IP_, "
                                        + " _vm.CREATEDFLAG_,_vm.CREATEDRESULTMSG_,_vm.OPERATEFAILFLAG_,_vm.ADDTIME_, "
                                        + " _cr.IP_ as CIP_, _user.REALNAME_ , CASE WHEN _userVm.ID_ IS NOT NULL THEN _portalUser.REALNAME_ ELSE '管理员' END,  "
                                        + "  CASE WHEN _userVm.ID_ IS NOT NULL THEN _userVm.DUETIME_ ELSE NULL  END"
                                        + " FROM tb_virtualmachine as _vm  "
                                        + " INNER JOIN tb_domain as _domain ON _domain.ID_ = _vm.DOMAIN_ID_ "
                                        + " INNER JOIN tb_computeresource as _cr ON _cr.ID_ = _vm.COMPUTERESOURCE_ID_ "
                                        + " LEFT JOIN tb_user as _user ON _user.ID_ = _vm.CREATEUSER_ID_ "
                                        + " LEFT JOIN tb_portaluserbinvirtualmachine as _userVm ON _userVm.VIRTUALMACHINE_ID = _vm.ID_ "
                                        + " LEFT JOIN tb_portaluser as _portalUser ON _portalUser.ID_ = _userVm.PORTALUSER_ID_ "
                                        + " WHERE _cr.ID_=:resourceId order by _vm.ADDTIME_ asc");

        List<Object> resultSet = (List<Object>) listBySQL(querySQL.toString(), Arrays.asList("resourceId"),
                        Arrays.asList((Object) resourceId));
        return resultSet;
    }

    /**
     * 获取分页数据
     * 
     * @param long2
     * @param long1
     * @param string
     * 
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getQueryCount(String name, Long machineRoomID, Long machineRackID, Long computeId, Long domainId,
                    Long userId) throws SQLException {
        StringBuilder queryQL = new StringBuilder(
                        "select count(*) from VirtualMachineEntity _virtual where _virtual.domain.user.id=:userId ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        // 名字 
        if (!StringUtil.isBlank(name)) {
            queryQL.append(" and  _virtual.name like :name ");
            paramNames.add("name");
            paramValues.add("%" + name + "%");
        }
        //机房
        if (machineRoomID != null) {
            queryQL.append(" and  _virtual.computeResource.machineRack.machineRoom.id=:roomID ");
            paramNames.add("roomID");
            paramValues.add(machineRoomID);
        }
        //机柜
        if (machineRackID != null) {
            queryQL.append(" and  _virtual.computeResource.machineRack.id=:rackID ");
            paramNames.add("rackID");
            paramValues.add(machineRackID);
        }
        // 类型 
        if (computeId != null) {
            queryQL.append(" and  _virtual.computeResource.id=:computeId ");
            paramNames.add("computeId");
            paramValues.add(computeId);
        }
        //资源
        if (domainId != null) {
            queryQL.append(" and _virtual.domain.id=:domainId ");
            paramNames.add("domainId");
            paramValues.add(domainId);
        }
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 用in根据一批id查询
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<VirtualMachineEntity> queryByVMIDs(String ids) throws SQLException { //查询所有
        return list("from VirtualMachineEntity where id in (" + ids + ")");
    }

    /**
     * 用in根据一批id查询
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> queryVmInfoAndHostIpByVMIDsOrderByCRIP(String ids) throws SQLException { //查询所有
        return (List<Object[]>) list(
                        "select tb_cr.ip,tb_vm  from VirtualMachineEntity tb_vm inner join tb_vm.computeResource tb_cr where tb_vm.id in ("
                                        + ids + ") and tb_vm.createdFlag=:createSuccess order by tb_cr.ip",
                        Arrays.asList("createSuccess"), Arrays.asList((Object) Constant.VM_CREATE_SUCCESS));
    }

    /**
     * webservice 用户分页获取创建成功的虚拟机数据
     * 
     * @param start
     * @param pagesize
     * @return
     * @throws SQLException
     *             sql异常
     */
    public List<VirtualMachineEntity> queryByPortalUserID(String userId, Integer start, Integer limit)
                    throws SQLException { //查询所有
        StringBuilder queryQL = new StringBuilder("select _vm from PortalUserBinVirtualMachineEntity as _userVm "
                        + " inner join _userVm.portalUser as _user  inner join _userVm.virtualMachine as _vm "
                        + " where _vm.createdFlag  is not :createdFlag and _user.userId=:userId ");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("createdFlag");
        paramValues.add(Constant.VM_CREATE_FAILED);
        paramNames.add("userId");
        paramValues.add(userId);
        //排序
        queryQL.append(" order by _vm.addTime desc");
        List<VirtualMachineEntity> resultSet = (List<VirtualMachineEntity>) pageQuery(queryQL.toString(), paramNames,
                        paramValues, start, limit);
        //如果没有查到数据，并且当前页数大于1，就查找上一页数据，避免出现删除后没数据现象
        if (resultSet.size() == 0 && start > 1) {
            resultSet = (List<VirtualMachineEntity>) pageQuery(queryQL.toString(), paramNames, paramValues, start,
                            limit);
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
    public Integer getQueryCountByPortalUserID(String userId) throws SQLException {
        StringBuilder queryQL = new StringBuilder("SELECT  COUNT(*) FROM PortalUserBinVirtualMachineEntity as _userVm "
                        + " inner JOIN _userVm.portalUser as _appuser where _appuser.userId=:userId");
        //条件拼装列表
        List<String> paramNames = new ArrayList<String>();
        List<Object> paramValues = new ArrayList<Object>();
        paramNames.add("userId");
        paramValues.add(userId);
        return Integer.parseInt(uniqueResultObject(queryQL.toString(), paramNames, paramValues).toString());
    }

    /**
     * 获取域下虚机总数
     * 
     * @param domainId
     * @return
     * @throws SQLException
     *             sql异常
     */
    public Integer getVmCountByDomainId(Long domainId) throws SQLException {
        Object obj = uniqueResultObject(
                        "select count(tb_vm.id) from VirtualMachineEntity tb_vm where tb_vm.domain.id = :domainId ",
                        Arrays.asList(new String[] { "domainId" }), Arrays.asList(new Object[] { domainId }));
        return Integer.valueOf(obj.toString());
    }

    /**
     * @param id
     * @return
     * @throws Exception
     *             所有异常
     */
    public Integer getvmCountByPoolId(Long poolId) throws Exception {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm where _vm.computeResource.computeResourcePool.id=:computeId",
                        "computeId", poolId).toString());
    }

    /**
     * 获取当前域及子域的虚机总和
     * 
     * @param id
     * @return
     * @throws Exception
     *             所有异常
     */
    public Integer getvmCountByPoolIdAndDomainId(Long poolId, Long domainId) throws Exception {
        return Integer.parseInt(uniqueResultObject(
                        "select count(*) from VirtualMachineEntity as _vm where _vm.computeResource.computeResourcePool.id=:computeId and _vm.domain.id=:domainId ",
                        Arrays.asList(new String[] { "computeId", "domainId" }),
                        Arrays.asList(new Object[] { poolId, domainId })).toString());
    }
}
