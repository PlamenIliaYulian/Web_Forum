package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.CommentDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostFilterOptionsDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.TagDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.*;
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

    public static final String NOT_CREATOR_ERROR = "You are not the creator of this post";
    private final AuthenticationHelper authenticationHelper;

    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;

    private final ModelsMapper modelsMapper;

    private final RoleService roleService;

    public PostMvcController(AuthenticationHelper authenticationHelper,
                             PostService postService,
                             TagService tagService,
                             CommentService commentService,
                             ModelsMapper modelsMapper,
                             RoleService roleService) {
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.tagService = tagService;

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
            model.addAttribute("commentDto", new CommentDto());
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
            authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(id);
            PostDto postDto = modelsMapper.postDtoFromPost(post);
            model.addAttribute("postDto", postDto);
            model.addAttribute("tagDto", new TagDto());
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
                                 @Valid @ModelAttribute("tagDto") TagDto tagDto,
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
            if(tagDto.getTag() != null && !tagDto.getTag().isEmpty()){
                Tag tag = modelsMapper.tagFromDto(tagDto);
                postService.addTagToPost(post, tag, loggedInUser);
            }
            return "redirect:/posts/{id}";
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

    /*Plamka*/
    /*This is the endpoint / page which allows the user to write a new comment*/
//    @GetMapping("/{id}/comment")
//    public String showAddCommentForm(@PathVariable int id,
//                                     Model model,
//                                     HttpSession session) {
//        try {
//            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
//            Post post = postService.getPostById(id);
//            PostDto postDto = modelsMapper.postDtoFromPost(post);
//            model.addAttribute("postDto", postDto);
//            return "SinglePost";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "Error";
//        } catch (AuthenticationException e) {
//            return "redirect:/auth/login";
//        }
//    }

    @PostMapping("/{id}/addComment")
    public String handleAddCommentToPost(@PathVariable int id,
                                         @Valid @ModelAttribute("commentDto") CommentDto commentDto,
                                         BindingResult errors,
                                         Model model,
                                         HttpSession session) {
        if (errors.hasErrors()) {
            return "SinglePost";
        }
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(id);
            Comment commentToAdd = modelsMapper.commentFromDto(commentDto);
            postService.addCommentToPost(post, commentToAdd, loggedInUser);
            return "redirect:/posts/{id}";
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

    @PostMapping("/{postId}/comment/{commentId}/delete")
    public String removeCommentFromPost(@PathVariable int postId,
                                        @PathVariable int commentId,
                                        HttpSession session,
                                        Model model) {
        try {
            User loggedUser = authenticationHelper.tryGetUserFromSession(session);
            Post postToRemoveCommentFrom = postService.getPostById(postId);
            Comment commentToBeRemoved = commentService.getCommentById(commentId);
            postService.removeCommentFromPost(postToRemoveCommentFrom, commentToBeRemoved, loggedUser);
            return "redirect:/posts/{id}";
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

    @GetMapping("/{postId}/comments/{commentId}/edit")
    public String showEditCommentPage(@PathVariable int postId,
                                      @PathVariable int commentId,
                                      Model model,
                                      HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(postId);
            Comment commentToBeEdited = commentService.getCommentById(commentId);
            CommentDto commentDto = modelsMapper.commentDtoFromComment(commentToBeEdited);
            model.addAttribute("commentDto", commentDto);
            return "CommentEdit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "Error";
        }
    }

    @PostMapping("/{postId}/comments/{commentId}/edit")
    public String handleEditCommentPage(@PathVariable int postId,
                                        @PathVariable int commentId,
                                        @Valid @ModelAttribute("commentDto") CommentDto commentDto,
                                        BindingResult errors,
                                        Model model,
                                        HttpSession session) {
        if (errors.hasErrors()) {
            return "SinglePost";
        }
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Comment commentToBeEdited = modelsMapper.commentFromDto(commentDto, commentId);
            commentService.updateComment(commentToBeEdited, loggedInUser);
            return "redirect:/posts/{postId}";
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

    /*Yuli*/
    /*This is the endpoint which will be taking care of deleting a post.
     * NOTE - make sure the redirect the user to >>> /posts */

    @GetMapping("/{id}/delete")
    public String showDeletePostPage(@PathVariable int id,
                                     Model model,
                                     HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(session);
            Post post = postService.getPostById(id);
            if (!post.getCreatedBy().equals(loggedInUser) && !loggedInUser.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                model.addAttribute("error", NOT_CREATOR_ERROR);
                return "Error";
            }
            model.addAttribute("post", post);
            return "PostDelete";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }


    @PostMapping("/{id}/delete")
    public String handleDeletePost(@PathVariable int id,
                                   Model model,
                                   HttpSession httpSession) {
        try {
            User loggedInUser = authenticationHelper.tryGetUserFromSession(httpSession);
            Post post = postService.getPostById(id);
            if (!post.getCreatedBy().equals(loggedInUser) && !loggedInUser.getRoles().contains(roleService.getRoleById(1))) {
                model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                model.addAttribute("error", NOT_CREATOR_ERROR);
                return "Error";
            }
            postService.deletePost(post, loggedInUser);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "Error";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable int postId,
                           Model model,
                           HttpSession httpSession) {
        try {
            User loggedUser = authenticationHelper.tryGetUserFromSession(httpSession);
            Post postToBeLiked = postService.getPostById(postId);
            postService.likePost(postToBeLiked,loggedUser);
            return "redirect:/posts/{postId}";
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

    @PostMapping("/{postId}/dislike")
    public String dislikePost(@PathVariable int postId,
                              Model model,
                              HttpSession httpSession) {
        try {
            User loggedUser = authenticationHelper.tryGetUserFromSession(httpSession);
            Post postToBeLiked = postService.getPostById(postId);
            postService.dislikePost(postToBeLiked,loggedUser);
            return "redirect:/posts/{postId}";
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

    /*@PostMapping("/{id}/edit/addTag")
    public String addTagToPost() {
        return null;
    }*/

    @PostMapping("/{postId}/edit/removeTag/{tagId}")
    public String removeTagToPost(@PathVariable int postId,
                                   @PathVariable int tagId,
                                   Model model,
                                   HttpSession httpSession) {
        try {
            User loggedUser = authenticationHelper.tryGetUserFromSession(httpSession);
            Post postToBeEdited = postService.getPostById(postId);
            Tag tagToBeRemoved = tagService.getTagById(tagId);
            postService.removeTagFromPost(postToBeEdited, tagToBeRemoved, loggedUser);
            return "redirect:/posts/{postId}";
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


}
