package com.open.common.utils.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author Zale
 * @date 16/8/10
 */
public class ApacheHttpClient implements HttpClient {
  @Override
  public String postForm(String url, Map<String, String> t, String charset) throws IOException {
    List<NameValuePair> formparams = new ArrayList<>();
    if (t != null && t.size() > 0) {
      t.forEach((k, v) -> formparams.add(new BasicNameValuePair(k, v)));
    }

    UrlEncodedFormEntity formentity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
    HttpPost post = new HttpPost(url);
    post.setEntity(formentity);
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try (CloseableHttpResponse resposne = httpClient.execute(post)) {
      HttpEntity httpEntity = resposne.getEntity();
      if (httpEntity == null) {
        throw new ClientProtocolException("Response contains no content");
      }
      String jsonStr =
          convertStream2Json(
              httpEntity.getContent(), ContentType.getOrDefault(httpEntity).getCharset());
      return jsonStr;
    }
  }

  @Override
  public String postJSON(String url, String json, String charset) throws IOException {
    return null;
  }

  private String convertStream2Json(InputStream inputStream, Charset charset) {
    String jsonStr = "";
    // ByteArrayOutputStream相当于内存输出流
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len = 0;
    // 将输入流转移到内存输出流中
    try {
      while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
        out.write(buffer, 0, len);
      }
      // 将内存流转换为字符串
      jsonStr = new String(out.toByteArray(), charset);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return jsonStr;
  }
}
