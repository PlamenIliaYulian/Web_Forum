package com.PlamenIliaYulian.Web_Forum.repositories;

import com.PlamenIliaYulian.Web_Forum.models.Comment;

import java.util.List;

public interface CommentRepository {



    Comment getCommentById(int id);
    Comment getCommentByContent(String content);
    Comment createComment(Comment comment);

    Comment updateComment(Comment comment);
    void deleteCommentFromPost(Comment comment);

    /*TODO implement Comment FilterOptions*/
    List<Comment> getAllComments();
    Comment likeComment(Comment comment);
    Comment dislikeComment(Comment comment);


}
