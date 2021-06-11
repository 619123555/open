package com.open.gateway.util;

import com.alibaba.fastjson.JSON;

public class Result {

    private String code;
    private String msg;

    public Result setCode(String code) {
        this.code = code;
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
