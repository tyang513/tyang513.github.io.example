package com.talkingdata.framework.gateway.exception;

/**
 * 工具类异常
 * @author tao.yang
 * @date 2019-11-18
 */
public class CommonException extends Exception {


    public CommonException(Throwable cause){

    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

}
