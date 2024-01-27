package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.PostFilterOptions;

import java.util.List;

public interface PostRepository {

    Post createPost(Post post);

    Post updatePost(Post post);

    Post softDeletePost(Post post);

    List<Post> getAllPosts(PostFilterOptions postFilterOptions);

    Post getPostByTitle(String title);

    Post getPostById(int id);


}
