package com.PlamenIliaYulian.Web_Forum.helpers;

import com.PlamenIliaYulian.Web_Forum.models.*;

public interface ModelsMapper {
    Post postFromDto(PostDto postDto);

    Comment commentFromDto(CommentDto commentDto);

    Tag tagFromDto(TagDto tagDto);

    User userFromDto(UserDto userDto);

    Post postFromDto(PostDto postDto, Post postWeGotFromTitle);

}
