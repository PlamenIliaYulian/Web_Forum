package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AvatarService {
    Avatar createAvatar(Avatar avatar);

    Avatar getDefaultAvatar();

    String uploadPictureToCloudinary(MultipartFile multipartFile);
}
