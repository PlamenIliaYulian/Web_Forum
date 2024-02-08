package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserMvcDtoUpdate extends UserDtoUpdate{
    private String confirmPassword;

    @NotEmpty(message = "Confirmation password should not be empty.")
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 symbols.")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
