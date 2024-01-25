package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    /*TODO Plamen*/
    User createUser(User user);

    /*TODO ✔ Ilia ✔*/
    void deleteUser(User userToBeDeleted, User userIsAuthorized);

    /*TODO July - DONE*/
    User updateUser(User userToBeUpdated, User userIsAuthorized);

    /*TODO Plamen*/
    /*TODO implement User FilterOptions
    *  Don't forget to check if they are authorized(admin only)*/
    List<User> getAllUsers(User user, UserFilterOptions userFilterOptions);

    /*TODO ✔  Ilia ✔ */
    User getUserByFirstName(String firstName, User userIsAdmin);

    /*TODO July - DONE*/
    User getUserByUsername(String username);

    /*TODO Plamen*/
    User getUserByEmail(String email, User userIsAuthorized);

    /*TODO ✔  Ilia ✔ */
    User getUserById(int id);

    /*TODO July - DONE*/
    User updateToAdmin(User toBeUpdated, int userIsAdmin);

    /*TODO Plamen*/
    User blockUser(User userToDoChanges,int userToBeBlocked);

    /*TODO ✔ Ilia ✔*/
    User unBlockUser(User userToDoChanges,int userToBeBlocked);

    /*TODO July - DONE*/
    User addAvatar(int userToBeUpdated, byte[] avatar, User userIsAuthorized);

    /*TODO Plamen*/
    User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized);

    /*TODO July*/
    User makeAdministrativeChanges(User userToDoUpdates, User userToBeUpdated);
}
