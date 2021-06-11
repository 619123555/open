package com.open.gateway.util;


import com.open.common.constants.CommonEnum;

public class ResultGenerator {


    public static Result ok() {
        return new Result().setCode(CommonEnum.SUCCESS.getCode()).setMsg(CommonEnum.SUCCESS.getMsg());
    }

    public static Result ok(String msg) {
        return new Result().setCode(CommonEnum.SUCCESS.getCode()).setMsg(msg);
    }

    public static Result error(String message) {
        return new Result().setCode(CommonEnum.ISV_MISSING_METHOD.getCode()).setMsg(message);
    }

    public static Result error(String code, String message) {
        return new Result().setCode(code).setMsg(message);
    }
}
