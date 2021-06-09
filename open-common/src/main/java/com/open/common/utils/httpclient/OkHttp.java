package com.open.common.utils.httpclient;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class OkHttp {

    private static final MediaType JSON =
            MediaType.get(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE);

    private static final MediaType XML =
            MediaType.get(org.springframework.http.MediaType.APPLICATION_XML_VALUE);

    private static OkHttpClient okHttpClient;

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    static {
        ConnectionPool pool = new ConnectionPool(200, 5, TimeUnit.MINUTES);
        okHttpClient =
                new OkHttpClient.Builder()
                        .retryOnConnectionFailure(false)
                        .connectionPool(pool)
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .sslSocketFactory(createSSLSocketFactory())
                        .hostnameVerifier(new TrustAllHostnameVerifier())
                        .build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return ssfFactory;
    }

    private static class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String doPost(String url, Map<String, String> maps) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<Entry<String, String>> entrySet = maps.entrySet();
        for (Entry<String, String> entry : entrySet) {
            formBodyBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        RequestBody body = formBodyBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        return execute(request);
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json) {
        Request request =
                new Request.Builder()
                        .url(url)
                        .addHeader("Connection", "close")
                        .post(RequestBody.create(JSON, json))
                        .build();
        return execute(request);
    }

    public static String get(String url, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while(iterator.hasNext()) {
                Entry entry = (Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Request request =
                new Request.Builder().url(sb.toString()).addHeader("Connection", "close").build();
        return execute(request);
    }

    public static String get(String url) {
        return get(url, null);
    }

    public static String postXml(String url, String xml) {
        RequestBody requestBody = RequestBody.create(XML, xml);
        Request request =
                new Request.Builder().url(url).post(requestBody).addHeader("Connection", "close").build();
        return execute(request);
    }

    public static String postJson(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request =
                new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();
        return execute(request);
    }

    public static String put(String url) {
        Request request = new Request.Builder().url(url).addHeader("Connection", "close").build();
        return execute(request);
    }

    public static String putJson(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request =
                new Request.Builder().url(url).addHeader("Connection", "close").put(body).build();
        return execute(request);
    }

    private static String execute(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("Exception", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}
