package com.open.gateway.channel.card;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.CardTopUpReq;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.utils.Md5Util;
import com.open.common.utils.StringUtils;
import com.open.common.utils.signature.AesUtilsHelp;
import com.open.gateway.entity.CardOrder;
import com.open.gateway.mapper.CardOrderMapper;
import com.open.gateway.service.NotifyService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @description: 汇付宝卡类交易通道
 */
@Service("HFB")
@Slf4j
@RequiredArgsConstructor
public class HFBChannelServiceImpl implements CardChannelService, NotifyService {

  @Value("${web.host:1}")
  private String host;

  private final static String agentId = "";
  private final static String notifyUrl = "/notify/HFB";
  private final static String md5Key = "";
  private final static String topUpUrl = "https://pay.Heepay.com/Api/CardPaySubmitService.aspx";
  private final static String queryUrl = "https://query.Heepay.com/Api/CardPayQueryService.aspx";

  private final CardOrderMapper cardOrderMapper;

  @Override
  public ResponseData topUp(CardTopUpReq cardTopUpReq) {
    Map<String, Object> req = new HashMap<>(16);
    req.put("agent_id", agentId);
    req.put("bill_id", cardTopUpReq.getOrderId());
    req.put("bill_time", DateUtil.format(cardTopUpReq.getCreateTime(), DatePattern.PURE_DATETIME_FORMAT));
    req.put("card_type", cardTopUpReq.getCardType());
    req.put("card_data", cardTopUpReq.getCardData());
    req.put("pay_amt", cardTopUpReq.getAmount());
    req.put("client_ip", cardTopUpReq.getIp());
    req.put("notify_url", host + notifyUrl);
    req.put("time_stamp", DateUtil.format(cardTopUpReq.getCreateTime(), DatePattern.PURE_DATETIME_FORMAT));
    String waitSign = AesUtilsHelp.sortMap(req);
    waitSign = waitSign + "|||" + md5Key;
    req.put("sign", Md5Util.MD5(waitSign));

    log.info("汇付宝充值请求信息:{}", req);
    String result = HttpUtil.get(topUpUrl, req);
    log.info("汇付宝充值返回信息: tradeNo:{}, result:{}", cardTopUpReq.getTradeNo(), result);

    if(StringUtils.isEmpty(result)){
      return ResponseData.error("充值链路异常.");
    }

    JSONObject resultJson = JSONObject.parseObject(result);
    if(!"0".equals(resultJson.getString("ret_code"))){
      return ResponseData.error(resultJson.getString("ret_msg"));
    }

    JSONObject resp = new JSONObject();
    resp.put("channelTradeNo", resultJson.getString("jnet_bill_no"));
    resp.put("realAmount", resultJson.getString("card_real_amt"));
    resp.put("settleAmount", resultJson.getString("card_settle_amt"));
    if("0".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.DOING.name());
    }else if("1".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.SUCCESS.name());
    }else if("-1".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.FAIL.name());
    }
    return ResponseData.ok(resp);
  }

  @Override
  public ResponseData query(String tradeNo) {
    Map<String, Object> req = new HashMap<>(16);
    req.put("agent_id", agentId);
    req.put("bill_id", tradeNo);
    req.put("time_stamp", DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT));
    String waitSign = AesUtilsHelp.sortMap(req);
    waitSign = waitSign + "|||" + md5Key;
    req.put("sign", Md5Util.MD5(waitSign));

    log.info("汇付宝充值查询请求信息:{}", req);
    String result = HttpUtil.get(queryUrl, req);
    log.info("汇付宝充值查询返回信息: tradeNo:{}, result:{}", tradeNo, result);

    if(StringUtils.isEmpty(result)){
      return ResponseData.error("充值查询链路异常.");
    }

    JSONObject resultJson = JSONObject.parseObject(result);
    if(!"0".equals(resultJson.getString("ret_code"))){
      return ResponseData.error(resultJson.getString("ret_msg"));
    }

    JSONObject resp = new JSONObject();
    resp.put("channelTradeNo", resultJson.getString("jnet_bill_no"));
    resp.put("realAmount", resultJson.getString("card_real_amt"));
    resp.put("settleAmount", resultJson.getString("card_settle_amt"));
    if("0".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.DOING.name());
    }else if("1".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.SUCCESS.name());
    }else if("-1".equals(resultJson.getString("bill_status"))){
      resp.put("orderStatus", TradeStatusEnum.FAIL.name());
    }
    return ResponseData.ok(resp);
  }


  @Override
  public ResponseData execute(HttpServletRequest request) {
    JSONObject notifyParam = StringUtils.getAllRequestParam(request);
    log.info("汇付宝通知参数:{}", notifyParam);

    if(!"0".equals(notifyParam.getString("ret_code"))){
      return ResponseData.error(notifyParam.getString("ret_msg"));
    }

    CardOrder cardOrder = cardOrderMapper.selectByPrimaryKey(notifyParam.getString("jnet_bill_no"));
    log.info("根据通知查询到的订单:{}", cardOrder);
    if(cardOrder == null){
      return ResponseData.error("error");
    }

    if(TradeStatusEnum.SUCCESS.name().equals(cardOrder.getStatus())){
      return ResponseData.ok("ok");
    }

    cardOrder.setRealAmount(StringUtils.isEmpty(notifyParam.getString("card_real_amt")) ? null : new BigDecimal(notifyParam.getString("card_real_amt")));
    cardOrder.setSettleAmount(StringUtils.isEmpty(notifyParam.getString("card_real_amt")) ? null : new BigDecimal(notifyParam.getString("card_real_amt")));
    if("0".equals(notifyParam.getString("bill_status"))){
      cardOrder.setStatus(TradeStatusEnum.DOING.name());
    }else if("1".equals(notifyParam.getString("bill_status"))){
      cardOrder.setCompleteTime(new Date());
      cardOrder.setStatus(TradeStatusEnum.SUCCESS.name());
    }else if("-1".equals(notifyParam.getString("bill_status"))){
      cardOrder.setStatus(TradeStatusEnum.FAIL.name());
    }
    cardOrderMapper.updateByPrimaryKey(cardOrder);
    return ResponseData.ok("ok");
  }
}
