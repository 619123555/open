package com.trans.payment.boss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trans.payment"})
@MapperScan(basePackages = {"com.trans.payment.*.mapper"})
public class BossApplication {

  public static void main(String[] args) {
    SpringApplication.run(BossApplication.class, args);
  }
}
