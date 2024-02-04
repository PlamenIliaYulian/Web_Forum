package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;

public interface AvatarService {
    Avatar createAvatar(Avatar avatar);
    Avatar getDefaultAvatar();
}
