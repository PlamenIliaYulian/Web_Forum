package com.PlamenIliaYulian.Web_Forum.controllers;


import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidUserInputException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
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


    @PostMapping
    public Post createPost(@Valid @RequestBody PostDto postDto,
                           @RequestHeader HttpHeaders headers) {
        try {
            User creatorOfPost = authenticationHelper.tryGetUser(headers);
            Post postToBeCreated = modelsMapper.postFromDto(postDto);
            return postService.createPost(postToBeCreated, creatorOfPost);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{postTitle}")
    public void deletePost(@PathVariable String postTitle,
                           @RequestHeader HttpHeaders headers) {
        User userMakingRequest = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(postTitle);
        postService.deletePost(post, userMakingRequest);
    }

    /*Ilia*/
    @PutMapping("/{title}")
    public Post updatePost(@PathVariable String title,
                           @RequestHeader HttpHeaders headers,
                           @Valid @RequestBody PostDto postDto) {
        User userMakingRequest = authenticationHelper.tryGetUser(headers);
        Post postByTitle = postService.getPostByTitle(title);
        Post postToUpdate = modelsMapper.postFromDto(postDto, postByTitle);
        return postService.updatePost(postToUpdate, userMakingRequest);
    }

    /*TODO implement Post FilterOptions*/
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{title}")
    public Post getPostByTitle(@PathVariable String title) {
        return postService.getPostByTitle(title);
    }

    /*Ilia*/
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{title}/like")
    public Post likePost(@RequestHeader HttpHeaders headers,
                         @PathVariable String title) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostByTitle(title);
            return postService.likePost(post, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{title}/dislike")
    public Post dislikePost(@RequestHeader HttpHeaders headers,
                            @PathVariable String title) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        return postService.dislikePost(post, authorizedUser);
    }

    /*Ilia*/
    @PutMapping("/{title}/tags")
    public Post addTagToPost(@RequestHeader HttpHeaders headers,
                             @PathVariable String title,
                             @Valid @RequestBody TagDto tagDto) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        Tag tag = modelsMapper.tagFromDto(tagDto);
        return postService.addTagToPost(post, tag, authorizedUser);
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*TODO - ask Plamkata if he likes this.*/
    @PostMapping("/{title}/comments")
    public Post addCommentToPost(@RequestHeader HttpHeaders headers,
                                 @PathVariable String title,
                                 @Valid @RequestBody CommentDto commentDto) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post postToComment = postService.getPostByTitle(title);
        Comment commentToAdd = modelsMapper.commentFromDto(commentDto);
        return postService.addCommentToPost(postToComment, commentToAdd, authorizedUser);
    }

    @PutMapping("/{title}/comments/{commentId}")
    public Post removeCommentFromPost(@RequestHeader HttpHeaders headers,
                                      @PathVariable String title,
                                      @PathVariable int commentId) {

        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postToComment = postService.getPostByTitle(title);
            return postService.removeCommentFromPost(postToComment, commentId, authorizedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    /*Ilia*/
    @GetMapping("/{title}/comments")
    public List<Comment> getAllCommentsRelatedToPost(@RequestHeader HttpHeaders headers,
                                                     @PathVariable String title) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post postWithComments = postService.getPostByTitle(title);
        return postService.getAllCommentsRelatedToPost(postWithComments);
    }
}
