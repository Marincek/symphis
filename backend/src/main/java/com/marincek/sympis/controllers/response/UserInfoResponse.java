package com.marincek.sympis.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private String firstName;
    private String username;
    private String token;

    public UserInfoResponse(String username, String firstName, String token) {
        this.username = username;
        this.firstName = firstName;
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return this.username;
    }
}
