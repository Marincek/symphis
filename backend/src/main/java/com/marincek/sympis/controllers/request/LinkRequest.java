package com.marincek.sympis.controllers.request;

import com.marincek.sympis.controllers.validation.TagConstraint;
import com.marincek.sympis.domain.Link;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class LinkRequest {

    @NotNull(message = "Please provide url")
    private String url;
    @Size(min = 1, max = 20, message = "You can add between 1 and 20 tags")
    @TagConstraint
    private List<String> tags;

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public Link convert(){
        return new Link(url,tags);
    }
}
