package com.trans.payment.common.constants;

public class CommonConst {
    /**
     * 成功
     */
    public final static String SUCCESS = "10000";
    /**
     * 失败
     */
    public final static String FAILED = "20000";

    /**
     * 处理中
     */
    public final static String DOING = "30000";


    private String code;
    private String Msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public CommonConst() {
    }

    public CommonConst(String code, String msg) {
        this.code = code;
        Msg = msg;
    }
}
