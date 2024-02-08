package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotEmpty(message = "Username should not be empty.")
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 symbols.")
    private String username;
    @NotEmpty(message = "Password should not be empty.")
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 symbols.")
    private String password;
    @NotEmpty
    private String passwordConfirm;
    @NotEmpty(message = "First name should not be empty.")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols.")
    private String firstName;
    @NotEmpty(message = "Last name should not be empty.")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols.")
    private String lastName;
    @Email(message = "Invalid email.")
    @NotEmpty(message = "Email should not be empty.")
    private String email;

    public RegisterDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
