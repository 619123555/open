package com.trans.payment.boss.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DesUtils {
  private static final Logger logger = LoggerFactory.getLogger(DesUtils.class);

  private static byte[] md5(String strSrc)
  {
    byte[] returnByte = null;
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      returnByte = md5.digest(strSrc.getBytes("GBK"));
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return returnByte;
  }

  private static byte[] getEnKey(String spKey)
  {
    byte[] desKey = null;
    try {
      byte[] desKey1 = md5(spKey);
      desKey = new byte[24];
      int i = 0;
      while ((i < desKey1.length) && (i < 24)) {
        desKey[i] = desKey1[i];
        i++;
      }
      if (i < 24) {
        desKey[i] = 0;
        i++;
      }
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return desKey;
  }

  public static byte[] encrypt(byte[] src, byte[] enKey)
  {
    byte[] encryptedData = null;
    try {
      DESedeKeySpec dks = new DESedeKeySpec(enKey);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
      SecretKey key = keyFactory.generateSecret(dks);
      Cipher cipher = Cipher.getInstance("DESede");
      cipher.init(1, key);
      encryptedData = cipher.doFinal(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return encryptedData;
  }

  public static String getBase64Encode(byte[] src)
  {
    String requestValue = "";
    try {
      BASE64Encoder base64en = new BASE64Encoder();
      requestValue = base64en.encode(src);
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return requestValue;
  }

  private static String filter(String str)
  {
    String output = null;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < str.length(); i++) {
      int asc = str.charAt(i);
      if ((asc != 10) && (asc != 13)) {
        sb.append(str.subSequence(i, i + 1));
      }
    }
    output = new String(sb);
    return output;
  }

  public static String getURLEncode(String src)
  {
    String requestValue = "";
    try {
      requestValue = URLEncoder.encode(src);
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return requestValue;
  }

  public static String getDESEncrypt(String src, String spkey)
  {
    String requestValue = "";
    try
    {
      byte[] enKey = getEnKey(spkey);

      byte[] src2 = src.getBytes("UTF-16LE");

      byte[] encryptedData = encrypt(src2, enKey);

      String base64String = getBase64Encode(encryptedData);

      String base64Encrypt = filter(base64String);

      requestValue = getURLEncode(base64Encrypt);
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return requestValue;
  }

  public static String getURLDecoderdecode(String src)
  {
    String requestValue = "";
    try {
      requestValue = URLDecoder.decode(src);
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return requestValue;
  }

  public static String deCrypt(byte[] debase64, String spKey)
  {
    String strDe = null;
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("DESede");
      byte[] key = getEnKey(spKey);
      DESedeKeySpec dks = new DESedeKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
      SecretKey sKey = keyFactory.generateSecret(dks);
      cipher.init(2, sKey);
      byte[] ciphertext = cipher.doFinal(debase64);
      strDe = new String(ciphertext, "UTF-16LE");
    } catch (Exception ex) {
      strDe = "";
      ex.printStackTrace();
    }
    return strDe;
  }

  public static String getDESDecrypt(String src, String spkey)
  {
    String requestValue = "";
    try
    {
      String urlValue = getURLDecoderdecode(src);

      BASE64Decoder base64Decode = new BASE64Decoder();
      byte[] base64DValue = base64Decode.decodeBuffer(urlValue);

      requestValue = deCrypt(base64DValue, spkey);
    } catch (Exception e) {
      logger.debug("Exception:{}", e);
    }
    return requestValue;
  }
}
