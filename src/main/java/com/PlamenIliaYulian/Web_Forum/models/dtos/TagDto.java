package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {
    @NotNull(message = "Tag can't be empty")
    @Size(min = 3, max = 30)
    private String tag;

    public TagDto() {

    }

    public TagDto(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
