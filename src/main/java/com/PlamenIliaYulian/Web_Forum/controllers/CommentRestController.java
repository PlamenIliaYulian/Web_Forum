package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.models.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")

public class CommentRestController {

    /*TODO implement Comment FilterOptions*/
    @GetMapping()
    public List<Comment> getAllComments() {
    return null;
    }
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id) {
        return null;
    }

    @PutMapping("/{id}/like")
    public Comment likeComment(@PathVariable String id) {
        return null;
    }

    /*Ilia*/
    @PutMapping("/{id}/dislike")
    public Comment dislikeComment(@PathVariable String id){
        return null;
    }

}
