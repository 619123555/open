package com.trans.payment.boss.commons;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";

    public static Result ok() {
        return new Result().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> ok(T data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result error() {
        return new Result().setCode(ResultCode.FAIL).setMessage(DEFAULT_FAIL_MESSAGE);
    }

    public static Result error(String message) {
        return new Result().setCode(ResultCode.FAIL).setMessage(message);
    }
}
