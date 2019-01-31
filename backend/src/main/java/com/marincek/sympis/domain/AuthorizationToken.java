package com.marincek.sympis.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AuthorizationToken {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date lastUsed;

    public AuthorizationToken(String username, String token, Date lastUsed) {
        this.username = username;
        this.token = token;
        this.lastUsed = lastUsed;
    }

    public AuthorizationToken() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}

