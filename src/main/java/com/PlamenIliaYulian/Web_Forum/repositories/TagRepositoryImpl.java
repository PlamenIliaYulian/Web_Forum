package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getTagByName(String name) {
        return null;
    }

    @Override
    public Tag createTag(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
            return tag;
        }
    }

    @Override
    public void deleteTag(Tag tag) {

    }

    @Override
    public Tag updateTag(Tag tag) {
        return null;
    }

    @Override
    public Set<Tag> getAllTags() {
        return null;
    }
}
