package com.marincek.sympis.service.impl;

import com.marincek.sympis.domain.User;
import com.marincek.sympis.domain.exceptions.UnknownUserException;
import com.marincek.sympis.repository.UserRepository;
import com.marincek.sympis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultUserService implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(User user) {
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return user.getUuid();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UnknownUserException::new);
    }

    @Override
    public String getFirstName(String username) {
        return userRepository.findFirstName(username).orElseThrow(UnknownUserException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles("USER");
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

        return builder.build();
    }

}
