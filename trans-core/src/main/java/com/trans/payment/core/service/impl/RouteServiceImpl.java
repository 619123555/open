package com.trans.payment.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trans.payment.core.service.RouteService;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {

  @Override
  public JSONObject execute() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("channelId", "testChannel");
    return jsonObject;
  }
}
