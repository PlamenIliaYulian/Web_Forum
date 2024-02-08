package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeMvcController {

    private final UserService userService;

    public HomeMvcController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession){
        return httpSession.getAttribute("currentUser")!=null;
    }

    @GetMapping
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "home";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "home";
    }



//    @GetMapping("/count")
//    public void getAllUsersCount() {
//        System.out.println(userService.getAllUsersCount());
//    }
}

