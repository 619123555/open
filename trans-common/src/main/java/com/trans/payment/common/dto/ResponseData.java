package com.trans.payment.common.dto;

import com.trans.payment.common.enums.ResultCode;
import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseData<T> implements Serializable {


    private static final long serialVersionUID = 783015033603078674L;
    private int code;
    private String msg;
    private T data;

    public static ResponseData ok() {
        return ok("");
    }

    public static ResponseData ok(Object o) {
        return new ResponseData(ResultCode.SUCCESS, o);
    }

    public static ResponseData error() {
        return new ResponseData(ResultCode.FAIL);
    }

    public static ResponseData error(ResultCode code) {
        return error(code, "");
    }

    public static ResponseData error(String message) {
        ResultCode code = ResultCode.FAIL;
        code.setMsg(message);
        return new ResponseData(code, message);
    }

    public static ResponseData error(ResultCode code, Object o) {
        return new ResponseData(code, o);
    }

    public ResponseData(ResultCode resultCode) {
        setResultCode(resultCode);
    }

    public ResponseData(ResultCode resultCode, T data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "code=" + code +
                ", sub_msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
