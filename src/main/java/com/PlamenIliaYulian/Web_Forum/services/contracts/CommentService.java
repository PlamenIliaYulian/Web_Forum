package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface CommentService {

    /*TODO Plamen*/
    Comment getCommentById(int id);

    /*TODO ✔ Iliya ✔*/
    Comment getCommentByContent(String content);

    /*TODO 📌 July 📌 - DONE - last updated 26.01.2024*/
    Comment createComment(Comment comment, User commentCreator);

    /*TODO Plamen*/
    Comment updateComment(Comment comment, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    void deleteComment(Comment comment);

    /*TODO July - 📌 DONE 📌 - last updated 26.01.2024*/
    /*TODO implement Comment FilterOptions*/
    List<Comment> getAllComments(User userExecutingTheRequest, CommentFilterOptions commentFilterOptions);

    /*TODO Plamen*/
    Comment likeComment(Comment comment, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    Comment dislikeComment(int commentId, User authorizedUser);
}
