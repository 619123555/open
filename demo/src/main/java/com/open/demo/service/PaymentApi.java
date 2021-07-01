package com.open.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.demo.model.DataRsp;
import java.util.HashMap;
import java.util.Map;

public class PaymentApi extends ApiAbstract{

  @Override
  public void executor() {
    Map<String, Object> dataMap = new HashMap<>(16);
//    dataMap.put("payType", "ALIPAY");
    dataMap.put("payType", "CARD");
    dataMap.put("tradeNo", System.currentTimeMillis() + "");
    dataMap.put("amount", 1); // 单位(分)
    dataMap.put("payeeAccNo", "6228480038125000000");
    dataMap.put("payeeAccName", "施祺");
    dataMap.put("payeeAccType", "0");
    dataMap.put("payeeBankCode", "308307000024");
    dataMap.put("payeeBankName", "招商银行股份有限公司连云港连云支行");
    dataMap.put("remark", "");

    String dataContent = JSON.toJSONString(dataMap);
    DataRsp dataRsp = reqSend("trans.payment.order", dataContent);
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
