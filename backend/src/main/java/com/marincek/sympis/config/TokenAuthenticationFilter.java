package com.marincek.sympis.config;

import com.marincek.sympis.domain.AuthorizationToken;
import com.marincek.sympis.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    private UserDetailsService customUserDetailsService;
    private TokenService tokenService;
    private final String AUTH_TOKEN_HEADER_NAME = "x-auth-token";

    @Autowired
    public TokenAuthenticationFilter(UserDetailsService userDetailsService, TokenService TokenService) {
        this.customUserDetailsService = userDetailsService;
        this.tokenService = TokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String authToken = httpServletRequest.getHeader(AUTH_TOKEN_HEADER_NAME);

            if (StringUtils.hasText(authToken)) {
                Optional<AuthorizationToken> optionalToken = tokenService.findByToken(authToken);
                if(optionalToken.isPresent()){
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(optionalToken.get().getUsername());
                    if(userDetails!= null && optionalToken.get().getToken().equals(authToken)){
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
