package com.PlamenIliaYulian.Web_Forum.models;

import java.util.Optional;

public class PostFilterOptions {
    private Optional<Integer> minLikes;
    private Optional<Integer> minDislikes;
    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> createdBefore;
    private Optional<String> createdAfter;
    private Optional<String> createdBy;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;
    private Optional<String> tag;


    public PostFilterOptions(Integer minLikes, Integer minDislikes, String title,
                             String content, String createdBefore, String createdAfter, String createdBy,
                             String sortBy, String sortOrder, String tag) {

        this.minLikes = Optional.ofNullable(minLikes);
        this.minDislikes = Optional.ofNullable(minDislikes);
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.createdBefore = Optional.ofNullable(createdBefore);
        this.createdAfter = Optional.ofNullable(createdAfter);
        this.createdBy = Optional.ofNullable(createdBy);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
        this.tag = Optional.ofNullable(tag);
    }

    public Optional<String> getTag() {
        return tag;
    }

    public Optional<String> getCreatedAfter() {
        return createdAfter;
    }

    public Optional<Integer> getMinLikes() {
        return minLikes;
    }

    public Optional<Integer> getMinDislikes() {
        return minDislikes;
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