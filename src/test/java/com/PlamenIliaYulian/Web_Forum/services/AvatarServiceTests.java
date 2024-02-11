package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.AvatarRepository;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    public void getDefaultAvatar_Should_CallAvatarRepositoryAndPass(){
        Mockito.when(avatarRepository.getDefaultAvatar())
                .thenReturn(new Avatar());

        avatarService.getDefaultAvatar();

        Mockito.verify(avatarRepository, Mockito.times(1))
                .getDefaultAvatar();
    }
}
