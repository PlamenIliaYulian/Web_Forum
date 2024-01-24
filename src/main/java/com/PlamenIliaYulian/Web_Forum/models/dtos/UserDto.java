package com.PlamenIliaYulian.Web_Forum.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {
    @NotNull(message = "Firstname can't be empty.")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols.")
    private String firstName;

    @NotNull(message = "Lastname can't be empty.")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols.")
    private String lastName;

    @NotNull(message = "Username can't be empty.")
    @Size(min = 4, max = 20, message = "Username should be between 4 and 20 symbols.")
    private String userName;

    @Email(message = "Email is invalid.")
    private String email;

    /*TODO how to implement regex validation*/
    @Size(min = 6, max = 20, message = "Password should be between 6 and 20 symbols.")
    private String password;

    public UserDto() {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
