package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final ModelsMapper modelsMapper;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final RoleService roleService;

    public AuthenticationMvcController(ModelsMapper modelsMapper, AuthenticationHelper authenticationHelper, UserService userService, RoleService roleService) {
        this.modelsMapper = modelsMapper;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return null;
    }

    @PostMapping("/login")
    public String handleLogin(){
        return null;
    }

    @PostMapping("/logout")
    public String handleLogout(){
        return null;
    }

    @GetMapping("/register")
    public String showRegisterPage(){
        return null;
    }

    @PostMapping("/register")
    public String handleRegister(){
        return null;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession){
        return httpSession.getAttribute("currentUser")!=null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsLoggedAndAdmin(HttpSession httpSession){
        return (httpSession.getAttribute("currentUser")!=null &&
                authenticationHelper
                .tryGetUserFromSession(httpSession)
                .getRoles()
                .contains(roleService.getRoleById(1)));
    }

    @ModelAttribute("isNotBlocked")
    public boolean populateIsLoggedAndNotBlocked(HttpSession httpSession){
        return (httpSession.getAttribute("currentUser")!=null &&
                !authenticationHelper
                        .tryGetUserFromSession(httpSession)
                        .isBlocked());
    }

}
