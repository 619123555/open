package com.open.common.configuration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

  @Resource
  private RedisProperties redisProperties;
  @Value("${spring.redis.jedis.pool.max-active:300}")
  private int maxActive;
  @Value("${spring.redis.jedis.pool.max-idle:100}")
  private int maxIdle;
  @Value("${spring.redis.jedis.pool.max-wait:10000}")
  private Long maxWait;
  @Value("${spring.redis.jedis.pool.min-idle:10}")
  private int minIdle;
  @Value("${spring.redis.timeout:5000}")
  private int timeout;
  @Value("${spring.redis.maxAttempts:3}")
  private int maxAttempts;

  public RedisConfiguration() {
  }

  @Bean
  @ConditionalOnProperty("spring.redis.cluster.nodes")
  public JedisCluster getJedisCluster() {
    Cluster cluster = this.redisProperties.getCluster();
    if (cluster == null) {
      throw new IllegalArgumentException("redis cluster nodes not defined");
    } else {
      String password = this.redisProperties.getPassword();
      List<String> clusterNodes = cluster.getNodes();
      Set<HostAndPort> nodes = new HashSet(clusterNodes.size());
      Iterator var5 = clusterNodes.iterator();

      while(var5.hasNext()) {
        String ipPort = (String)var5.next();
        String[] ipPortPair = ipPort.split(":");
        nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
      }

      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxIdle(this.maxIdle);
      config.setMaxWaitMillis(this.maxWait);
      config.setMinIdle(this.minIdle);
      config.setMaxTotal(this.maxActive);
      return password != null ? new JedisCluster(nodes, this.timeout, this.timeout, this.maxAttempts, password, config) : new JedisCluster(nodes, this.timeout, this.maxAttempts, config);
    }
  }

  @Bean
  @ConditionalOnProperty("spring.redis.cluster.nodes")
  public RedisClusterConfiguration getClusterConfig() {
    Cluster cluster = this.redisProperties.getCluster();
    if (cluster == null) {
      throw new IllegalArgumentException("redis cluster nodes not defined");
    } else {
      RedisClusterConfiguration rcc = new RedisClusterConfiguration(this.redisProperties.getCluster().getNodes());
      rcc.setMaxRedirects(this.redisProperties.getCluster().getMaxRedirects());
      String password = this.redisProperties.getPassword();
      if (password != null) {
        rcc.setPassword(password);
      }

      return rcc;
    }
  }

  @Bean
  @ConditionalOnProperty("spring.redis.cluster.nodes")
  public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration cluster) {
    return new JedisConnectionFactory(cluster);
  }

  @Bean
  public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(connectionFactory);
    template.afterPropertiesSet();
    return template;
  }

}
