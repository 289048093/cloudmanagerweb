/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Nov 14, 2012  3:14:34 PM
 */
package com.cloudking.cloudmanagerweb;

/**
 * @author CloudKing
 */
public class JPAEventException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 8768006127628453628L;

    /**
     * 
     */
    public JPAEventException(){
        super();
    }

    /**
     * 
     */
    public JPAEventException(String message){
        super(message);
    }

    /**
     * 
     */
    public JPAEventException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * 
     */
    public JPAEventException(Throwable cause){
        super(cause);
    }
}
