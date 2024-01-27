package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull(message = "Comment can't be empty.")
    @Size(min = 10, max = 4096, message = "Comment should be between 10 and 4096 symbols.")
    private String comment;

    public CommentDto() {
    }

    public CommentDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
