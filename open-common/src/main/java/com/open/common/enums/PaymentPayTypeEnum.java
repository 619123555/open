package com.open.common.enums;

/**
 *@Description: 代付相关订单交易类型
 */
public enum PaymentPayTypeEnum {
  PAYMENT("代付", "0001"),
  PAYMENT_QUERY("代付订单查询", "0002");

  private String key;
  private String value;

  PaymentPayTypeEnum(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
