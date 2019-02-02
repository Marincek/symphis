package com.marincek.sympis.service;

import com.marincek.sympis.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    String register(User user);

    User findByUsername(String username);

    String getFirstName(String username);
}
