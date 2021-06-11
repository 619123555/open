package com.open.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.enums.ResultCode;
import com.open.common.utils.validator.ValidatorUtils;
import com.open.common.utils.validator.group.AddGroup;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.constants.CommonEnum;
import com.open.common.exception.GatewayException;
import com.open.gateway.service.ApiCommonService;
import com.open.gateway.service.NotifyService;
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
public class OpenController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OpenController.class);

    @Autowired
    Map<String, ApiCommonService> apiMaps;
//    @Autowired
    Map<String, NotifyService> channelNotifyMap;

    @PostMapping("/gateway")
    public String post(@RequestHeader HttpHeaders headers, @RequestBody ApiReq apiReq) {
        logger.info("参数:{},header:{}", apiReq, headers);
        ValidatorUtils.gatewayValidateEntity(apiReq, AddGroup.class);
        try {
            ResponseData rsp = apiMaps.get(apiReq.getService()).execute(apiReq);
            return JSONObject.toJSONString(rsp);
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
        ResponseData rsp = channelNotifyMap.get(channelId).execute(request);
        if (rsp != null && ResultCode.SUCCESS.equals(rsp.getCode())) {
            return rsp.getData().toString();
        }
        return null;
    }
}
