package com.open.common.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.ReflectionUtils;

@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({RedissonProperties.class, RedisProperties.class})
public class RedissonConfiguration {
  private static final String REDIS_PROTOCOL_PREFIX = "redis://";
  private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

  @Autowired(required = false)
  private List<RedissonConfigurationCustomizer> redissonConfigurationCustomizers;

  @Autowired
  private RedissonProperties redissonProperties;

  @Autowired
  private RedisProperties redisProperties;

  @Autowired
  private ApplicationContext ctx;

  @Bean
  public RedissonClient redissonClient() {
    Config config = null;
    Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
    Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
    int timeout;
    if (null == timeoutValue) {
      timeout = 10000;
    } else if (!(timeoutValue instanceof Integer)) {
      Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
      timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
    } else {
      timeout = (Integer) timeoutValue;
    }
    if (redissonProperties.getConfig() != null) {
      try {
        config = Config.fromYAML(redissonProperties.getConfig());
      } catch (IOException e) {
        try {
          config = Config.fromJSON(redissonProperties.getConfig());
        } catch (IOException e1) {
          throw new IllegalArgumentException("Can't parse config", e1);
        }
      }
    } else if (redissonProperties.getFile() != null) {
      try {
        InputStream is = getConfigStream();
        config = Config.fromYAML(is);
      } catch (IOException e) {
        try {
          InputStream is = getConfigStream();
          config = Config.fromJSON(is);
        } catch (IOException e1) {
          throw new IllegalArgumentException("Can't parse config", e1);
        }
      }
    } else {
      config = new Config();
      String prefix = REDIS_PROTOCOL_PREFIX;
      Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
      if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
        prefix = REDISS_PROTOCOL_PREFIX;
      }

      config.useSingleServer()
          .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
          .setConnectTimeout(timeout)
          .setDatabase(redisProperties.getDatabase())
          .setPassword(redisProperties.getPassword());
    }
    if (redissonConfigurationCustomizers != null) {
      for (RedissonConfigurationCustomizer customizer : redissonConfigurationCustomizers) {
        customizer.customize(config);
      }
    }
    return Redisson.create(config);
  }

  private InputStream getConfigStream() throws IOException {
    Resource resource = ctx.getResource(redissonProperties.getFile());
    return resource.getInputStream();
  }
}
