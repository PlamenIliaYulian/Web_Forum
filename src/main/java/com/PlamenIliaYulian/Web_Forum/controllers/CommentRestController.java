package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {

    private final AuthenticationHelper authenticationHelper;
    private final CommentService commentService;

    @Autowired
    public CommentRestController(AuthenticationHelper authenticationHelper, CommentService commentService) {
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
    }

    /*TODO implement Comment FilterOptions*/
    @GetMapping
    public List<Comment> getAllPosts(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) Integer likes,
                                  @RequestParam(required = false) Integer dislikes,
                                  @RequestParam(required = false) String content,
                                  @RequestParam(required = false) String createdBefore,
                                  @RequestParam(required = false) String createdBy,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        try {
            User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
            CommentFilterOptions commentFilterOptions =
                    new CommentFilterOptions(likes, dislikes, content, createdBefore, createdBy, sortBy, sortOrder);
            return commentService.getAllComments(userExecutingTheRequest, commentFilterOptions);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id) {
        return null;
    }

    @PutMapping("/{id}/like")
    public Comment likeComment(@PathVariable String id) {
        return null;
    }

    /*Ilia*/
    @PutMapping("/{id}/dislike")
    public Comment dislikeComment(@PathVariable String id) {
        return null;
    }

}
