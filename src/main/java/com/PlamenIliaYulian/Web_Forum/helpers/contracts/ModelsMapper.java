package com.PlamenIliaYulian.Web_Forum.helpers.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;

public interface ModelsMapper {
    Post postFromDto(PostDto postDto);

    Comment commentFromDto(CommentDto commentDto);
    Comment commentFromDto(CommentDto commentDto, String content);

    Tag tagFromDto(TagDto tagDto);
    Tag tagFromDto(TagDto tagDto, String name);
    User userFromDto(UserDto userDto);
    User userFromDtoUpdate(UserDto userDto, int id);

    Post postFromDto(PostDto postDto, Post postWeGotFromTitle);

    User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, String username);

    User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, String username);
}
