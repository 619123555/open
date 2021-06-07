package com.trans.payment.core.util;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

public class ResponseUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    public static void printJson(HttpServletResponse response, String code, String msg) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(WebConstant.DEFAULT_CHARSET);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Map<String, String> map = new HashMap<>(16);
            map.put(WebConstant.GATEWAY_CODE, code);
            map.put(WebConstant.GATEWAY_MSG, msg);
            out.write(JSON.toJSONString(map));
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
