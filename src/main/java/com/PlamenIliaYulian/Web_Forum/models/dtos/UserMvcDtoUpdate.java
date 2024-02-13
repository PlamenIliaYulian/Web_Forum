package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserMvcDtoUpdate extends UserDtoUpdate{


    @NotEmpty(message = "Confirmation password should not be empty.")
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 symbols.")
    private String confirmPassword;

    @Size(min = 8, max = 15, message = "Phone number should be between 8 and 15 digits")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
