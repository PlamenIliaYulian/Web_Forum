package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
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
        post.setCreatedOn(LocalDateTime.now());
        post.setCreatedBy(authorizedUser);
        return postRepository.createPost(post);
    }

    @Override
    public void deletePost(Post post, User authorizedUser) {
        checkModifyPermission(post.getPostId(), authorizedUser);
        postRepository.deletePost(post);
    }

    /*Ilia*/
    @Override
    public Post updatePost(Post post, User authorizedUser) {
        checkIfUserIsBlocked(authorizedUser);
        checkIfUserIsAuthorized(post, authorizedUser);
        return postRepository.updatePost(post);
    }

    private void checkIfUserIsBlocked(User authorizedUser) {
        if (authorizedUser.isBlocked()) {
            throw new UnauthorizedOperationException("You are blocked and cannot create/modify/delete posts.");
        }
    }

    /*TODO Is this is the best way to check if someone is Admin? */
    private void checkIfUserIsAuthorized(Post post, User authorizedUser) {
        if (!authorizedUser.equals(post.getCreatedBy()) &&
                !authorizedUser.getRoles().contains(new Role(1, "ROLE_ADMIN"))) {
            throw new UnauthorizedOperationException("You have to be admin or the post creator to modify/delete the post.");
        }
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
    public Post likePost(Post post, User authorizedUser) {
        Post postToLike = postRepository.getPostByTitle(post.getTitle());
        /* Once we have the post:
            - we need to check if the authorizedUser has already liked this post;
            - we need to check if the authorizedUser has already disliked this post;

         Based on the info we get, we need to perform certain actions.*/
        return null;
    }

    @Override
    public Post dislikePost(Post post, User authorizedUser) {
        return null;
    }

    /*Ilia*/
    @Override
    public Post addTagToPost(Post post, Tag tag, User authorizedUser) {
        checkIfUserIsBlocked(authorizedUser);
        checkIfUserIsAuthorized(post,authorizedUser);
        if (tagService.getTagByName(tag.getName()) == null) {
            tagService.createTag(tag);
        }
        Set<Tag> postTags = post.getTags();
        postTags.add(tag);
        return postRepository.updatePost(post);
    }

    @Override
    public Post removeTagFromPost(Post post, Tag tag, User authorizedUser) {
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

    @Override
    public Post removeCommentFromPost(Post postToComment, int commentId, User authorizedUser) {
        Comment comment = commentService.getCommentById(commentId);
        return null;
    }

    /*Ilia - we are not calling the repository.*/
    @Override
    public List<Comment> getAllCommentsRelatedToPost(Post postWithComments) {
        return postWithComments.getRelatedComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentId))
                .collect(Collectors.toList());
    }


    private void checkModifyPermission(int postId, User user) {
        Post post = postRepository.getPostById(postId);

        if (!(isAdmin(user) || post.getCreatedBy().equals(user))) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_OPERATION);
        }
    }

    private boolean isAdmin(User userIsAuthorized) {
        List<Role> rolesOfAuthorizedUser = userIsAuthorized.getRoles().stream().toList();
        for (Role currentRoleToBeChecked : rolesOfAuthorizedUser) {
            if (currentRoleToBeChecked.getName().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

}
