package com.open.common.enums;

/**
 *@Description: 代付相关订单交易类型
 */
public enum PaymentPayTypeEnum {
  ALIPAY("0001", "支付宝"),
  CARD("0002", "银行卡");

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
