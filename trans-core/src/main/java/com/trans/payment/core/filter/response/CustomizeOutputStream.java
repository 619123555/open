package com.trans.payment.core.filter.response;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.constants.CommonEnum;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.common.utils.DateUtils;
import com.trans.payment.common.utils.RsaUtils;
import com.trans.payment.core.filter.request.CustomizeRequestWrapper;
import com.trans.payment.core.util.WebConstant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizeOutputStream extends ServletOutputStream {

    private static Logger logger = LoggerFactory.getLogger(CustomizeOutputStream.class);

    private ServletOutputStream outputStream;
    private int count;
    private String securityKey;
    private String organizationId;
    private String service;
    private HttpServletRequest servletRequest;

    public CustomizeOutputStream(HttpServletRequest servletRequest, ServletOutputStream outputStream
            , CustomizeRequestWrapper requestWrapper) {
        this.outputStream = outputStream;
        this.securityKey = requestWrapper.getSecurityKey();
        this.servletRequest = servletRequest;
        this.organizationId = requestWrapper.getOrganizationId();
        this.service = requestWrapper.getService();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener listener) {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(this.buildByteEncrypted(b), off, count);
    }

    private byte[] buildByteEncrypted(byte[] bytes) throws UnsupportedEncodingException, GatewayException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(WebConstant.ORGANIZATION_ID, organizationId);
        jsonObject.put(WebConstant.SERVICE_KEY, service);
        jsonObject.put(WebConstant.TIMESTAMP_KEY, DateUtils.formatDateTime(new Date()));
        if (bytes == null || bytes.length == 0) {
            jsonObject.put(WebConstant.GATEWAY_CODE, CommonEnum.ISV_SUSPECTED_ATTACK.getCode());
            jsonObject.put(WebConstant.GATEWAY_MSG, CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
            byte[] encrypted = jsonObject.toJSONString().getBytes();
            this.count = encrypted.length;
            return encrypted;
        }
        String body = new String(bytes, WebConstant.DEFAULT_CHARSET);
        String pixfix = "\"code\"";
        if (body.contains(pixfix)) {
            byte[] encrypted = body.getBytes();
            this.count = encrypted.length;
            return encrypted;
        }
        jsonObject.put(WebConstant.ENCRYPTED_KEY, this.encrypt(body, securityKey));
        jsonObject.put(WebConstant.GATEWAY_CODE, CommonEnum.SUCCESS.getCode());
        jsonObject.put(WebConstant.GATEWAY_MSG, CommonEnum.SUCCESS.getMsg());
        logger.debug(jsonObject.toString());
        byte[] encrypted = jsonObject.toJSONString().getBytes();
        this.count = encrypted.length;
        return encrypted;
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    private String filter(String body) {
        return body.replaceAll("\\u0000", "");
    }

    private String encrypt(String body, String securityKey) {
        try {
            return Base64.encode(RsaUtils
                .encryptByPublicKey(filter(body).getBytes(WebConstant.DEFAULT_CHARSET), securityKey));
        } catch (Throwable th) {
            logger.error("Throwable", th);
            throw new GatewayException(CommonEnum.ISV_INVALID_ENCRYPT.getMsg());
        }
    }

    public int getCount() {
        return count;
    }
}
