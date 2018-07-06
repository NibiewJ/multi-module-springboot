package com.logictech.api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.logictech.api.restful.HelloRestfulController;
import com.logictech.framework.entity.so.AppException;
import com.logictech.framework.security.TokenInterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

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
//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret"))
//                .build();
//        DecodedJWT jwt = null;
//        try {
//            jwt = verifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new AppException("验证用户失败, 请重新登录后再试");
//        }
        return true;
    }

    /**
     * 将登陆用户设置到 IOC
     *
     * @param token
     */
    @Override
    public void setTokenUser(String token) throws UnsupportedEncodingException {
//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret"))
//                .build();
//        DecodedJWT jwt = null;
//        try {
//            jwt = verifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new AppException("验证用户失败, 请重新登录后再试");
//        }
//        if (jwt != null) {
//            Map<String, Claim> claims = jwt.getClaims();
//            Date expired = claims.get("expired").as(Date.class);
//            String username = claims.get("name").asString();
//            Integer sex = claims.get("sex").asInt();
//            logger.debug("过期时间为: {}", expired);
//            if (new Date().before(expired)) {
//                logger.debug("token 有效, 用户姓名为: {}", username);
//            }
//        }
    }
}
    