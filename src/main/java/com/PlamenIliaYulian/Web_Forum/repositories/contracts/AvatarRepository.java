package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;

import java.util.Optional;

public interface AvatarRepository {

    Avatar getAvatarById (int id);

    Avatar createAvatar(Avatar avatar);
    Avatar getDefaultAvatar();

    Optional<Avatar> getAvatarByName(String name);
}
