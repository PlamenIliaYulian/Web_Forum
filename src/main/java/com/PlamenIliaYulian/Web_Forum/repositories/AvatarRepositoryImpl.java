package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AvatarRepositoryImpl implements AvatarRepository {

    private final SessionFactory sessionFactory;

    public AvatarRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Avatar getAvatarById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Avatar> query = session.createQuery("from Avatar where avatarId = :id", Avatar.class);
            query.setParameter("id", id);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Avatar", id);
            }
            return query.list().get(0);
        }
    }

    public Avatar createAvatar(Avatar avatar) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(avatar);
            session.getTransaction().commit();
            return getAvatarById(avatar.getAvatarId());
        }
    }

    @Override
    public Avatar getDefaultAvatar() {
        try (Session session = sessionFactory.openSession()) {
            Query<Avatar> query = session.createQuery("FROM Avatar WHERE avatarId = 1", Avatar.class);
            List<Avatar> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Avatar", "avatar ID", "#1");
            }
            return result.get(0);
        }
    }

    @Override
    public Avatar getAvatarByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Avatar> query = session.createQuery("from Avatar where avatar = :name", Avatar.class);
            query.setParameter("name", name);

            List<Avatar> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Avatar", "avatar name", name);
            }
            return result.get(0);
        }
    }
}
