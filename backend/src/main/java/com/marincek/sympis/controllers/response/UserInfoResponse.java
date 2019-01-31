package com.marincek.sympis.controllers.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private String username;
    private String token;
    private String exception;

    public UserInfoResponse(String username, String token) {
        this.token = token;
        this.username = username;
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
