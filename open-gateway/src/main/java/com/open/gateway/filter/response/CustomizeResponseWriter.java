package com.open.gateway.filter.response;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.open.common.utils.RsaUtils;
import com.open.gateway.filter.request.CustomizeRequestWrapper;
import com.open.gateway.util.WebConstant;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizeResponseWriter extends PrintWriter {

    private static final Logger logger = LoggerFactory.getLogger(CustomizeResponseWriter.class);

    private StringBuilder buffer;
    private String securityKey;
    private HttpServletRequest servletRequest;

    public CustomizeResponseWriter(HttpServletRequest servletRequest, ServletOutputStream out
            , CustomizeRequestWrapper requestWrapper) {
        super(out);
        this.buffer = new StringBuilder();
        this.securityKey = requestWrapper.getSecurityKey();
        this.servletRequest = servletRequest;
    }

    private String filter(String body) {
        return body.replaceAll("\\u0000", "");
    }


    @Override
    public void write(String s) {
        JSONObject responseObject = new JSONObject();
        responseObject.put(WebConstant.ENCRYPTED_KEY, this.encrypt(securityKey));
        super.write(responseObject.toJSONString());
    }

    private String encrypt(String securityKey) {
        try {
            return Base64.encode(RsaUtils.encryptByPublicKey(filter(buffer.toString()).getBytes(WebConstant.DEFAULT_CHARSET), securityKey));
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        }
        return null;
    }
}

