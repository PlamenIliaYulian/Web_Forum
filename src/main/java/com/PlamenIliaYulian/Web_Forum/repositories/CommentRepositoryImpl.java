package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Comment getCommentById(int id) {
        return null;
    }

    @Override
    public Comment getCommentByContent(String content) {
        return null;
    }

    @Override
    public Comment createComment(Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
            return comment;
        }
    }

    @Override
    public void deleteCommentFromPost(Comment comment) {
    }

    @Override
    public List<Comment> getAllComments() {
        return null;
    }

//    @Override
//    public Comment likeComment(Comment comment) {
//        return null;
//    }
//
//    @Override
//    public Comment dislikeComment(Comment comment) {
//        return null;
//    }
}
