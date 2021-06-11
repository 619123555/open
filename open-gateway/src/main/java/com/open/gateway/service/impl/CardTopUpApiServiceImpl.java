package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.ApiReq;
import com.open.common.dto.gateway.CardTopUpReq;
import com.open.common.dto.gateway.CardTopUpResp;
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
import org.springframework.util.StringUtils;

/**
 * @description:
 */
@Service("trans.card.topup")
@Slf4j
public class CardTopUpApiServiceImpl extends AbstractApiService {

  @Autowired
  CardOrderMapper cardOrderMapper;
  @Autowired
  CardChannelService cardChannelService;

  @Override
  public ResponseData execute(ApiReq apiReq) throws GatewayException {
    CardTopUpReq cardTopUpReq = JSONObject.parseObject(apiReq.getData(), CardTopUpReq.class);
    log.info("卡充值交易请求参数:{}", cardTopUpReq);
    ValidatorUtils.gatewayValidateEntity(cardTopUpReq, AddGroup.class);

    CardOrder cardOrder = new CardOrder();
    BeanUtils.copyProperties(cardTopUpReq, cardOrder);

    cardOrder.setId(IdGen.uuidString());
    cardOrder.setVersion(1);
    cardOrder.setOrganizationId(apiReq.getOrganizationId());
    cardOrder.setStatus(TradeStatusEnum.INIT.name());
    cardOrder.setChannelId("HFB");
    cardOrder.setCreateTime(new Date());
    cardOrderMapper.insert(cardOrder);

    cardTopUpReq.setOrderId(cardOrder.getId());
    log.info("卡充值交易请求通道信息: tradeNo:{}, result:{}", cardOrder.getTradeNo(), cardTopUpReq);
    ResponseData result = cardChannelService.topUp(cardTopUpReq);
    log.info("卡充值交易通道返回信息: tradeNo:{}, result:{}", cardOrder.getTradeNo(), result);

    if(!ResultCode.SUCCESS.equals(result.getCode())){
      cardOrder.setStatus(TradeStatusEnum.FAIL.name());
      cardOrder.setRemark(result.getMsg());
      cardOrderMapper.updateByPrimaryKey(cardOrder);
      return ResponseData.error(result.getMsg());
    }

    JSONObject resultData = (JSONObject) JSONObject.toJSON(result.getData());
    cardOrder.setStatus(resultData.getString("orderStatus"));
    cardOrder.setChannelTradeNo(resultData.getString("channelTradeNo"));
    cardOrder.setRealAmount(StringUtils.isEmpty(resultData.getString("card_real_amt")) ? null : new BigDecimal(resultData.getString("card_real_amt")));
    cardOrder.setSettleAmount(StringUtils.isEmpty(resultData.getString("card_settle_amt")) ? null :new BigDecimal(resultData.getString("card_settle_amt")));
    cardOrderMapper.updateByPrimaryKey(cardOrder);

    CardTopUpResp cardTopUpResp = new CardTopUpResp();
    BeanUtils.copyProperties(cardOrder, cardTopUpResp);
    return ResponseData.ok();
  }
}
