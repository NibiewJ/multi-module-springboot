package com.logictech.framework.config;

import com.logictech.framework.annotation.IgnoreCommonResponse;
import com.logictech.framework.entity.so.ResultEntity;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 14:38
 */
@ControllerAdvice
public class ResponseConfig implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Type type = returnType.getGenericParameterType();
        // 是否有IgnoreCommonResponse 注解
        boolean isIgnore = returnType.getMethod().getAnnotation(IgnoreCommonResponse.class) != null
                || String.class.equals(type);
        // 是否是 RestController
        boolean isRestController = returnType.getDeclaringClass().getAnnotation(RestController.class) != null;
        // 是否是 adminJsonController
        boolean isViewJSONController = returnType.getMethod().getAnnotation(ResponseBody.class) != null;

        // 不进行包装的
        boolean noAware = ResultEntity.class.equals(type) || isIgnore || !isRestController || isViewJSONController;
        return !noAware;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ResultEntity result = new ResultEntity(body);
        return result;
    }
}
    