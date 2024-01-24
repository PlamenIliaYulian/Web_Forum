package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull(message = "Comment can't be empty.")
    @Size(min = 10, max = 4096, message = "Comment should be between 10 and 4096 symbols.")
    private String content;

    public CommentDto() {
    }

    public CommentDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
