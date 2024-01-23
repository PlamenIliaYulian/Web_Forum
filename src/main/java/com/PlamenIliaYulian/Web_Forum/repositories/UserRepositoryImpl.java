package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final SessionFactory sessionFactory;
    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByFirstName(String firstName) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User updateToAdmin(User toBeUpdated) {
        return null;
    }

    @Override
    public User blockUser(User toBeBlocked) {
        return null;
    }

    @Override
    public User unBlockUser(User toBeUnblocked) {
        return null;
    }

    @Override
    public User addAvatar(User userToBeUpdated, byte[] avatar) {
        return null;
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber) {
        return null;
    }
}
