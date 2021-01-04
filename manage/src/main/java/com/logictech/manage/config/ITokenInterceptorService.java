package com.logictech.manage.config;

import com.logictech.framework.security.TokenInterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 22:46
 */
@Service
public class ITokenInterceptorService implements TokenInterceptorService {

    public static final Logger logger = LoggerFactory.getLogger(ITokenInterceptorService.class);
    /**
     * 确认 token 正确
     *
     * @param token
     * @return
     */
    @Override
    public boolean checkToken(String token) throws UnsupportedEncodingException {
        return true;
    }

    /**
     * 将登陆用户设置到 IOC
     *
     * @param token
     */
    @Override
    public void setTokenUser(String token) throws UnsupportedEncodingException {
    }
}
    