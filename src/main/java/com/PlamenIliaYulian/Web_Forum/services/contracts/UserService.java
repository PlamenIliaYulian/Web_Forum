package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    /*Plamen*/
    User createUser(User user);

    /*✔ Ilia ✔*/
    void deleteUser(User userToBeDeleted, User userIsAuthorized);

    /*July - 📌 DONE 📌 - last updated 25.01.2024*/
    User updateUser(User userToBeUpdated, User userIsAuthorized);

    /*Plamen*/
    List<User> getAllUsers(User user, UserFilterOptions userFilterOptions);

    /*✔  Ilia ✔ */
    User getUserByFirstName(String firstName, User userIsAdmin);

    /*July - 📌 DONE 📌 - last updated 25.01.2024*/
    User getUserByUsername(String username);

    /*TODO test Plamen*/
    User getUserByEmail(String email, User userIsAuthorized);

    /*✔  Ilia ✔ */
    User getUserById(int id);

    /*July - 📌 DONE 📌- last updated 25.01.2024*/
    User addAvatar(int userToBeUpdated, byte[] avatar, User userIsAuthorized);

    /*Plamen*/
    User addPhoneNumber(User userToBeUpdated, String phoneNumber, User userIsAuthorized);

    /*July - 📌 DONE 📌- last updated 25.01.2024*/
    User makeAdministrativeChanges(User userToMakeUpdates, User userToBeUpdated);

    /*Ilia TODO tests*/
    User deleteAvatar(int id, User userToDoChanges);
}
