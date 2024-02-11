package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.AvatarService;
import org.springframework.stereotype.Service;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;

    /*TODO PLAMEN*/
    public AvatarServiceImpl(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    /*TODO ILIQ*/
    @Override
    public Avatar createAvatar(Avatar avatar) {
        return avatarRepository.createAvatar(avatar);
    }

    /*TODO July - done*/
    @Override
    public Avatar getDefaultAvatar() {
        return avatarRepository.getDefaultAvatar();
    }


}
