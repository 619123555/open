package com.trans.payment.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数错误异常
 */
public class ParamErrorException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ParamErrorException.class);
    private static final long serialVersionUID = -7626079060833574809L;

    private String code = "20000";
    private String msg;


    public ParamErrorException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ParamErrorException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        logger.error("错误码:{},错误信息:{}", code, msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
