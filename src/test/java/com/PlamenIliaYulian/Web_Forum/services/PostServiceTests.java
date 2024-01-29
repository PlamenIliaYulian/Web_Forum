package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidOperationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.InvalidUserInputException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.PostRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    PostRepository postRepository;
    @Mock
    CommentService commentService;


    @InjectMocks
    PostServiceImpl postService;

    @Test
    public void createPost_Should_CreatePost_When_PostCreatorIsNotBlockedAndValidPostIsPassed() {
        User postCreator = TestHelpers.createMockNoAdminUser();
        Post postToBeCreated = TestHelpers.createMockPost1();
        Mockito.when(postRepository.createPost(postToBeCreated))
                .thenReturn(postToBeCreated);
        Post createdPost = postService.createPost(postToBeCreated, postCreator);
        Mockito.verify(postRepository, Mockito.times(1))
                .createPost(postToBeCreated);
        Assertions.assertEquals(createdPost, postToBeCreated);
    }

    @Test
    public void createPost_Should_Throw_When_PostCreatorIsBlocked() {
        User postCreator = TestHelpers.createMockNoAdminUser();
        postCreator.setBlocked(true);
        Post postToBeCreated = TestHelpers.createMockPost1();
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.createPost(postToBeCreated, postCreator));
    }

    @Test
    public void getAllPosts_Should_Pass() {
        User userExecutingTheRequest = TestHelpers.createMockNoAdminUser();
        List<Post> posts = new ArrayList<>();
        posts.add(TestHelpers.createMockPost1());
        PostFilterOptions filterOptions = TestHelpers.createPostFilterOptions();
        Mockito.when(postRepository.getAllPosts(Mockito.any()))
                .thenReturn(posts);
        List<Post> returnedPosts = postService.getAllPosts(userExecutingTheRequest, filterOptions);
        Mockito.verify(postRepository, Mockito.times(1))
                .getAllPosts(filterOptions);
        Assertions.assertEquals(posts, returnedPosts);
    }

    @Test
    public void likePost_Should_Throw_When_UserTryingToLikeThePostIsPostCreator() {
        User userTryingToLikeThePost = TestHelpers.createMockNoAdminUser();
        Post postToLike = TestHelpers.createMockPost1();
        postToLike.setCreatedBy(userTryingToLikeThePost);
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.likePost(postToLike, userTryingToLikeThePost));
    }

    @Test
    public void likePost_Should_Throw_When_UserTryingToLikeThePostHasAlreadyLikedIt() {
        User userTryingToLikeThePost = TestHelpers.createMockNoAdminUser();
        userTryingToLikeThePost.setUserId(777);
        Post postToLike = TestHelpers.createMockPost1();
        Set<User> usersWhoLikedThePost = new TreeSet<>();
        usersWhoLikedThePost.add(userTryingToLikeThePost);
        Set<User> usersWhoDislikedThePost = new TreeSet<>();
        postToLike.setUsersWhoLikedPost(usersWhoLikedThePost);
        postToLike.setUsersWhoDislikedPost(usersWhoDislikedThePost);
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.likePost(postToLike, userTryingToLikeThePost));
    }

    @Test
    public void likePost_Should_Pass_When_UserTryingToLikeThePostHasNotLikedItBefore() {
        User userTryingToLikeThePost = TestHelpers.createMockNoAdminUser();
        userTryingToLikeThePost.setUserId(777);
        Post postToLike = TestHelpers.createMockPost1();
        Set<User> usersWhoLikedThePost = new TreeSet<>();
        Set<User> usersWhoDislikedThePost = new TreeSet<>();
        postToLike.setUsersWhoLikedPost(usersWhoLikedThePost);
        postToLike.setUsersWhoDislikedPost(usersWhoDislikedThePost);
        Mockito.when(postRepository.updatePost(postToLike))
                .thenReturn(postToLike);
        postService.likePost(postToLike, userTryingToLikeThePost);
        Mockito.verify(postRepository, Mockito.times(1))
                .updatePost(postToLike);
    }

    @Test
    public void removeTagFromPost_Should_Throw_When_UserTryingToRemoveTheIsBlocked() {
        Post postToRemoveTagFrom = TestHelpers.createMockPost1();
        Tag tagToRemoveFromPost = TestHelpers.createMockTag();
        User blockedUser = TestHelpers.createMockNoAdminUser();
        blockedUser.setBlocked(true);
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.removeTagFromPost(postToRemoveTagFrom, tagToRemoveFromPost, blockedUser));
    }

    @Test
    public void removeTagFromPost_Should_Throw_When_UserTryingToRemoveIsNotPostCreator() {
        User notPostCreator = TestHelpers.createMockNoAdminUser();
        notPostCreator.setUserId(777);
        Post postToRemoveTagFrom = TestHelpers.createMockPost1();
        Tag tagToRemoveFromPost = TestHelpers.createMockTag();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.removeTagFromPost(postToRemoveTagFrom, tagToRemoveFromPost, notPostCreator));
    }

    @Test
    public void removeTagFromPost_Should_Throw_When_UserTryingToRemoveIsNotAdmin() {
        User notAdmin = TestHelpers.createMockNoAdminUser();
        notAdmin.setUserId(777);
        Post postToRemoveTagFrom = TestHelpers.createMockPost1();
        Tag tagToRemoveFromPost = TestHelpers.createMockTag();
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.removeTagFromPost(postToRemoveTagFrom, tagToRemoveFromPost, notAdmin));
    }

    @Test
    public void removeTagFromPost_Should_Throw_When_ThePostDoesNotContainTheTag() {
        User postCreator = TestHelpers.createMockNoAdminUser();
        Post postToRemoveTagFrom = TestHelpers.createMockPost1();
        Tag tagToRemoveFromPost = TestHelpers.createMockTag();
        postToRemoveTagFrom.setTags(new TreeSet<Tag>());
        Assertions.assertThrows(InvalidUserInputException.class,
                () -> postService.removeTagFromPost(postToRemoveTagFrom, tagToRemoveFromPost, postCreator));
    }

    @Test
    public void removeTagFromPost_Should_Pass_When_AllArgumentsProvidedToTheMethodAreOk() {
        User postCreator = TestHelpers.createMockNoAdminUser();
        Post postToRemoveTagFrom = TestHelpers.createMockPost1();
        Tag tagToRemoveFromPost = TestHelpers.createMockTag();
        Set<Tag> tags = new TreeSet<>();
        tags.add(tagToRemoveFromPost);
        postToRemoveTagFrom.setTags(tags);
        Mockito.when(postRepository.updatePost(postToRemoveTagFrom))
                .thenReturn(postToRemoveTagFrom);
        postService.removeTagFromPost(postToRemoveTagFrom, tagToRemoveFromPost, postCreator);
        Mockito.verify(postRepository, Mockito.times(1))
                .updatePost(postToRemoveTagFrom);
    }

    @Test
    public void removeCommentFromPost_Should_Throw_When_UserIsBlocked() {
        Post postToRemoveCommentFrom = TestHelpers.createMockPost1();
        String commentContent = "Comment";
        User blockedUser = TestHelpers.createMockNoAdminUser();
        blockedUser.setBlocked(true);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.removeCommentFromPost(
                        postToRemoveCommentFrom,
                        commentContent,
                        blockedUser
                ));
    }


    @Test
    public void removeCommentFromPost_Should_Throw_When_WeCannotFindTheCommentByItsContent() {
        Post postToRemoveCommentFrom = TestHelpers.createMockPost1();
        String commentContent = "Comment";
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        Mockito.when(commentService.getCommentByContent(commentContent))
                .thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> postService.removeCommentFromPost(postToRemoveCommentFrom, commentContent, nonBlockedUser));
    }


    @Test
    public void removeCommentFromPost_Should_Throw_When_UserTryingToRemoveCommentIsNotItsCreatorNorAdmin() {
        Post postToRemoveCommentFrom = TestHelpers.createMockPost1();
        String commentContent = "Comment";
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        nonBlockedUser.setUserId(777);
        Comment commentToRemove = TestHelpers.createMockComment1();

        Mockito.when(commentService.getCommentByContent(commentContent))
                .thenReturn(commentToRemove);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> postService.removeCommentFromPost(postToRemoveCommentFrom, commentContent, nonBlockedUser));
    }

    @Test
    public void removeCommentFromPost_Should_Throw_When_TheCommentDoesNotBelongToThisPost() {
        Post postToRemoveCommentFrom = TestHelpers.createMockPost1();
        Set<Comment> comments = new TreeSet<>();
        postToRemoveCommentFrom.setRelatedComments(comments);
        String commentContent = "Comment";
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        Comment commentToRemove = TestHelpers.createMockComment1();

        Mockito.when(commentService.getCommentByContent(commentContent))
                .thenReturn(commentToRemove);

        Assertions.assertThrows(InvalidOperationException.class,
                () -> postService.removeCommentFromPost(postToRemoveCommentFrom, commentContent, nonBlockedUser));
    }

    @Test
    public void removeCommentFromPost_Should_Pass_When_AllArgumentsAreOk() {
        Comment commentToRemove = TestHelpers.createMockComment1();
        Post postToRemoveCommentFrom = TestHelpers.createMockPost1();
        Set<Comment> relatedComments = new TreeSet<>();
        relatedComments.add(commentToRemove);
        postToRemoveCommentFrom.setRelatedComments(relatedComments);
        String commentContent = "Comment";
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        Mockito.when(commentService.getCommentByContent(commentContent))
                .thenReturn(commentToRemove);
        Mockito.doNothing().when(commentService).deleteComment(commentToRemove);
        Mockito.when(postRepository.updatePost(postToRemoveCommentFrom))
                .thenReturn(postToRemoveCommentFrom);
        postService.removeCommentFromPost(postToRemoveCommentFrom, commentContent, nonBlockedUser);
        Mockito.verify(postRepository, Mockito.times(1))
                .updatePost(postToRemoveCommentFrom);
    }

    @Test
    public void deletePost_Should_Throw_When_UserIsBlocked(){
        Post postToDelete = TestHelpers.createMockPost1();
        User userWhoTriesToDelete = TestHelpers.createMockNoAdminUser();
        userWhoTriesToDelete.setBlocked(true);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> postService.deletePost(postToDelete, userWhoTriesToDelete));
    }

    @Test
    public void deletePost_Should_Throw_When_UserIsNotAdminOrSameUser(){
        Post postToDelete = TestHelpers.createMockPost1();
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        nonBlockedUser.setUserId(777);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> postService.deletePost(postToDelete, nonBlockedUser));

    }

    @Test
    public void deletePost_Should_Pass_When_ArgumentsAreValid(){
        Post postToDelete = TestHelpers.createMockPost1();
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();
        postToDelete.setCreatedBy(nonBlockedUser);

        postService.deletePost(postToDelete, nonBlockedUser);

        Mockito.verify(postRepository,Mockito.times(1))
                .softDeletePost(postToDelete);

    }

    @Test
    public void getPostByTitle_Should_Return_When_PostExists(){
        Post post = TestHelpers.createMockPost1();

        Mockito.when(postRepository.getPostByTitle(Mockito.anyString()))
                .thenReturn(post);

        Post result = postService.getPostByTitle(post.getTitle());

        Assertions.assertEquals(1, result.getPostId());
        Assertions.assertEquals("Mock post title.", result.getTitle());
        Assertions.assertEquals("Mock post random content.", result.getContent());
        Assertions.assertEquals(1, result.getPostId());

    }

    @Test
    public void dislikePost_Should_Throw_When_UserAlreadyDislikedPost(){
        Post postToDislike = TestHelpers.createMockPost1();
        User userToDislike = TestHelpers.createMockNoAdminUser();

        Set<User> usersWhoDislikedPost = new HashSet<>();
        usersWhoDislikedPost.add(userToDislike);
        postToDislike.setUsersWhoDislikedPost(usersWhoDislikedPost);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> postService.dislikePost(postToDislike, userToDislike));

    }

    @Test
    public void dislikePost_Should_Pass_When_Valid(){
        Post postToDislike = TestHelpers.createMockPost1();
        User userToDislike = TestHelpers.createMockNoAdminUser();
        userToDislike.setUserId(888);

        Mockito.when(postRepository.updatePost(postToDislike))
                .thenReturn(postToDislike);

        postService.likePost(postToDislike, userToDislike);

        Mockito.verify(postRepository, Mockito.times(1))
                .updatePost(postToDislike);

    }

    @Test
    public void addCommentToPost_Should_Throw_When_UserIsBlocked(){
        Post postToAddComment = TestHelpers.createMockPost1();
        User userToAddCommentToPost = TestHelpers.createMockNoAdminUser();
        Comment comment = TestHelpers.createMockComment1();
        userToAddCommentToPost.setBlocked(true);
        postToAddComment.setCreatedBy(userToAddCommentToPost);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> postService.addCommentToPost(postToAddComment, comment, userToAddCommentToPost));

    }

    @Test
    public void addCommentToPost_Should_Pass_When_ArgumentsAreValid(){
        Post postToAddComment = TestHelpers.createMockPost1();
        User userToAddCommentToPost = TestHelpers.createMockNoAdminUser();
        Comment comment = TestHelpers.createMockComment1();

        Mockito.when(postRepository.updatePost(postToAddComment))
                .thenReturn(postToAddComment);

        postService.addCommentToPost(postToAddComment, comment, userToAddCommentToPost);

        Mockito.verify(postRepository, Mockito.times(1))
                .updatePost(postToAddComment);

    }

}
