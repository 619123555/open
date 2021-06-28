package com.open.demo.utils;

/**
 * @description: 卡类型枚举
 */
public enum CardTypeEnum {
  HUI_YUAN_GE_REN(2, "汇元个人会员余额支付"),
  HUI_YUAN_SHANG_JIA(3, "汇元商家会员余额支付"),
  JUN_WANG(10, "骏网一卡通支付"),
  SHEN_ZHOU_XING(13, "神州行"),
  LIAN_TONG(14, "联通卡"),
  DIAN_XIN(15, "电信卡"),
  SHENG_DA(41, "盛大一卡通"),
  WANG_YI(42, "网易一卡通"),
  ZHENG_TU(43, "征途一卡通"),
  WAN_MEI(44, "完美一卡通"),
  SOU_HU(46, "搜狐一卡通"),
  JIU_YOU(47, "久游一卡通"),
  QQ(57, "QQ币充值卡"),
  GUANG_YU(58, "光宇一卡通");

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
