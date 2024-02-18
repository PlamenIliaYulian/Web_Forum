package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.AvatarService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @Override
    public Avatar createAvatar(Avatar avatar) {
        return avatarRepository.createAvatar(avatar);
    }

    @Override
    public Avatar getDefaultAvatar() {
        return avatarRepository.getDefaultAvatar();
    }

    @Override
    public String uploadPictureToCloudinary(MultipartFile multipartFile) {
        return avatarRepository.uploadPictureToCloudinary(multipartFile);
    }
}
