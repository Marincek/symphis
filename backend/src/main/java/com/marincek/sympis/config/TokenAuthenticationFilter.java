package com.marincek.sympis.config;

import com.marincek.sympis.domain.AuthorizationToken;
import com.marincek.sympis.service.TokenService;
import com.marincek.sympis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String AUTH_TOKEN_HEADER_NAME = "x-auth-token";

    private UserService userDetailsService;
    private TokenService tokenService;

    @Autowired
    public TokenAuthenticationFilter(UserService userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            String authToken = getTokenFromHeader(servletRequest);

            validateTokenAndCreateAuthenticationContext(authToken);

            filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromHeader(ServletRequest servletRequest){
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        return httpServletRequest.getHeader(AUTH_TOKEN_HEADER_NAME);
    }

    private void validateTokenAndCreateAuthenticationContext(String authToken){
        if (StringUtils.hasText(authToken)) {
            Optional<AuthorizationToken> optionalToken = tokenService.findByToken(authToken);
            if (optionalToken.isPresent()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(optionalToken.get().getUsername());
                if (userDetails != null && optionalToken.get().getToken().equals(authToken)) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
        }
    }
}
