package com.trans.payment.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(PreException.class);


    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public PreException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public PreException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public PreException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
        logger.error("响应编号:{}-描述信息:{}", code, msg);
    }

    public PreException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public PreException(String msg, PreErrorCode errCode) {
        super(msg);
        this.msg = msg;
        this.code = errCode.getCode();
        logger.error("响应编号:{}-描述信息:{}", code, msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
