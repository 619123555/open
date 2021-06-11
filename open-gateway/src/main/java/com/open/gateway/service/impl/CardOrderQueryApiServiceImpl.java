package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.dto.gateway.CardOrderQueryReq;
import com.open.common.dto.gateway.CardTopUpResp;
import com.open.common.enums.ResultCode;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.exception.GatewayException;
import com.open.common.utils.validator.ValidatorUtils;
import com.open.common.utils.validator.group.AddGroup;
import com.open.gateway.channel.card.CardChannelService;
import com.open.gateway.entity.CardOrder;
import com.open.gateway.mapper.CardOrderMapper;
import com.open.gateway.service.AbstractApiService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description: 卡订单查询
 */
@Service("trans.card.order.query")
@Slf4j
public class CardOrderQueryApiServiceImpl extends AbstractApiService {

  @Autowired
  CardOrderMapper cardOrderMapper;
  @Autowired
  Map<String, CardChannelService> cardChannelServiceMap;

  @Override
  public ResponseData execute(ApiReq apiReq) throws GatewayException {
    CardOrderQueryReq cardOrderQueryReq = JSONObject.parseObject(apiReq.getData(), CardOrderQueryReq.class);
    log.info("卡充值订单查询请求参数:{}", cardOrderQueryReq);
    ValidatorUtils.gatewayValidateEntity(cardOrderQueryReq, AddGroup.class);

    CardOrder cardOrder = new CardOrder();
    cardOrder.setTradeNo(cardOrderQueryReq.getTradeNo());
    cardOrder = cardOrderMapper.selectOne(cardOrder);
    log.info("查询到的订单:{}", cardOrder);
    if(cardOrder == null){
      return ResponseData.error("订单不存在");
    }

    CardTopUpResp cardTopUpResp = new CardTopUpResp();
    if(TradeStatusEnum.SUCCESS.name().equals(cardOrder.getStatus())){
      BeanUtils.copyProperties(cardOrder, cardTopUpResp);
      return ResponseData.ok(cardTopUpResp);
    }

    ResponseData result = cardChannelServiceMap.get(cardOrder.getChannelId()).query(cardOrder.getTradeNo());
    if(!ResultCode.SUCCESS.equals(result.getCode())){
      return ResponseData.error(result.getMsg());
    }

    JSONObject resultData = (JSONObject) JSONObject.toJSON(result.getData());
    cardOrder.setStatus(resultData.getString("orderStatus"));
    cardOrder.setChannelTradeNo(resultData.getString("channelTradeNo"));
    cardOrder.setRealAmount(StringUtils.isEmpty(resultData.getString("card_real_amt")) ? null : new BigDecimal(resultData.getString("card_real_amt")));
    cardOrder.setSettleAmount(StringUtils.isEmpty(resultData.getString("card_settle_amt")) ? null :new BigDecimal(resultData.getString("card_settle_amt")));
    cardOrderMapper.updateByPrimaryKey(cardOrder);

    BeanUtils.copyProperties(cardOrder, cardTopUpResp);
    return ResponseData.ok(cardTopUpResp);
  }
}