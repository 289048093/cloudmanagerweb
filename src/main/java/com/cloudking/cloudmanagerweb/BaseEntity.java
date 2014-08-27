/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 所有实体类的父类，包含一个ID主键
 * 
 * @author CloudKing
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_")
    private Long id;

    /**
     * 获得ID主键
     * 
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID主键
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

}
