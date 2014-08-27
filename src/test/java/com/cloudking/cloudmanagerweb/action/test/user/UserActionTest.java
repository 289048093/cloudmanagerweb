/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.test.user;

import org.testng.annotations.Test;
import  com.cloudking.cloudmanagerweb.action.test.BaseActionTest;
/**
 * 用户Action测试
 * 
 * @author CloudKing
 * 
 */
@Test
public class UserActionTest extends BaseActionTest {
    
    /**
     * 
     * @throws Exception 所有异常
     */
    public void registerUserTest() throws Exception {
//        //验证码数据准备
//        Map<String, Object> requestParams = new HashMap<String, Object>();
//        requestParams.put(Constant.VERIFY_CODE, "123456");
//        
//        UserAction userAction = createAction(UserAction.class, "/userManager", "base", "register", requestParams);
//        session.setAttribute(Constant.VERIFY_CODE, "123456");
//        
//        UserVO userVO = new UserVO();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("test1");
//        userEntity.setPassword("123456");
//        userEntity.setEmail("test1@test.com");
//        userEntity.setRegisterTime(new Date());
//        userVO.setUserEntity(userEntity);
//        userAction.setUserVO(userVO);
//
//        String result = userAction.register();
//        Assert.assertEquals(result, "register");

//        Assert.assertTrue(userAction.getFieldErrors().size() == 1, "Must have one field error");

    }
}
