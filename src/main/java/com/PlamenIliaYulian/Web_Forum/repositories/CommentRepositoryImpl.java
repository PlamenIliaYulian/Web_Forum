package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment where commentId = :id and isDeleted = false ", Comment.class);
            query.setParameter("commentId", id);
            if(query.list().isEmpty()){
                throw new EntityNotFoundException("Comment", id);
            }
            return query.list().get(0);
        }
    }

    /*Ilia*/
    @Override
    public Comment getCommentByContent(String content) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment b where b.content = :content AND b.isDeleted = false",
                    Comment.class);
            query.setParameter("content", content);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Comment", "content", content);
            }
            return query.list().get(0);
        }
    }

    @Override
    public Comment createComment(Comment comment) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
            return comment;
        }
    }

    @Override
    public List<Comment> getAllComments() {
        return null;
    }

}
