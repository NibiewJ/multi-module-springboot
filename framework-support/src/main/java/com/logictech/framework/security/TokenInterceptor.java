package com.logictech.framework.security;

import com.logictech.framework.annotation.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author JG.Hannibal
 * @since 2017/12/26 17:09
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    public static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Resource
    protected TokenInterceptorService tokenInterceptorService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.getAnnotation(Authorization.class) == null) {
            return true;
        }
        // 从header中得到token
        String authorization = request.getHeader("x-lt-token");
        logger.debug("拦截器到的token为: [{}]", authorization);
        if ("".equals(authorization) || authorization == null) {
            throw new UnknownTokenException();
        }

        // 验证 token
        boolean isTokenPassed = tokenInterceptorService.checkToken(authorization);
        // 如果验证token失败，并且方法注明了Authorization，返回401错误
        if (!isTokenPassed) {
            logger.debug("验证token失败");
            throw new UnknownTokenException();
        }
        tokenInterceptorService.setTokenUser(authorization);
        return true;
    }
}
    