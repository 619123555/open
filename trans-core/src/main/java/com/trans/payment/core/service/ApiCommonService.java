package com.trans.payment.core.service;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.dto.gateway.ApiReq;
import com.trans.payment.common.exception.GatewayException;

public interface ApiCommonService {
    JSONObject execute(ApiReq apiReq) throws GatewayException;
}
