package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;
import com.PlamenIliaYulian.Web_Forum.services.contracts.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    public static final String PASSWORD_CONFIRMATION_SHOULD_MATCH_PASSWORD = "Password confirmation should match password.";
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final RoleService roleService;

    private final FileStorageService fileStorageService;

    public UserMvcController(AuthenticationHelper authenticationHelper,
                             ModelsMapper modelsMapper,
                             UserService userService,
                             PostService postService,
                             CommentService commentService,
                             RoleService roleService, FileStorageService fileStorageService) {
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.roleService = roleService;
        this.fileStorageService = fileStorageService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsLoggedAndAdmin(HttpSession httpSession) {
        return (httpSession.getAttribute("currentUser") != null &&
                authenticationHelper
                        .tryGetUserFromSession(httpSession)
                        .getRoles()
                        .contains(roleService.getRoleById(1)));
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @ModelAttribute("loggedUser")
    public User populateLoggedUser(HttpSession httpSession) {
        if (httpSession.getAttribute("currentUser") != null) {
            return authenticationHelper.tryGetUserFromSession(httpSession);
        }
        return new User();
    }

    @ModelAttribute("adminRole")
    public Role populateAdminRole() {
        return roleService.getRoleById(1);
    }

    @GetMapping("/search")
    public String showUsersAdminPage(@ModelAttribute("userFilterOptionsDto") UserFilterOptionsDto userFilterOptionsDto,
                                     Model model,
                                     HttpSession session) {

        User loggedUser;
        try {
            loggedUser = authenticationHelper.tryGetUserFromSession(session);
            Role adminRole = roleService.getRoleById(1);
            if (loggedUser.getRoles().contains(adminRole)) {
                UserFilterOptions userFilterOptions = modelsMapper.userFilterOptionsFromDto(userFilterOptionsDto);
                model.addAttribute("users", userService.getAllUsers(userFilterOptions));
                model.addAttribute("userFilterOptionsDto", userFilterOptionsDto);
                return "AllUsers";
            }
            model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/administrative-changes")
    public String showUserAdministrativePage(@PathVariable int id,
                                             Model model,
                                             HttpSession session) {
        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);

            if (!userLoggedIn.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            UserMvcAdminChangesDto userMvcAdminChangesDto = modelsMapper.userMvcAdminChangesDtoFromUser(userById);
            model.addAttribute("userById", userById);
            model.addAttribute("userLoggedIn", userLoggedIn);
            model.addAttribute("userMvcAdminChangesDto", userMvcAdminChangesDto);
            model.addAttribute("allRoles", roleService.getAllRoles());
            model.addAttribute("roleDto", new RoleDto());

            return "UserAdministrativeChanges";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/{id}/administrative-changes")
    public String handleUserAdministrativeChanges(@PathVariable int id,
                                                  @Valid @ModelAttribute("userMvcAdminChangesDto") UserMvcAdminChangesDto userMvcAdminChangesDto,
                                                  BindingResult errors,
                                                  HttpSession session,
                                                  Model model) {

        User userById;
        try {
            userById = userService.getUserById(id);
            model.addAttribute("userById", userById);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }

        if (errors.hasErrors()) {
            return "UserEdit";
        }

        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            if (!userLoggedIn.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            User userUpdated = modelsMapper.userFromUserMvcAdminChangesDto(userMvcAdminChangesDto, id);
            userService.updateUser(userUpdated, userLoggedIn);
            return "redirect:/users/search";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "email_exists", e.getMessage());
            return "UserEdit";
        }
    }

    @PostMapping("/{userId}/administrative-changes/add-role")
    public String handleAddRoleToUser(@PathVariable int userId,
                                      @Valid @ModelAttribute("roleDto") RoleDto roleDto,
                                      BindingResult errors,
                                      Model model,
                                      HttpSession session) {
        if (errors.hasErrors()) {
            return "UserAdministrativeChanges";
        }

        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(userId);
            Role roleToAdd = modelsMapper.roleFromRoleDto(roleDto);
            userService.addRoleToUser(roleToAdd, userById, loggedInUser);
            return "redirect:/users/{userId}/administrative-changes";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @GetMapping("/{userId}/administrative-changes/remove-role/{roleId}")
    public String handleRemoveRoleFromUser(@PathVariable int userId,
                                           @PathVariable int roleId,
                                           Model model,
                                           HttpSession httpSession) {
        try {
            User loggedUser = authenticationHelper.tryGetUserFromSession(httpSession);
            User userById = userService.getUserById(userId);
            Role roleToBeRemoved = roleService.getRoleById(roleId);
            if (roleId == 3) {
                return "redirect:/users/{userId}/administrative-changes";
            }
            userService.removeRoleFromUser(roleToBeRemoved, userById, loggedUser);
            return "redirect:/users/{userId}/administrative-changes";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "redirect:/users/{userId}/administrative-changes";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUserPage(@PathVariable int id,
                                     Model model,
                                     HttpSession session) {

        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            User user = userService.getUserById(id);
            model.addAttribute("userById", user);
            model.addAttribute("userPosts", postService.getPostsByCreator(user));
            model.addAttribute("userComments", commentService.getCommentsByCreator(user));
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("avatar", fileStorageService.load(user.getAvatar().getAvatar()));
            return "SingleUser";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }


    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable int id,
                               Model model,
                               HttpSession session) {
        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            User userById = userService.getUserById(id);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }
            UserMvcDtoUpdate userMvcDtoUpdate = modelsMapper.userMvcDtoFromUser(userById);
            model.addAttribute("userMvcDtoUpdate", userMvcDtoUpdate);
            model.addAttribute("userById", userById);
            model.addAttribute("userLoggedIn", userLoggedIn);
            return "UserEdit";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/{id}/edit")
    public String handleEditUser(@PathVariable int id,
                                 @Valid @ModelAttribute("userMvcDtoUpdate") UserMvcDtoUpdate userMvcDtoUpdate,
                                 BindingResult errors,
                                 HttpSession session,
                                 Model model) {

        User userById = null;
        try {
            userById = userService.getUserById(id);
            model.addAttribute("userById", userById);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }

        if (errors.hasErrors()) {
            return "UserEdit";
        }

        if (!userMvcDtoUpdate.getPassword().equals(userMvcDtoUpdate.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password_error", PASSWORD_CONFIRMATION_SHOULD_MATCH_PASSWORD);
            return "UserEdit";
        }
        try {
            User userLoggedIn = authenticationHelper.tryGetUserFromSession(session);
            if (!userLoggedIn.equals(userById)) {
                model.addAttribute("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                return "Error";
            }

            User userUpdated = modelsMapper.userFromMvcDtoUpdate(userMvcDtoUpdate, id);
            userService.updateUser(userUpdated, userLoggedIn);
            return "redirect:/users/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "email_exists", e.getMessage());
            return "UserEdit";
        }
    }

    @PostMapping("/{id}/edit/remove-avatar")
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
            userService.deleteAvatar(id, userLoggedIn);
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/{id}/edit/add-phone-number")
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
            userService.addPhoneNumber(userById, phoneNumber.getPhone(), userLoggedIn);
            return "redirect:/{id}";
        } catch (AuthenticationException e) {
            model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "Error";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
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
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
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
            return "redirect:/";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/{id}/edit/photos/upload")
    public String uploadAvatar(@PathVariable int id,
                               Model model,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session) {
        String message = "";
        try {
            User userById = userService.getUserById(id);
            model.addAttribute("userById", userById);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }

        try {
            Avatar newAvatar = fileStorageService.uploadImageToFileSystem(file);
            userService.addAvatar(id, newAvatar.getAvatar(), authenticationHelper.tryGetUserFromSession(session));
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            model.addAttribute("message", message);
            return "redirect:/users/{id}";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            model.addAttribute("message", message);
            return "Error";
        }
    }




}
