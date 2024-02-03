package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    @GetMapping
    public String showHomePage() {
        return "index";
    }
}

