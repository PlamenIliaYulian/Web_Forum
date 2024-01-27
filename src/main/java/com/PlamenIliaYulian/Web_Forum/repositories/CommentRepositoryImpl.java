package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            query.setParameter("id", id);
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
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
            return comment;
        }
    }

    @Override
    public Comment updateComment(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
            return getCommentById(comment.getCommentId());
        }
    }

    @Override
    public List<Comment> getAllComments(CommentFilterOptions commentFilterOptions) {
        try (Session session = sessionFactory.openSession();) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();

            commentFilterOptions.getLikes().ifPresent(value -> {
                filters.add(" likes = :likes ");
                parameters.put("likes", value);
            });
            commentFilterOptions.getDislikes().ifPresent(value -> {
                filters.add(" dislikes = :dislikes ");
                parameters.put("dislikes", value);
            });
            commentFilterOptions.getContent().ifPresent(value -> {
                filters.add(" content like :content ");
                parameters.put("content", String.format("%%%s%%", value));
            });

            /*TODO create createAfter for filtering purpose*/
            commentFilterOptions.getCreatedBefore().ifPresent(value -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(value, formatter);

                filters.add(" createdOn < :createdBefore ");
                parameters.put("createdBefore", date);
            });
            commentFilterOptions.getCreatedBy().ifPresent(value -> {
                filters.add(" createdBy.userName like :createdBy ");
                parameters.put("createdBy", String.format("%%%s%%", value));
            });

            filters.add(" isDeleted = false ");
            parameters.put("isDeleted", false);
            StringBuilder queryString = new StringBuilder("FROM Comment ");
                queryString.append(" WHERE ")
                        .append(String.join(" AND ", filters));

            queryString.append(generateOrderBy(commentFilterOptions));
            Query<Comment> query = session.createQuery(queryString.toString(), Comment.class);
            query.setProperties(parameters);
            return query.list();

        }
    }

    private String generateOrderBy(CommentFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "likes":
                orderBy = "likes";
                break;
            case "dislikes":
                orderBy = "dislikes";
                break;
            case "content":
                orderBy = "content";
                break;
            case "createdBefore":
                orderBy = "createdOn";
                break;
            case "createdBy":
                orderBy = "createdBy.userName";
                break;
        }
        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }

}
