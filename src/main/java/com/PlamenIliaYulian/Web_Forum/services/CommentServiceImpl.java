package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Role;
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

    @Override
    public Comment getCommentByContent(String content) {
        return null;
    }

    @Override
    public Comment createComment(Comment comment, User authorizedUser) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment, User authorizedUser) {
        checkModifyPermission(comment.getCommentId(), authorizedUser);
        return commentRepository.updateComment(comment);
    }

    @Override
    public void deleteCommentFromPost(Comment comment, User authorizedUser) {

    }

    @Override
    public List<Comment> getAllComments() {
        return null;
    }

    @Override
    public Comment likeComment(Comment comment, User authorizedUser) {
        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();

        if(usersWhoLiked.contains(authorizedUser)){
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }

        usersWhoDisliked.remove(authorizedUser);
        usersWhoLiked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }

    @Override
    public Comment dislikeComment(Comment comment, User authorizedUser) {
        return null;
    }

    private void checkModifyPermission(int commentId, User user) {
        Comment comment = commentRepository.getCommentById(commentId);

        if (!(isAdmin(user) || comment.getCreatedBy().equals(user))) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
    }

    private boolean isAdmin(User userIsAuthorized) {
        List<Role> rolesOfAuthorizedUser = userIsAuthorized.getRoles().stream().toList();
        for (Role currentRoleToBeChecked : rolesOfAuthorizedUser) {
            if (currentRoleToBeChecked.getName().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
