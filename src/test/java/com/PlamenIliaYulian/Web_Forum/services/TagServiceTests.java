package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Mockito.when(tagRepository.getTagByName(tagToCreate.getTag()))
                .thenThrow(EntityNotFoundException.class);
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

        tagService.getAllTags();

        Mockito.verify(tagRepository, Mockito.times(1))
                .getAllTags();
    }

    @Test
    public void deleteTag_Should_Throw_When_UserIsNotAdmin(){
        Tag tag = TestHelpers.createMockTag();
        User nonAdminUser = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> tagService.deleteTag(tag, nonAdminUser));
    }

    @Test
    public void deleteTag_Should_Pass_When_UserIsAdmin(){
        Tag tagToDelete = TestHelpers.createMockTag();
        User adminUser = TestHelpers.createMockAdminUser();
        Post post = TestHelpers.createMockPost1();
        Set<Post> relatedPost = new HashSet<>();
        relatedPost.add(post);
        tagToDelete.setRelatedPosts(relatedPost);

        tagService.deleteTag(tagToDelete, adminUser);

        Mockito.verify(tagRepository, Mockito.times(1))
                .updateTag(tagToDelete);
    }

    @Test
    public void getTagByName_Should_CallRepository() {
        tagService.getTagByName(Mockito.anyString());

        Mockito.verify(tagRepository, Mockito.times(1))
                .getTagByName(Mockito.anyString());
    }

    @Test
    public void getTagById_Should_CallRepository() {
        tagService.getTagById(Mockito.anyInt());

        Mockito.verify(tagRepository, Mockito.times(1))
                .getTagById(Mockito.anyInt());
    }
    @Test
    public void updateTag_Should_Throw_When_UserIsBlocked(){
        User userToUpdate = TestHelpers.createMockNoAdminUser();
        Tag tagToBeUpdated = TestHelpers.createMockTag();
        userToUpdate.setBlocked(true);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> tagService.updateTag(tagToBeUpdated, userToUpdate));
    }

    @Test
    public void updateTag_Should_CallRepository(){
        User userToUpdate = TestHelpers.createMockNoAdminUser();
        Tag tagToBeUpdated = TestHelpers.createMockTag();

        tagService.updateTag(tagToBeUpdated, userToUpdate);

        Mockito.verify(tagRepository, Mockito.times(1))
                .updateTag(tagToBeUpdated);
    }

}
