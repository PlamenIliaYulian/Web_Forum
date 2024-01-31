package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.services.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
        /*Unique email validation.*/
        /*Unique username validation.*/
        /*Ilia -The validations has to be moved to a separate method in this class.
        Otherwise, it is hard to read and to understand the logic behind the code*/
        boolean duplicateExists = true;

        try {
            userRepository.getUserByUsername(user.getUserName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUserName());
        }


        try {
            userRepository.getUserByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUserName());
        }

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
        /*Unique email validation.*/
        PermissionHelper.isAdminOrSameUser(userToBeUpdated, userIsAuthorized, UNAUTHORIZED_OPERATION);
        return userRepository.updateUser(userToBeUpdated);
    }

    @Override
    public List<User> getAllUsers(User userExecutingTheRequest,
                                  UserFilterOptions userFilterOptions) {
        PermissionHelper.isAdmin(userExecutingTheRequest, UNAUTHORIZED_OPERATION);
        return userRepository.getAllUsers(userFilterOptions);
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
        return userRepository.updateUser(userToUpdateAvatarTo);
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
        /*Unique phone number validation.*/
        PermissionHelper.isAdmin(userToBeUpdated, UNAUTHORIZED_OPERATION);
        /*Check if the two users are one and the same. If not - throw exception.
         * If we want to let the admin set phone number to another admin we can set
         * it in administrative changes.*/

        /*Ilia -The validations has to be moved to a separate method in this class.
        Otherwise, it is hard to read and to understand the logic behind the code*/

        boolean duplicateExists = true;

        try {
            userRepository.getUserByPhoneNumber(userToBeUpdated.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "Phone number", userToBeUpdated.getPhoneNumber());
        }

        userToBeUpdated.setPhoneNumber(phoneNumber);
        return userRepository.updateUser(userToBeUpdated);
    }

    @Override
    public User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated) {
        PermissionHelper.isAdmin(userToMakeUpdates, UNAUTHORIZED_OPERATION);
        return userRepository.makeAdministrativeChanges(userToBeUpdated);
    }

}

