package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.exception.GatewayException;
import com.open.common.dto.gateway.ApiReq;
import com.open.gateway.mapper.PaymentOrderMapper;
import com.open.gateway.service.AbstractApiService;
import com.open.gateway.service.RouteService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 代付余额查询
 */
@Service("trans.payment.query")
public class PaymentBalanceQueryApiServiceImpl extends AbstractApiService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentBalanceQueryApiServiceImpl.class);

  @Autowired
  RouteService routeService;
  @Resource
  PaymentOrderMapper paymentOrderMapper;

  @Override
  public JSONObject execute(ApiReq apiReq) throws GatewayException {
    JSONObject data = JSONObject.parseObject(apiReq.getData(), JSONObject.class);
    logger.info("代付余额查询请求参数:{}", data);


    logger.info("代付余额查询返回参数:{}", "");
    return null;
  }
}
