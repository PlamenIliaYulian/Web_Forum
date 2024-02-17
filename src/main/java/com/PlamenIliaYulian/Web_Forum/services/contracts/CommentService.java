package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface CommentService {

    Comment getCommentById(int id);

    Comment getCommentByContent(String content);

    Comment createComment(Comment comment, User commentCreator);

    Comment updateComment(Comment comment, User authorizedUser);

    void deleteComment(Comment comment);

    List<Comment> getAllComments(User userExecutingTheRequest, CommentFilterOptions commentFilterOptions);

    Comment likeComment(Comment comment, User authorizedUser);

    Comment dislikeComment(Comment comment, User authorizedUser);
    List<Comment> getCommentsByCreator(User user);
}
