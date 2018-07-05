package com.logictech.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan({"com.logictech.framework","com.logictech.api"})
public class APIApplication {

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }
}
