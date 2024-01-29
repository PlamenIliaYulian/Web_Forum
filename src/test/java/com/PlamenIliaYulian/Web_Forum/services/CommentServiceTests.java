package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Comment;
import com.PlamenIliaYulian.Web_Forum.models.CommentFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.repositories.CommentRepositoryImpl;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.CommentRepository;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.PostRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void createComment_Should_Throw_When_TheUserIsBlocked() {
        User blockedUser = TestHelpers.createMockNoAdminUser();
        blockedUser.setBlocked(true);
        Comment commentToCreate = TestHelpers.createMockComment1();
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> commentService.createComment(commentToCreate, blockedUser));
    }

    @Test
    public void createComment_Should_Pass_When_UserIsNotBlockedAndValidCommentPassed() {
        User nonBlockeduser = TestHelpers.createMockNoAdminUser();
        Comment commentToCreate = TestHelpers.createMockComment1();
        Mockito.when(commentRepository.createComment(commentToCreate))
                .thenReturn(commentToCreate);
        Comment createdComment = commentService.createComment(commentToCreate, nonBlockeduser);
        Mockito.verify(commentRepository, Mockito.times(1))
                .createComment(commentToCreate);
        Assertions.assertEquals(commentToCreate, createdComment);
    }

    @Test
    public void getAllComments_Should_ReturnAllComments() {
        User userSearchingForAllComments = TestHelpers.createMockNoAdminUser();
        Mockito.when(commentService.getAllComments(userSearchingForAllComments, Mockito.any()))
                .thenReturn(new ArrayList<Comment>());
        List<Comment> result = commentService.getAllComments(
                userSearchingForAllComments,
                TestHelpers.createCommentFilterOptions());
        Mockito.verify(commentRepository, Mockito.times(1))
                .getAllComments(Mockito.any());
        Assertions.assertEquals(result, new ArrayList<Comment>());
    }


}
