package com.trans.payment.common.utils.signature;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** @author admin */
public class AesSign {

  public String signAlgorithms = "SHA1PRNG";

  public AesSign() {}

  public AesSign(String signAlgorithms) {
    this.signAlgorithms = signAlgorithms;
  }

  /**
   * 加密
   *
   * @param content 需要加密的内容
   * @param password 加密密码
   * @return
   */
  public String encrypt(String content, String password) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom random = SecureRandom.getInstance(signAlgorithms);
    random.setSeed(password.getBytes());
    kgen.init(128, random);

    SecretKey secretKey = kgen.generateKey();
    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    byte[] byteContent = content.getBytes("utf-8");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String result = Base64Util.encryptBASE64(cipher.doFinal(byteContent));
    return result;
  }

  /**
   * 加密 密钥必须是16位的 待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出异常：
   *
   * @param content 需要加密的内容
   * @param password 加密密码
   * @return
   */
  public static byte[] encrypt2(String content, String password) {
    try {
      SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
      byte[] byteContent = content.getBytes("utf-8");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] result = cipher.doFinal(byteContent);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) throws Exception {

    System.out.println(decryptt("vNVhYNklk50tWUq0KYNRwuWd5aNSx24nT4hjLe7wrfNIibu6ULtC2xwJviPTUIYAI0HhwqM2ZHouffaQQYDakWuahDaFq9wZKfrKEdtCV1WGMewQIgIvE8yh9Y100000","ks9KUrbWJj46AftX"));
//    KeyGenerator kgen = null;
//    try {
//      kgen = KeyGenerator.getInstance("AES");
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    }
//    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//    random.setSeed("ks9KUrbWJj46AftX".getBytes());
//    kgen.init(128, random);
//
//    SecretKey secretKey = kgen.generateKey();
//
//    byte[] enCodeFormat = secretKey.getEncoded();
//    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//    Cipher cipher = null;
//    try {
//      cipher = Cipher.getInstance("AES");
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    } catch (NoSuchPaddingException e) {
//      e.printStackTrace();
//    }
//    cipher.init(Cipher.DECRYPT_MODE, key);
//    System.out.println(new String(cipher.doFinal(Base64Util.decryptBASE64("vNVhYNklk50tWUq0KYNRwuWd5aNSx24nT4hjLe7wrfNIibu6ULtC2xwJviPTUIYAI0HhwqM2ZHouffaQQYDakWuahDaFq9wZKfrKEdtCV1WGMewQIgIvE8yh9Y1"))));
  }

  /**
   * 解密
   *
   * @param content 待解密内容
   * @param password 解密密钥
   * @return
   */
  public String decrypt(String content, String password) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom random = SecureRandom.getInstance(signAlgorithms);
    random.setSeed(password.getBytes());
    kgen.init(128, random);

    SecretKey secretKey = kgen.generateKey();

    byte[] enCodeFormat = secretKey.getEncoded();
    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return new String(cipher.doFinal(Base64Util.decryptBASE64(content)));
  }

  /**
   * 将二进制转换成16进制
   *
   * @param buf
   * @return
   */
  public static String parseByte2HexStr(byte[] buf) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

  /**
   * 将16进制转换为二进制
   *
   * @param hexStr
   * @return
   */
  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1) {
      return null;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }

  public static String decryptt(String content, String password) throws Exception {
    byte[] aesKey = password.getBytes("UTF-8");
    SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    IvParameterSpec iv = new IvParameterSpec(aesKey, 0, cipher.getBlockSize());
    cipher.init(2, key, iv);
    return new String(cipher.doFinal(Base64Util.decryptBASE64(content)));
  }
}
