package com.logictech.framework.security;

import com.logictech.framework.entity.so.AppException;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 22:41
 */
public class UnknownTokenException extends AppException {
    public UnknownTokenException() {
        super("验证用户失败, 请重新登录后再试");
    }

    public UnknownTokenException(String message) {
        super(message);
    }
}