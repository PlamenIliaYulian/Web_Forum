package com.PlamenIliaYulian.Web_Forum.helpers;

import com.PlamenIliaYulian.Web_Forum.models.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class TestHelpers {

    public static User createMockAdminUser() {
        Set<Role> roles = new TreeSet<>();
        roles.add(createMockRoleAdmin());
        roles.add(createMockRoleStaffMember());
        roles.add(createMockRoleMember());

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("Mock_Admin_User@gmail.com");
        mockUser.setPassword("MnOpQrStUvWxYzAbCdEf");
        mockUser.setFirstName("Mock Admin");
        mockUser.setLastName("User");
        mockUser.setUserName("Mock_Admin_User");
        mockUser.setDeleted(false);
        mockUser.setBlocked(false);
        mockUser.setAvatar(null);
        mockUser.setRoles(roles);
        mockUser.setPhoneNumber("0891234567");
        return mockUser;
    }

    public static User createMockNoAdminUser() {
        Set<Role> roles = new TreeSet<>(Set.of(createMockRoleMember()));
        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setEmail("Mock_NoAdmin_User@gmail.com");
        mockUser.setPassword("MnOpQrStUvWxYzAbCdEf");
        mockUser.setFirstName("Mock NoAdmin");
        mockUser.setLastName("User");
        mockUser.setUserName("Mock_NoAdmin_User");
        mockUser.setDeleted(false);
        mockUser.setBlocked(false);
        mockUser.setAvatar(null);
        mockUser.setRoles(roles);
        mockUser.setPhoneNumber("0891234567");
        return mockUser;
    }

    public static Tag createMockTag() {
        Tag mockTag = new Tag();
        mockTag.setTagId(777);
        mockTag.setTag("#luckyNumber");
        mockTag.setDeleted(false);
        mockTag.setRelatedPosts(null);
        return mockTag;
    }

    public static Post createMockPost1() {
        Set<Comment> relatedComments = new TreeSet<>();
        Set<Tag> relatedTags = new TreeSet<>();
        relatedTags.add(createMockTag());

        Set<User> usersWhoLikedThePost = new TreeSet<>();
        usersWhoLikedThePost.add(createMockAdminUser());

        Set<User> usersWhoDislikedThePost = new TreeSet<>();
        User userWhodisliked = createMockNoAdminUser();
        userWhodisliked.setUserId(3);
        usersWhoDislikedThePost.add(userWhodisliked);

        Post mockPost = new Post();

        mockPost.setPostId(1);
        mockPost.setLikes(1);
        mockPost.setDislikes(1);
        mockPost.setDeleted(false);
        mockPost.setTitle("Mock post title.");
        mockPost.setContent("Mock post random content.");
        mockPost.setCreatedOn(LocalDateTime.of(2024, 1, 28, 0, 0, 0));
        mockPost.setCreatedBy(createMockNoAdminUser());
        mockPost.setRelatedComments(relatedComments);
        mockPost.setTags(relatedTags);
        mockPost.setUsersWhoLikedPost(usersWhoLikedThePost);
        mockPost.setUsersWhoDislikedPost(usersWhoDislikedThePost);

        return mockPost;
    }

    public static Post createMockPost2() {
        Set<Comment> relatedComments = new TreeSet<>();
        Set<Tag> relatedTags = new TreeSet<>();
        relatedTags.add(createMockTag());

        Set<User> usersWhoLikedThePost = new TreeSet<>();
        usersWhoLikedThePost.add(createMockAdminUser());

        Set<User> usersWhoDislikedThePost = new TreeSet<>();
        User userWhodisliked = createMockNoAdminUser();
        userWhodisliked.setUserId(3);
        usersWhoDislikedThePost.add(userWhodisliked);

        Post mockPost = new Post();

        mockPost.setPostId(1);
        mockPost.setLikes(1);
        mockPost.setDislikes(1);
        mockPost.setDeleted(false);
        mockPost.setTitle("Mock post2 title.");
        mockPost.setContent("Mock post2 random content.");
        mockPost.setCreatedOn(LocalDateTime.of(2024, 1, 28, 0, 0, 0));
        mockPost.setCreatedBy(createMockNoAdminUser());
        mockPost.setRelatedComments(relatedComments);
        mockPost.setTags(relatedTags);
        mockPost.setUsersWhoLikedPost(usersWhoLikedThePost);
        mockPost.setUsersWhoDislikedPost(usersWhoDislikedThePost);

        return mockPost;
    }

    public static Role createMockRoleAdmin() {
        Role mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setName("ADMIN");
        return mockRole;
    }

    public static Role createMockRoleStaffMember() {
        Role mockRole = new Role();
        mockRole.setRoleId(3);
        mockRole.setName("STAFF_MEMBER");
        return mockRole;
    }

    public static Role createMockRoleMember() {
        Role mockRole = new Role();
        mockRole.setRoleId(2);
        mockRole.setName("MEMBER");
        return mockRole;
    }

    public static Comment createMockComment1() {
        Comment mockComment = new Comment();
        Set<User> usersWhoLikedTheComment = new TreeSet<>();
        Set<User> usersWhoDislikedTheComment = new TreeSet<>();

        mockComment.setCommentId(1);
        mockComment.setLikes(0);
        mockComment.setDislikes(0);
        mockComment.setDeleted(false);
        mockComment.setContent("Mock comment random content.");
        mockComment.setCreatedOn(LocalDateTime.of(2024, 1, 28, 0, 0, 0));
        mockComment.setCreatedBy(createMockNoAdminUser());
        mockComment.setUsersWhoLikedComment(usersWhoLikedTheComment);
        mockComment.setUsersWhoDislikedComment(usersWhoDislikedTheComment);
        return mockComment;
    }

    public static PostFilterOptions createPostFilterOptions() {
        return new PostFilterOptions(
                0,
                0,
                "Test title",
                "Test content",
                "2024-01-31 00:00:00",
                "2024-01-31 00:00:00",
                "Test User",
                null,
                null,
                null
        );
    }

    public static CommentFilterOptions createCommentFilterOptions() {
        return new CommentFilterOptions(
                "Content",
                null,
                "2024-01-31 00:00:00",
                "Comment_Creator",
                null,
                null
        );
    }

    public static UserFilterOptions createUserFilterOptions(){
        return new UserFilterOptions(
                "username",
                "email",
                "firstName",
                null,
                null
        );
    }
    public static Avatar createAvatar() {
        Avatar avatar = new Avatar();
        avatar.setAvatarId(1);
        return avatar;
    }

    public static MultipartFile createMultipartFile() {
        return new MockMultipartFile("sourceFile.tmp", "Hello World".getBytes());
    }
}
