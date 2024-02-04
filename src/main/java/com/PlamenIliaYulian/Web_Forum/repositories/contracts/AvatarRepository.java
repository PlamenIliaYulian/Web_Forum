package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;

public interface AvatarRepository {

    Avatar getAvatarById (int id);

    Avatar createAvatar(Avatar avatar);
    Avatar getDefaultAvatar();
}
