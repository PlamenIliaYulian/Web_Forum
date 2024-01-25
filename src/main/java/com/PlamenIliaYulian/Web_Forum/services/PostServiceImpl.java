package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidOperationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidUserInputException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.PermissionHelper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.PostRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    public static final String UNAUTHORIZED_OPERATION = "Unauthorized operation.";
    public static final String NO_TAG_RELATED_TO_POST = "The post does not contain such a tag.";
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
    public Post createPost(Post post, User authenticatedUser) {
        PermissionHelper.isBlocked(authenticatedUser, UNAUTHORIZED_OPERATION);
        post.setCreatedOn(LocalDateTime.now());
        post.setCreatedBy(authenticatedUser);
        return postRepository.createPost(post);
    }

    @Override
    public void deletePost(Post post, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);
        post.setDeleted(true);
        postRepository.updatePost(post);
    }

    /*Ilia*/
    @Override
    public Post updatePost(Post post, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);
        return postRepository.updatePost(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return null;
    }

    @Override
    public Post getPostByTitle(String title) {
        return postRepository.getPostByTitle(title);
    }

    /*Ilia*/
    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public Post likePost(Post post, User authenticatedUser) {
        PermissionHelper.isNotSameUser(post.getCreatedBy(), authenticatedUser, UNAUTHORIZED_OPERATION);
        Set<User> usersWhoDislikedThePost = post.getUsersWhoDislikedPost();
        Set<User> usersWhoLikedThePost = post.getUsersWhoLikedPost();

        if (usersWhoLikedThePost.contains(authenticatedUser)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
        usersWhoDislikedThePost.remove(authenticatedUser);
        usersWhoLikedThePost.add(authenticatedUser);

        return postRepository.updatePost(post);
    }

    @Override
    public Post dislikePost(Post post, User authorizedUser) {
        return null;
    }

    /*Ilia*/
    @Override
    public Post addTagToPost(Post post, Tag tag, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);
        try {
            tagService.getTagByName(tag.getName());
        } catch (EntityNotFoundException e) {
            tagService.createTag(tag);
        }
        Set<Tag> postTags = post.getTags();
        postTags.add(tag);
        return postRepository.updatePost(post);
    }

    @Override
    public Post removeTagFromPost(Post post, Tag tag, User authenticatedUser) {
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authenticatedUser, UNAUTHORIZED_OPERATION);

        Set<Tag> tagsOfThePost = post.getTags();
        if (!tagsOfThePost.contains(tag)) {
            throw new InvalidUserInputException(NO_TAG_RELATED_TO_POST);
        }
        tagsOfThePost.remove(tag);
        return postRepository.updatePost(post);
    }

    /*TODO Plamkata*/
    @Override
    public Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments) {
        /*check is authorized or blocked*/

        commentService.createComment(commentToBeAdded);
        Set<Comment> comments = postToComment.getRelatedComments();
        comments.add(commentToBeAdded);
        postToComment.setRelatedComments(comments);
        return postRepository.updatePost(postToComment);

    }

    @Override
    public Post removeCommentFromPost(Post postToRemoveCommentFrom, int commentId, User authorizedUser) {
        Comment commentToBeRemoved = commentService.getCommentById(commentId);
        User commentCreator = commentToBeRemoved.getCreatedBy();
        PermissionHelper.isAdminOrSameUser(commentCreator, authorizedUser, UNAUTHORIZED_OPERATION);

        Set<Comment> comments = postToRemoveCommentFrom.getRelatedComments();
        if (!comments.contains(commentToBeRemoved)) {
            throw new InvalidOperationException("The comment does not belong to this post.");
        }
        comments.remove(commentToBeRemoved);
        commentService.deleteComment(commentToBeRemoved);
        return postRepository.updatePost(postToRemoveCommentFrom);
    }

    @Override
    public List<Comment> getAllCommentsRelatedToPost(Post postWithComments) {
        return postWithComments.getRelatedComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentId))
                .collect(Collectors.toList());
    }
}
