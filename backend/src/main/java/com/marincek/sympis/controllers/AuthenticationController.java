package com.marincek.sympis.controllers;

import com.marincek.sympis.controllers.request.AuthenticationRequest;
import com.marincek.sympis.controllers.request.UserRegistrationRequest;
import com.marincek.sympis.controllers.response.UserInfoResponse;
import com.marincek.sympis.service.TokenService;
import com.marincek.sympis.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationProvider authenticationManager;
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationProvider authenticationManager, TokenService tokenService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserInfoResponse> register(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        try {
            userService.register(userRegistrationRequest.toUser());
            return login(new AuthenticationRequest(userRegistrationRequest.getUsername(), userRegistrationRequest.getPassword()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserInfoResponse(e));
        }
    }

    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ResponseEntity<UserInfoResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String authorizationToken = tokenService.createToken(username).getToken();

            return new ResponseEntity<>(new UserInfoResponse(username, authorizationToken), HttpStatus.OK);

        } catch (BadCredentialsException bce) {
            return new ResponseEntity<>(new UserInfoResponse(bce), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(new UserInfoResponse("INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/logout", method = { RequestMethod.GET })
    public ResponseEntity logout() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @RequestMapping(value = "/refresh", method = { RequestMethod.GET })
    public ResponseEntity<UserInfoResponse> refreshToken(Principal principal, @RequestHeader("x-auth-token") String token) {
        String refreshedToken = tokenService.refreshToken(principal.getName(), token).getToken();
        return new ResponseEntity<>(new UserInfoResponse(principal.getName(), refreshedToken), HttpStatus.OK);
    }

}
