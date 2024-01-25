package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    /*TODO Plamen*/
    User createUser(User user);

    /*TODO ✔ Ilia ✔*/
    void deleteUser(User userToBeDeleted, User userIsAuthorized);

    /*TODO July - DONE - last updated 25.01.2024*/
    User updateUser(User userToBeUpdated, User userIsAuthorized);

    /*TODO Plamen*/
    /*TODO implement User FilterOptions
    *  Don't forget to check if they are authorized(admin only)*/
    List<User> getAllUsers(User user, UserFilterOptions userFilterOptions);

    /*TODO ✔  Ilia ✔ */
    User getUserByFirstName(String firstName, User userIsAdmin);

    /*TODO July - DONE - last updated 25.01.2024*/
    User getUserByUsername(String username);

    /*TODO Plamen*/
    User getUserByEmail(String email, User userIsAuthorized);

    /*TODO ✔  Ilia ✔ */
    User getUserById(int id);

    /*TODO July - DONE - last updated 25.01.2024*/
    User updateToAdmin(User toBeUpdated, int userIsAdmin);

    /*TODO Plamen*/
    User blockUser(User userToDoChanges,int userToBeBlocked);

    /*TODO ✔ Ilia ✔*/
    User unBlockUser(User userToDoChanges,int userToBeBlocked);

    /*TODO July - DONE - last updated 25.01.2024*/
    User addAvatar(int userToBeUpdated, byte[] avatar, User userIsAuthorized);

    /*TODO Plamen*/
    User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized);

    /*TODO July - DONE - last updated 25.01.2024*/
    User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated);
}
