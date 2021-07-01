package com.open.gateway.channel.payment;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.open.common.dto.ResponseData;
import com.open.common.enums.TradeStatusEnum;
import com.open.common.utils.shandeUtil.CertUtil;
import com.open.common.utils.shandeUtil.CryptoUtil;
import com.open.common.utils.shandeUtil.HttpClient;
import com.open.common.utils.shandeUtil.RandomStringGenerator;
import com.open.common.utils.shandeUtil.SDKConfig;
import com.open.common.utils.shandeUtil.SDKUtil;
import com.open.gateway.entity.PaymentOrder;
import com.open.gateway.service.NotifyService;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @description: 杉德代付
 */
@Service("SHAN_DE")
@Slf4j
public class ShanDePaymentChannelServiceImpl implements PaymentChannelService, NotifyService, InitializingBean {

  @Override
  public void afterPropertiesSet() throws Exception {
    //加载配置文件
    SDKConfig.getConfig().loadPropertiesFromSrc();
    //加载证书
    try {
      CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public ResponseData payment(PaymentOrder paymentOrder) {

    JSONObject request = new JSONObject();
    request.put("version", "01");								//版本号
    request.put("tranTime", DateUtil.format(paymentOrder.getCreateTime(), DatePattern.PURE_DATETIME_FORMAT));                     //交易时间
    request.put("orderCode", paymentOrder.getId());                      //订单号
//    request.put("timeOut", );                      //订单超时时间
    request.put("tranAmt", String.format("%012d", paymentOrder.getAmount().divide(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).toString()));                                 //金额
    request.put("currencyCode", paymentOrder.getCurrency());                    //币种
    if("0".equals(paymentOrder.getPayeeAccType())){
      // 对私
      request.put("productId", "00000004");              //产品ID 00000003 对公 00000004 对私
      request.put("accAttr", "0");                                            //账户属性     0-对私   1-对公
      request.put("accType", "4");                                            //账号类型      3-公司账户  4-银行卡
    } else if ("1".equals(paymentOrder.getPayeeAccType())) {
      // 对公
      request.put("productId", "00000003");              //产品ID 00000003 对公 00000004 对私
      request.put("accAttr", "1");                                            //账户属性     0-对私   1-对公
      request.put("accType", "3");                                            //账号类型      3-公司账户  4-银行卡
    }
    request.put("accNo", paymentOrder.getPayeeAccNo());                            //收款人账户号
    request.put("accName", paymentOrder.getPayeeAccName());                                       		//收款人账户名
//    request.put("provNo", "");                                              //收款人开户省份编码
//    request.put("cityNo", "");                                              //收款人开会城市编码
    request.put("bankName", paymentOrder.getPayeeBankName());                                            //收款账户开户行名称
    request.put("bankType", paymentOrder.getPayeeBankCode());                                			//收款人账户联行号
    request.put("remark", paymentOrder.getRemark());                                          	//摘要
    request.put("payMode", "1"); 											//付款模式
    request.put("channelType", "07");                                         //渠道类型
//    request.put("extendParams", "");										//业务扩展参数
//    request.put("reqReserved", "");                                         //请求方保留域
//    request.put("extend", "");                                              //扩展域
//    request.put("phone", "");												//手机号

    String merId = SDKConfig.getConfig().getMid(); 			//商户ID
    String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID

    log.info("杉德代付接口请求信息:{}", request);
    JSONObject result = requestServer(request, "/agentpay", "RTPM", merId, plMid);
    log.info("杉德代付接口返回信息:{}", result);

    if(null == result){
      return ResponseData.error("代付链路异常.");
    }

    if(!"0000".equals(result.getString("respCode"))){
      return ResponseData.error(result.getString("respDesc"));
    }

    JSONObject resp = new JSONObject();
    resp.put("channelTradeNo", result.getString("sandSerial"));
    if("0".equals(result.getString("resultFlag"))){
      resp.put("orderStatus", TradeStatusEnum.SUCCESS.name());
    }else if("1".equals(result.getString("resultFlag"))){
      resp.put("orderStatus", TradeStatusEnum.FAIL.name());
    }else if("2".equals(result.getString("resultFlag"))){
      resp.put("orderStatus", TradeStatusEnum.DOING.name());
    }

    return ResponseData.ok(resp);
  }

  @Override
  public ResponseData paymentInquiry() {
    JSONObject request = new JSONObject();
    request.put("version", "01");                     // 版本号
    request.put("productId", "00000004");              //产品ID 00000003 对公 00000004 对私
    request.put("tranTime", "20210119092720");           	      // 查询订单的交易时间 原订单的tranTime
    request.put("orderCode", "202101190927208");                  // 要查询的订单号
    request.put("extend", "");                                    // 扩展域
    String merId = SDKConfig.getConfig().getMid(); 			//商户ID
    String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID

    log.info("杉德代付订单查询接口请求信息:{}", request);
    JSONObject result = requestServer(request, "/queryOrder", "ODQU", merId, plMid);
    log.info("单的代付订单查询接口返回信息:{}", result);


    return null;
  }

  @Override
  public ResponseData execute(HttpServletRequest request) {
    return null;
  }


  public static JSONObject requestServer(JSONObject request, String reqAddr, String transCode, String merId, String plId) {

    String reqData = request.toJSONString();
    log.info("请求数据：\n"+reqData);

    try {

      String aesKey = RandomStringGenerator.getRandomStringByLength(16);
      byte[] aesKeyBytes = aesKey.getBytes("UTF-8");

      byte[] plainBytes = reqData.getBytes("UTF-8");
      String encryptData = new String(Base64.encodeBase64(
          CryptoUtil.AESEncrypt(plainBytes, aesKeyBytes, "AES",
              "AES/ECB/PKCS5Padding", null)),
          "UTF-8");

      String sign = new String(Base64.encodeBase64(
          CryptoUtil.digitalSign(plainBytes, CertUtil.getPrivateKey(),
              "SHA1WithRSA")), "UTF-8");

      String encryptKey = new String(Base64.encodeBase64(
          CryptoUtil.RSAEncrypt(aesKeyBytes, CertUtil.getPublicKey(), 2048, 11,
              "RSA/ECB/PKCS1Padding")), "UTF-8");

      String accessType = getAccessType(plId, merId);

      Map<String, String> reqMap = new HashMap<String, String>();
      //整体报文格式
      reqMap.put("transCode", transCode); // 交易码
      reqMap.put("accessType", accessType); // 接入类型
      reqMap.put("merId", merId); // 合作商户ID	杉德系统分配，唯一标识
      reqMap.put("plId", plId);  // 平台商户ID	平台接入必填，商户接入为空
      reqMap.put("encryptKey", encryptKey); // 加密后的AES秘钥
      reqMap.put("encryptData", encryptData); // 加密后的请求/应答报文
      reqMap.put("sign", sign); // 签名
      reqMap.put("extend", ""); // 扩展域

      String result;
      try {
        log.info("请求报文：\n"+reqMap);
        result = HttpClient
            .doPost(SDKConfig.getConfig().getUrl() + reqAddr, reqMap, 30000, 60000);
        result = URLDecoder.decode(result, "UTF-8");
      } catch (IOException e) {
        log.error(e.getMessage());
        return null;
      }

      log.info("响应报文：\n"+result);
      Map<String, String> responseMap = SDKUtil.convertResultStringToMap(result);

      String retEncryptKey = (String)responseMap.get("encryptKey");
      String retEncryptData = (String)responseMap.get("encryptData");
      String retSign = (String)responseMap.get("sign");

      log.debug("retEncryptKey:[" + retEncryptKey + "]");
      log.debug("retEncryptData:[" + retEncryptData + "]");
      log.debug("retSign:[" + retSign + "]");

      byte[] decodeBase64KeyBytes = Base64.decodeBase64(retEncryptKey
          .getBytes("UTF-8"));

      byte[] merchantAESKeyBytes = CryptoUtil.RSADecrypt(
          decodeBase64KeyBytes, CertUtil.getPrivateKey(), 2048, 11,
          "RSA/ECB/PKCS1Padding");

      byte[] decodeBase64DataBytes = Base64.decodeBase64(retEncryptData
          .getBytes("UTF-8"));

      byte[] respDataBytes = CryptoUtil.AESDecrypt(decodeBase64DataBytes,
          merchantAESKeyBytes, "AES", "AES/ECB/PKCS5Padding", null);

      String respData = new String(respDataBytes, "UTF-8");
      log.info("retData:[" + respData + "]");

      byte[] signBytes = Base64.decodeBase64(retSign
          .getBytes("UTF-8"));

      boolean isValid = CryptoUtil.verifyDigitalSign(respDataBytes, signBytes,
          CertUtil.getPublicKey(), "SHA1WithRSA");

      if(!isValid) {
        log.error("verify sign fail.");
        return null;
      }
      log.info("verify sign success");

      JSONObject respJson = JSONObject.parseObject(respData);
      return respJson;

    }  catch (Exception e) {

      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * 获取接入类型 0-商户接入，默认   1-平台接入
   *
   * @param plId
   * @param merId
   * @return
   */
  public static String getAccessType(String plId, String merId) {

    if(StringUtils.isNotBlank(plId)) {
      return "1";
    }
    return "0";
  }

}
