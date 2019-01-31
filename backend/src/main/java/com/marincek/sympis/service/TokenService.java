package com.marincek.sympis.service;


import com.marincek.sympis.domain.AuthorizationToken;

import java.util.Optional;

public interface TokenService {

    AuthorizationToken createToken(String username);

    AuthorizationToken refreshToken(String username, String oldToken);

    Optional<AuthorizationToken> findByUsername(String username);

    Optional<AuthorizationToken> findByToken(String token);

}
