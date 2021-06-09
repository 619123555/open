package com.open.common.utils.validation;

/** @author admin */
public enum ValidationRule {
  /** string */
  STRING,
  /** INTEGER */
  INTEGER,
  /** Regex expression: REX_BOOLEAN */
  BOOLEAN,
  /** Regex expression: REX_NUMERIC */
  NUMERIC,
  /** Regex expression: REX_DATE */
  DATE,
  /** Regex expression: REX_DATETIME */
  DATETIME,
  /** Regex expression: REX_IPV4 */
  IPV4,
  /** Regex expression: REX_IPV4 with *, 192.168.*.* */
  IPV4_ANONYMS,
  /** Regex expression: REX_EMAIL */
  EMAIL,
  /** Regex expression: REX_HTTP */
  HTTP,
  /** Regex expression: REX_URL */
  URL,
  /** Regex expression: REX_SAFE_REG_NAME (e.g. block characters */
  SAFE_REG_NAME,
  /** Regex expression: REX_SAFE_IMG_TYPE (image/gif, image/pjpeg, image/png, */
  SAFE_IMG_TYPE,
  /** Regex expression: REX_SAFE_IMG_EXT (gif|jpeg|jpg|png|bmp) */
  SAFE_IMG_EXT,
  // Regex expression: REX_PHONE_CA (e.g. 123-456-7890 88888)
  // PHONE_CA,
  /** Regex expression: REX_PHONE_CN (e.g. 10086,0731-88888888,13800000000) */
  PHONE_CN,
  /** Regex expression: REX_POSTALCODE_CA (e.g. 410000) */
  POSTALCODE_CN,
  /** Regex expression: REX_MOBILE (e.g. 13873100000) */
  MOBILE_PHONE,
}
