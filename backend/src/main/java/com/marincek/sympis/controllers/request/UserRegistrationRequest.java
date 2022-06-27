package com.marincek.sympis.controllers.request;

import com.marincek.sympis.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserRegistrationRequest {

    @NotNull (message = "Please provide username")
    private String username;
    @NotNull (message = "Please provide password")
    private String password;
    @NotNull (message = "Please provide email")
    @Email
    private String email;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        return user;
    }
}
