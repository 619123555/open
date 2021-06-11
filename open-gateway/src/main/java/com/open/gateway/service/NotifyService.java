package com.open.gateway.service;

import com.open.common.dto.ResponseData;
import javax.servlet.http.HttpServletRequest;

public interface NotifyService {

  /**
   * 接收上游异步通知入口
   */
  ResponseData execute(HttpServletRequest request);
}
