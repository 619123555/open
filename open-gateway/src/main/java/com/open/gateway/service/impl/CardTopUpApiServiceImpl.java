package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.dto.gateway.CardTopUpReq;
import com.open.common.dto.gateway.CardTopUpRsp;
import com.open.common.enums.ResultCode;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.exception.GatewayException;
import com.open.common.utils.IdGen;
import com.open.common.utils.validator.ValidatorUtils;
import com.open.common.utils.validator.group.AddGroup;
import com.open.gateway.channel.card.CardChannelService;
import com.open.gateway.entity.CardOrder;
import com.open.gateway.mapper.CardOrderMapper;
import com.open.gateway.service.AbstractApiService;
import java.math.BigDecimal;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @description: 卡充值
 */
@Service("trans.card.order.topup")
@Slf4j
public class CardTopUpApiServiceImpl extends AbstractApiService {

  @Autowired
  CardOrderMapper cardOrderMapper;
  @Autowired
  CardChannelService cardChannelService;

  @Override
  @Transactional(rollbackFor = {Exception.class, Error.class})
  public JSONObject execute(ApiReq apiReq) throws GatewayException {
    CardTopUpReq cardTopUpReq = (CardTopUpReq) super.pretreatment(apiReq, CardTopUpReq.class);
    log.info("卡充值交易请求参数:{}", cardTopUpReq);

    CardTopUpRsp cardTopUpRsp = new CardTopUpRsp();

    CardOrder cardOrder = new CardOrder();
    BeanUtils.copyProperties(cardTopUpReq, cardOrder);

    cardOrder.setId(IdGen.uuidString());
    cardOrder.setVersion(1);
    cardOrder.setOrganizationId(apiReq.getOrganizationId());
    cardOrder.setStatus(TradeStatusEnum.INIT.name());
    cardOrder.setChannelId("HFB");
    cardOrder.setCreateTime(new Date());
    cardOrder.setCardData(null);
    cardOrder.setAmount(new BigDecimal(cardTopUpReq.getAmount()));
    cardOrder.setPayType("TOPUP");
    cardOrderMapper.insert(cardOrder);

    cardTopUpReq.setOrderId(cardOrder.getId());
    log.info("卡充值交易请求通道信息: tradeNo:{}, request:{}", cardOrder.getTradeNo(), cardTopUpReq);
    ResponseData result = cardChannelService.topUp(cardTopUpReq);
    log.info("卡充值交易通道返回信息: tradeNo:{}, result:{}", cardOrder.getTradeNo(), result);

    if(ResultCode.SUCCESS.getCode() != result.getCode()){
      cardOrder.setStatus(TradeStatusEnum.FAIL.name());
      cardOrder.setRemark(result.getMsg());
      cardOrderMapper.updateByPrimaryKey(cardOrder);

      cardTopUpRsp.setSubCode(result.getCode());
      cardTopUpRsp.setSubMsg(result.getMsg());
      return (JSONObject) JSONObject.toJSON(cardTopUpRsp);
    }

    JSONObject resultData = (JSONObject) JSONObject.toJSON(result.getData());
    cardOrder.setStatus(resultData.getString("orderStatus"));
    cardOrder.setChannelTradeNo(resultData.getString("channelTradeNo"));
    cardOrder.setMercFee(new BigDecimal(resultData.getString("realAmount")).multiply(new BigDecimal("0.19")).setScale(2, BigDecimal.ROUND_HALF_UP));
    cardOrder.setChannelCost(new BigDecimal(resultData.getString("realAmount")).multiply(new BigDecimal("0.14")).setScale(2, BigDecimal.ROUND_HALF_UP));
    cardOrder.setRealAmount(new BigDecimal(resultData.getString("realAmount")));
    cardOrder.setSettleAmount(new BigDecimal(resultData.getString("realAmount")));
    cardOrderMapper.updateByPrimaryKey(cardOrder);


    BeanUtils.copyProperties(cardOrder, cardTopUpRsp);
    cardTopUpRsp.setOrderId(cardOrder.getId());
    cardTopUpRsp.setSubCode(ResultCode.SUCCESS.getCode());
    cardTopUpRsp.setSubMsg(ResultCode.SUCCESS.getMsg());
    return (JSONObject) JSONObject.toJSON(cardTopUpRsp);
  }
}
