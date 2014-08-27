/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.computeresourcepool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.service.computeresourcepool.ComputeResourcePoolService;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.ComputeResourcePoolVO;

/**
 * 资源池action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/computeResourcePoolManager")
@Results( {
                @Result(name = "success", type = "dispatcher", location = "/computeResourcePool/computeResourcePool.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/computeResourcePool/computeResourcePool.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/computeResourcePool/computeResourcePool.jsp") })
public class ComputeResourcePoolAction extends BaseAction<ComputeResourcePoolVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 6416779594168589278L;
    /**
     * computeResourcePoolService
     */
    @Resource
    private transient ComputeResourcePoolService computeResourcePoolService;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/computeResourcePool")
    public String execute() throws Exception {
        return SUCCESS;
    }

    /**
     * 查询
     * 
     * @throws Exception
     *             所有异常
     */
    public String query() throws Exception {
        computeResourcePoolService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 添加资源池
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            computeResourcePoolService.insert(cloudContext);
        }
        return JUMP;
    }

    /**
     * 查询指定计算节点池下的计算节点
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String queryComputeResourceByPoolId() throws Exception {
        computeResourcePoolService.queryComputeResourceByPoolId(cloudContext);
        return JSON;
    }

    /**
     * 删除计算节点
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String deleteComputeResource() throws Exception {
        computeResourcePoolService.updateSetComputeResourceFromPool(cloudContext);
        return JSON;
    }

    /**
     * 查询指定计算节点下的虚拟机
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String queryVirtualMachineByComputeResourceId() throws Exception {
        computeResourcePoolService.queryVirtualMachineByComputeResourceId(cloudContext);
        return JSON;
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String delete() throws Exception {
        computeResourcePoolService.delete(cloudContext);
        return JUMP;
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String update() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            computeResourcePoolService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 初始化新增或则修改
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddOrUpdate() throws Exception {
        computeResourcePoolService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 查询资源
     * 
     * @return
     * @throws Exception
     *             all exception
     */
    public String queryResource() throws Exception {
        computeResourcePoolService.queryResource(cloudContext);
        return JSON;
    }

    /**
     * 设置资源
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String updateComputeResources() throws Exception {
        computeResourcePoolService.updateSetComputeResource(cloudContext);
        return JUMP;
    }

    /**
     * 根据机房查找机架，级联操作
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String queryRackByRoom() throws Exception {
        computeResourcePoolService.queryRackByRoom(cloudContext);
        return JSON;
    }

    /**
     * 参数验证方法 (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#validate()
     */
    @Override
    public void validate() {
        super.validate();
        HttpServletRequest req = getRequest();
        String action = req.getRequestURI();
        Boolean isAdd = action.lastIndexOf("add.action") != -1;
        Boolean isUpdate = action.lastIndexOf("update.action") != -1;
        if (isAdd) {
            String name = req.getParameter("cloudContext.vo.name");
            String cpuRate = req.getParameter("cloudContext.vo.cpuRate");
            String memoryRate = req.getParameter("cloudContext.vo.memoryRate");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("资源池名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("资源池名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(cpuRate)) {
                cloudContext.addErrorMsg("cpu超配比例不能为空! \\r");
            } else if (!cpuRate.matches("^\\d{1,2}.?\\d*$")) {
                cloudContext.addErrorMsg("cpu超配比例必须为一个小于100的数! \\r");
            }
            if (StringUtil.isBlank(memoryRate)) {
                cloudContext.addErrorMsg("内存超配比例不能为空! \\r");
            } else if (!memoryRate.matches("^\\d{1,2}(\\.(\\d)+)?$")) {
                cloudContext.addErrorMsg("内存超配比例必须为一个小于100的数! \\r");
            }
        }
        if (isAdd || isUpdate) {
            String domainIdsStr = req.getParameter("cloudContext.params.domainIDs");
            if (StringUtil.isBlank(domainIdsStr)) {
                cloudContext.addErrorMsg("必须选择一个域! \\r");
            }
        }
    }
}
