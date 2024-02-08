package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/AdminPanel")
public class AdminMvcController {


    @GetMapping()
    public String showAdminPage() {
        return "home";
    }

    @GetMapping("/search")
    public String showAllUsers(){
        return "home";
    }



}
