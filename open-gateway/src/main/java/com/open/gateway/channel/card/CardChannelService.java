package com.open.gateway.channel.card;

import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.CardTopUpReq;

/**
 * @description: 卡类交易通道接口
 */
public interface CardChannelService {

  // 充值
  ResponseData topUp(CardTopUpReq cardTopUpReq);

  // 查询
  ResponseData query(String tradeNo);
}
