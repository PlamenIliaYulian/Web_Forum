package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    /*Ilia - we don't need this method.*/
    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    /*Ilia*/
    @Override
    public User getUserByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE firstName = :firstName AND isDeleted = false ", User.class);
            query.setParameter("firstName", firstName);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "first name", firstName);
            }
            return result.get(0);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE userName = :username AND isDeleted = false ", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email and isDeleted = false ", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    /*Ilia*/
    @Override
    public User getUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE userId = :id AND isDeleted = false ", User.class);
            query.setParameter("id", id);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", id);
            }
            return result.get(0);
        }
    }

    @Override
    public User updateToAdmin(User toBeUpdated) {
        return updateUser(toBeUpdated);
    }

    @Override
    public User blockUser(User toBeBlocked) {
        return null;
    }

    /*Ilia - not calling this method. We don't need it.*/
    @Override
    public User unBlockUser(User toBeUnblocked) {
        return null;
    }

    @Override
    public User addAvatar(User userToBeUpdated) {
        return updateUser(userToBeUpdated);
    }

    @Override
    public User addPhoneNumber(User userToBeUpdated, String phoneNumber) {
        return null;
    }

    @Override
    public User makeAdministrativeChanges(User userToBeUpdated) {
        return null;
    }
}
