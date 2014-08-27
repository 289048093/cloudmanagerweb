/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.action.rrd;

import java.io.ByteArrayInputStream;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.cloudking.cloudmanagerweb.BaseAction;
import com.cloudking.cloudmanagerweb.BaseVO;
import com.cloudking.cloudmanagerweb.service.rrd.RRDService;

/**
 * 监控Action
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("cloudmanagerweb-default")
@Results( { @Result(type = "stream", name = "success", params = { "contentType", "image/png", "inputName", "imgIS",
                "allowCaching", "false" }) })
@Namespace("/rrdManager")
public class RRDAction extends BaseAction<BaseVO> {

    /**
     * rrdService
     */
    @Resource
    private transient RRDService rrdService;

    /**
     * 输出流
     */
    private transient ByteArrayInputStream imgIS;

    public ByteArrayInputStream getImgIS() {
        return imgIS;
    }

    public void setImgIS(ByteArrayInputStream imgIS) {
        this.imgIS = imgIS;
    }

    /**
     * 默认action
     * 
     * @throws Exception
     *             所有异常
     */
    @Action("/rrd")
    public String execute() throws Exception {
        return INPUT;
    }

    /**
     * 画图
     * 
     * @throws Exception
     *             所有异常
     */
    public String getImg() throws Exception {
        rrdService.getImg(cloudContext);
        byte[] result = (byte[]) cloudContext.getObjectParam("img");
        imgIS = new ByteArrayInputStream(result);
        return SUCCESS;
    }

}
