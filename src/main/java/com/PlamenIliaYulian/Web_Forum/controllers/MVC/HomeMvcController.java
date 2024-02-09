package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeMvcController {

    private final UserService userService;
    private final PostService postService;

    private final AuthenticationHelper authenticationHelper;
    private final RoleService roleService;

    public HomeMvcController(UserService userService, PostService postService, AuthenticationHelper authenticationHelper, RoleService roleService) {
        this.userService = userService;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.roleService = roleService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }
    @ModelAttribute("isAdmin")
    public boolean populateIsLoggedAndAdmin(HttpSession httpSession) {
        return (httpSession.getAttribute("currentUser") != null &&
                authenticationHelper
                        .tryGetUserFromSession(httpSession)
                        .getRoles()
                        .contains(roleService.getRoleById(1)));
    }

    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("totalUsersCount", userService.getAllUsersCount());
        model.addAttribute("totalPostsCount", postService.getAllPostsCount());
        model.addAttribute("mostCommentedPosts", postService.getMostCommentedPosts());
        model.addAttribute("mostRecentPosts", postService.getMostRecentlyCreatedPosts());
        return "Home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "About";
    }
}

