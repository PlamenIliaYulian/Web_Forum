package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role getRoleById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("FROM Role WHERE roleId = :id ", Role.class);
            query.setParameter("id", id);
            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", id);
            }
            return result.get(0);
        }
    }

    @Override
    public Role getRoleByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("FROM Role WHERE name = :name ", Role.class);
            query.setParameter("name", name);
            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", "name", name);
            }
            return result.get(0);
        }
    }
}
