package com.marincek.sympis.service;

import com.marincek.sympis.domain.Link;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

public interface LinkService {

    List<Link> findAllLinksForUser(String username);

    Link addLink(Link link) throws MalformedURLException, UnsupportedEncodingException;

    Link addLinkForUser(String username, Link link) throws MalformedURLException, UnsupportedEncodingException;

    List<String> findAllTagsForUrl(String url) throws MalformedURLException, UnsupportedEncodingException;

    void delete(Long id);
}
