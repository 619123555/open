package com.trans.payment.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.trans.payment"})
@MapperScan(basePackages = {"com.trans.payment.*.mapper"})
public class CoreApplication {
  public static void main(String[] args) {
    SpringApplication.run(CoreApplication.class, args);
  }
}
