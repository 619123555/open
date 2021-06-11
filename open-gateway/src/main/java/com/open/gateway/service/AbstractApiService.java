package com.open.gateway.service;

import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;

public abstract class AbstractApiService implements ApiCommonService {

    @Override
    public ResponseData execute(ApiReq apiReq) throws GatewayException {
        return null;
    }
}
