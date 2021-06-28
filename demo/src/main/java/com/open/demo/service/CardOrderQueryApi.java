package com.open.demo.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.demo.model.DataRsp;
import java.util.HashMap;
import java.util.Map;

public class CardOrderQueryApi extends ApiAbstract{

  @Override
  public void executor() {
    Map<String, String> dataMap = new HashMap<>(16);
    dataMap.put("tradeNo", System.currentTimeMillis() + "");

    String dataContent = JSON.toJSONString(dataMap);
    DataRsp dataRsp = reqSend("trans.card.order.query", dataContent);
    if(dataRsp == null){
      System.out.println(dataRsp.getCode());
      return;
    }
    if (!"10000".equals(dataRsp.getCode())) {
      System.out.println(dataRsp.getMsg());
      return;
    }

    JSONObject resultData = JSONObject.parseObject(dataRsp.getData());
    System.out.println("业务数据:" + resultData.toJSONString());
  }
}
