package com.open.gateway.service;

import com.open.common.dto.ResponseData;
import javax.servlet.http.HttpServletRequest;

public interface NotifyService {
  ResponseData execute(HttpServletRequest request);
}
