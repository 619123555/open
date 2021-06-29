package com.open.gateway.service;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;
import com.open.common.utils.validator.ValidatorUtils;
import com.open.common.utils.validator.group.AddGroup;

public abstract class AbstractApiService implements ApiCommonService {

    public Object pretreatment(ApiReq apiReq, Class clazz) throws GatewayException {
        JSONObject reqJson = (JSONObject) JSONObject.parse(apiReq.getData());
        Object object = JSONObject.toJavaObject(reqJson, clazz);
        ValidatorUtils.gatewayValidateEntity(object, AddGroup.class);
        return object;
    }
}
