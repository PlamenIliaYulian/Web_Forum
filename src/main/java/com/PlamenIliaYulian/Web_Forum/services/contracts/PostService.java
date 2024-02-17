package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;

import java.util.List;

public interface PostService {
    Post createPost(Post post, User authorizedUser);

    void deletePost(Post post, User authorizedUser);

    Post updatePost(Post post, User authorizedUser);

    List<Post> getAllPosts(User userExecutingTheRequest, PostFilterOptions postFilterOptions);

    Post getPostByTitle(String title);

    Post getPostById(int id);

    Post likePost(Post post, User authorizedUser);

    Post dislikePost(Post post, User authorizedUser);

    Post addTagToPost(Post post, Tag tag, User authorizedUser);

    Post removeTagFromPost(Post post, Tag tag, User authorizedUser);

    Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments);

    List<Comment> getAllCommentsRelatedToPost(Post postWithComments);

    Post removeCommentFromPost(Post postToComment, Comment comment, User authorizedUser);
    Long getAllPostsCount();
    List<Post> getMostCommentedPosts();
    List<Post> getMostLikedPosts();
    List<Post> getMostRecentlyCreatedPosts();
    List<Post> getPostsByCreator(User user);

}
