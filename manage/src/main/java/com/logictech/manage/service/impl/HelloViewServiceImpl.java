package com.logictech.manage.service.impl;

import com.logictech.framework.okhttp.OkHttpSender;
import com.logictech.manage.service.HelloViewService;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 14:06
 */
@Service
public class HelloViewServiceImpl implements HelloViewService {
    /**
     * 获得信息
     *
     * @param message
     * @return
     */
    @Override
    public String getMessage(String message) throws IOException {
        OkHttpSender.post("http://localhost:8766/api/index", "{\"\"}", OkHttpSender.JSON);
        return message;
    }
}
    