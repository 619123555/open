package com.trans.payment.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;

public interface RedisCacheDaoService {

  <T> void save(final String key, final T obj);

  <T> void save(final String key, final T obj, Long expires);

  <T> void delete(final String key, final Class<T> type);

  <T> T read(String key, Class<T> type);

  <T> T readAndDelete(String key, Class<T> type);

  <T> void pushSetItem(final String key, final T obj);

  <T> void pushSetItem(final String key, final T obj, Long expires);

  <T> void removeSetItem(final String key, final T obj);

  <T> Set<T> readSet(String key, Class<T> type);

  <T> void pushLisItem(String key, T obj);

  <T> void pushListItem(String key, T obj, Long expires);

  <T> void removeListItem(String key, T obj);

  <T> List<T> readList(String key, Class<T> type);

  Long incr(final String key);

  Long incr(final String key, final long seconds);

  Long incrBy(final String key, final Long integer);

  <T> Boolean setNx(final String key, final T obj, final Long expires);

  <T> void update(final String key, final T obj);

  <T> void update(final String key, final T obj, final long offset);

  Boolean hasKey(final String key);

  Boolean zadd(String key, double score, String member);

  Boolean zaddWithExpire(String key, double score, String member, int seconds);

  long zadd(String key, Map<String, Double> scoreMembers);

  long zcard(String key);

  long zcount(String key, double min, double max);

  long zlength(String key);

  double zincrby(String key, double score, String member);

  Set<String> zrange(String key, int start, int end);

  Set<String> zrangeByScore(String key, double min, double max);

  Long zrank(String key, String member);

  long zrevrank(String key, String member);

  Long zrem(String key, String member);

  Long zrem(String key);

  Long zremrangeByRank(String key, int start, int end);

  Long zremrangeByScore(String key, double min, double max);

  Set<String> zrevrange(String key, int start, int end);

  double zscore(String key, String memebr);

  RedisTemplate<Serializable, Serializable> getRedisTemplate();
}
