/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.util.concurrent.LinkedBlockingQueue;

import com.cloudking.cloudmanagerweb.util.LogUtil;

/**
 * 队列类
 * 
 * @author CloudKing
 */
public final class CloudKingQueue implements Runnable {
    /**
     * 真正的队列
     */
    private static LinkedBlockingQueue<CloudKingQueueData> queue = new LinkedBlockingQueue<CloudKingQueueData>();
    /**
     * 单例类
     */
    private static CloudKingQueue instance = new CloudKingQueue();

    /**
     * 启动标记
     */
    private static Boolean startFlag = false;

    /**
     * 默认构造方法
     */
    private CloudKingQueue(){
    }

    /**
     * 添加数据到队列中，阻塞
     * 
     * @param cloudKingQueueData
     * @return
     * @throws InterruptedException
     *             线程中断
     */
    public static void put(CloudKingQueueData cloudKingQueueData)
                    throws InterruptedException {
        if (cloudKingQueueData == null) {
            throw new IllegalArgumentException("传入的CloudKingQueueData为空.");
        } else {
            queue.put(cloudKingQueueData);
        }
    }

    /**
     * 获取队列头的CloudKingQueueData，阻塞,
     * 
     * @param cloudKingQueueData
     * @return
     * @throws InterruptedException
     *             线程中断
     */
    private static CloudKingQueueData take() throws InterruptedException {
        return queue.take();
    }

    /**
     * 开始
     */
    public static void start() {
        if (!startFlag) {
            startFlag = true;
            new Thread(instance).start();
        } else {
            throw new RuntimeException("启动失败，已经启动.");
        }
    }

    /**
     * 线程run方法
     */
    @Override
    public void run() {
        CloudKingQueueData cloudKingQueueData = null;
        while (true) {
            try {
                cloudKingQueueData = CloudKingQueue.take();
                if (cloudKingQueueData != null) {
                    cloudKingQueueData.execute();
                }
            } catch (InterruptedException e) {
                LogUtil.error(e);
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
    }
}
