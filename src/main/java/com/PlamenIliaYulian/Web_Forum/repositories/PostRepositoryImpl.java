package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    @Override
    public void deletePost(Post post) {

    }

    @Override
    public Post updatePost(Post post) {
        return null;
    }

    @Override
    public List<Post> getAllPosts() {
        return null;
    }

    @Override
    public Post getPostByTitle(String title) {
        return null;
    }

    @Override
    public Post getPostById(int id) {
        return null;
    }

    @Override
    public Post likePost(Post post) {
        return null;
    }

    @Override
    public Post dislikePost(Post post) {
        return null;
    }

    @Override
    public Post addTagToPost(Post post) {
        return null;
    }

    @Override
    public Post removeTagToPost(Post post) {
        return null;
    }

    @Override
    public Post addCommentToPost(Post postToComment) {
        return null;
    }
}
