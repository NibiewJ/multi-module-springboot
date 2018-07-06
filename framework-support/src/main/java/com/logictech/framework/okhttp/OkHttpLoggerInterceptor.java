package com.logictech.framework.okhttp;

import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author JG.Hannibal
 * @since 2018/1/5 13:23
 */
public class OkHttpLoggerInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    public static final Logger logger = LoggerFactory.getLogger(OkHttpSender.class);

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        RequestBody requestBody = request.body();

        String body = null;

        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            body = buffer.readString(charset);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("╔═════════════════════════════════");
            logger.debug("║ 请求方式: {} 请求地址: {}", request.method(), request.url());
            logger.debug("║ 请求时间: {} ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            logger.debug("║ Content-Type: {}", request.body().contentType());
            logger.debug("║ 请求参数: {}", body);
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        String rBody;
        BufferedSource source = responseBody.source();
        // Buffer the entire body.
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        rBody = buffer.clone().readString(charset);

        if (logger.isDebugEnabled()) {
            logger.debug("║ 响应时间: {} ms", tookMs);
            logger.debug("║ 响应编码: {} 响应消息: {}", response.code(), response.message());
            logger.debug("║ 请求地址: {}", response.request().url());
            logger.debug("║ 响应体: {}", rBody);
            logger.debug("╚═════════════════════════════════");
        }
        return response;
    }
}
    