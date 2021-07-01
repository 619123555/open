package com.open.gateway.channel.payment;

import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.gateway.entity.PaymentOrder;

/**
 * @description: 代付交易通道接口
 */
public interface PaymentChannelService {

  ResponseData payment(PaymentOrder paymentOrder);

  ResponseData paymentInquiry();

}
