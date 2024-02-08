package com.PlamenIliaYulian.Web_Forum.controllers.REST;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.TagDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag")
public class TagRestController {

    private final TagService tagService;
    private final AuthenticationHelper authenticationHelper;

    private final ModelsMapper modelsMapper;


    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }

    @Operation(
            summary = "Pulls from the database the details of all tags created in the system.",
            description = "Used to obtain the information of all tags available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The details of all tags available in the system.",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = Tag.class)))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "The login credentials of the user trying to execute the creation do not match the respective record in the database.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
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
    @GetMapping("/search")
    public List<Tag> getAllTags(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return tagService.getAllTags();
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Get a single tag by numeric ID.",
            description = "Get only one tag info by providing numeric ID in the endpoint.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID must be numeric. For example '/api/v1/tags/3'.",
                            example = "2")

            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Response",
                            content = @Content(
                                    schema = @Schema(implementation = Tag.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found status.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Missing tag", value = "Tag with ID '200' not found.",
                                                    description = "There is no such tag with the provided ID.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing Authentication.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Not authenticated", value = "The requested resource requires authentication.",
                                                    description = "You need to be authenticated to get a tag by ID.")
                                    },
                                    mediaType = "Plain text")
                    ),
            })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable int id,
                          @RequestHeader HttpHeaders headers) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            return tagService.getTagById(id);
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


    @Operation(
            summary = "Creates a new tag using the details provided in the body of the post request.",
            description = "Used to create a new tag in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "The login credentials of the user trying to execute the creation do not match the respective record in the database.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
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
    @PostMapping()
    public Tag createTag(@RequestBody TagDto tagDto,
                         @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = modelsMapper.tagFromDto(tagDto);
            return tagService.createTag(tag, user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Deletes tag by the provided tag ID.",
            description = "Used to delete specific Tag by given ID.",
            parameters = {
                    @Parameter(name = "id",
                            description = "Path variable.",
                            example = "2"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Tag.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "Username trying to execute the request must be either admin OR the same user to which the profile belongs.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no tag with this 'ID'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Tag with ID '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    /*Plamen*/
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@RequestHeader HttpHeaders headers,
                          @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = tagService.getTagById(id);
            tagService.deleteTag(tag, user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @Operation(
            summary = "Modify Tag by numeric ID.",
            description = "Use numeric ID to change a tag.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body is consisted of a tag as text."),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "A numeric path variable.",
                            example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Response",
                            content = @Content(
                                    schema = @Schema(implementation = Tag.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no tag with this 'ID'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Tag with ID '2' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing Authentication.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Not authenticated", value = "The requested resource requires authentication.",
                                                    description = "You need to be authenticated to modify a Beer.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Not authorized.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Not authorized", value = "Unauthorized operation.",
                                                    description = "Only admin or beer creator can modify the Beer.")
                                    },
                                    mediaType = "Plain text")
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public Tag updateTag(@RequestHeader HttpHeaders headers,
                         @PathVariable int id,
                         @RequestBody TagDto tagDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tagToBeUpdated = modelsMapper.tagFromDto(tagDto, id);
            return tagService.updateTag(tagToBeUpdated, user);
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
