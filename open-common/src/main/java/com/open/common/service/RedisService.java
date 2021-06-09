package com.open.common.service;

import java.util.List;
import java.util.Set;

public interface RedisService {

  /**
   * redis设置字符串
   *
   * @param key k
   * @param value v
   * @return boolean
   */
  boolean set(final String key, final String value);

  /**
   * 设置字符串且加上过期时间
   *
   * @param key k
   * @param value v
   * @param expire e
   * @return boolean
   */
  boolean set(final String key, final String value, long expire);

  /**
   * hash set
   *
   * @param key k
   * @param item item
   * @param value v
   * @return boolean
   */
  boolean hset(String key, String item, String value);

  /**
   * 获取数据
   *
   * @param key k
   * @param item item
   * @return obj
   */
  Object hget(String key, String item);

  /**
   * 删除hash值
   *
   * @param key k
   * @param item item
   * @return Long
   */
  Long hdel(String key, Object... item);

  /**
   * get
   *
   * @param key key
   * @return String
   */
  String get(final String key);

  /**
   * 设置key的有效期
   *
   * @param key key
   * @param expire 时间
   * @return boolean
   */
  boolean expire(final String key, long expire);

  /**
   * setlist
   *
   * @param key key
   * @param list list
   * @param <T> t
   * @return T
   */
  <T> boolean setList(String key, List<T> list);

  /**
   * getlist
   *
   * @param key key
   * @param clz clz
   * @param <T> t
   * @return T
   */
  <T> List<T> getList(String key, Class<T> clz);

  /**
   * lpush
   *
   * @param key key
   * @param obj obj
   * @return long
   */
  long lpush(final String key, Object obj);

  /**
   * rpush
   *
   * @param key k
   * @param obj obj
   * @return long
   */
  long rpush(final String key, Object obj);

  /**
   * lpop
   *
   * @param key k
   * @return s
   */
  String lpop(final String key);

  /**
   * 删除
   *
   * @param key key
   * @return boolean
   */
  boolean del(final String key);

  /**
   * 清除redis数据
   *
   * @return boolean
   */
  boolean flushDb();

  /**
   * 返回rediskey总数
   *
   * @return Long
   */
  Long redisDbSize();

  /**
   * 计数器
   *
   * @param key k
   * @param seconds s
   * @return int
   */
  long incr(final String key, int seconds);

  long decr(final String key, int delta);

  /**
   * 生成递增参数
   *
   * @param key key
   * @return Long
   */
  long incr(final String key);

  /**
   * 查询key过期时间
   *
   * @param key k
   * @return Long
   */
  Long ttl(final String key);

  /**
   * setex
   *
   * @param key k
   * @param seconds s
   * @param value v
   * @return boolean
   */
  boolean setex(final String key, final int seconds, final String value);

  /**
   * key是否存在
   *
   * @param key key
   * @return boolean
   */
  boolean hasKey(final String key);

  /**
   * 模糊查询key集合
   *
   * @param key k
   * @return set
   */
  Set<String> keys(final String key);
}
