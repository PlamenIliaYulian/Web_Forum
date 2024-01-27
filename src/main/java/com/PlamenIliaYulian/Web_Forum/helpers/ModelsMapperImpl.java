package com.PlamenIliaYulian.Web_Forum.helpers;

import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.PostService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelsMapperImpl implements ModelsMapper {

    private final TagService tagService;

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public ModelsMapperImpl(TagService tagService, CommentService commentService, PostService postService, UserService userService) {
        this.tagService = tagService;
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }


    @Override
    public Post postFromDto(PostDto postDto, Post postWeGotFromTitle) {
        postWeGotFromTitle.setContent(postDto.getContent());
        postWeGotFromTitle.setTitle(postDto.getTitle());
        return postWeGotFromTitle;
    }

    @Override
    public User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, String username) {
        User user = userService.getUserByUsername(username);
        user.setBlocked(userAdministrativeDto.isBlocked());
        user.setDeleted(userAdministrativeDto.isDeleted());
        user.setRoles(userAdministrativeDto.getRoles());
        return user;
    }

    @Override
    public User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, String username) {
        User user = userService.getUserByUsername(username);
        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setEmail(userDtoUpdate.getEmail());
        user.setPassword(userDtoUpdate.getPassword());
        return user;
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
        comment.setContent(commentDto.getComment());
        return comment;
    }

    @Override
    public Comment commentFromDto(CommentDto commentDto, String content) {
        Comment comment = commentService.getCommentByContent(content);
        comment.setContent(commentDto.getComment());
        return comment;
    }

    /*TODO add # to tags.*/
    @Override
    public Tag tagFromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getTag());
        return tag;
    }
    @Override
    public Tag tagFromDto(TagDto tagDto, String name) {
        Tag tag = tagService.getTagByName(name);
        tag.setName(tagDto.getTag());
        return tag;
    }

    @Override
    public User userFromDtoUpdate(UserDto userDto, int id) {
        User user = userService.getUserById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
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
