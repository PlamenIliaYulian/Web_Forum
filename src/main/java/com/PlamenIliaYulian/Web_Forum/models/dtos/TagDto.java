package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {
    @NotNull(message = "Tag can't be empty")
    @Size(min = 3, max = 30)
    private String name;

    public TagDto() {

    }

    public TagDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
