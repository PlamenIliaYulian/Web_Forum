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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.PlamenIliaYulian.Web_Forum.models.Tag;

import java.util.List;
import java.util.Set;

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
                              AuthenticationHelper authenticationHelper,
                              ModelsMapper modelsMapper,
                              TagService tagService) {
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
        this.tagService = tagService;
    }


    @Operation(
            summary = "Creates a new post using the details provided in the body of the post request.",
            description = "Used to create a new post in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "The user trying to create the post has been blocked by the administrators of the system.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match the credentials of any of the users registered in the system.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no username with the specific 'username' provided in the headers. ",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with username 'username' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
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
    @Operation(
            summary = "Deletes specific post from the system.",
            description = "Used to soft delete specific post from the system by given ID.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific ID to search in the system",
                            example = "2"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Post.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            responseCode = "401",
                            description = "User trying to execute the request must be either admin OR the same user to which the profile belongs.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no post with this 'ID'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with ID '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    /*Plamen*/
    @SecurityRequirement(name = "BasicAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id,
                           @RequestHeader HttpHeaders headers) {
        try {
            User userMakingRequest = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
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
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable int id,
                           @RequestHeader HttpHeaders headers,
                           @Valid @RequestBody PostDto postDto) {
        try {
            User userMakingRequest = authenticationHelper.tryGetUser(headers);
            Post postByTitle = postService.getPostById(id);
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
    @Operation(
            summary = "Retrieves specific post in the system.",
            description = "Used to find a post by given title in the system.",
            parameters = {
                    @Parameter(name = "title",
                            description = "Specific title to search in the system",
                            example = "Java Basics"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Post.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no post with this 'title'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with title 'Java Basics' not found.")
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
    @SecurityRequirement(name = "BasicAuth")
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
    @GetMapping("/{id}")
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

    @PutMapping("/{id}/likes")
    public Post likePost(@RequestHeader HttpHeaders headers,
                         @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
            return postService.likePost(post, authenticatedUser);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @Operation(
            summary = "Dislikes specific post.",
            description = "Used to dislike a post by given id in the system.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific id to search in the system",
                            example = "2"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Post.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no post with this 'title'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Post with title 'Java Basics' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "BasicAuth")
    /*Plamen*/
    @PutMapping("/{id}/dislikes")
    public Post dislikePost(@RequestHeader HttpHeaders headers,
                            @PathVariable int id) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
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
    @PutMapping("/{id}/tags")
    public Post addTagToPost(@RequestHeader HttpHeaders headers,
                             @PathVariable int id,
                             @Valid @RequestBody TagDto tagDto) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
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

    @DeleteMapping("/{id}/tags/{tagName}")
    public Post removeTagFromPost(@RequestHeader HttpHeaders headers,
                                  @PathVariable int id,
                                  @PathVariable String tagName) {
        try {
            User authenticatedUser = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(id);
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


    @Operation(
            summary = "Add comment to a specific Post",
            description = "Used to find a post by given Id and add comment to id.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Specific id to search in the system",
                            example = "3"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Post.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no post with this 'id'.",
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
    @SecurityRequirement(name = "BasicAuth")
    /*Plamen*/
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/comments")
    public Post addCommentToPost(@RequestHeader HttpHeaders headers,
                                 @PathVariable int id,
                                 @Valid @RequestBody CommentDto commentDto) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postToComment = postService.getPostById(id);
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

    @DeleteMapping("/{id}/comments/{comment}")
    public Post removeCommentFromPost(@RequestHeader HttpHeaders headers,
                                      @PathVariable int id,
                                      @PathVariable String comment) {

        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postToComment = postService.getPostById(id);
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
    @GetMapping("/{id}/comments")
    public List<Comment> getAllCommentsRelatedToPost(@RequestHeader HttpHeaders headers,
                                                     @PathVariable int id) {
        try {
            User authorizedUser = authenticationHelper.tryGetUser(headers);
            Post postWithComments = postService.getPostById(id);
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

    @GetMapping("/count")
    public Long getAllPostCount() {
        return postService.getAllPostsCount();
    }

    @GetMapping("/top10mostcommented")
    public List<Post> getMostCommentedPosts() {
        return postService.getMostCommentedPosts();
    }
    @GetMapping("/top10mostliked")
    public List<Post> getMostLikedPosts() {
        return postService.getMostCommentedPosts();
    }
    @GetMapping("/top10mostrecentlycreated")
    public List<Post> getMostRecentlyCreatedPosts() {
        return postService.getMostCommentedPosts();
    }
}
