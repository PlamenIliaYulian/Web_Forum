package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Tag;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl implements TagRepository{

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
        return null;
    }

    @Override
    public void deleteTag(Tag tag) {

    }

    @Override
    public Tag updateTag(Tag tag) {
        return null;
    }
}
