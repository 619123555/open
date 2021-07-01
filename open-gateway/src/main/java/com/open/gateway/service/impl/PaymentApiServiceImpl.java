package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.PaymentReq;
import com.open.common.dto.gateway.PaymentRsp;
import com.open.common.enums.PaymentPayTypeEnum;
import com.open.common.enums.ResultCode;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.utils.IdGen;
import com.open.gateway.channel.payment.PaymentChannelService;
import com.open.gateway.entity.PaymentOrder;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.exception.GatewayException;
import com.open.gateway.mapper.PaymentOrderMapper;
import com.open.gateway.service.AbstractApiService;
import com.open.gateway.service.RouteService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 代付
 */
@Service("trans.payment.order")
@Slf4j
public class PaymentApiServiceImpl extends AbstractApiService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentApiServiceImpl.class);

  @Autowired
  Map<String, PaymentChannelService> map;
  @Autowired
  RouteService routeService;
  @Resource
  PaymentOrderMapper paymentOrderMapper;

  @Override
  public JSONObject execute(ApiReq apiReq) throws GatewayException {
    PaymentReq paymentReq = (PaymentReq) super.pretreatment(apiReq, PaymentReq.class);
    logger.info("代付请求参数:{}", paymentReq);

    PaymentRsp paymentRsp = new PaymentRsp();


//    JSONObject routeRsp = routeService.execute();
//    logger.info("通道路由结果:{}", routeRsp);

    PaymentOrder paymentOrder = new PaymentOrder();
    BeanUtils.copyProperties(paymentReq, paymentOrder);

    paymentOrder.setId(IdGen.uuidString());
    paymentOrder.setStatus(TradeStatusEnum.INIT.name());
    paymentOrder.setCreateTime(new Date());
    paymentOrder.setAmount(new BigDecimal(paymentReq.getAmount() * 100));
    paymentOrder.setCurrency("156");
    paymentOrderMapper.insert(paymentOrder);

    log.info("代付交易请求通道信息: tradeNo:{}, request:{}", paymentOrder.getTradeNo(), paymentOrder);
    ResponseData result = map.get("SHAN_DE").payment(paymentOrder);
    log.info("代付交易通道返回信息: tradeNo:{}, result:{}", paymentOrder.getTradeNo(), result);

    if(ResultCode.FAIL.getCode() == result.getCode()){
      paymentOrder.setStatus(TradeStatusEnum.FAIL.name());
      paymentOrder.setRemark(result.getMsg());
      paymentOrderMapper.updateByPrimaryKey(paymentOrder);

      paymentRsp.setSubCode(result.getCode());
      paymentRsp.setSubMsg(result.getMsg());
      return (JSONObject) JSONObject.toJSON(paymentOrder);
    }

    JSONObject resultData = (JSONObject) JSONObject.toJSON(result.getData());
    paymentOrder.setStatus(resultData.getString("orderStatus"));
    paymentOrder.setChannelTradeNo(resultData.getString("channelTradeNo"));
    paymentOrderMapper.updateByPrimaryKey(paymentOrder);


    BeanUtils.copyProperties(paymentOrder, paymentRsp);
    paymentRsp.setOrderId(paymentOrder.getId());
    paymentRsp.setSubCode(ResultCode.SUCCESS.getCode());
    paymentRsp.setSubMsg(ResultCode.SUCCESS.getMsg());
    return (JSONObject) JSONObject.toJSON(paymentRsp);
  }
}
