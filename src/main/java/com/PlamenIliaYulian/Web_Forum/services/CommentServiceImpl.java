package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";
    public static final String MULTIPLE_LIKE_ERROR = "You have already liked this comment";
    public static final String YOU_ARE_THE_CREATOR_OF_THIS_COMMENT = "You are the creator of this comment";

    private final UserService userService;
    private final CommentRepository commentRepository;


    @Autowired
    public CommentServiceImpl(UserService userService, CommentRepository commentRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }

    /*Ilia*/
    @Override
    public Comment getCommentByContent(String content) {
        return commentRepository.getCommentByContent(content);
    }

    /*TODO - Yuli - DONE. Last updated on 26.01.2024.*/
    @Override
    public Comment createComment(Comment comment, User commentCreator) {
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setDeleted(false);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setCreatedBy(commentCreator);
        return commentRepository.createComment(comment);
    }

    @Override
    public Comment updateComment(Comment comment, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(comment.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);
        return commentRepository.updateComment(comment);
    }

    /*Ilia.*/
    @Override
    public void deleteComment(Comment comment) {

    }

    @Override
    public List<Comment> getAllComments(User userExecutingTheRequest, CommentFilterOptions commentFilterOptions) {
        return commentRepository.getAllComments(commentFilterOptions);
    }

    @Override
    public Comment likeComment(Comment comment, User authorizedUser) {
        PermissionHelper.isNotSameUser(comment.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if (usersWhoLiked.contains(authorizedUser)) {
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }

        usersWhoDisliked.remove(authorizedUser);
        usersWhoLiked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }

    /*Ilia*/
    @Override
    public Comment dislikeComment(Comment comment, User authorizedUser) {
        PermissionHelper.isNotSameUser(comment.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if (usersWhoLiked.contains(authorizedUser)) {
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }

        usersWhoLiked.remove(authorizedUser);
        usersWhoDisliked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }
}