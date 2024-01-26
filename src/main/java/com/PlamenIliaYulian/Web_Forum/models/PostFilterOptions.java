package com.PlamenIliaYulian.Web_Forum.models;

import java.util.Optional;

public class PostFilterOptions {


    public PostFilterOptions(Integer likes, Integer dislikes, String title,
                             String content, String createdBefore, String createdBy,
                             String sortBy, String sortOrder) {
        this.likes = Optional.ofNullable(likes);
        this.dislikes = Optional.ofNullable(dislikes);
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.createdBefore = Optional.ofNullable(createdBefore);
        this.createdBy = Optional.ofNullable(createdBy);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    private Optional<Integer> likes;
    private Optional<Integer> dislikes;
    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> createdBefore;
    private Optional<String> createdBy;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public Optional<Integer> getLikes() {
        return likes;
    }

    public Optional<Integer> getDislikes() {
        return dislikes;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getCreatedBefore() {
        return createdBefore;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}