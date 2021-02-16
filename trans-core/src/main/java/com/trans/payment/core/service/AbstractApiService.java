package com.trans.payment.core.service;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.common.exception.GatewayException;
import javax.servlet.http.HttpServletRequest;

/**
 * 网关抽象公共类
 */
public abstract class AbstractApiService implements ApiCommonService {


    @Override
    public JSONObject execute(ApiReq apiReq) throws GatewayException {
        return null;
    }

    @Override
    public JSONObject notify(String channnelId, HttpServletRequest request) {
        return null;
    }

}
