package com.open.gateway.util;

public class RedisKeyConfigure {
  public static final String PUSH_PREFIX = "PUSH";
  public static final String BROOKE_PREFIX = "BROOKE";
  public static final String USER_CENTER_KEY = "HQ_QRCODE_TRADE_TOPIC_WITH_USER_CENTER";

  public RedisKeyConfigure() {
  }

  public static String PushAppPushInfoCacheKey(String organizationId) {
    return RedisKeyConfigure.Key.with("PUSH").append("ORGANIZATION").append(organizationId).append("PUSH").string();
  }

  public static String BrookeAccessTokenCacheKey(String organizationId) {
    return RedisKeyConfigure.Key.with("BROOKE").append("TOKEN").append(organizationId).string();
  }

  public static String BrookeJsapiTicketCacheKey(String organizationId) {
    return RedisKeyConfigure.Key.with("BROOKE").append("JSAPI").append(organizationId).string();
  }

  public static String genKey(String serviceName, String... keys) {
    RedisKeyConfigure.Key key = RedisKeyConfigure.Key.with(serviceName);
    String[] var3 = keys;
    int var4 = keys.length;

    for(int var5 = 0; var5 < var4; ++var5) {
      String s = var3[var5];
      key.append(s);
    }

    return key.string();
  }

  public static class Key {
    private StringBuilder builder;

    private Key() {
      this.builder = new StringBuilder();
    }

    private Key(String key) {
      this();
      this.builder.append(key);
    }

    public static RedisKeyConfigure.Key with(String key) {
      return new RedisKeyConfigure.Key(key);
    }

    public RedisKeyConfigure.Key append(Object key) {
      this.builder.append(":").append(key);
      return this;
    }

    public String string() {
      return this.toString();
    }

    public String toString() {
      return this.builder.toString();
    }
  }
}
