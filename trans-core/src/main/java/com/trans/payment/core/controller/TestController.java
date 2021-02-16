package com.trans.payment.core.controller;

import com.trans.payment.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @Autowired
  TestService testService;

  @RequestMapping("/test")
  public void test(){
    testService.test();
  }
}
