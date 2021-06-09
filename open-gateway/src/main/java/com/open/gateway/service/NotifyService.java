package com.open.gateway.service;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;

public interface NotifyService {

  /**
   * 接收上游异步通知入口
   */
  JSONObject execute(HttpServletRequest request);
}
