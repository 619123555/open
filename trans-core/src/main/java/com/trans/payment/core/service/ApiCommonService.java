package com.trans.payment.core.service;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.common.exception.GatewayException;
import javax.servlet.http.HttpServletRequest;

public interface ApiCommonService {

    JSONObject execute(ApiReq apiReq) throws GatewayException;

    /**
     * 接收上游异步通知入口
     * @param channelId 通道编码
     * @param request HttpServletRequest
     * @return String 根据不同的上游返回指定的串
     */
    JSONObject notify(String channelId, HttpServletRequest request);

}
