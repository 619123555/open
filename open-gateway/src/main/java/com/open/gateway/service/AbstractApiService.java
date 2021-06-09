package com.open.gateway.service;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;

/**
 * 网关抽象公共类
 */
public abstract class AbstractApiService implements ApiService {


    @Override
    public JSONObject execute(ApiReq apiReq) throws GatewayException {
        return null;
    }
}
