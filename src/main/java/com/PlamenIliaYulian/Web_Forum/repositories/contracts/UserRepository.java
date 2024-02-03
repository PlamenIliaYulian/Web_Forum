package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    User createUser(User user);
    User updateUser(User user);
    List<User> getAllUsers(UserFilterOptions userFilterOptions);

    User getUserByFirstName(String firstName);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserById(int id);
    User getUserByPhoneNumber(String phoneNumber);
    User makeAdministrativeChanges(User userToBeUpdated);
    byte[] getDefaultAvatar();
}
