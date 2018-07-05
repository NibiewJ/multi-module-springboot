package com.logictech.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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


    @GetMapping({"/", "index", "index.html"})
    public Map adminIndex() throws Exception {

        return new HashMap(3) {{
            put("code", 0);
            put("message", "成功");
            put("data", "This Active Profile is: [" + env + "], Project Name: [" + PROJECT_NAME + "]");
        }};
    }

}
    