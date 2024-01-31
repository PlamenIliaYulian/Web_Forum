package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import org.hibernate.id.uuid.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    /*ToDO Change names to MockRepository and MockService*/

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void updateUser_Should_Update_User_When_TheSameUserTriesToUpdateTheirProfile() {
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        Mockito.when(userRepository.updateUser(userToBeUpdated)).thenReturn(userToBeUpdated);
        User updatedUser = userService.updateUser(userToBeUpdated, userToBeUpdated);
        Mockito.verify(userRepository, Mockito.times(1)).updateUser(userToBeUpdated);
        Assertions.assertEquals(userToBeUpdated, updatedUser);

    }

    @Test
    public void updateUser_Should_Throw_When_UnauthorizedUserTriesToUpdate() {
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        User userThatCannotUpdate = TestHelpers.createMockNoAdminUser();
        userThatCannotUpdate.setUserId(100);
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> userService.updateUser(userToBeUpdated, userThatCannotUpdate));
    }

    @Test
    public void getUserByUsername_Should_ReturnUser_When_EverythingIsOk() {
        String mockUserNameByWhichWeSearchUser = "Mock_NoAdmin_User";
        Mockito.when(userRepository.getUserByUsername(mockUserNameByWhichWeSearchUser))
                .thenReturn(TestHelpers.createMockNoAdminUser());
        User userWeFound = userService.getUserByUsername(mockUserNameByWhichWeSearchUser);
        Mockito.verify(userRepository, Mockito.times(1))
                .getUserByUsername(mockUserNameByWhichWeSearchUser);
        Assertions.assertEquals(userWeFound, TestHelpers.createMockNoAdminUser());
    }

    @Test
    public void addAvatar_Should_AddAvatar_When_TheCorrectUserIsTryingToUpdateTheAvatar() {
        User userToUpdateAvatarTo = TestHelpers.createMockNoAdminUser();
        Mockito.when(userRepository.getUserById(userToUpdateAvatarTo.getUserId()))
                .thenReturn(userToUpdateAvatarTo);
        Mockito.when(userRepository.updateUser(userToUpdateAvatarTo))
                .thenReturn(userToUpdateAvatarTo);
        User updatedUser = userService.addAvatar(userToUpdateAvatarTo.getUserId(), null, userToUpdateAvatarTo);
        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(userToUpdateAvatarTo);
        Assertions.assertEquals(updatedUser, userToUpdateAvatarTo);
    }

    @Test
    public void addAvatar_Should_ThrowEntityNotFoundException_When_ThereIsNoUserWithProvidedUserID() {
        User userToUpdateAvatarTo = TestHelpers.createMockNoAdminUser();
        Mockito.when(userRepository.getUserById(userToUpdateAvatarTo.getUserId()))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.addAvatar(userToUpdateAvatarTo.getUserId(), null, userToUpdateAvatarTo));
    }

    @Test
    public void addAvatar_Should_ThrowUnauthorizedOperationException_When_TheUserTryingToUpdateAvatarIsNotSame() {
        User userToUpdateAvatarTo = TestHelpers.createMockNoAdminUser();
        User unauthorizedUser = TestHelpers.createMockNoAdminUser();
        unauthorizedUser.setUserId(777);
        Mockito.when(userRepository.getUserById(userToUpdateAvatarTo.getUserId()))
                .thenReturn(unauthorizedUser);
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> userService.addAvatar(userToUpdateAvatarTo.getUserId(), null, userToUpdateAvatarTo));
    }

    @Test
    public void makeAdministrativeChanges_Should_Pass_When_TheUserTryingToMakeTheChangesIsAdmin() {
        User adminUser = TestHelpers.createMockAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        Mockito.when(userRepository.makeAdministrativeChanges(userToBeUpdated))
                .thenReturn(userToBeUpdated);
        User updatedUser = userService.makeAdministrativeChanges(adminUser, userToBeUpdated);
        Mockito.verify(userRepository, Mockito.times(1))
                .makeAdministrativeChanges(userToBeUpdated);
        Assertions.assertEquals(userToBeUpdated, updatedUser);
    }

    @Test
    public void makeAdministrativeChanges_Should_Throw_When_UserTryingToMakeChangesIsNotAdmin() {
        User nonAdminUser = TestHelpers.createMockNoAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.makeAdministrativeChanges(nonAdminUser, userToBeUpdated));
    }

    @Test
    public void createUser_Should_CallRepository(){

        User userToBeCreated = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserByUsername(userToBeCreated.getUserName()))
                        .thenThrow(new EntityNotFoundException("User", "username", userToBeCreated.getUserName()));

        userService.createUser(TestHelpers.createMockNoAdminUser());

        Mockito.verify(userRepository, Mockito.times(1))
                .createUser(Mockito.any(User.class));
    }

    @Test
    public void createUser_Should_Throw_When_UsernameExists(){
        User existingUser = TestHelpers.createMockNoAdminUser();
        User userToBeCreated = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(DuplicateEntityException.class,
                ()-> userService.createUser(userToBeCreated));

    }

    @Test
    public void createUser_Should_Throw_When_EmailExists(){
        User existingUser = TestHelpers.createMockNoAdminUser();
        User userToBeCreated = TestHelpers.createMockNoAdminUser();
        existingUser.setUserName("some new username");

        Assertions.assertThrows(DuplicateEntityException.class,
                ()-> userService.createUser(userToBeCreated));

    }

    @Test
    public void getAllUser_Should_Throw_When_UserIsNotAdmin(){
        UserFilterOptions userFilterOptions = TestHelpers.createUserFilterOptions();
        User nonAdminUser = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> userService.getAllUsers(nonAdminUser, userFilterOptions));

    }

    @Test
    public void getAllUsers_Should_Pass_When_Valid(){
        User adminUser = TestHelpers.createMockAdminUser();
        List<User> users = new ArrayList<>();
        users.add(TestHelpers.createMockNoAdminUser());
        UserFilterOptions userFilterOptions = TestHelpers.createUserFilterOptions();

        Mockito.when(userRepository.getAllUsers(Mockito.any()))
                .thenReturn(users);

        List<User> result = userService.getAllUsers(adminUser, userFilterOptions);

        Mockito.verify(userRepository, Mockito.times(1))
                .getAllUsers(userFilterOptions);

        Assertions.assertEquals(users, result);

    }

    @Test
    public void getUserByUsername_Should_CallRepository(){
        User userToFind = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserByUsername(Mockito.anyString()))
                .thenReturn(userToFind);

        User result = userService.getUserByUsername(userToFind.getUserName());

        Assertions.assertEquals(userToFind, result);
        Assertions.assertEquals(userToFind.getUserId(), result.getUserId());
        Assertions.assertEquals(userToFind.getUserName(), result.getUserName());
        Assertions.assertEquals(userToFind.getEmail(), result.getEmail());

    }

    @Test
    public void addPhoneNumber_Should_Throw_When_UserIsNotAdmin(){
        String phoneNumber = "088913141414";
        User userTryingToAdd = TestHelpers.createMockNoAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        userToBeUpdated.setUserId(777);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> userService.addPhoneNumber(userToBeUpdated, phoneNumber, userTryingToAdd));

    }

    @Test
    public void addPhoneNumber_Should_CallRepository_When_UserIsAdmin(){
        String phoneNumber = "088913141414";
        User userTryingToAdd = TestHelpers.createMockAdminUser();
        User userToBeUpdated = TestHelpers.createMockAdminUser();

        Mockito.when(userRepository.getUserByPhoneNumber(userToBeUpdated.getPhoneNumber()))
                        .thenThrow(new EntityNotFoundException("User", "Phone number", userToBeUpdated.getPhoneNumber()));

        userService.addPhoneNumber(userToBeUpdated, phoneNumber, userTryingToAdd);

        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(userToBeUpdated);

    }

    @Test
    public void addPhoneNumber_Should_Throw_When_PhoneNumberExists(){
        User userToBeUpdated = TestHelpers.createMockAdminUser();
        User userToDoUpdates = TestHelpers.createMockAdminUser();
        String phoneNumber = "0891234567";

        Assertions.assertThrows(DuplicateEntityException.class,
                ()-> userService.addPhoneNumber(userToBeUpdated, phoneNumber, userToDoUpdates));

    }

    /*Ilia*/
    @Test
    public void deleteUser_Throw_When_UserIsNotAdminAndNotSame() {
        User user = TestHelpers.createMockNoAdminUser();
        User userWhoDeletes = TestHelpers.createMockNoAdminUser();
        userWhoDeletes.setUserId(30);

        Assertions
                .assertThrows(UnauthorizedOperationException.class,
                        ()->userService.deleteUser(user,userWhoDeletes));
    }
    /*Ilia*/
    @Test
    public void deleteUser_Should_DeleteUser_When_UserValidParametersPassed() {
        User user = TestHelpers.createMockNoAdminUser();

        userService.deleteUser(user, user);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .updateUser(user);
    }

    /*Ilia*/
    @Test
    public void getUserByFirstName_Throw_When_UserIsNotAdmin() {
        User user = TestHelpers.createMockNoAdminUser();

        Assertions
                .assertThrows(UnauthorizedOperationException.class,
                        ()->userService.getUserByFirstName("firstName",user));
    }

    /*Ilia*/
    @Test
    public void getUserByFirstName_CallRepository_When_ValidParametersPassed() {
        User user = TestHelpers.createMockAdminUser();

        userService.getUserByFirstName("firstName", user);

        Mockito
                .verify(userRepository, Mockito.times(1))
                .getUserByFirstName("firstName");
    }

    /*Ilia*/
    @Test
    public void getUsersById_Should_ReturnUser_When_MethodCalled() {
        Mockito
                .when(userRepository.getUserById(2))
                .thenReturn(TestHelpers.createMockNoAdminUser());

        User user = userService.getUserById(2);

        Assertions
                .assertEquals(2, user.getUserId());
    }
}
