package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TagServiceTests {
    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    public void createTag_Should_Pass_When_TagCreatorIsNotBlocked() {
        Tag tagToCreate = TestHelpers.createMockTag();
        User nonBlockedTagCreator = TestHelpers.createMockNoAdminUser();
        Mockito.when(tagRepository.createTag(tagToCreate))
                .thenReturn(tagToCreate);
        Tag createdTag = tagService.createTag(tagToCreate, nonBlockedTagCreator);
        Mockito.verify(tagRepository, Mockito.times(1))
                .createTag(tagToCreate);
        Assertions.assertEquals(createdTag, tagToCreate);
    }

    @Test
    public void createTag_Should_Throw_When_TagCreatorIsBlocked() {
        Tag tagToCreate = TestHelpers.createMockTag();
        User blockedTagCreator = TestHelpers.createMockNoAdminUser();
        blockedTagCreator.setBlocked(true);
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> tagService.createTag(tagToCreate, blockedTagCreator));
    }

    @Test
    public void getAllTags_Should_Pass() {
        List<Tag> allTags = new ArrayList<>();
        allTags.add(TestHelpers.createMockTag());
        Mockito.when(tagRepository.getAllTags())
                .thenReturn(allTags);
        List<Tag> returnedTags = tagService.getAllTags();
        Mockito.verify(tagRepository, Mockito.times(1))
                .getAllTags();
        Assertions.assertEquals(allTags, returnedTags);
        Assertions.assertEquals(allTags.get(0), returnedTags.get(0));
    }
}
