package com.logictech.api.restful;

import com.logictech.api.service.HelloRestfulService;
import com.logictech.framework.config.StartupListenerConfig;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.logictech.bizz.consts.CommonConst.PROJECT_NAME;

/**
 * @author JG.Hannibal
 * @since 2018/7/4 13:06
 */
@RestController
@RequestMapping("/api")
public class HelloRestfulController {

    @Value("${env}")
    private String env;

    @Resource
    private HelloRestfulService helloRestfulService;


    public static final Logger logger = LoggerFactory.getLogger(HelloRestfulController.class);

    @PostMapping({"/", "index", "index.html"})
    public boolean adminIndex(@NotBlank String name) throws Exception {
        logger.info("This Active Profile is: [{}], Project Name: [{}]", env, PROJECT_NAME);
        return true;
    }

}