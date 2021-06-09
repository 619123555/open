package com.open.demo.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.open.demo.model.DataRsp;
import com.open.demo.utils.Base64Utils;
import com.open.demo.utils.Constant;
import com.open.demo.utils.RsaUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ApiAbstract {

  public abstract void executor();

  public DataRsp reqSend(String service, String dataContent){
    String data = null;
    try {
      data = Base64Utils
          .getBase64(RsaUtils.encryptByPublicKey(dataContent.getBytes("utf-8"), Constant.PUBLIC_KEY));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    Map<String, String> reqMap = new HashMap<>(16);
    reqMap.put("organizationId", Constant.ORGANIZATION_ID);
    reqMap.put("service", service);
    reqMap.put("signType", Constant.SIGN_TYPE);
    reqMap.put("version", Constant.VERSION);
    reqMap.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    // 请求流水号32位以内唯一字符串 对接过程中可以通过该id快速定位问题
    reqMap.put("requestId", System.nanoTime() + "");
    reqMap.put("data", data);
    String content = JSON.toJSONString(reqMap);

    System.out.println(content);
    String result = HttpUtil.post(Constant.GATEWAY_URL, content);
    System.out.println(result);

    if (!StrUtil.isEmpty(result)) {
      DataRsp dataRsp = JSONObject.parseObject(result, DataRsp.class);
      String code = dataRsp.getCode();
      // 网关成功才有data参数
      if ("10000".equals(code)) {
        String rspContent = new String(RsaUtils.decryptByPrivateKey(Base64Utils.getBytesBase64(dataRsp.getData()), Constant.PRIVATE_KEY));
        System.out.println("明文业务数据:" + rspContent);
        dataRsp.setData(rspContent);
      }
      return dataRsp;
    }
    return null;
  }

}
