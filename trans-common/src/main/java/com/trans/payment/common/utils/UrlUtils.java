package com.trans.payment.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.codec.net.URLCodec;

/** @author zhouyang */
public class UrlUtils {

  public static String doFormUrlEncode(Map<String, String> params)
      throws UnsupportedEncodingException {
    StringBuffer buf = new StringBuffer();
    for (Entry<String, String> pair : params.entrySet()) {
      URLCodec codec = new URLCodec();
      if (pair.getKey() != null) {
        buf.append("&");
        buf.append(codec.encode(pair.getKey(), "utf-8"));
        buf.append("=");
        if (pair.getValue() != null) {
          buf.append(codec.encode(pair.getValue(), "utf-8"));
        }
      }
    }
    if (buf.length() > 0) {
      buf.deleteCharAt(0);
    }
    return buf.toString();
  }

  public static String encode(String param) throws UnsupportedEncodingException {
    URLCodec codec = new URLCodec();
    return codec.encode(param, "utf-8");
  }
}
