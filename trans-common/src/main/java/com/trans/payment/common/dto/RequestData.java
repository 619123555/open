package com.trans.payment.common.dto;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class RequestData implements Serializable {

    private static final long serialVersionUID = -8008271239921313489L;

    @NotEmpty(message = "version必传")
    @Length(min = 1, max = 5, message = "version 长度错误")
    private String version;

    @NotEmpty(message = "charset必传")
    @Length(min = 1, max = 5, message = "charset 长度错误")
    private String charset;

    @Length(min = 1, max = 2000, message = "sign 长度错误")
    private String sign;

    private String src;

    @NotEmpty(message = "service必传")
    @Length(min = 1, max = 50, message = "service 长度错误")
    private String service;

    private String biz_content;

    @NotEmpty(message = "timestamp必传")
    private String timestamp;

    public String getBiz_content () {
        return biz_content;
    }

    public void setBiz_content (String biz_content) {
        this.biz_content = biz_content;
    }

    public String getVersion () {
        return version;
    }

    public void setVersion (String version) {
        this.version = version;
    }

    public String getCharset () {
        return charset;
    }

    public void setCharset (String charset) {
        this.charset = charset;
    }

    public String getSign () {
        return sign;
    }

    public void setSign (String sign) {
        this.sign = sign;
    }

    public String getService () {
        return service;
    }

    public void setService (String service) {
        this.service = service;
    }


    public String getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return src;
    }

    public void setSource(String src) {
        this.src = src;
    }
}
