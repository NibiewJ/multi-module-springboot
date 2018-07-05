package com.logictech.framework.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author JG.Hannibal
 * @since 2017/12/12 下午10:45
 */
@Component
public class StartupListenerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // todo something after App initialized.
        System.out.println("启动成功");
    }
}
    