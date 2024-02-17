package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.AvatarService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import com.PlamenIliaYulian.Web_Forum.services.helpers.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";
    private final UserRepository userRepository;

    private final AvatarService avatarService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AvatarService avatarService) {
        this.userRepository = userRepository;
        this.avatarService = avatarService;
    }

    @Override
    public User createUser(User user) {
        checkForUniqueUsername(user);
        checkForUniqueEmail(user);
        user.setAvatar(avatarService.getDefaultAvatar());
        return userRepository.createUser(user);
    }

    @Override
    public void deleteUser(User userToBeDeleted, User userIsAuthorized) {
        PermissionHelper.isAdminOrSameUser(userToBeDeleted, userIsAuthorized, UNAUTHORIZED_OPERATION);
        userToBeDeleted.setDeleted(true);
        userRepository.updateUser(userToBeDeleted);
    }

    @Override
    public User updateUser(User userToBeUpdated, User userIsAuthorized) {
        PermissionHelper.isAdminOrSameUser(userToBeUpdated, userIsAuthorized, UNAUTHORIZED_OPERATION);
        checkForUniqueEmail(userToBeUpdated);
        return userRepository.updateUser(userToBeUpdated);
    }

    @Override
    public List<User> getAllUsers(UserFilterOptions userFilterOptions) {
        return userRepository.getAllUsers(userFilterOptions);
    }

    @Override
    public List<User> getAllUsers(User userExecutingTheRequest,
                                  UserFilterOptions userFilterOptions) {
        PermissionHelper.isAdmin(userExecutingTheRequest, UNAUTHORIZED_OPERATION);
        return userRepository.getAllUsers(userFilterOptions);
    }

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

    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }


    @Override
    public User addAvatar(int userToBeUpdated, String avatar, User userIsAuthorized) {
        User userToUpdateAvatarTo = userRepository.getUserById(userToBeUpdated);
        PermissionHelper.isSameUser(userToUpdateAvatarTo, userIsAuthorized, UNAUTHORIZED_OPERATION);

        Avatar avatarToAdd = new Avatar();
        avatarToAdd.setAvatar(avatar);
        avatarToAdd = avatarService.createAvatar(avatarToAdd);

        userToUpdateAvatarTo.setAvatar(avatarToAdd);
        return userRepository.updateUser(userToUpdateAvatarTo);
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
        PermissionHelper.isAdmin(userToBeUpdated, UNAUTHORIZED_OPERATION);
        checkIfPhoneNumberUnique(userToBeUpdated);
        userToBeUpdated.setPhoneNumber(phoneNumber);
        return userRepository.updateUser(userToBeUpdated);
    }



    @Override
    public User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated) {
        PermissionHelper.isAdmin(userToMakeUpdates, UNAUTHORIZED_OPERATION);
        return userRepository.makeAdministrativeChanges(userToBeUpdated);
    }

    @Override
    public User deleteAvatar(int id, User userToDoChanges) {
        PermissionHelper.isAdminOrSameUser(userRepository.getUserById(id), userToDoChanges, UNAUTHORIZED_OPERATION);
        userToDoChanges.setAvatar(avatarService.getDefaultAvatar());
        return userToDoChanges;
    }

    @Override
    public long getAllUsersCount() {
        return userRepository.getAllUsersCount();
    }

    /*TODO test*/
    @Override
    public void addRoleToUser(Role roleToAdd, User userById, User loggedInUser) {
        PermissionHelper.isAdmin(loggedInUser, UNAUTHORIZED_OPERATION);
        userById.getRoles().add(roleToAdd);
        userRepository.updateUser(userById);
    }

    /*TODO test*/
    @Override
    public void removeRoleFromUser(Role roleToBeRemoved, User userById, User loggedUser) {
        PermissionHelper.isAdmin(loggedUser, UNAUTHORIZED_OPERATION);
        userById.getRoles().remove(roleToBeRemoved);
        userRepository.updateUser(userById);
    }

    private void checkForUniqueUsername(User user) {
        boolean duplicateExists = true;

        try {
            userRepository.getUserByUsername(user.getUserName());

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUserName());
        }
    }

    private void checkForUniqueEmail(User user) {
        boolean duplicateExists = true;

        try {
            User existingUser = userRepository.getUserByEmail(user.getEmail());
            if (existingUser.getUserId() == user.getUserId()) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }
    private void checkIfPhoneNumberUnique(User userToBeUpdated) {
        boolean duplicateExists = true;

        try {
            User user = userRepository.getUserByPhoneNumber(userToBeUpdated.getPhoneNumber());
            if (user.getUserId() == userToBeUpdated.getUserId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "Phone number", userToBeUpdated.getPhoneNumber());
        }
    }
}

