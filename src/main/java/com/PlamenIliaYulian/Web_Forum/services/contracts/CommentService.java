package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface CommentService {

    /*Plamen*/
    Comment getCommentById(int id);

    /*✔ Iliya ✔*/
    Comment getCommentByContent(String content);

    /*📌 July 📌 - DONE - last updated 26.01.2024*/
    Comment createComment(Comment comment, User commentCreator);

    /*Plamen*/
    Comment updateComment(Comment comment, User authorizedUser);

    /*✔ Iliya ✔*/
    void deleteComment(Comment comment);

    /*July - 📌 DONE 📌 - last updated 26.01.2024*/
    List<Comment> getAllComments(User userExecutingTheRequest, CommentFilterOptions commentFilterOptions);

    /*Plamen*/
    Comment likeComment(Comment comment, User authorizedUser);

    /*✔ Iliya ✔*/
    Comment dislikeComment(Comment comment, User authorizedUser);
    List<Comment> getCommentsByCreator(User user);
}
