package com.open.gateway.channel.card;

import com.open.common.dto.ResponseData;

/**
 * @description: 卡类交易通道接口
 */
public interface CardChannelService {

  // 充值
  ResponseData topUp();

  // 查询
  ResponseData query();
}
