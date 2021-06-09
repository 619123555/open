package com.open.demo.service;

import com.open.demo.model.DataRsp;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

public class UserRelationRegisterApi extends ApiAbstract{

  @Override
  public void executor() {
    Map<String, String> dataMap = new HashMap<>(16);
    dataMap.put("userMobile", "18730241020");
    dataMap.put("serialNumber", System.nanoTime() + "");

    String dataContent = JSON.toJSONString(dataMap);
    DataRsp dataRsp = reqSend("user.relation.register", dataContent);
  }
}
