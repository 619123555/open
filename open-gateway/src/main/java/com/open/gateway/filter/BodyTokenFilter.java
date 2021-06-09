package com.open.gateway.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.common.constants.CommonEnum;
import com.open.common.exception.GatewayException;
import com.open.common.service.RedisCacheDaoService;
import com.open.common.utils.StringUtils;
import com.open.gateway.filter.rate.RateLimitService;
import com.open.gateway.filter.request.CustomizeCommonRequestWrapper;
import com.open.gateway.entity.GatewayService;
import com.open.gateway.util.RedisKeyConfigure;
import com.open.gateway.util.ResponseUtils;
import com.open.gateway.util.WebConstant;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

public class BodyTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(BodyTokenFilter.class);

    @Resource
    private RedisCacheDaoService redisCacheDaoService;

    private static final long DEFAULT_EXPIRY_IN_MILLS = 90 * 1000L;
    private static final long DEFAULT_LIMIT_SECONDS = 60L;

    @Value("${platform.dev.mode:false}")
    private boolean dev;

    @Resource
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (!dev) {
            JSONObject jsonObject = JSON.parseObject(IoUtil.read(request.getInputStream(), WebConstant.DEFAULT_CHARSET));
            if (jsonObject == null || jsonObject.size() == 0) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_COMMON_PARAMS.getCode(),
                        CommonEnum.ISV_MISSING_COMMON_PARAMS.getMsg());
                return;
            }
            String organizationId = jsonObject.getString(WebConstant.ORGANIZATION_ID);
            String service = jsonObject.getString(WebConstant.SERVICE_KEY);
            String timestamp = jsonObject.getString(WebConstant.TIMESTAMP_KEY);
            String requestId = jsonObject.getString(WebConstant.REQUEST_ID);
            String data = jsonObject.getString(WebConstant.ENCRYPTED_KEY);
            String signType = jsonObject.getString(WebConstant.SIGNTYPE_KEY);
            String version = jsonObject.getString(WebConstant.VERSION_KEY);
            if (StringUtils.isEmpty(organizationId)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_ORGANIZATION_ID.getCode(),
                        CommonEnum.ISV_MISSING_ORGANIZATION_ID.getMsg());
                return;
            }
            if (StringUtils.isEmpty(service)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_METHOD.getCode(),
                        CommonEnum.ISV_MISSING_METHOD.getMsg());
                return;
            }
            if (StringUtils.isEmpty(signType)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_SIGNATURE_TYPE.getCode(),
                        CommonEnum.ISV_MISSING_SIGNATURE_TYPE.getMsg());
                return;
            }
            if (StringUtils.isEmpty(version)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_VERSION.getCode(),
                        CommonEnum.ISV_MISSING_VERSION.getMsg());
                return;
            }
            if (StringUtils.isEmpty(signType)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_METHOD.getCode(),
                        CommonEnum.ISV_MISSING_METHOD.getMsg());
                return;
            }
            if (StringUtils.isEmpty(timestamp)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_TIMESTAMP.getCode(),
                        CommonEnum.ISV_MISSING_TIMESTAMP.getMsg());
                return;
            }
            if (!StringUtils.isNotEmptyBatch(requestId, data)) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_MISSING_COMMON_PARAMS.getCode(),
                        CommonEnum.ISV_MISSING_COMMON_PARAMS.getMsg());
                return;
            }
            try {
                GatewayService gs = this.selectServiceDetail(organizationId, service, version);
                this.safetyPreInspection(organizationId, requestId, timestamp);
                this.rateLimitService.limitCheck(
                    RedisKeyConfigure
                        .genKey(WebConstant.MODULE_NAME, WebConstant.RATE_LIMIT, organizationId, service),
                    gs.getLimits(),
                    DEFAULT_LIMIT_SECONDS);
            } catch (GatewayException e) {
                ResponseUtils.printJson(response, e.getCode(), e.getMsg());
                return;
            } catch (Exception e) {
                ResponseUtils.printJson(
                        response,
                        CommonEnum.ISV_SUSPECTED_ATTACK.getCode(),
                        CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
                return;
            }
            filterChain.doFilter(new CustomizeCommonRequestWrapper(request, jsonObject.toJSONString()), response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void safetyPreInspection(String organizationId, String requestId, String timestamp) {
        String key = RedisKeyConfigure.genKey(WebConstant.MODULE_NAME, organizationId, requestId);
        if (!this.redisCacheDaoService.hasKey(key)) {
            long time = DateUtil.parseDateTime(timestamp).getTime();
            long timeMillis = System.currentTimeMillis();
            if (timeMillis - time > DEFAULT_EXPIRY_IN_MILLS) {
                throw new GatewayException(CommonEnum.ISV_INVALID_REQUEST_TIME.getMsg());
            }
            this.redisCacheDaoService.save(key, new byte[0], DEFAULT_LIMIT_SECONDS);
        } else {
            throw new GatewayException(CommonEnum.ISV_INVALID_REQUEST_REPEAT.getMsg());
        }
    }

    private GatewayService selectServiceDetail(String organizationId, String service, String version)
            throws GatewayException {
        GatewayService gs = this.rateLimitService.selectServiceDetail(organizationId, service, version);
        if (gs == null || gs.getLimits() == null || gs.getUseStatus() != 0) {
            logger.info("service close");
            throw new GatewayException(CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
        }
        return gs;
    }
}
