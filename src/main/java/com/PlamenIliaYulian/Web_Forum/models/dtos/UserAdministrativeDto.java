package com.PlamenIliaYulian.Web_Forum.models.dtos;

import com.PlamenIliaYulian.Web_Forum.models.Role;

import java.util.Set;

public class UserAdministrativeDto {

    private boolean isDeleted;
    private boolean isBlocked;
    private Set<Role> roles;

    public UserAdministrativeDto() {
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
