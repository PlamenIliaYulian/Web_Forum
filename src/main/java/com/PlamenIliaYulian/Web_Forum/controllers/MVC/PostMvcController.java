package com.PlamenIliaYulian.Web_Forum.controllers.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")

public class PostMvcController {
    /*Plamka*/
    /*User must  be already logged*/
    @GetMapping
    public String showAllPosts() {
        return null;
    }

    /*Iliika*/
    /*This is the page where the user will have to insert all the new post's details*/
    @GetMapping("/new")
    public String showCreateNewPostForm() {
        return null;
    }

    /*Yuli*/
    /*This will be the endpoint which will be taking care of creating a new post.*/
    @PostMapping("/new")
    public String createPost() {
        return null;
    }

    /*Plamka*/
    /*Post + all related comments are rendered.
     * We should also be able to like / dislike the post from this page.*/
    @GetMapping("/{id}")
    public String showSinglePost() {
        return null;
    }

    /*Iliika*/
    /*This is the page where the post's creator will be able to edit the post:
     * - can add tag;*/
    @GetMapping("/{id}/edit")
    public String showEditPostForm() {
        return null;
    }

    /*Yuli*/
    /*This is the endpoint / page which will take care of editing the post.
     * NOTE - once the edit is successful redirect the user to the original post's page*/
    @PostMapping("/{id}/edit")
    public String editPost() {
        return null;
    }

    /*Plamka*/
    /*This is the endpoint / page which allows the user to write a new comment*/
    @GetMapping("/{id}/comment")
    public String showAddCommentForm() {
        return null;
    }

    /*Iliika*/
    /*This is the endpoint which will be taking care of adding the comment to the respective post*/
    @PostMapping("/{id}/comment")
    public String addCommentToPost() {
        return null;
    }

    /*Yuli*/
    /*This is the endpoint which will be taking care of deleting a post.
     * NOTE - make sure the redirect the user to >>> /posts */
    @GetMapping("/{id}/delete")
    public String deletePost() {
        return null;
    }

}
