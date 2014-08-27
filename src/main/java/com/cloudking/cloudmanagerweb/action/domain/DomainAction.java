/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.domain;

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
import com.cloudking.cloudmanagerweb.service.domain.DomainService;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;

/**
 * 域action
 * 
 * @author CloudKing
 */
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Namespace("/domainManager")
@Results( { @Result(name = "success", type = "dispatcher", location = "/domain/domain.jsp"),
                @Result(name = "error", type = "dispatcher", location = "/domain/domain.jsp"),
                @Result(name = "jump", type = "dispatcher", location = "/jump.jsp"),
                @Result(name = "input", type = "dispatcher", location = "/domain/domain.jsp") })
public class DomainAction extends BaseAction<DomainVO> {

    /**
     * 
     */
    private static final long serialVersionUID = 3483323948900166673L;
    /**
     * domainService
     */
    @Resource
    private transient DomainService domainService;

    /**
     * 
     * @exception Exception
     *                抛出所有异常
     */
    @Action("/domain")
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
        domainService.query(cloudContext);
        return SUCCESS;
    }

    /**
     * 获取菜单Json数据
     * 
     * @throws Exception
     *             所有异常
     */
    public String queryMenuJsonTree() throws Exception {
        domainService.queryMenuJsonTree(cloudContext);
        return JSON;
    }

    /**
     * 获取指定菜单权限
     * 
     * @throws Exception
     *             所有异常
     */
    public String queryRightsByMenu() throws Exception {
        domainService.queryRightsByMenu(cloudContext);
        return JSON;
    }

    /**
     * 保存授权
     * 
     * @throws Exception
     *             所有异常
     */
    public String saveAuthorization() throws Exception {
        domainService.saveAuthorization(cloudContext);
        return JSON;
    }

    /**
     * 添加域
     * 
     * @return
     * @throws Exception
     *             sql异常
     */
    public String add() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            domainService.insert(cloudContext);
        }
        return JUMP;
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String delete() throws Exception {
        domainService.delete(cloudContext);
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
            domainService.update(cloudContext);
        }
        return JUMP;
    }

    /**
     * 设置用户
     * 
     * @return
     * @exception Exception
     *                all exception
     */
    public String updateDomainUser() throws Exception {
        domainService.updateDomainUsers(cloudContext);
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
        domainService.initAddOrUpdate(cloudContext);
        return JSON;
    }

    /**
     * 自定义资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initAddPool() throws Exception {
        domainService.addPool(cloudContext);
        return JSON;
    }

    /**
     * 向导模块查询机柜
     * 
     * @param cloudContext
     * @throws Exception
     * @throws Exception
     *             所有异常
     */
    public String queryRackByRoom() throws Exception {
        domainService.queryRackByRoom(cloudContext);
        return JSON;
    }

    /**
     * 向导查询资源
     * 
     * @return
     * @throws Exception
     *             all exception
     */
    public String queryResource() throws Exception {
        domainService.queryResource(cloudContext);
        return JSON;
    }

    /**
     * 向导提交
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String guaidInsert() throws Exception {
        domainService.insert4Guaid(cloudContext);
        return JUMP;
    }

    /**
     * 选择上级时，查找域下的资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String querykDomainPoolAndStorageCapacity() throws Exception {
        domainService.queryDomainPoolAndStorageCapacity(cloudContext);
        return JSON;
    }

    /**
     * 初始化资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String initPools() throws Exception {
        domainService.initPools(cloudContext);
        return JSON;
    }

    /**
     * 设置资源池
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    public String updatePools() throws Exception {
        if (cloudContext.getSuccessIngoreWarn()) {
            domainService.updatePools(cloudContext);
        }
        return JUMP;
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
        Boolean isUpdatePools = action.lastIndexOf("updatePools.action") != -1;
        Boolean query = false;
        if (isAdd) {
            String name = req.getParameter("cloudContext.vo.name");
            String storageCapacity = req.getParameter("cloudContext.vo.storageCapacity");
            String cpuTotalNum = req.getParameter("cloudContext.vo.cpuTotalNum");
            String memoryCapacity = req.getParameter("cloudContext.vo.memoryCapacity");
            if (StringUtil.isBlank(name)) {
                cloudContext.addErrorMsg("资源池名称不能为空! \\r");
            } else if (!name.matches("^[\\w\\u4e00-\\u9fa5]{0,20}$")) {
                cloudContext.addErrorMsg("资源池名称必须为长度20以内的字母数字下划线或者汉字组成! \\r");
            }
            if (StringUtil.isBlank(storageCapacity)) {
                cloudContext.addErrorMsg("存储大小不能为空! \\r");
            } else if (!storageCapacity.matches("^\\d+$")) {
                cloudContext.addErrorMsg("存储大小必须为自然数数! \\r");
                query = true;
            }
            if (StringUtil.isBlank(cpuTotalNum)) {
                cloudContext.addErrorMsg("cpu核数不能为空! \\r");
            } else if (!cpuTotalNum.matches("^\\d+$")) {
                cloudContext.addErrorMsg("内cpu核数必须为自然数! \\r");
                query = true;
            }
            if (StringUtil.isBlank(memoryCapacity)) {
                cloudContext.addErrorMsg("内存大小不能为空! \\r");
            } else if (!memoryCapacity.matches("^\\d+$")) {
                cloudContext.addErrorMsg("内存大小必须为自然数! \\r");
                query = true;
            }
            String domainIdsStr = req.getParameter("cloudContext.params.superDomainCode");
            if (StringUtil.isBlank(domainIdsStr)) {
                cloudContext.addErrorMsg("必须选择一个上级域! \\r");
            }
        }
        if (isUpdatePools) {
            String prevPoolIdsStr = cloudContext.getStringParam("prevPoolIds");
            if (prevPoolIdsStr == null) {
                cloudContext.addErrorMsg("js参数为空：js：prevPoolIdsStr");
            }
        }
        if (query) {//如果类型不匹配就会调用 input result
            cloudContext.clearVoAndPageInfo();
            try {
                domainService.query(cloudContext);
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
    }
}
