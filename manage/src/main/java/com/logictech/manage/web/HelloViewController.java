package com.logictech.manage.web;

import com.logictech.manage.service.HelloViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author JG.Hannibal
 * @since 2018/7/4 14:25
 */
@Controller
public class HelloViewController {

    @Resource
    private HelloViewService helloViewService;

    @GetMapping({"/hello"})
    public String adminIndex(ModelMap model) throws Exception {
        model.addAttribute("text", helloViewService.getMessage("Hello World!!!!"));
        return "home";
    }

}