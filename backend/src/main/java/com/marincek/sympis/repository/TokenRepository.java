package com.marincek.sympis.repository;

import com.marincek.sympis.domain.AuthorizationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<AuthorizationToken, Long> {

    //@Query("SELECT t FROM AuthorizationToken t WHERE t.username = :username")
    Optional<AuthorizationToken> findByUsername(@Param("username") String username);

    Optional<AuthorizationToken> findByToken(String token);

    @Query("SELECT t FROM AuthorizationToken t WHERE t.id = :id")
    Optional<AuthorizationToken> findById(@Param("id") String id);

    void deleteByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AuthorizationToken t SET t.token = :token, t.lastUsed = :lastUsed where t.username = :username")
    void updateToken(@Param("token") String token, @Param("lastUsed") Date lastUsed, @Param("username") String username);


}
