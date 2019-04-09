package com.marincek.sympis.repository;

import com.marincek.sympis.domain.Link;
import com.marincek.sympis.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class LinkRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LinkRepository linkRepository;

    private User user;

    @Before
    public void setUp(){
        user = new User("therock");
        user.setPassword("passcode");
        user.setEmail("test@test.com");
        user.setFirstName("Dwayne");
        user.setLastName("Johnson");
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindAllByUsernameThenReturnLinkList() {
        // given
        Link link = new Link();
        link.setUser(user);
        link.setUrl("http://someurl.com");
        link.setTags(Arrays.asList("tag1","tag2","tag3","tag4","tag5"));
        entityManager.persist(link);
        entityManager.flush();

        // when
        List<Link> foundLinks = linkRepository.findAllByUsername(user.getUsername());

        // then
        assertNotNull(foundLinks);
        assertFalse(foundLinks.isEmpty());
        assertEquals(1, foundLinks.size());
        assertEquals(5, foundLinks.get(0).getTags().size());
    }

    @Test
    public void whenFindAllTagsForUrlThenReturnStringList() {
        // given
        Link link = new Link();
        link.setUser(user);
        link.setUrl("http://someurl.com");
        link.setTags(Arrays.asList("tag1","tag2","tag3","tag4","tag5"));
        entityManager.persist(link);
        entityManager.flush();

        // when
        List<String> foundTags = linkRepository.findAllTagsForUrl(link.getUrl());

        // then
        assertNotNull(foundTags);
        assertFalse(foundTags.isEmpty());
        assertEquals(5, foundTags.size());
    }

    @Test
    public void testExistingLinkForUser() {
        // given
        Link link = new Link();
        link.setUser(user);
        link.setUrl("http://someurl.com");
        link.setTags(Arrays.asList("tag1","tag2","tag3","tag4","tag5"));
        entityManager.persist(link);
        entityManager.flush();

        // when
        Link foundLink = linkRepository.findByUserAndUrl(user.getUsername(), link.getUrl());

        // then
        assertNotNull(foundLink);
        assertEquals(link.getUrl(), foundLink.getUrl());
        assertEquals(5, foundLink.getTags().size());
    }

    @Test
    public void testExistingLinkForUserWithNonexistingURL() {
        // given
        // non existing url

        // when
        Link foundLink = linkRepository.findByUserAndUrl(user.getUsername(), "http://somerandomurl.com");

        // then
        assertNull(foundLink);
    }
}
