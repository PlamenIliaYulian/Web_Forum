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

    @Override
    public void deleteUser(User userToBeDeleted, User userIsAuthorized) {
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

    @Override
    public User getUserByFirstName(String firstName, User userIsAdmin) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User getUserByEmail(String email, User userIsAuthorized) {
        return userRepository.getUserByEmail(email);
    }

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

    @Override
    public User unBlockUser(User userToDoChanges,int userToBeBlocked) {
        return null;
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
