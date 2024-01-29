package com.PlamenIliaYulian.Web_Forum.helpers;

import com.PlamenIliaYulian.Web_Forum.models.*;
import org.antlr.v4.runtime.tree.Tree;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class TestHelpers {

    /*Yuli*/
    public static User createMockAdminUser() {
        Set roles = new TreeSet<>();
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

    /*Yuli*/
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

    /*Yuli*/
    public static Tag createMockTag() {
        Tag mockTag = new Tag();
        /*Setting relatedPosts to null, because the method createTag_Should_Pass_When_TagCreatorIsNotBlocked
        * in "TagServiceTests" falls into stackOverflow...
        *
        *  Set<Post> relatedPosts = new TreeSet<>();
        relatedPosts.add(createMockPost1());*/
        mockTag.setTagId(777);
        mockTag.setName("#luckyNumber");
        mockTag.setDeleted(false);
        mockTag.setRelatedPosts(null);
        return mockTag;
    }

    /*Yuli*/
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

    /*Yuli*/
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

    /*Yuli*/
    public static Role createMockRoleAdmin() {
        Role mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setName("ROLE_ADMIN");
        return mockRole;
    }

    /*Yuli*/
    public static Role createMockRoleStaffMember() {
        Role mockRole = new Role();
        mockRole.setRoleId(3);
        mockRole.setName("ROLE_STAFF_MEMBER");
        return mockRole;
    }

    /*Yuli*/
    public static Role createMockRoleMember() {
        Role mockRole = new Role();
        mockRole.setRoleId(2);
        mockRole.setName("ROLE_MEMBER");
        return mockRole;
    }

    /*Yuli*/
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

}
