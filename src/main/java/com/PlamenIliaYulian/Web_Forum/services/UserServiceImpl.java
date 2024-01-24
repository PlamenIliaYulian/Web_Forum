package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<User> getAllUsers() {
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
    public User getUserByEmail(String email, User userIsAdmin) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User updateToAdmin(User toBeUpdated, User userIsAdmin) {
        if (!isAdmin(userIsAdmin)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
        Set<Role> rolesOfTheUserToBeUpdated = toBeUpdated.getRoles();
        rolesOfTheUserToBeUpdated.add(new Role(1, "ROLE_ADMIN"));
        return userRepository.updateToAdmin(toBeUpdated);
    }

    @Override
    public User blockUser(User toBeBlocked, User userIsAdmin) {
        return null;
    }

    @Override
    public User unBlockUser(User toBeUnblocked, User userIsAdmin) {
        return null;
    }

    @Override
    public User addAvatar(User userToBeUpdated, byte[] avatar, User userIsAuthorized) {
        if (!isSameUser(userToBeUpdated, userIsAuthorized)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
        userToBeUpdated.setAvatar(avatar);
        return userRepository.addAvatar(userToBeUpdated);
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
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
