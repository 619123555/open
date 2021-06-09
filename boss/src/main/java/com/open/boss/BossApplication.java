package com.open.boss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.open"})
@MapperScan(basePackages = {"com.open.boss.mapper"})
public class BossApplication {

  public static void main(String[] args) {
    SpringApplication.run(BossApplication.class, args);
  }
}
