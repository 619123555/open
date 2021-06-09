package com.open.common.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author admin */
public class Validation {

  public static final Pattern REX_CAPITAL_PATTERN = Pattern.compile("[A-Z]");
  public static final Pattern REX_LOWERCASE_PATTERN = Pattern.compile("[a-z]");
  public static final Pattern REX_DIGITS_PATTERN = Pattern.compile("[0-9]");
  public static final Pattern REX_LETTERANDNUMBER_PATTERN = Pattern.compile("[A-Za-z0-9]+");

  public static final Pattern REX_INTEGER_PATTERN = Pattern.compile("^-?\\d+$");

  public static final Pattern REX_NATURAL_NUMBER_PATTERN = Pattern.compile("^\\d+$");

  public static final Pattern REX_BOOLEAN_PATTERN = Pattern.compile("^0|1|False|True|false|true$");
  public static final Pattern REX_NUMERIC_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
  public static final Pattern REX_DATE_PATTERN =
      Pattern.compile("^\\d{2,4}[-\\/]\\d{1,2}[-\\/]\\d{1,4}$");
  public static final Pattern REX_DATE_YYYYMM_PATTERN = Pattern.compile("^[0-9]{6}$");
  public static final Pattern REX_DATE_YYYYMMDD_PATTERN = Pattern.compile("^[0-9]{8}$");

  public static final Pattern REX_DATETIME_PATTERN =
      Pattern.compile("^\\d{2,4}[-\\/]\\d{1,2}[-\\/]\\d{1,4}(?:\\d{1,2}:\\d{1,2}:\\d{1,2})?$");

  public static final Pattern REX_IPV4_PATTERN =
      Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

  public static final Pattern REX_IPV4_ANONYMS_PATTERN =
      Pattern.compile("^(\\d{1,3})\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)$");

  public static final Pattern REX_EMAIL_PATTERN =
      Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

  public static final Pattern REX_HTTP_PATTERN =
      Pattern.compile(
          "^(REX_HTTP|https):\\/\\/[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*(\\.[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*)+(\\:\\d+)?(\\S+)?$");

  public static final Pattern REX_URL_PATTERN =
      Pattern.compile(
          "^[a-zA-Z]+:\\/\\/[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*(\\.[0-9a-zA-Z]+(\\-[0-9a-zA-Z]+)*)+(\\:\\d+)?(\\S+)?$");

  public static final Pattern REX_ID_LIST_PATTERN = Pattern.compile("^\\d+(?:,\\d+)*$");
  public static final Pattern REX_SAFE_REG_NAME_PATTERN =
      Pattern.compile("^[^'\"<>,&\\n\\r\\f\\t]+$");

  public static final Pattern REX_SAFE_IMG_TYPE_PATTERN =
      Pattern.compile("^(?:(?:image/gif)|(?:image/pjpeg)|(?:image/png)|(?:image/bmp))$");

  public static final Pattern REX_SAFE_IMG_EXT_PATTERN =
      Pattern.compile("\\.?(gif|jpeg|jpg|png|bmp)$");

  public static final Pattern REX_PHONE_CA_PATTERN =
      Pattern.compile("^\\d{3}\\-\\d{3}\\-\\d{4}( \\d{1,5})?$");

  public static final Pattern REX_POSTALCODE_CA_PATTERN =
      Pattern.compile("^[A-Za-z][0-9][A-Za-z] [0-9][A-Za-z][0-9]$");

  public static final Pattern REX_PHONE_CN_PATTERN =
      Pattern.compile("^(0\\d{2,4}\\-)?[^0]\\d{4,10}(\\-\\d{1,5})?$");

  public static final Pattern REX_POSTALCODE_CN_PATTERN = Pattern.compile("^\\d{6}$");

  public static final Pattern REX_MOBILE_PHONE_PATTERN = Pattern.compile("^1\\d{10}$");
  public static final Pattern REX_FIXED_TELEPHONE_PATTERN =
      Pattern.compile("^(0[0-9]{2,3}\\-())?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");

