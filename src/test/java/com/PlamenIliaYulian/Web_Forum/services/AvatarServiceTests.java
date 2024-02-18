package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTests {


    @Mock
    AvatarRepository avatarRepository;

    @InjectMocks
    AvatarServiceImpl avatarService;

    @Test
    public void createAvatar_Should_CallRepository() {
        Avatar avatar = TestHelpers.createAvatar();

        avatarService.createAvatar(avatar);

        Mockito.verify(avatarRepository, Mockito.times(1))
                .createAvatar(avatar);

    }

    @Test
    public void getDefaultAvatar_Should_CallAvatarRepositoryAndPass(){
        Mockito.when(avatarRepository.getDefaultAvatar())
                .thenReturn(new Avatar());

        avatarService.getDefaultAvatar();

        Mockito.verify(avatarRepository, Mockito.times(1))
                .getDefaultAvatar();
    }

    @Test
    public void uploadPictureToCloudinary__Should_CallAvatarRepositoryAndPass() {
        MultipartFile multipartFile = TestHelpers.createMultipartFile();

        avatarService.uploadPictureToCloudinary(multipartFile);

        Mockito.verify(avatarRepository, Mockito.times(1))
                .uploadPictureToCloudinary(multipartFile);
    }

}
