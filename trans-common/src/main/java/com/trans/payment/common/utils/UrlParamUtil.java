package com.trans.payment.common.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author young
 * @date 1/14/16
 */
public class UrlParamUtil {

  public static String createLinkString(Map<String, Object> params) {
    return createLinkString(params, false);
  }

  public static String createLinkString(Map<String, Object> params, Boolean encodeValue) {
    if (params == null || params.size() == 0) {
      return "";
    }
    List<String> keys = new ArrayList<>(params.keySet());
    Collections.sort(keys);
    StringBuffer linkString = new StringBuffer();
    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      Object value = params.get(key);
      if (value == null) {
        continue;
      }
      if (encodeValue) {
        try {
          value = URLEncoder.encode(value.toString(), "UTF-8");
        } catch (Throwable th) {
          continue;
        }
      }
      if (linkString.length() > 0) {
        linkString.append("&");
      }
      linkString.append(key).append("=").append(value);
    }
    return linkString.toString();
  }
}
