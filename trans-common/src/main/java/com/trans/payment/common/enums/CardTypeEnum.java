package com.trans.payment.common.enums;

/**
 * @description: 卡类型枚举
 */
public enum CardTypeEnum {
  DEBIT(0, "借记卡"),
  CREDIT(1, "贷记卡");

  private int key;
  private String value;

  CardTypeEnum(int key, String value) {
    this.key = key;
    this.value = value;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }

  /**
   * 根据value找到对应枚举类型
   */
  public static CardTypeEnum queryByValue(String value){
    for (CardTypeEnum c: CardTypeEnum.values()){
      if(c.getValue().equals(value)){
        return c;
      }
    }

    return null;
  }

  /**
   * 判断数值是否属于枚举类的值
   * @param key
   * @return
   */
  public static boolean isInclude(int key){
    boolean include = false;
    for (CardTypeEnum c: CardTypeEnum.values()){
      if(c.getKey() == key){
        include = true;
        break;
      }
    }
    return include;
  }
}
