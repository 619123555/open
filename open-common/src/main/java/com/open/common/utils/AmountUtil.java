package com.open.common.utils;

import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/** @author admin */
public class AmountUtil {

  /**
   * 将元字符串转换为分
   *
   * @param amountStr
   * @return
   */
  public static BigDecimal parseAmountStr2BigDecimal(String amountStr) {
    if (amountStr == null || "".equals(amountStr)) {
      return new BigDecimal(0);
    }
    BigDecimal amount = new BigDecimal(amountStr);
    BigDecimal db = amount.multiply(new BigDecimal(100));
    return db;
  }

  /**
   * 将元字符串转换为分
   *
   * @param amount
   * @return
   */
  public static String parseBigDecimal2Fen(BigDecimal amount) {
    if (amount == null) {
      return "0";
    }
    BigDecimal db = amount.multiply(new BigDecimal(100));
    DecimalFormat df = new DecimalFormat("#");
    String s = df.format(db);
    return s;
  }

  /**
   * 将分字符串转换为元
   *
   * @param amountStr
   * @return
   */
  public static BigDecimal parseAmountStr2yan(String amountStr) {
    if (amountStr == null || "".equals(amountStr)) {
      return new BigDecimal(0);
    }
    BigDecimal amount = new BigDecimal(amountStr);
    DecimalFormat df = new DecimalFormat("0.00");
    BigDecimal d = amount.divide(new BigDecimal(100));
    String s = df.format(d);
    BigDecimal yamount = new BigDecimal(s);
    return (BigDecimal) yamount;
  }

  /**
   * 将分转换为元
   *
   * @param amountLong
   * @return
   */
  public static String parseAmountDouble2Str(Double amountLong) {
    if (amountLong == null) {
      return "0.00";
    }
    DecimalFormat df = new DecimalFormat("0.00");
    double d = amountLong / 100;
    String s = df.format(d);
    return s;
  }

  /**
   * 将元字符串转换为分
   *
   * @param amountStr
   * @return
   */
  public static long parseAmountStr2Long(String amountStr) {
    if (amountStr == null || "".equals(amountStr)) {
      return 0L;
    }
    double amount = Double.parseDouble(amountStr);
    Double db = amount * 100;
    DecimalFormat df = new DecimalFormat("#");
    String s = df.format(db);
    return Long.parseLong(s);
  }

  /**
   * 将字符串转换为Long
   *
   * @param amountStr
   * @return
   */
  public static long str2Long(String amountStr) {
    if (amountStr == null || "".equals(amountStr)) {
      return 0L;
    }
    double amount = Double.parseDouble(amountStr);
    Double db = amount;
    DecimalFormat df = new DecimalFormat("#");
    String s = df.format(db);
    return Long.parseLong(s);
  }

  /**
   * 将Long转str
   *
   * @param amountLong
   * @return
   */
  public static String long2Str(Long amountLong) {
    if (amountLong == null) {
      return "0";
    }
    String s = Long.toString(amountLong);
    return s;
  }

  /**
   * 将分转换为元
   *
   * @param amountLong
   * @return
   */
  public static String parseAmountLong2Str(Long amountLong) {
    if (amountLong == null) {
      return "0.00";
    }
    DecimalFormat df = new DecimalFormat("0.00");
    double d = amountLong / 100d;
    String s = df.format(d);
    return s;
  }

  /**
   * 将元转换为分
   *
   * @param yuan
   * @return
   */
  public static Long yuan2Fen(Double yuan) {
    Double dFen = yuan * 100;
    Long lFen = dFen.longValue();
    return lFen;
  }

  /**
   * 将分转换为元(四舍五入到分)
   *
   * @param fen
   * @return
   */
  public static Double fen2Yuan(Long fen) {
    Double yuan = fen / 100.00;
    BigDecimal big = new BigDecimal(fen / 100.00);
    yuan = big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    return yuan;
  }

  /**
   * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 表示表示需要精确到小数点以后几位。
   * @return 两个参数的商
   */
  public static double div(double v1, double v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /** 去掉小数点 */
  public static String spitStr(String amountStr) {
    int index = amountStr.indexOf(".");
    if (index != -1) {
      amountStr = amountStr.substring(0, index);
    }
    return amountStr;
  }

  /**
   * 判断是不是包含有小数 如果小数，
   *
   * @return
   */
  public static Map<String, String> checkBalanceDecimal(String balance) {
    Map<String, String> resMap = Maps.newHashMap();
    int balanceIndex = balance.indexOf(".");
    // 表示包含小数点
    if (balanceIndex != -1) {
      String balanceStr = balance.substring(0, balanceIndex) + "00";
      int balanceInt1 = Integer.valueOf(balanceStr).intValue();
      //
      String balance2 = "";
      try {
        long balanceLong = AmountUtil.parseAmountStr2Long(balance);
        balance2 = String.valueOf(balanceLong);
      } catch (Exception e) {
        resMap.put("retCode", "0001");
        resMap.put("retMsg", "金额异常");
        return resMap;
      }
      int balanceInt2 = Integer.valueOf(balance2).intValue();
      //
      if (balanceInt2 > balanceInt1) {
        resMap.put("retCode", "0001");
        resMap.put("retMsg", "请输入整数");
        return resMap;
      }
    }
    resMap.put("retCode", "0000");
    resMap.put("retMsg", "---");
    return resMap;
  }

  /**
   * 将分为单位的转换为元 （除100）
   *
   * @param amount
   * @return
   * @throws Exception
   */
  public static String changeF2Y(Long amount) {
    if (amount == null) {
      return "0.00";
    }
    return BigDecimal.valueOf(amount).divide(new BigDecimal(100)).toString();
  }
}
