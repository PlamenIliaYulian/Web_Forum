package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface UserService {

    /*TODO Plamen*/
    User createUser(User user);

    /*TODO Ilia*/
    void deleteUser(User userToBeDeleted, User userIsAuthorized);

    /*TODO July*/
    User updateUser(User userToBeUpdated, User userIsAuthorized);

    /*TODO Plamen*/
    /*TODO implement User FilterOptions*/
    List<User> getAllUsers();
    /*TODO Ilia*/
    User getUserByFirstName(String firstName, User userIsAdmin);
    /*TODO July*/
    User getUserByUsername(String username);
    /*TODO Plamen*/
    User getUserByEmail(String email, User userIsAdmin);
    /*TODO Ilia*/
    User getUserById(int id);
    /*TODO July*/
    User updateToAdmin(User toBeUpdated, User userIsAdmin);
    /*TODO Plamen*/
    User blockUser(User toBeBlocked, User userIsAdmin);
    /*TODO Ilia*/
    User unBlockUser(User toBeUnblocked, User userIsAdmin);
    /*TODO July*/
    User addAvatar(User userToBeUpdated, byte[] avatar, User userIsAuthorized);
    /*TODO Plamen*/
    User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized);



}
