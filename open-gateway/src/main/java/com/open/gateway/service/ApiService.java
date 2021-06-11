package com.open.gateway.service;

import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;

public interface ApiService {

    ResponseData execute(ApiReq apiReq) throws GatewayException;

}
