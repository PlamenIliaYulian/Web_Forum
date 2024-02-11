package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.services.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";
    public static final String MULTIPLE_LIKE_ERROR = "You have already liked this comment";
    public static final String MULTIPLE_DISLIKE_ERROR = "You have already disliked this comment";
    public static final String YOU_ARE_THE_CREATOR_OF_THIS_COMMENT = "You are the creator of this comment";


    private final CommentRepository commentRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {

        this.commentRepository = commentRepository;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }


    @Override
    public Comment getCommentByContent(String content) {
        return commentRepository.getCommentByContent(content);
    }

    /*TODO PLAMEN */
    @Override
    public Comment createComment(Comment comment, User commentCreator) {
        PermissionHelper.isBlocked(commentCreator, UNAUTHORIZED_OPERATION);
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


    @Override
    public void deleteComment(Comment comment) {
        PermissionHelper.isBlocked(comment.getCreatedBy(), UNAUTHORIZED_OPERATION);
        comment.setDeleted(true);
        commentRepository.softDeleteComment(comment);
    }

    /*TODO ILIQ */
    @Override
    public List<Comment> getAllComments(User userExecutingTheRequest, CommentFilterOptions commentFilterOptions) {
        return commentRepository.getAllComments(commentFilterOptions);
    }

    /*TODO JULY:
    * I had to add >>> "userWhoWillLikeComment.setUserId(777);" to the test which Plamkata had already written
    * so  that the system would count it for the coverage.
    * The test was already catching the >>> "UnauthorizedOperationException" exception, but this exception was coming
    * from line 82, where we check if the user that created the comment is the one trying to like the comment.*/
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
        PermissionHelper.isNotSameUser(comment.getCreatedBy(), authorizedUser,YOU_ARE_THE_CREATOR_OF_THIS_COMMENT);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if(usersWhoDisliked.contains(authorizedUser)){
            throw new UnauthorizedOperationException(MULTIPLE_DISLIKE_ERROR);
        }

        usersWhoLiked.remove(authorizedUser);
        usersWhoDisliked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }

    /*TODO PLAMEn */
    @Override
    public List<Comment> getCommentsByCreator(User user) {
        return commentRepository.getCommentsByCreator(user);
    }
}