package com.open.demo;

import com.open.demo.service.ApiAbstract;
import com.open.demo.service.UserRelationRegisterApi;

public class Main {

  public static void main(String[] args) {
    // 银联用户拉新
    ApiAbstract apiAbstract = new UserRelationRegisterApi();
    apiAbstract.executor();
  }
}
