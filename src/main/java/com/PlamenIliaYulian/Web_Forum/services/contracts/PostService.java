package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface PostService {
    /*TODO July - DONE*/
    Post createPost(Post post, User authorizedUser);

    /*TODO Plamen*/
    void deletePost(Post post, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    Post updatePost(Post post, User authorizedUser);

    /*TODO July*/
    /*TODO implement Post FilterOptions*/
    List<Post> getAllPosts();

    /*TODO Plamen*/
    Post getPostByTitle(String title);

    /*TODO ✔ Iliya ✔*/
    Post getPostById(int id);

    /*TODO July*/
    Post likePost(Post post, User authorizedUser);

    /*TODO Plamen*/
    Post dislikePost(Post post, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    Post addTagToPost(Post post, Tag tag, User authorizedUser);

    /*TODO July*/
    Post removeTagFromPost(Post post, Tag tag, User authorizedUser);

    /*TODO Plamen*/
    Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments);
    /*TODO ✔ Iliya ✔*/
    List<Comment> getAllCommentsRelatedToPost(Post postWithComments);
    /*TODO July*/
    Post removeCommentFromPost(Post postToComment, int commentId, User authorizedUser);
}
