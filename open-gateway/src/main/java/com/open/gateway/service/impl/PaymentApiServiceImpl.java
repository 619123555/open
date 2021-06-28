package com.open.gateway.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.enums.PaymentPayTypeEnum;
import com.open.common.enums.TradeStatusEnum;
import com.open.gateway.entity.PaymentOrder;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;
import com.open.gateway.mapper.PaymentOrderMapper;
import com.open.gateway.service.AbstractApiService;
import com.open.gateway.service.RouteService;
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
    JSONObject data = JSONObject.parseObject(apiReq.getData(), JSONObject.class);
    logger.info("代付请求参数:{}", data);

    JSONObject routeRsp = routeService.execute();
    logger.info("通道路由结果:{}", routeRsp);

    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setId(IdUtil.simpleUUID());
    paymentOrder.setPayType(PaymentPayTypeEnum.PAYMENT.name());
    paymentOrder.setTradeNo(IdUtil.simpleUUID());
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
    return null;
  }
}
