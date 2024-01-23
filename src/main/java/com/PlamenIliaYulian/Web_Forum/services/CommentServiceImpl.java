package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public class CommentServiceImpl implements CommentService{
    @Override
    public Comment getCommentById(int id) {
        return null;
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
        return null;
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
        return null;
    }

    @Override
    public Comment dislikeComment(Comment comment, User authorizedUser) {
        return null;
    }
}
