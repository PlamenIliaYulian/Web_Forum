package com.PlamenIliaYulian.Web_Forum.models.dtos;

import java.util.Optional;

public class PostFilterOptionsDto {
    private int likes;
    private int dislikes;
    private String title;
    private String content;
    private String createdBefore;
    private String createdBy;
    private String sortBy;
    private String sortOrder;

    public PostFilterOptionsDto(int likes, int dislikes, String title, String content, String createdBefore, String createdBy, String sortBy, String sortOrder) {
        this.likes = likes;
        this.dislikes = dislikes;
        this.title = title;
        this.content = content;
        this.createdBefore = createdBefore;
        this.createdBy = createdBy;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public PostFilterOptionsDto() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(String createdBefore) {
        this.createdBefore = createdBefore;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
