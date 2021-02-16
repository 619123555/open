package com.trans.payment.core.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.core.gateway.entity.ApiReq.AddGroup;
import com.trans.payment.core.gateway.entity.CommonEnum;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.common.exception.ValidatorUtils;
import com.trans.payment.core.service.ApiCommonService;
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
public class ApiOpenController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenController.class);

    @Autowired
    Map<String, ApiCommonService> apiMaps;

    @PostMapping("/gateway")
    public String post(@RequestHeader HttpHeaders headers, @RequestBody ApiReq apiReq) {
        Long start = System.currentTimeMillis();
        logger.info("参数:{},header:{}", apiReq, headers);
        ValidatorUtils.gatewayValidateEntity(apiReq, AddGroup.class);
        try {
            JSONObject rsp = apiMaps.get(apiReq.getService()).execute(apiReq);
            return rsp.toJSONString();
        } catch (GatewayException e) {
            throw new GatewayException(e.getCode(), e.getMsg());
        } catch (Exception e) {
            throw new GatewayException(CommonEnum.ISV_SUSPECTED_ATTACK.getMsg());
        }
    }
}
