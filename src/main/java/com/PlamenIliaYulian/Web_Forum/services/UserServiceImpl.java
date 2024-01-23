package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User userToBeDeleted, User userIsAuthorized) {
    }

    @Override
    public User updateUser(User userToBeUpdated, User userIsAuthorized) {
        return null;
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
        return null;
    }

    @Override
    public User getUserByEmail(String email, User userIsAdmin) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User updateToAdmin(User toBeUpdated, User userIsAdmin) {
        return null;
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
        return null;
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized) {
        return null;
    }



}
