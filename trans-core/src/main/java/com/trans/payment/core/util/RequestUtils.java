package com.trans.payment.core.util;

public class RequestUtils {
  private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal();

  public RequestUtils() {
  }

  public static String getTraceId() {
    return (String)THREAD_LOCAL.get();
  }

  public static void setTraceId(String traceId) {
    THREAD_LOCAL.set(traceId);
  }

  public static void removeTraceId() {
    THREAD_LOCAL.remove();
  }
}
