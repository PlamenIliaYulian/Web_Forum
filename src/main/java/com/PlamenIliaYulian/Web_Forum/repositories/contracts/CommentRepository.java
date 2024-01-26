package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;

import java.util.List;

public interface CommentRepository {

    Comment getCommentById(int id);

    Comment getCommentByContent(String content);

    Comment createComment(Comment comment);

    Comment updateComment(Comment comment);

    /*TODO implement Comment FilterOptions*/
    List<Comment> getAllComments(CommentFilterOptions commentFilterOptions);

}
