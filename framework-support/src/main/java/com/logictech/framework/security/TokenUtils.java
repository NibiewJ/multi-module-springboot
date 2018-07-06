package com.logictech.framework.security;

import io.jsonwebtoken.*;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JG.Hannibal
 * @since 2018/7/6 09:18
 */
public class TokenUtils {

    /**
     * 加密秘钥
     */
    private final static String BASE64_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    /**
     * 有效时间
     */
    private final static long EXPIRES_SECOND = 60 * 60 * 1000 * 3L;

    /**
     * 生成 token
     * @param map 需要放入 body 的数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String createToken(Map<String, Object> map) throws UnsupportedEncodingException {
        // jwt 生成
        String compactJws = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRES_SECOND))
                .signWith(SignatureAlgorithm.HS512, BASE64_SECRET).compact();
        return compactJws;
    }

    /**
     * 获得 body 的数据, 抛出异常则 token 无效
     * @param jwtStr token 字符串
     * @return
     */
    public static Map<String, Object> getBodyByToken(String jwtStr) throws UnknownTokenException {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(BASE64_SECRET))
                    .parseClaimsJws(jwtStr).getBody();
        } catch (ExpiredJwtException e) {
            throw new UnknownTokenException("用户登录已过期, 请重新登录!");
        }
        return claims;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(createToken(new HashMap(){{
            put("username", "J");
            put("admin", true);
        }}));

        String authentication = "eyJhbGciOiJIUzUxMiJ9.eyJhZG1pbiI6dHJ1ZSwiZXhwIjoxNTMwODU3NjM2LCJ1c2VybmFtZSI6IkoxIn0=.l4_jYcEpw3QeRg4w5F9N2zKTB0xeZrHJ-icSuKy1NDExTNVtj8ZKd-Zm7c44kd-BuA2qrHNcLPqDhRyjOYVk-Q";
        Map<String, Object> body = getBodyByToken(authentication);
        System.out.println("过期时间: "+ body.get("exp"));
        System.out.println("username: "+ body.get("username"));
        System.out.println("admin: "+ body.get("admin"));
    }
}
    