package com.open.gateway.channel.card;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.dto.gateway.CardTopUpReq;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.utils.IdGen;
import com.open.common.utils.Md5Util;
import com.open.common.utils.StringUtils;
import com.open.common.utils.signature.AesUtilsHelp;
import com.open.gateway.entity.CardOrder;
import com.open.gateway.mapper.CardOrderMapper;
import com.open.gateway.service.NotifyService;
import java.math.BigDecimal;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

/**
 * @description: 汇付宝卡类交易通道
 */
@Service("HFB")
@Slf4j
@RequiredArgsConstructor
public class HFBChannelServiceImpl implements CardChannelService, NotifyService {

  @Value("${web.host:1}")
  private String host;

  private final static String agentId = "2130811";
  private final static String notifyUrl = "/notify/HFB";
  private final static String md5Key = "37470DD29E1442ACB882B1AF";
  private final static String desKey = "2A4855B0ADB3473985A7955E";
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
    req.put("card_data", encodeDES(desKey, cardTopUpReq.getCardData()));
    req.put("notify_url", host + notifyUrl);
    req.put("time_stamp", DateUtil.format(cardTopUpReq.getCreateTime(), DatePattern.PURE_DATETIME_FORMAT));
    req.put("pay_amt", cardTopUpReq.getAmount());
    req.put("client_ip", cardTopUpReq.getIp());
    String waitSign = "agent_id=" + req.get("agent_id") + "&bill_id=" + req.get("bill_id") + "&bill_time=" + req.get("bill_time") +
        "&card_type=" + req.get("card_type") + "&card_data=" + req.get("card_data") +
        "&pay_amt=" + req.get("pay_amt") + "&notify_url=" + req.get("notify_url") + "&time_stamp=" + req.get("time_stamp");
    waitSign = waitSign + "|||" + md5Key;
    req.put("sign", Md5Util.MD5(waitSign).toLowerCase());

    JSONObject goodsDetailJson = new JSONObject();
    goodsDetailJson.put("id", IdGen.uuidString().substring(0, 5));
    goodsDetailJson.put("name", "绿林侠盗:亡命之徒传说-充值");
    goodsDetailJson.put("num", "1");
    goodsDetailJson.put("price", cardTopUpReq.getAmount());
    goodsDetailJson.put("url", "端游联盟充值");
    req.put("goods_detail", goodsDetailJson);

    log.info("汇付宝充值请求信息:{}", req);
    String result = HttpUtil.get(topUpUrl, req);
    log.info("汇付宝充值返回信息: tradeNo:{}, result:{}", cardTopUpReq.getTradeNo(), result);

    if(StringUtils.isEmpty(result)){
      return ResponseData.error("充值链路异常.");
    }

    JSONObject resultJson = stringToJson(result);
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

    JSONObject resultJson = stringToJson(result);
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

  public String encodeDES(String datasource){
    try{
      SecureRandom random = new SecureRandom();
      DESKeySpec desKey = new DESKeySpec("2A4855B0ADB3473985A7955E".getBytes());
      //创建一个密匙工厂，然后用它把DESKeySpec转换成
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey securekey = keyFactory.generateSecret(desKey);
      //Cipher对象实际完成加密操作
      Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
      //用密匙初始化Cipher对象
      cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
      //现在，获取数据并加密
      return Base64.encodeBase64String(cipher.doFinal(datasource.getBytes()));
    }catch(Throwable e){
      e.printStackTrace();
      return null;
    }
  }


  public JSONObject stringToJson(String s){
    JSONObject result = new JSONObject();
    String[] strs = s.split("&");
    for(String kv: strs){
      if(kv.split("=").length == 2){
        result.put(kv.split("=")[0], kv.split("=")[1]);
      }
    }
    return result;
  }


  public static void main(String[] args) {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    Security.addProvider(new BouncyCastleProvider());
    System.out.println(encodeDES("JUNNET_123456_123456_COM", "123456"));
  }

  public static String encodeDES(String key, String msg){
    try {
      // 生成密钥
      byte[] bytes = key.getBytes("UTF-8");
      SecretKey deskey = new SecretKeySpec(bytes, "DESede");
      // 加密工具
      Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
      // 加密
      c1.init(Cipher.ENCRYPT_MODE, deskey);
      byte[] msgBytes = msg.getBytes("UTF-8");
      byte[] doFinal = c1.doFinal(msgBytes);
      for (int i = 0; i < doFinal.length; i++) {
        System.out.print(doFinal[i] + "\t");
      }
      return HexUtil.encodeHexStr(doFinal);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
