package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /*TODO - Yuli*/
    @Override
    public Comment createComment(Comment comment) {
        return null;
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
    /*TODO Filter Options to be added*/
    @Override
    public List<Comment> getAllComments() {
        return null;
    }

    @Override
    public Comment likeComment(Comment comment, User authorizedUser) {
        PermissionHelper.isNotSameUser(comment.getCreatedBy(), authorizedUser,UNAUTHORIZED_OPERATION);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if(usersWhoLiked.contains(authorizedUser)){
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }

        usersWhoDisliked.remove(authorizedUser);
        usersWhoLiked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }
    /*Ilia*/
    @Override
    public Comment dislikeComment(Comment comment, User authorizedUser) {
        PermissionHelper.isNotSameUser(comment.getCreatedBy(), authorizedUser,UNAUTHORIZED_OPERATION);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if(usersWhoLiked.contains(authorizedUser)){
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }

        usersWhoLiked.remove(authorizedUser);
        usersWhoDisliked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }
}