package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
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

    /*TODO - Yuli - ask Plamkata and Iliikata how they`d like us to implement this method. At the moment it is not */
    @Override
    public Comment createComment(Post postToAddCommentTo, Comment comment, User authorizedUser) {
        return null;
    }

    @Override
    public Comment updateComment(Comment comment, User authorizedUser) {
        checkModifyPermission(comment.getCommentId(), authorizedUser);
        return commentRepository.updateComment(comment);
    }

    /*Ilia We are not using this method. Have to delete it. We are using this in Post layers.*/
    @Override
    public void deleteCommentFromPost(Comment comment, User authorizedUser) {
        checkIfUserIsAuthorized(comment, authorizedUser);

    }
    /*TODO Is this is the best way to check if someone is Admin? */
    private void checkIfUserIsAuthorized(Comment comment, User authorizedUser) {
        if (!authorizedUser.equals(comment.getCreatedBy()) &&
                !authorizedUser.getRoles().contains(new Role(1, "ROLE_ADMIN"))) {
            throw new UnauthorizedOperationException("You have to be admin or the comment creator to modify/delete the comment.");
        }
    }

    @Override
    public List<Comment> getAllComments() {
        return null;
    }

    @Override
    public Comment likeComment(Comment comment, User authorizedUser) {

        if(comment.getCreatedBy().equals(authorizedUser)){
            throw new UnauthorizedOperationException(YOU_ARE_THE_CREATOR_OF_THIS_COMMENT);
        }

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
        checkThatTheUserIsNotTheCreator(comment, authorizedUser);

        Set<User> usersWhoLiked = comment.getUsersWhoLikedComment();
        Set<User> usersWhoDisliked = comment.getUsersWhoDislikedComment();
        usersWhoLiked.remove(authorizedUser);
        usersWhoDisliked.add(authorizedUser);

        return commentRepository.updateComment(comment);
    }

    private void checkThatTheUserIsNotTheCreator(Comment comment, User authorizedUser) {
        if (comment.getCreatedBy().equals(authorizedUser)) {
           throw new  UnauthorizedOperationException("As the comment creator, you cannot react to your comments.");
        }
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
