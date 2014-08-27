/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

/**
 * 分页信息类
 * 
 * @author CloudKing
 * 
 */
public class PageInfo {
    /**
     * 当前页
     */
    private Integer nowPage = 1;
    /**
     * 每页数据
     */
    private Integer eachPageData = 20;
    /**
     * 总页数
     */
    private Integer pageCount = 1;
    /**
     * 数据总数
     */
    private Integer dataCount = 0;

    /**
     * 获取当前页
     * 
     * @return
     */
    public Integer getNowPage() {
        return nowPage;
    }

    /**
     * 设置当前页
     * 
     * @param nowPage
     */
    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    /**
     * 获取总页数
     * 
     * @return
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * 设置总页数
     * 
     * @param pageCount
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 获取总数据数
     * 
     * @return
     */
    public Integer getDataCount() {
        return dataCount;
    }

    /**
     * 设置总数据数
     * 
     * @param dataCount
     */
    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    /**
     * 获取起始页
     * 
     * @return
     */
    public Integer getStart() {
        return (nowPage - 1) * eachPageData;
    }
    /**
     * 获取每页显示数据数
     * 
     * @return
     */
    public Integer getEachPageData() {
        return eachPageData;
    }

    /**
     * 设置每页显示数据数
     * 
     * @param eachPageData
     */
    public void setEachPageData(Integer eachPageData) {
        this.eachPageData = eachPageData;
    }
    
    /**
     * 返回最大数
     * @return
     */
    public Integer getLimit(){
        return eachPageData;
    }
}
