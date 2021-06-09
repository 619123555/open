package com.open.common.utils;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {
  private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
  private static final char SEPARATOR = '_';
  private static final String CHARSET_NAME = "UTF-8";
  private static final String UNKNOWN_CHAR = "unknown";
  private static final Pattern NUMBER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

  public StringUtils() {
  }

  public static byte[] getBytes(String str) {
    if (str != null) {
      try {
        return str.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var2) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static String toString(byte[] bytes) {
    try {
      return new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException var2) {
      return "";
    }
  }

  public static boolean inString(String str, String... strs) {
    if (str != null) {
      String[] var2 = strs;
      int var3 = strs.length;

      for(int var4 = 0; var4 < var3; ++var4) {
        String s = var2[var4];
        if (str.equals(trim(s))) {
          return true;
        }
      }
    }

    return false;
  }

  public static Double toDouble(Object val) {
    if (val == null) {
      return 0.0D;
    } else {
      try {
        return Double.valueOf(trim(val.toString()));
      } catch (Exception var2) {
        return 0.0D;
      }
    }
  }

  public static boolean isNumber(String str) {
    return NUMBER_PATTERN.matcher(str).matches();
  }

  public static Float toFloat(Object val) {
    return toDouble(val).floatValue();
  }

  public static Long toLong(Object val) {
    return toDouble(val).longValue();
  }

  public static Integer toInteger(Object val) {
    return toLong(val).intValue();
  }

  public static String getRemoteAddr(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }

    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }

    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("HTTP_CLIENT_IP");
    }

    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
    }

    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("x-real-ip");
    }

    if (org.springframework.util.StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }

    String[] ipAddresses = ipAddress.split(",");
    if (ipAddresses.length > 1) {
      ipAddress = ipAddresses[0];
    }

    return "0:0:0:0:0:0:0:1".equals(ipAddress) ? "127.0.0.1" : ipAddress;
  }

  public static String toCamelCase(String s) {
    if (s == null) {
      return null;
    } else {
      s = s.toLowerCase();
      StringBuilder sb = new StringBuilder(s.length());
      boolean upperCase = false;

      for(int i = 0; i < s.length(); ++i) {
        char c = s.charAt(i);
        if (c == '_') {
          upperCase = true;
        } else if (upperCase) {
          sb.append(Character.toUpperCase(c));
          upperCase = false;
        } else {
          sb.append(c);
        }
      }

      return sb.toString();
    }
  }

  public static String toCapitalizeCamelCase(String s) {
    if (s == null) {
      return null;
    } else {
      s = toCamelCase(s);
      return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
  }

  public static String toUnderScoreCase(String s) {
    if (s == null) {
      return null;
    } else {
      StringBuilder sb = new StringBuilder();
      boolean upperCase = false;

      for(int i = 0; i < s.length(); ++i) {
        char c = s.charAt(i);
        boolean nextUpperCase = true;
        if (i < s.length() - 1) {
          nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
        }

        if (i > 0 && Character.isUpperCase(c)) {
          if (!upperCase || !nextUpperCase) {
            sb.append('_');
          }

          upperCase = true;
        } else {
          upperCase = false;
        }

        sb.append(Character.toLowerCase(c));
      }

      return sb.toString();
    }
  }

  public static String jsGetVal(String objectString) {
    StringBuilder result = new StringBuilder();
    StringBuilder val = new StringBuilder();
    String[] vals = split(objectString, ".");

    for(int i = 0; i < vals.length; ++i) {
      val.append("." + vals[i]);
      result.append("!" + val.substring(1) + "?'':");
    }

    result.append(val.substring(1));
    return result.toString();
  }

  public static String decodeHttpParam(HttpServletRequest request, String param) {
    if (!isBlank(param)) {
      String value = request.getParameter(param);
      if (!isBlank(value)) {
        try {
          String newString = new String(value.getBytes("iso8859-1"), "utf-8");
          return newString.trim();
        } catch (Exception var4) {
        }
      }
    }

    return null;
  }

  public static String subSplitToInStr(String str) {
    StringBuffer conditionStr = new StringBuffer();
    String resStr = "";
    if (str != null && !"".equals(str)) {
      conditionStr.append("('");
      String[] strs = str.split(",");
      String[] var4 = strs;
      int var5 = strs.length;

      for(int var6 = 0; var6 < var5; ++var6) {
        String strp = var4[var6];
        conditionStr.append(strp).append("','");
      }

      resStr = conditionStr.substring(0, conditionStr.length() - 2) + ")";
    }

    return resStr;
  }

  public static String getOrigin(HttpServletRequest request, String webUrl) {
    Enumeration en = request.getHeaders("Origin");
    String[] urls = webUrl.split(",");

    while(en.hasMoreElements()) {
      String reqOrigin = (String)en.nextElement();
      String[] var5 = urls;
      int var6 = urls.length;

      for(int var7 = 0; var7 < var6; ++var7) {
        String url = var5[var7];
        if (reqOrigin.equals(url)) {
          return reqOrigin;
        }
      }
    }

    return null;
  }

  public static String getOrigin(ServletRequest req) {
    HttpServletRequest request = (HttpServletRequest)req;
    Enumeration en = request.getHeaders("Origin");
    return en.hasMoreElements() ? (String)en.nextElement() : null;
  }

  public static byte[] getBytes(String data, String encode) {
    try {
      return null == data ? null : data.getBytes(encode);
    } catch (UnsupportedEncodingException var3) {
      throw new RuntimeException(var3);
    }
  }

  public static String getBodyString(HttpServletRequest request) {
    ServletInputStream ris = null;

    try {
      ris = request.getInputStream();
      StringBuilder content = new StringBuilder();
      byte[] b = new byte[1024];
      boolean var4 = true;

      int lens;
      while((lens = ris.read(b)) > 0) {
        content.append(new String(b, 0, lens));
      }

      return content.toString();
    } catch (IOException var5) {
      var5.printStackTrace();
      return null;
    }
  }

  public static String generateSignatureParam(JSONObject sortedParams) {
    StringBuilder content = new StringBuilder();
    List<String> keys = new ArrayList(sortedParams.keySet());
    Collections.sort(keys);

    for(int i = 0; i < keys.size(); ++i) {
      String key = (String)keys.get(i);
      String value = (String)sortedParams.get(key);
      if (value != null && !"".equals(value)) {
        content.append((i == 0 ? "" : "&") + key + "=" + value);
      }
    }

    return content.toString();
  }

  public static JSONObject getAllRequestParam(final HttpServletRequest request) {
    JSONObject params = new JSONObject();
    Map requestParams = request.getParameterMap();
    Iterator iter = requestParams.keySet().iterator();

    while(iter.hasNext()) {
      String name = (String)iter.next();
      String[] values = (String[])((String[])requestParams.get(name));
      String valueStr = "";

      for(int i = 0; i < values.length; ++i) {
        valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
      }

      params.put(name, valueStr);
    }

    return params;
  }

  public static String hashHmac(String data, String key) {
    try {
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
      sha256Hmac.init(secretKey);
      byte[] array = sha256Hmac.doFinal(data.getBytes("UTF-8"));
      StringBuilder sb = new StringBuilder();
      byte[] var6 = array;
      int var7 = array.length;

      for(int var8 = 0; var8 < var7; ++var8) {
        byte item = var6[var8];
        sb.append(Integer.toHexString(item & 255 | 256).substring(1, 3));
      }

      return sb.toString().toUpperCase();
    } catch (Exception var10) {
      var10.printStackTrace();
      logger.error("关注微信配置签名出异常" + var10.getMessage());
      return "";
    }
  }

  public static String getPicSuffix(String imgPath) {
    return imgPath.substring(imgPath.lastIndexOf(".")).trim().toLowerCase();
  }

  public static boolean isNotEmptyBatch(String... strs) {
    String[] var1 = strs;
    int var2 = strs.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      String str = var1[var3];
      if (isEmpty(str)) {
        return false;
      }
    }

    return true;
  }

  public static String newString(byte[] buff, String code) {
    if (null == buff) {
      return null;
    } else {
      String res = null;

      try {
        res = new String(buff, code);
        return res;
      } catch (Exception var4) {
        throw new RuntimeException(var4);
      }
    }
  }
}
