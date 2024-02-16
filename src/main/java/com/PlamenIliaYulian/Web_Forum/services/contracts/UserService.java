package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    User createUser(User user);

    void deleteUser(User userToBeDeleted, User userIsAuthorized);

    User updateUser(User userToBeUpdated, User userIsAuthorized);

    List<User> getAllUsers(UserFilterOptions userFilterOptions);
    List<User> getAllUsers(User user, UserFilterOptions userFilterOptions);

    User getUserByFirstName(String firstName, User userIsAdmin);

    User getUserByUsername(String username);

    User getUserByEmail(String email, User userIsAuthorized);

    User getUserById(int id);

    User addAvatar(int userToBeUpdated, String avatar, User userIsAuthorized);

    User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized);

    User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated);

    User deleteAvatar(int id, User userToDoChanges);
    long getAllUsersCount();

    void addRoleToUser(Role roleToAdd, User userById, User loggedInUser);

    void removeRoleFromUser(Role roleToBeRemoved, User userById, User loggedUser);
}
