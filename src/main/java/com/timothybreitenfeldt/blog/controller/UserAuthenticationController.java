package com.timothybreitenfeldt.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequestDto;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponseDto;
import com.timothybreitenfeldt.blog.dto.RegisterRequestDto;
import com.timothybreitenfeldt.blog.service.UserAuthenticationService;

@RestController
@RequestMapping("/api")
public class UserAuthenticationController {

    @Autowired
    private UserAuthenticationService authorAuthenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAuthor(@Valid @RequestBody RegisterRequestDto registerRequest) {
        this.authorAuthenticationService.registerAuthor(registerRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authorLogin(@Valid @RequestBody AuthenticationRequestDto authenticationRequest) {
        return this.authorAuthenticationService.authorLogin(authenticationRequest);
    }

}
