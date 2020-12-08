package com.timothybreitenfeldt.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequest;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponse;
import com.timothybreitenfeldt.blog.dto.RegisterRequest;
import com.timothybreitenfeldt.blog.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAuthor(@RequestBody RegisterRequest registerRequest) {
        this.authService.registerAuthor(registerRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        return this.authService.login(authenticationRequest)
    }

}
