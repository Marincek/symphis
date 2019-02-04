package com.marincek.sympis.service;

import com.marincek.sympis.domain.AuthorizationToken;
import com.marincek.sympis.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @MockBean
    private TokenRepository tokenRepository;

    @Test
    public void testCreateToken(){
        AuthorizationToken authorizationToken = new AuthorizationToken("therock", "generated_token", new Date());
        Mockito.when(tokenRepository.save(any(AuthorizationToken.class))).thenReturn(authorizationToken);

        authorizationToken = tokenService.createToken("therock");

        assertNotNull(authorizationToken);
        assertNotNull(authorizationToken.getToken());
        assertEquals("therock", authorizationToken.getUsername());
    }

    @Test(expected = RuntimeException.class)
    public void testRefreshTokenNonExistingUser(){
        Mockito.when(tokenRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        tokenService.refreshToken("someUsername","some_old_token");
    }

    @Test
    public void testRefreshToken(){
        AuthorizationToken authorizationToken = new AuthorizationToken("therock", "some_old_token", new Date());
        Mockito.when(tokenRepository.findByUsername(anyString())).thenReturn(Optional.of(authorizationToken));
        Mockito.when(tokenRepository.save(any(AuthorizationToken.class))).thenReturn(new AuthorizationToken("therock", "new_generated_token", new Date()));

        authorizationToken = tokenService.refreshToken("someUsername","some_old_token");

        assertNotNull(authorizationToken);
        assertNotNull(authorizationToken.getToken());
        assertEquals("new_generated_token", authorizationToken.getToken());
        assertEquals("therock", authorizationToken.getUsername());
    }

    @Test
    public void testFindByUsername(){
        AuthorizationToken authorizationToken = new AuthorizationToken("therock", "generated_token", new Date());
        Mockito.when(tokenRepository.findByUsername(anyString())).thenReturn(Optional.of(authorizationToken));

        Optional<AuthorizationToken> authorizationTokenOptional = tokenService.findByUsername("therock");

        assertNotNull(authorizationTokenOptional);
        assertTrue(authorizationTokenOptional.isPresent());
        assertEquals("generated_token", authorizationTokenOptional.get().getToken());
        assertEquals("therock", authorizationTokenOptional.get().getUsername());
    }

    @Test
    public void testFindByToken(){
        AuthorizationToken authorizationToken = new AuthorizationToken("therock", "generated_token", new Date());
        Mockito.when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(authorizationToken));

        Optional<AuthorizationToken> authorizationTokenOptional = tokenService.findByToken("generated_token");

        assertNotNull(authorizationTokenOptional);
        assertTrue(authorizationTokenOptional.isPresent());
        assertEquals("generated_token", authorizationTokenOptional.get().getToken());
        assertEquals("therock", authorizationTokenOptional.get().getUsername());
    }



}
