package com.trans.payment.common.utils.httpclient;

import java.io.IOException;
import java.util.Map;

/**
 * @author Zale
 * @date 16/8/10
 */
public interface HttpClient {
  /**
   * httpclient form提交表单
   *
   * @param url 地址
   * @param t 参数
   * @param charset
   * @return String
   * @throws IOException
   */
  String postForm(String url, Map<String, String> t, String charset) throws IOException;

  /**
   * post提交json串
   *
   * @param url
   * @param json
   * @param charset
   * @return String
   * @throws IOException
   */
  String postJSON(String url, String json, String charset) throws IOException;
}
