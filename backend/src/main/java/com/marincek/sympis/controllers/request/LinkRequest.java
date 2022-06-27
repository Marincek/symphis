package com.marincek.sympis.controllers.request;

import com.marincek.sympis.controllers.validation.TagConstraint;
import com.marincek.sympis.domain.Link;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.util.List;

public class LinkRequest {

    @URL
    @NotNull(message = "Please provide url")
    private String url;

    @TagConstraint
    private List<String> tags;

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public Link convert() {
        return new Link(url, tags);
    }
}
