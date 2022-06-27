package com.marincek.sympis.service.impl;

import com.marincek.sympis.domain.Link;
import com.marincek.sympis.domain.User;
import com.marincek.sympis.domain.exceptions.ExistingUrlException;
import com.marincek.sympis.domain.exceptions.UnknownUserException;
import com.marincek.sympis.repository.LinkRepository;
import com.marincek.sympis.repository.UserRepository;
import com.marincek.sympis.service.LinkService;
import com.marincek.sympis.service.UrlNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

@Component
public class DefaultLinkService implements LinkService {

    private LinkRepository linkRepository;
    private UserRepository userRepository;
    private UrlNormalizer urlNormalizer;

    @Autowired
    public DefaultLinkService(LinkRepository linkRepository, UserRepository userRepository, UrlNormalizer urlNormalizer) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.urlNormalizer = urlNormalizer;
    }

    @Override
    public List<Link> findAllLinksForUser(String username) {
        return linkRepository.findAllByUsername(username);
    }

    @Override
    public Link addLink(Link link) throws MalformedURLException, UnsupportedEncodingException {
        link.setUrl(urlNormalizer.normalize(link.getUrl()));
        if (linkRepository.findByUserAndUrl(link.getUser().getUsername(), link.getUrl()) != null) {
            throw new ExistingUrlException();
        }
        return linkRepository.save(link);
    }

    @Override
    public Link addLinkForUser(String username, Link link) throws MalformedURLException, UnsupportedEncodingException {
        User user = userRepository.findByUsername(username).orElseThrow(UnknownUserException::new);
        link.setUser(user);

        return addLink(link);
    }

    @Override
    public List<String> findAllTagsForUrl(String url) throws MalformedURLException, UnsupportedEncodingException {
        return linkRepository.findAllTagsForUrl(urlNormalizer.normalize(url));
    }

    @Override
    public void delete(Long id) {
        linkRepository.deleteById(id);
    }
}
