package com.trans.payment.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.core.mapper.PaymentOrderMapper;
import com.trans.payment.core.service.AbstractApiService;
import com.trans.payment.core.service.RouteService;
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
//    return (JSONObject) JSONObject.toJSON("{'code':'000'}");
    return new JSONObject();
  }
}
