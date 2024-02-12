package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostFilterOptionsDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.RoleService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")

public class PostMvcController {

    private final AuthenticationHelper authenticationHelper;

    private final PostService postService;

    private final UserService userService;

    private final CommentService commentService;

    private final ModelsMapper modelsMapper;

    private final RoleService roleService;

    public PostMvcController(AuthenticationHelper authenticationHelper,
                             PostService postService,
                             UserService userService,
                             CommentService commentService,
                             ModelsMapper modelsMapper,
                             RoleService roleService) {
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.modelsMapper = modelsMapper;
        this.roleService = roleService;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
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

    @ModelAttribute("loggedUser")
    public User populateLoggedUser(HttpSession httpSession) {
        if (httpSession.getAttribute("currentUser") != null) {
            return authenticationHelper.tryGetUserFromSession(httpSession);
        }
        return new User();
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterDto") PostFilterOptionsDto postFilterOptionsDto,
                               HttpSession session,
                               Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        PostFilterOptions filterOptions = modelsMapper.postFilterOptionsFromDto(postFilterOptionsDto);
        List<Post> posts = postService.getAllPosts(user, filterOptions);
        model.addAttribute("posts", posts);
        model.addAttribute("filterDto", postFilterOptionsDto);
        model.addAttribute("user", user);
        return "Posts";
    }

    @GetMapping("/new")
    public String showCreateNewPostForm(HttpSession session,
                                        Model model) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
            model.addAttribute("postDto", new PostDto());
            return "NewPost";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("postDto") PostDto postDto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {

        if (errors.hasErrors()) {
            return "NewPost";
        }
        try {
            User user = authenticationHelper.tryGetUserFromSession(session);
            Post postToBeCreated = modelsMapper.postFromDto(postDto);
            Post newPost = postService.createPost(postToBeCreated, user);
            int postId = newPost.getPostId();
            model.addAttribute("postId", postId);
            return "redirect:/posts/{postId}";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id,
                                 Model model,
                                 HttpSession session) {

        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(id);
            model.addAttribute("loggedInUser", loggedInUser);
            model.addAttribute("post", post);
            model.addAttribute("relatedComments", post.getRelatedComments());
            return "SinglePost";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditPostForm(@PathVariable int id,
                                   Model model,
                                   HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(id);
            PostDto postDto = modelsMapper.postDtoFromPost(post);
            model.addAttribute("postDto", postDto);
            return "PostEdit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/edit")
    public String handleEditPost(@PathVariable int id,
                                 @Valid @ModelAttribute("postDto") PostDto postDto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        if (errors.hasErrors()) {
            return "PostEdit";
        }

        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = modelsMapper.postFromDto(postDto, id);
            postService.updatePost(post, loggedInUser);
            return "redirect:/posts/{id}";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        }  catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    /*Plamka*/
    /*This is the endpoint / page which allows the user to write a new comment*/
    @GetMapping("/{id}/comment")
    public String showAddCommentForm() {
        return null;
    }

    /*Iliika*/
    /*This is the endpoint which will be taking care of adding the comment to the respective post*/
    @PostMapping("/{id}/addComment")
    public String addCommentToPost() {
        return null;
    }

    @PostMapping("/{id}/deleteComment")
    public String removeCommentFromPost() {
        return null;
    }

    @GetMapping("/{postId}/comments/{commentId}/edit")
    public String showEditCommentPage() {
        return null;
    }

    @PostMapping("/{postId}/comments/{commentId}/edit")
    public String handleEditCommentPage() {
        return null;
    }

    /*Yuli*/
    /*This is the endpoint which will be taking care of deleting a post.
     * NOTE - make sure the redirect the user to >>> /posts */
    @GetMapping("/{id}/delete")
    public String deletePost() {
        return null;
    }

    @PostMapping("/{id}/like")
    public String likePost() {
        return null;
    }

    @PostMapping("/{id}/dislike")
    public String dislikePost() {
        return null;
    }

    @PostMapping("/{id}/edit/addTag")
    public String addTagToPost() {
        return null;
    }

    @PostMapping("/{id}/edit/removeTag/{tagId}")
    public String remmoveTagToPost() {
        return null;
    }


}
