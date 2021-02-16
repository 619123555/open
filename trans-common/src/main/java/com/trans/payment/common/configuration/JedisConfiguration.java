package com.trans.payment.common.configuration;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
public class JedisConfiguration {

  @Bean
  public JedisCluster jedisCluster(){
    Set<HostAndPort> nodeList = new HashSet();
    nodeList.add(new HostAndPort("39.97.101.65", 6380));
    nodeList.add(new HostAndPort("39.97.101.65", 6381));
    nodeList.add(new HostAndPort("39.97.101.65", 6382));
    return new JedisCluster(nodeList, 3000, 10);
  }
}
