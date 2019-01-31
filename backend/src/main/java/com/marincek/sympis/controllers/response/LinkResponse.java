package com.marincek.sympis.controllers.response;

import com.marincek.sympis.domain.Link;

import java.util.List;

public class LinkResponse{

    private String url;
    private List<String> tags;
    private String exception;

    public LinkResponse(Link link) {
        this.url = link.getUrl();
        this.tags = link.getTags();
    }

    public LinkResponse(Exception exception) {
        this.exception = exception.getMessage();
    }

    public LinkResponse(String exceptionMsg) {
        this.exception = exceptionMsg;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getException() {
        return exception;
    }
}
