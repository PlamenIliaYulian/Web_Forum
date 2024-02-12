package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
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
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
        }
        return post;
    }

    /*Ilia - I did not implement this method*/
    @Override
    public Post updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
        return getPostById(post.getPostId());
    }

    @Override
    public Post softDeletePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
        return post;
    }

    /*TODO tags*/
    @Override
    public List<Post> getAllPosts(PostFilterOptions postFilterOptions) {

        try (Session session = sessionFactory.openSession();) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();


            postFilterOptions.getMinLikes().ifPresent(value ->{
                filters.add(" likes >=: minLikes ");
                parameters.put("minLikes", value);
            });

            postFilterOptions.getMaxLikes().ifPresent(value ->{
                filters.add(" likes <=: maxLikes ");
                parameters.put("maxLikes", value);
            });

            postFilterOptions.getMinDislikes().ifPresent(value ->{
                filters.add(" dislikes >=: minDislikes ");
                parameters.put("minDislikes", value);
            });

            postFilterOptions.getMaxDislikes().ifPresent(value ->{
                filters.add(" dislikes <=: maxDislikes ");
                parameters.put("maxDislikes", value);
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


            postFilterOptions.getCreatedAfter().ifPresent(value -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(value, formatter);

                filters.add(" createdOn > :createdAfter ");
                parameters.put("createdAfter", date);
            });


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

    @Override
    public Long getAllPostsCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(*) from Post where isDeleted=false ", Long.class);
            return query.list().get(0);
        }
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post WHERE isDeleted = false ORDER BY size(relatedComments) DESC LIMIT 10", Post.class);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostLikedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post WHERE isDeleted = false ORDER BY likes DESC LIMIT 10", Post.class);
            return query.list();
        }
    }
    @Override
    public List<Post> getMostRecentlyCreatedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post WHERE isDeleted = false ORDER BY createdOn DESC LIMIT 10", Post.class);
            return query.list();
        }
    }

    @Override
    public List<Post> getPostsByCreator(User user) {
        try (Session session = sessionFactory.openSession();) {
            Query<Post> query = session.createQuery("from Post where createdBy.id = :creatorId AND isDeleted = false",
                    Post.class);
            query.setParameter("creatorId", user.getUserId());
            return query.list();
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
