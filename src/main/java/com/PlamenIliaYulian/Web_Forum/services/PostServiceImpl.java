package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService{

    private final TagService tagService;
    private final UserService userService;
    private final CommentService commentService;
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(TagService tagService, UserService userService, CommentService commentService, PostRepository postRepository) {
        this.tagService = tagService;
        this.userService = userService;
        this.commentService = commentService;
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Post post, User authorizedUser) {
        return null;
    }

    @Override
    public void deletePost(Post post, User authorizedUser) {

    }

    @Override
    public Post updatePost(Post post, User authorizedUser) {
        return null;
    }

    @Override
    public List<Post> getAllPosts() {
        return null;
    }

    @Override
    public Post getPostByTitle(String title) {
        return null;
    }

    @Override
    public Post getPostById(int id) {
        return null;
    }

    @Override
    public Post likePost(Post post, User authorizedUser) {
        return null;
    }

    @Override
    public Post dislikePost(Post post, User authorizedUser) {
        return null;
    }

    @Override
    public Post addTagToPost(Post post, Tag tag) {
        return null;
    }

    @Override
    public Post removeTagToPost(Post post, Tag tag) {
        return null;
    }

    @Override
    public Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments) {
        commentService.createComment(commentToBeAdded, userWhoComments);
        Set<Comment> comments = postToComment.getRelatedComments();
        comments.add(commentToBeAdded);
        //row 87 is unnecessary
        postToComment.setRelatedComments(comments);
        postRepository.addCommentToPost(postToComment);
        return null;

    }
}
