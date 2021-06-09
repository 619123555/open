package com.open.gateway.filter.request;

import com.open.gateway.util.WebConstant;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CustomizeCommonRequestWrapper extends HttpServletRequestWrapper {

    private String body;

    public CustomizeCommonRequestWrapper(HttpServletRequest request, String body) {
        super(request);
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CustomizeServletInputStream(new ByteArrayInputStream(body.getBytes()));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding() == null ? WebConstant.DEFAULT_CHARSET : getCharacterEncoding();
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }
}
