package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidOperationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidUserInputException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.services.helpers.PermissionHelper;
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
    static final String MULTIPLE_LIKE_ERROR = "You have already liked this comment";
    static final String MULTIPLE_DISLIKE_ERROR = "You have already disliked this comment";
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
        /*TODO implement unique name verification*/
        post.setCreatedOn(LocalDateTime.now());
        post.setCreatedBy(authenticatedUser);
        return postRepository.createPost(post);
    }

    @Override
    public void deletePost(Post post, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);

        post.getRelatedComments().forEach(commentService::deleteComment);
        post.setDeleted(true);
        postRepository.softDeletePost(post);
    }

    /*Ilia*/
    @Override
    public Post updatePost(Post post, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);

        return postRepository.updatePost(post);
    }

    /*TODO for Yuli - Ask Iliikata and Plamkata why we pass >>> User userExecutingTheRequest.
    *  We do not use it anywhere.*/
    @Override
    public List<Post> getAllPosts(User userExecutingTheRequest, PostFilterOptions postFilterOptions) {
        return postRepository.getAllPosts(postFilterOptions);
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
            throw new UnauthorizedOperationException(MULTIPLE_LIKE_ERROR);
        }
        usersWhoDislikedThePost.remove(authenticatedUser);
        usersWhoLikedThePost.add(authenticatedUser);

        return postRepository.updatePost(post);
    }

    /*why do we make the first authorization*/
    @Override
    public Post dislikePost(Post post, User authorizedUser) {
        PermissionHelper.isNotSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);
        Set<User> usersWhoDislikedThePost = post.getUsersWhoDislikedPost();
        Set<User> usersWhoLikedThePost = post.getUsersWhoLikedPost();

        if(usersWhoDislikedThePost.contains(authorizedUser)){
            throw new UnauthorizedOperationException(MULTIPLE_DISLIKE_ERROR);
        }
        usersWhoLikedThePost.remove(authorizedUser);
        usersWhoDislikedThePost.add(authorizedUser);

        return postRepository.updatePost(post);
    }

    /*Ilia*/
    @Override
    public Post addTagToPost(Post post, Tag tag, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authorizedUser, UNAUTHORIZED_OPERATION);

        tag = tagService.createTag(tag, authorizedUser);
        Set<Tag> postTags = post.getTags();
        postTags.add(tag);
        return postRepository.updatePost(post);
    }

    @Override
    public Post removeTagFromPost(Post post, Tag tag, User authenticatedUser) {
        PermissionHelper.isBlocked(authenticatedUser, UNAUTHORIZED_OPERATION);
        PermissionHelper.isAdminOrSameUser(post.getCreatedBy(), authenticatedUser, UNAUTHORIZED_OPERATION);

        Set<Tag> tagsOfThePost = post.getTags();
        if (!tagsOfThePost.contains(tag)) {
            throw new InvalidUserInputException(NO_TAG_RELATED_TO_POST);
        }
        tagsOfThePost.remove(tag);
        return postRepository.updatePost(post);
    }

    /*Plamkata*/
    @Override
    public Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments) {
        PermissionHelper.isBlocked(userWhoComments,UNAUTHORIZED_OPERATION);
        commentService.createComment(commentToBeAdded, userWhoComments);
        Set<Comment> comments = postToComment.getRelatedComments();
        comments.add(commentToBeAdded);
        postToComment.setRelatedComments(comments);
        return postRepository.updatePost(postToComment);
    }

    @Override
    public Post removeCommentFromPost(Post postToRemoveCommentFrom, String comment, User authorizedUser) {
        PermissionHelper.isBlocked(authorizedUser,UNAUTHORIZED_OPERATION);
        Comment commentToBeRemoved = commentService.getCommentByContent(comment);
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
    public Long getAllPostsCount() {
        return postRepository.getAllPostsCount();
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        return postRepository.getMostCommentedPosts();
    }

    @Override
    public List<Post> getMostLikedPosts() {
        return postRepository.getMostLikedPosts();
    }

    @Override
    public List<Post> getMostRecentlyCreatedPosts() {
        return postRepository.getMostRecentlyCreatedPosts();
    }

    /*Ilia*/
    @Override
    public List<Comment> getAllCommentsRelatedToPost(Post postWithComments) {
        return postWithComments.getRelatedComments()
                .stream()
                .filter(comment -> !comment.isDeleted())
                .sorted(Comparator.comparing(Comment::getCommentId))
                .collect(Collectors.toList());
    }
}
