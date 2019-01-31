package com.marincek.sympis.service.impl;

import com.marincek.sympis.domain.AuthorizationToken;
import com.marincek.sympis.repository.TokenRepository;
import com.marincek.sympis.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class DefaultTokenService implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public AuthorizationToken createToken(String username) {
        return tokenRepository.save(new AuthorizationToken(username, generateToken(), new Date()));
    }

    @Override
    public AuthorizationToken refreshToken(String username, String oldToken) {
        Optional<AuthorizationToken> dbAuthorizationTokenOptional = tokenRepository.findByUsername(username);
        if(dbAuthorizationTokenOptional.isPresent() && oldToken.equals(dbAuthorizationTokenOptional.get().getToken())){
            tokenRepository.delete(dbAuthorizationTokenOptional.get());
            return createToken(username);
        }else {
            throw new RuntimeException("User "+username+" have 0 old tokens");
        }
    }

    @Override
    public Optional<AuthorizationToken> findByUsername(String username) {
        return tokenRepository.findByUsername(username);
    }

    @Override
    public Optional<AuthorizationToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    private String generateToken(){
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }
}
