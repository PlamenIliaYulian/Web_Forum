package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Post;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TagServiceTests {
    /*ToDO Change names to MockRepository and MockService*/

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl tagService;

    /*TODO Yuli Update the test with the new logic.*/
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



}
