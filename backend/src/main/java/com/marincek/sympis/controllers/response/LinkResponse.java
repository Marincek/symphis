package com.marincek.sympis.controllers.response;

import com.marincek.sympis.domain.Link;

import java.util.List;

public class LinkResponse {

    private final Long id;
    private final String url;
    private final List<String> tags;

    public LinkResponse(Link link) {
        this.id = link.getId();
        this.url = link.getUrl();
        this.tags = link.getTags();
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public List<String> getTags() {
        return this.tags;
    }
}
