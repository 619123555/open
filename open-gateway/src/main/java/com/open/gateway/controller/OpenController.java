package com.open.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.common.constants.CommonEnum;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;
import com.open.common.utils.validator.ValidatorUtils;
import com.open.common.utils.validator.group.AddGroup;
import com.open.gateway.service.ApiService;
import com.open.gateway.service.impl.GatewayLogService;
import com.open.gateway.util.RequestUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一入口
 */
@RestController
public class OpenController {

    private static final Logger logger = LoggerFactory.getLogger(OpenController.class);

    @Autowired
    GatewayLogService gatewayLogService;
    @Autowired
    Map<String, ApiService> apiMaps;

    @PostMapping("/gateway")
    public String post(@RequestHeader HttpHeaders headers, @RequestBody ApiReq apiReq) {
        Long start = System.currentTimeMillis();
        logger.info("参数:{},header:{}", apiReq, headers);
        ValidatorUtils.gatewayValidateEntity(apiReq, AddGroup.class);
        try {
            JSONObject rsp = apiMaps.get(apiReq.getService()).execute(apiReq);
            gatewayLogService.requestLog(apiReq, start, rsp, RequestUtils.getTraceId());
            return rsp.toJSONString();
        } catch (GatewayException e) {
            throw new GatewayException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            throw new GatewayException(CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
        }
    }
}
