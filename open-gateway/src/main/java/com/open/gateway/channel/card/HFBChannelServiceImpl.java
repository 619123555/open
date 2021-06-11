package com.open.gateway.channel.card;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.utils.IdGen;
import com.open.common.utils.Md5Util;
import com.open.common.utils.StringUtils;
import com.open.common.utils.signature.AesUtilsHelp;
import com.open.gateway.service.NotifyService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: 汇付宝卡类交易通道
 */
@Service
@Slf4j
@AllArgsConstructor
public class HFBChannelServiceImpl implements CardChannelService, NotifyService {

  private final static String agentId = "";
  private final static String notifyUrl = "";
  private final static String md5Key = "";
  private final static String topUpUrl = "https://pay.Heepay.com/Api/CardPaySubmitService.aspx";
  private final static String queryUrl = "https://query.Heepay.com/Api/CardPayQueryService.aspx";

  @Override
  public ResponseData topUp() {
    Map<String, Object> req = new HashMap<>(16);
    req.put("agent_id", agentId);
    req.put("bill_id", IdGen.uuidString());
    req.put("bill_time", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
    req.put("card_type", "");
    req.put("card_data", "");
    req.put("pay_amt", "10");
    req.put("client_ip", "192.168.1.1");
    req.put("notify_url", notifyUrl);
    req.put("time_stamp", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
    String waitSign = AesUtilsHelp.sortMap(req);
    waitSign = waitSign + "|||" + md5Key;
    req.put("sign", Md5Util.MD5(waitSign));

    String result = HttpUtil.get(topUpUrl, req);
    if("".equals(result)){
      log.error("汇付宝充值返回空, tradeNo:{}", IdGen.uuidString());
    }

    JSONObject resultJson = JSONObject.parseObject(result);
    if(!"0".equals(resultJson.getString("ret_code"))){
      log.error("汇付宝充值失败, tradeNo:{}, 返回信息:{}", IdGen.uuidString(), resultJson.getString("ret_msg"));
    }

    return ResponseData.ok();
  }

  @Override
  public ResponseData query() {
    Map<String, Object> req = new HashMap<>(16);
    req.put("agent_id", agentId);
    req.put("bill_id", IdGen.uuidString());
    req.put("time_stamp", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
    String waitSign = AesUtilsHelp.sortMap(req);
    waitSign = waitSign + "|||" + md5Key;
    req.put("sign", Md5Util.MD5(waitSign));

    String result = HttpUtil.get(topUpUrl, req);
    if("".equals(result)){
      log.error("汇付宝充值查询返回空, tradeNo:{}", IdGen.uuidString());
    }

    JSONObject resultJson = JSONObject.parseObject(result);
    if(!"0".equals(resultJson.getString("ret_code"))){
      log.error("汇付宝充值查询失败, tradeNo:{}, 返回信息:{}", IdGen.uuidString(), resultJson.getString("ret_msg"));
    }

    return ResponseData.ok();
  }


  @Override
  public ResponseData execute(HttpServletRequest request) {
    JSONObject notifyParam = StringUtils.getAllRequestParam(request);
    log.info("汇付宝通知参数:{}", notifyParam);

    return ResponseData.ok();
  }
}
