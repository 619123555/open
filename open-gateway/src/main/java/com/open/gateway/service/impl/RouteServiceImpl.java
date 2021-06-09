package com.open.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.gateway.service.RouteService;
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
