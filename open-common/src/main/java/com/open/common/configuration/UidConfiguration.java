package com.open.common.configuration;

import com.open.common.utils.IdWorker;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UidConfiguration {

  @Bean
  public IdWorker getUidGenerator() {
    return new IdWorker(new Random().nextInt(31) + 1, new Random().nextInt(31) + 1);
  }
}
