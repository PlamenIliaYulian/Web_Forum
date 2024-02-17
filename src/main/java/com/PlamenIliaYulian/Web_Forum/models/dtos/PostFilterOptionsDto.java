package com.PlamenIliaYulian.Web_Forum.models.dtos;

public class PostFilterOptionsDto {
    private int minLikes;
    private int minDislikes;
    private String title;
    private String content;
    private String createdBefore;
    private String createdAfter;
    private String createdBy;
    private String sortBy;
    private String sortOrder;
    private String tag;

    public PostFilterOptionsDto(int minLikes,
                                int minDislikes,
                                String title,
                                String content,
                                String createdBefore,
                                String createdAfter,
                                String createdBy,
                                String sortBy,
                                String sortOrder,
                                String tag) {
        this.minLikes = minLikes;
        this.minDislikes = minDislikes;
        this.title = title;
        this.content = content;
        this.createdBefore = createdBefore;
        this.createdAfter = createdAfter;
        this.createdBy = createdBy;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.tag = tag;
    }

    public PostFilterOptionsDto() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getMinLikes() {
        return minLikes;
    }

    public void setMinLikes(int minLikes) {
        this.minLikes = minLikes;
    }


    public int getMinDislikes() {
        return minDislikes;
    }

    public void setMinDislikes(int minDislikes) {
        this.minDislikes = minDislikes;
    }


    public String getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(String createdAfter) {
        this.createdAfter = createdAfter;
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
