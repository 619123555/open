package com.open.common.utils;


/**
 * 封装各种生成唯一性ID算法的工具类.
 *
 * @author Administrator
 */
public class IdGen {

  private static volatile IdWorker idWorker = null;

  public static IdWorker getIdWorker() {
    if (idWorker == null) {
      synchronized (IdWorker.class) {
        if (idWorker == null) {
          idWorker = SpringContextHolder.getBean(IdWorker.class);
        }
      }
    }
    return idWorker;
  }

  public static String uuidString() {
    return getIdWorker().nextId().toString();
  }

  public static long uuidLong() {
    return getIdWorker().nextId();
  }

  public static String uid() {
    return getIdWorker().systemTime() + uuidLong();
  }
}
