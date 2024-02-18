package com.PlamenIliaYulian.Web_Forum.repositories.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AvatarRepository {

    Avatar getAvatarById(int id);

    Avatar createAvatar(Avatar avatar);

    Avatar getDefaultAvatar();

    Avatar getAvatarByName(String name);
    String uploadPictureToCloudinary(MultipartFile multipartFile);
}
