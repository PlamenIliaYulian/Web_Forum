package com.PlamenIliaYulian.Web_Forum.controllers.REST;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.CommentDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @Operation(
            summary = "Get all comments with option to filter and sort.",
            description = "Get a list of all comments. Also through parameters you can filter out comments and sort them.",
            parameters = {
                    @Parameter(
                            name = "content",
                            description = "If the beer name consist provided text in the 'contebt' parameter.",
                            example = "data algorithm"),
                    @Parameter(
                            name = "createdAfter",
                            description = "Select the most distant creation date.",
                            example = "2024-01-27 13:33:04"),
                    @Parameter(
                            name = "createdBefore",
                            description = "Select the nearest creation date.",
                            example = "2024-01-27 13:33:04"),
                    @Parameter(
                            name = "createdBy",
                            description = "If the beer creator's name consist the text provided in the 'createdBy' parameter.",
                            example = "john"),
                    @Parameter(
                            name = "sortBy",
                            description = "You can choose to sort the comments list by 'likes', 'dislikes', 'content', 'createdOn' or 'createdBy'.",
                            example = "likes"),
                    @Parameter(
                            name = "sortOrder",
                            description = "You can choose to sort the comments list in descending order by typing 'desc'. The default is an ascending order.",
                            example = "desc")

            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Response",
                            content = @Content(
                                    schema = @Schema(implementation = Comment.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing Authentication.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Not authenticated", value = "The requested resource requires authentication.",
                                                    description = "You need to be authenticated to view all comments.")
                                    },
                                    mediaType = "Plain text")
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/search")
    public List<Comment> getAllComments(@RequestHeader HttpHeaders headers,
                                        @RequestParam(required = false) String content,
                                        @RequestParam(required = false) String createdAfter,
                                        @RequestParam(required = false) String createdBefore,
                                        @RequestParam(required = false) String createdBy,
                                        @RequestParam(required = false) String sortBy,
                                        @RequestParam(required = false) String sortOrder) {
        try {
            User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
            CommentFilterOptions commentFilterOptions =
                    new CommentFilterOptions(content, createdBefore, createdAfter,createdBy, sortBy, sortOrder);
            return commentService.getAllComments(userExecutingTheRequest, commentFilterOptions);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Updates a specific comment",
            description = "Used to update the content of a specific comment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body is consisted of the content of the comment only."),
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific id to search in the system",
                            example = "3"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = Comment.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match any user in the database",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no comment with this 'id'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with id '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User trying to execute the request must be either admin OR the same user to which the profile belongs.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable int id,
                                 @RequestHeader HttpHeaders headers,
                                 @Valid @RequestBody CommentDto commentDto) {
        try {
            User userToAuthenticate = authenticationHelper.tryGetUser(headers);
            Comment newComment = modelsMapper.commentFromDto(commentDto, id);
            return commentService.updateComment(newComment, userToAuthenticate);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Puts like to a specific comment",
            description = "Used to like the content of a specific comment.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific id to search in the system",
                            example = "3"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = Comment.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match any user in the database",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no comment with this 'id'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with id '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User trying to execute the request must not be the user that created the comment.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}/likes")
    public Comment likeComment(@PathVariable int id,
                               @RequestHeader HttpHeaders headers) {
        try {
            User userToAuthenticate = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.getCommentById(id);
            return commentService.likeComment(comment, userToAuthenticate);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @Operation(
            summary = "Puts dislike to a specific comment",
            description = "Used to dislike the content of a specific comment.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific id to search in the system",
                            example = "3"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = Comment.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match any user in the database",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no comment with this 'id'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with id '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User trying to execute the request must not be the user that created the comment..",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}/dislikes")
    public Comment dislikeComment(@PathVariable int id,
                                  @RequestHeader HttpHeaders headers) {
        try {
            User userToDislikeComment = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.getCommentById(id);
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
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
