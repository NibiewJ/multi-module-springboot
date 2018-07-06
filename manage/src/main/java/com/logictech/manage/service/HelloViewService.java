package com.logictech.manage.service;

import java.io.IOException;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 14:05
 */
public interface HelloViewService {
    /** 获得信息
     * @param message
     * @return
     */
    String getMessage(String message) throws IOException;
}
