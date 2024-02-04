package com.PlamenIliaYulian.Web_Forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment implements Comparable<Comment>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "content")
    private String content;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "comments_users_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersWhoLikedComment;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "comments_users_dislikes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersWhoDislikedComment;

    public Set<User> getUsersWhoLikedComment() {
        return usersWhoLikedComment;
    }

    public void setUsersWhoLikedComment(Set<User> usersWhoLikedComment) {
        this.usersWhoLikedComment = usersWhoLikedComment;
    }

    public Set<User> getUsersWhoDislikedComment() {
        return usersWhoDislikedComment;
    }

    public void setUsersWhoDislikedComment(Set<User> usersWhoDislikedComment) {
        this.usersWhoDislikedComment = usersWhoDislikedComment;
    }

    public Comment() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    @Override
    public int compareTo(Comment o) {
        return Integer.compare(this.getCommentId(), o.getCommentId());
    }
}
