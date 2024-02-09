package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;


    @Test
    public void getById_Should_ReturnComment_When_MatchExist(){
        Comment comment = TestHelpers.createMockComment1();

        Mockito.when(commentRepository.getCommentById(1))
                .thenReturn(comment);

        Comment result = commentService.getCommentById(1);

        Assertions.assertEquals(1, result.getCommentId());
        Assertions.assertEquals(0, result.getLikes());
        Assertions.assertEquals(0, result.getDislikes());
        Assertions.assertEquals("Mock comment random content.", result.getContent());
    }

    @Test
    public void updateComment_Should_Throw_When_UserIsBlocked(){
        Comment commentToUpdate = TestHelpers.createMockComment1();
        User blockedUser = TestHelpers.createMockNoAdminUser();
        blockedUser.setBlocked(true);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.updateComment(commentToUpdate, blockedUser));
    }

    @Test
    public void updateComment_Should_Throw_When_UserIsNotAdminOrSameUser(){
        Comment commentToUpdate = TestHelpers.createMockComment1();
        User blockedUser = TestHelpers.createMockNoAdminUser();
        blockedUser.setUserId(777);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.updateComment(commentToUpdate, blockedUser));
    }

    @Test
    public void updateComment_Should_Pass_When_UserIsValid(){
        Comment commentToUpdate = TestHelpers.createMockComment1();
        User nonBlockedUser = TestHelpers.createMockNoAdminUser();

        commentService.updateComment(commentToUpdate, nonBlockedUser);

        Mockito.verify(commentRepository, Mockito.times(1))
                .updateComment(commentToUpdate);
    }

    @Test
    public void likeComment_Should_Throw_When_UserIsCreator(){
        Comment commentToLike = TestHelpers.createMockComment1();
        User userWhoCreatedTheComment = TestHelpers.createMockNoAdminUser();
        commentToLike.setCreatedBy(userWhoCreatedTheComment);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.likeComment(commentToLike, userWhoCreatedTheComment));
    }

    @Test
    public void likeComment_Should_Throw_When_UserAlreadyLikedComment(){
        Comment commentToLike = TestHelpers.createMockComment1();
        User userWhoWillLikeComment = TestHelpers.createMockNoAdminUser();
        Set<User> usersWhoLikeComment = new HashSet<>();
        Set<User> usersWhoDislikeComment = new HashSet<>();
        usersWhoLikeComment.add(userWhoWillLikeComment);
        commentToLike.setUsersWhoLikedComment(usersWhoLikeComment);
        commentToLike.setUsersWhoDislikedComment(usersWhoDislikeComment);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.likeComment(commentToLike, userWhoWillLikeComment));
    }

    @Test
    public void likeComment_Should_Pass_When_ArgumentsAreValid(){
        Comment commentToLike = TestHelpers.createMockComment1();
        User userWhoLikeComment = TestHelpers.createMockNoAdminUser();
        userWhoLikeComment.setUserId(777);
        Set<User> usersWhoLikedComment = new HashSet<>();
        Set<User> usersWhoDislikedComment = new HashSet<>();
        commentToLike.setUsersWhoLikedComment(usersWhoLikedComment);
        commentToLike.setUsersWhoDislikedComment(usersWhoDislikedComment);

        Mockito.when(commentRepository.updateComment(commentToLike))
                .thenReturn(commentToLike);

        commentService.likeComment(commentToLike, userWhoLikeComment);

        Mockito.verify(commentRepository, Mockito.times(1))
                .updateComment(commentToLike);
    }

    /*Ilia*/
    @Test
    public void getCommentByContent_Should_ReturnComment_When_MethodCalled() {
        Mockito.when(commentRepository.getCommentByContent(Mockito.anyString()))
                .thenReturn(TestHelpers.createMockComment1());

        Comment comment = commentService.getCommentByContent("Mock comment random content.");

        Assertions.assertEquals(1, comment.getCommentId());
    }
    /*Ilia*/
    @Test
    public void deleteComment_Throw_When_UserIsBlocked() {
        Comment comment = TestHelpers.createMockComment1();
        comment.getCreatedBy().setBlocked(true);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.deleteComment(comment));
    }
    /*Ilia*/
    @Test
    public void deleteComment_Should_DeleteComment_When_UserIsNotBlocked() {
        Comment comment = TestHelpers.createMockComment1();

        commentService.deleteComment(comment);

        Mockito.verify(commentRepository, Mockito.times(1))
                .softDeleteComment(comment);
    }
    /*Ilia*/
    @Test
    public void dislikeComment_Throw_When_IsTheSameUser() {
        Comment comment = TestHelpers.createMockComment1();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.dislikeComment(comment, comment.getCreatedBy()));
    }
    /*Ilia*/
    @Test
    public void dislikeComment_Throw_When_UserAlreadyDislikedComment() {
        Comment comment = TestHelpers.createMockComment1();
        User userDisliked = TestHelpers.createMockNoAdminUser();
        userDisliked.setUserId(20);
        Set<User> dislikeUsers = comment.getUsersWhoDislikedComment();
        dislikeUsers.add(userDisliked);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> commentService.dislikeComment(comment, userDisliked));
    }
    /*Ilia*/
    @Test
    public void dislikeComment_Should_UpdateComment_When_ValidParametersPassed () {
        Comment comment = TestHelpers.createMockComment1();
        User userDisliked = TestHelpers.createMockNoAdminUser();
        userDisliked.setUserId(20);

        Mockito.when(commentRepository.updateComment(comment))
                .thenReturn(comment);

        commentService.dislikeComment(comment, userDisliked);

        Mockito.verify(commentRepository, Mockito.times(1))
                .updateComment(comment);
    }
}
