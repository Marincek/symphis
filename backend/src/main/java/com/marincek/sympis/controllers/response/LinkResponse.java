package com.marincek.sympis.controllers.response;

import com.marincek.sympis.domain.Link;

import java.util.List;

public class LinkResponse {

    private Long id;
    private String url;
    private List<String> tags;

    public LinkResponse(Link link) {
        this.id = link.getId();
        this.url = link.getUrl();
        this.tags = link.getTags();
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }
}
