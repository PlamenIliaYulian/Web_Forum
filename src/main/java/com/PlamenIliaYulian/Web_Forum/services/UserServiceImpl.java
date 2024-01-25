package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
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
        /*This method have to return exception if no authorization. Don't have to do it in our method.*/
        if(!isAdminOrSameUser(userToBeDeleted,userIsAuthorized)) {
            throw new UnauthorizedOperationException("You are not admin or the stated user to modify/delete user.");
        }
        userToBeDeleted.setDeleted(true);
        userRepository.updateUser(userToBeDeleted);
    }

    @Override
    public User updateUser(User userToBeUpdated, User userIsAuthorized) {
        if (!isAdminOrSameUser(userToBeUpdated, userIsAuthorized)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
        return userRepository.updateUser(userToBeUpdated);
    }

    @Override
    public List<User> getAllUsers(User userExecutingTheRequest, UserFilterOptions userFilterOptions) {
        return null;
    }

    /*Ilia*/
    @Override
    public User getUserByFirstName(String firstName, User userIsAdmin) {
        /*I'm not sure how we will use this method.*/
        /*This method have to return exception if no authorization. Don't have to do it in our method.*/
        if(!isAdmin(userIsAdmin)) {
            throw new UnauthorizedOperationException("You are not admin and cannot get user by first name.");
        }
        return userRepository.getUserByFirstName(firstName);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email, User userIsAuthorized) {
        return userRepository.getUserByEmail(email);
    }

    /*Ilia*/
    @Override
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User updateToAdmin(User userTryingToAuthorize, int userToBeChangedToAdmin) {
        if (!isAdmin(userTryingToAuthorize)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
        User userToBeUpdated = userRepository.getUserById(userToBeChangedToAdmin);
        Set<Role> rolesOfTheUserToBeUpdated = userToBeUpdated.getRoles();
        rolesOfTheUserToBeUpdated.add(new Role(1, "ROLE_ADMIN"));
        return userRepository.updateToAdmin(userToBeUpdated);
    }

    @Override
    public User blockUser(User userToDoChanges,int userToBeBlocked) {
        return null;
    }

    /*Ilia*/
    @Override
    public User unBlockUser(User userToDoChanges,int idUserToBeUnblocked) {
        if (!isAdmin(userToDoChanges)) {
            throw new UnauthorizedOperationException("You are not the admin and cannot block users.");
        }
        User userToBeUnblocked = userRepository.getUserById(idUserToBeUnblocked);
        userToBeUnblocked.setBlocked(false);
        return userRepository.updateUser(userToBeUnblocked);
    }

    @Override
    public User addAvatar(int userToBeUpdated, byte[] avatar, User userIsAuthorized) {
//        if (!isSameUser(userToBeUpdated, userIsAuthorized)) {
//            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
//        }
//        userToBeUpdated.setAvatar(avatar);
//        return userRepository.addAvatar(userToBeUpdated);
        return  null;
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
        return null;
    }

    @Override
    public User makeAdministrativeChanges(User userToDoUpdates, User userToBeUpdated) {
        return null;
    }

    private boolean isAdminOrSameUser(User userToBeUpdated, User userIsAuthorized) {
        if (!userToBeUpdated.equals(userIsAuthorized)) {
            List<Role> rolesOfAuthorizedUser = userIsAuthorized.getRoles().stream().toList();
            for (Role currentRoleToBeChecked : rolesOfAuthorizedUser) {
                if (currentRoleToBeChecked.getName().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAdmin(User userIsAuthorized) {
        List<Role> rolesOfAuthorizedUser = userIsAuthorized.getRoles().stream().toList();
        for (Role currentRoleToBeChecked : rolesOfAuthorizedUser) {
            if (currentRoleToBeChecked.getName().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameUser(User userToBeUpdated, User userIsAuthorized) {
        return userToBeUpdated.equals(userIsAuthorized);
    }
}
