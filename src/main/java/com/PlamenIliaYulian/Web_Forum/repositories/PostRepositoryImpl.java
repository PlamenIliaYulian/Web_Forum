package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.TemporalAccessorParser;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return getPostById(post.getPostId());
        }
    }

    @Override
    public Post softDeletePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public List<Post> getAllPosts(PostFilterOptions postFilterOptions) {

        try (Session session = sessionFactory.openSession();) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();

            postFilterOptions.getLikes().ifPresent(value -> {
                filters.add(" likes = :likes ");
                parameters.put("likes", value);
            });
            postFilterOptions.getDislikes().ifPresent(value -> {
                filters.add(" dislikes = :dislikes ");
                parameters.put("dislikes", value);
            });
            postFilterOptions.getTitle().ifPresent(value -> {
                filters.add(" title like :title ");
                parameters.put("title", String.format("%%%s%%", value));
            });
            postFilterOptions.getContent().ifPresent(value -> {
                filters.add(" content like :content ");
                parameters.put("content", String.format("%%%s%%", value));
            });

            postFilterOptions.getCreatedBefore().ifPresent(value -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(value, formatter);

                filters.add(" createdOn < :createdBefore ");
                parameters.put("createdBefore", date);
            });

            /*TODO CRATED AFTER IMPL YULI*/
            postFilterOptions.getCreatedBy().ifPresent(value -> {
                filters.add(" createdBy.userName like :createdBy ");
                parameters.put("createdBy", String.format("%%%s%%", value));
            });

            filters.add(" isDeleted = false ");
            parameters.put("isDeleted", false);
            StringBuilder queryString = new StringBuilder("FROM Post ");
            queryString.append(" WHERE ")
                    .append(String.join(" AND ", filters));

            queryString.append(generateOrderBy(postFilterOptions));
            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(parameters);
            return query.list();

        }
    }

    @Override
    public Post getPostByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title AND isDeleted = false", Post.class);
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

    private String generateOrderBy(PostFilterOptions filterOptions) {
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
            case "title":
                orderBy = "title";
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

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }
}
