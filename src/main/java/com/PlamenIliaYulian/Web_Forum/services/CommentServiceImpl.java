package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;


    @Autowired
    public CommentServiceImpl(UserService userService, CommentRepository commentRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

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
