package com.logictech.manage.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * @author JG.Hannibal
 * @since 2017/11/10 下午11:55
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
