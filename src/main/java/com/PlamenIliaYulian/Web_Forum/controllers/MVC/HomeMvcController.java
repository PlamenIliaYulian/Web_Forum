package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;

    public HomeMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showHomePage() {
        return "index";
    }

//    @GetMapping("/count")
//    public void getAllUsersCount() {
//        System.out.println(userService.getAllUsersCount());
//    }
}

