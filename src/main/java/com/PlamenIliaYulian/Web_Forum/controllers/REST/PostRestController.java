package com.PlamenIliaYulian.Web_Forum.controllers.REST;


import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidUserInputException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.CommentDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PostDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.TagDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.PlamenIliaYulian.Web_Forum.models.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Post")
public class PostRestController {

    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;
    private final TagService tagService;

    @Autowired
    public PostRestController(PostService postService,
                              AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper, TagService tagService) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
        this.tagService = tagService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post createPost(@Valid @RequestBody PostDto postDto,
                           @RequestHeader HttpHeaders headers) {
        try {
            User creatorOfPost = authenticationHelper.tryGetUser(headers);
            Post postToBeCreated = modelsMapper.postFromDto(postDto);
            return postService.createPost(postToBeCreated, creatorOfPost);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /*Plamen*/
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postTitle}")
    public void deletePost(@PathVariable String postTitle,
                           @RequestHeader HttpHeaders headers) {
        try {
            User userMakingRequest = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(postTitle);
            postService.deletePost(post, userMakingRequest);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*Ilia*/
    @PutMapping("/{title}")
    public Post updatePost(@PathVariable String title,
                           @RequestHeader HttpHeaders headers,
                           @Valid @RequestBody PostDto postDto) {
        try {
            User userMakingRequest = authenticationHelper.tryGetUser(headers);
            Post postByTitle = postService.getPostByTitle(title);
            Post postToUpdate = modelsMapper.postFromDto(postDto, postByTitle);
            return postService.updatePost(postToUpdate, userMakingRequest);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<Post> getAllPosts(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) Integer likes,
                                  @RequestParam(required = false) Integer dislikes,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false) String content,
                                  @RequestParam(required = false) String createdBefore,
                                  @RequestParam(required = false) String createdBy,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        try {
            User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
            PostFilterOptions postFilterOptions =
                    new PostFilterOptions(likes, dislikes, title, content, createdBefore, createdBy, sortBy, sortOrder);
            return postService.getAllPosts(userExecutingTheRequest, postFilterOptions);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /*Plamen*/
    @GetMapping("/title/{title}")
    public Post getPostByTitle(@PathVariable String title, @RequestHeader HttpHeaders headers) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            return postService.getPostByTitle(title);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /*Ilia*/
    @GetMapping("/id/{id}")
    public Post getPostById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            return postService.getPostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{title}/likes")
    public Post likePost(@RequestHeader HttpHeaders headers,
                         @PathVariable String title) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(title);
            return postService.likePost(post, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /*Plamen*/
    @PutMapping("/{title}/dislikes")
    public Post dislikePost(@RequestHeader HttpHeaders headers,
                            @PathVariable String title) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(title);
            return postService.dislikePost(post, authenticatedUser);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /*Ilia*/
    @PutMapping("/{title}/tags")
    public Post addTagToPost(@RequestHeader HttpHeaders headers,
                             @PathVariable String title,
                             @Valid @RequestBody TagDto tagDto) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(title);
            Tag tag = modelsMapper.tagFromDto(tagDto);
            return postService.addTagToPost(post, tag, authorizedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage());
        }
    }

    @DeleteMapping("/{title}/tags/{tagName}")
    public Post removeTagFromPost(@RequestHeader HttpHeaders headers,
                                  @PathVariable String title,
                                  @PathVariable String tagName) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(title);
            Tag tag = tagService.getTagByName(tagName);
            return postService.removeTagFromPost(post, tag, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    /*Plamen*/
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{title}/comments")
    public Post addCommentToPost(@RequestHeader HttpHeaders headers,
                                 @PathVariable String title,
                                 @Valid @RequestBody CommentDto commentDto) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postToComment = postService.getPostByTitle(title);
            Comment commentToAdd = modelsMapper.commentFromDto(commentDto);
            return postService.addCommentToPost(postToComment, commentToAdd, authorizedUser);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/{title}/comments/{comment}")
    public Post removeCommentFromPost(@RequestHeader HttpHeaders headers,
                                      @PathVariable String title,
                                      @PathVariable String comment) {

        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postToComment = postService.getPostByTitle(title);
            return postService.removeCommentFromPost(postToComment, comment, authorizedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /*Ilia*/
    @GetMapping("/{title}/comments")
    public List<Comment> getAllCommentsRelatedToPost(@RequestHeader HttpHeaders headers,
                                                     @PathVariable String title) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postWithComments = postService.getPostByTitle(title);
            return postService.getAllCommentsRelatedToPost(postWithComments);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }
}
