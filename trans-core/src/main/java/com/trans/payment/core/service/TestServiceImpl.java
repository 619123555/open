package com.trans.payment.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

  private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);


  @Override
  public void test() {
    logger.info("测试能不能用");
  }
}
