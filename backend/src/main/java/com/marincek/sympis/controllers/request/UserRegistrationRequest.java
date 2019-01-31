package com.marincek.sympis.controllers.request;

import com.marincek.sympis.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserRegistrationRequest {

    @NotNull(message = "Please provide username")
    private String username;
    @NotNull(message = "Please provide password")
    private String password;
    @NotNull(message = "Please provide email")
    @Email
    private String email;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}
