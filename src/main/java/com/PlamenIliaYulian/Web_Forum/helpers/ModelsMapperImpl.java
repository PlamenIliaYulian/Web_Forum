package com.PlamenIliaYulian.Web_Forum.helpers;

import com.PlamenIliaYulian.Web_Forum.models.*;
import org.springframework.stereotype.Component;

@Component
public class ModelsMapperImpl implements ModelsMapper {
    @Override
    public Post postFromDto(PostDto postDto, Post postWeGotFromTitle) {
        postWeGotFromTitle.setContent(postDto.getContent());
        postWeGotFromTitle.setTitle(postDto.getTitle());
        return postWeGotFromTitle;
    }

    @Override
    public Post postFromDto(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;

    }

    @Override
    public Comment commentFromDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        return comment;
    }

    @Override
    public Tag tagFromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        return tag;
    }

    @Override
    public User userFromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }


}
