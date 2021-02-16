package com.trans.payment.core.gateway.entity;

/**
 * 网关公共响应消息枚举
 */
public enum CommonEnum {

    SUCCESS("10000", "SUCCESS", "调用成功"),
    ISV_MISSING_METHOD("20000", "ISV.MISSING-METHOD", "缺少方法名参数"),
    ISV_AUTH_RSA_TIME_OUT("20000", "ISV.AUTH-RSA-TIME-OUT", "访问秘钥已过期"),
    ISV_MISSING_SIGNATURE_TYPE("20000", "ISV.MISSING-SIGNATURE-TYPE", "检查请求参数，缺少signType参数"),
    ISV_MISSING_APP_ID("20000", "ISV.MISSING-APP-ID", "检查请求参数，缺少appId参数"),
    ISV_MISSING_COMMON_PARAMS("20000", "ISV.MISSING-COMMON-PARAMS", "公共参数不能为空"),
    ISV_MISSING_TIMESTAMP("20000", "ISV.MISSING-TIMESTAMP", "检查请求参数，缺少timestamp参数"),
    ISV_MISSING_VERSION("20000", "ISV.MISSING-VERSION", "缺少版本参数"),
    ISV_INVALID_ENCRYPT("20000", "ISV.INVALID-ENCRYPT", "解密异常"),
    ISV_INVALID_PARAMETER("20000", "ISV.INVALID-PARAMETER", "参数格式不正确"),
    ISV_INVALID_METHOD("20000", "ISV.INVALID-METHOD", "不存在的方法名"),
    ISV_INVALID_TIMESTAMP("20000", "ISV.INVALID-TIMESTAMP", "非法的时间戳参数"),
    ISP_MORE_THAN_LIMIT("20000", "ISP.MORE-THAN-LIMIT", "超出服务流量限制"),
    ISV_SUSPECTED_ATTACK("20000", "ISV.SUSPECTED-ATTACK", "服务暂不可用"),
    ISV_INVALID_REQUEST_METHOD("20000", "ISV.INVALID-REQUEST-METHOD", "不支持POST以外请求"),
    ISV_INVALID_REQUEST_TIME("20000", "ISV.INVALID-REQUEST_TIME", "需要下游同步时间戳"),
    ISV_INVALID_REQUEST_REPEAT("20000", "ISV.INVALID-REQUEST-REPEAT", "拦截重复请求"),
    ISV_RSA_SECURITY_ERROR("20000", "ISV.RSA-SECURITY-ERROR", "公钥管理操作失败");

    CommonEnum(String code, String msg, String msgDesc) {
        this.code = code;
        this.msg = msg;
        this.msgDesc = msgDesc;
    }

    private String code;
    private String msg;
    private String msgDesc;

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

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    @Override
    public String toString() {
        return "CommonEnum{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", msgDesc='" + msgDesc + '\'' +
                '}';
    }
}
