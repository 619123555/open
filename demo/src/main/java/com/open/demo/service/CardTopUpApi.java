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
    Map<String, Object> dataMap = new HashMap<>(16);
    dataMap.put("tradeNo", System.currentTimeMillis() + "");
    dataMap.put("amount", 10);
    dataMap.put("cardType", CardTypeEnum.JUN_WANG.getKey());
    dataMap.put("cardData", "2102020940320106,6663102102736909,1");
    dataMap.put("ip", "192.168.0.5");
    dataMap.put("remark", "");
    dataMap.put("createTime", DateUtil.formatDateTime(new Date()));

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
