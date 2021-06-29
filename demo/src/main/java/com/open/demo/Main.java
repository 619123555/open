package com.open.demo;

import com.open.demo.service.ApiAbstract;
import com.open.demo.service.CardOrderQueryApi;
import com.open.demo.service.CardTopUpApi;

public class Main {

  public static void main(String[] args) {
    // 充值
    ApiAbstract apiAbstract = new CardTopUpApi();
    apiAbstract.executor();

    // 充值查询
//    ApiAbstract apiAbstract = new CardOrderQueryApi();
//    apiAbstract.executor();
  }
}
