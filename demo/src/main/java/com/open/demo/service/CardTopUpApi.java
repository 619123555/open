package com.open.demo.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.demo.model.DataRsp;
import com.alibaba.fastjson.JSON;
import com.open.demo.utils.CardTypeEnum;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CardTopUpApi extends ApiAbstract{

  @Override
  public void executor() {
    Map<String, String> dataMap = new HashMap<>(16);
    dataMap.put("tradeNo", System.currentTimeMillis() + "");
    dataMap.put("amount", "10");
    dataMap.put("cardType", String.valueOf(CardTypeEnum.JUN_WANG.getKey()));
    dataMap.put("cardData", "18730241020");
    dataMap.put("ip", "192.168.0.5");
    dataMap.put("remark", "");
    dataMap.put("createTime", DateUtil.formatTime(new Date()));

    String dataContent = JSON.toJSONString(dataMap);
    DataRsp dataRsp = reqSend("trans.card.order.topup", dataContent);
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
