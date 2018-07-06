package com.logictech.framework.okhttp;

import okhttp3.*;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * @author JG.Hannibal
 * @since 2017/12/7 下午8:28
 */
public class OkHttpSender {
    public static final MediaType FROM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType XML = MediaType.parse("application/xml;charset=gbk");

    private static OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
            .hostnameVerifier((hostname, session) -> {
                //强行返回true 即验证成功
                return true;
            })
            .addInterceptor(new OkHttpLoggerInterceptor())
            .build();

    public static String postForm(String url, String requestData) throws IOException {
        return post(url, requestData, FROM).body().string();
    }

    public static String postGBK(String url, String requestData) throws IOException {
        // gbk
        return convertCharset(post(url, requestData, XML).body(), "gbk");
    }

    /**
     * 基础的 post 方法
     *
     * @param url         地址
     * @param requestData 数据
     * @param mediaType   Content-Type
     * @return
     * @throws IOException
     */
    public static Response post(String url, String requestData, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, requestData);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 基础的 get 方法
     *
     * @param url         地址
     * @param requestData 数据
     * @return
     * @throws IOException
     */
    public static Response get(String url, String requestData) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private static String convertCharset(ResponseBody origBody, String targetCharsetName) throws IOException {
        byte[] responseBytes = origBody.bytes();
        String responseGBK = new String(responseBytes, targetCharsetName);
        return responseGBK;
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
    