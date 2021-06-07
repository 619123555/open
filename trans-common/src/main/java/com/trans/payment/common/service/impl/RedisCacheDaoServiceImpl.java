package com.trans.payment.common.service.impl;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.trans.payment.common.service.RedisCacheDaoService;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisCacheDaoServiceImpl implements RedisCacheDaoService {
  private RedisTemplate<Serializable, Serializable> redisTemplate;

  @Autowired
  public RedisCacheDaoServiceImpl(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
    return this.redisTemplate;
  }

  public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public <T> void save(final String key, final T obj) {
    this.save(key, obj, (Long)null);
  }

  public <T> void save(final String key, final T obj, final Long expires) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        if (expires != null && expires > 0L) {
          connection.set(k, serializer.serialize(obj), Expiration.seconds(expires), SetOption.UPSERT);
        } else {
          connection.set(k, serializer.serialize(obj));
        }

        return null;
      }
    });
  }

  public <T> T readAndDelete(String key, Class<T> type) {
    return this.redisTemplate.execute(new RedisCallback<T>() {
      public T doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(type);
        byte[] k = serializer.serialize(key);
        if (connection.exists(k)) {
          T val = (T) serializer.deserialize(connection.get(k));
          connection.expire(k, 0L);
          return val;
        } else {
          return null;
        }
      }
    });
  }

  public <T> void delete(final String key, final Class<T> type) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(type);
        byte[] k = serializer.serialize(key);
        connection.expire(k, 0L);
        return null;
      }
    });
  }

  public <T> T read(final String key, final Class<T> type) {
    return this.redisTemplate.execute(new RedisCallback<T>() {
      public T doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(type);
        byte[] k = serializer.serialize(key);
        return connection.exists(k) ? (T) serializer.deserialize(connection.get(k)) : null;
      }
    });
  }

  public <T> void pushSetItem(final String key, final T obj) {
    this.pushSetItem(key, obj, (Long)null);
  }

  public <T> void pushSetItem(final String key, final T obj, final Long expires) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(obj);
        if (!connection.sIsMember(k, v)) {
          connection.sAdd(k, new byte[][]{v});
        }

        if (expires != null && expires > 0L) {
          connection.expire(k, expires);
        }

        return null;
      }
    });
  }

  public <T> Set<T> readSet(final String key, final Class<T> type) {
    return (Set)this.redisTemplate.execute(new RedisCallback<Set<T>>() {
      public Set<T> doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(type);
        byte[] k = serializer.serialize(key);
        if (!connection.exists(k)) {
          return null;
        } else {
          Set<byte[]> value = connection.sMembers(k);
          Set<T> rst = new HashSet();
          Iterator var6 = value.iterator();

          while(var6.hasNext()) {
            byte[] b = (byte[])var6.next();
            rst.add((T) serializer.deserialize(b));
          }

          return rst;
        }
      }
    });
  }

  public <T> void removeSetItem(final String key, final T obj) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(obj);
        if (!connection.sIsMember(k, v)) {
          connection.sRem(k, new byte[][]{v});
        }

        return null;
      }
    });
  }

  public <T> void pushLisItem(final String key, final T obj) {
    this.pushListItem(key, obj, (Long)null);
  }

  public <T> void pushListItem(final String key, final T obj, final Long expires) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(obj);
        connection.rPush(k, new byte[][]{v});
        if (expires != null && expires > 0L) {
          connection.expire(k, expires);
        }

        return null;
      }
    });
  }

  public <T> void removeListItem(final String key, final T obj) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(obj);
        connection.lRem(k, 1L, v);
        return null;
      }
    });
  }

  public <T> List<T> readList(final String key, final Class<T> type) {
    return (List)this.redisTemplate.execute(new RedisCallback<List<T>>() {
      public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(type);
        byte[] k = serializer.serialize(key);
        if (connection.exists(k)) {
          byte[] value = connection.get(k);
          return (List)serializer.deserialize(value);
        } else {
          return null;
        }
      }
    });
  }

  public Long incr(final String key) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Long.class);
        byte[] k = serializer.serialize(key);
        return connection.incr(k);
      }
    });
  }

  public Long incr(final String key, long seconds) {
    boolean bo = this.redisTemplate.hasKey(key);
    long count = (Long)this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
      RedisSerializer<String> redisSerializer = this.redisTemplate.getStringSerializer();
      return connection.incr(redisSerializer.serialize(key));
    });
    if (!bo) {
      this.expire(key, seconds);
    }

    return count;
  }

  private boolean expire(final String key, long expire) {
    return this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
  }

  public Long incrBy(final String key, final Long integer) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Long.class);
        byte[] k = serializer.serialize(key);
        return connection.incrBy(k, integer);
      }
    });
  }

  public <T> Boolean setNx(final String key, final T obj, final Long expires) {
    return (Boolean)this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
      FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
      byte[] k = serializer.serialize(key);
      Boolean isSet = connection.setNX(k, serializer.serialize(obj));
      if (isSet && expires != null && expires > 0L) {
        connection.expire(k, expires);
      }

      return isSet;
    });
  }

  public <T> void update(String key, T obj, long offset) {
    this.redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(obj.getClass());
        byte[] k = serializer.serialize(key);
        connection.setRange(k, serializer.serialize(obj), offset);
        return null;
      }
    });
  }

  public <T> void update(String key, T obj) {
    this.update(key, obj, 0L);
  }

  public Boolean hasKey(String key) {
    return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Long.class);
        byte[] k = serializer.serialize(key);
        return connection.exists(k);
      }
    });
  }

  public Boolean zadd(String key, double score, String member) {
    return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        return connection.zAdd(k, score, v);
      }
    });
  }

  public Boolean zaddWithExpire(String key, double score, String member, int seconds) {
    return (Boolean)this.redisTemplate.execute(new RedisCallback<Boolean>() {
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        Boolean exists = Boolean.FALSE;
        if (connection.exists(k)) {
          exists = Boolean.TRUE;
        }

        Boolean zAddReturn = connection.zAdd(k, score, v);
        if (!exists) {
          connection.expire(k, (long)seconds);
        }

        return zAddReturn;
      }
    });
  }

  public long zadd(String key, Map<String, Double> scoreMembers) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        Set<Tuple> tuples = new HashSet();
        Iterator var5 = scoreMembers.entrySet().iterator();

        while(var5.hasNext()) {
          Entry<String, Double> member = (Entry)var5.next();
          byte[] tempVal = serializer.serialize(member.getKey());
          Tuple tuple = new DefaultTuple(tempVal, (Double)member.getValue());
          tuples.add(tuple);
        }

        return connection.zAdd(k, tuples);
      }
    });
  }

  public long zcard(String key) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        return connection.zCard(k);
      }
    });
  }

  public long zcount(String key, double min, double max) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        return connection.zCount(k, min, max);
      }
    });
  }

  public long zlength(String key) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        Set<byte[]> set = connection.zRange(k, 0L, -1L);
        return (long)set.size();
      }
    });
  }

  public double zincrby(String key, double score, String member) {
    return (Double)this.redisTemplate.execute(new RedisCallback<Double>() {
      public Double doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        return connection.zIncrBy(k, score, v);
      }
    });
  }

  public Set<String> zrange(String key, int start, int end) {
    return (Set)this.redisTemplate.execute(new RedisCallback<Set<String>>() {
      public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        Set<byte[]> data = connection.zRange(k, (long)start, (long)end);
        Set<String> convert = new HashSet();
        Iterator var6 = data.iterator();

        while(var6.hasNext()) {
          byte[] barray = (byte[])var6.next();
          convert.add((String)serializer.deserialize(barray));
        }

        return convert;
      }
    });
  }

  public Set<String> zrangeByScore(String key, double min, double max) {
    return (Set)this.redisTemplate.execute(new RedisCallback<Set<String>>() {
      public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        Set<byte[]> data = connection.zRangeByScore(k, min, max);
        Set<String> convert = new HashSet();
        Iterator var6 = data.iterator();

        while(var6.hasNext()) {
          byte[] barray = (byte[])var6.next();
          convert.add((String)serializer.deserialize(barray));
        }

        return convert;
      }
    });
  }

  public Long zrank(String key, String member) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        return connection.zRank(k, v);
      }
    });
  }

  public long zrevrank(String key, String member) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        return connection.zRevRank(k, v);
      }
    });
  }

  public Long zrem(String key, String member) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(member);
        return connection.zRem(k, new byte[][]{v});
      }
    });
  }

  public Long zrem(String key) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        return connection.del(new byte[][]{k});
      }
    });
  }

  public Long zremrangeByRank(String key, int start, int end) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        return connection.zRemRange(k, (long)start, (long)end);
      }
    });
  }

  public Long zremrangeByScore(String key, double min, double max) {
    return (Long)this.redisTemplate.execute(new RedisCallback<Long>() {
      public Long doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Double.class);
        byte[] k = serializer.serialize(key);
        return connection.zRemRangeByScore(k, min, max);
      }
    });
  }

  public Set<String> zrevrange(String key, int start, int end) {
    return (Set)this.redisTemplate.execute(new RedisCallback<Set<String>>() {
      public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        Set<byte[]> data = connection.zRevRange(k, (long)start, (long)end);
        Set<String> convert = new HashSet();
        Iterator var6 = data.iterator();

        while(var6.hasNext()) {
          byte[] barray = (byte[])var6.next();
          convert.add((String)serializer.deserialize(barray));
        }

        return convert;
      }
    });
  }

  public double zscore(String key, String memebr) {
    return (Double)this.redisTemplate.execute(new RedisCallback<Double>() {
      public Double doInRedis(RedisConnection connection) throws DataAccessException {
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        byte[] k = serializer.serialize(key);
        byte[] v = serializer.serialize(memebr);
        return connection.zScore(k, v);
      }
    });
  }
}
