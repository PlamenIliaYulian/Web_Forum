package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getTagByName(String name) {
        try (Session session = sessionFactory.openSession();) {
            Query<Tag> query = session.createQuery("from Tag where tag = :name AND isDeleted = false",
                    Tag.class);
            query.setParameter("name", name);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Tag", "name", name);
            }
            return query.list().get(0);
        }
    }

    @Override
    public Tag createTag(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
        }
        return tag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tag);
            session.getTransaction().commit();
        }
        return tag;
    }

    @Override
    public List<Tag> getAllTags() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("FROM Tag WHERE isDeleted = false ", Tag.class);
            return query.list();
        }
    }

    @Override
    public Tag getTagById(int tagId) {
        try (Session session = sessionFactory.openSession();) {
            Query<Tag> query = session.createQuery("from Tag where tagId = :id AND isDeleted = false",
                    Tag.class);
            query.setParameter("id", tagId);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Tag", tagId);
            }
            return query.list().get(0);
        }
    }
}
