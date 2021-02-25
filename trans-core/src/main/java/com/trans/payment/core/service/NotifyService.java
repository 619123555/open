package com.trans.payment.core.service;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;

public interface NotifyService {
  JSONObject execute(HttpServletRequest request);
}
