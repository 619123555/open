package com.open.gateway.service;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;

public interface ApiService {

    JSONObject execute(ApiReq apiReq) throws GatewayException;

}
