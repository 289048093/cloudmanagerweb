/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.template;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.FaultTolerantDAO;
import com.cloudking.cloudmanagerweb.dao.FileUploadDAO;
import com.cloudking.cloudmanagerweb.dao.TemplateDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.dao.VirtualMachineDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.FaultTolerantEntity;
import com.cloudking.cloudmanagerweb.entity.FileUploadEntity;
import com.cloudking.cloudmanagerweb.entity.TemplateEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.LoadTempateUtil;
import com.cloudking.cloudmanagerweb.util.LogUtil;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.TemplateVO;

/**
 * 模版service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("templateService")
public class TemplateService extends BaseService {
    /**
     * 模版DAO
     */
    @Resource
    private TemplateDAO templateDAO;

    /**
     * fileUploadDAO
     */
    @Resource
    private FileUploadDAO fileUploadDAO;
    /**
     * userDAO
     */
    @Resource
    private UserDAO userDAO;
    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * 虚机DAO
     */
    @Resource
    private VirtualMachineDAO virtualMachineDAO;
    /**
     * 容错DAO
     */
    @Resource
    private FaultTolerantDAO faultTolerantDAO;

    /**
     * 添加模版
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<TemplateVO> cloudContext) throws Exception {
        String domainCode = cloudContext.getVo().getDomainCode();
        DomainEntity domain = domainDAO.findByCode(domainCode);
        //验证
        TemplateEntity templateEntity = templateDAO.getByNameAndDomainCode(cloudContext.getVo().getName(), domainCode);
        //如果当前模版已经存在，并且关联了当前域或者前代的域，则不让创建
        if (templateEntity != null) {
            cloudContext.addErrorMsg(String.format("名字为【%1$s】的模板已经在当前域存在", cloudContext.getVo().getName()));
            return;
        }
        templateEntity = new TemplateEntity();
        cloudContext.getVo().setAddTime(new Date());
        BeanUtils.copyProperties(cloudContext.getVo(), templateEntity);
        templateEntity.setDomain(domain);
        //根据ISO类型设置状态
        if (Constant.TEMPLATE_TYPE_LOCAL.equals(templateEntity.getType())) {
            templateEntity.setStatus(Constant.TEMPLATE_STATUS_OK);
            templateDAO.insert(templateEntity);
            insertEventLog(String.format("创建模版【%1$s】", templateEntity.getName()), domain.getId(), cloudContext);
        } else if (Constant.TEMPLATE_TYPE_DOWNLOAD.equals(templateEntity.getType())) {
            String url = cloudContext.getVo().getUrl();
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            templateEntity.setFileName(fileName);
            templateEntity.setStatus(Constant.TEMPLATE_STATUS_DOWNLOADING);
            templateDAO.insert(templateEntity);
            //容错检查
            FaultTolerantEntity ftEntity = new FaultTolerantEntity();
            ftEntity.setType(Constant.TEMPLATE_FAULT_TOTERANT);
            ftEntity.setRefid(templateEntity.getId());
            faultTolerantDAO.insert(ftEntity);
            insertEventLog(String.format("创建模版【%1$s】", templateEntity.getName()), domain.getId(), cloudContext);
            try {
                LoadTempateUtil.downloadTmplate(templateEntity.getId(), domainCode, templateEntity.getFileName(),
                                cloudContext.getVo().getUrl());
            } catch (Exception e) {
                templateDAO.delete(templateEntity);
                LogUtil.error(e);
                cloudContext.addErrorMsg(e.getMessage());
                return;
            }finally{
                faultTolerantDAO.delete(ftEntity);
            }
        }
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
    public void query(CloudContext<TemplateVO> cloudContext) throws Exception {
        Long currentDomainId = cloudContext.getLongParam("qDomain");
        Set<TemplateVO> queryResult = new HashSet<TemplateVO>();
        List<DomainEntity> domains = domainDAO.queryByUserId(cloudContext.getLoginedUser().getId());
        if (currentDomainId != null) {
            DomainEntity currentDomain = domainDAO.get(currentDomainId);
            queryTemplate(currentDomain, queryResult, domains, cloudContext);
        } else {//TODO  ..优化sql
            for (DomainEntity e : domains) {
                queryTemplate(e, queryResult, domains, cloudContext);
            }
        }
        List<TemplateVO> list = new ArrayList<TemplateVO>(queryResult);
        Collections.sort(list);
        //结果集

        cloudContext.addParam("templates", list);
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
     * 
     * @param domain
     * @param queryResult
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    private void queryTemplate(DomainEntity domain, Set<TemplateVO> queryResult, List<DomainEntity> domains,
                    CloudContext<TemplateVO> cloudContext) throws Exception {
        if (cloudContext.getBooleanParam("queryAll")) {
            //总数据数
            cloudContext.getPageInfo().setDataCount(
                            templateDAO.getQueryCountAllPrev(cloudContext.getStringParam("qName"), domain.getCode()));
        } else {
            //总数据数
            cloudContext.getPageInfo().setDataCount(
                            templateDAO.getQueryCount(cloudContext.getStringParam("qName"), domain.getId()));
        }
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<Object[]> objess = null;
            if (cloudContext.getBooleanParam("queryAll")) {
                objess = templateDAO.queryAllPrev(cloudContext.getStringParam("qName"), domain.getCode(), cloudContext
                                .getPageInfo());
            } else {
                objess = templateDAO.query(cloudContext.getStringParam("qName"), domain.getId(), cloudContext
                                .getPageInfo());
            }
            TemplateVO templateVO = null;
            TemplateEntity templateEntity = null;
            DomainEntity domainEntity = null;
            for (Object[] objs : objess) {
                templateVO = new TemplateVO();
                templateEntity = (TemplateEntity) objs[0];
                domainEntity = (DomainEntity) objs[1];
                BeanUtils.copyProperties(templateEntity, templateVO);
                templateVO.setDomainName(domainEntity.getName());
                //如果是查询当前域下的
                templateVO.setDomainId(domainEntity.getId());
                if (cloudContext.getBooleanParam("queryAll")) {
                    templateVO.setSelfTemplate(domains.contains(domainEntity));
                } else {
                    templateVO.setSelfTemplate(true);
                }
                queryResult.add(templateVO);
            }
        }
    }

    /**
     * 初始化上传
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initUpload(CloudContext<TemplateVO> cloudContext) throws Exception {
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
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<TemplateVO> cloudContext) throws Exception {
        TemplateEntity template = templateDAO.get(cloudContext.getVo().getId());
        if (!userDAO.findByTemplateId(template.getId()).getId().equals(cloudContext.getLoginedUser().getId())) {
            cloudContext.addErrorMsg("只能删除当前下的模版");
            return;
        }
        if (virtualMachineDAO.getVmCountByTemplate(template.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，有虚拟机使用此模版");
            return;
        }
        if (cloudContext.getBooleanParam("deleteFile")) {
            //如果是模版是下载的，就将文件删除
            File domainTmplateDir = new File(ProjectUtil.getTemplateDir(), template.getDomain().getCode());
            if (domainTmplateDir.exists()) {
                File templateFile = new File(domainTmplateDir, template.getFileName());
                if (templateFile.exists()) {
                    if (!templateFile.delete()) {
                        cloudContext.addWarnMsg("镜像文件删除失败;");
                    }
                } else {
                    cloudContext.addWarnMsg("镜像文件不存在");
                }
            } else {
                cloudContext.addWarnMsg("镜像文件夹不存在");
            }
        }
        templateDAO.delete(template);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog(String.format("删除模版【%1$s】", template.getName()), template.getDomain().getId(), cloudContext);
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<TemplateVO> cloudContext) throws Exception {
        TemplateEntity templateEntity = templateDAO.get(cloudContext.getVo().getId());
        if (templateEntity == null) {
            cloudContext.addErrorMsg("模版不存在！");
            return;
        }
        if (!templateEntity.getDomain().getId().equals(cloudContext.getLoginedUser().getDomainID())) {
            cloudContext.addErrorMsg("只能修改当前域下的模版");
            return;
        }
        templateEntity.setDesc(cloudContext.getVo().getDesc());
        templateEntity.setUsername(cloudContext.getVo().getUsername());
        templateEntity.setPassword(cloudContext.getVo().getPassword());
        templateDAO.update(templateEntity);
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
    public void initAddOrUpdate(CloudContext<TemplateVO> cloudContext) throws Exception {
        //获取镜像文件名
        queryImg(cloudContext.getLoginedUser().getDomainCode(), cloudContext);
        //获取用户的域
        TemplateVO templateVO = new TemplateVO();
        templateVO.setDomainCode(cloudContext.getLoginedUser().getDomainCode());
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
            TemplateEntity templateEntity = templateDAO.get(cloudContext.getVo().getId());
            if (templateEntity == null) {
                cloudContext.addErrorMsg("模版不存在！");
                return;
            }
            BeanUtils.copyProperties(templateEntity, templateVO);
        }
        cloudContext.addParam("dataVo", templateVO);
    }

    /**
     * 切换域 获取相关数据
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryData4ChangeDomain(CloudContext<TemplateVO> cloudContext) throws SQLException {
        queryImg(cloudContext.getVo().getDomainCode(), cloudContext);
    }

    /**
     * 查找指定域下的镜像
     * 
     * @param domainCode
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    private void queryImg(String domainCode, CloudContext<TemplateVO> cloudContext) throws SQLException {
        //获取镜像文件名
        List<String> fileNames = new ArrayList<String>();
        File tempDir = ProjectUtil.getTemplateDir();
        File currentDomainTempDir = new File(tempDir.getAbsolutePath() + File.separator + domainCode);
        List<String> usedFileNames = templateDAO.queryUsedFileNamesByDomainCode(domainCode);
        File[] files = currentDomainTempDir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".img"));
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (!usedFileNames.contains(file.getName())) {
                    fileNames.add(file.getName());
                }
            }
        }
        cloudContext.addParam("fileList", fileNames);
    }

    /**
     * 查询文件上传表是否存在相同的文件名
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void queryFileExsit(CloudContext<TemplateVO> cloudContext) throws SQLException {
        String md5 = cloudContext.getStringParam("md5");
        Integer existCounter = fileUploadDAO.queryMD5Exsit(md5);
        cloudContext.addParam("existFlag", true);
        if (existCounter > 0) {
            cloudContext.addParam("existMD5Flag", existCounter > 0 ? true : false);
        } else {
            String filename = cloudContext.getStringParam("filename");
            String domainCode = cloudContext.getStringParam("domainCode");
            File file = new File(new File(ProjectUtil.getTemplateDir(), domainCode), filename);
            if (file.exists()) {
                cloudContext.addParam("existFileNameFlag", existCounter > 0 ? true : false);
            }
        }
    }

    /**
     * 新增文件上传表
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void insertFileUpload(CloudContext<TemplateVO> cloudContext) throws SQLException {
        String filename = cloudContext.getStringParam("filename");
        if (StringUtil.isBlank(filename)) {
            cloudContext.addErrorMsg("文件名不存在");
            return;
        }
        String md5 = cloudContext.getStringParam("md5");
        Date createTime = new Date();

        FileUploadEntity fileUploadEntity = new FileUploadEntity();
        fileUploadEntity.setFilename(filename);
        fileUploadEntity.setMd5(md5);
        fileUploadEntity.setCreateTime(createTime);

        fileUploadDAO.insert(fileUploadEntity);
        cloudContext.addSuccessMsg("操作成功");
    }

    /**
     * 删除文件上传表
     * 
     * @param cloudContext
     * @throws SQLException
     *             sql异常
     */
    public void deleteFileUpload(CloudContext<TemplateVO> cloudContext) throws SQLException {
        Long fileUploadId = cloudContext.getLongParam("fileUploadId");
        if (fileUploadId == null) {
            cloudContext.addErrorMsg("文件不存在");
            return;
        }
        fileUploadDAO.deleteById(fileUploadId);
        cloudContext.addSuccessMsg("操作成功");
    }
}
