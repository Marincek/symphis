package com.marincek.sympis.controllers.request;


import javax.validation.constraints.NotNull;

public class AuthenticationRequest {

    @NotNull (message = "Please provide username")
    private String username;
    @NotNull (message = "Please provide password")
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
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

}
