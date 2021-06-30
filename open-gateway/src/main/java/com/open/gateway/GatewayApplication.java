package com.open.gateway;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.open"})
@MapperScan(basePackages = {"com.open.gateway.mapper"})
@EnableScheduling
public class GatewayApplication {
  public static void main(String[] args) {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    Security.addProvider(new BouncyCastleProvider());
    SpringApplication.run(GatewayApplication.class, args);
  }
}
