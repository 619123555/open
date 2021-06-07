package com.trans.payment.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.trans.payment.common.service.RedisService;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RedisServiceImpl implements RedisService {

  private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

  @Resource private RedisTemplate<String, ?> redisTemplate;

  @Resource private StringRedisTemplate stringRedisTemplate;

  @Override
  public boolean set(final String key, final String value) {
    return redisTemplate.execute(
        (RedisCallback<Boolean>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              connection.set(serializer.serialize(key), serializer.serialize(value));
              return true;
            });
  }

  @Override
  public boolean set(final String key, final String value, final long expire) {
    return redisTemplate.execute(
        (RedisCallback<Boolean>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              connection.set(serializer.serialize(key), serializer.serialize(value));
              connection.expire(serializer.serialize(key), expire);
              return true;
            });
  }

  @Override
  public boolean hset(String key, String item, String value) {
    try {
      redisTemplate.opsForHash().put(key, item, value);
      return true;
    } catch (Exception e) {
      logger.error("IOException", e);
      return false;
    }
  }

  @Override
  public Object hget(String key, String item) {
    return redisTemplate.opsForHash().get(key, item);
  }

  @Override
  public Long hdel(String key, Object... item) {
    return redisTemplate.opsForHash().delete(key, item);
  }

  @Override
  public String get(final String key) {
    if (StringUtils.isEmpty(key)) {
      return null;
    }
    return redisTemplate.execute(
        (RedisCallback<String>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              byte[] value = connection.get(serializer.serialize(key));
              return serializer.deserialize(value);
            });
  }

  @Override
  public boolean expire(final String key, long expire) {
    return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
  }

  @Override
  public boolean hasKey(final String key) {
    return redisTemplate.hasKey(key);
  }

  @Override
  public Set<String> keys(final String key) {
    return redisTemplate.keys(key);
  }

  @Override
  public Long ttl(final String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  @Override
  public boolean setex(final String key, final int seconds, final String value) {
    boolean result =
        redisTemplate.execute(
            (RedisCallback<Boolean>)
                connection -> {
                  RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                  connection.set(serializer.serialize(key), serializer.serialize(value));
                  return true;
                });
    if (result) {
      expire(key, seconds);
    }
    return result;
  }

  @Override
  public <T> boolean setList(String key, List<T> list) {
    String value = JSON.toJSONString(list);
    return set(key, value);
  }

  @Override
  public <T> List<T> getList(String key, Class<T> clz) {
    String json = get(key);
    if (json != null) {
      List<T> list = JSON.parseArray(json, clz);
      return list;
    }
    return null;
  }

  /**
   * 往list头添加元素
   *
   * @param key key
   * @param obj obj
   * @return long
   */
  @Override
  public long lpush(final String key, Object obj) {
    final String value = JSON.toJSONString(obj);
    return redisTemplate.execute(
        (RedisCallback<Long>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
              return count;
            });
  }

  @Override
  public long rpush(final String key, Object obj) {
    final String value = JSON.toJSONString(obj);
    return redisTemplate.execute(
        (RedisCallback<Long>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
              return count;
            });
  }

  @Override
  public String lpop(final String key) {
    String result =
        redisTemplate.execute(
            (RedisCallback<String>)
                connection -> {
                  RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                  byte[] res = connection.lPop(serializer.serialize(key));
                  return serializer.deserialize(res);
                });
    return result;
  }

  @Override
  public boolean del(final String key) {
    return redisTemplate.execute(
        (RedisCallback<Boolean>)
            connection -> {
              RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
              connection.del(serializer.serialize(key));
              return true;
            });
  }

  @Override
  public boolean flushDb() {
    return redisTemplate.execute(
        (RedisCallback<Boolean>)
            connection -> {
              connection.flushDb();
              return true;
            });
  }

  /**
   * redis键值个数
   *
   * @return Long
   */
  @Override
  public Long redisDbSize() {
    Long size =
        (Long)
            redisTemplate.execute(
                (RedisCallback<Object>)
                    connection -> {
                      StringRedisConnection redis = (StringRedisConnection) connection;
                      return redis.dbSize();
                    });
    return size;
  }

  @Override
  public long incr(final String key, int seconds) {
    boolean bo = redisTemplate.hasKey(key);
    long count =
        redisTemplate.execute(
            (RedisCallback<Long>)
                connection -> {
                  RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                  return connection.incr(redisSerializer.serialize(key));
                });
    if (!bo) {
      expire(key, seconds);
    }
    return count;
  }

  @Override
  public long incr(final String key) {
    if (StringUtils.isEmpty(key)) {
      return 1L;
    }
    long count =
        redisTemplate.execute(
            (RedisCallback<Long>)
                connection -> {
                  RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                  return connection.incr(redisSerializer.serialize(key));
                });
    return count;
  }

    @Override
    public long decr(final String key, int seconds) {
        boolean bo = redisTemplate.hasKey(key);
        if (!bo) {
            expire(key, seconds);
        }
        return redisTemplate.execute(
                (RedisCallback<Long>)
                        connection -> {
                            RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                            return connection.decr(redisSerializer.serialize(key));
                        });
    }

  public long incrBy(final String key, long increment) {
    return redisTemplate.execute(
        (RedisCallback<Long>)
            connection -> {
              RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
              return connection.incrBy(redisSerializer.serialize(key), increment);
            });
  }
}
