package com.PlamenIliaYulian.Web_Forum.models.dtos;

import com.PlamenIliaYulian.Web_Forum.models.Role;

import java.util.Set;

public class  UserMvcAdminChangesDto{

    private boolean isDeleted;
    private boolean isBlocked;

    public UserMvcAdminChangesDto() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
