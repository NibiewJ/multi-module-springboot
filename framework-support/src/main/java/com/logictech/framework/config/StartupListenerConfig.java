package com.logictech.framework.config;

import com.logictech.framework.aop.ControllerLoggerAop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(StartupListenerConfig.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // todo something after App initialized.
        logger.info("启动成功");
    }
}
    