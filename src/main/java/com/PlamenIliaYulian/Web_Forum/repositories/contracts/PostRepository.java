package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Post;

import java.util.List;

public interface PostRepository {

    Post createPost(Post post);

    Post updatePost(Post post);

    /*TODO implement Post FilterOptions*/
    List<Post> getAllPosts();

    Post getPostByTitle(String title);

    Post getPostById(int id);


}
