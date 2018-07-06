package com.logictech.api.service.impl;

import com.logictech.api.service.HelloRestfulService;
import com.logictech.framework.okhttp.OkHttpSender;
import org.springframework.stereotype.Service;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 14:08
 */
@Service
public class HelloRestfulServiceImpl implements HelloRestfulService {
    /**
     * 获得信息
     *
     * @param message
     * @return
     */
    @Override
    public String getMessage(String message) {
        return message;
    }
}
    