package com.open.common.utils.httpclient;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Zale
 * @date 16/8/10
 */
public class OkHttpClient implements HttpClient {
  private okhttp3.OkHttpClient client;
  private static final String DEFAULT_CHARSET = "UTF-8";

  /** 默认60秒 */
  private long timeout = 60;

  public OkHttpClient() {
    client = buildHttpClient();
  }

  private okhttp3.OkHttpClient buildHttpClient() {
    return new okhttp3.OkHttpClient.Builder()
        .connectTimeout(getTimeout(), TimeUnit.SECONDS)
        .readTimeout(getTimeout(), TimeUnit.SECONDS)
        .writeTimeout(getTimeout(), TimeUnit.SECONDS)
        .build();
  }

  public long getTimeout() {
    return timeout;
  }

  public OkHttpClient(long timeout) {
    this.timeout = timeout;
    client = buildHttpClient();
  }

  public String get(String url, Map<String, String> map, String charset) throws IOException {
    if (map != null && map.size() > 0) {
      StringBuilder sb = new StringBuilder(url);
      sb.append("?");
      map.forEach(
          (k, v) -> {
            sb.append(k).append("=").append(v).append("&");
          });
      url = sb.deleteCharAt(sb.lastIndexOf("&")).toString();
    }
    Request request = new Request.Builder().url(url).get().build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  @Override
  public String postForm(String url, Map<String, String> map, String charset) throws IOException {
    FormBody.Builder builder = new FormBody.Builder();

    if (map != null) {
      boolean needChange = false;
      if (StringUtils.isNotEmpty(charset) && !DEFAULT_CHARSET.equals(charset.toUpperCase())) {
        needChange = true;
      }
      for (Map.Entry<String, String> entry : map.entrySet()) {
        String val = entry.getValue();
        if (Objects.nonNull(val)) {
          if (needChange) {
            val = new String(entry.getValue().getBytes(charset), DEFAULT_CHARSET);
          }
          builder.addEncoded(entry.getKey(), val);
        }
      }
    }
    FormBody body = builder.build();
    Request request = new Request.Builder().url(url).post(body).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body() == null ? null : response.body().string();
    }
  }

  @Override
  public String postJSON(String url, String json, String charset) throws IOException {
    RequestBody jsonBody =
        RequestBody.create(MediaType.parse("application/json; charset=" + charset), json);
    Request request = new Request.Builder().url(url).post(jsonBody).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
