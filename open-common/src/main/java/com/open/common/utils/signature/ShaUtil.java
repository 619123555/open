package com.open.common.utils.signature;

import java.security.MessageDigest;
import org.springframework.util.StringUtils;

/**
 * @author zhaoyang
 * @date 30/12/2016
 */
public class ShaUtil {

  public static final char[] HEXDIGITS = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public static String sha1(String str) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    try {
      MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
      mdTemp.update(str.getBytes("UTF-8"));
      byte[] md = mdTemp.digest();
      int j = md.length;
      char[] buf = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        buf[k++] = HEXDIGITS[byte0 >>> 4 & 0xf];
        buf[k++] = HEXDIGITS[byte0 & 0xf];
      }
      return new String(buf);
    } catch (Throwable th) {
      return null;
    }
  }
}
