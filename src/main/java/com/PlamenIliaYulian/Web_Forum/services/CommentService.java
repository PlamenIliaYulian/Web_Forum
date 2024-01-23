package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface CommentService {

    /*TODO Plamen*/
    Comment getCommentById(int id);
    /*TODO Iliya*/
    Comment getCommentByContent(String content);
    /*TODO July*/
    Comment createComment(Comment comment, User authorizedUser);
    /*TODO Plamen*/
    Comment updateComment(Comment comment, User authorizedUser);
    /*TODO Iliya*/
    void deleteCommentFromPost(Comment comment, User authorizedUser);
    /*TODO July*/
    /*TODO implement Comment FilterOptions*/
    List<Comment> getAllComments();
    /*TODO Plamen*/
    Comment likeComment(Comment comment, User authorizedUser);
    /*TODO Iliya*/
    Comment dislikeComment(Comment comment, User authorizedUser);
}
