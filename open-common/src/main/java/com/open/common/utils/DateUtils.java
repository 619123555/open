package com.open.common.utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

  public static final String DAY_PATTERN = "yyyy-MM-dd";
  public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final Date DEFAULT_DATE = DateUtils.parseByDayPattern("1970-01-01");

  public static Date getBusinsessDate() {
    return new Date();
  }

  public static String getBusinsessDay() {
    return getDate("yyyyMMdd");
  }

  /**
   * 获取时间年月日时分
   *
   * @return
   */
  public static String getBusinsessMin() {
    return getDate("yyyyMMddHHmm");
  }

  private static String[] parsePatterns = {
    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
    "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
    "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
  };

  /** 得到当前日期字符串 格式（yyyy-MM-dd） */
  public static String getDate() {
    return getDate("yyyy-MM-dd");
  }

  /** 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E" */
  public static String getDate(String pattern) {
    return DateFormatUtils.format(new Date(), pattern);
  }

  /** 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E" */
  public static String formatDate(Date date, Object... pattern) {
    String formatDate = null;
    if (pattern != null && pattern.length > 0) {
      formatDate = DateFormatUtils.format(date, pattern[0].toString());
    } else {
      formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
    }
    return formatDate;
  }

  public static String formatDate(Date date, String patt) {
    SimpleDateFormat sdf = new SimpleDateFormat(patt);
    String format = sdf.format(date);
    return format;
  }

  /** 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss） */
  public static String formatDateTime(Date date) {
    return formatDate(date, "yyyy-MM-dd HH:mm:ss");
  }

  /** 得到当前时间字符串 格式（HH:mm:ss） */
  public static String getTime() {
    return formatDate(new Date(), "HH:mm:ss");
  }

  /** 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss） */
  public static String getDateTime() {
    return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
  }

  /** 得到当前年份字符串 格式（yyyy） */
  public static String getYear() {
    return formatDate(new Date(), "yyyy");
  }

  /** 得到当前月份字符串 格式（MM） */
  public static String getMonth() {
    return formatDate(new Date(), "MM");
  }

  /** 得到当天字符串 格式（dd） */
  public static String getDay() {
    return formatDate(new Date(), "dd");
  }

  /** 得到当前星期字符串 格式（E）星期几 */
  public static String getWeek() {
    return formatDate(new Date(), "E");
  }

  /**
   * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
   * "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd
   * HH:mm" }
   */
  public static Date parseDate(Object str) {
    if (str == null) {
      return null;
    }
    try {
      return parseDate(str.toString(), parsePatterns);
    } catch (ParseException e) {
      return null;
    }
  }

  /**
   * 获取过去的天数
   *
   * @param date
   * @return
   */
  public static long pastDays(Date date) {
    if (date != null) {
      long t = System.currentTimeMillis() - date.getTime();
      return t / (24 * 60 * 60 * 1000);
    } else {
      return 0;
    }
  }

  /**
   * 获取过去的小时
   *
   * @param date
   * @return
   */
  public static long pastHour(Date date) {
    long t = System.currentTimeMillis() - date.getTime();
    return t / (60 * 60 * 1000);
  }

  /**
   * 获取过去的分钟
   *
   * @param date
   * @return
   */
  public static long pastMinutes(Date date) {
    long t = System.currentTimeMillis() - date.getTime();
    return t / (60 * 1000);
  }

  /**
   * 转换为时间（天,时:分:秒.毫秒）
   *
   * @param timeMillis
   * @return
   */
  public static String formatDateTime(long timeMillis) {
    long day = timeMillis / (24 * 60 * 60 * 1000);
    long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
    long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
    long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
    long sss =
        (timeMillis
            - day * 24 * 60 * 60 * 1000
            - hour * 60 * 60 * 1000
            - min * 60 * 1000
            - s * 1000);
    return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
  }

  /**
   * 获取两个日期之间的天数
   *
   * @param before
   * @param after
   * @return
   */
  public static double getDistanceOfTwoDate(Date before, Date after) {
    long beforeTime = before.getTime();
    long afterTime = after.getTime();
    return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
  }

  /**
   * 计算几天前或几天后的日期
   *
   * @param dayCount 正数-几天前，负数-几天后
   * @param format 获取的日期格式
   * @return
   */
  public static String getDateBefore(int dayCount, String format) {
    Format f = new SimpleDateFormat(format);
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dayCount);
    return f.format(calendar.getTime());
  }

  /**
   * 计算传入时间date几天前或几天后的日期
   *
   * @param date 传入的时间
   * @param dayCount 正数-几天前，负数-几天后
   * @param format 获取的日期格式
   * @return
   */
  public static String getDateBefore(Date date, int dayCount, String format) {
    Format f = new SimpleDateFormat(format);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dayCount);
    return f.format(calendar.getTime());
  }

  /**
   * 判断字符串是否符合日期格式
   *
   * @param date
   * @return
   */
  public static boolean checkStrFormat(String date) {
    if ((date == null) || (date.length() < 8)) {
      return false;
    }
    try {
      boolean result = false;
      SimpleDateFormat formatter;
      char dateSpace = date.charAt(4);
      String[] format;
      if ((dateSpace == '-') || (dateSpace == '/')) {
        format = new String[4];
        String strDateSpace = Character.toString(dateSpace);
        format[0] = "yyyy" + strDateSpace + "MM" + strDateSpace + "dd";
        format[1] = "yyyy" + strDateSpace + "MM" + strDateSpace + "d";
        format[2] = "yyyy" + strDateSpace + "M" + strDateSpace + "dd";
        format[3] = "yyyy" + strDateSpace + "M" + strDateSpace + "d";
      } else {
        format = new String[1];
        format[0] = "yyyyMMdd";
      }

      for (int i = 0; i < format.length; i++) {
        String aFormat = format[i];
        formatter = new SimpleDateFormat(aFormat);
        formatter.setLenient(false);
        String tmp = formatter.format(formatter.parse(date));
        if (date.equals(tmp)) {
          result = true;
          break;
        }
      }
      return result;
    } catch (ParseException e) {
      return false;
    }
  }

  /**
   * 比较两个日期大小
   *
   * @param date1
   * @param date2
   * @param pattern
   * @return：1前者大于后者，-1前者小于后者，0两日期相等
   */
  public static int compareDate(String date1, String date2, String pattern) {
    DateFormat df = new SimpleDateFormat(pattern);
    try {
      Date dt1 = df.parse(date1);
      Date dt2 = df.parse(date2);
      if (dt1.getTime() > dt2.getTime()) {
        return 1;
      } else if (dt1.getTime() < dt2.getTime()) {
        return -1;
      } else {
        return 0;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return 0;
  }

  /**
   * 当前日期加月，或者天数
   *
   * @param date
   * @param months
   * @param days
   * @return
   */
  public static String addMonthsToDate(Date date, int months, int days) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, months);
    c.roll(Calendar.DATE, days);
    return DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
  }

  /**
   * Format date by 'yyyy-MM-dd HH:mm:ss' pattern
   *
   * @param date
   * @return
   */
  public static String formatByDateTimePattern(Date date) {
    return DateFormatUtils.format(date, DATETIME_PATTERN);
  }

  /**
   * Get current day using format date by 'yyyy-MM-dd HH:mm:ss' pattern
   *
   * @return
   * @author yebo
   */
  public static String getCurrentDayByDayPattern() {
    Calendar cal = Calendar.getInstance();
    return formatByDayPattern(cal.getTime());
  }

  /**
   * Format date by 'yyyy-MM-dd' pattern
   *
   * @param date
   * @return
   */
  public static String formatByDayPattern(Date date) {
    if (date != null) {
      return DateFormatUtils.format(date, DAY_PATTERN);
    } else {
      return null;
    }
  }

  /**
   * Parse date by 'yyyy-MM-dd' pattern
   *
   * @param str
   * @return
   */
  public static Date parseByDayPattern(String str) {
    return parseDate(str, DAY_PATTERN);
  }

  /**
   * Parse date by 'yyyy-MM-dd HH:mm:ss' pattern
   *
   * @param str
   * @return
   */
  public static Date parseByDateTimePattern(String str) {
    return parseDate(str, DATETIME_PATTERN);
  }

  public static Date parseDate(String str, String pattern) {
    try {
      return parseDate(str, new String[] {pattern});
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /** 最低年限 */
  public static final int MIN = 1930;

  /**
   * 验证小于当前日期 是否有效
   *
   * @param iYear 待验证日期(年)
   * @param iMonth 待验证日期(月 1-12)
   * @param iDate 待验证日期(日)
   * @return 是否有效
   */
  public static boolean valiDate(int iYear, int iMonth, int iDate) {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int datePerMonth;
    if (iYear < MIN || iYear >= year) {
      return false;
    }
    if (iMonth < 1 || iMonth > 12) {
      return false;
    }
    switch (iMonth) {
      case 4:
      case 6:
      case 9:
      case 11:
        datePerMonth = 30;
        break;
      case 2:
        boolean dm =
            ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
                && (iYear > MIN && iYear < year);
        datePerMonth = dm ? 29 : 28;
        break;
      default:
        datePerMonth = 31;
    }
    return (iDate >= 1) && (iDate <= datePerMonth);
  }

  /**
   * 格式化为标准时间 yyyyMMddHHmmss
   *
   * @param date
   * @return
   */
  public static String format2StandardDate(Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return simpleDateFormat.format(date);
  }

  public static int getSecondsBetweenDate(Date before, Date after) {
    Long sec = after.getTime() - before.getTime();
    Long t = sec / 1000;
    if(t > 0) {
      return t.intValue();
    } else {
      return 0;
    }
  }

  public static int getMinutesBetweenDate(Date before, Date after) {
    Long sec = after.getTime() - before.getTime();
    Long t = sec / (1000 * 60);
    if(t > 0) {
      return t.intValue();
    } else {
      return 0;
    }
  }

  public static Date parseToTodayDesignatedDate(Date date, String time) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd " + time);
    Date end = null;
    try {
      end = sdf.parse(sdf1.format(date));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return end;
  }
}
