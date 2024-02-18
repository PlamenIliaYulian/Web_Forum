package com.PlamenIliaYulian.Web_Forum.controllers.REST;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.dtos.PhoneNumberDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserAdministrativeDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserDtoUpdate;
import com.PlamenIliaYulian.Web_Forum.services.contracts.AvatarService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;
    private final AvatarService avatarService;

    public UserRestController(UserService userService,
                              AuthenticationHelper authenticationHelper,
                              ModelsMapper modelsMapper,
                              AvatarService avatarService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
        this.avatarService = avatarService;
    }

    @Operation(
            summary = "Creates new user in the system.",
            description = "Used to created new user with given username, first name, last name, password and email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body consists of the username, password, first name, last name and the email of the user."),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Username", value = "Duplication exist.",
                                                    description = "User with provided username already exists."),
                                            @ExampleObject(name = "Email", value = "Duplication exist.",
                                                    description = "User with provided email already exists.")

                                    },
                                    mediaType = "Plain text")
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {

        try {
            User user = modelsMapper.userFromDto(userDto);
            return userService.createUser(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(
            summary = "Delete a single user by numeric ID.",
            description = "Delete a user by providing numeric ID in the endpoint. You need to be either admin or the user itself.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID must be numeric. For example '/api/v1/users/3'.",
                            example = "3"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Success response when the user has been deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found status.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Missing user", value = "User with ID '200' not found.",
                                                    description = "There is no such user with the provided ID.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing Authentication.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Not authenticated", value = "The requested resource requires authentication.",
                                                    description = "You need to be authenticated to view a User.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Not authorized.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Not authorized", value = "Unauthorized operation.",
                                                    description = "Only admin or user itself can view User info.")
                                    },
                                    mediaType = "Plain text")
                    )
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteUser(@RequestHeader HttpHeaders headers,
                    @PathVariable int id) {
        try {
            User userIsAuthenticated = authenticationHelper.tryGetUser(headers);
            User userToBeDeleted = userService.getUserById(id);
            userService.deleteUser(userIsAuthenticated, userToBeDeleted);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    @Operation(
            summary = "Updates the information of the user found by the numeric ID.",
            description = "Used to update User's first name, last name, email or password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body consists of the password, first name, last name and the email of the user."),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID must be numeric. For example '/api/v1/users/3'.",
                            example = "3"
                    ),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no user with this 'ID'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with ID '3' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Email", value = "Duplication exist.",
                                                    description = "User with provided email already exists.")

                                    },
                                    mediaType = "Plain text")
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    User updateUser(@RequestHeader HttpHeaders headers,
                    @PathVariable int id,
                    @Valid @RequestBody UserDtoUpdate userDtoUpdate) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromDtoUpdate(userDtoUpdate, id);
            return userService.updateUser(userToBeUpdated, userToDoUpdates);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Operation(
            summary = "Updates a more specific information (deleting user, blocking user etc.) of the user found by the provided id.",
            description = "Used by the system's administrators to delete, block / unblock etc. a specific user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body consists of the array of Roles, isAdmin and isBlocked."),
            parameters = {
                    @Parameter(name = "id",
                            description = "Path variable (the id of the user whose profile will be updated.",
                            example = "1"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match any of the admins' login credentials.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "User trying to execute the request must be an admin.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no user with this 'id'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with ID '3' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}/administrative-changes")
    public User updateAdministrativeChanges(@PathVariable int id,
                                            @RequestHeader HttpHeaders headers,
                                            @Valid @RequestBody UserAdministrativeDto userAdministrativeDto) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromAdministrativeDto(userAdministrativeDto, id);
            return userService.makeAdministrativeChanges(userToDoUpdates, userToBeUpdated);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Retrieves information related to a specific users registered in the system.",
            description = "Used to retrieve information about users with optional filtering applied.",
            parameters = {
                    @Parameter(name = "username",
                            description = "Username to apply filtering with.",
                            example = "emily_jackson"),
                    @Parameter(name = "email",
                            description = "Email to apply filtering with",
                            example = "emily_jackson@email.com"),
                    @Parameter(name = "first_name",
                            description = "First name of the user to apply filtering with",
                            example = "Emily"),
                    @Parameter(name = "sortBy",
                            description = "Sort users by specific condition",
                            example = "emily"),
                    @Parameter(name = "sortOrder",
                            description = "Sort order of the filtering",
                            example = "desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/search")
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        try {
            User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
            UserFilterOptions userFilterOptions = new UserFilterOptions(username, email, firstName, sortBy, sortOrder);
            return userService.getAllUsers(userExecutingTheRequest, userFilterOptions);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @Operation(
            summary = "View a single user info by numeric ID.",
            description = "Get a user info by providing numeric ID in the endpoint. You need to be either admin or the user itself.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID must be numeric. For example '/api/v1/users/3'.",
                            example = "3"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Response"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found status.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Missing user", value = "User with ID '200' not found.",
                                                    description = "There is no such user with the provided ID.")
                                    },
                                    mediaType = "Plain text")
                    )
            })
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        try {
            return userService.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @Operation(
            summary = "Uploads an avatar / profile picture to user's profile.",
            description = "Used to update user's profile by updating their avatar / profile picture.",
            parameters = {
                    @Parameter(name = "userToBeUpdated",
                            description = "ID of the user whose profile will be updated.",
                            example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no user with this 'username'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with ID '3' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping(value = "/{id}/avatar")
    public User updateAvatar(@PathVariable int id,
                             @RequestParam("avatar") MultipartFile file,
                             @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            String newAvatar = avatarService.uploadPictureToCloudinary(file);
            return userService.addAvatar(id, newAvatar, userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Deletes an avatar / profile picture to user's profile and puts the default one.",
            description = "Used to update user's profile by deleting its avatar / profile picture.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the user must be numeric. For example '/api/v1/users/3/avatar'.",
                            example = "3"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found status.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Missing user", value = "User with ID '200' not found.",
                                                    description = "There is no such user with the provided ID.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing Authentication.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Not authenticated", value = "The requested resource requires authentication.",
                                                    description = "You need to be authenticated to view a User.")
                                    },
                                    mediaType = "Plain text")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Not authorized.",
                            content = @Content(
                                    examples = {
                                            @ExampleObject(name = "Not authorized", value = "Unauthorized operation.",
                                                    description = "Only admin or user itself can delete user avatar photo.")
                                    },
                                    mediaType = "Plain text")
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{id}/avatar")
    public User deleteAvatar(@PathVariable int id,
                             @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            return userService.deleteAvatar(id, userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Uploads a phone number to user's profile.",
            description = "Used to update user's profile by updating their phone number.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body consists of the phone number that has to be attached to the user."),
            parameters = {
                    @Parameter(name = "userToBeUpdated",
                            description = "ID of the user whose profile will be updated.",
                            example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success response.",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
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
                            description = "There is no user with this 'ID'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with ID 1 not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}/phone-number")
    public User addPhoneNumber(@PathVariable int id,
                               @RequestBody PhoneNumberDto phoneNumberDto,
                               @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = userService.getUserById(id);
            return userService.addPhoneNumber(userToBeUpdated, phoneNumberDto.getPhone(), userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Pulls from the database the count of all created users in the system.",
            description = "Used to obtain the total amount of users registered in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Response"
                    )
            })
    @GetMapping("/count")
    public Long getAllUsersCount() {
        return userService.getAllUsersCount();
    }
}
