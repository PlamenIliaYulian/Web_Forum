package com.PlamenIliaYulian.Web_Forum.controllers;


import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;

    @Autowired
    public PostRestController(PostService postService,
                              AuthenticationHelper authenticationHelper,
                              ModelsMapper modelsMapper) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }


    @PostMapping
    public Post createPost(@Valid @RequestBody PostDto postDto,
                           @RequestHeader HttpHeaders headers) {
        User userMakingRequest = authenticationHelper.tryGetUser(headers);
        Post post = modelsMapper.postFromDto(postDto);
        return postService.createPost(post, userMakingRequest);
    }

    @DeleteMapping("/{postTitle}")
    public void deletePost(@PathVariable String postTitle,
                           @RequestHeader HttpHeaders headers) {
        User userMakingRequest = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(postTitle);
        postService.deletePost(post, userMakingRequest);
    }

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

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{title}/like")
    public Post likePost(@RequestHeader HttpHeaders headers,
                         @PathVariable String title) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        return postService.likePost(post, authorizedUser);
    }

    @PutMapping("/{title}/dislike")
    public Post dislikePost(@RequestHeader HttpHeaders headers,
                            @PathVariable String title) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        return postService.dislikePost(post, authorizedUser);
    }

    @PutMapping("/{title}")
    public Post addTagToPost(@RequestHeader HttpHeaders headers,
                             @PathVariable String title,
                             @Valid @RequestBody TagDto tagDto) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        Tag tag = modelsMapper.tagFromDto(tagDto);
        return postService.addTagToPost(post, tag, authorizedUser);
    }

    @PutMapping("/{title}")
    public Post removeTagFromPost(@RequestHeader HttpHeaders headers,
                                  @PathVariable String title,
                                  @Valid @RequestBody TagDto tagDto) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post post = postService.getPostByTitle(title);
        Tag tag = modelsMapper.tagFromDto(tagDto);
        return postService.removeTagFromPost(post, tag, authorizedUser);
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

    @GetMapping("/{title}/comments")
    public List<Comment> getAllCommentsRelatedToPost(@RequestHeader HttpHeaders headers,
                                                     @PathVariable String title) {
        User authorizedUser = authenticationHelper.tryGetUser(headers);
        Post postWithComments = postService.getPostByTitle(title);
        return postService.getAllCommentsRelatedToPost(postWithComments);
    }
}
