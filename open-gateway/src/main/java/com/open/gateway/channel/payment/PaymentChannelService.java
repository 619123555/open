package com.open.gateway.channel.payment;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 代付交易通道接口
 */
public interface PaymentChannelService {

  JSONObject payment();

  JSONObject paymentInquiry();

  JSONObject notice();
}
