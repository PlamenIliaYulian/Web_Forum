package com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;

public interface ModelsMapper {
    Post postFromDto(PostDto postDto);

    Comment commentFromDto(CommentDto commentDto);
    Comment commentFromDto(CommentDto commentDto, String content);
    Comment commentFromDto(CommentDto commentDto, int id);

    Tag tagFromDto(TagDto tagDto);
    Tag tagFromDto(TagDto tagDto, String name);
    Tag tagFromDto(TagDto tagDto, int id);
    User userFromDto(UserDto userDto);
    User userFromDtoUpdate(UserDto userDto, int id);

    Post postFromDto(PostDto postDto, Post postWeGotFromTitle);

    User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, String username);
    User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, int id);
    User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, String username);
    User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, int id);


    UserFilterOptions userFilterOptionsFromDto(UserFilterOptionsDto dto);

    PostFilterOptions postFilterOptionsFromDto(PostFilterOptionsDto dto);


}
