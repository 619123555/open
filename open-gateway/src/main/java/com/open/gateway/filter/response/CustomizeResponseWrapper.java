package com.open.gateway.filter.response;

import com.open.gateway.filter.request.CustomizeRequestWrapper;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizeResponseWrapper extends HttpServletResponseWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomizeResponseWrapper.class);

    private ServletOutputStream outputStream;

    public CustomizeResponseWrapper(HttpServletRequest servletRequest, HttpServletResponse response
            , CustomizeRequestWrapper requestWrapper) {
        super(response);
        try {
            this.outputStream = new CustomizeOutputStream(servletRequest, response.getOutputStream(), requestWrapper);
        } catch (IOException e) {
            logger.error("OutputStream Failed!!", e);
        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        this.outputStream.flush();
        this.outputStream.close();
    }


    @Override
    public ServletOutputStream getOutputStream() {
        return this.outputStream;
    }
}

