package com.timothybreitenfeldt.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequest;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponse;
import com.timothybreitenfeldt.blog.dto.RegisterRequest;
import com.timothybreitenfeldt.blog.service.AuthorAuthenticationService;

@RestController
@RequestMapping("/api")
public class AuthorAuthenticationController {

    @Autowired
    private AuthorAuthenticationService authorAuthenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAuthor(@Valid @RequestBody RegisterRequest registerRequest) {
        this.authorAuthenticationService.registerAuthor(registerRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authorLogin(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return this.authorAuthenticationService.authorLogin(authenticationRequest);
    }

}