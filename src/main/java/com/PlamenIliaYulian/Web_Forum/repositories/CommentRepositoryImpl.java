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
            Comment comment = session.get(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", id);
            }
            return comment;
        }
    }

    /*Ilia*/
    @Override
    public Comment getCommentByContent(String content) {
        try(Session session = sessionFactory.openSession()) {
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
        return null;
    }
    /*Ilia - We are not using this method.*/
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
//    @Override
//    public Comment dislikeComment(Comment comment, User authorizedUser) {
//        String hqlQuery = "DELETE FROM comments_users_likes "  +
//                "WHERE comment_id = :currentCommentId AND user_id = :currentUserId";
//
//        try(Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            Query query = session.createQuery(hqlQuery);
//            query.setParameter("comment_id", comment.getCommentId());
//            query.setParameter("user_id", authorizedUser.getUserId());
//            query.executeUpdate();
//            session.getTransaction().commit();
//
//        }
//        return comment;
//    }
}
