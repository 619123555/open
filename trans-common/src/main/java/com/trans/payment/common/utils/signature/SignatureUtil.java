package com.trans.payment.common.utils.signature;

import com.trans.payment.common.utils.signature.sign.AssemblyUtil;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author admin @创建人：YYX @创建时间：2016/3/18 15:50 */
public class SignatureUtil {

  private static Logger logger = LoggerFactory.getLogger(HmacSign.class);

  /**
   * 加密
   *
   * @param signType
   * @param params
   * @param privatekey
   * @param algorithm
   * @return
   * @throws Exception
   */
  public static String sign(
      String signType, Map<String, Object> params, String privatekey, String algorithm)
      throws Exception {
    String result = "";
    Map<String, Object> tempResult = AssemblyUtil.paraFilter(params);
    String input = AssemblyUtil.createLinkString(tempResult);
    logger.info("return ===" + input);
    switch (signType) {
      case "AES":
        AesSign aesSign = new AesSign(algorithm);
        result = aesSign.encrypt(input, privatekey);
        break;
      case "DES":
        DesSign desSign = new DesSign(algorithm);
        result = desSign.encrypt(input.getBytes("UTF-8"), privatekey);
        break;
      case "HMAC":
        HmacSign sign = new HmacSign(algorithm);
        result = sign.encryptHMAC(input, privatekey);
        break;
      case "RSA":
        RsaSign rsaSign = new RsaSign(algorithm);
        result = rsaSign.sign(input, privatekey);
        break;
      case "MD5":
        result = Md5Util.mD5Encode(input + privatekey, "UTF-8");
        break;
      default:
        break;
    }
    return result;
  }

  /**
   * 验签
   *
   * @param signType
   * @param params
   * @param sign
   * @param signKey
   * @param algorithm
   * @return
   * @throws Exception
   */
  public static boolean verify(
      String signType, Map<String, Object> params, String sign, String signKey, String algorithm)
      throws Exception {
    boolean flag = false;
    String result = "";

    Map<String, Object> tempResult = AssemblyUtil.paraFilter(params);
    String input = AssemblyUtil.createLinkString(tempResult);
    logger.info("====" + input);
    switch (signType) {
      case "AES":
        AesSign aesSign = new AesSign(algorithm);
        result = aesSign.decrypt(sign, signKey);
        if (input.equals(result)) {
          flag = true;
        }
        break;
      case "DES":
        DesSign desSign = new DesSign(algorithm);
        result = desSign.decrypt(sign, signKey);
        if (input.equals(result)) {
          flag = true;
        }
        break;
      case "HMAC":
        HmacSign hmacSign = new HmacSign(algorithm);
        flag = hmacSign.checkSignature(input, signKey, sign);
        break;
      case "RSA":
        RsaSign rsaSign = new RsaSign(algorithm);
        flag = rsaSign.verify(input, sign, signKey);
        break;
      case "MD5":
        result = Md5Util.mD5Encode(input + signKey, "UTF-8");
        if (sign.equals(result)) {
          flag = true;
        }
        break;
      default:
        break;
    }
    return flag;
  }
}
