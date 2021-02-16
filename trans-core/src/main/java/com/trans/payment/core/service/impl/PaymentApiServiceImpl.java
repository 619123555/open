package com.trans.payment.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.core.service.AbstractApiService;
import com.trans.payment.core.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 代付
 */
@Service("trans.payment")
public class PaymentApiServiceImpl extends AbstractApiService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentApiServiceImpl.class);

  @Autowired
  RouteService routeService;

  @Override
  public JSONObject execute(ApiReq apiReq) throws GatewayException {
    JSONObject paymentReq = JSONObject.parseObject(apiReq.getData(), JSONObject.class);
    JSONObject data = paymentReq.getJSONObject("data");
    logger.info("代付请求参数:{}", data);

    JSONObject routeRsp = routeService.execute();
    logger.info("通道路由结果:{}", routeRsp);

    logger.info("代付返回参数:{}", "");
    return (JSONObject) JSONObject.toJSON("");
  }
}
