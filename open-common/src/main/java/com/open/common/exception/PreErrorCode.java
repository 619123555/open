package com.open.common.exception;

public enum PreErrorCode {
    NONE(0, "SUCCESS"),
    ARGS_INVALID(400, "请求参数有误"),
    AUTH_TOKEN_INVALID(403, "访问令牌非法"),
    SERVICE_UNAVAILABLE(9999, "服务不可用"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_INVOKE_ERROR(501, "服务调用出错"),
    SIGNATURE_INVALID(400320, "签名校验错误"),
    REPEAT_REQUEST(400330, "请求拒绝，重复请求"),
    NO_DATA_FOUND(400400, "没有数据"),
    NETWORK_ERROR(400500, "网络通讯故障"),
    BUSINESS(400600, "业务处理异常"),
    SERVICE_NOT_EXISTS(420000, "服务不存在"),
    DB_ERROR(500200, "数据库错误"),
    UNKNOW_ERROR(999999, "网络超时或未知异常"),
    LOGIN_INVALID(401, "登录失效");

    int code;
    String message;

    private PreErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
