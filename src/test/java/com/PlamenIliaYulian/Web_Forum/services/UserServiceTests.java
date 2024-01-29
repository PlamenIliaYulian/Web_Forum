package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
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
}
