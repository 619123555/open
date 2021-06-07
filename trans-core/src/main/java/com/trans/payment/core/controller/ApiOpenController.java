package com.trans.payment.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.utils.validator.ValidatorUtils;
import com.trans.payment.common.utils.validator.group.AddGroup;
import com.trans.payment.common.dto.gateway.ApiReq;
import com.trans.payment.common.constants.CommonEnum;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.core.service.ApiCommonService;
import com.trans.payment.core.service.NotifyService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一入口
 */
@RestController
public class ApiOpenController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenController.class);

    @Autowired
    Map<String, ApiCommonService> apiMaps;
//    @Autowired
    Map<String, NotifyService> channelNotifyMap;

    @PostMapping("/gateway")
    public String post(@RequestHeader HttpHeaders headers, @RequestBody ApiReq apiReq) {
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

    @RequestMapping("/notify/{channelId}")
    public String notify(@RequestHeader HttpHeaders headers, HttpServletRequest request,
        @PathVariable("channelId") String channelId) {
        logger.info("channelId:{},header:{}", channelId, headers);
        JSONObject rsp = channelNotifyMap.get(channelId).execute(request);
        if (rsp != null && rsp.containsKey("subCode")) {
            return rsp.getString("subMsg");
        }
        return null;
    }
}
