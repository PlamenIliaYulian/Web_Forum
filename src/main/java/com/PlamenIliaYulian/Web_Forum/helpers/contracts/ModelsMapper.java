package com.PlamenIliaYulian.Web_Forum.helpers.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;

public interface ModelsMapper {
    Post postFromDto(PostDto postDto);

    Comment commentFromDto(CommentDto commentDto);

    Tag tagFromDto(TagDto tagDto);
    Tag tagFromDto(TagDto tagDto, String name);
    User userFromDto(UserDto userDto);
    User userFromDto(UserDto userDto, int id);
    User userFromDto(UserDto userDto, String username);

    Post postFromDto(PostDto postDto, Post postWeGotFromTitle);

    User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, String username);

}
