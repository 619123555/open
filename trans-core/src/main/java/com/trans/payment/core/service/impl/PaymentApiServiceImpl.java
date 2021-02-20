package com.trans.payment.core.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.trans.payment.common.enums.PaymentPayTypeEnum;
import com.trans.payment.common.enums.TradeStatusEnum;
import com.trans.payment.core.entity.PaymentOrder;
import com.trans.payment.core.gateway.entity.ApiReq;
import com.trans.payment.common.exception.GatewayException;
import com.trans.payment.core.mapper.PaymentOrderMapper;
import com.trans.payment.core.service.AbstractApiService;
import com.trans.payment.core.service.RouteService;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
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
  @Resource
  PaymentOrderMapper paymentOrderMapper;

  @Override
  public JSONObject execute(ApiReq apiReq) throws GatewayException {
    JSONObject paymentReq = JSONObject.parseObject(apiReq.getData(), JSONObject.class);
    JSONObject data = paymentReq.getJSONObject("data");
    logger.info("代付请求参数:{}", data);

    JSONObject routeRsp = routeService.execute();
    logger.info("通道路由结果:{}", routeRsp);

    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setId(IdUtil.simpleUUID());
    paymentOrder.setPayType(PaymentPayTypeEnum.PAYMENT.name());
    paymentOrder.setTradeNo("");
    paymentOrder.setAmount(new BigDecimal(2));
    paymentOrder.setStatus(TradeStatusEnum.INIT.name());
    paymentOrder.setCreateTime(new Date());
    paymentOrder.setCurrency("156");
    paymentOrder.setPayerAccNo("");
    paymentOrder.setPayerAccName("");
    paymentOrder.setPayeeAccNo("");
    paymentOrder.setPayeeAccName("");
    paymentOrder.setPayeeAccType("");
//    paymentOrder.setPayeeCertType("");
//    paymentOrder.setPayeeCertNo("");
    paymentOrder.setPayeePhone("");
//    if(对公){
//      paymentOrder.setPayeeBankCode("");
//      paymentOrder.setPayeeBankName("");
//    }
    paymentOrder.setRemark("");
    paymentOrder.setChannelId(routeRsp.getString("channelId"));
    paymentOrder.setChannelTradeNo("");
    paymentOrderMapper.insert(paymentOrder);


    logger.info("代付返回参数:{}", "");
//    return (JSONObject) JSONObject.toJSON("{'code':'000'}");
    return new JSONObject();
  }
}
