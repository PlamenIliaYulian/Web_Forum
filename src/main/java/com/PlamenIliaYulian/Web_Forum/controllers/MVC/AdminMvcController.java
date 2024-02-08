package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostFilterOptionsDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserFilterOptionsDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin-panel")
public class AdminMvcController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;
    private final RoleService roleService;

    private final ModelsMapper modelsMapper;

    @Autowired
    public AdminMvcController(UserService userService,
                              PostService postService,
                              CommentService commentService,
                              AuthenticationHelper authenticationHelper, RoleService roleService,
                              ModelsMapper modelsMapper) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.roleService = roleService;
        this.modelsMapper = modelsMapper;
    }

    @GetMapping("/main")
    public String showMainAdminPage() {
        return "MainAdminPanel";
    }

    @GetMapping("/users")
    public String showUsersAdminPage(
            @ModelAttribute("userFilterOptionsDto") UserFilterOptionsDto userFilterOptionsDto,
            Model model,
            HttpSession session) {
        User loggedUser;
        try {
            loggedUser = authenticationHelper.tryGetUserFromSession(session);
            Role adminRole = roleService.getRoleById(1);
            if (loggedUser.getRoles().contains(adminRole)) {
                UserFilterOptions userFilterOptions = modelsMapper.userFilterOptionsFromDto(userFilterOptionsDto);
                model.addAttribute("users", userService.getAllUsers(userFilterOptions));
                return "UsersAdminPanel";
            }
            model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/posts")
    public String showPostsAdminPage(
            @ModelAttribute("postFilterOptions") PostFilterOptionsDto postFilterOptionsDto,
            Model model,
            HttpSession session) {
        User loggedUser;
        try {
            loggedUser = authenticationHelper.tryGetUserFromSession(session);
            Role adminRole = roleService.getRoleById(1);
            if (loggedUser.getRoles().contains(adminRole)) {
                PostFilterOptions postFilterOptions = modelsMapper.postFilterOptionsFromDto(postFilterOptionsDto);
                model.addAttribute("posts", postService.getAllPosts(loggedUser, postFilterOptions));
                return "PostsAndCommentsAdminPanel";
            }
            model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

}
