package com.marincek.sympis.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private String firstName;
    private String username;
    private String token;
    private String exception;

    public UserInfoResponse(String username, String firstName, String token) {
        this.username = username;
        this.firstName = firstName;
        this.token = token;
    }

    public UserInfoResponse(Exception exception) {
        this.exception = exception.getMessage();
    }

    public UserInfoResponse(String exceptionMsg) {
        this.exception = exceptionMsg;
    }

    public String getToken() {
        return this.token;
    }

    public String getUsername() {
        return this.username;
    }

    public String getException() {
        return exception;
    }
}
