package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotNull;

public class RoleDto {
    @NotNull
    int dtoRoleId;

    public RoleDto() {
    }

    public RoleDto(int dtoRoleId) {
        this.dtoRoleId = dtoRoleId;
    }

    public int getDtoRoleId() {
        return dtoRoleId;
    }

    public void setDtoRoleId(int dtoRoleId) {
        this.dtoRoleId = dtoRoleId;
    }
}
