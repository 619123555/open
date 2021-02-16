package com.trans.payment.core.channel;

import com.alibaba.fastjson.JSONObject;

public interface PaymentChannelService {

  JSONObject payment();

  JSONObject paymentInquiry();

  JSONObject notice();
}
