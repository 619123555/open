package com.trans.payment.core.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.trans.payment.core.filter.response.CustomizeOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

public class MyFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
            throws IOException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int length = JSON.writeJSONString(outnew,
                fastJsonConfig.getCharset(),
                obj,
                fastJsonConfig.getSerializeConfig(),
                fastJsonConfig.getSerializeFilters(),
                fastJsonConfig.getDateFormat(),
                JSON.DEFAULT_GENERATE_FEATURE,
                fastJsonConfig.getSerializerFeatures());
        OutputStream out = outputMessage.getBody();
        outnew.writeTo(out);
        if (fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength(
                    (out instanceof CustomizeOutputStream) ? ((CustomizeOutputStream) out).getCount() : length);
        }
        outnew.close();
    }
}
