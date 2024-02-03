package com.PlamenIliaYulian.Web_Forum.controllers.REST;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.CommentDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment")
public class CommentRestController {

    private final AuthenticationHelper authenticationHelper;

    private final CommentService commentService;

    private final ModelsMapper modelsMapper;

    @Autowired
    public CommentRestController(AuthenticationHelper authenticationHelper, CommentService commentService, ModelsMapper modelsMapper) {
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
        this.modelsMapper = modelsMapper;
    }


    @GetMapping
    public List<Comment> getAllComments(@RequestHeader HttpHeaders headers,
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


    @PutMapping("/{content}")
    public Comment updateComment(@PathVariable String content,
                                 @RequestHeader HttpHeaders headers,
                                 @Valid @RequestBody CommentDto commentDto) {
        try {
            User userToAuthenticate = authenticationHelper.tryGetUser(headers);
            Comment newComment = modelsMapper.commentFromDto(commentDto, content);
            return commentService.updateComment(newComment, userToAuthenticate);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{content}/likes")
    public Comment likeComment(@PathVariable String content, @RequestHeader HttpHeaders headers) {
        try {
            User userToAuthenticate = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.getCommentByContent(content);
            return commentService.likeComment(comment, userToAuthenticate);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*Ilia*/
    @PutMapping("/{content}/dislikes")
    public Comment dislikeComment(@PathVariable String content,
                                  @RequestHeader HttpHeaders headers) {
        try {
            User userToDislikeComment = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.getCommentByContent(content);
            return commentService.dislikeComment(comment, userToDislikeComment);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage()
            );
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }
}
