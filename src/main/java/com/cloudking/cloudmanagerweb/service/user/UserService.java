/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.LoginedUser;
import com.cloudking.cloudmanagerweb.dao.DomainDAO;
import com.cloudking.cloudmanagerweb.dao.MenuDAO;
import com.cloudking.cloudmanagerweb.dao.RightsDAO;
import com.cloudking.cloudmanagerweb.dao.UserDAO;
import com.cloudking.cloudmanagerweb.entity.DomainEntity;
import com.cloudking.cloudmanagerweb.entity.MenuEntity;
import com.cloudking.cloudmanagerweb.entity.RightsEntity;
import com.cloudking.cloudmanagerweb.entity.UserEntity;
import com.cloudking.cloudmanagerweb.util.Constant;
import com.cloudking.cloudmanagerweb.util.StringUtil;
import com.cloudking.cloudmanagerweb.vo.DomainVO;
import com.cloudking.cloudmanagerweb.vo.MenuVO;
import com.cloudking.cloudmanagerweb.vo.UserVO;

/**
 * 用户service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unused")
@Service("userService")
public class UserService extends BaseService {
    /**
     * 用户DAO
     */
    @Resource
    private UserDAO userDAO;

    /**
     * 域DAO
     */
    @Resource
    private DomainDAO domainDAO;
    /**
     * menuDAO
     */
    @Resource
    private MenuDAO menuDAO;
    /**
     * rightsDAO
     */
    @Resource
    private RightsDAO rightsDAO;

    /**
     * 注销
     * 
     * @param userVO
     * @return
     * @throws Exception
     *             所有异常
     */
    public void logout(CloudContext<UserVO> cloudContext) throws Exception {
        insertEventLog("用户注销", cloudContext);
    }

    /**
     * 登陆
     * 
     * @return
     * @throws Exception
     *             所有异常
     */
    @SuppressWarnings("unchecked")
    public void login(CloudContext<UserVO> cloudContext) throws Exception {
        String username = cloudContext.getVo().getUsername();
        String password = cloudContext.getVo().getPassword();
        String checkCode = cloudContext.getStringParam("checkCode");
        String verifyCode = cloudContext.getStringParam(Constant.VERIFY_CODE);

        if (!checkCode.equals(verifyCode)) {
            cloudContext.addErrorMsg("验证码错误");
            return;
        }
        String md5pwd = StringUtil.encrypt(username, password);
        UserEntity loginUser = userDAO.findUserByUsernameAndPassword(username, md5pwd);
        if (loginUser == null) {
            cloudContext.addErrorMsg("用户名或密码错误!");
            return;
        }
        List<DomainEntity> domains = loginUser.getDomains();
        if (domains.size() < 1) {
            cloudContext.addErrorMsg("用户没有分配域，不能登录!");
            return;
        }
        LoginedUser loginUserVO = new LoginedUser();
        loginUserVO.setId(loginUser.getId());
        loginUserVO.setLastLoginTime(new Date());
        loginUserVO.setUsername(loginUser.getUsername());
        loginUserVO.setRealname(loginUser.getRealname());

        DomainEntity domain = domains.iterator().next();
        String maxCode = domain.getCode();
        Integer maxCodeIntVal = Integer.parseInt(maxCode);
        Long domainId = domain.getId();
        String code = null;
        for (DomainEntity e : domains) {
            code = e.getCode();
            if (code.length() == maxCode.length()) {
                maxCode = Integer.parseInt(code) < maxCodeIntVal ? code : maxCode;
            } else {
                maxCode = e.getCode().length() < maxCode.length() ? e.getCode() : maxCode;
            }
            if (maxCode.equals(code)) {
                domainId = e.getId();
            }
        }

        //存放相应的权限
        List<RightsEntity> rightsEntitys = rightsDAO.queryRightsByDomain(domainId);
        StringBuilder rightsUrls = new StringBuilder();
        for (RightsEntity rightsEntity : rightsEntitys) {
            rightsUrls.append(rightsEntity.getUrl() + ",");
        }
        loginUserVO.setRights(rightsUrls.toString());
        //存放相应的菜单
        List<MenuEntity> menuEntitys = menuDAO.queryByDomain(domainId);
        MenuVO menuVO = null;
        for (MenuEntity menuEntity : menuEntitys) {
            menuVO = new MenuVO();
            BeanUtils.copyProperties(menuEntity, menuVO);
            loginUserVO.addMenu(menuVO);
        }
        List<DomainEntity> domainEntitys = loginUser.getDomains();
        DomainVO domainVO = null;
        List<DomainVO> domainVOs = new ArrayList<DomainVO>();
        for (DomainEntity domainEntity : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domainEntity, domainVO);
            domainVOs.add(domainVO);
        }
        loginUserVO.setDomains(domainVOs);
        loginUserVO.setDomainID(domainId);
        loginUserVO.setDomainCode(maxCode);
        loginUserVO.setDomainName(domain.getName());
        cloudContext.addParam(Constant.LOGINED_USER, loginUserVO);
        cloudContext.setLoginedUser(loginUserVO);
        insertEventLog("用户登录", cloudContext);
    }

    /**
     * 登录页域查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void queryDomainForLogin(CloudContext<UserVO> cloudContext) throws Exception {
        List<DomainEntity> domainEntitys = domainDAO.listOrderBy("code ASC");
        List<DomainVO> domainVOS = new ArrayList<DomainVO>();
        DomainVO domainVO = null;
        for (DomainEntity domain : domainEntitys) {
            domainVO = new DomainVO();
            BeanUtils.copyProperties(domain, domainVO);
            domainVOS.add(domainVO);
        }
        cloudContext.addParam("domains", domainVOS);
    }

    /**
     * 添加用户
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void insert(CloudContext<UserVO> cloudContext) throws Exception {
        //验证
        UserEntity userEntity = userDAO.findUserByUsername(cloudContext.getVo().getUsername());
        if (userEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】已经存在", cloudContext.getVo().getUsername()));
            return;
        }
        DomainEntity loginedDomain = domainDAO.get(cloudContext.getLoginedUser().getDomainID());
        if (loginedDomain == null) {
            cloudContext.addErrorMsg("添加失败：当前登录的用户的域不存在！");
            return;
        }
        userEntity = new UserEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), userEntity);
        userEntity.setPassword(StringUtil.encrypt(userEntity.getUsername(), userEntity.getPassword()));
        userEntity.setDeleteFlag(false);
        userEntity.setAddTime(new Date());
        //提示信息
        userDAO.insert(userEntity);
        cloudContext.addSuccessMsg("添加成功!");
        insertEventLog("新建用户:" + userEntity.getRealname(), cloudContext);
    }

    /**
     * 查询
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void query(CloudContext<UserVO> cloudContext) throws Exception {
        //结果集
        List<UserVO> queryResult = new ArrayList<UserVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(userDAO.getQueryCount(cloudContext.getStringParam("qName")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<UserEntity> userEntitys = userDAO.query(cloudContext.getStringParam("qName"), cloudContext
                            .getPageInfo());
            UserVO userVO = null;
            for (UserEntity userEntity : userEntitys) {
                userVO = new UserVO();
                BeanUtils.copyProperties(userEntity, userVO);
                queryResult.add(userVO);
            }
        }
//        //初始化域
//        cloudContext.addParam("domains", domainDAO.queryDescendantDomainByCode(cloudContext.getLoginedUser()
//                        .getDomainCode()));
        cloudContext.addParam("users", queryResult);
    }

    /**
     * 删除
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void delete(CloudContext<UserVO> cloudContext) throws Exception {
        UserEntity userEntity = userDAO.get(cloudContext.getVo().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("用户不存在！");
            return;
        }
        if (domainDAO.countByUserId(userEntity.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，当前有域和此用户关联！");
            return;
        }
        userEntity.setDeleteFlag(true);
        userDAO.update(userEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");
        insertEventLog("删除用户：" + userEntity.getRealname(), cloudContext);
    }

    /**
     * 更新
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void update(CloudContext<UserVO> cloudContext) throws Exception {
        UserEntity userEntity = userDAO.get(cloudContext.getVo().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("用户不存在！");
            return;
        }
        if (!StringUtil.isBlank(cloudContext.getVo().getPassword())) {
            String md5Pwd = StringUtil.encrypt(userEntity.getUsername(), cloudContext.getVo().getPassword());
            userEntity.setPassword(md5Pwd);
        }
        userEntity.setEmail(cloudContext.getVo().getEmail());
        userEntity.setCellPhone(cloudContext.getVo().getCellPhone());
        userEntity.setTelPhone(cloudContext.getVo().getTelPhone());
        userEntity.setRealname(cloudContext.getVo().getRealname());
        userDAO.update(userEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
        insertEventLog("修改用户：" + userEntity.getRealname(), cloudContext);
    }

    /**
     * 更新自身用户信息
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void updateSelf(CloudContext<UserVO> cloudContext) throws Exception {
        UserEntity userEntity = userDAO.get(cloudContext.getVo().getId());
        if (userEntity == null) {
            cloudContext.addErrorMsg("用户不存在！");
            return;
        }
        String oldPassword = cloudContext.getStringParam("oldPassword");
        if (!StringUtil.encrypt(userEntity.getUsername(), oldPassword).equals(userEntity.getPassword())) {
            cloudContext.addErrorMsg("原密码错误，请重新输入");
            return;
        }
        if (!StringUtil.isBlank(cloudContext.getVo().getPassword())) {
            String md5Pwd = StringUtil.encrypt(userEntity.getUsername(), cloudContext.getVo().getPassword());
            userEntity.setPassword(md5Pwd);
        }
        userEntity.setEmail(cloudContext.getVo().getEmail());
        userEntity.setCellPhone(cloudContext.getVo().getCellPhone());
        userEntity.setTelPhone(cloudContext.getVo().getTelPhone());
        userDAO.update(userEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
        insertEventLog("修改自己基本信息", cloudContext);
    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<UserVO> cloudContext) throws Exception {
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            UserEntity userEntity = userDAO.get(cloudContext.getVo().getId());
            if (userEntity == null) {
                cloudContext.addErrorMsg("用户不存在！");
                return;
            }
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userEntity, userVO);
            cloudContext.addParam("dataVo", userVO);

            List<DomainVO> domainVOs = new ArrayList<DomainVO>();
            DomainVO domainVO = null;
            for (DomainEntity domainEntity : userEntity.getDomains()) {
                domainVO = new DomainVO();
                BeanUtils.copyProperties(domainEntity, domainVO);
                domainVOs.add(domainVO);
            }
            cloudContext.addParam("ownDomains", domainVOs);
            cloudContext.addParam("isLoginedUser", cloudContext.getLoginedUser().getId().equals(
                            cloudContext.getVo().getId()));
        }
    }
}
