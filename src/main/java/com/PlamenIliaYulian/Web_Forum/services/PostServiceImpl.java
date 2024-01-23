package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public class PostServiceImpl implements PostService{
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
}
