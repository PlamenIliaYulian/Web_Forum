package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;

import java.util.List;

public interface PostService {
    /*TODO 📌 July 📌- DONE - last updated 25.01.2024*/
    Post createPost(Post post, User authorizedUser);

    /*TODO Plamen*/
    void deletePost(Post post, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    Post updatePost(Post post, User authorizedUser);

    /*TODO 📌 July 📌- DONE - last updated 26.01.2024*/
    /*TODO implement Post FilterOptions*/
    List<Post> getAllPosts(User userExecutingTheRequest, PostFilterOptions postFilterOptions);

    /*TODO Plamen*/
    Post getPostByTitle(String title);

    /*TODO ✔ Iliya ✔*/
    Post getPostById(int id);

    /*TODO 📌 July 📌- DONE - last updated 25.01.2024*/
    Post likePost(Post post, User authorizedUser);

    /*TODO Plamen*/
    Post dislikePost(Post post, User authorizedUser);

    /*TODO ✔ Iliya ✔*/
    Post addTagToPost(Post post, Tag tag, User authorizedUser);

    /*TODO 📌 July 📌- DONE - last updated 25.01.2024*/
    Post removeTagFromPost(Post post, Tag tag, User authorizedUser);

    /*TODO Plamen*/
    Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments);

    /*TODO ✔ Iliya ✔*/
    List<Comment> getAllCommentsRelatedToPost(Post postWithComments);

    /*TODO 📌 July 📌- DONE - last updated 25.01.2025*/
    Post removeCommentFromPost(Post postToComment, int commentId, User authorizedUser);
}
