package com.PlamenIliaYulian.Web_Forum.models.dtos;

public class UserFilterByUsernameOptionsDto {
    private String username;

    public UserFilterByUsernameOptionsDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
