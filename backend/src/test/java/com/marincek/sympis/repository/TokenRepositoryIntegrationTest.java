package com.marincek.sympis.repository;

import com.marincek.sympis.domain.AuthorizationToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TokenRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void testTokenFindByUsername() {
        // given
        AuthorizationToken authorizationToken = new AuthorizationToken("therock", "random_token", new Date());
        entityManager.persist(authorizationToken);
        entityManager.flush();

        // when
        Optional<AuthorizationToken> foundToken = tokenRepository.findByUsername(authorizationToken.getUsername());

        // then
        assertNotNull(foundToken);
        assertTrue(foundToken.isPresent());
        assertEquals(authorizationToken.getToken(), foundToken.get().getToken());
        assertEquals(authorizationToken.getUsername(), foundToken.get().getUsername());
    }

    @Test
    public void testTokenFindByUsernameNonExisting() {
        // given
        // non existing

        // when
        Optional<AuthorizationToken> foundToken = tokenRepository.findByUsername("randomuser");

        // then
        assertFalse(foundToken.isPresent());
    }


    @Test
    public void testFindByToken() {
        // given
        AuthorizationToken authorizationToken = new AuthorizationToken("therock","random_token",new Date());
        entityManager.persist(authorizationToken);
        entityManager.flush();

        // when
        Optional<AuthorizationToken> foundToken = tokenRepository.findByToken(authorizationToken.getToken());

        // then
        assertNotNull(foundToken);
        assertTrue(foundToken.isPresent());
        assertEquals(authorizationToken.getToken(), foundToken.get().getToken());
        assertEquals(authorizationToken.getUsername(), foundToken.get().getUsername());
    }

    @Test
    public void testFindByTokenNonExisting() {
        // given
        // non existing

        // when
        Optional<AuthorizationToken> foundToken = tokenRepository.findByToken("random_token");

        // then
        assertNotNull(foundToken);
        assertFalse(foundToken.isPresent());
    }

    @Test
    public void testDeleteByUsername() {
        // given
        AuthorizationToken authorizationToken = new AuthorizationToken("therock","random_token", new Date());
        entityManager.persist(authorizationToken);
        entityManager.flush();

        // when
        tokenRepository.deleteByUsername(authorizationToken.getUsername());
        Optional<AuthorizationToken> foundToken = tokenRepository.findByUsername(authorizationToken.getUsername());

        // then
        assertNotNull(foundToken);
        assertFalse(foundToken.isPresent());
    }

    @Test
    @Transactional
    public void testUpdateToken() {
        // given
        AuthorizationToken authorizationToken = new AuthorizationToken("therock",  "random_token",new Date());
        entityManager.persist(authorizationToken);
        entityManager.flush();

        // when
        tokenRepository.updateToken("fresh_new_token", new Date(), authorizationToken.getUsername());
        Optional<AuthorizationToken> foundToken = tokenRepository.findByUsername(authorizationToken.getUsername());

        // then
        assertNotNull(foundToken);
        assertTrue(foundToken.isPresent());
        assertEquals("fresh_new_token", foundToken.get().getToken());
    }
}
