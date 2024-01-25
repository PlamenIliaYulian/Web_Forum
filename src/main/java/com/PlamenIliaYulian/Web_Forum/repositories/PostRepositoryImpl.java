package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Post createPost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
            return post;
        }
    }

    /*Ilia - I did not implement this method*/
    @Override
    public Post updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return null;
    }

    @Override
    public Post getPostByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }
            return result.get(0);
        }
    }

    /*Ilia*/
    @Override
    public Post getPostById(int id) {
        try (Session session = sessionFactory.openSession();) {
            Query<Post> query = session.createQuery("from Post where postId = :post_id AND isDeleted = false",
                    Post.class);
            query.setParameter("post_id", id);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Post", id);
            }
            return query.list().get(0);
        }
    }
}
