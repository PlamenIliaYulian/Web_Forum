package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface CommentRepository {

    Comment getCommentById(int id);

    Comment getCommentByContent(String content);

    Comment createComment(Comment comment);

    Comment updateComment(Comment comment);
    Comment softDeleteComment(Comment comment);

    List<Comment> getAllComments(CommentFilterOptions commentFilterOptions);
    List<Comment> getCommentsByCreator(User user);
}
