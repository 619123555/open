package com.open.common.enums;

/**
 * @description: 订单状态枚举
 */
public enum TradeStatusEnum {
  INIT("初始化"),
  DOING("处理中"),
  SUCCESS("成功"),
  FAIL("失败"),
  VOID("撤销");

  TradeStatusEnum(String desc) {
  }
}
