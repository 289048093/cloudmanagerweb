/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Dec 15, 2012  12:59:28 AM
 */
package com.cloudking.cloudmanagerweb.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务报警次数包装类
 * @author CloudKing
 *
 */
public class ServiceWarnNumWrapper {
    /**
     * wrapper
     */
    private static ServiceWarnNumWrapper serviceWarnNumWrapper;
    /**
     * map
     */
    private Map<String, Integer> serviceWarnNum;

    /**
     * 构造方法
     */
    private ServiceWarnNumWrapper(){
        serviceWarnNum = new HashMap<String, Integer>();
    }
    /**
     * 获取实例
     * @return
     */
    public static ServiceWarnNumWrapper getInstance(){
        if(serviceWarnNumWrapper==null){
            serviceWarnNumWrapper= new ServiceWarnNumWrapper();
        }
        return serviceWarnNumWrapper;
    }
    
    /**
     * 添加
     */
    public ServiceWarnNumWrapper put(String identity,Integer warnNum){
        this.serviceWarnNum.put(identity, warnNum);
        return this;
    }
    /**
     * 删除
     */
    public ServiceWarnNumWrapper remove(String indetity){
        this.serviceWarnNum.remove(indetity);
        return this;
    }
    /**
     * 获取
     */
    public Integer get(String identity){
        return this.serviceWarnNum.get(identity)==null?0:this.serviceWarnNum.get(identity);
    }
    /**
     * 清空
     */
    public ServiceWarnNumWrapper clear(){
        this.serviceWarnNum.clear();
        return this;
    }
    /**
     * 累加   
     * 如果Service无报警记录则创建一个记录
     */
    public ServiceWarnNumWrapper plusOne(String identity){
        Integer warnNum = this.serviceWarnNum.get(identity);
        if(warnNum==null){
            this.serviceWarnNum.put(identity, 1);
        }else{
            this.serviceWarnNum.put(identity, ++warnNum);
        }
        return this;
    }
}
