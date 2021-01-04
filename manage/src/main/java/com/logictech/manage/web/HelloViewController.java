package com.logictech.manage.web;

import com.logictech.manage.service.HelloViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author JG.Hannibal
 * @since 2018/7/4 14:25
 */
@Controller
public class HelloViewController {

    @Resource
    private HelloViewService helloViewService;

    @GetMapping({"","/","index"})
    public String adminIndex(ModelMap model) throws Exception {
        model.addAttribute("text", "Hello World!!!!");
        return "home";
    }

    @GetMapping({"/hello"})
    public String hello(ModelMap model) throws Exception {
        model.addAttribute("text", helloViewService.getMessage("Hello World!!!!"));
        return "home";
    }

}