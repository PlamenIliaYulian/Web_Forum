package com.PlamenIliaYulian.Web_Forum.models;

import java.util.Optional;

public class CommentFilterOptions {

    private Optional<String> content;

    private Optional<String> createdBefore;
    private Optional<String> createdAfter;

    private Optional<String> createdBy;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;
    public CommentFilterOptions(String content, String createdBefore, String createdAfter, String createdBy,
                                String sortBy, String sortOrder) {

        this.content = Optional.ofNullable(content);
        this.createdAfter = Optional.ofNullable(createdAfter);
        this.createdBefore = Optional.ofNullable(createdBefore);
        this.createdBy = Optional.ofNullable(createdBy);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getCreatedAfter() {
        return createdAfter;
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