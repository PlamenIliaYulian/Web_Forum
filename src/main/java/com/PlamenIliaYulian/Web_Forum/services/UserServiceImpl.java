package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    /*Ilia*/
    @Override
    public void deleteUser(User userToBeDeleted, User userIsAuthorized) {
        PermissionHelper.isAdminOrSameUser(userToBeDeleted, userIsAuthorized, UNAUTHORIZED_OPERATION);
        userToBeDeleted.setDeleted(true);
        userRepository.updateUser(userToBeDeleted);
    }

    @Override
    public User updateUser(User userToBeUpdated, User userIsAuthorized) {
        PermissionHelper.isAdminOrSameUser(userToBeUpdated, userIsAuthorized, UNAUTHORIZED_OPERATION);
        return userRepository.updateUser(userToBeUpdated);
    }

    @Override
    public List<User> getAllUsers(User userExecutingTheRequest,
                                  UserFilterOptions userFilterOptions) {
        return null;
    }

    /*Ilia*/
    @Override
    public User getUserByFirstName(String firstName, User userIsAdmin) {
        PermissionHelper.isAdmin(userIsAdmin, UNAUTHORIZED_OPERATION);
        return userRepository.getUserByFirstName(firstName);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email,
                               User userIsAuthorized) {
        return userRepository.getUserByEmail(email);
    }

    /*Ilia*/
    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }


    @Override
    public User addAvatar(int userToBeUpdated, byte[] avatar, User userIsAuthorized) {
        User userToUpdateAvatarTo = userRepository.getUserById(userToBeUpdated);
        PermissionHelper.isSameUser(userToUpdateAvatarTo, userIsAuthorized, UNAUTHORIZED_OPERATION);
        userToUpdateAvatarTo.setAvatar(avatar);
        return userRepository.addAvatar(userToUpdateAvatarTo);
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
        return null;
    }

    @Override
    public User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated) {
        PermissionHelper.isAdmin(userToMakeUpdates, UNAUTHORIZED_OPERATION);
        return userRepository.makeAdministrativeChanges(userToBeUpdated);
    }

}

