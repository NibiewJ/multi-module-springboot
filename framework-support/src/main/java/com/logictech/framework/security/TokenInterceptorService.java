package com.logictech.framework.security;

import java.io.UnsupportedEncodingException;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 22:38
 */
public interface TokenInterceptorService {

    /**
     * 确认 token 正确
     * @param token
     * @return
     */
    boolean checkToken(String token) throws UnsupportedEncodingException;

    /**
     * 将登陆用户设置到 IOC
     * @param token
     */
    void setTokenUser(String token) throws UnsupportedEncodingException;
}
