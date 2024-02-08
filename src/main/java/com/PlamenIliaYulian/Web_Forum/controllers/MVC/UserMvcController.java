package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PhoneNumberDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.RegisterDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserFilterByUsernameOptionsDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserMvcDtoUpdate;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final RoleService roleService;

    public UserMvcController(AuthenticationHelper authenticationHelper,
                             ModelsMapper modelsMapper,
                             UserService userService,
                             PostService postService,
                             CommentService commentService,
                             RoleService roleService) {
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.roleService = roleService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllUsersPage(@ModelAttribute("userFilterOptionsDto") UserFilterByUsernameOptionsDto userFilterByUsernameOptionsDto,
                                   Model model,
                                   HttpSession session) {
        User loggedUser;
        try {
            loggedUser = authenticationHelper.tryGetUserFromSession(session);
            UserFilterOptions userFilterOptions = modelsMapper.userFilterOptionsFromUsernameOptionsDto(userFilterByUsernameOptionsDto);
            model.addAttribute("users", userService.getAllUsers(userFilterOptions));
            return "AllUsers";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUserPage(@PathVariable int id,
                                     Model model,
                                     HttpSession session) {

        try {
            authenticationHelper.tryGetUserFromSession(session);
            model.addAttribute("userById", userService.getUserById(id));
            return "SingleUser";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/posts")
    public String showSingleUsersPosts(@PathVariable int id,
                                       Model model,
                                       HttpSession session) {

        try {
            authenticationHelper.tryGetUserFromSession(session);
            User user = userService.getUserById(id);
            model.addAttribute("userById", user);
            model.addAttribute("userPosts",postService.getPostsByCreator(user));
            return "SingleUserPosts";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/comments")
    public String showSingleUserComments(@PathVariable int id,
                                         Model model,
                                         HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
            User user = userService.getUserById(id);
            model.addAttribute("userById", user);
            model.addAttribute("userPosts",commentService.getCommentsByCreator(user));
            return "SingleUserComments";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable int id,
                               Model model,
                               HttpSession session,
                               @Valid @ModelAttribute("userDtoUpdate") UserMvcDtoUpdate userMvcDtoUpdate) {

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error",HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            model.addAttribute("userById", userById);
            model.addAttribute("userLoggedIn", userLoggedIn);
            return "UserEdit";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/edit")
    public String handleEditUser(@PathVariable int id,
                                 @Valid @ModelAttribute("userDtoUpdate") UserMvcDtoUpdate userMvcDtoUpdate,
                                 BindingResult errors,
                                 HttpSession session,
                                 Model model) {

        if (errors.hasErrors()) {
            return "UserEdit";
        }

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            User userUpdated = modelsMapper.userFromMvcDtoUpdate(userMvcDtoUpdate, id);
            userService.updateUser(userUpdated,userLoggedIn);
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        }
    }

    /*TODO Research how to upload Photo.*/
    @PostMapping("/{id}/edit/UploadAvatar")
    public String handleEditUserUploadAvatar(@PathVariable int id,
                                             HttpSession session,
                                             Model model) {

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
/*
            userService.addAvatar(id, byte[] avatar, userLoggedIn);
*/
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        }
    }

    @PostMapping("/{id}/edit/RemoveAvatar")
    public String handleEditUserRemoveAvatar(@PathVariable int id,
                                             HttpSession session,
                                             Model model) {

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            userService.deleteAvatar(id,userLoggedIn);
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        }
    }

    @PostMapping("/{id}/edit/AddPhoneNumber")
    public String handleEditUserAddPhoneNumber(@PathVariable int id,
                                               HttpSession session,
                                               Model model,
                                               @Valid @ModelAttribute("phoneNumber") PhoneNumberDto phoneNumber,
                                               BindingResult errors) {

        if (errors.hasErrors()) {
            return "UserEdit";
        }

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            if (!userLoggedIn.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            userService.addPhoneNumber(userById,phoneNumber.getPhone(),userLoggedIn);
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        }
    }

    @GetMapping("/{id}/edit/delete")
    public String showDeleteConfirmationPage(@PathVariable int id,
                                             Model model,
                                             HttpSession session) {

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById) && !userLoggedIn.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            return "DeleteConfirmation";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/edit/delete")
    public String handleDeleteUser(@PathVariable int id,
                                   Model model,
                                   HttpSession session) {

        try {
            User userById = userService.getUserById(id);
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            if (!userLoggedIn.equals(userById) && !userLoggedIn.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            session.removeAttribute("currentUser");
            userService.deleteUser(userById, userLoggedIn);
            return "Home";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

}
