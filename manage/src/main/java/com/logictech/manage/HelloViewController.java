package com.logictech.manage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author JG.Hannibal
 * @since 2018/7/4 14:25
 */
@Controller
public class HelloViewController {

    @GetMapping({"/hello"})
    public String adminIndex(ModelMap model) throws Exception {
        model.addAttribute("text", "Hello World!");
        return "home";
    }

}