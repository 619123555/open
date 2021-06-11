package com.open.common.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

  private static final char[] DIGITS =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  public Md5Util() {}

  public static String md5(String text, String inputCharset) {
    MessageDigest msgDigest = null;

    try {
      msgDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException var6) {
      throw new IllegalStateException("System doesn't support MD5 algorithm.");
    }

    try {
      msgDigest.update(text.getBytes(inputCharset));
    } catch (UnsupportedEncodingException var5) {
      throw new IllegalStateException("System doesn't support your  EncodingException.");
    }

    byte[] bytes = msgDigest.digest();
    String md5Str = new String(encodeHex(bytes));
    return md5Str;
  }

  public static char[] encodeHex(byte[] data) {
    int l = data.length;
    char[] out = new char[l << 1];
    int i = 0;

    for (int var4 = 0; i < l; ++i) {
      out[var4++] = DIGITS[(240 & data[i]) >>> 4];
      out[var4++] = DIGITS[15 & data[i]];
    }

    return out;
  }

  public static final String MD5(String s) {
    char[] hexDigits =
        new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    try {
      byte[] btInput = s.getBytes(Charset.forName("UTF-8"));
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      mdInst.update(btInput);
      byte[] md = mdInst.digest();
      int j = md.length;
      char[] str = new char[j * 2];
      int k = 0;

      for (int i = 0; i < j; ++i) {
        byte byte0 = md[i];
        str[k++] = hexDigits[byte0 >>> 4 & 15];
        str[k++] = hexDigits[byte0 & 15];
      }

      return (new String(str)).toUpperCase();
    } catch (Exception var10) {
      var10.printStackTrace();
      return "";
    }
  }

  public static String byteArrayToString(byte[] b) {
    StringBuilder resultSb = new StringBuilder();

    for (int i = 0; i < b.length; ++i) {
      resultSb.append(byteToHexString(b[i]));
    }

    return resultSb.toString();
  }

  private static String byteToHexString(byte b) {
    int n = b;
    if (b < 0) {
      n = 256 + b;
    }

    int d1 = n / 16;
    int d2 = n % 16;
    return DIGITS[d1] + DIGITS[d2] + "";
  }

  public static String md5(String password) {
    String resultString = null;

    try {
      resultString = new String(password);
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteArrayToString(md.digest(resultString.getBytes()));
      return resultString;
    } catch (Exception var3) {
      return null;
    }
  }
}
