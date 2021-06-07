package com.trans.payment.core.filter;

import com.trans.payment.common.constants.CommonEnum;
import com.trans.payment.core.filter.request.CustomizeRequestWrapper;
import com.trans.payment.core.filter.response.CustomizeResponseWrapper;
import com.trans.payment.core.service.impl.RsaInfoService;
import com.trans.payment.core.util.ResponseUtils;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

public class BodySecurityFilter extends OncePerRequestFilter {


    @Value("${platform.dev.mode:false}")
    private boolean dev;

    @Resource
    private RsaInfoService rsaInfoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (!dev) {
            CustomizeRequestWrapper customizeRequestWrapper = new CustomizeRequestWrapper(request, rsaInfoService);
            if (StringUtils.isNotBlank(customizeRequestWrapper.getMsg())) {
                ResponseUtils.printJson(response, CommonEnum.ISV_INVALID_ENCRYPT.getCode(),
                        customizeRequestWrapper.getMsg());
                return;
            }
            CustomizeResponseWrapper customizeResponseWrapper = new CustomizeResponseWrapper(request, response, customizeRequestWrapper);
            filterChain.doFilter(customizeRequestWrapper, customizeResponseWrapper);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
