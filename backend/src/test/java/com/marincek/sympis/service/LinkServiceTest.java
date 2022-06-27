package com.marincek.sympis.service;

import com.marincek.sympis.domain.Link;
import com.marincek.sympis.domain.User;
import com.marincek.sympis.domain.exceptions.ExistingUrlException;
import com.marincek.sympis.domain.exceptions.UnknownUserException;
import com.marincek.sympis.repository.LinkRepository;
import com.marincek.sympis.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkServiceTest {

    @Autowired
    private LinkService linkService;
    @MockBean
    private LinkRepository linkRepository;
    @MockBean
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        user = new User("therock");
        user.setPassword("passcode");
        user.setEmail("test@test.com");
        user.setFirstName("Dwayne");
        user.setLastName("Johnson");

        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        List<Link> links = new ArrayList<>();
        links.add(generateLink());
        links.add(generateLink());
        links.add(generateLink());

        Mockito.when(linkRepository.findAllByUsername(anyString())).thenReturn(links);

        Mockito.when(linkRepository.findAllByUsername(anyString())).thenReturn(links);
    }

    private Link generateLink() {
        Link link = new Link();
        link.setUser(userRepository.findByUsername("some").get());
        link.setUrl("http://someurl" + System.currentTimeMillis() + ".com");
        link.setTags(Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5"));
        return link;
    }

    /**
     * List<Link> findAllLinksForUser(String username);
     * <p>
     * Link addLink(Link link) throws MalformedURLException, UnsupportedEncodingException;
     * <p>
     * Link addLinkForUser(String username, Link link) throws MalformedURLException, UnsupportedEncodingException;
     * <p>
     * List<String> findAllTagsForUrl(String url) throws MalformedURLException, UnsupportedEncodingException;
     * <p>
     * void delete(Long id);
     */


    @Test
    public void testAddNonExistingLinkForUser() throws MalformedURLException, UnsupportedEncodingException {
        Link link = new Link("http://someurl.com", Arrays.asList("tag1", "tag2", "tag3"));

        Mockito.when(linkRepository.findByUserAndUrl(anyString(), eq("http://someurl.com"))).thenReturn(null);
        Mockito.when(linkRepository.save(link)).thenReturn(link);

        link = linkService.addLinkForUser("marincek", link);

        assertEquals("http://someurl.com", link.getUrl());
        assertEquals(3, link.getTags().size());
    }

    @Test(expected = ExistingUrlException.class)
    public void testAddExistingLinkForUser() throws MalformedURLException, UnsupportedEncodingException {
        Link link = new Link("http://someurl.com", Arrays.asList("tag1", "tag2", "tag3"));
        link.setUser(user);

        Mockito.when(linkRepository.findByUserAndUrl(anyString(), eq(link.getUrl()))).thenReturn(link);

        linkService.addLink(link);
    }

    @Test(expected = UnknownUserException.class)
    public void testAddLinkForNonExestingUser() throws MalformedURLException, UnsupportedEncodingException {
        Link link = new Link("http://someurl.com", Arrays.asList("tag1", "tag2", "tag3"));
        link.setUser(user);

        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        linkService.addLinkForUser("someUser", link);
    }

    @Test
    public void testFindAllLinksForUser() {
        List<Link> links = new ArrayList<>();
        links.add(generateLink());
        links.add(generateLink());
        links.add(generateLink());

        Mockito.when(linkRepository.findAllByUsername(anyString())).thenReturn(links);

        links = linkService.findAllLinksForUser("marincek");

        assertNotNull(links);
        assertFalse(links.isEmpty());
        assertEquals(3, links.size());
    }

    @Test
    public void testFindAllTagsForUrl() throws MalformedURLException, UnsupportedEncodingException {
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");

        Mockito.when(linkRepository.findAllTagsForUrl(anyString())).thenReturn(tags);

        tags = linkService.findAllTagsForUrl("http://someurl.com");

        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        assertEquals(3, tags.size());
    }

    @Test(expected = MalformedURLException.class)
    public void testFindAllTagsForUrlWithWrongUrl() throws MalformedURLException, UnsupportedEncodingException {
        linkService.findAllTagsForUrl("someurl.com");
    }
}