  public static final Pattern REX_LICENSE_PLATE_PATTERN =
      Pattern.compile("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
  public static final Integer[] ID_NUM_FACTOR =
      new Integer[] {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
  public static final String[] ID_NUM_PARITY_BIT =
      new String[] {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

  /**
   * 是否空字符串
   *
   * @param c
   * @return
   */
  public static boolean isEmpty(Object c) {
    return c == null || "".equals(c.toString().trim());
  }

  public static boolean isMobilePhone(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_MOBILE_PHONE_PATTERN.matcher(c).matches();
  }

  public static boolean isFixedTelephone(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_FIXED_TELEPHONE_PATTERN.matcher(c).matches();
  }

  public static boolean isDateTime(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_DATETIME_PATTERN.matcher(c).matches();
  }

  public static boolean isInteger(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_INTEGER_PATTERN.matcher(c).matches();
  }

  /**
   * isNaturalNumber（自然数：0 + 正整数）
   *
   * @param c
   * @return
   */
  public static boolean isNaturalNumber(Object c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_NATURAL_NUMBER_PATTERN.matcher(c.toString()).matches();
  }

  public static boolean isDate(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_DATE_PATTERN.matcher(c).matches();
  }

  public static boolean isNumeric(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_NUMERIC_PATTERN.matcher(c).matches();
  }

  public static boolean isEmail(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_EMAIL_PATTERN.matcher(c).matches();
  }

  public static boolean isHttp(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_HTTP_PATTERN.matcher(c).matches();
  }

  public static boolean isUrl(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_URL_PATTERN.matcher(c).matches();
  }

  public static boolean isPhoneCA(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_PHONE_CA_PATTERN.matcher(c).matches();
  }

  public static boolean isPhoneCN(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_PHONE_CN_PATTERN.matcher(c).matches();
  }

  public static boolean isPostalcodeCA(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_POSTALCODE_CA_PATTERN.matcher(c).matches();
  }

  public static boolean isPostalcodeCN(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_POSTALCODE_CN_PATTERN.matcher(c).matches();
  }

  public static boolean isIDList(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_ID_LIST_PATTERN.matcher(c).matches();
  }

  public static boolean isBoolean(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_BOOLEAN_PATTERN.matcher(c).matches();
  }

  public static boolean isSafeRegName(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_SAFE_REG_NAME_PATTERN.matcher(c).matches();
  }

  public static boolean isSafeImgType(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_SAFE_IMG_TYPE_PATTERN.matcher(c).matches();
  }

  public static boolean isSafeImgExt(String c) {
    if (isEmpty(c)) {
      return false;
    }
    return REX_SAFE_IMG_EXT_PATTERN.matcher(c).matches();
  }

  public static boolean isIPv4(String c) {
    if (isEmpty(c)) {
      return false;
    }
    Matcher match = REX_IPV4_PATTERN.matcher(c);
    if (match.matches()) {
      int ip1 = Integer.parseInt(match.group(1));
      int ip2 = Integer.parseInt(match.group(2));
      int ip3 = Integer.parseInt(match.group(3));
      int ip4 = Integer.parseInt(match.group(4));
      if (ip1 < 0 || ip1 > 255 || ip2 < 0 || ip2 > 255 || ip3 < 0 || ip3 > 255 || ip4 < 0
          || ip4 > 255) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  /**
   * isIPAnonyms, 192.168.1.*
   *
   * @param c
   * @return
   */
  public static boolean isIPv4Anonyms(String c) {
    if (isEmpty(c)) {
      return false;
    }
    Matcher match = REX_IPV4_ANONYMS_PATTERN.matcher(c);
    if (match.matches()) {
      int ip1 = Integer.parseInt(match.group(1).replace('*', '1'));
      int ip2 = Integer.parseInt(match.group(2).replace('*', '1'));
      int ip3 = Integer.parseInt(match.group(3).replace('*', '1'));
      int ip4 = Integer.parseInt(match.group(4).replace('*', '1'));
      if (ip1 < 0 || ip1 > 255 || ip2 < 0 || ip2 > 255 || ip3 < 0 || ip3 > 255 || ip4 < 0
          || ip4 > 255) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  /**
   * isSafePassword (Includes at least one small letter and one capital letter and one digit.)
   *
   * @param c
   * @return
   */
  public static boolean isSafePassword(String c) {
    if (isEmpty(c)) {
      return false;
    }
    if (REX_CAPITAL_PATTERN.matcher(c).matches()
        && REX_LOWERCASE_PATTERN.matcher(c).matches()
        && REX_DIGITS_PATTERN.matcher(c).matches()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 字母和数字
   *
   * @param c
   * @return
   */
  public static boolean isLetterAndNumber(String c) {
    if (isEmpty(c)) {
      return false;
    }
    if (REX_LETTERANDNUMBER_PATTERN.matcher(c).matches()) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isHexNumber(char c) {
    return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
  }

  /**
   * 判断是否为“”式的时期
   *
   * @param dateStr
   * @return
   */
  public static boolean isDate6(String dateStr) {
    if (isEmpty(dateStr) || !REX_DATE_YYYYMM_PATTERN.matcher(dateStr).matches()) {
      return false;
    }
    return isValidDateRange(date6Split(dateStr));
  }

  /**
   * 判断是否为“YYYYMMDD”式的时期
   *
   * @param dateStr
   * @return
   */
  public static boolean isDate8(String dateStr) {
    if (isEmpty(dateStr) || !REX_DATE_YYYYMMDD_PATTERN.matcher(dateStr).matches()) {
      return false;
    }
    return isValidDateRange(date8Split(dateStr));
  }

  public static boolean isLeapYear(Integer year) {
    return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
  }

  public static boolean isInvalidYear(Integer year) {
    return year < 1700 || year > 2500;
  }

  public static boolean isInvalidMonth(Integer month) {
    return month < 1 || month > 12;
  }

  public static boolean isInvalidDay(Integer day, Integer month, Integer year) {
    Integer[] iaMonthDays = new Integer[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    if (isLeapYear(year)) {
      iaMonthDays[1] = 29;
    }
    return day < 1 || day > iaMonthDays[month - 1];
  }

  /**
   * split date 0-YY,1-MM,2-DD
   *
   * @param dateStr
   * @return
   */
  private static Integer[] date6Split(String dateStr) {
    final Integer yearBase = 1900;
    Integer year = null, month = null, day = null;

    year = yearBase + Integer.valueOf(dateStr.substring(0, 2));
    month = Integer.valueOf(dateStr.substring(2, 4));
    day = Integer.valueOf(dateStr.substring(4, 6));
    return new Integer[] {year, month, day};
  }

  /**
   * split date 0-YYYY,1-MM,2-DD
   *
   * @param dateStr
   * @return
   */
  private static Integer[] date8Split(String dateStr) {
    Integer year = null, month = null, day = null;

    year = Integer.valueOf(dateStr.substring(0, 4));
    month = Integer.valueOf(dateStr.substring(4, 6));
    if (dateStr.length() == 8) {
      day = Integer.valueOf(dateStr.substring(6, 8));
      return new Integer[] {year, month, day};
    } else {
      return new Integer[] {year, month};
    }
  }

  private static boolean isValidDateRange(Integer[] dateSplitResult) {
    Integer year = dateSplitResult[0], month = dateSplitResult[1], day = dateSplitResult[2];
    if (isInvalidYear(year)) {
      return false;
    }
    if (isInvalidMonth(month)) {
      return false;
    }
    if (isInvalidDay(day, month, year)) {
      return false;
    }
    return true;
  }

  /**
   * 18位/15位身份证号码校验
   *
   * @param idNumber
   * @return
   */
  public static boolean isIdentityCardNum(String idNumber) {
    if (isEmpty(idNumber) || (idNumber.length() != 18)) {
      return false;
    }

    // initialize
    if (idNumber.length() == 18) {
      // check date
      String date8 = idNumber.substring(6, 14);
      if (isDate8(date8) == false) {
        return false;
      }
      int totalMulAiWi = 0;
      char charAt;
      // check and set value, calculate the totalmulAiWi
      for (int i = 0; i < 17; i++) {
        charAt = idNumber.charAt(i);
        if (charAt < '0' || charAt > '9') {
          return false;
        }
        totalMulAiWi += Integer.valueOf(String.valueOf(charAt)) * ID_NUM_FACTOR[i];
      }

      // calculate the check digit
      String checkDigit = ID_NUM_PARITY_BIT[totalMulAiWi % 11];
      // check last digit
      if (!checkDigit.equalsIgnoreCase(String.valueOf(idNumber.charAt(17)))) {
        return false;
      }
    } else { // length is 15
      // check date
      String date6 = idNumber.substring(6, 12);
      if (isDate6(date6) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * 车牌号校验
   *
   * @param licensePlate
   * @return
   */
  public static boolean isLicensePlate(String licensePlate) {
    if (isEmpty(licensePlate)) {
      return false;
    }
    return REX_LICENSE_PLATE_PATTERN.matcher(licensePlate).matches();
  }

  public static boolean isValidBooleanValue(Integer intValue) {
    if (intValue == null) {
      return false;
    }
    if (intValue < 0 || intValue > 1) {
      return false;
    }
    return true;
  }
}
