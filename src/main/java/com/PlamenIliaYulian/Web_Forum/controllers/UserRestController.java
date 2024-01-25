package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserAdministrativeDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserDto;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;

    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        User user = modelsMapper.userFromDto(userDto);
        return userService.createUser(user);
    }

    /*Ilia*/
    @DeleteMapping("/{username}")
    void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        User userIsAuthenticated = authenticationHelper.tryGetUser(headers);
        User userToBeDeleted = userService.getUserByUsername(username);
        userService.deleteUser(userIsAuthenticated, userToBeDeleted);
    }

    /*TODO - Yuli - implemented:*/
    @PutMapping("/{username}")
    User updateUser(@RequestHeader HttpHeaders headers,
                    @PathVariable String username,
                    @Valid @RequestBody UserDto userDto) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromDto(userDto, username);
            return userService.updateUser(userToBeUpdated, userToDoUpdates);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/administrative-changes/{username}")
    public User updateAdministrativeChanges(@PathVariable String username,
                                            @RequestHeader HttpHeaders headers,
                                            @Valid @RequestBody UserAdministrativeDto userAdministrativeDto) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromAdministrativeDto(userAdministrativeDto, username);
            return userService.makeAdministrativeChanges(userToDoUpdates, userToBeUpdated);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    /*Ilia*/
    /*TODO It is not the best way for this method.*/
    @GetMapping("/first_name/{firstName}")
    public User getUserByFirstName(@PathVariable String firstName,
                                   @RequestHeader HttpHeaders headers) {
        User userToBeAuthorized = authenticationHelper.tryGetUser(headers);
        return userService.getUserByFirstName(firstName, userToBeAuthorized);
    }
    /*TODO It is not the best way for this method.*/
    @GetMapping("/username/{username}")
    public User getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }


    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
        UserFilterOptions userFilterOptions = new UserFilterOptions(username, email, firstName, sortBy, sortOrder);
        return userService.getAllUsers(userExecutingTheRequest, userFilterOptions);
    }

    /*TODO It is not the best way for this method.*/
    /*Ilia*/
    @GetMapping("/id/{id}")
    public User getUserById(@RequestParam int id) {
        return userService.getUserById(id);
    }

//    @PutMapping("/{userId}/updateToAdmin")
//    public User updateToAdmin(@PathVariable int userId, @RequestHeader HttpHeaders headers){
//        User userToDoChanges = authenticationHelper.tryGetUser(headers);
//        return userService.updateToAdmin(userToDoChanges, userId);
//    }
//
//    @PutMapping("/{userToBeBlocked}/block")
//    public User blockUser(@PathVariable int userToBeBlocked, @RequestHeader HttpHeaders headers){
//        User userToDoChanges = authenticationHelper.tryGetUser(headers);
//        return userService.blockUser(userToDoChanges, userToBeBlocked);
//    }
//
//    @PutMapping("/{userToBeBlocked}/unblock")
//    public User unBlockUser(@PathVariable int userToBeBlocked, @RequestHeader HttpHeaders headers){
//        User userToDoChanges = authenticationHelper.tryGetUser(headers);
//        return userService.unBlockUser(userToDoChanges, userToBeBlocked);
//    }

    @PutMapping("/{userToBeUpdated}/avatar")
    public User addAvatar(@PathVariable int userToBeUpdated, @RequestBody byte[] avatar, @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            return userService.addAvatar(userToBeUpdated, avatar, userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PutMapping("/{username}/PhoneNumber")
    public User addPhoneNumber(@PathVariable String username, @RequestBody String phoneNumber, @RequestHeader HttpHeaders headers) {
        User userToDoChanges = authenticationHelper.tryGetUser(headers);
        User userToBeUpdated = userService.getUserByUsername(username);
        return userService.addPhoneNumber(userToDoChanges, phoneNumber, userToBeUpdated);
    }

}
