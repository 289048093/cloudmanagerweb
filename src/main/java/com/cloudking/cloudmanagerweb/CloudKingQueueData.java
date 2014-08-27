/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

/**
 * 队列数据的抽象类
 * 
 * @author CloudKing
 */
public abstract class CloudKingQueueData {
    /**
     * 抽象方法
     * 
     * @throws Exception
     *             所有异常
     */
    public abstract void execute() throws Exception;
}
